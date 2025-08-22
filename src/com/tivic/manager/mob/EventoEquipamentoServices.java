package com.tivic.manager.mob;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import com.dataprom.radar.ws.infracao.ItemTransmitirFotoInfracao;
import com.dataprom.radar.ws.infracao.ItemTransmitirPerfilLacoInfracao;
import com.dataprom.radar.ws.infracao.ItemTransmitirVideoInfracao;
import com.dataprom.radar.ws.infracao.TransmitirInfracaoASVV1Request.ItemTransmitirInfracaoASVV1;
import com.dataprom.radar.ws.infracao.TransmitirInfracaoESTV1Request.ItemTransmitirInfracaoESTV1;
import com.dataprom.radar.ws.infracao.TransmitirInfracaoIMTV1Request.ItemTransmitirInfracaoIMTV1;
import com.dataprom.radar.ws.infracao.TransmitirInfracaoPSFV1Request.ItemTransmitirInfracaoPSFV1;
import com.dataprom.radar.ws.infracao.TransmitirInfracaoVALV1Request.ItemTransmitirInfracaoVALV1;
import com.tivic.manager.conf.ManagerConf;
import com.tivic.manager.fta.CategoriaVeiculo;
import com.tivic.manager.fta.CategoriaVeiculoServices;
import com.tivic.manager.fta.Cor;
import com.tivic.manager.fta.CorServices;
import com.tivic.manager.fta.EspecieVeiculo;
import com.tivic.manager.fta.EspecieVeiculoServices;
import com.tivic.manager.fta.MarcaModelo;
import com.tivic.manager.fta.MarcaModeloServices;
import com.tivic.manager.fta.TipoVeiculo;
import com.tivic.manager.fta.TipoVeiculoServices;
import com.tivic.manager.fta.Veiculo;
import com.tivic.manager.fta.VeiculoDTO;
import com.tivic.manager.fta.VeiculoServices;
import com.tivic.manager.grl.Arquivo;
import com.tivic.manager.grl.ArquivoServices;
import com.tivic.manager.grl.ParametroServices;
import com.tivic.manager.grl.equipamento.Equipamento;
import com.tivic.manager.grl.equipamento.EquipamentoServices;
import com.tivic.manager.grl.equipamento.repository.EquipamentoDAO;
import com.tivic.manager.mob.alpr.AlprFactory;
import com.tivic.manager.mob.alpr.AlprResult;
import com.tivic.manager.mob.alpr.AlprService;
import com.tivic.manager.mob.radar.crypto.LocalDecrypt;
import com.tivic.manager.seg.AuthData;
import com.tivic.manager.util.LogUtils;
import com.tivic.manager.util.Util;
import com.tivic.sol.connection.Conexao;

import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;
import sol.dao.Search;
import sol.util.Result;

public class EventoEquipamentoServices {

	public static final int ST_EVENTO_NAO_PROCESSADO = 0;
	public static final int ST_EVENTO_CONFIRMADO = 1;
	public static final int ST_EVENTO_CANCELADO = 2;
	public static final int ST_EVENTO_PENDENTE_CONFIRMACAO = 3;
	public static final int ST_EVENTO_FINALIZADO = 4;

	public static Result save(EventoEquipamento eventoEquipamento) {
		return save(eventoEquipamento, null, null);
	}

	public static Result save(EventoEquipamento eventoEquipamento, AuthData authData) {
		return save(eventoEquipamento, authData, null);
	}

	public static Result save(EventoEquipamento eventoEquipamento, AuthData authData, Connection connect) {
		boolean isConnectionNull = connect == null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			if (eventoEquipamento == null)
				return new Result(-1, "Erro ao salvar. EventoEquipamento ï¿½ nulo");

			int retorno;
			if (eventoEquipamento.getCdEvento() == 0) {

				if (!Util.isStrBaseAntiga()) {
					int lgEnderecoEquipamentoServidor = ParametroServices
							.getValorOfParametroAsInteger("MOB_LG_LOCAL_EQUIPAMENTO_SERVIDOR", 0, 0, connect);

					if (lgEnderecoEquipamentoServidor == 1) {
						Equipamento equipamento = EquipamentoServices
								.getByIdEquipamento(eventoEquipamento.getNmEquipamento(), connect);
						if (equipamento != null)
							eventoEquipamento.setDsLocal(equipamento.getDsLocal());
					}
				}

				retorno = EventoEquipamentoDAO.insert(eventoEquipamento, connect);
				eventoEquipamento.setCdEvento(retorno);
			} else {
				retorno = EventoEquipamentoDAO.update(eventoEquipamento, connect);
			}

			if (retorno <= 0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();

			return new Result(retorno, (retorno <= 0) ? "Erro ao salvar..." : "Salvo com sucesso...",
					"EVENTOEQUIPAMENTO", eventoEquipamento);
		} catch (Exception e) {
			e.printStackTrace();
			if (isConnectionNull)
				Conexao.rollback(connect);
			return new Result(-1, e.getMessage());
		} finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static Result remove(EventoEquipamento eventoEquipamento) {
		return remove(eventoEquipamento.getCdEvento());
	}

	public static Result remove(int cdEvento) {
		return remove(cdEvento, false, null, null);
	}

	public static Result remove(int cdEvento, boolean cascade) {
		return remove(cdEvento, cascade, null, null);
	}

	public static Result remove(int cdEvento, boolean cascade, AuthData authData) {
		return remove(cdEvento, cascade, authData, null);
	}

	public static Result remove(int cdEvento, boolean cascade, AuthData authData, Connection connect) {
		boolean isConnectionNull = connect == null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			int retorno = 0;
			if (cascade) {
				/** SE HOUVER, REMOVER TABELAS ASSOCIADAS **/
				retorno = 1;
			}
			if (!cascade || retorno > 0)
				retorno = EventoEquipamentoDAO.delete(cdEvento, connect);
			if (retorno <= 0) {
				Conexao.rollback(connect);
				return new Result(-2, "Este registro estï¿½ vinculado a outros e não pode ser excluï¿½do!");
			} else if (isConnectionNull)
				connect.commit();
			return new Result(1, "Registro excluï¿½do com sucesso!");
		} catch (Exception e) {
			e.printStackTrace();
			if (isConnectionNull)
				Conexao.rollback(connect);
			return new Result(-1, "Erro ao excluir registro!");
		} finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ResultSetMap getAll() {
		return getAll(null);
	}

	public static ResultSetMap getAll(Connection connect) {
		boolean isConnectionNull = connect == null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM mob_evento_equipamento");
			return new ResultSetMap(pstmt.executeQuery());
		} catch (SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! EventoEquipamentoServices.getAll: " + sqlExpt);
			return null;
		} catch (Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! EventoEquipamentoServices.getAll: " + e);
			return null;
		} finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ResultSetMap find(ArrayList<ItemComparator> criterios) {
		return find(criterios, null);
	}

	public static ResultSetMap find(ArrayList<ItemComparator> criterios, Connection connect) {
	    boolean isConnectionNull = connect == null;
	    try {
	        if (isConnectionNull) {
	            connect = Conexao.conectar();
	            connect.setAutoCommit(false);
	        }

	        Map<String, Object> infoCriterios = processarCriterios(criterios);

	        String baseQuery = executarConsulta((String) infoCriterios.get("tpEquipamentoFilter"), (String[]) infoCriterios.get("sqlLimit"), (ArrayList<ItemComparator>) infoCriterios.get("crt"), connect);
	        ResultSetMap rsm = Search.findAndLog(baseQuery, ordenaConsulta((String[]) infoCriterios.get("sqlLimit")), (ArrayList<ItemComparator>) infoCriterios.get("crt"), connect, isConnectionNull);

	        if ((int) infoCriterios.get("qtDeslocamento") != -1) {
	            String countQuery = construirConsultaContagem((String) infoCriterios.get("tpEquipamentoFilter"));
	            ResultSetMap rsmTotal = Search.find(countQuery, "", (ArrayList<ItemComparator>) infoCriterios.get("crt"), connect, isConnectionNull);

	            if (rsmTotal.next()) {
	                rsm.setTotal(rsmTotal.getInt("COUNT"));
	            }
	        }
	        return rsm;

	    } catch (Exception e) {
	        e.printStackTrace(System.out);
	        System.err.println("Erro! EventoEquipamentoServices.find: " + e);
	        return null;
	    } finally {
	        if (isConnectionNull) {
	            Conexao.desconectar(connect);
	        }
	    }
	}

	private static Map<String, Object> processarCriterios(ArrayList<ItemComparator> criterios) {
	    ArrayList<ItemComparator> crt = new ArrayList<>();
	    int qtLimite = 0;
	    int qtDeslocamento = -1;
	    String orderBy = "";
	    String tpEquipamentoFilter = null;

	    for (ItemComparator criterio : criterios) {
	        String coluna = criterio.getColumn();
	        String valor = criterio.getValue();

	        switch (coluna.toLowerCase()) {
	            case "qtlimite":
	                qtLimite = Integer.parseInt(valor);
	                break;
	            case "qtdeslocamento":
	                qtDeslocamento = Integer.parseInt(valor);
	                break;
	            case "orderby":
	                orderBy = " ORDER BY " + valor.trim();
	                break;
	            case "tp_equipamento":
	                tpEquipamentoFilter = valor.trim();
	                break;
	            default:
	                crt.add(criterio);
	        }
	    }
	    String[] sqlLimit = com.tivic.manager.util.Util.getLimitAndSkip(qtLimite, qtDeslocamento);

	    Map<String, Object> infoCriterios = new HashMap<>();
	    infoCriterios.put("crt", crt);
	    infoCriterios.put("qtLimite", qtLimite);
	    infoCriterios.put("qtDeslocamento", qtDeslocamento);
	    infoCriterios.put("orderBy", orderBy);
	    infoCriterios.put("tpEquipamentoFilter", tpEquipamentoFilter);
	    infoCriterios.put("sqlLimit", sqlLimit);

	    return infoCriterios;
	}

	private static String executarConsulta(String tpEquipamentoFilter, String[] sqlLimit, ArrayList<ItemComparator> crt, Connection connect) throws SQLException {
	    String baseQuery = "SELECT " + sqlLimit[0] +
	            " A.*, B.nm_tipo_evento, B.id_tipo_evento, C.*, C.nm_equipamento AS nm_equipamento_2, C.tp_equipamento as tp_equipamento_2, " +
	            "E.*, E.cd_infracao AS cod_infracao, F.*, G.cd_ait " +
	            "FROM mob_evento_equipamento A " +
	            "LEFT OUTER JOIN mob_tipo_evento B ON (A.cd_tipo_evento = B.cd_tipo_evento) " +
	            "LEFT OUTER JOIN grl_equipamento C ON (A.cd_equipamento = C.cd_equipamento) " +
	            "LEFT OUTER JOIN mob_infracao E ON (B.cd_infracao = E.cd_infracao) " +
	            "LEFT OUTER JOIN mob_evento_motivo_cancelamento F ON (A.cd_motivo_cancelamento = F.cd_motivo_cancelamento) " +
	            "LEFT OUTER JOIN mob_ait_evento G ON (G.cd_evento = A.cd_evento) ";

	    if (tpEquipamentoFilter != null && !tpEquipamentoFilter.isEmpty()) {
	        baseQuery += " WHERE C.tp_equipamento = ?";
	    }

	    return baseQuery;
	}
	
	private static String ordenaConsulta(String[] sqlLimit) {
		return "ORDER BY A.dt_conclusao, A.dt_evento ASC " + sqlLimit[1];
	}

	private static String construirConsultaContagem(String tpEquipamentoFilter) {
	    String consultaContagem = "SELECT COUNT(DISTINCT A.cd_evento) " +
	            "FROM mob_evento_equipamento A " +
	            "LEFT OUTER JOIN mob_tipo_evento B ON (A.cd_tipo_evento = B.cd_tipo_evento) " +
	            "LEFT OUTER JOIN grl_equipamento C ON (A.cd_equipamento = C.cd_equipamento) " +
	            "LEFT OUTER JOIN mob_infracao E ON (B.cd_infracao = E.cd_infracao) " +
	            "LEFT OUTER JOIN mob_evento_motivo_cancelamento F ON (A.cd_motivo_cancelamento = F.cd_motivo_cancelamento) " +
	            "LEFT OUTER JOIN mob_ait_evento G ON (G.cd_evento = A.cd_evento)";

	    if (tpEquipamentoFilter != null && !tpEquipamentoFilter.isEmpty()) {
	        consultaContagem += " WHERE C.tp_equipamento = ?";
	    }
	    return consultaContagem;
	}



	/**
	 * IMT - IMAGEM TESTE
	 * 
	 * @param list
	 * @return
	 */
	public static Result saveIMT(List<ItemTransmitirInfracaoIMTV1> list) {
		return saveIMT(list, null);
	}

	public static Result saveIMT(List<ItemTransmitirInfracaoIMTV1> list, Connection connect) {

		boolean isConnectionNull = connect == null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			Result r = new Result(-1);

			for (ItemTransmitirInfracaoIMTV1 item : list) {


				// 1. BUSCAR EQUIPAMENTO
				Equipamento equipamento = EquipamentoServices.getByIdEquipamento(item.getEquipamento(), connect);

				// TODO: FIX: somente enquanto não alterar a tabela de equipamento
				// boolean decrypt = !equipamento.getIdEquipamento().equals("GOR00140022");
				boolean decrypt = equipamento.getLgCriptografia() == 1;

				// 2. GRAVAR EVENTO
				EventoEquipamento evento = new EventoEquipamento();

				if (equipamento != null)
					evento.setCdEquipamento(equipamento.getCdEquipamento());

				evento.setNmEquipamento(item.getEquipamento());
				evento.setIdIdentificacaoInmetro(item.getIdentificacaoInmetro());
				evento.setNmKmRodovia(item.getKmRodovia());
				evento.setDsLocal(item.getLocal());
				evento.setNmMarcaEquipamento(item.getMarcaEquipamento());
				evento.setNmMetrosRodovia(item.getMetrosRodovia());
				evento.setNmModeloEquipamento(item.getModeloEquipamento());
				evento.setNrSerial(item.getNumeroSerial());
				evento.setNmOrgaoAutuador(item.getOrgaoAutuador());
				evento.setNrPlaca(item.getPlaca());
				evento.setNmRodovia(item.getRodovia());
				evento.setNmSentidoRodovia(item.getSentidoRodovia());
				evento.setTpEquipamento(item.getTipoEquipamento());
				evento.setTpVeiculo(item.getTipoVeiculo());
				evento.setNmUfRodovia(item.getUfRodovia());
				evento.setIdCidade(item.getCodCidade());
				evento.setVlComprimentoVeiculo(item.getComprimentoVeiculo());
				evento.setIdMedida(item.getIdMedida());
				evento.setNrPista(item.getPista());
				evento.setVlConsiderada(item.getVelocidadeConsiderada());
				evento.setVlLimite(item.getVelocidadeLimite());
				evento.setVlMedida(item.getVelocidadeMedida());
				evento.setVlVelocidadeTolerada(item.getVelocidadeTolerada());
				evento.setDtAfericao(Util.convDateToCalendar(item.getDataAfericao()));
				evento.setDtConclusao(Util.convDateToCalendar(item.getDataConclusao()));
				evento.setDtExecucaoJob(Util.convDateToCalendar(item.getDataExecucaoJob()));

				evento.setDtEvento(new GregorianCalendar());
				// PARAMETRIZAR O TIPO DE EVENTO IMT
				TipoEvento tipoEvento = TipoEventoServices.getByIdTipoEvento("IMT", connect);
				if (tipoEvento != null)
					evento.setCdTipoEvento(tipoEvento.getCdTipoEvento());

				List<ItemTransmitirFotoInfracao> fotos = item.getFotos();
				List<ItemTransmitirPerfilLacoInfracao> perfis = item.getPerfis();
				List<ItemTransmitirVideoInfracao> videos = item.getVideos();

				r = save(evento, null, connect);

				if (r.getCode() > 0) {

					evento.setCdEvento(((EventoEquipamento) r.getObjects().get("EVENTOEQUIPAMENTO")).getCdEvento());

					if (r.getCode() > 0 && fotos.size() > 0) {
						// 2. GRAVAR IMAGENS ANEXADAS
						r = saveFotosEvento(evento, fotos, decrypt, connect);
					}

					if (r.getCode() > 0 && videos.size() > 0) {
						// 3. GRAVAR VIDEOS ANEXADOS
						r = saveVideosEvento(evento, videos, connect);
					}

					if (r.getCode() > 0 && perfis.size() > 0) {
						// 4. GRAVAR PERFIS ANEXADOS
						r = savePerfisEvento(evento, perfis, connect);
					}
				}

			}

			if (r.getCode() <= 0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();

			return new Result(r.getCode(), r.getCode() <= 0 ? "Erro ao gravar eventos de equipamento."
					: "Eventos de equipamento gravados com sucesso.");
		} catch (Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! EventoEquipamentoServices.saveIMT: " + e);
			return new Result(-1, "Erro ao gravar eventos de equipamento.");
		} finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	/**
	 * VAL - VELOCIDADE ACIMA DO LIMITE
	 * 
	 * @param list
	 * @return
	 */
	public static Result saveVAL(List<ItemTransmitirInfracaoVALV1> list) {
		return saveVAL(list, null);
	}

	public static Result saveVAL(List<ItemTransmitirInfracaoVALV1> list, Connection connect) {

		boolean isConnectionNull = connect == null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			Result r = new Result(-1);

			for (ItemTransmitirInfracaoVALV1 item : list) {


				// 1. BUSCAR EQUIPAMENTO
				Equipamento equipamento = EquipamentoServices.getByIdEquipamento(item.getEquipamento(), connect);

				// TODO: FIX: somente enquanto não alterar a tabela de equipamento
				// boolean decrypt = !equipamento.getIdEquipamento().equals("GOR00140022");
				boolean decrypt = equipamento.getLgCriptografia() == 1;

				// 2. GRAVAR EVENTO
				EventoEquipamento evento = new EventoEquipamento();

				if (equipamento != null)
					evento.setCdEquipamento(equipamento.getCdEquipamento());

				evento.setNmEquipamento(item.getEquipamento());
				evento.setTpClassificacao(item.getClassificacao());
				evento.setIdIdentificacaoInmetro(item.getIdentificacaoInmetro());
				evento.setNmKmRodovia(item.getKmRodovia());
				evento.setDsLocal(item.getLocal());
				evento.setNmMarcaEquipamento(item.getMarcaEquipamento());
				evento.setNmMetrosRodovia(item.getMetrosRodovia());
				evento.setNmModeloEquipamento(item.getModeloEquipamento());
				evento.setNrSerial(item.getNumeroSerial());
				evento.setNmOrgaoAutuador(item.getOrgaoAutuador());
				evento.setNrPlaca(item.getPlaca());
				evento.setNmRodovia(item.getRodovia());
				evento.setNmSentidoRodovia(item.getSentidoRodovia());
				evento.setTpEquipamento(item.getTipoEquipamento());
				evento.setTpVeiculo(item.getTipoVeiculo());
				evento.setNmUfRodovia(item.getUfRodovia());
				evento.setIdCidade(item.getCodCidade());
				evento.setVlComprimentoVeiculo(item.getComprimentoVeiculo());
				evento.setIdMedida(item.getIdMedida());
				evento.setNrPista(item.getPista());
				evento.setVlConsiderada(item.getVelocidadeConsiderada());
				evento.setVlLimite(item.getVelocidadeLimite());
				evento.setVlMedida(item.getVelocidadeMedida());
				evento.setVlVelocidadeTolerada(item.getVelocidadeTolerada());
				evento.setDtAfericao(Util.convDateToCalendar(item.getDataAfericao()));
				evento.setDtConclusao(Util.convDateToCalendar(item.getDataConclusao()));
				evento.setLgTempoReal(item.isTempoReal() ? 1 : 0);

				evento.setDtEvento(new GregorianCalendar());

				// PARAMETRIZAR O TIPO DE EVENTO VAL
				TipoEvento tipoEvento = TipoEventoServices.getByIdTipoEvento("VAL", connect);
				if (tipoEvento != null)
					evento.setCdTipoEvento(tipoEvento.getCdTipoEvento());

				List<ItemTransmitirFotoInfracao> fotos = item.getFotos();
				List<ItemTransmitirPerfilLacoInfracao> perfis = item.getPerfis();
				List<ItemTransmitirVideoInfracao> videos = item.getVideos();

				r = save(evento, null, connect);

				if (r.getCode() > 0) {

					evento.setCdEvento(((EventoEquipamento) r.getObjects().get("EVENTOEQUIPAMENTO")).getCdEvento());

					if (r.getCode() > 0 && fotos.size() > 0) {
						// 2. GRAVAR IMAGENS ANEXADAS
						r = saveFotosEvento(evento, fotos, decrypt, connect);
					}

					if (r.getCode() > 0 && videos.size() > 0) {
						// 3. GRAVAR VIDEOS ANEXADOS
						r = saveVideosEvento(evento, videos, connect);
					}

					if (r.getCode() > 0 && perfis.size() > 0) {
						// 4. GRAVAR PERFIS ANEXADOS
						r = savePerfisEvento(evento, perfis, connect);
					}
				}
			}

			if (r.getCode() <= 0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();

			return new Result(r.getCode(), r.getCode() <= 0 ? "Erro ao gravar eventos de equipamento."
					: "Eventos de equipamento gravados com sucesso.");
		} catch (Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! EventoEquipamentoServices.saveVAL: " + e);
			return new Result(-1, "Erro ao gravar eventos de equipamento.");
		} finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	/**
	 * ASV - AVANCO SINAL VERMELHO
	 * 
	 * @param list
	 * @return
	 */
	public static Result saveASV(List<ItemTransmitirInfracaoASVV1> list) {
		return saveASV(list, null);
	}

	public static Result saveASV(List<ItemTransmitirInfracaoASVV1> list, Connection connect) {

		boolean isConnectionNull = connect == null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			Result r = new Result(-1);

			for (ItemTransmitirInfracaoASVV1 item : list) {

				// 1. BUSCAR EQUIPAMENTO
				Equipamento equipamento = EquipamentoServices.getByIdEquipamento(item.getEquipamento(), connect);

				// TODO: FIX: somente enquanto não alterar a tabela de equipamento
				// boolean decrypt = !equipamento.getIdEquipamento().equals("GOR00140022");
				boolean decrypt = equipamento.getLgCriptografia() == 1;

				// 2. GRAVAR EVENTO
				EventoEquipamento evento = new EventoEquipamento();

				if (equipamento != null)
					evento.setCdEquipamento(equipamento.getCdEquipamento());

				evento.setNmEquipamento(item.getEquipamento());
				// evento.setTpClassificacao(item.getClassificacao());
				evento.setIdIdentificacaoInmetro(item.getIdentificacaoInmetro());
				evento.setNmKmRodovia(item.getKmRodovia());
				evento.setDsLocal(item.getLocal());
				evento.setNmMarcaEquipamento(item.getMarcaEquipamento());
				evento.setNmMetrosRodovia(item.getMetrosRodovia());
				evento.setNmModeloEquipamento(item.getModeloEquipamento());
				evento.setNrSerial(item.getNumeroSerial());
				evento.setNmOrgaoAutuador(item.getOrgaoAutuador());
				evento.setNrPlaca(item.getPlaca());
				evento.setNmRodovia(item.getRodovia());
				evento.setNmSentidoRodovia(item.getSentidoRodovia());
				evento.setTpEquipamento(item.getTipoEquipamento());
				evento.setTpVeiculo(item.getTipoVeiculo());
				evento.setNmUfRodovia(item.getUfRodovia());
				evento.setIdCidade(item.getCodCidade());
				evento.setVlComprimentoVeiculo(item.getComprimentoVeiculo());
				evento.setIdMedida(item.getIdMedida());
				evento.setNrPista(item.getPista());
				// evento.setVlConsiderada(item.getVelocidadeConsiderada());
				evento.setVlLimite(item.getTempoTolerancia());
				evento.setVlMedida(item.getTempoMedido());
				// evento.setVlVelocidadeTolerada(item.getVelocidadeTolerada());
				evento.setDtAfericao(Util.convDateToCalendar(item.getDataAfericao()));
				evento.setDtConclusao(Util.convDateToCalendar(item.getDataConclusao()));
				evento.setLgTempoReal(item.isTempoReal() ? 1 : 0);

				evento.setDtEvento(new GregorianCalendar());

				// PARAMETRIZAR O TIPO DE EVENTO VAL
				TipoEvento tipoEvento = TipoEventoServices.getByIdTipoEvento("ASV", connect);
				if (tipoEvento != null)
					evento.setCdTipoEvento(tipoEvento.getCdTipoEvento());

				List<ItemTransmitirFotoInfracao> fotos = item.getFotos();
				List<ItemTransmitirPerfilLacoInfracao> perfis = item.getPerfis();
				List<ItemTransmitirVideoInfracao> videos = item.getVideos();

				r = save(evento, null, connect);

				if (r.getCode() > 0) {

					evento.setCdEvento(((EventoEquipamento) r.getObjects().get("EVENTOEQUIPAMENTO")).getCdEvento());

					if (r.getCode() > 0 && fotos.size() > 0) {
						// 2. GRAVAR IMAGENS ANEXADAS
						r = saveFotosEvento(evento, fotos, decrypt, connect);
					}

					if (r.getCode() > 0 && videos.size() > 0) {
						// 3. GRAVAR VIDEOS ANEXADOS
						r = saveVideosEvento(evento, videos, connect);
					}

					if (r.getCode() > 0 && perfis.size() > 0) {
						// 4. GRAVAR PERFIS ANEXADOS
						r = savePerfisEvento(evento, perfis, connect);
					}
				}
			}

			if (r.getCode() <= 0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();

			return new Result(r.getCode(), r.getCode() <= 0 ? "Erro ao gravar eventos de equipamento."
					: "Eventos de equipamento gravados com sucesso.");
		} catch (Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! EventoEquipamentoServices.saveASV: " + e);
			return new Result(-1, "Erro ao gravar eventos de equipamento.");
		} finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	/**
	 * PSF - PARADA SOBRE A FAIXA
	 * 
	 * @param list
	 * @return
	 */
	public static Result savePSF(List<ItemTransmitirInfracaoPSFV1> list) {
		return savePSF(list, null);
	}

	public static Result savePSF(List<ItemTransmitirInfracaoPSFV1> list, Connection connect) {

		boolean isConnectionNull = connect == null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			Result r = new Result(-1);

			for (ItemTransmitirInfracaoPSFV1 item : list) {


				// 1. BUSCAR EQUIPAMENTO
				Equipamento equipamento = EquipamentoServices.getByIdEquipamento(item.getEquipamento(), connect);

				// TODO: FIX: somente enquanto não alterar a tabela de equipamento
				// boolean decrypt = !equipamento.getIdEquipamento().equals("GOR00140022");
				boolean decrypt = equipamento.getLgCriptografia() == 1;

				// 2. GRAVAR EVENTO
				EventoEquipamento evento = new EventoEquipamento();

				if (equipamento != null)
					evento.setCdEquipamento(equipamento.getCdEquipamento());

				evento.setNmEquipamento(item.getEquipamento());
				// evento.setTpClassificacao(item.getClassificacao());
				evento.setIdIdentificacaoInmetro(item.getIdentificacaoInmetro());
				evento.setNmKmRodovia(item.getKmRodovia());
				evento.setDsLocal(item.getLocal());
				evento.setNmMarcaEquipamento(item.getMarcaEquipamento());
				evento.setNmMetrosRodovia(item.getMetrosRodovia());
				evento.setNmModeloEquipamento(item.getModeloEquipamento());
				evento.setNrSerial(item.getNumeroSerial());
				evento.setNmOrgaoAutuador(item.getOrgaoAutuador());
				evento.setNrPlaca(item.getPlaca());
				evento.setNmRodovia(item.getRodovia());
				evento.setNmSentidoRodovia(item.getSentidoRodovia());
				evento.setTpEquipamento(item.getTipoEquipamento());
				evento.setTpVeiculo(item.getTipoVeiculo());
				evento.setNmUfRodovia(item.getUfRodovia());
				evento.setIdCidade(item.getCodCidade());
				evento.setVlComprimentoVeiculo(item.getComprimentoVeiculo());
				evento.setIdMedida(item.getIdMedida());
				evento.setNrPista(item.getPista());
				// evento.setVlConsiderada(item.getTempoPermanencia());
				evento.setVlLimite(item.getTempoTolerancia());
				evento.setVlPermanencia(item.getTempoPermanencia());
				evento.setVlSemaforoVermelho(item.getTempoSemaforoVermelho());
				// evento.setVlVelocidadeTolerada(item.getVelocidadeTolerada());
				evento.setDtAfericao(Util.convDateToCalendar(item.getDataAfericao()));
				evento.setDtConclusao(Util.convDateToCalendar(item.getDataConclusao()));
				evento.setLgTempoReal(item.isTempoReal() ? 1 : 0);

				evento.setDtEvento(new GregorianCalendar());

				// PARAMETRIZAR O TIPO DE EVENTO VAL
				TipoEvento tipoEvento = TipoEventoServices.getByIdTipoEvento("PSF", connect);
				if (tipoEvento != null)
					evento.setCdTipoEvento(tipoEvento.getCdTipoEvento());

				List<ItemTransmitirFotoInfracao> fotos = item.getFotos();
				List<ItemTransmitirPerfilLacoInfracao> perfis = item.getPerfis();
				List<ItemTransmitirVideoInfracao> videos = item.getVideos();

				r = save(evento, null, connect);

				if (r.getCode() > 0) {

					evento.setCdEvento(((EventoEquipamento) r.getObjects().get("EVENTOEQUIPAMENTO")).getCdEvento());

					if (r.getCode() > 0 && fotos.size() > 0) {
						// 2. GRAVAR IMAGENS ANEXADAS
						r = saveFotosEvento(evento, fotos, decrypt, connect);
					}

					if (r.getCode() > 0 && videos.size() > 0) {
						// 3. GRAVAR VIDEOS ANEXADOS
						r = saveVideosEvento(evento, videos, connect);
					}

					if (r.getCode() > 0 && perfis.size() > 0) {
						// 4. GRAVAR PERFIS ANEXADOS
						r = savePerfisEvento(evento, perfis, connect);
					}
				}
			}

			if (r.getCode() <= 0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();

			return new Result(r.getCode(), r.getCode() <= 0 ? "Erro ao gravar eventos de equipamento."
					: "Eventos de equipamento gravados com sucesso.");
		} catch (Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! EventoEquipamentoServices.savePSF: " + e);
			return new Result(-1, "Erro ao gravar eventos de equipamento.");
		} finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	/**
	 * EST - ESTATISTICA
	 * 
	 * @param list
	 * @return
	 */
	public static Result saveEST(List<ItemTransmitirInfracaoESTV1> list) {
		return saveEST(list, null);
	}

	public static Result saveEST(List<ItemTransmitirInfracaoESTV1> list, Connection connect) {

		boolean isConnectionNull = connect == null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			Result r = new Result(-1);

			for (ItemTransmitirInfracaoESTV1 item : list) {


				// 1. BUSCAR EQUIPAMENTO
				Equipamento equipamento = EquipamentoServices.getByIdEquipamento(item.getEquipamento(), connect);

				// TODO: FIX: somente enquanto não alterar a tabela de equipamento
				// boolean decrypt = !equipamento.getIdEquipamento().equals("GOR00140022");
				boolean decrypt = equipamento.getLgCriptografia() == 1;

				// 2. GRAVAR EVENTO
				EventoEquipamento evento = new EventoEquipamento();

				if (equipamento != null)
					evento.setCdEquipamento(equipamento.getCdEquipamento());

				evento.setNmEquipamento(item.getEquipamento());
				evento.setIdIdentificacaoInmetro(item.getIdentificacaoInmetro());
				evento.setNmKmRodovia(item.getKmRodovia());
				evento.setDsLocal(item.getLocal());
				evento.setNmMarcaEquipamento(item.getMarcaEquipamento());
				evento.setNmMetrosRodovia(item.getMetrosRodovia());
				evento.setNmModeloEquipamento(item.getModeloEquipamento());
				evento.setNrSerial(item.getNumeroSerial());
				evento.setNmOrgaoAutuador(item.getOrgaoAutuador());
				evento.setNrPlaca(item.getPlaca());
				evento.setNmRodovia(item.getRodovia());
				evento.setNmSentidoRodovia(item.getSentidoRodovia());
				evento.setTpEquipamento(item.getTipoEquipamento());
				evento.setTpVeiculo(item.getTipoVeiculo());
				evento.setNmUfRodovia(item.getUfRodovia());
				evento.setIdCidade(item.getCodCidade());
				evento.setVlComprimentoVeiculo(item.getComprimentoVeiculo());
				evento.setIdMedida(item.getIdMedida());
				evento.setNrPista(item.getPista());
				evento.setVlConsiderada(item.getVelocidadeConsiderada());
				evento.setVlLimite(item.getVelocidadeLimite());
				evento.setVlMedida(item.getVelocidadeMedida());
				evento.setVlVelocidadeTolerada(item.getVelocidadeTolerada());
				evento.setDtAfericao(Util.convDateToCalendar(item.getDataAfericao()));
				evento.setDtConclusao(Util.convDateToCalendar(item.getDataConclusao()));
				evento.setLgTempoReal(item.isTempoReal() ? 1 : 0);

				evento.setDtEvento(new GregorianCalendar());
				// PARAMETRIZAR O TIPO DE EVENTO VAL
				TipoEvento tipoEvento = TipoEventoServices.getByIdTipoEvento("EST", connect);
				if (tipoEvento != null)
					evento.setCdTipoEvento(tipoEvento.getCdTipoEvento());

				List<ItemTransmitirFotoInfracao> fotos = item.getFotos();
				List<ItemTransmitirPerfilLacoInfracao> perfis = item.getPerfis();
				List<ItemTransmitirVideoInfracao> videos = item.getVideos();

				r = save(evento, null, connect);

				if (r.getCode() > 0) {

					evento.setCdEvento(((EventoEquipamento) r.getObjects().get("EVENTOEQUIPAMENTO")).getCdEvento());

					if (r.getCode() > 0 && fotos.size() > 0) {
						// 2. GRAVAR IMAGENS ANEXADAS
						r = saveFotosEvento(evento, fotos, decrypt, connect);
					}

					if (r.getCode() > 0 && videos.size() > 0) {
						// 3. GRAVAR VIDEOS ANEXADOS
						r = saveVideosEvento(evento, videos, connect);
					}

					if (r.getCode() > 0 && perfis.size() > 0) {
						// 4. GRAVAR PERFIS ANEXADOS
						r = savePerfisEvento(evento, perfis, connect);
					}
				}
			}

			if (r.getCode() <= 0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();

			return new Result(r.getCode(), r.getCode() <= 0 ? "Erro ao gravar eventos de equipamento."
					: "Eventos de equipamento gravados com sucesso.");
		} catch (Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! EventoEquipamentoServices.saveEST: " + e);
			return new Result(-1, "Erro ao gravar eventos de equipamento.");
		} finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	/**
	 * ARQUIVOS ACESSORIOS - PERFIS
	 * 
	 * @param evento
	 * @param perfis
	 * @param connect
	 * @return
	 */
	private static Result savePerfisEvento(EventoEquipamento evento, List<ItemTransmitirPerfilLacoInfracao> perfis,
			Connection connect) {

		boolean isConnectionNull = connect == null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			Result r = new Result(-1);


			for (ItemTransmitirPerfilLacoInfracao perfil : perfis) {

				// 4.1. GRAVAR ARQUIVO
				Arquivo arquivo = new Arquivo();
				arquivo.setCdArquivo(0);
				arquivo.setBlbArquivo(perfil.getAmostras());
				arquivo.setDtArquivamento(new GregorianCalendar());
				arquivo.setDtCriacao(new GregorianCalendar());

				r = ArquivoServices.save(arquivo, null, connect);

				// 4.2. GRAVAR ARQUIVO x EVENTO
				if (r.getCode() > 0) {
					arquivo.setCdArquivo(((Arquivo) r.getObjects().get("ARQUIVO")).getCdArquivo());

					EventoArquivo eventoArquivo = new EventoArquivo();

					eventoArquivo.setCdEvento(evento.getCdEvento());
					eventoArquivo.setCdArquivo(arquivo.getCdArquivo());
					eventoArquivo.setTpArquivo(EventoArquivoServices.TP_ARQUIVO_PERFIL);
					eventoArquivo.setDtArquivo(new GregorianCalendar());

					r = EventoArquivoServices.save(eventoArquivo, null, connect);

					if (r.getCode() <= 0)
						break;
				} else
					break;
			}

			if (r.getCode() <= 0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();

			return new Result(r.getCode(),
					r.getCode() <= 0 ? "Erro ao gravar perfis de evento." : "Perfis de evento gravados com sucesso.");
		} catch (Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! EventoEquipamentoServices.savePerfisEvento: " + e);
			return new Result(-1, "Erro ao gravar perfis de evento.");
		} finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	/**
	 * ARQUIVOS ACESSORIOS - VIDEOS
	 * 
	 * @param evento
	 * @param perfis
	 * @param connect
	 * @return
	 */
	private static Result saveVideosEvento(EventoEquipamento evento, List<ItemTransmitirVideoInfracao> videos,
			Connection connect) {

		boolean isConnectionNull = connect == null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			Result r = new Result(-1);

			for (ItemTransmitirVideoInfracao video : videos) {

				// 3.1. GRAVAR ARQUIVO
				Arquivo arquivo = new Arquivo();
				arquivo.setCdArquivo(0);
				arquivo.setBlbArquivo(video.getVideo());
				arquivo.setDtArquivamento(new GregorianCalendar());
				arquivo.setDtCriacao(new GregorianCalendar());

				r = ArquivoServices.save(arquivo, null, connect);

				// 3.2. GRAVAR ARQUIVO x EVENTO
				if (r.getCode() > 0) {
					arquivo.setCdArquivo(((Arquivo) r.getObjects().get("ARQUIVO")).getCdArquivo());

					EventoArquivo eventoArquivo = new EventoArquivo();

					eventoArquivo.setCdEvento(evento.getCdEvento());
					eventoArquivo.setCdArquivo(arquivo.getCdArquivo());
					eventoArquivo.setTpArquivo(EventoArquivoServices.TP_ARQUIVO_VIDEO);
					eventoArquivo.setIdArquivo(String.valueOf(video.getNumeroVideo()));
					eventoArquivo.setTpEventoFoto(video.getTipoEventoFoto());
					eventoArquivo.setDtArquivo(new GregorianCalendar());

					r = EventoArquivoServices.save(eventoArquivo, null, connect);

					if (r.getCode() <= 0)
						break;
				} else
					break;
			}

			if (r.getCode() <= 0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();

			return new Result(r.getCode(),
					r.getCode() <= 0 ? "Erro ao gravar videos de evento." : "Videos de evento gravados com sucesso.");
		} catch (Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! EventoEquipamentoServices.saveVideosEvento: " + e);
			return new Result(-1, "Erro ao gravar videos de evento.");
		} finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}

	}

	/**
	 * ARQUIVOS ACESSORIOS - FOTOS
	 * 
	 * @param evento
	 * @param perfis
	 * @param connect
	 * @return
	 */
	private static Result saveFotosEvento(EventoEquipamento evento, List<ItemTransmitirFotoInfracao> fotos,
			boolean decrypt, Connection connect) {
		boolean isConnectionNull = connect == null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			Result r = new Result(-1);

			for (ItemTransmitirFotoInfracao foto : fotos) {

				// 2.1. GRAVAR ARQUIVO

				byte[] fotoBytes = foto.getFoto();
				byte[] fotoBytesDec = null;

				if (decrypt) {
					fotoBytesDec = LocalDecrypt.decrypt(fotoBytes);
				}

				Arquivo arquivo = new Arquivo();
				arquivo.setCdArquivo(0);
				arquivo.setNmArquivo(String.valueOf(foto.getNumeroImagem()));
				arquivo.setBlbArquivo(fotoBytesDec != null ? fotoBytesDec : fotoBytes);
				arquivo.setDtArquivamento(new GregorianCalendar());
				arquivo.setDtCriacao(Util.convDateToCalendar(foto.getDataFoto()));

				r = ArquivoServices.save(arquivo, null, connect);

				// 2.2. GRAVAR ARQUIVO x EVENTO

				if (r.getCode() > 0) {
					arquivo.setCdArquivo(((Arquivo) r.getObjects().get("ARQUIVO")).getCdArquivo());

					// houve decryt e hï¿½ dados
					if (fotoBytesDec != null && fotoBytesDec.length > 0) {
						PreparedStatement pstmt = connect
								.prepareStatement("UPDATE grl_arquivo SET blb_arquivo_temp=? WHERE cd_arquivo=?");
						pstmt.setBytes(1, fotoBytes);
						pstmt.setInt(2, arquivo.getCdArquivo());
						pstmt.execute();
					}

					EventoArquivo eventoArquivo = new EventoArquivo();

					eventoArquivo.setCdEvento(evento.getCdEvento());
					eventoArquivo.setCdArquivo(arquivo.getCdArquivo());
					eventoArquivo.setTpArquivo(EventoArquivoServices.TP_ARQUIVO_FOTO);
					eventoArquivo.setIdArquivo(String.valueOf(foto.getNumeroImagem()));
					eventoArquivo.setTpEventoFoto(foto.getTipoEventoFoto());
					eventoArquivo.setTpFoto(foto.getTipoFoto());
					eventoArquivo.setDtArquivo(Util.convDateToCalendar(foto.getDataFoto()));

					r = EventoArquivoServices.save(eventoArquivo, null, connect);

					if (r.getCode() <= 0)
						break;
				} else
					break;
			}

			if (r.getCode() <= 0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();

			return new Result(r.getCode(),
					r.getCode() <= 0 ? "Erro ao gravar fotos de evento." : "Fotos de evento gravados com sucesso.");
		} catch (Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! EventoEquipamentoServices.saveFotosEvento: " + e);
			return new Result(-1, "Erro ao gravar fotos de evento.");
		} finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}

	}

	public static Result saveCameraEventos(EventoEquipamento evento, ArrayList<Arquivo> arquivos, Connection connect) {

		boolean isConnectionNull = connect == null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			Result r = new Result(-1);

			r = save(evento, null, connect);

			if (r.getCode() > 0) {

				evento.setCdEvento(((EventoEquipamento) r.getObjects().get("EVENTOEQUIPAMENTO")).getCdEvento());

				for (Arquivo arquivo : arquivos) {
					r = ArquivoServices.save(arquivo, null, connect);

					// 2.2. GRAVAR ARQUIVO x EVENTO

					if (r.getCode() > 0) {
						arquivo.setCdArquivo(((Arquivo) r.getObjects().get("ARQUIVO")).getCdArquivo());

						EventoArquivo eventoArquivo = new EventoArquivo();

						eventoArquivo.setCdEvento(evento.getCdEvento());
						eventoArquivo.setCdArquivo(arquivo.getCdArquivo());
						eventoArquivo.setTpArquivo(EventoArquivoServices.TP_ARQUIVO_FOTO);
						eventoArquivo.setIdArquivo(arquivo.getNmArquivo().substring(0, 9));
//						eventoArquivo.setTpEventoFoto(foto.getTipoEventoFoto());
//						eventoArquivo.setTpFoto(foto.getTipoFoto());
						eventoArquivo.setDtArquivo(evento.getDtEvento());

						r = EventoArquivoServices.save(eventoArquivo, null, connect);
					}
				}
			}
			if (r.getCode() <= 0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();

			return new Result(r.getCode(), r.getCode() <= 0 ? "Erro ao gravar eventos de equipamento."
					: "Eventos de equipamento gravados com sucesso.", "EVENTOEQUIPAMENTO", evento);
		} catch (Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! EventoEquipamentoServices.saveEST: " + e);
			return new Result(-1, "Erro ao gravar eventos de equipamento.");
		} finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	/**
	 * ARQUIVOS ACESSORIOS - FOTOS - USADO PELO SYNC VIA FTP
	 * 
	 * @param evento
	 * @param perfis
	 * @param connect
	 * @return
	 */
	private static Result saveArquivosEventoFtp(EventoEquipamento evento, List<Arquivo> arquivos, int tpArquivo,
			Connection connect) {
		boolean isConnectionNull = connect == null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			Result r = new Result(-1);

			for (Arquivo arquivo : arquivos) {

				// 2.1. GRAVAR ARQUIVO

//				Arquivo arquivo = new Arquivo();
//				arquivo.setCdArquivo(0);
//				arquivo.setNmArquivo(String.valueOf(foto.getNumeroImagem()));
//				arquivo.setBlbArquivo(fotoBytesDec!=null ? fotoBytesDec : fotoBytes);
//				arquivo.setDtArquivamento(new GregorianCalendar());
//				arquivo.setDtCriacao(Util.convDateToCalendar(foto.getDataFoto()));
//				
				r = ArquivoServices.save(arquivo, null, connect);

				// 2.2. GRAVAR ARQUIVO x EVENTO

				if (r.getCode() > 0) {
					arquivo.setCdArquivo(((Arquivo) r.getObjects().get("ARQUIVO")).getCdArquivo());

					EventoArquivo eventoArquivo = new EventoArquivo();

					eventoArquivo.setCdEvento(evento.getCdEvento());
					eventoArquivo.setCdArquivo(arquivo.getCdArquivo());
					eventoArquivo.setTpArquivo(tpArquivo);
					// eventoArquivo.setIdArquivo(arquivo.getNmArquivo());
					eventoArquivo.setTpEventoFoto(3);
					eventoArquivo.setTpFoto(0);
					eventoArquivo.setDtArquivo(arquivo.getDtCriacao());

					r = EventoArquivoServices.save(eventoArquivo, null, connect);

					if (r.getCode() <= 0)
						break;
				} else
					break;
			}

			if (r.getCode() <= 0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();

			return new Result(r.getCode(), r.getCode() <= 0 ? "Erro ao gravar arquivos de evento."
					: "Arquivos de evento gravados com sucesso.");
		} catch (Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! EventoEquipamentoServices.saveArquivosEventoFtp: " + e);
			return new Result(-1, "Erro ao gravar arquivos de evento.");
		} finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}

	}

	/**
	 * Sincronia do Radar Server com o banco local do municipio
	 * 
	 * @return
	 */
	synchronized public static Result syncRadarServer() {
		return syncRadarServer(null);
	}

	/**
	 * Sincronia do Radar Server com o banco local do municipio
	 * 
	 * @return
	 */
	synchronized public static Result syncRadarServer(Connection connect) {
		boolean isConnectionNull = connect == null;
		PreparedStatement pstmt;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			// 1. VERIFICA ULTIMA SINCRONIZACAO NO BANCO LOCAL
			String[] sqlLimit = Util.getLimitAndSkip(1, 0);

			pstmt = connect.prepareStatement("SELECT " + sqlLimit[0]
					+ " * FROM mob_evento_equipamento A, mob_tipo_evento B "
					+ " WHERE A.cd_tipo_evento = B.cd_tipo_evento "
					+ " AND B.id_tipo_evento IN ('ASV', 'VAL', 'PSF', 'RLP', 'CEX', 'LNP', 'LHP', 'CLP') "
					+ " AND A.st_evento <> " + EventoEquipamentoServices.ST_EVENTO_NAO_PROCESSADO
					+ " AND A.dt_processamento is not null " + " ORDER BY A.dt_processamento DESC " + sqlLimit[1]);

			ResultSetMap rsmArquivos = new ResultSetMap(pstmt.executeQuery());
			GregorianCalendar dtUltimoProcessamento = null;

			if (rsmArquivos.next()) {
				dtUltimoProcessamento = rsmArquivos.getGregorianCalendar("DT_PROCESSAMENTO");
			}

			// 2. BUSCA EVENTOS APOS ULTIMA SINCRONIZACAO NO RADAR SERVER PARA O ORGAO
			// AUTUADOR INDICADO

			String driver = Util.getConfManager().getProps().getProperty("RADAR_DRIVER");
			String url = Util.getConfManager().getProps().getProperty("RADAR_DBPATH");
			String login = Util.getConfManager().getProps().getProperty("RADAR_LOGIN");
			String senha = Util.getConfManager().getProps().getProperty("RADAR_PASS");

			String nmOrgaoAutuador = Util.getConfManager().getProps().getProperty("RADAR_ORGAO_AUTUADOR");

			if (driver == null || url == null || login == null || senha == null || nmOrgaoAutuador == null) {
				System.out.println(
						"Impossível sincronizar com o servidor do radar. Valores de configurações inexistentes.");
				return new Result(-2,
						"Impossível sincronizar com o servidor do radar. Valores de configurações inexistentes.");
			}

			Connection connectRS = Conexao.conectar(driver, url, login, senha);
			PreparedStatement pstmtRS = connectRS
					.prepareStatement("SELECT * FROM mob_evento_equipamento A, mob_tipo_evento B "
							+ " WHERE A.cd_tipo_evento = B.cd_tipo_evento "
							+ " AND B.id_tipo_evento IN ('ASV', 'VAL', 'PSF', 'RLP', 'CEX', 'LNP', 'LHP', 'CLP') "
							+ " AND A.st_evento <> " + EventoEquipamentoServices.ST_EVENTO_NAO_PROCESSADO
							+ " AND A.nm_orgao_autuador = ? "
							+ (dtUltimoProcessamento != null ? " AND A.dt_processamento > ?" : ""));
			pstmtRS.setString(1, nmOrgaoAutuador);

			if (dtUltimoProcessamento != null)
				pstmtRS.setTimestamp(2, new Timestamp(dtUltimoProcessamento.getTimeInMillis()));

			ResultSetMap rsmEventosRS = new ResultSetMap(pstmtRS.executeQuery());

			// 2.1 Nï¿½O HAVENDO EVENTOS, FINALIZAR
			if (rsmEventosRS.size() == 0) {
				System.out
						.println("Nao foi necessï¿½rio sincronizar com o servidor do radar. Nenhum evento encontrado.");
				return new Result(-3,
						"Nao foi necessï¿½rio sincronizar com o servidor do radar. Nenhum evento encontrado.");
			}

			Result r = null;

			// 3. ADICIONA OS EVENTOS NO BANCO LOCAL
			if (rsmEventosRS.size() > 0) {
				System.out.println(
						"Sincronizando " + rsmEventosRS.size() + " evento(s) encontrados no servidor do radar.");

				while (rsmEventosRS.next()) {
					Equipamento equipamento = getEquipamentoLocal(rsmEventosRS, connect, connectRS);
					
					TipoEvento tipoEvento = TipoEventoServices
							.getByIdTipoEvento(rsmEventosRS.getString("ID_TIPO_EVENTO"), connect);

					if (equipamento == null) {
						String msg = "Evento não contém um equipamento válido ou registrado no banco local:\n"
								+ "Equip.\t" + rsmEventosRS.getString("NM_EQUIPAMENTO") + "\n" + "Tp. Evt.\t"
								+ rsmEventosRS.getString("ID_TIPO_EVENTO") + "\n" + "Dt. Evt.\t"
								+ Util.convCalendarString(rsmEventosRS.getGregorianCalendar("DT_EVENTO"));
						System.out.println(msg);
						return new Result(-3, msg);
					}

					if (tipoEvento == null) {
						String msg = "Evento não contém um tipo de evento válido ou registrado no banco local:\n"
								+ "Equip.\t" + rsmEventosRS.getString("NM_EQUIPAMENTO") + "\n" + "Tp. Evt.\t"
								+ rsmEventosRS.getString("ID_TIPO_EVENTO") + "\n" + "Dt. Evt.\t"
								+ Util.convCalendarString(rsmEventosRS.getGregorianCalendar("DT_EVENTO"));
						System.out.println(msg);
						return new Result(-3, msg);
					}

					
					//VERIFICACAO DE AITS DUPLICADOS PELA HORA/PLACA/EQUIPAMENTO
					GregorianCalendar dtAfericaoMinutoMais = rsmEventosRS.getGregorianCalendar("dt_conclusao");
					dtAfericaoMinutoMais.add(Calendar.MINUTE, 1);
					GregorianCalendar dtAfericaoMinutoMenos = rsmEventosRS.getGregorianCalendar("dt_conclusao");
					dtAfericaoMinutoMenos.add(Calendar.MINUTE, -1);
					ResultSet rs2 = connect.prepareStatement(
							"SELECT * FROM mob_evento_equipamento "
						  + "	WHERE nr_placa = '"+rsmEventosRS.getString("NR_PLACA")+"'"
						  + "     AND cd_equipamento = " + equipamento.getCdEquipamento() 
						  + "     AND dt_conclusao <= '" + Util.formatDate(dtAfericaoMinutoMais, "yyyy-MM-dd HH:mm:ss") + "'"
						  + "     AND dt_conclusao >= '" + Util.formatDate(dtAfericaoMinutoMenos, "yyyy-MM-dd HH:mm:ss") + "'").executeQuery();

					if(rs2.next()) {
						LogUtils.debug("EventoEquipamentoServices.syncRadarServer: Evento possivelmente duplicado... "+rsmEventosRS.getString("CD_EVENTO"));
						continue;
					}
					
					
					int cdVeiculo = 0;
					if (rsmEventosRS.getInt("CD_VEICULO") > 0) {
						PreparedStatement pstmtVeiculoRS = connectRS
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
						pstmtVeiculoRS.setInt(1, rsmEventosRS.getInt("CD_VEICULO"));

						ResultSetMap rsmVeiculoRS = new ResultSetMap(pstmtVeiculoRS.executeQuery());
						VeiculoDTO veiculoDtoRS = new VeiculoDTO.Builder(rsmVeiculoRS.getLines().get(0)).build();

						PreparedStatement pstmtVeiculo = connect
								.prepareStatement("SELECT A.* from fta_veiculo A WHERE A.nr_placa = ?");
						pstmtVeiculo.setString(1, rsmEventosRS.getString("NR_PLACA"));
						ResultSet rsVeiculo = pstmtVeiculo.executeQuery();

						if (!rsVeiculo.next()) {
							MarcaModelo marca = null;
							Cor corVeiculo = null;
							TipoVeiculo tipoVeiculo = null;
							EspecieVeiculo especieVeiculo = null;
							CategoriaVeiculo categoriaVeiculo = null;

							if (rsmVeiculoRS.next()) {
								if (rsmVeiculoRS.getString("NM_MODELO") != null) {
									marca = MarcaModeloServices.getByNome(rsmVeiculoRS.getString("NM_MODELO"), connect);
								}
								if (rsmVeiculoRS.getString("NM_COR_VEICULO") != null) {
									corVeiculo = CorServices.getByNome(rsmVeiculoRS.getString("NM_COR_VEICULO"),
											connect);
								}
								if (rsmVeiculoRS.getString("NM_TIPO_VEICULO") != null) {
									tipoVeiculo = TipoVeiculoServices
											.getByNome(rsmVeiculoRS.getString("NM_TIPO_VEICULO"), connect);
								}
								if (rsmVeiculoRS.getString("DS_ESPECIE") != null) {
									especieVeiculo = EspecieVeiculoServices
											.getByNome(rsmVeiculoRS.getString("DS_ESPECIE"), connect);
								}
								if (rsmVeiculoRS.getString("NM_CATEGORIA") != null) {
									categoriaVeiculo = CategoriaVeiculoServices
											.getByNome(rsmVeiculoRS.getString("NM_CATEGORIA"), connect);
								}
							}

							if ((marca != null && corVeiculo != null && tipoVeiculo != null
									&& especieVeiculo != null)) {
								Veiculo veiculo = veiculoDtoRS;
								veiculo.setCdVeiculo(0);
								veiculo.setCdMarca(marca.getCdMarca());
								veiculo.setCdCor(corVeiculo.getCdCor());
								veiculo.setNmCor(corVeiculo.getNmCor());
								veiculo.setCdTipoVeiculo(tipoVeiculo.getCdTipoVeiculo());
								veiculo.setCdEspecie(especieVeiculo.getCdEspecie());
								// veiculo.setCdCategoria(categoriaVeiculo.getCdCategoria());

								Result result = VeiculoServices.save(veiculo, null, connect);
								veiculo = (Veiculo) result.getObjects().get("VEICULO");

								cdVeiculo = result.getCode() < 0 ? 0 : result.getCode();
							}
						} else {
							cdVeiculo = rsVeiculo.getInt("cd_veiculo");
						}
					}

					EventoEquipamento evento = new EventoEquipamento(0, equipamento.getCdEquipamento(),
							tipoEvento.getCdTipoEvento(), rsmEventosRS.getGregorianCalendar("DT_EVENTO"),
							rsmEventosRS.getString("NM_ORGAO_AUTUADOR"), rsmEventosRS.getString("NM_EQUIPAMENTO"),
							rsmEventosRS.getString("DS_LOCAL"), rsmEventosRS.getString("ID_IDENTIFICACAO_INMETRO"),
							rsmEventosRS.getGregorianCalendar("DT_AFERICAO"), rsmEventosRS.getInt("NR_PISTA"),
							rsmEventosRS.getGregorianCalendar("DT_CONCLUSAO"), rsmEventosRS.getInt("VL_LIMITE"),
							rsmEventosRS.getInt("VL_VELOCIDADE_TOLERADA"), rsmEventosRS.getInt("VL_MEDIDA"),
							rsmEventosRS.getInt("VL_CONSIDERADA"), rsmEventosRS.getString("NR_PLACA"),
							rsmEventosRS.getInt("LG_TEMPO_REAL"), rsmEventosRS.getInt("TP_VEICULO"),
							rsmEventosRS.getInt("VL_COMPRIMENTO_VEICULO"), rsmEventosRS.getInt("ID_MEDIDA"),
							rsmEventosRS.getString("NR_SERIAL"), rsmEventosRS.getString("NM_MODELO_EQUIPAMENTO"),
							rsmEventosRS.getString("NM_RODOVIA"), rsmEventosRS.getString("NM_UF_RODOVIA"),
							rsmEventosRS.getString("NM_KM_RODOVIA"), rsmEventosRS.getString("NM_METROS_RODOVIA"),
							rsmEventosRS.getString("NM_SENTIDO_RODOVIA"), rsmEventosRS.getInt("ID_CIDADE"),
							rsmEventosRS.getString("NM_MARCA_EQUIPAMENTO"), rsmEventosRS.getInt("TP_EQUIPAMENTO"),
							rsmEventosRS.getInt("NR_PISTA"), rsmEventosRS.getGregorianCalendar("DT_EXECUCAO_JOB"),
							rsmEventosRS.getString("ID_UUID"), rsmEventosRS.getInt("TP_RESTRICAO"),
							rsmEventosRS.getInt("TP_CLASSIFICACAO"), rsmEventosRS.getInt("VL_PERMANENCIA"),
							rsmEventosRS.getInt("VL_SEMAFORO_VERMELHO"), rsmEventosRS.getInt("ST_EVENTO"),
							rsmEventosRS.getInt("CD_MOTIVO_CANCELAMENTO"), rsmEventosRS.getString("TXT_EVENTO"),
							rsmEventosRS.getInt("LG_OPLR"), rsmEventosRS.getGregorianCalendar("DT_CANCELAMENTO"),
							rsmEventosRS.getInt("CD_USUARIO_CANCELAMENTO"),
							rsmEventosRS.getGregorianCalendar("DT_PROCESSAMENTO"), cdVeiculo, 
							rsmEventosRS.getInt("LG_ENVIADO"), 
							0, 
							rsmEventosRS.getInt("VL_TOLERANCIA"));

					r = EventoEquipamentoServices.save(evento, null, connect);

					// 4. BUSCA ARQUIVOS VINCULADOS AO EVENTO
					if (r.getCode() > 0) {

						evento.setCdEvento(((EventoEquipamento) r.getObjects().get("EVENTOEQUIPAMENTO")).getCdEvento());

						pstmtRS = connectRS.prepareStatement("SELECT * FROM mob_evento_arquivo A, grl_arquivo B "
								+ " WHERE A.cd_arquivo = B.cd_arquivo " + " AND A.cd_evento = ? "
								+ " AND A.tp_arquivo = " + EventoArquivoServices.TP_ARQUIVO_FOTO);
						pstmtRS.setInt(1, rsmEventosRS.getInt("CD_EVENTO"));

						ResultSet rsEventoArquivosRS = pstmtRS.executeQuery();

						// 5. GRAVAR ARQUIVOS VINCULADOS AO EVENTO LOCALMENTE
						while (rsEventoArquivosRS.next()) {

							// 5.1. GRAVAR ARQUIVO

							Arquivo arquivo = new Arquivo();
							arquivo.setCdArquivo(0);
							arquivo.setNmArquivo(rsEventoArquivosRS.getString("NM_ARQUIVO"));
							arquivo.setBlbArquivo(rsEventoArquivosRS.getBytes("BLB_ARQUIVO"));
							arquivo.setDtArquivamento(
									Util.convTimestampToCalendar(rsEventoArquivosRS.getTimestamp("DT_ARQUIVAMENTO")));
							arquivo.setDtCriacao(
									Util.convTimestampToCalendar(rsEventoArquivosRS.getTimestamp("DT_CRIACAO")));

							r = ArquivoServices.save(arquivo, null, connect);

							// 2.2. GRAVAR ARQUIVO x EVENTO

							if (r.getCode() > 0) {
								arquivo.setCdArquivo(((Arquivo) r.getObjects().get("ARQUIVO")).getCdArquivo());

								EventoArquivo eventoArquivo = new EventoArquivo();

								eventoArquivo.setCdEvento(evento.getCdEvento());
								eventoArquivo.setCdArquivo(arquivo.getCdArquivo());
								eventoArquivo.setTpArquivo(rsEventoArquivosRS.getInt("TP_ARQUIVO"));
								eventoArquivo.setIdArquivo(rsEventoArquivosRS.getString("ID_ARQUIVO"));
								eventoArquivo.setTpEventoFoto(rsEventoArquivosRS.getInt("TP_EVENTO_FOTO"));
								eventoArquivo.setTpFoto(rsEventoArquivosRS.getInt("TP_FOTO"));
								eventoArquivo.setDtArquivo(
										Util.convTimestampToCalendar(rsEventoArquivosRS.getTimestamp("DT_ARQUIVO")));

								r = EventoArquivoServices.save(eventoArquivo, null, connect);

								if (r.getCode() <= 0)
									break;
							} else
								break;
						}
					}

					if (r.getCode() <= 0)
						break;
				}

			}

			if (r.getCode() <= 0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();

			Conexao.desconectar(connectRS);

			return new Result(1, rsmEventosRS.size() + " evento(s) sincronizados com o servidor do radar.");

		} catch (SQLException sqlExpt) {
			Conexao.rollback(connect);
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! EventoEquipamentoServices.syncRadarServer: " + sqlExpt);
			return null;
		} catch (Exception e) {
			Conexao.rollback(connect);
			e.printStackTrace(System.out);
			System.err.println("Erro! EventoEquipamentoServices.syncRadarServer: " + e);
			return null;
		} finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	private static Equipamento getEquipamentoLocal(ResultSetMap rsmEventosRS, Connection connect,
			Connection connectRS) throws Exception {
		Equipamento equipamentoRadar = EquipamentoDAO
				.get(rsmEventosRS.getInt("CD_EQUIPAMENTO"), connectRS);
		
		if(equipamentoRadar == null) {
			throw new Exception("O equipamento não foi encontrado no banco origem (RADAR).");
		}
							
		Equipamento equipamento = EquipamentoServices
				.getByIdAndTp(equipamentoRadar.getIdEquipamento(), equipamentoRadar.getTpEquipamento(), connect);

		if(equipamento == null) {
			throw new Exception("O equipamento não foi encontrado no banco destino (ÓRGÃO).");
		}
		
		return equipamento;
	}

	/*
	 * STATS
	 */

	public static ResultSetMap statsQtInfracaoComprimentoVeiculo() {
		try {
			GregorianCalendar hoje = new GregorianCalendar();
			GregorianCalendar dtInicial = new GregorianCalendar(hoje.get(Calendar.YEAR), Calendar.JANUARY, 1, 0, 0, 0);

			return statsQtInfracaoComprimentoVeiculo(dtInicial, hoje, null, null);
		} catch (Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! EventoEquipamentoServices.statsQtInfracaoComprimentoVeiculo: " + e);
			return null;
		}
	}

	public static ResultSetMap statsQtInfracaoComprimentoVeiculo(String idOrgao) {
		try {
			GregorianCalendar hoje = new GregorianCalendar();
			GregorianCalendar dtInicial = new GregorianCalendar(hoje.get(Calendar.YEAR), Calendar.JANUARY, 1, 0, 0, 0);

			return statsQtInfracaoComprimentoVeiculo(dtInicial, hoje, idOrgao, null);
		} catch (Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! EventoEquipamentoServices.statsQtInfracaoComprimentoVeiculo: " + e);
			return null;
		}
	}

	public static ResultSetMap statsQtInfracaoComprimentoVeiculo(GregorianCalendar dtInicial,
			GregorianCalendar dtFinal) {
		return statsQtInfracaoComprimentoVeiculo(dtInicial, dtFinal, null, null);
	}

	public static ResultSetMap statsQtInfracaoComprimentoVeiculo(GregorianCalendar dtInicial, GregorianCalendar dtFinal,
			String idOrgao) {
		return statsQtInfracaoComprimentoVeiculo(dtInicial, dtFinal, idOrgao, null);
	}

	public static ResultSetMap statsQtInfracaoComprimentoVeiculo(GregorianCalendar dtInicial, GregorianCalendar dtFinal,
			String idOrgao, Connection connect) {
		boolean isConnectionNull = connect == null;
		try {
			connect = isConnectionNull ? Conexao.conectar() : connect;

			PreparedStatement ps = connect.prepareStatement(" SELECT A.cd_equipamento, C.id_orgao, "
					+ "		COUNT(*) filter (WHERE vl_comprimento_veiculo < 3) AS leve1,"
					+ " 	COUNT(*) filter (WHERE vl_comprimento_veiculo >= 3 AND vl_comprimento_veiculo < 6) AS leve2,"
					+ " 	COUNT(*) filter (WHERE vl_comprimento_veiculo >= 6 AND vl_comprimento_veiculo < 15) AS pesado1,"
					+ " 	COUNT(*) filter (WHERE vl_comprimento_veiculo >= 15 AND vl_comprimento_veiculo < 19) AS pesado2,"
					+ " 	COUNT(*) filter (WHERE vl_comprimento_veiculo >= 19) AS especial"
					+ " FROM mob_evento_equipamento A"
					+ " LEFT OUTER JOIN grl_equipamento B ON (A.cd_equipamento = B.cd_equipamento)"
					+ " LEFT OUTER JOIN mob_orgao C ON (B.cd_orgao = C.cd_orgao)" + " WHERE A.dt_conclusao >= ?"
					+ " AND A.dt_conclusao <= ?"
					// + " AND A.vl_considerada >= A.vl_velocidade_tolerada"
					+ (idOrgao != null ? " AND C.id_orgao = '" + idOrgao + "'" : "")
					+ " GROUP BY A.cd_equipamento, C.id_orgao" + " ORDER BY C.id_orgao");
			ps.setTimestamp(1, Util.convCalendarToTimestamp(dtInicial));
			ps.setTimestamp(2, Util.convCalendarToTimestamp(dtFinal));

			ResultSetMap rsm = new ResultSetMap(ps.executeQuery());

			int tLeve1 = 0;
			int tLeve2 = 0;
			int tPesado1 = 0;
			int tPesado2 = 0;
			int tEspecial = 0;
			while (rsm.next()) {
				Equipamento equipamento = EquipamentoDAO.get(rsm.getInt("cd_equipamento"), connect);

				rsm.setValueToField("nm_equipamento", equipamento.getNmEquipamento());
				rsm.setValueToField("ds_equipamento", equipamento.getDsLocal().toUpperCase() + "\n("
						+ rsm.getString("id_orgao") + " - " + equipamento.getIdEquipamento() + ")");
				rsm.setValueToField("total_equipamento", rsm.getInt("leve1") + rsm.getInt("leve2")
						+ rsm.getInt("pesado1") + rsm.getInt("pesado2") + rsm.getInt("especial"));

				tLeve1 += rsm.getInt("leve1");
				tLeve2 += rsm.getInt("leve2");
				tPesado1 += rsm.getInt("pesado1");
				tPesado2 += rsm.getInt("pesado2");
				tEspecial += rsm.getInt("especial");
			}
			rsm.beforeFirst();

			HashMap<String, Object> regTotais = new HashMap<String, Object>();
			regTotais.put("T_LEVE1", tLeve1);
			regTotais.put("T_LEVE2", tLeve2);
			regTotais.put("T_PESADO1", tPesado1);
			regTotais.put("T_PESADO2", tPesado2);
			regTotais.put("T_ESPECIAL", tEspecial);
			regTotais.put("T_TOTAL_EQUIPAMENTO", tLeve1 + tLeve2 + tPesado1 + tPesado2 + tEspecial);

			rsm.addRegister(regTotais);

			return rsm;
		} catch (Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! EventoEquipamentoServices.statsQtInfracaoComprimentoVeiculo: " + e);
			return null;
		} finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ResultSetMap statsQtInfracaoVelocidade() {
		try {
			GregorianCalendar hoje = new GregorianCalendar();
//			GregorianCalendar dtInicial = new GregorianCalendar(hoje.get(Calendar.YEAR), hoje.get(Calendar.MONTH), 1, 0, 0, 0);
			GregorianCalendar dtInicial = new GregorianCalendar(hoje.get(Calendar.YEAR), Calendar.JANUARY, 1, 0, 0, 0);

			return statsQtInfracaoVelocidade(dtInicial, hoje, null, null);
		} catch (Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! EventoEquipamentoServices.statsQtInfracaoVelocidade: " + e);
			return null;
		}
	}

	public static ResultSetMap statsQtInfracaoVelocidade(String idOrgao) {
		try {
			GregorianCalendar hoje = new GregorianCalendar();
			GregorianCalendar dtInicial = new GregorianCalendar(hoje.get(Calendar.YEAR), hoje.get(Calendar.MONTH), 1, 0,
					0, 0);

			return statsQtInfracaoVelocidade(dtInicial, hoje, idOrgao, null);
		} catch (Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! EventoEquipamentoServices.statsQtInfracaoVelocidade: " + e);
			return null;
		}
	}

	public static ResultSetMap statsQtInfracaoVelocidade(GregorianCalendar dtInicial, GregorianCalendar dtFinal) {
		return statsQtInfracaoVelocidade(dtInicial, dtFinal, null, null);
	}

	public static ResultSetMap statsQtInfracaoVelocidade(GregorianCalendar dtInicial, GregorianCalendar dtFinal,
			String idOrgao, Connection connect) {
		boolean isConnectionNull = connect == null;
		try {
			connect = isConnectionNull ? Conexao.conectar() : connect;

			PreparedStatement ps = connect.prepareStatement("select A.cd_equipamento, C.id_orgao, "
					+ "    count(*) filter (where (100 - (CAST (vl_velocidade_tolerada as FLOAT) / CAST (vl_considerada as FLOAT)* 100)) <= 20) as menor_20, "
					+ "    count(*) filter (where (100 - (CAST (vl_velocidade_tolerada as FLOAT) / CAST (vl_considerada as FLOAT)* 100)) > 20 AND "
					+ "                           (100 - (CAST (vl_velocidade_tolerada as FLOAT) / CAST (vl_considerada as FLOAT) * 100)) <= 50) as entre_20_50, "
					+ "    count(*) filter (where (100 - (CAST (vl_velocidade_tolerada as FLOAT) / CAST (vl_considerada as FLOAT)* 100)) > 50) as maior_50 "
					+ "from mob_evento_equipamento A "
					+ "left outer join grl_equipamento b on (a.cd_equipamento = b.cd_equipamento) "
					+ "left outer join mob_orgao c on (b.cd_orgao = c.cd_orgao) " + "where A.dt_conclusao >= ? "
					+ " and A.dt_conclusao <= ? " +
					// " and A.vl_considerada >= A.vl_velocidade_tolerada " +
					" and A.cd_tipo_evento = 9 " + (idOrgao != null ? " and C.id_orgao = '" + idOrgao + "'" : "")
					+ "group by A.cd_equipamento, C.id_orgao " + "order by C.id_orgao");
			ps.setTimestamp(1, Util.convCalendarToTimestamp(dtInicial));
			ps.setTimestamp(2, Util.convCalendarToTimestamp(dtFinal));

			ResultSetMap rsm = new ResultSetMap(ps.executeQuery());

			int tMenor20 = 0;
			int tEntre2050 = 0;
			int tMaior50 = 0;

			int tASV = 0;
			int tPSF = 0;

			while (rsm.next()) {
				Equipamento equipamento = EquipamentoDAO.get(rsm.getInt("cd_equipamento"), connect);

				rsm.setValueToField("nm_equipamento", equipamento.getNmEquipamento());
				rsm.setValueToField("ds_equipamento", equipamento.getDsLocal().toUpperCase() + "\n("
						+ rsm.getString("id_orgao") + " - " + equipamento.getIdEquipamento() + ")");

				tMenor20 += rsm.getInt("menor_20");
				tEntre2050 += rsm.getInt("entre_20_50");
				tMaior50 += rsm.getInt("maior_50");

				// ASV - 2
				int asv = 0;
				ps = connect.prepareStatement(
						" SELECT COUNT(*) AS asv " + " FROM mob_evento_equipamento" + " WHERE cd_equipamento = ?"
								+ " AND dt_conclusao BETWEEN ? AND ?" + " AND cd_tipo_evento = 2");
				ps.setInt(1, equipamento.getCdEquipamento());
				ps.setTimestamp(2, Util.convCalendarToTimestamp(dtInicial));
				ps.setTimestamp(3, Util.convCalendarToTimestamp(dtFinal));

				ResultSet rs = ps.executeQuery();
				if (rs.next()) {
					asv = rs.getInt("asv");

					rsm.setValueToField("asv", asv);
					tASV += rs.getInt("asv");
				}

				// PSF - 3
				int psf = 0;
				ps = connect.prepareStatement(
						" SELECT COUNT(*) AS psf " + " FROM mob_evento_equipamento" + " WHERE cd_equipamento = ?"
								+ " AND dt_conclusao BETWEEN ? AND ?" + " AND cd_tipo_evento = 3");
				ps.setInt(1, equipamento.getCdEquipamento());
				ps.setTimestamp(2, Util.convCalendarToTimestamp(dtInicial));
				ps.setTimestamp(3, Util.convCalendarToTimestamp(dtFinal));

				rs = ps.executeQuery();
				if (rs.next()) {
					psf = rs.getInt("psf");

					rsm.setValueToField("psf", psf);
					tPSF += rs.getInt("psf");
				}

				rsm.setValueToField("total_equipamento",
						rsm.getInt("menor_20") + rsm.getInt("entre_20_50") + rsm.getInt("maior_50") + asv + psf);

			}
			rsm.beforeFirst();

			HashMap<String, Object> regTotais = new HashMap<String, Object>();
			regTotais.put("T_MENOR_20", tMenor20);
			regTotais.put("T_ENTRE_20_50", tEntre2050);
			regTotais.put("T_MAIOR_50", tMaior50);
			regTotais.put("T_ASV", tASV);
			regTotais.put("T_PSF", tPSF);
			regTotais.put("T_TOTAL_EQUIPAMENTO", tMenor20 + tEntre2050 + tMaior50 + tASV + tPSF);

			rsm.addRegister(regTotais);

			return rsm;
		} catch (Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! EventoEquipamentoServices.statsQtInfracaoVelocidade: " + e);
			return null;
		} finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	/**
	 * Sincronia do Radar Server com o banco local do municipio
	 * 
	 * @return
	 */
	public static Result scanFtpRadar() {
		return scanFtpRadar(null);
	}

	public static Result scanFtpRadar(Connection connect) {
		boolean isConnectionNull = connect == null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			Result r = new Result(-1);

			String ftpRadarPath = Util.getConfManager().getProps().getProperty("RADAR_FTP_PATH");

			if (ftpRadarPath == null || ftpRadarPath.equals("")) {
				System.out.println("Diretório de ftp para radares inválido.");
				return new Result(-2, "Diretório de ftp para radares inválido.");
			}

			/*
			 * 1. Buscar equipamentos do tipo radar que possuam sincronizaï¿½ï¿½o via FTP
			 */
			ResultSetMap rsmEquipamentos = EquipamentoServices.getAllRadarFtp(connect);

			if (rsmEquipamentos.size() == 0) {
				System.out.println("Nenhum radar registrado com sincronizaï¿½ï¿½o por FTP.");
				return new Result(-3, "Nenhum radar registrado com sincronizaï¿½ï¿½o por FTP.");
			}

			while (rsmEquipamentos.next()) {

				System.out.println("Sync. Equip.: " + rsmEquipamentos.getString("ID_EQUIPAMENTO"));

				File directory = new File(ftpRadarPath + "/" + rsmEquipamentos.getString("ID_EQUIPAMENTO"));
				if (!directory.exists())
					System.out.println("Nao existe diretorio para este equipamento.");

				/*
				 * 1. ERROS 2. INFRACOES 3. TRAFEGO
				 */

				// 1.ERROS

				// 2.INFRACOES
				File[] files = new File[0];
				directory = new File(
						ftpRadarPath + "/" + rsmEquipamentos.getString("ID_EQUIPAMENTO") + "/DADOS/INFRACAO");

				if (!directory.exists())
					System.out.println("Nao existe diretorio de infracao para este equipamento.");
				else {
					files = directory.listFiles();
					System.out.println("\tQt. Infrac.: " + files.length);
				}

				File dstPathProcesamento = new File(
						ftpRadarPath + "/" + rsmEquipamentos.getString("ID_EQUIPAMENTO") + "/PROCESSADO");

				if (!dstPathProcesamento.exists()) {
					System.out.println("Nao existe diretorio de processamento para este equipamento. Criando...");

					boolean dstPathCreation = dstPathProcesamento.mkdir();
					System.out.println(dstPathCreation ? "Criado diretório de processamento..."
							: "Não foi possível criar '" + dstPathProcesamento.getPath() + "'");

					if (!dstPathCreation) {
						System.out.println("Interrompendo processo de sincronizaï¿½ï¿½o...");
						break;
					}
				}

				File dstPath = new File(
						ftpRadarPath + "/" + rsmEquipamentos.getString("ID_EQUIPAMENTO") + "/PROCESSADO/INFRACAO");

				if (!dstPath.exists()) {
					System.out
							.println("Nao existe diretorio de infracoes processadas para este equipamento. Criando...");

					boolean dstPathCreation = dstPath.mkdir();
					System.out.println(dstPathCreation ? "Criado diretorio de infracoes processadas..."
							: "Não foi possível criar '" + dstPath.getPath() + "'");

					if (!dstPathCreation) {
						System.out.println("Interrompendo processo de sincronizaï¿½ï¿½o...");
						break;
					}
				}

				if (files.length == 0) {
					System.out.println("Nenhum arquivo para processar...");

					EventoEquipamentoMonitorServices
							.checkInfracao(ftpRadarPath + "/" + rsmEquipamentos.getString("ID_EQUIPAMENTO"));

					r = new Result(1);
				} else {
					for (int i = 0; i < files.length; i++) {

						r = new Result(-1);

						// ï¿½ um arquivo do tipo .zip
						if (files[i].isFile() && files[i].getName().endsWith("zip")) {

							// PROCESSA ARQUIVO DE ACORDO COM O TIPO DO EVENTO
							if (files[i].getName().endsWith("Avanco_Sinal.zip")) { // ASV
								r = processarASV(files[i], rsmEquipamentos.getInt("cd_equipamento"), connect);
							} else if (files[i].getName().endsWith("Parada_Faixa.zip")) { // PSF
								r = processarPSF(files[i], rsmEquipamentos.getInt("cd_equipamento"), connect);
							} else { // VAL
								r = processarVAL(files[i], rsmEquipamentos.getInt("cd_equipamento"), connect);
							}

							if (r.getCode() < 0) {
								break;
							}
						}

						// movendo arquivo
						if (r.getCode() > 0) {
							System.out.println("Movendo arquivo '" + files[i].toPath() + "' para '"
									+ Paths.get(dstPath.getPath() + "/" + files[i].getName()) + "'");
							Files.move(files[i].toPath(), Paths.get(dstPath.getPath() + "/" + files[i].getName()),
									StandardCopyOption.ATOMIC_MOVE);
						}
					}
				}

				if (r.getCode() <= 0) {
					System.out.println("Erro ao sync. Equip.: " + rsmEquipamentos.getString("ID_EQUIPAMENTO"));
					break;
				}

			}

			if (r.getCode() <= 0) {
				Conexao.rollback(connect);
				System.out.println("Erro ao sincronizar radares por FTP.");
			} else if (isConnectionNull) {
				connect.commit();
				System.out.println("Radares sincronizados por FTP com sucesso.");
			}

			return new Result(r.getCode(), r.getCode() <= 0 ? "Erro ao sincronizar radares por FTP."
					: "Radares sincronizados por FTP com sucesso.");
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("Erro! EventoEquipamentoServices.scanFtpRadar: " + e);
			return new Result(-1, "Erro ao gravar fotos de evento.");
		} finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static Result scanFtpTrafegoRadar() {
		return scanFtpTrafegoRadar(null);
	}

	public static Result scanFtpTrafegoRadar(Connection connect) {
		boolean isConnectionNull = connect == null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			Result r = new Result(-1);

			String ftpRadarPath = Util.getConfManager().getProps().getProperty("RADAR_FTP_PATH");

			if (ftpRadarPath == null || ftpRadarPath.equals("")) {
				System.out.println("Diretório de ftp para radares inválido.");
				return new Result(-2, "Diretório de ftp para radares inválido.");
			}

			/*
			 * 1. Buscar equipamentos do tipo radar que possuam sincronizaï¿½ï¿½o via FTP
			 */
			ResultSetMap rsmEquipamentos = EquipamentoServices.getAllRadarFtp(connect);

			if (rsmEquipamentos.size() == 0) {
				System.out.println("Nenhum radar registrado com sincronização por FTP.");
				return new Result(-3, "Nenhum radar registrado com sincronização por FTP.");
			}

			while (rsmEquipamentos.next()) {

				System.out.println("Sync. Equip.: " + rsmEquipamentos.getString("ID_EQUIPAMENTO"));

				File directory = new File(ftpRadarPath + "/" + rsmEquipamentos.getString("ID_EQUIPAMENTO"));
				if (!directory.exists())
					System.out.println("Nao existe diretorio para este equipamento.");

				File[] files = new File[0];

				File dstPathProcesamento = new File(
						ftpRadarPath + "/" + rsmEquipamentos.getString("ID_EQUIPAMENTO") + "/PROCESSADO");

				if (!dstPathProcesamento.exists()) {
					System.out.println("Nao existe diretorio de processamento para este equipamento. Criando...");

					boolean dstPathCreation = dstPathProcesamento.mkdir();
					System.out.println(dstPathCreation ? "Criado diretório de processamento..."
							: "Não foi possível criar '" + dstPathProcesamento.getPath() + "'");

					if (!dstPathCreation) {
						System.out.println("Interrompendo processo de sincronizaï¿½ï¿½o...");
						break;
					}
				}

				File dstPath = new File(
						ftpRadarPath + "/" + rsmEquipamentos.getString("ID_EQUIPAMENTO") + "/PROCESSADO/TRAFEGO");

				// 3.TRAFEGO
				try {
					directory = new File(
							ftpRadarPath + "/" + rsmEquipamentos.getString("ID_EQUIPAMENTO") + "/DADOS/TRAFEGO");
					if (!directory.exists())
						System.out.println("Nao existe diretorio de trafego para este equipamento.");
					else {
						files = directory.listFiles();

						System.out.println("Processando tráfego de " + rsmEquipamentos.getString("ID_EQUIPAMENTO"));

						if (files.length > 0) {
							System.out.println("\tQt. Traf.: " + files.length);
							r = MedicaoTrafegoServices.processarArquivos(files,
									rsmEquipamentos.getInt("CD_EQUIPAMENTO"), connect);

							if (r.getCode() <= 0) {
								System.out.println("Erro ao processar trï¿½fego de Equip.: "
										+ rsmEquipamentos.getString("ID_EQUIPAMENTO"));
								break;
							}

							// MOVENDO ARQUIVOS
							dstPath = new File(ftpRadarPath + "/" + rsmEquipamentos.getString("ID_EQUIPAMENTO")
									+ "/PROCESSADO/TRAFEGO");
							System.out.println("\tMovendo arquivos de tráfego...");
							for (File file : files) {
								Files.move(file.toPath(), Paths.get(dstPath.getPath() + "/" + file.getName()),
										StandardCopyOption.ATOMIC_MOVE);
							}
						}
					}
				} catch (Exception e) {
					System.out.println(
							"!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
					System.out.println("\tErro ao registrar tráfego de " + rsmEquipamentos.getString("ID_EQUIPAMENTO"));
					System.out.println(
							"!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
				}
			}

			if (r.getCode() <= 0) {
				Conexao.rollback(connect);
				System.out.println("Erro ao sincronizar radares por FTP.");
			} else if (isConnectionNull) {
				connect.commit();
				System.out.println("Trï¿½fego sincronizado por FTP com sucesso.");
			}

			return new Result(r.getCode(), r.getCode() <= 0 ? "Erro ao sincronizar radares por FTP."
					: "Radares sincronizados por FTP com sucesso.");
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("Erro! EventoEquipamentoServices.scanFtpTrafegoRadar: " + e);
			return new Result(-1, "Erro ao gravar fotos de evento.");
		} finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static Result scanFtpEventoTrafegoRadar() {
		return scanFtpEventoTrafegoRadar(null);
	}

	public static Result scanFtpEventoTrafegoRadar(Connection connect) {
		boolean isConnectionNull = connect == null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			Result r = new Result(-1);

			String ftpRadarPath = Util.getConfManager().getProps().getProperty("RADAR_FTP_PATH");

			if (ftpRadarPath == null || ftpRadarPath.equals("")) {
				System.out.println("Diretório de ftp para radares inválido.");
				return new Result(-2, "Diretório de ftp para radares inválido.");
			}

			/*
			 * 1. Buscar equipamentos do tipo radar que possuam sincronizaï¿½ï¿½o via FTP
			 */
			ResultSetMap rsmEquipamentos = EquipamentoServices.getAllRadarFtp(connect);

			if (rsmEquipamentos.size() == 0) {
				System.out.println("Nenhum radar registrado com sincronização por FTP.");
				return new Result(-3, "Nenhum radar registrado com sincronização por FTP.");
			}

			while (rsmEquipamentos.next()) {

				System.out.println("Sync. Equip.: " + rsmEquipamentos.getString("ID_EQUIPAMENTO"));

				File directory = new File(ftpRadarPath + "/" + rsmEquipamentos.getString("ID_EQUIPAMENTO"));
				if (!directory.exists())
					System.out.println("Nao existe diretorio para este equipamento.");

				File[] files = new File[0];

				File dstPathProcesamento = new File(
						ftpRadarPath + "/" + rsmEquipamentos.getString("ID_EQUIPAMENTO") + "/PROCESSADO");

				if (!dstPathProcesamento.exists()) {
					System.out.println("Nao existe diretorio de processamento para este equipamento. Criando...");

					boolean dstPathCreation = dstPathProcesamento.mkdir();
					System.out.println(dstPathCreation ? "Criado diretório de processamento..."
							: "Não foi possível criar '" + dstPathProcesamento.getPath() + "'");

					if (!dstPathCreation) {
						System.out.println("Interrompendo processo de sincronização...");
						break;
					}
				}

				File dstPath = new File(
						ftpRadarPath + "/" + rsmEquipamentos.getString("ID_EQUIPAMENTO") + "/PROCESSADO/FOTOS");

				// 3.TRAFEGO
				try {

					directory = new File(
							ftpRadarPath + "/" + rsmEquipamentos.getString("ID_EQUIPAMENTO") + "/DADOS/FOTOS");
					if (!directory.exists()) {
						System.out.println("Nao existe diretorio de trafego para este equipamento.");
					} else {
						files = directory.listFiles();

						System.out.println("Processando tráfego de " + rsmEquipamentos.getString("ID_EQUIPAMENTO"));

						System.out.println("files.length: " + files.length);
						System.out.println("\t" + directory.getAbsolutePath());

						if (files.length > 0) {
							System.out.println("\tQt. Traf.: " + files.length);

							for (int i = 0; i < files.length; i++) {
								if (files[i].isFile() && files[i].getName().endsWith("zip")) {
									r = processarIMT(files[i], rsmEquipamentos.getInt("CD_EQUIPAMENTO"), connect);

									if (r.getCode() <= 0) {
										System.out.println("Erro ao processar trï¿½fego de Equip.: "
												+ rsmEquipamentos.getString("ID_EQUIPAMENTO"));
										break;
									}
								}
							}

							// MOVENDO ARQUIVOS
							dstPath = new File(ftpRadarPath + "/" + rsmEquipamentos.getString("ID_EQUIPAMENTO")
									+ "/PROCESSADO/FOTOS");
							System.out.println("\tMovendo arquivos de tráfego...");
							for (File file : files) {
								Files.move(file.toPath(), Paths.get(dstPath.getPath() + "/" + file.getName()),
										StandardCopyOption.ATOMIC_MOVE);
							}
						}
					}
				} catch (Exception e) {
					System.out.println(
							"!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
					System.out.println("\tErro ao registrar tráfego de " + rsmEquipamentos.getString("ID_EQUIPAMENTO"));
					System.out.println(
							"!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
					e.printStackTrace(System.out);
				}
			}

			if (r.getCode() <= 0) {
				Conexao.rollback(connect);
				System.out.println("Erro ao sincronizar radares por FTP.");
			} else if (isConnectionNull) {
				connect.commit();
				System.out.println("Tráfego sincronizado por FTP com sucesso.");
			}

			return new Result(r.getCode(), r.getCode() <= 0 ? "Erro ao sincronizar radares por FTP."
					: "Radares sincronizados por FTP com sucesso.");
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("Erro! EventoEquipamentoServices.scanFtpTrafegoRadar: " + e);
			return new Result(-1, "Erro ao gravar fotos de evento.");
		} finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	/**
	 * Processa eventos de PSF
	 * 
	 * @param Arquivo       .zip do evento
	 * @param cdEquipamento cï¿½digo do equipamento
	 * @param connect
	 * @return
	 * @throws Exception
	 */
	private static Result processarPSF(File file, int cdEquipamento, Connection connect) throws Exception {

		System.out.println("\tProcessando arquivo " + file.getName());

		Result r = new Result(1);

		// PARAMETRIZAR O TIPO DE EVENTO VAL
		TipoEvento tipoEvento = TipoEventoServices.getByIdTipoEvento("PSF", connect);

		ZipInputStream zis = new ZipInputStream(new FileInputStream(file));
		ZipEntry zipEntry = zis.getNextEntry();

		TimeZone timezone = TimeZone.getTimeZone("America/Recife");
		GregorianCalendar dtEntry = new GregorianCalendar(timezone);

		EventoEquipamento evento = null;
		List<Arquivo> fotos = new ArrayList<Arquivo>();
		List<Arquivo> videos = new ArrayList<Arquivo>();

		while (zipEntry != null) {

			if (zipEntry.getName().endsWith("_Pan00.jpg")) {
				// dtEntry.setTimeInMillis(zipEntry.getTime());

				// yyyyMMdd_HHmmss_SSSS_1.1.jpg
				String str = zipEntry.getName();
				String dateString = str.split("_")[0] + "_" + str.split("_")[1];
				SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd_HHmmss");
				Date date = formatter.parse(dateString);

				dtEntry.setTimeInMillis(date.getTime());
			}

			if (zipEntry.getName().endsWith("_Pan00.jpg") || zipEntry.getName().endsWith("_Priv00.jpg")
					|| zipEntry.getName().endsWith(".mp4") || zipEntry.getName().endsWith(".xml")) {

				System.out.println("\t\t- " + zipEntry.getName());

				byte[] buffer = new byte[1024];
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				int len;
				while ((len = zis.read(buffer)) > 0) {
					baos.write(buffer, 0, len);
				}
				baos.close();

				byte[] fileContent = baos.toByteArray();

				// gravar imagens
				if (zipEntry.getName().endsWith("jpg")) {
					Arquivo arquivo = new Arquivo();
					arquivo.setCdArquivo(0);
					arquivo.setNmArquivo(zipEntry.getName());
					arquivo.setBlbArquivo(fileContent);
					arquivo.setDtArquivamento(new GregorianCalendar());
					arquivo.setDtCriacao(dtEntry);

					fotos.add(arquivo);
				}
				// gravar videos
				if (zipEntry.getName().endsWith("mp4")) {
					Arquivo arquivo = new Arquivo();
					arquivo.setCdArquivo(0);
					arquivo.setNmArquivo(zipEntry.getName());
					arquivo.setBlbArquivo(fileContent);
					arquivo.setDtArquivamento(new GregorianCalendar());
					arquivo.setDtCriacao(dtEntry);

					videos.add(arquivo);
				}

				// gravar evento
				if (zipEntry.getName().endsWith("xml")) {

					// 2. GRAVAR EVENTO
					evento = new EventoEquipamento();

					evento.setCdEquipamento(cdEquipamento);

					ByteArrayInputStream bais = new ByteArrayInputStream(fileContent);

					DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
					DocumentBuilder parser = factory.newDocumentBuilder();
					Document document = parser.parse(new InputSource(bais));

					NodeList infracaoNodeList = document.getElementsByTagName("INFRACAO");
					for (int j = 0; j < infracaoNodeList.getLength(); j++) {
						Node currentNode = infracaoNodeList.item(j);

						if (currentNode.getNodeType() == Node.ELEMENT_NODE) {

							NodeList nodeList = currentNode.getChildNodes();

							for (int k = 0; k < nodeList.getLength(); k++) {

								Node node = nodeList.item(k);

								if (node.getNodeType() == Node.ELEMENT_NODE) {

									switch (node.getNodeName()) {
									case "NomeEquipamento":
										evento.setNmEquipamento(node.getTextContent());
										break;
									case "TipoClassificacao":

										break;
									case "IdentificacaoINMETRO":
										evento.setIdIdentificacaoInmetro(node.getTextContent());
										break;
									case "LocalInstalacao":
										evento.setDsLocal(node.getTextContent());
										break;
									case "Cidade":

										break;
									case "UF":
										break;
									case "Sentido":
										evento.setNmSentidoRodovia(node.getTextContent());
										break;
									case "Marca":
										evento.setNmMarcaEquipamento(node.getTextContent());
										break;
									case "Modelo":
										evento.setNmModeloEquipamento(node.getTextContent());
										break;
									case "NumSerie":
										evento.setNrSerial(node.getTextContent());
										break;
									case "TipoEquipamento":
										evento.setTpEquipamento(EquipamentoServices.RADAR_FIXO);
										break;
									case "OrgAtuador":
									case "OrgAutuador":
										evento.setNmOrgaoAutuador(node.getTextContent());
										break;
									case "OCR_fotos":
										evento.setNrPlaca(node.getTextContent());
										break;
									case "CodigoCidade":

										break;
									case "Comprimento":
										float vlComprimento = new Float(node.getTextContent());

										evento.setVlComprimentoVeiculo(Math.round(vlComprimento));

										/*
										 * 0 - 2.99 - Leve 1 (Ex: Moto) 3 - 5.99 - Leve 2 6 - 14.99 - Pesado 1 15 -
										 * 18.99 - Pesado 2 > 19 - Especial
										 */
										if (vlComprimento < 3)
											evento.setTpVeiculo(0);
										else if (vlComprimento >= 3 && vlComprimento < 6)
											evento.setTpVeiculo(1);
										else if (vlComprimento >= 6 && vlComprimento < 15)
											evento.setTpVeiculo(2);
										else if (vlComprimento >= 15 && vlComprimento < 19)
											evento.setTpVeiculo(3);
										else if (vlComprimento >= 19)
											evento.setTpVeiculo(4);

										break;
									case "IdentificacaoMedida":

										break;
									case "Faixa":
										evento.setNrPista(new Integer(node.getTextContent()));
										break;
									case "TempoTolerancia":
										float vlVelocidadeLimite = new Float(node.getTextContent());
										evento.setVlLimite(Math.round(vlVelocidadeLimite));
										evento.setVlVelocidadeTolerada(Math.round(vlVelocidadeLimite));
										break;
									case "TempoPermanecia":
										float vlVelocidadeMedida = new Float(node.getTextContent());
										evento.setVlMedida(Math.round(vlVelocidadeMedida));
										evento.setVlConsiderada(Math.round(vlVelocidadeMedida));
										break;
									case "DataAfericao":
										evento.setDtAfericao(dtEntry);
										break;

									default:
										System.out.println("\t\t\tCAMPO INDEFINIDO: " + node.getNodeName());
									}

								}
							}
						}
					}

					if (tipoEvento != null)
						evento.setCdTipoEvento(tipoEvento.getCdTipoEvento());

					evento.setNmEquipamento(evento.getNrSerial());

					evento.setLgTempoReal(0);
					evento.setDtConclusao(dtEntry);
					evento.setDtEvento(new GregorianCalendar());
				}
			}

			zipEntry = zis.getNextEntry();
		}

		System.out.println("\tSalvando evento...");
		r = save(evento, null, connect);

		if (r.getCode() > 0) {

			System.out.println("\tSalvando arquivos do evento...");
			evento.setCdEvento(((EventoEquipamento) r.getObjects().get("EVENTOEQUIPAMENTO")).getCdEvento());
			r = saveArquivosEventoFtp(evento, fotos, EventoArquivoServices.TP_ARQUIVO_FOTO, connect);
			r = saveArquivosEventoFtp(evento, videos, EventoArquivoServices.TP_ARQUIVO_VIDEO, connect);

		} else {
			System.out.println("\tErro ao salvar evento...");

			zis.closeEntry();
			zis.close();

			return new Result(-1, "Erro ao salvar evento...");
		}

		zis.closeEntry();
		zis.close();

		return r;
	}

	/**
	 * Processa eventos de ASV
	 * 
	 * @param Arquivo       .zip do evento
	 * @param cdEquipamento cï¿½digo do equipamento
	 * @param connect
	 * @return
	 * @throws Exception
	 */
	private static Result processarASV(File file, int cdEquipamento, Connection connect) throws Exception {

		System.out.println("\tProcessando arquivo " + file.getName());

		Result r = new Result(1);

		// PARAMETRIZAR O TIPO DE EVENTO VAL
		TipoEvento tipoEvento = TipoEventoServices.getByIdTipoEvento("ASV", connect);

		ZipInputStream zis = new ZipInputStream(new FileInputStream(file));
		ZipEntry zipEntry = zis.getNextEntry();

		TimeZone timezone = TimeZone.getTimeZone("America/Recife");
		GregorianCalendar dtEntry = new GregorianCalendar(timezone);

		EventoEquipamento evento = null;
		List<Arquivo> fotos = new ArrayList<Arquivo>();
		List<Arquivo> videos = new ArrayList<Arquivo>();

		while (zipEntry != null) {

			if (zipEntry.getName().endsWith("_Pan00.jpg")) {
				// dtEntry.setTimeInMillis(zipEntry.getTime());

				// yyyyMMdd_HHmmss_SSSS_1.1.jpg
				String str = zipEntry.getName();
				String dateString = str.split("_")[0] + "_" + str.split("_")[1];
				SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd_HHmmss");
				Date date = formatter.parse(dateString);

				dtEntry.setTimeInMillis(date.getTime());
			}

			if (zipEntry.getName().endsWith("_Pan00.jpg") || zipEntry.getName().endsWith("_Priv00.jpg")
					|| zipEntry.getName().endsWith(".mp4") || zipEntry.getName().endsWith(".xml")) {

				System.out.println("\t\t- " + zipEntry.getName());

				byte[] buffer = new byte[1024];
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				int len;
				while ((len = zis.read(buffer)) > 0) {
					baos.write(buffer, 0, len);
				}
				baos.close();

				byte[] fileContent = baos.toByteArray();

				// gravar imagens
				if (zipEntry.getName().endsWith("jpg")) {
					Arquivo arquivo = new Arquivo();
					arquivo.setCdArquivo(0);
					arquivo.setNmArquivo(zipEntry.getName());
					arquivo.setBlbArquivo(fileContent);
					arquivo.setDtArquivamento(new GregorianCalendar());
					arquivo.setDtCriacao(dtEntry);

					fotos.add(arquivo);
				}
				// gravar videos
				if (zipEntry.getName().endsWith("mp4")) {
					Arquivo arquivo = new Arquivo();
					arquivo.setCdArquivo(0);
					arquivo.setNmArquivo(zipEntry.getName());
					arquivo.setBlbArquivo(fileContent);
					arquivo.setDtArquivamento(new GregorianCalendar());
					arquivo.setDtCriacao(dtEntry);

					videos.add(arquivo);
				}

				// gravar evento
				if (zipEntry.getName().endsWith("xml")) {

					// 2. GRAVAR EVENTO
					evento = new EventoEquipamento();

					evento.setCdEquipamento(cdEquipamento);

					// evento.setTpClassificacao(item.getClassificacao());
					// evento.setNmKmRodovia(item.getKmRodovia());
					// evento.setNmUfRodovia(item.getUfRodovia());
					// evento.setIdMedida(item.getIdMedida());
					// evento.setVlVelocidadeTolerada(item.getVelocidadeTolerada());

					ByteArrayInputStream bais = new ByteArrayInputStream(fileContent);

					DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
					DocumentBuilder parser = factory.newDocumentBuilder();
					Document document = parser.parse(new InputSource(bais));

					NodeList infracaoNodeList = document.getElementsByTagName("INFRACAO");
					for (int j = 0; j < infracaoNodeList.getLength(); j++) {
						Node currentNode = infracaoNodeList.item(j);

						if (currentNode.getNodeType() == Node.ELEMENT_NODE) {

							NodeList nodeList = currentNode.getChildNodes();

							for (int k = 0; k < nodeList.getLength(); k++) {

								Node node = nodeList.item(k);

								if (node.getNodeType() == Node.ELEMENT_NODE) {

									switch (node.getNodeName()) {
									case "NomeEquipamento":
										evento.setNmEquipamento(node.getTextContent());
										break;
									case "TipoClassificacao":

										break;
									case "IdentificacaoINMETRO":
										evento.setIdIdentificacaoInmetro(node.getTextContent());
										break;
									case "LocalInstalacao":
										evento.setDsLocal(node.getTextContent());
										break;
									case "Cidade":

										break;
									case "UF":
										break;
									case "Sentido":
										evento.setNmSentidoRodovia(node.getTextContent());
										break;
									case "Marca":
										evento.setNmMarcaEquipamento(node.getTextContent());
										break;
									case "Modelo":
										evento.setNmModeloEquipamento(node.getTextContent());
										break;
									case "NumSerie":
										evento.setNrSerial(node.getTextContent());
										break;
									case "TipoEquipamento":
										evento.setTpEquipamento(EquipamentoServices.RADAR_FIXO);
										break;
									case "OrgAtuador":
									case "OrgAutuador":
										evento.setNmOrgaoAutuador(node.getTextContent());
										break;
									case "OCR_fotos":
										evento.setNrPlaca(node.getTextContent());
										break;
									case "CodigoCidade":

										break;
									case "Comprimento":
										float vlComprimento = new Float(node.getTextContent());

										evento.setVlComprimentoVeiculo(Math.round(vlComprimento));

										/*
										 * 0 - 2.99 - Leve 1 (Ex: Moto) 3 - 5.99 - Leve 2 6 - 14.99 - Pesado 1 15 -
										 * 18.99 - Pesado 2 > 19 - Especial
										 */
										if (vlComprimento < 3)
											evento.setTpVeiculo(0);
										else if (vlComprimento >= 3 && vlComprimento < 6)
											evento.setTpVeiculo(1);
										else if (vlComprimento >= 6 && vlComprimento < 15)
											evento.setTpVeiculo(2);
										else if (vlComprimento >= 15 && vlComprimento < 19)
											evento.setTpVeiculo(3);
										else if (vlComprimento >= 19)
											evento.setTpVeiculo(4);

										break;
									case "IdentificacaoMedida":

										break;
									case "Faixa":
										evento.setNrPista(new Integer(node.getTextContent()));
										break;
//		    	    	        		case "VelocidadeConsiderada":
//		    	    	        			float vlVelocidadeConsiderada = new Float(node.getTextContent());
//		    	    	        			evento.setVlConsiderada(Math.round(vlVelocidadeConsiderada));
//		    	    	        			break;
									case "TempoTolerancia":
										float vlVelocidadeLimite = new Float(node.getTextContent());
										evento.setVlLimite(Math.round(vlVelocidadeLimite));
										break;
									case "TempoMedido":
										float vlVelocidadeMedida = new Float(node.getTextContent());
										evento.setVlMedida(Math.round(vlVelocidadeMedida));
										break;
									case "DataAfericao":
										evento.setDtAfericao(dtEntry);
										break;

									default:
										System.out.println("\t\t\tCAMPO INDEFINIDO: " + node.getNodeName());
									}

								}
							}
						}
					}

					if (tipoEvento != null)
						evento.setCdTipoEvento(tipoEvento.getCdTipoEvento());

					evento.setNmEquipamento(evento.getNrSerial());

					evento.setLgTempoReal(0);
					evento.setDtConclusao(dtEntry);
					evento.setDtEvento(new GregorianCalendar());

					// System.out.println("\t\t\t"+evento);

				}

			}

			zipEntry = zis.getNextEntry();
		}

		System.out.println("\tSalvando evento...");
		r = save(evento, null, connect);

		if (r.getCode() > 0) {

			System.out.println("\tSalvando arquivos do evento...");
			evento.setCdEvento(((EventoEquipamento) r.getObjects().get("EVENTOEQUIPAMENTO")).getCdEvento());
			r = saveArquivosEventoFtp(evento, fotos, EventoArquivoServices.TP_ARQUIVO_FOTO, connect);
			r = saveArquivosEventoFtp(evento, videos, EventoArquivoServices.TP_ARQUIVO_VIDEO, connect);

		} else {
			System.out.println("\tErro ao salvar evento...");

			zis.closeEntry();
			zis.close();

			return new Result(-1, "Erro ao salvar evento...");
		}

		zis.closeEntry();
		zis.close();

		return r;
	}

	/**
	 * Processa eventos de VAL
	 * 
	 * @param Arquivo       .zip do evento
	 * @param cdEquipamento cï¿½digo do equipamento
	 * @param connect
	 * @return
	 * @throws Exception
	 */
	private static Result processarVAL(File file, int cdEquipamento, Connection connect) throws Exception {

		System.out.println("\tProcessando arquivo " + file.getName());

		AlprService alprService = AlprFactory.get(ManagerConf.getInstance().get("ALPR"));
		String nrPlaca = null;

		Result r = new Result(1);

		// PARAMETRIZAR O TIPO DE EVENTO VAL
		TipoEvento tipoEvento = TipoEventoServices.getByIdTipoEvento("VAL", connect);

		ZipInputStream zis = new ZipInputStream(new FileInputStream(file));
		ZipEntry zipEntry = zis.getNextEntry();

		TimeZone timezone = TimeZone.getTimeZone("America/Recife");
		GregorianCalendar dtEntry = new GregorianCalendar(timezone);

		EventoEquipamento evento = null;
		List<Arquivo> fotos = new ArrayList<Arquivo>();

		while (zipEntry != null) {

			if (zipEntry.getName().endsWith(".1.jpg")) {
				// dtEntry.setTimeInMillis(zipEntry.getTime());

				// yyyyMMdd_HHmmss_SSSS_1.1.jpg
				String str = zipEntry.getName();
				String dateString = str.split("_")[0] + "_" + str.split("_")[1];
				SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd_HHmmss");
				Date date = formatter.parse(dateString);

				dtEntry.setTimeInMillis(date.getTime());
			}

			if (zipEntry.getName().endsWith(".1.tarja.jpg") || zipEntry.getName().endsWith(".2.tarja.jpg")
					|| zipEntry.getName().endsWith(".xml")) {

				System.out.println("\t\t- " + zipEntry.getName());

				byte[] buffer = new byte[1024];
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				int len;
				while ((len = zis.read(buffer)) > 0) {
					baos.write(buffer, 0, len);
				}
				baos.close();

				byte[] fileContent = baos.toByteArray();

				// gravar imagens
				if (zipEntry.getName().endsWith("jpg")) {
					Arquivo arquivo = new Arquivo();
					arquivo.setCdArquivo(0);
					arquivo.setNmArquivo(zipEntry.getName());
					arquivo.setBlbArquivo(fileContent);
					arquivo.setDtArquivamento(new GregorianCalendar());
					arquivo.setDtCriacao(dtEntry);

					fotos.add(arquivo);
				}

				// ALPR
				if (alprService != null) {
					try {
						if (zipEntry.getName().endsWith("1.tarja.jpg") && nrPlaca == null) {
							AlprResult result = alprService.recognize(fileContent, zipEntry.getName());
							if (result != null) {
								nrPlaca = result.getPlate();
							}
						}

					} catch (IOException e) {
					}
				}

				// gravar evento
				if (zipEntry.getName().endsWith("xml")) {

					// 2. GRAVAR EVENTO
					evento = new EventoEquipamento();

					evento.setCdEquipamento(cdEquipamento);

					ByteArrayInputStream bais = new ByteArrayInputStream(fileContent);

					DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
					DocumentBuilder parser = factory.newDocumentBuilder();
					Document document = parser.parse(new InputSource(bais));

					NodeList infracaoNodeList = document.getElementsByTagName("INFRACAO");
					for (int j = 0; j < infracaoNodeList.getLength(); j++) {
						Node currentNode = infracaoNodeList.item(j);

						if (currentNode.getNodeType() == Node.ELEMENT_NODE) {

							NodeList nodeList = currentNode.getChildNodes();

							for (int k = 0; k < nodeList.getLength(); k++) {

								Node node = nodeList.item(k);

								if (node.getNodeType() == Node.ELEMENT_NODE) {

									switch (node.getNodeName()) {
									case "NomeEquipamento":
										evento.setNmEquipamento(node.getTextContent());
										break;
									case "TipoClassificacao":

										break;
									case "IdentificacaoINMETRO":
										evento.setIdIdentificacaoInmetro(node.getTextContent());
										break;
									case "LocalInstalacao":
										evento.setDsLocal(node.getTextContent());
										break;
									case "Cidade":

										break;
									case "UF":
										break;
									case "Sentido":
										evento.setNmSentidoRodovia(node.getTextContent());
										break;
									case "Marca":
										evento.setNmMarcaEquipamento(node.getTextContent());
										break;
									case "Modelo":
										evento.setNmModeloEquipamento(node.getTextContent());
										break;
									case "NumSerie":
										evento.setNrSerial(node.getTextContent());
										break;
									case "TipoEquipamento":
										evento.setTpEquipamento(EquipamentoServices.RADAR_FIXO);
										break;
									case "OrgAtuador":
									case "OrgAutuador":
										evento.setNmOrgaoAutuador(node.getTextContent());
										break;
									case "OCR_fotos":
										String[] placas = node.getTextContent().split("\\|");
										if (placas != null && placas.length > 1) {
											for (int i = 0; i < placas.length; i++) {
												String placa = placas[i];
												System.out.println("\t\tOCR PLACA: " + placa);
												if (placa != null && placa != "") {
													evento.setNrPlaca(placa);
												}
											}
										} else {
											evento.setNrPlaca(node.getTextContent().replaceAll("\\|", ""));
										}

										break;
									case "CodigoCidade":

										break;
									case "Comprimento":
										float vlComprimento = new Float(node.getTextContent());

										evento.setVlComprimentoVeiculo(Math.round(vlComprimento));

										/*
										 * 0 - 2.99 - Leve 1 (Ex: Moto) 3 - 5.99 - Leve 2 6 - 14.99 - Pesado 1 15 -
										 * 18.99 - Pesado 2 > 19 - Especial
										 */
										if (vlComprimento < 3)
											evento.setTpVeiculo(0);
										else if (vlComprimento >= 3 && vlComprimento < 6)
											evento.setTpVeiculo(1);
										else if (vlComprimento >= 6 && vlComprimento < 15)
											evento.setTpVeiculo(2);
										else if (vlComprimento >= 15 && vlComprimento < 19)
											evento.setTpVeiculo(3);
										else if (vlComprimento >= 19)
											evento.setTpVeiculo(4);

										break;
									case "IdentificacaoMedida":

										break;
									case "Faixa":
										evento.setNrPista(new Integer(node.getTextContent()));
										break;
									case "VelocidadeConsiderada":
										float vlVelocidadeConsiderada = new Float(node.getTextContent());
										evento.setVlConsiderada((int) vlVelocidadeConsiderada);
										break;
									case "VelocidadeLimite":
										float vlVelocidadeLimite = new Float(node.getTextContent());
										evento.setVlLimite((int) vlVelocidadeLimite);

										// tolerada
										evento.setVlVelocidadeTolerada(Math.round(vlVelocidadeLimite) + 7);
										break;
									case "VelocidadeMedida":
										float vlVelocidadeMedida = new Float(node.getTextContent());
										evento.setVlMedida((int) vlVelocidadeMedida);
										break;
									case "DataAfericao":
										evento.setDtAfericao(dtEntry);
										break;

									default:
										System.out.println("\t\t\tCAMPO INDEFINIDO: " + node.getNodeName());
									}

								}
							}
						}
					}

					if (tipoEvento != null)
						evento.setCdTipoEvento(tipoEvento.getCdTipoEvento());

					evento.setNmEquipamento(evento.getNrSerial());

					if (nrPlaca != null) {
						System.out.println("\t\t\tOpenALPR: " + nrPlaca);
						evento.setNrPlaca(nrPlaca);
					}

					evento.setLgTempoReal(0);
					evento.setDtConclusao(dtEntry);
					evento.setDtEvento(new GregorianCalendar());

					// System.out.println("\t\t\t"+evento);

				}

			}

			zipEntry = zis.getNextEntry();
		}

		System.out.println("\tSalvando evento...");
		r = save(evento, null, connect);

		if (r.getCode() > 0) {

			System.out.println("\tSalvando imagens do evento...");
			evento.setCdEvento(((EventoEquipamento) r.getObjects().get("EVENTOEQUIPAMENTO")).getCdEvento());
			r = saveArquivosEventoFtp(evento, fotos, EventoArquivoServices.TP_ARQUIVO_FOTO, connect);

		} else {
			System.out.println("\tErro ao salvar evento...");

			zis.closeEntry();
			zis.close();

			return new Result(-1, "Erro ao salvar evento...");
		}

		zis.closeEntry();
		zis.close();

		return r;
	}

	private static Result processarIMT(File file, int cdEquipamento, Connection connect) throws Exception {

		System.out.println("\tProcessando arquivo " + file.getName());

		AlprService alprService = AlprFactory.get(ManagerConf.getInstance().get("ALPR"));
		String nrPlaca = null;

		Equipamento equipamento = EquipamentoDAO.get(cdEquipamento, connect);

		Result r = new Result(1);

		// PARAMETRIZAR O TIPO DE EVENTO VAL
		TipoEvento tipoEvento = TipoEventoServices.getByIdTipoEvento("IMT", connect);

		ZipInputStream zis = new ZipInputStream(new FileInputStream(file));
		ZipEntry zipEntry = zis.getNextEntry();

		TimeZone timezone = TimeZone.getTimeZone("America/Recife");
		GregorianCalendar dtEntry = new GregorianCalendar(timezone);

		EventoEquipamento evento = null;
		List<Arquivo> fotos = new ArrayList<Arquivo>();

		MedicaoTrafego medicao = null;

		while (zipEntry != null) {

			if (zipEntry.getName().endsWith(".1.jpg")) {
				// dtEntry.setTimeInMillis(zipEntry.getTime());

				// yyyyMMdd_HHmmss_SSSS_1.1.jpg
				String str = zipEntry.getName();
				String dateString = str.split("_")[0] + "_" + str.split("_")[1];
				SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd_HHmmss");
				Date date = formatter.parse(dateString);

				dtEntry.setTimeInMillis(date.getTime());
			}

			if (zipEntry.getName().endsWith(".1.tarja.jpg") || zipEntry.getName().endsWith(".2.tarja.jpg")
					|| zipEntry.getName().endsWith(".xml")) {

				System.out.println("\t\t- " + zipEntry.getName());

				byte[] buffer = new byte[1024];
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				int len;
				while ((len = zis.read(buffer)) > 0) {
					baos.write(buffer, 0, len);
				}
				baos.close();

				byte[] fileContent = baos.toByteArray();

				// gravar imagens
				if (zipEntry.getName().endsWith("jpg")) {
					Arquivo arquivo = new Arquivo();
					arquivo.setCdArquivo(0);
					arquivo.setNmArquivo(zipEntry.getName());
					arquivo.setBlbArquivo(fileContent);
					arquivo.setDtArquivamento(new GregorianCalendar());
					arquivo.setDtCriacao(dtEntry);

					fotos.add(arquivo);
				}

				// ALPR
				if (alprService != null) {
					if (zipEntry.getName().endsWith(".tarja.jpg") && nrPlaca == null) {
						AlprResult result = alprService.recognize(fileContent, zipEntry.getName(),
								equipamento.getNmEquipamento());
						if (result != null) {
							nrPlaca = result.getPlate();
						}
					}
				}

				// gravar evento
				if (zipEntry.getName().endsWith("xml")) {

					System.out.println("\t\tnrPlaca: " + nrPlaca);

					// 2. GRAVAR EVENTO
					evento = new EventoEquipamento();

					evento.setCdEquipamento(cdEquipamento);
					evento.setNrPlaca(nrPlaca);

					medicao = new MedicaoTrafego();
					medicao.setCdEquipamento(cdEquipamento);

					int cdFaixa = 0;
					int cdVia = 0;

					ByteArrayInputStream bais = new ByteArrayInputStream(fileContent);

					DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
					DocumentBuilder parser = factory.newDocumentBuilder();
					Document document = parser.parse(new InputSource(bais));

					NodeList infracaoNodeList = document.getElementsByTagName("INFRACAO");
					for (int j = 0; j < infracaoNodeList.getLength(); j++) {
						Node currentNode = infracaoNodeList.item(j);

						if (currentNode.getNodeType() == Node.ELEMENT_NODE) {

							NodeList nodeList = currentNode.getChildNodes();

							for (int k = 0; k < nodeList.getLength(); k++) {

								Node node = nodeList.item(k);

								if (node.getNodeType() == Node.ELEMENT_NODE) {

									switch (node.getNodeName()) {
									case "NomeEquipamento":
										evento.setNmEquipamento(node.getTextContent());
										break;
									case "TipoClassificacao":

										break;
									case "IdentificacaoINMETRO":
										evento.setIdIdentificacaoInmetro(node.getTextContent());
										break;
									case "LocalInstalacao":
										evento.setDsLocal(node.getTextContent());

										// VIA
										String nmVia = node.getTextContent(); // .split(",")[0].replaceAll("[0-9]",
																				// "").replaceAll(",", "").trim();
										// nmVia = nmVia.replace("Z", "_");

										PreparedStatement ps = connect
												.prepareStatement("SELECT * FROM mob_via WHERE nm_via iLIKE '%" + nmVia
														+ "%' AND cd_orgao = " + equipamento.getCdOrgao());
										ResultSet rs = ps.executeQuery();
										if (rs.next()) {
											cdVia = rs.getInt("cd_via");
											medicao.setCdVia(cdVia);
										}

										break;
									case "Cidade":

										break;
									case "UF":
										break;
									case "Sentido":
										evento.setNmSentidoRodovia(node.getTextContent());
										break;
									case "Marca":
										evento.setNmMarcaEquipamento(node.getTextContent());
										break;
									case "Modelo":
										evento.setNmModeloEquipamento(node.getTextContent());
										break;
									case "NumSerie":
										evento.setNrSerial(node.getTextContent());
										break;
									case "TipoEquipamento":
										evento.setTpEquipamento(EquipamentoServices.RADAR_FIXO);
										break;
									case "OrgAtuador":
									case "OrgAutuador":
										evento.setNmOrgaoAutuador(node.getTextContent());
										break;
									case "OCR_fotos":

										break;
									case "CodigoCidade":

										break;
									case "Comprimento":
										float vlComprimento = new Float(node.getTextContent());

										evento.setVlComprimentoVeiculo(Math.round(vlComprimento));

										/*
										 * 0 - 2.99 - Leve 1 (Ex: Moto) 3 - 5.99 - Leve 2 6 - 14.99 - Pesado 1 15 -
										 * 18.99 - Pesado 2 > 19 - Especial
										 */
										if (vlComprimento < 3)
											evento.setTpVeiculo(0);
										else if (vlComprimento >= 3 && vlComprimento < 6)
											evento.setTpVeiculo(1);
										else if (vlComprimento >= 6 && vlComprimento < 15)
											evento.setTpVeiculo(2);
										else if (vlComprimento >= 15 && vlComprimento < 19)
											evento.setTpVeiculo(3);
										else if (vlComprimento >= 19)
											evento.setTpVeiculo(4);

										break;
									case "IdentificacaoMedida":

										break;
									case "Faixa":
										evento.setNrPista(new Integer(node.getTextContent()));

										int nrFaixa = Integer.parseInt(node.getTextContent());

										if (medicao.getCdVia() > 0) {
											ps = connect.prepareStatement("SELECT * FROM mob_faixa WHERE cd_via = "
													+ medicao.getCdVia() + " AND nr_faixa = " + nrFaixa);
											rs = ps.executeQuery();
											if (rs.next()) {
												cdFaixa = rs.getInt("cd_faixa");
												medicao.setCdFaixa(cdFaixa);
											}
										}
										break;
									case "VelocidadeConsiderada":
										float vlVelocidadeConsiderada = new Float(node.getTextContent());
										evento.setVlConsiderada((int) vlVelocidadeConsiderada);
										break;
									case "VelocidadeLimite":
										float vlVelocidadeLimite = new Float(node.getTextContent());
										evento.setVlLimite((int) vlVelocidadeLimite);

										// tolerada
										evento.setVlVelocidadeTolerada(Math.round(vlVelocidadeLimite) + 7);
										break;
									case "VelocidadeMedida":
										float vlVelocidadeMedida = new Float(node.getTextContent());
										evento.setVlMedida((int) vlVelocidadeMedida);
										break;
									case "DataAfericao":
										evento.setDtAfericao(dtEntry);
										break;

									default:
										System.out.println("\t\t\tCAMPO INDEFINIDO: " + node.getNodeName());
									}
								}
							}
						}
					}

					if (tipoEvento != null)
						evento.setCdTipoEvento(tipoEvento.getCdTipoEvento());

					evento.setNmEquipamento(evento.getNrSerial());

					evento.setLgTempoReal(0);
					evento.setDtConclusao(dtEntry);
					evento.setDtEvento(new GregorianCalendar());

					medicao.setCdEquipamento(cdEquipamento);
					medicao.setCdVia(cdVia);
					medicao.setCdFaixa(cdFaixa);
					medicao.setCdMedicao(0);
					medicao.setQtVeiculos(1);
					medicao.setDtInicial(dtEntry);
					medicao.setDtFinal(dtEntry);

					medicao.setVlComprimentoVeiculo(new Double(evento.getVlComprimentoVeiculo()));
					medicao.setVlVelocidadeConsiderada(new Double(evento.getVlConsiderada()));
					medicao.setVlVelocidadeLimite(new Double(evento.getVlLimite()));
					medicao.setVlVelocidadeMedida(new Double(evento.getVlMedida()));
					medicao.setVlVelocidadeTolerada(new Double(evento.getVlVelocidadeTolerada()));

					medicao.setTpVeiculo(evento.getTpVeiculo());
				}

			}

			zipEntry = zis.getNextEntry();
		}

		System.out.println("\tSalvando evento...");
		r = save(evento, null, connect);

		if (r.getCode() > 0) {

//			System.out.println("\tSalvando imagens do evento...");
//			evento.setCdEvento( ((EventoEquipamento)r.getObjects().get("EVENTOEQUIPAMENTO")).getCdEvento() );
//			r = saveArquivosEventoFtp(evento, fotos, EventoArquivoServices.TP_ARQUIVO_FOTO, connect);

		} else {
			System.out.println("\tErro ao salvar evento...");

			zis.closeEntry();
			zis.close();

			return new Result(-1, "Erro ao salvar evento...");
		}

//		System.out.println("\tSalvando medição...");
//		r = MedicaoTrafegoServices.save(medicao, null, connect);
//		if(r.getCode() < 0) {
//			System.out.println("Erro! EventoEquipamentoServices.processarIMT: "+r.getMessage());
//		}

		zis.closeEntry();
		zis.close();

		return r;
	}

	public static Result importRadarEstatico() {
		return importRadarEstatico(null);
	}

	public static Result importRadarEstatico(Connection connection) {

		boolean isConnectionNull = connection == null;

		try {
			if (isConnectionNull) {
				connection = Conexao.conectar();
				connection.setAutoCommit(false);
			}

			Result result = null;

			ResultSetMap rsmEquipamento = EquipamentoServices.getAllRadarEstatico(connection);

			while (rsmEquipamento.next()) {

				String idEquipamento = rsmEquipamento.getString("id_equipamento");

				File folder = new File("D:/static/" + idEquipamento);
				File[] innerFolders = folder.listFiles();

				for (File dir : innerFolders) {
					if (dir.isDirectory()) {

						FilenameFilter filter = new FilenameFilter() {
							public boolean accept(File dir, String name) {
								return name.endsWith(".LOG");
							}
						};

						File[] logFiles = dir.listFiles(filter);

						for (File log : logFiles) {

							FileReader fr = new FileReader(log);
							BufferedReader br = new BufferedReader(fr);

							String line;
							while ((line = br.readLine()) != null) {

								String[] parts = line.split("_");

								// TODO: evento
								EventoEquipamento evento = new EventoEquipamento();
								evento.setCdEvento(0);
								evento.setCdEquipamento(rsmEquipamento.getInt("cd_equipamento"));
								evento.setDtConclusao(Util.stringToCalendar(parts[0] + parts[7], "HH:mm:ssyyyyMMdd"));
								evento.setDtAfericao(Util.stringToCalendar(parts[0] + parts[7], "HH:mm:ssyyyyMMdd"));
								evento.setDtEvento(new GregorianCalendar());
								evento.setStEvento(ST_EVENTO_NAO_PROCESSADO);
								evento.setDsLocal(parts[11]);
								evento.setTpEquipamento(rsmEquipamento.getInt("tp_equipamento"));
								evento.setVlLimite(Integer.parseInt(parts[2].trim()));
								evento.setVlMedida(Integer.parseInt(parts[3].trim()));
								evento.setVlConsiderada(Integer.parseInt(parts[4].trim()));
								evento.setIdMedida(Integer.parseInt(parts[8].trim()));

								TipoEvento tipoEvento = TipoEventoServices.getByIdTipoEvento("VAL", connection);
								evento.setCdTipoEvento(tipoEvento.getCdTipoEvento());

								evento.setNmOrgaoAutuador("PMVC");

								evento.setNmMarcaEquipamento("LASERTECH");

								result = save(evento, null, connection);

								if (result.getCode() <= 0) {
									if (isConnectionNull)
										connection.rollback();
									br.close();
									fr.close();
									return result;
								}

								evento.setCdEvento(result.getCode());

								// TODO: imagens

								filter = new FilenameFilter() {
									public boolean accept(File dir, String name) {
										return name.contains(parts[8].trim());
									}
								};

								File[] imgFiles = dir.listFiles(filter);
								List<Arquivo> fotos = new ArrayList<Arquivo>();

								for (File img : imgFiles) {
									if (!img.getName().contains("CM-")) {

										Arquivo arquivo = new Arquivo();
										arquivo.setCdArquivo(0);
										arquivo.setNmArquivo(img.getName());
										arquivo.setBlbArquivo(Files.readAllBytes(img.toPath()));
										arquivo.setDtArquivamento(new GregorianCalendar());
										arquivo.setDtCriacao(evento.getDtConclusao());

										fotos.add(arquivo);
									}
								}

								result = saveArquivosEventoFtp(evento, fotos, EventoArquivoServices.TP_ARQUIVO_FOTO,
										connection);

								if (result.getCode() <= 0) {
									if (isConnectionNull)
										connection.rollback();
									br.close();
									fr.close();
									return result;
								}

							}
							br.close();
							fr.close();
						}
					}
				}

			}

			if (isConnectionNull)
				connection.commit();

			return new Result(1, "Eventos importados com sucesso.");
		} catch (Exception e) {
			if (isConnectionNull)
				Conexao.rollback(connection);

			System.out.println("Erro! EventoEquipaetoServicer.importRadarEstatico");
			e.printStackTrace(System.out);

			return null;
		} finally {
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}

	public static void main(String[] args) {
		scanFtpRadar();
	}

}