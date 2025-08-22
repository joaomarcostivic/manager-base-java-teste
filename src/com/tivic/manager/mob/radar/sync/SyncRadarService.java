package com.tivic.manager.mob.radar.sync;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import org.json.JSONObject;

import com.tivic.manager.fta.Cor;
import com.tivic.manager.fta.CorServices;
import com.tivic.manager.fta.EspecieVeiculo;
import com.tivic.manager.fta.EspecieVeiculoServices;
import com.tivic.manager.fta.MarcaModelo;
import com.tivic.manager.fta.MarcaModeloServices;
import com.tivic.manager.fta.TipoVeiculo;
import com.tivic.manager.fta.TipoVeiculoServices;
import com.tivic.manager.fta.Veiculo;
import com.tivic.manager.fta.VeiculoDAO;
import com.tivic.manager.fta.VeiculoDTO;
import com.tivic.manager.fta.VeiculoServices;
import com.tivic.manager.grl.Arquivo;
import com.tivic.manager.grl.ArquivoDAO;
import com.tivic.manager.grl.ArquivoServices;
import com.tivic.manager.grl.Equipamento;
import com.tivic.manager.grl.EquipamentoDAO;
import com.tivic.manager.grl.EquipamentoServices;
import com.tivic.manager.grl.ParametroServices;
import com.tivic.manager.grl.ArquivoBuilder.ArquivoBuilder;
import com.tivic.manager.mob.Ait;
import com.tivic.manager.mob.AitImagem;
import com.tivic.manager.mob.AitServices;
import com.tivic.manager.mob.EventoArquivo;
import com.tivic.manager.mob.EventoArquivoDAO;
import com.tivic.manager.mob.EventoArquivoServices;
import com.tivic.manager.mob.EventoEquipamento;
import com.tivic.manager.mob.EventoEquipamentoDAO;
import com.tivic.manager.mob.EventoEquipamentoServices;
import com.tivic.manager.mob.Infracao;
import com.tivic.manager.mob.InfracaoDAO;
import com.tivic.manager.mob.InfracaoServices;
import com.tivic.manager.mob.Orgao;
import com.tivic.manager.mob.OrgaoServices;
import com.tivic.manager.mob.TipoCnhEnum;
import com.tivic.manager.mob.TipoEvento;
import com.tivic.manager.mob.TipoEventoDAO;
import com.tivic.manager.mob.TipoEventoServices;
import com.tivic.manager.mob.ait.builders.AitBuilder;
import com.tivic.manager.mob.aitevento.IAitEventoService;
import com.tivic.manager.rest.request.filter.Criterios;
import com.tivic.manager.util.DetranUtils;
import com.tivic.manager.util.LogUtils;
import com.tivic.manager.util.ResultSetMapper;
import com.tivic.manager.util.Util;
import com.tivic.sol.cdi.BeansFactory;
import com.tivic.sol.connection.Conexao;
import com.tivic.sol.log.ManagerLog;
import com.tivic.sol.search.SearchCriterios;

import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;
import sol.util.Result;

public class SyncRadarService {

	private DadosAcessoBancoRadar dadosAcessoBancoRadar;
	private ManagerLog managerLog;
	private IAitEventoService aitEventoService;

	public SyncRadarService() throws Exception {
		dadosAcessoBancoRadar = new DadosAcessoBancoRadar();
		managerLog = (ManagerLog) BeansFactory.get(ManagerLog.class);
		this.aitEventoService = (IAitEventoService) BeansFactory.get(IAitEventoService.class);
	}
	
	public void sincronizar() {
		Connection connection = null;
		Connection connectionRadarServer = null;
		try {
			
			managerLog.info("SINCRONIZACAO", "Iniciado a sincronização de eventos do processamento para o orgão "
					+ dadosAcessoBancoRadar.getNmOrgaoAutuador());
			connection = createConnection();
			int quantidadeEventosAnteriores = getQuantidadeEventos(connection);
			connectionRadarServer = getConexaoRadarServer();
			ResultSetMap rsmEventosProcessados = getEventosProcessados(connectionRadarServer);
			managerLog.info("SINCRONIZACAO",
					"Sincronizando " + rsmEventosProcessados.size() + " evento(s) encontrados no servidor do radar.");
			adicionarEventos(rsmEventosProcessados, connectionRadarServer, connection);
			int quantidadeEventosPosteriores = getQuantidadeEventos(connection);
			managerLog.info("SINCRONIZACAO", (quantidadeEventosPosteriores - quantidadeEventosAnteriores)
					+ " evento(s) adicionados no cliente.");
			connection.commit();
			connectionRadarServer.commit();
		} catch (SQLException sqlE) {
			sqlE.printStackTrace();
			managerLog.error("ERRO DE BANCO DE DADOS", sqlE.getMessage());
			Conexao.rollback(connection);
		} catch (Exception e) {
			managerLog.showLog(e);
			managerLog.error("PARADA", e.getMessage());
			Conexao.rollback(connection);
		} finally {
			Conexao.desconectar(connection);
			Conexao.desconectar(connectionRadarServer);
		}

	}

	private Connection createConnection() throws SQLException {
		Connection connection = Conexao.conectar();
		connection.setAutoCommit(false);
		return connection;
	}

	private int getQuantidadeEventos(Connection connection) throws SQLException {
		ResultSetMap rsm = new ResultSetMap(
				connection.prepareStatement("SELECT COUNT(*) FROM mob_evento_equipamento").executeQuery());
		rsm.next();
		return rsm.getInt("count");
	}

	private Connection getConexaoRadarServer() throws Exception {
		if (!dadosAcessoBancoRadar.possuiDadosCompletos())
			throw new Exception(
					"Não foi possível sincronizar com o servidor do radar. Valores de configurações inexistentes.");
		Connection connection = Conexao.conectar(dadosAcessoBancoRadar.getDriver(), dadosAcessoBancoRadar.getUrl(),
				dadosAcessoBancoRadar.getLogin(), dadosAcessoBancoRadar.getSenha());

		connection.setAutoCommit(false);
		return connection;
	}

	private ResultSetMap getEventosProcessados(Connection connectionRadarServer) throws Exception, SQLException {
		PreparedStatement pstmtRS = connectionRadarServer
				.prepareStatement("SELECT * FROM mob_evento_equipamento A, mob_tipo_evento B "
						+ " WHERE A.cd_tipo_evento = B.cd_tipo_evento "
						+ " AND B.id_tipo_evento IN ('ASV', 'VAL', 'PSF', 'RLP', 'CEX', 'LNP', 'LHP', 'CLP') "
						+ " AND A.st_evento <> " + EventoEquipamentoServices.ST_EVENTO_NAO_PROCESSADO
						+ " AND A.nm_orgao_autuador = ? "
						+ " AND (A.lg_enviado = 0 OR A.lg_enviado IS NULL) ORDER BY dt_evento ASC  ");
		pstmtRS.setString(1, dadosAcessoBancoRadar.getNmOrgaoAutuador());
		ResultSetMap rsmEventoProcessados = new ResultSetMap(pstmtRS.executeQuery());
		if (rsmEventoProcessados.size() == 0)
			throw new Exception("Nao foi necessário sincronizar com o servidor do radar. Nenhum evento encontrado.");
		return rsmEventoProcessados;
	}

	private void adicionarEventos(ResultSetMap rsmEventosProcessados, Connection connectionRadarServer, Connection connection) throws Exception {
		rsmEventosProcessados.beforeFirst();
		while (rsmEventosProcessados.next()) {
			managerLog.error("PASSO 1", " Sincronizando evento:  " + rsmEventosProcessados.getInt("CD_EVENTO") );
			Equipamento equipamento = getEquipamento(rsmEventosProcessados, connection);
			TipoEvento tipoEvento = getTipoEvento(rsmEventosProcessados, connection);
			int cdVeiculo = getVeiculo(rsmEventosProcessados, connectionRadarServer, connection);
			managerLog.info("PASSO 5", "Testando duplicidade do evento");
			GregorianCalendar dtAfericaoMinutoMais = rsmEventosProcessados.getGregorianCalendar("dt_conclusao");
			dtAfericaoMinutoMais.add(Calendar.MINUTE, 1);
			GregorianCalendar dtAfericaoMinutoMenos = rsmEventosProcessados.getGregorianCalendar("dt_conclusao");
			dtAfericaoMinutoMenos.add(Calendar.MINUTE, -1);
			ResultSet rs2 = connection.prepareStatement(
					"SELECT * FROM mob_evento_equipamento "
				  + "	WHERE nr_placa = '"+rsmEventosProcessados.getString("NR_PLACA")+"'"
				  + "     AND cd_equipamento = " + equipamento.getCdEquipamento() 
				  + "     AND dt_conclusao <= '" + Util.formatDate(dtAfericaoMinutoMais, "yyyy-MM-dd HH:mm:ss") + "'"
				  + "     AND dt_conclusao >= '" + Util.formatDate(dtAfericaoMinutoMenos, "yyyy-MM-dd HH:mm:ss") + "'").executeQuery();

			if(rs2.next()) {
				LogUtils.debug("EventoEquipamentoServices.syncRadarServer: Evento possivelmente duplicado... "+rsmEventosProcessados.getString("CD_EVENTO"));
				continue;
			}
			
			EventoEquipamento evento = salvarEvento(rsmEventosProcessados, cdVeiculo, equipamento, tipoEvento, connection);
			adicionarArquivosEvento(evento, rsmEventosProcessados.getInt("CD_EVENTO"), connectionRadarServer, connection);
			atualizarEventoRadar(rsmEventosProcessados, connectionRadarServer);
		}
	}

	private Equipamento getEquipamento(ResultSetMap rsmEventosProcessados, Connection connectionRadarServer)
			throws Exception {
		managerLog.info("PASSO 2", "Buscando equipamento");
		Equipamento equipamento = EquipamentoServices
				.getByIdEquipamento(rsmEventosProcessados.getString("NM_EQUIPAMENTO"), connectionRadarServer);
		if (equipamento == null) {
			String msg = "Evento não contém um equipamento válido ou registrado no banco local:\n" + "Equip.\t"
					+ rsmEventosProcessados.getString("NM_EQUIPAMENTO") + "\n" + "Tp. Evt.\t"
					+ rsmEventosProcessados.getString("ID_TIPO_EVENTO") + "\n" + "Dt. Evt.\t"
					+ Util.convCalendarString(rsmEventosProcessados.getGregorianCalendar("DT_EVENTO"));
			throw new Exception(msg);
		}
		return equipamento;
	}

	private TipoEvento getTipoEvento(ResultSetMap rsmEventosProcessados, Connection connectionRadarServer)
			throws Exception {
		managerLog.info("PASSO 3", "Buscando tipo de evento");
		TipoEvento tipoEvento = TipoEventoServices.getByIdTipoEvento(rsmEventosProcessados.getString("ID_TIPO_EVENTO"),
				connectionRadarServer);
		if (tipoEvento == null) {
			String msg = "Evento não contém um tipo de evento válido ou registrado no banco local:\n" + "Equip.\t"
					+ rsmEventosProcessados.getString("NM_EQUIPAMENTO") + "\n" + "Tp. Evt.\t"
					+ rsmEventosProcessados.getString("ID_TIPO_EVENTO") + "\n" + "Dt. Evt.\t"
					+ Util.convCalendarString(rsmEventosProcessados.getGregorianCalendar("DT_EVENTO"));
			throw new Exception(msg);
		}
		return tipoEvento;
	}

	private int getVeiculo(ResultSetMap rsmEventosProcessados, Connection connectionRadarServer, Connection connection)
			throws SQLException {
		managerLog.info("PASSO 4", "Buscando veículo");
		int cdVeiculo = rsmEventosProcessados.getInt("CD_VEICULO");
		String nrPlaca = rsmEventosProcessados.getString("NR_PLACA");
		if (cdVeiculo == 0)
			return 0;
		Veiculo veiculoLocal = getVeiculoLocal(nrPlaca, connection);
		if (veiculoLocal != null)
			return veiculoLocal.getCdVeiculo();

		cdVeiculo = adicionarVeiculo(cdVeiculo, connectionRadarServer, connection);

		return cdVeiculo;
	}
	
	private Veiculo getVeiculoLocal(String nrPlaca, Connection connection) throws SQLException {
		PreparedStatement pstmtVeiculo = connection
				.prepareStatement("SELECT A.* from fta_veiculo A WHERE A.nr_placa = ?");
		pstmtVeiculo.setString(1, nrPlaca);
		ResultSetMap rsmVeiculo = new ResultSetMap(pstmtVeiculo.executeQuery());
		if (rsmVeiculo.next())
			return VeiculoDAO.get(rsmVeiculo.getInt("cd_veiculo"), connection);
		return null;
	}

	private int adicionarVeiculo(int cdVeiculo, Connection connectionRadarServer, Connection connection)
			throws SQLException {
		try {
			VeiculoDTO veiculoDtoRadarServer = getVeiculoDto(cdVeiculo, connectionRadarServer);
			if (veiculoDtoRadarServer == null)
				return 0;
			return salvarVeiculo(veiculoDtoRadarServer, connection);
		} catch (Exception e) {
			return 0;
		}
	}

	private int salvarVeiculo(VeiculoDTO veiculoDtoRadarServer, Connection connection) throws Exception {
		MarcaModelo marca = getMarca(veiculoDtoRadarServer.getModelo().getNmModelo(), connection);
		Cor corVeiculo = getCor(veiculoDtoRadarServer.getCor().getNmCor(), connection);
		TipoVeiculo tipoVeiculo = getTipoVeiculo(veiculoDtoRadarServer.getTipoVeiculo().getNmTipoVeiculo(), connection);
		EspecieVeiculo especieVeiculo = getEspecie(veiculoDtoRadarServer.getEspecieVeiculo().getDsEspecie(),
				connection);

		int cdVeiculo = veiculoDtoRadarServer.getCdVeiculo();
		verificarInformacoesVeiculo(cdVeiculo, marca, corVeiculo, tipoVeiculo, especieVeiculo);
		Veiculo veiculo = veiculoDtoRadarServer;
		veiculo.setCdVeiculo(0);
		veiculo.setCdMarca(marca.getCdMarca());
		veiculo.setCdCor(corVeiculo.getCdCor());
		veiculo.setNmCor(corVeiculo.getNmCor());
		veiculo.setCdTipoVeiculo(tipoVeiculo.getCdTipoVeiculo());
		veiculo.setCdEspecie(especieVeiculo.getCdEspecie());

		Result result = VeiculoServices.save(veiculo, null, connection);
		if (result.getCode() < 0)
			throw new Exception(
					"Erro ao salvar veiculo de código " + cdVeiculo + " do servidor: " + result.getMessage());
		veiculo = (Veiculo) result.getObjects().get("VEICULO");
		managerLog.info("VEICULO", "Veiculo adicionado: " + veiculo);
		return veiculo.getCdVeiculo();

	}

	private VeiculoDTO getVeiculoDto(int cdVeiculo, Connection connectionRadarServer) throws SQLException {
		PreparedStatement pstmtVeiculoRadarServer = connectionRadarServer
				.prepareStatement("SELECT *, N.nm_cor as nm_cor_veiculo " + " FROM fta_veiculo A "
						+ " JOIN bpm_referencia 			        A2 ON (A.cd_veiculo      = A2.cd_referencia) "
						+ " JOIN bpm_bem        			        A3 ON (A2.cd_bem         = A3.cd_bem) "
						+ " LEFT OUTER JOIN fta_marca_modelo      B  ON (A.cd_marca        = B.cd_marca) "
						+ " LEFT OUTER JOIN grl_cidade            G  ON (A.cd_cidade       = G.cd_cidade) "
						+ " LEFT OUTER JOIN fta_categoria_veiculo M  ON (A.cd_categoria    = M.cd_categoria) "
						+ " LEFT OUTER JOIN fta_cor               N  ON (A.cd_cor          = N.cd_cor) "
						+ " LEFT OUTER JOIN fta_tipo_veiculo      O  ON (A.cd_tipo_veiculo = O.cd_tipo_veiculo) "
						+ " LEFT OUTER JOIN fta_especie_veiculo   P  ON (A.cd_especie = P.cd_especie)"
						+ " WHERE A.cd_veiculo = ?");
		pstmtVeiculoRadarServer.setInt(1, cdVeiculo);

		ResultSetMap rsmVeiculoRS = new ResultSetMap(pstmtVeiculoRadarServer.executeQuery());
		return new VeiculoDTO.Builder(rsmVeiculoRS.getLines().get(0)).build();
	}

	private MarcaModelo getMarca(String nmModelo, Connection connection) {
		try {
			return MarcaModeloServices.getByNome(nmModelo, connection);
		} catch (Exception e) {
			return null;
		}
	}

	private Cor getCor(String nmCor, Connection connection) {
		try {
			return CorServices.getByNome(nmCor, connection);
		} catch (Exception e) {
			return null;
		}
	}

	private TipoVeiculo getTipoVeiculo(String nmTipoVeiculo, Connection connection) {
		try {
			return TipoVeiculoServices.getByNome(nmTipoVeiculo, connection);
		} catch (Exception e) {
			return null;
		}
	}

	private EspecieVeiculo getEspecie(String dsEspecie, Connection connection) {
		try {
			return EspecieVeiculoServices.getByNome(dsEspecie, connection);
		} catch (Exception e) {
			return null;
		}
	}

	private void verificarInformacoesVeiculo(int cdVeiculo, MarcaModelo marca, Cor corVeiculo, TipoVeiculo tipoVeiculo,
			EspecieVeiculo especieVeiculo) throws Exception {
		if (marca == null)
			throw new Exception("Faltando marca do veículo de código " + cdVeiculo + " do servidor");
		if (corVeiculo == null)
			throw new Exception("Faltando cor de veículo de código " + cdVeiculo + " do servidor");
		if (tipoVeiculo == null)
			throw new Exception("Faltando tipo de veículo de código " + cdVeiculo + " do servidor");
		if (especieVeiculo == null)
			throw new Exception("Faltando espécie de veículo de código " + cdVeiculo + " do servidor");
	}

	private EventoEquipamento salvarEvento(ResultSetMap rsmEventosProcessados, int cdVeiculo, Equipamento equipamento,
			TipoEvento tipoEvento, Connection connection) throws Exception {
		managerLog.info("PASSO 6", "Salvando evento");
		EventoEquipamento evento = new EventoEquipamento();
		evento.setCdEquipamento(equipamento.getCdEquipamento());
		evento.setCdTipoEvento(tipoEvento.getCdTipoEvento());
		evento.setDtEvento(rsmEventosProcessados.getGregorianCalendar("DT_EVENTO"));
		evento.setNmOrgaoAutuador(rsmEventosProcessados.getString("NM_ORGAO_AUTUADOR"));
		evento.setNmEquipamento(rsmEventosProcessados.getString("NM_EQUIPAMENTO"));
		evento.setDsLocal(rsmEventosProcessados.getString("DS_LOCAL"));
		evento.setIdIdentificacaoInmetro(rsmEventosProcessados.getString("ID_IDENTIFICACAO_INMETRO"));
		evento.setDtAfericao(rsmEventosProcessados.getGregorianCalendar("DT_AFERICAO"));
		evento.setNrPista(rsmEventosProcessados.getInt("NR_PISTA"));
		evento.setDtConclusao(rsmEventosProcessados.getGregorianCalendar("DT_CONCLUSAO"));
		evento.setVlLimite(rsmEventosProcessados.getInt("VL_LIMITE"));
		evento.setVlVelocidadeTolerada(rsmEventosProcessados.getInt("VL_VELOCIDADE_TOLERADA"));
		evento.setVlMedida(rsmEventosProcessados.getInt("VL_MEDIDA"));
		evento.setVlConsiderada(rsmEventosProcessados.getInt("VL_CONSIDERADA"));
		evento.setNrPlaca(rsmEventosProcessados.getString("NR_PLACA"));
		evento.setLgTempoReal(rsmEventosProcessados.getInt("LG_TEMPO_REAL"));
		evento.setTpVeiculo(rsmEventosProcessados.getInt("TP_VEICULO"));
		evento.setVlComprimentoVeiculo(rsmEventosProcessados.getInt("VL_COMPRIMENTO_VEICULO"));
		evento.setIdMedida(rsmEventosProcessados.getInt("ID_MEDIDA"));
		evento.setNrSerial(rsmEventosProcessados.getString("NR_SERIAL"));
		evento.setNmModeloEquipamento(rsmEventosProcessados.getString("NM_MODELO_EQUIPAMENTO"));
		evento.setNmRodovia(rsmEventosProcessados.getString("NM_RODOVIA"));
		evento.setNmUfRodovia(rsmEventosProcessados.getString("NM_UF_RODOVIA"));
		evento.setNmKmRodovia(rsmEventosProcessados.getString("NM_KM_RODOVIA"));
		evento.setNmMetrosRodovia(rsmEventosProcessados.getString("NM_METROS_RODOVIA"));
		evento.setNmSentidoRodovia(rsmEventosProcessados.getString("NM_SENTIDO_RODOVIA"));
		evento.setIdCidade(rsmEventosProcessados.getInt("ID_CIDADE"));
		evento.setNmMarcaEquipamento(rsmEventosProcessados.getString("NM_MARCA_EQUIPAMENTO"));
		evento.setTpEquipamento(rsmEventosProcessados.getInt("TP_EQUIPAMENTO"));
		evento.setNrPista(rsmEventosProcessados.getInt("NR_PISTA"));
		evento.setDtExecucaoJob(rsmEventosProcessados.getGregorianCalendar("DT_EXECUCAO_JOB"));
		evento.setIdUuid(rsmEventosProcessados.getString("ID_UUID"));
		evento.setTpRestricao(rsmEventosProcessados.getInt("TP_RESTRICAO"));
		evento.setTpClassificacao(rsmEventosProcessados.getInt("TP_CLASSIFICACAO"));
		evento.setVlPermanencia(rsmEventosProcessados.getInt("VL_PERMANENCIA"));
		evento.setVlSemaforoVermelho(rsmEventosProcessados.getInt("VL_SEMAFORO_VERMELHO"));
		evento.setStEvento(rsmEventosProcessados.getInt("ST_EVENTO"));
		evento.setCdMotivoCancelamento(rsmEventosProcessados.getInt("CD_MOTIVO_CANCELAMENTO"));
		evento.setTxtEvento(rsmEventosProcessados.getString("TXT_EVENTO"));
		evento.setLgOlpr(rsmEventosProcessados.getInt("LG_OPLR"));
		evento.setDtCancelamento(rsmEventosProcessados.getGregorianCalendar("DT_CANCELAMENTO"));
		evento.setDtProcessamento(rsmEventosProcessados.getGregorianCalendar("DT_PROCESSAMENTO"));
		evento.setCdVeiculo(cdVeiculo);
		evento.setLgEnviado(1);
		Result r = EventoEquipamentoServices.save(evento, null, connection);
		if (r.getCode() < 0)
			throw new Exception("Erro ao salvar evento do equipamento " + evento.getNmEquipamento() + ", na data "
					+ Util.formatDate(rsmEventosProcessados.getGregorianCalendar("DT_EVENTO"), "dd/MM/yyyy HH:mm")
					+ ": " + r.getMessage());
		EventoEquipamento eventoEquipamento = (EventoEquipamento) r.getObjects().get("EVENTOEQUIPAMENTO");
		managerLog.info("EVENTO", "Evento adicionado: " + eventoEquipamento);
		return eventoEquipamento;
	}

	private void adicionarArquivosEvento(EventoEquipamento evento, int cdEventoRadar, Connection connectionRadarServer,
			Connection connection) throws SQLException, Exception {
		managerLog.info("PASSO 7", "Buscando arquivos");
		ResultSetMap arquivosEvento = getArquivos(cdEventoRadar, connectionRadarServer, connection);

		while (arquivosEvento.next()) {
			Arquivo arquivo = saveArquivo(arquivosEvento, connection);
			saveEventoArquivo(arquivosEvento, evento, arquivo, connection);
		}
	}

	private ResultSetMap getArquivos(int cdEventoRadar, Connection connectionRadarServer, Connection connection)
			throws SQLException {
		PreparedStatement pstmtRadarServer = connectionRadarServer.prepareStatement(
				"SELECT * FROM mob_evento_arquivo A, grl_arquivo B " + " WHERE A.cd_arquivo = B.cd_arquivo "
						+ "   AND A.cd_evento = ? " + "   AND A.tp_arquivo = " + EventoArquivoServices.TP_ARQUIVO_FOTO);
		pstmtRadarServer.setInt(1, cdEventoRadar);
		return new ResultSetMap(pstmtRadarServer.executeQuery());
	}

	private Arquivo saveArquivo(ResultSetMap arquivosEvento, Connection connection) throws Exception {
		Arquivo arquivo = buildArquivo(arquivosEvento);
		Result result = ArquivoServices.save(arquivo, null, connection);
		if (result.getCode() <= 0)
			throw new Exception(
					"Erro ao salvar arquivo " + arquivosEvento.getString("NM_ARQUIVO") + ": " + result.getMessage());
		arquivo = (Arquivo) result.getObjects().get("ARQUIVO");
		managerLog.info("ARQUIVO", "Arquivo adicionado: " + arquivo);
		return arquivo;
	}
	
	private Arquivo buildArquivo(ResultSetMap arquivosEvento) {
		return new ArquivoBuilder()
				.setCdArquivo(0)
				.setNmArquivo(arquivosEvento.getString("NM_ARQUIVO"))
				.setBlbArquivo((byte[]) arquivosEvento.getObject("BLB_ARQUIVO"))
				.setDtArquivamento(Util.convTimestampToCalendar(arquivosEvento.getTimestamp("DT_ARQUIVAMENTO")))
				.setDtCriacao(Util.convTimestampToCalendar(arquivosEvento.getTimestamp("DT_CRIACAO")))
				.build();
	}

	private void saveEventoArquivo(ResultSetMap arquivosEvento, EventoEquipamento evento, Arquivo arquivo,
			Connection connection) throws Exception {
		EventoArquivo eventoArquivo = new EventoArquivo();
		eventoArquivo.setCdEvento(evento.getCdEvento());
		eventoArquivo.setCdArquivo(arquivo.getCdArquivo());
		eventoArquivo.setTpArquivo(arquivosEvento.getInt("TP_ARQUIVO"));
		eventoArquivo.setIdArquivo(arquivosEvento.getString("ID_ARQUIVO"));
		eventoArquivo.setTpEventoFoto(arquivosEvento.getInt("TP_EVENTO_FOTO"));
		eventoArquivo.setTpFoto(arquivosEvento.getInt("TP_FOTO"));
		eventoArquivo.setDtArquivo(Util.convTimestampToCalendar(arquivosEvento.getTimestamp("DT_ARQUIVO")));
		eventoArquivo.setLgImpressao(arquivosEvento.getInt("LG_IMPRESSAO"));
		Result result = EventoArquivoServices.save(eventoArquivo, null, connection);
		if (result.getCode() <= 0)
			throw new Exception("Erro ao salvar evento de arquivo " + arquivosEvento.getString("NM_ARQUIVO") + ": "
					+ result.getMessage());
	}

	private void atualizarEventoRadar(ResultSetMap rsmEventosProcessados, Connection connectionRadarServer)
			throws Exception {
		EventoEquipamento eventoEquipamento = EventoEquipamentoDAO.get(rsmEventosProcessados.getInt("cd_evento"),
				connectionRadarServer);
		eventoEquipamento.setLgEnviado(1);
		if (EventoEquipamentoDAO.update(eventoEquipamento, connectionRadarServer) < 0)
			throw new Exception("Erro ao atualizar evento de código " + eventoEquipamento.getCdEvento() + " no radar");
	}

	public void verificarEventos(GregorianCalendar dtVerificacao) {
		Connection connection = null;
		Connection connectionRadarServer = null;
		try {
			managerLog.info("VERIFICACAO",
					"Iniciado a verificação de eventos do processamento para o orgão "
							+ dadosAcessoBancoRadar.getNmOrgaoAutuador() + " no dia "
							+ Util.formatDate(dtVerificacao, "dd/MM/yyyy"));
			connection = createConnection();
			connectionRadarServer = getConexaoRadarServer();
			ResultSetMap rsmEventosVerificados = getEventosVerificados(dtVerificacao, connectionRadarServer);
			managerLog.info("VERIFICACAO", "Verificação de " + rsmEventosVerificados.size()
					+ " evento(s) encontrados no servidor do radar nessa data.");
			managerLog.info("VERIFICACAO", rsmEventosVerificados.size() + " evento(s) verificados");
		} catch (SQLException sqlE) {
			managerLog.error("ERRO DE BANCO DE DADOS", sqlE.getMessage());
			Conexao.rollback(connection);
		} catch (Exception e) {
			managerLog.error("PARADA", e.getMessage());
			Conexao.rollback(connection);
		} finally {
			Conexao.desconectar(connection);
			Conexao.desconectar(connectionRadarServer);
		}
	}

	private ResultSetMap getEventosVerificados(GregorianCalendar dtVerificacao, Connection connectionRadarServer)
			throws Exception, SQLException {
		PreparedStatement pstmtRS = connectionRadarServer
				.prepareStatement("SELECT * FROM mob_evento_equipamento A, mob_tipo_evento B "
						+ " WHERE A.cd_tipo_evento = B.cd_tipo_evento "
						+ " AND B.id_tipo_evento IN ('ASV', 'VAL', 'PSF', 'RLP', 'CEX', 'LNP', 'LHP', 'CLP') "
						+ " AND A.st_evento <> " + EventoEquipamentoServices.ST_EVENTO_NAO_PROCESSADO
						+ " AND A.nm_orgao_autuador = ? " + " AND A.dt_processamento > '"
						+ Util.formatDate(dtVerificacao, "yyyy-MM-dd 00:00:00") + "'" + " AND A.dt_processamento < '"
						+ Util.formatDate(dtVerificacao, "yyyy-MM-dd 23:59:59") + "' " + "  ORDER BY A.cd_evento");
		pstmtRS.setString(1, dadosAcessoBancoRadar.getNmOrgaoAutuador());
		ResultSetMap rsmEventoProcessados = new ResultSetMap(pstmtRS.executeQuery());
		if (rsmEventoProcessados.size() == 0)
			throw new Exception("Nao foi necessário sincronizar com o servidor do radar. Nenhum evento encontrado.");
		return rsmEventoProcessados;
	}

	public AitSyncDTO converterEventosAit(List<EventoEquipamento> eventos, int cdUsuario) throws Exception {
		AitSyncDTO aitSyncDTO = new AitSyncDTO();
		Orgao orgao = OrgaoServices.getOrgaoUnico();

		for (EventoEquipamento eventoEquipamento : eventos) {
			EventoEquipamento evento = EventoEquipamentoDAO.get(eventoEquipamento.getCdEvento());
			
			if (eventoHasAit(evento.getCdEvento())) {
				aitSyncDTO.addEventosNaoEmitidos(evento, "Esse evento já possui um AIT vinculado.");
				managerLog.info("Emissão de AITs", "O Evento " + evento.getCdEvento() + " já possui um AIT vinculado.");
				continue;
			}
			
			Veiculo veiculo = VeiculoServices.getVeiculoByPlaca(evento.getNrPlaca());
			Equipamento equipamento = EquipamentoDAO.get(evento.getCdEquipamento());

			Ait ait = buildAit(evento, orgao, equipamento, cdUsuario);

			if (veiculo == null) {
				DetranUtils detranUtils = new DetranUtils();
				JSONObject response = detranUtils.getVeiculo(ait.getNrPlaca()).asJSON();

				if (response == null || response.has("messageError")) {
					aitSyncDTO.addEventosNaoEmitidos(evento, "Não foi possível efetuar a busca de placa do veículo.");
					continue;
				}

				ResultSetMap marcaModelo = MarcaModeloServices.find(new Criterios("A.cd_marca",
						String.valueOf(response.get("codigoMarcaMod")), ItemComparator.EQUAL, Types.VARCHAR));

				if (!marcaModelo.next()) {
					aitSyncDTO.addEventosNaoEmitidos(evento,
							"Marca de veículo " + String.valueOf(response.get("marcaModelo")) + " não encontrada.");
					continue;
				}

				marcaModelo.goTo(0);

				EspecieVeiculo especieVeiculo = EspecieVeiculoServices
						.getByNome(String.valueOf(response.get("especie")));

				if (especieVeiculo == null) {
					aitSyncDTO.addEventosNaoEmitidos(evento,
							"Espécie de veículo " + String.valueOf(response.get("especie")) + " não encontrada.");
					continue;
				}

				Cor corVeiculo = CorServices.getByNome(String.valueOf(response.get("cor")));

				if (corVeiculo == null) {
					aitSyncDTO.addEventosNaoEmitidos(evento,
							"Cor de veículo " + String.valueOf(response.get("cor")) + " não encontrada.");
					continue;
				}

				ait.setCdMarcaAutuacao(marcaModelo.getInt("CD_MARCA"));
				ait.setCdMarcaVeiculo(marcaModelo.getInt("CD_MARCA"));
				ait.setCdEspecieVeiculo(especieVeiculo.getCdEspecie());
				ait.setCdCorVeiculo(corVeiculo.getCdCor());
			} else {
				ait.setCdMarcaAutuacao(veiculo.getCdMarca());
				ait.setCdMarcaVeiculo(veiculo.getCdMarca());
				ait.setCdEspecieVeiculo(veiculo.getCdEspecie());
				ait.setCdCorVeiculo(veiculo.getCdCor());
			}

			ArrayList<AitImagem> imagens = buscarImagens(evento);
			
		    boolean isAitImagensValid = imagens.stream().anyMatch(eventoArquivo -> eventoArquivo.getLgImpressao() > 0);

		    if (!isAitImagensValid) {
		    	aitSyncDTO.addEventosNaoEmitidos(evento, "Nenhuma das imagens do evento está elegível para impressão.");
		    	continue;
		    }

			Result result = AitServices.emitirAit(ait, evento, imagens, null, null, null);
			if (result.getCode() < 0)
				aitSyncDTO.addEventosNaoEmitidos(evento, result.getMessage());
			else
				aitSyncDTO.addAitEmitido(ait);

			if (!evento.getDtConclusao().equals(ait.getDtInfracao()))
				managerLog.info("Emissão de AITs", "O AIT " + ait.getCdAit() + " está com a data de infração diferente da data do evento " + evento.getCdEvento());
		}

		return aitSyncDTO;
	}
	
	private boolean eventoHasAit(int cdEvento) throws Exception {
		SearchCriterios searchCriterios = new SearchCriterios();
		searchCriterios.addCriteriosEqualInteger("cd_evento", cdEvento);
		
		return !this.aitEventoService.find(searchCriterios).isEmpty();
	}
	
	private Ait buildAit(EventoEquipamento evento, Orgao orgao, Equipamento equipamento, int cdUsuario) {
		return new AitBuilder()
			.nrPlaca(evento.getNrPlaca())
			.cdEquipamento(evento.getCdEquipamento())
			.dtInfracao(evento.getDtConclusao())
			.dsLocalInfracao(evento.getDsLocal())
			.cdAgente(orgao.getCdAgenteResponsavel())
			.stAit(AitServices.ST_CONFIRMADO)
			.cdUsuario(cdUsuario)
			.vlLatitude(equipamento.getVlLatitude())
			.vlLongitude(equipamento.getVlLongitude())
			.vlVelocidadeAferida(calcularVelocidadeMedida(evento))
			.vlVelocidadePenalidade(Double.parseDouble(String.valueOf(evento.getVlConsiderada())))
			.vlVelocidadePermitida(Double.parseDouble(String.valueOf(evento.getVlLimite())))
			.cdInfracao(getInfracao(evento))
			.cdCidade(orgao.getCdCidade())
			.dtAfericao(equipamento.getDtAfericao())
			.nrLacre(equipamento.getNrLacre())
			.nrInventarioInmetro(equipamento.getNrInventarioInmetro())
			.dsPontoReferencia("PISTA" + evento.getNrPista())
			.tpCnhCondutor(TipoCnhEnum.NAO_INFORMADO.getKey())
			.build();
	}

	private double calcularVelocidadeMedida(EventoEquipamento evento) {
		if (evento.getVlMedida() <= 100 && (evento.getVlMedida() - evento.getVlConsiderada()) != 7) {
			return evento.getVlConsiderada() + 7;
		}
		return evento.getVlMedida();
	}

	private int getInfracao(EventoEquipamento evento) {
		double prFaixa = evento.getVlLimite() - evento.getVlConsiderada();
		int nrCodDetran = getNumeroInfracao(prFaixa, evento);
		return InfracaoServices.getCodInfracao(nrCodDetran);
	}

	private int getNumeroInfracao(double prFaixa, EventoEquipamento evento) {
		int codInfracao = 0;
		if (prFaixa <= 20)
			codInfracao = ParametroServices.getValorOfParametroAsInteger("MOB_NR_INFRACAO_VELOCIDADE_20",
					ParametroServices.getValorOfParametroAsInteger("NR_INFRACAO_VELOCIDADE_20", 0), 0);
		else if (prFaixa > 20 && prFaixa <= 50)
			codInfracao = ParametroServices.getValorOfParametroAsInteger("MOB_NR_INFRACAO_VELOCIDADE_20_50",
					ParametroServices.getValorOfParametroAsInteger("NR_INFRACAO_VELOCIDADE_20_50", 0), 0);
		else if (prFaixa > 50)
			codInfracao = ParametroServices.getValorOfParametroAsInteger("MOB_NR_INFRACAO_VELOCIDADE_50",
					ParametroServices.getValorOfParametroAsInteger("NR_INFRACAO_VELOCIDADE_50", 0), 0);

		if (codInfracao > 0)
			return codInfracao;

		TipoEvento tipoEvento = TipoEventoDAO.get(evento.getCdTipoEvento());
		Infracao infracao = InfracaoDAO.get(tipoEvento.getCdInfracao());
		return infracao.getNrCodDetran();

	}

	private ArrayList<AitImagem> buscarImagens(EventoEquipamento evento) {
		ArrayList<AitImagem> imagens = new ArrayList<AitImagem>();
		ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
		criterios.add(new ItemComparator("cd_evento", String.valueOf(evento.getCdEvento()), ItemComparator.EQUAL, Types.INTEGER));
		ResultSetMap rsm = EventoArquivoServices.find(criterios);
		while (rsm.next()) {
			EventoArquivo eventoArquivo = EventoArquivoDAO.get(rsm.getInt("cd_evento"), rsm.getInt("cd_arquivo"));
			Arquivo arquivo = ArquivoDAO.get(eventoArquivo.getCdArquivo());

			AitImagem imagem = new AitImagem();
			imagem.setBlbImagem(arquivo.getBlbArquivo());
			imagem.setLgImpressao(eventoArquivo.getLgImpressao());
			imagens.add(imagem);
		}

		return imagens;
	}

	public List<EventoEquipamento> getEventosNaoEmitidos() throws Exception {
		Connection connection = null;
		try {
			connection = Conexao.conectar();
			ResultSetMap rsm = new ResultSetMap(connection.prepareStatement("SELECT * FROM mob_evento_equipamento A "
					+ " WHERE st_evento = " + EventoEquipamentoServices.ST_EVENTO_CONFIRMADO
					+ "  AND NOT EXISTS (SELECT * FROM mob_ait_evento B WHERE A.cd_evento = B.cd_evento)"
					+ "  AND cd_evento IN ( SELECT cd_evento FROM mob_evento_arquivo sA, grl_arquivo sB WHERE sA.cd_arquivo = sB.cd_arquivo )"
					+ " ORDER BY dt_evento ASC").executeQuery());
			return (new ResultSetMapper<EventoEquipamento>(rsm, EventoEquipamento.class)).toList();
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("Erro ao buscar os eventos");
		} finally {
			Conexao.desconectar(connection);
		}
	}

}