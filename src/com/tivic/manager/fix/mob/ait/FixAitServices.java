package com.tivic.manager.fix.mob.ait;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.tivic.manager.grl.Bairro;
import com.tivic.manager.grl.BairroDAO;
import com.tivic.manager.grl.BairroServices;
import com.tivic.manager.grl.Cidade;
import com.tivic.manager.grl.Equipamento;
import com.tivic.manager.grl.EquipamentoDAO;
import com.tivic.manager.grl.ParametroServices;
import com.tivic.manager.grl.cidade.cidadeproprietario.CorretorIDCidade;
import com.tivic.manager.grl.pessoa.TipoPessoaEnum;
import com.tivic.manager.log.file.FileLogger;
import com.tivic.manager.mob.Ait;
import com.tivic.manager.mob.AitDAO;
import com.tivic.manager.mob.AitMovimentoDAO;
import com.tivic.manager.mob.AitMovimentoServices;
import com.tivic.manager.mob.AitPagamento;
import com.tivic.manager.mob.AitPagamentoServices;
import com.tivic.manager.mob.AitServices;
import com.tivic.manager.mob.ArquivoMovimento;
import com.tivic.manager.mob.ArquivoMovimentoDAO;
import com.tivic.manager.mob.ServicoDetranConsultaServices;
import com.tivic.manager.mob.TipoCnhEnum;
import com.tivic.manager.mob.TipoStatusEnum;
import com.tivic.manager.mob.ait.AitRepository;
import com.tivic.manager.mob.ait.AitRepositoryDAO;
import com.tivic.manager.mob.ait.AitService;
import com.tivic.manager.mob.aitmovimento.AitMovimentoRepository;
import com.tivic.manager.mob.aitmovimento.AitMovimentoRepositoryDAO;
import com.tivic.manager.mob.aitmovimento.MovimentoNaoAtualizaStatusAit;
import com.tivic.manager.mob.aitmovimento.TipoLgEnviadoDetranEnum;
import com.tivic.manager.mob.aitmovimento.aitmovimentobuilder.AitMovimentoBuilder;
import com.tivic.manager.mob.grafica.LoteImpressao;
import com.tivic.manager.mob.grafica.LoteImpressaoAit;
import com.tivic.manager.mob.grafica.LoteImpressaoAitDAO;
import com.tivic.manager.mob.grafica.LoteImpressaoAitSituacao;
import com.tivic.manager.mob.grafica.LoteImpressaoDAO;
import com.tivic.manager.mob.inconsistencias.TipoInconsistenciaEnum;
import com.tivic.manager.mob.infracao.TipoCompetenciaEnum;
import com.tivic.manager.mob.lote.impressao.TipoLoteDocumentoEnum;
import com.tivic.manager.mob.lote.impressao.TipoLoteNotificacaoEnum;
import com.tivic.manager.str.AitMovimento;
import com.tivic.manager.wsdl.AitDetranObject;
import com.tivic.manager.wsdl.detran.mg.ServicoDetranServicesMG;
import com.tivic.manager.wsdl.detran.mg.consultaautobaseestadual.ConsultaAutoBaseEstadual;
import com.tivic.manager.wsdl.detran.mg.consultaautobaseestadual.ConsultaAutoBaseEstadualDadosRetorno;
import com.tivic.manager.wsdl.detran.mg.consultarplaca.ConsultarPlacaDadosRetorno;
import com.tivic.manager.wsdl.detran.mg.notificacao.TipoNotificacaoEnum;
import com.tivic.manager.wsdl.detran.mg.notificacao.alterarprazorecurso.AlteraPrazoRecursoDTO;
import com.tivic.manager.wsdl.detran.mg.soap.ArquivoConfiguracaoMg;
import com.tivic.manager.wsdl.interfaces.ServicoDetranObjeto;
import com.tivic.sol.cdi.BeansFactory;
import com.tivic.sol.connection.Conexao;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.log.ManagerLog;
import com.tivic.sol.search.SearchCriterios;

import sol.dao.ResultSetMap;
import sol.util.Result;

public class FixAitServices {
	
	/**
	 * 
	 * @return {@link Optional<String>}
	 */
	public Optional<String> fixAitBairro() {

		Connection conn = Conexao.conectar();
		try {

			conn.setAutoCommit(false);
			
			PreparedStatement ps = conn.prepareStatement("select ds_saida->'bairroImovelPossuidor' as nm_bairro, cd_ait from mob_arquivo_movimento "
					+ " where cd_ait in ("
					+ "		select cd_ait from mob_ait where cd_bairro is null"
					+ "		and dt_infracao > '2020-10-01 00:00:00'"
					+ " )"
					+ " and ds_saida->>'codigoRetorno' = '0'"
					+ " and tp_status = 2");
			
			ResultSetMap rsm = new ResultSetMap(ps.executeQuery());
			
			System.out.println("\tFixAitServices.fixAitBairro");
			System.out.println("\t"+rsm.getLines().size()+" AITS encontrados...");
			
			while(rsm.next()) {
				String nmBairro = rsm.getString("nm_bairro");
				
				Ait ait = AitDAO.get(rsm.getInt("cd_ait"), conn);
				
				Bairro bairro = BairroServices.getBairroByNome(nmBairro, conn);
				if(bairro == null) {
					bairro = new Bairro();
					bairro.setCdCidade(ait.getCdCidade());
					bairro.setNmBairro(nmBairro);
					
					Result result = BairroServices.save(bairro, conn);
					if(result.getCode() <= 0) {
						Conexao.rollback(conn);
						return Optional.of(result.getMessage());
					}
					
					bairro = (Bairro)result.getObjects().get("BAIRRO");
				}
				
				ait.setCdBairro(bairro.getCdBairro());
				
				
				if(AitDAO.update(ait, conn) <= 0) {
					Conexao.rollback(conn);
					return Optional.of("Erro ao atualizar AIT "+ait.getIdAit());
				}
				

				System.out.print((rsm.getPointer()+1)+"|");
				
			}
			System.out.println();
			
			conn.commit();
			
			return Optional.empty();
		} catch (SQLException e) {
			Conexao.rollback(conn);
			return Optional.of(e.getMessage());
		} finally {
			Conexao.desconectar(conn);
		}
	}
	
	/**
	 * Lança cancelamentos em AITs sem defesa e sem pagamento
	 * AITs esses do radar AZCV10001119000009, faixa 2
	 * 
	 * Para se executado SOMENTE no SIMTRANS
	 * 
	 * @since 11/01/2021
	 * @author Maurício
	 * @throws IOException 
	 */
	@SuppressWarnings("deprecation")
	public Optional<String> fixCancelaAit() throws IOException {

		Connection conn = Conexao.conectar();
		try {
			conn.setAutoCommit(false);
			
			FileLogger logger = new FileLogger();			
			
			String query = 
					"select A.codigo_ait, A.nr_ait, A.nr_placa " + 
					"from ait A " + 
					"join mob_ait_evento B ON (A.codigo_ait = B.cd_ait) " + 
					"join mob_evento_equipamento C on (b.cd_evento = c.cd_evento) " + 
					"join grl_equipamento D on (c.cd_equipamento = d.cd_equipamento) " + 
					"where C.cd_equipamento = 167 " + 
					"and a.nr_controle is not null " + 
					"and C.dt_conclusao >= '2020-09-12 00:00:00' " + 
					"and C.nr_pista = 2 " + 
					"and not exists ( " + 
					"	select Z.* from ait_movimento Z " + 
					"	where Z.codigo_ait = A.codigo_ait " + 
					"	and Z.tp_status = 7 " + 
					") " + 
					"and not exists ( " + 
					"	select Y.* from ait_pagamento Y " + 
					"	where Y.codigo_ait = A.codigo_ait " + 
					")";
			
			ResultSetMap rsm = new ResultSetMap(conn.prepareStatement(query).executeQuery());
			
			logger.log(rsm.getLines().size()+" aits encontrados...", true);
			int count = 0;
			
			while(rsm.next()) {	
				int cdAit = rsm.getInt("codigo_ait");
				
				logger.log(rsm.getPointer()+1+"", true);
				logger.log("AIT "+rsm.getString("nr_ait")+" ["+cdAit+"]", true);
				
				int tpStatus = -1;	
				String st = "";
				if(!hasMovimento(cdAit, AitMovimentoServices.NAI_ENVIADO, conn)) {
					tpStatus = AitMovimentoServices.CANCELA_REGISTRO_MULTA;
					st = "NAI nao enviada";
				} else if(hasMovimento(cdAit, AitMovimentoServices.NAI_ENVIADO, conn) && !hasMovimento(cdAit, AitMovimentoServices.NIP_ENVIADA, conn)) {
					tpStatus = AitMovimentoServices.CANCELAMENTO_AUTUACAO;
					st = "NAI enviada & NIP não enviada";
				} else if(hasMovimento(cdAit, AitMovimentoServices.NIP_ENVIADA, conn)) {
					tpStatus = AitMovimentoServices.CANCELAMENTO_MULTA;
					st = "NIP enviada";
				}
				
				if(tpStatus == -1) {
					logger.log("tpStatus == -1", true);
					continue;
				}
				
				logger.log(st+" : "+TipoStatusEnum.valueOf(tpStatus), true);
				
				AitMovimento movimento = new AitMovimento();
				movimento.setCodigoAit(cdAit);
				movimento.setNrMovimento(0);
				movimento.setDtMovimento(new GregorianCalendar());
				movimento.setTpStatus(tpStatus);
				movimento.setCodOcorrencia(4); // AIT INCONSISTENTE
				movimento.setDtDigitacao(new GregorianCalendar());
				movimento.setCdUsuario(48); // AGENTE	
								
				Result result = com.tivic.manager.str.AitMovimentoServices.save(movimento, null, conn);
				if(result.getCode() < 0) {
					logger.log(result.getMessage(), true);
					conn.rollback();
					return Optional.of(result.getMessage());
				}				

				AitMovimento _m = (AitMovimento)result.getObjects().get("MOVIMENTO");
				_m.setCodigoAit(cdAit);				
				logger.log(_m.toString());
				
				count++;			
								
				if(count % 100 == 0) {
					conn.commit();
					Conexao.desconectar(conn);
					conn = Conexao.conectar();
					conn.setAutoCommit(false);
					
					logger.log("\tefetivando até "+count, true);
				} 
				

				logger.log("\n", true);
			}
			
			logger.log(count+" cancelamentos lançados", true);
			
			if(!conn.isClosed())
				conn.commit();
						
			return Optional.empty();
		} catch (Exception e) {
			Conexao.rollback(conn);
			return Optional.of(e.getMessage());
		} finally {
			Conexao.desconectar(conn);
		}
	}
	
	private boolean hasMovimento(int cdAit, int tpStatus, Connection conn) throws SQLException {
		String query = "SELECT nr_movimento FROM ait_movimento WHERE codigo_ait = ? AND tp_status = ?";
		
		PreparedStatement ps = conn.prepareStatement(query);
		ps.setInt(1, cdAit);
		ps.setInt(2, tpStatus);
		
		ResultSet rs = ps.executeQuery();
		
		return rs.next();		
	}
	
	public Optional<String> setPagamento(AitPagamentoFix pagamento) throws SQLException {
		
		Connection conn = Conexao.conectar();
		conn.setAutoCommit(false);
		
		Ait ait = AitServices.getById(pagamento.getIdAit(), conn);
		
		if(ait == null) {
			conn.rollback();
			Conexao.desconectar(conn);
			
			return Optional.of(pagamento.getIdAit()+" não existe.");
		}
		
		com.tivic.manager.mob.AitMovimento aitMovimento = new com.tivic.manager.mob.AitMovimento();
		aitMovimento.setCdAit(ait.getCdAit());
		aitMovimento.setTpStatus(AitMovimentoServices.MULTA_PAGA);
		aitMovimento.setDtDigitacao(new GregorianCalendar());
		aitMovimento.setDtMovimento(pagamento.getDtPagamento());
		aitMovimento.setLgEnviadoDetran(AitMovimentoServices.NAO_ENVIADO);
		
		Result result = AitMovimentoServices.save(aitMovimento, null, conn);
		if(result.getCode() <= 0) {
			conn.rollback();
			Conexao.desconectar(conn);
			
			return Optional.of(result.getMessage());
		}
		
		
		AitPagamento aitPagamento = new AitPagamento(ait.getCdAit(), 0d, pagamento.getVlPago(), pagamento.getNmBanco(), null, pagamento.getDtPagamento(), null);
		result = AitPagamentoServices.save(aitPagamento, null, conn);
		if(result.getCode() <= 0) {
			conn.rollback();
			Conexao.desconectar(conn);
			
			return Optional.of(result.getMessage());
		}
		
		
		
		conn.commit();		
		Conexao.desconectar(conn);
		
		return Optional.empty();
	}
	
	public Optional<String> updateAitDetran() throws Exception {
		
		Connection conn = Conexao.conectar();
		conn.setAutoCommit(false);

		System.out.println("=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=");
		System.out.println("UPDATE FROM DETRAN");
		
		PreparedStatement ps = conn.prepareStatement("SELECT cd_ait FROM mob_ait WHERE nr_controle IS NULL or nr_controle = 0");
		ResultSetMap rsm = new ResultSetMap(ps.executeQuery());
		
		System.out.println(rsm.getLines().size()+" registros encontrados...");
		
		int i = 0;
		Result result;
		while(rsm.next()) {
			Ait ait = AitDAO.get(rsm.getInt("cd_ait"), conn);
			
			System.out.println((++i) + ". " + ait.getIdAit());
			
			result = AitServices.updateDetran(ait, conn);
			
			if(result.getCode() <= 0) {
				conn.rollback();
				Conexao.desconectar(conn);
				
				System.out.println(result.getMessage());
				
				return Optional.of(result.getMessage());
			}
		}

		conn.commit();		
		Conexao.desconectar(conn);
		
		System.out.println("Concluído");
		System.out.println("=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=");
		
		return Optional.empty();
	}
	
	public Optional<String> updateAitsSemBairro() throws Exception {
		
		Connection conn = Conexao.conectar();
		Result res;
		
		try {
			System.out.println("=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=");
			System.out.println("UPDATE AITS SEM BAIRRO");
			
			PreparedStatement pstmt = conn.prepareStatement("SELECT cd_ait FROM MOB_AIT WHERE CD_BAIRRO IS NULL");
			ResultSetMap _rsm = new ResultSetMap(pstmt.executeQuery());
			System.out.printf("%d registros encontrados... \n", _rsm.getLines().size());
			int updatedAits = 0;
			
			System.out.println("Iniciando fix..");
			while(_rsm.next()) {
				Ait ait = AitDAO.get(_rsm.getInt("cd_ait"));
				res = AitServices.updateDetran(ait, conn);
				System.out.println(res.toString());
				if(res.getCode() <= 0) {
					
					Conexao.desconectar(conn);
					
					System.out.println(res.getMessage());				
					return Optional.of(res.getMessage());
				}
				
				ait = (Ait) res.getObjects().get("AIT");
				Bairro bairro = BairroDAO.get(ait.getCdBairro());
				updatedAits++;
				
				if(bairro != null && ait != null )
					System.out.printf("%d -> AIT: %s - Bairro: %s \n", updatedAits, ait.getIdAit(), bairro.getNmBairro());
				else {
					return Optional.of("Erro na atualizacao do " + updatedAits + "°  AIT");
				}
			}
			
			System.out.printf("%d AITs atualizados. \n", updatedAits);
			Conexao.desconectar(conn);
			
			return Optional.empty();
		} catch(Exception ex) {
			ex.printStackTrace(System.out);
			return Optional.of(ex.getMessage());
		} finally {
			Conexao.desconectar(conn);
		}
		
	}
	
	
	public static void fixStatusAit() throws Exception {
		CustomConnection customConnection = new CustomConnection();
		try {
			customConnection.initConnection(true);
			AitService aitService = new AitService();
			AitRepository aitRepository = new AitRepositoryDAO();
			
			System.out.println("----------------------------------- INICIADO ---------------------------------------");
			List<Ait> aits = aitService.find(new SearchCriterios(), customConnection);
			for(Ait ait: aits) {
				System.out.println("AIT: " + ait.getIdAit());
				System.out.println(ait.getTpStatus());
				System.out.println(ait.getCdMovimentoAtual());
				System.out.println("....");
				
				PreparedStatement pst = customConnection.getConnection().prepareStatement(
						"SELECT cd_movimento, tp_status FROM mob_ait_movimento A "+ 
						" WHERE A.cd_ait = " + ait.getCdAit() +
						"   AND A.tp_status NOT IN (" +  new MovimentoNaoAtualizaStatusAit().getMovimento().toString().replace("[", "").replace("]", "")  + ")" +
						" 	AND A.lg_enviado_detran NOT IN (" + TipoLgEnviadoDetranEnum.NAO_ENVIAR_CANCELADO.getKey()  + ")" + 
						"   AND EXISTS (SELECT * FROM mob_arquivo_movimento B WHERE A.cd_ait = B.cd_ait AND A.cd_movimento = B.cd_movimento AND B.ds_saida->>'codigoRetorno'='0')" +
						" ORDER BY dt_movimento DESC " + 
						" LIMIT 1 ");
				
				System.out.println("SQL : " + pst);
				
				ResultSetMap rsm = new ResultSetMap(pst.executeQuery());
				
				if(rsm.size() == 0) {
					rsm = new ResultSetMap(customConnection.getConnection().prepareStatement(
							"SELECT cd_movimento, tp_status FROM mob_ait_movimento A "+ 
							" WHERE A.cd_ait = " + ait.getCdAit() +
							"   AND A.tp_status NOT IN (" +  new MovimentoNaoAtualizaStatusAit().getMovimento().toString().replace("[", "").replace("]", "")  + ")" +
							" 	AND A.lg_enviado_detran NOT IN (" + TipoLgEnviadoDetranEnum.NAO_ENVIAR_CANCELADO.getKey()  + ")" + 
							" ORDER BY dt_movimento DESC " + 
							" LIMIT 1 "
					).executeQuery());
				}
				
				if (rsm.next()) {
					ait.setTpStatus(rsm.getInt("tp_status"));
					ait.setCdMovimentoAtual(rsm.getInt("cd_movimento"));
					aitRepository.update(ait, customConnection);
				}
				System.out.println(ait.getTpStatus());
				System.out.println(ait.getCdMovimentoAtual());
				System.out.println();
				System.out.println();
			}
			System.out.println("----------------------------------- FINALIZADO ---------------------------------------");
			customConnection.finishConnection();
		} finally {
			customConnection.closeConnection();
		}
	}
	
	public static void fixDataFimPrazoDefesaAit() throws Exception {
		CustomConnection customConnection = new CustomConnection();
		try {
			customConnection.initConnection(true);
			AitRepository aitRepository = new AitRepositoryDAO();
			AitMovimentoRepository aitMovimentoRepository = new AitMovimentoRepositoryDAO();
			System.out.println("----------------------------------- INICIADO ---------------------------------------");
				ResultSetMap rsm = new ResultSetMap(customConnection.getConnection().prepareStatement(
						"SELECT DISTINCT ON (C.cd_ait) A.cd_ait, A.dt_vencimento, B.cd_movimento, B.cd_ait, B.tp_status," 
						+ " C.ds_saida, C.tp_status, C.nr_erro, C.ds_saida->>'dataLimiteRecurso' as dt_vencimento_atual"
						+ " FROM mob_ait A"
						+ " JOIN mob_ait_movimento B on (B.cd_ait = A.cd_ait and B.tp_status = 39)"
						+ " JOIN mob_arquivo_movimento C on (C.cd_ait = B.cd_ait)" 
						+ " WHERE C.nr_erro = '115' AND A.dt_vencimento is null"
				).executeQuery());
				while (rsm.next()) {
					System.out.println("SQL : " + rsm);

					Ait ait = AitDAO.get(rsm.getInt("cd_ait"));
						System.out.println(" AIT: " + ait.getCdAit() + ", VENCIMENTO: " + ait.getDtVencimento());
					com.tivic.manager.mob.AitMovimento aitMovimento = AitMovimentoServices.getMovimentoTpStatus(ait.getCdAit(), AitMovimentoServices.FIM_PRAZO_DEFESA);
						System.out.println("MOVIMENTO: " + aitMovimento.getCdMovimento() + ", STATUS: " + aitMovimento.getTpStatus() + ", ENVIADO DETRAN: " + aitMovimento.getLgEnviadoDetran());
					
					String dtVencimento = montarDataVencimento(rsm.getString("dt_vencimento_atual"));
					SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy"); 
					GregorianCalendar dtFimPrazoRecurso =  new GregorianCalendar();
					dtFimPrazoRecurso.setTime(formato.parse(dtVencimento));
					ait.setDtVencimento(dtFimPrazoRecurso);	
					aitMovimento.setLgEnviadoDetran(TipoStatusEnum.ENVIADO_AO_DETRAN.getKey());
					
					aitRepository.update(ait, customConnection);
					aitMovimentoRepository.update(aitMovimento, customConnection);
						System.out.println("UPDATE-> AIT: " + ait.getCdAit() + ", VENCIMENTO: " + ait.getDtVencimento());
						System.out.println("UPDATE-> MOVIMENTO: " + aitMovimento.getCdMovimento() + ", STATUS: " + aitMovimento.getTpStatus() + ", ENVIADO DETRAN: " + aitMovimento.getLgEnviadoDetran());
				}
				
			System.out.println("----------------------------------- FINALIZADO ---------------------------------------");
			customConnection.finishConnection();
		} finally {
			customConnection.closeConnection();
		}
	}
	
	private static String montarDataVencimento(String data)
	{
		try
		{
			String dtVencimento = null;
			
			StringBuilder stringBuilderData = new StringBuilder(data);
			stringBuilderData.insert(data.length() - 2, '/');
			stringBuilderData.insert(data.length() - 4, '/');
			
			String[] dismemberData = stringBuilderData.toString().split("\\p{Punct}");
			dtVencimento = dismemberData[2] + "/" + dismemberData[1] + "/" + dismemberData[0];

			return dtVencimento;
		}
		catch (Exception e)
		{
			System.out.println("Erro in FixAitService > montarDataVencimento()");
			e.printStackTrace();
			return null;
		}
	}
	
	public static void alterarPrazoVencimentoMariana() throws Exception {
		CustomConnection customConnection = new CustomConnection();
		try {
			customConnection.initConnection(true);
			PreparedStatement pst = customConnection.getConnection().prepareStatement(
					"select A.cd_ait, A.id_ait, A.dt_infracao, A.dt_vencimento from mob_ait A\r\n" + 
					"	where A.dt_vencimento between '2023-01-20 00:00:00' AND '2023-01-20 00:00:00' \r\n" + 
					"		AND not exists (\r\n" + 
					"			select B.* from mob_ait_movimento B where B.tp_status = 113 and B.lg_enviado_detran = 1 AND B.cd_ait = A.cd_ait\r\n" + 
					"		)\r\n" + 
					"	order by cd_ait asc");
			System.out.println("SQL : " + pst);
			ResultSetMap rsm = new ResultSetMap(pst.executeQuery());
			System.out.println("Resultado consulta SIZE: " + rsm.getLines().size());
			while (rsm.next()) {
				try {
					System.out.println("Alterando Data CD AIT: " + rsm.getString("CD_AIT"));
					System.out.println("Alterando Data ID AIT: " + rsm.getString("ID_AIT"));
					AlteraPrazoRecursoDTO alterarPrazoRecurso = new AlteraPrazoRecursoDTO();
					alterarPrazoRecurso.setCdAit(rsm.getInt("CD_AIT"));
					alterarPrazoRecurso.setCdUsuario(1);
					alterarPrazoRecurso.setNovoPrazoRecurso(new GregorianCalendar(2023, 0, 20));
					alterarPrazoRecurso.setTipoRecurso(TipoNotificacaoEnum.NOVO_PRAZO_JARI.getKey());
					new ServicoDetranServicesMG().alterarPrazoRecurso(alterarPrazoRecurso);
				}
				catch (Exception e) {
					System.out.println("XXXXXXXXXXXXXXXXXX ERROR XXXXXXXXXXXXXXXXXXXXX");
					System.out.println("Não foi possivel atualizar o AIT.");
				}
			}
			customConnection.closeConnection();
		}
		finally {
			customConnection.finishConnection();
		}
	}
			
	public static void fixStImpressaoLoteImpressaoAit() throws Exception {
		CustomConnection customConnection = new CustomConnection();
		try {
			customConnection.initConnection(true);
			System.out.println("----------------------------------- INICIADO ---------------------------------------");
				ResultSetMap rsm = new ResultSetMap(customConnection.getConnection().prepareStatement(
						"SELECT DISTINCT ON (A.cd_ait) A.cd_ait, B.tp_status, C.cd_lote_impressao, C.st_impressao, D.tp_documento "
						+ " FROM mob_ait A"
						+ " JOIN mob_ait_movimento B ON (B.cd_ait = A.cd_ait) "
						+ " JOIN mob_lote_impressao_ait C ON (C.cd_ait = A.cd_ait) "
						+ " JOIN mob_lote_impressao D ON (D.cd_lote_impressao = C.cd_lote_impressao AND D.tp_documento = " + TipoLoteDocumentoEnum.LOTE_NIP.getKey() + ")"
						+ " WHERE A.cd_ait = B.cd_ait"
						+ " AND EXISTS( "
						+ "  SELECT B5.tp_status from mob_ait_movimento B5" 
						+ "		WHERE (B5.tp_status = " + TipoStatusEnum.NIP_ENVIADA.getKey() + " ) AND B5.cd_ait = A.cd_ait" 
						+ "		AND EXISTS ( "
						+ "		SELECT B6.tp_status, B6.dt_movimento, B6.lg_enviado_detran FROM mob_ait_movimento B6"
						+ "			WHERE (B6.tp_status = " + TipoStatusEnum.CANCELAMENTO_NIP.getKey() 
						+ "			AND B6.dt_movimento > ( " 
						+ "					SELECT B7.dt_movimento FROM mob_ait_movimento B7 " 
						+ "						WHERE B7.tp_status = " + TipoStatusEnum.NIP_ENVIADA.getKey()
						+ "						AND B7.cd_ait = A.cd_ait ORDER BY dt_movimento DESC LIMIT 1 ) "
						+ "			) AND B6.cd_ait = A.cd_ait ORDER BY B6.dt_movimento DESC " 
						+ "		) AND B5.cd_ait = A.cd_ait "
						+ "     AND NOT EXISTS ("
						+ "			SELECT B8.tp_status FROM mob_ait_movimento B8" 
						+ "			WHERE B8.tp_status = "+ TipoStatusEnum.MULTA_PAGA.getKey() + "  AND B8.cd_ait = A.cd_ait" 
						+ "		)"
						+ " )AND A.cd_ait = B.cd_ait "
				).executeQuery());
				while (rsm.next()) {
					System.out.println("<----------- SITUAÇÃO ATUAL: ----------->");
					Ait ait = AitDAO.get(rsm.getInt("cd_ait"));

						System.out.println(" AIT: " + ait.getCdAit());
					com.tivic.manager.mob.AitMovimento aitMovimento = AitMovimentoServices.getMovimentoTpStatus(ait.getCdAit(), AitMovimentoServices.DEFESA_INDEFERIDA);
						System.out.println(" Movimento: " + aitMovimento.getTpStatus());
					LoteImpressaoAit loteImpressaoAit = LoteImpressaoAitDAO.get(rsm.getInt("cd_lote_impressao"), ait.getCdAit());
						System.out.println(" Situação do Lote " + loteImpressaoAit.getStImpressao());
					LoteImpressao loteImpressao = LoteImpressaoDAO.get(loteImpressaoAit.getCdLoteImpressao());
						System.out.println(" Cd do Lote " + loteImpressao.getCdLoteImpressao() + 
								"\n Tipo de Lote: " + loteImpressao.getTpDocumento());

					loteImpressaoAit.setStImpressao(LoteImpressaoAitSituacao.REGISTRO_CANCELADO);
						
					LoteImpressaoAitDAO.update(loteImpressaoAit, customConnection.getConnection());
					System.out.println("<----------- RESULTADO: -----------> \n AIT: " + ait.getCdAit() + 
							"\n Situação do Lote: "+ loteImpressaoAit.getStImpressao() + "\n Tipo de Lote: " + loteImpressao.getTpDocumento());

				}
				
			System.out.println("----------------------------------- FINALIZADO ---------------------------------------");
			customConnection.finishConnection();
		} finally {
			customConnection.closeConnection();
		}
	}
	
	public static void fixCdEquipamentoRadar() throws Exception {
	    CustomConnection customConnection = new CustomConnection();
	    try {
	        customConnection.initConnection(true);
	        System.out.println("----------------------------------- INICIADO ---------------------------------------");
	        ResultSetMap rsm = new ResultSetMap(customConnection.getConnection().prepareStatement(
	                "SELECT * FROM mob_ait WHERE id_ait LIKE '%AG%' AND cd_equipamento IS NULL AND cd_infracao = 1141"
	        ).executeQuery());
	        int contador = 0;
	        while (rsm.next()) {
	        	Ait ait = AitDAO.get(rsm.getInt("cd_ait"));
	            System.out.println("<----------- REGISTRO ATUAL: ----------->");
	            System.out.println("código do AIT: " + ait.getCdAit() + " | " + "ID do AIT: " + ait.getIdAit() + " | " + "CD do equipamento: " + ait.getCdEquipamento() + " | " + "CD da infracao: " + ait.getCdInfracao());

	            // Atualizar o valor do cd_equipamento para 451
	            
	            ait.setCdEquipamento(451);
	            AitDAO.update(ait, customConnection.getConnection());
	            System.out.println("<----------- REGISTRO ATUALIZADO: ----------->");
	            System.out.println("código do AIT: " + ait.getCdAit() + " | " + "ID do AIT: " + ait.getIdAit() + " | " + "CD do equipamento: " + ait.getCdEquipamento() + " | " + "CD da infracao: " + ait.getCdInfracao());
	            System.out.println("AITs atualizados: " + contador);
	            contador++;
	        }
	        System.out.println("total: " + rsm.size());
	        
	        System.out.println("----------------------------------- FINALIZADO ---------------------------------------");
	        customConnection.finishConnection();
	    } finally {
	        customConnection.closeConnection();
	    }
	}
	
	public static void fixCdEquipamentoAit() throws Exception {
	    CustomConnection customConnection = new CustomConnection();
	    try {
	        customConnection.initConnection(true);
	        System.out.println("----------------------------------- INICIADO ---------------------------------------");
	        ResultSetMap rsm = new ResultSetMap(customConnection.getConnection().prepareStatement(
	                "SELECT * FROM mob_ait WHERE id_ait LIKE '%AG%' AND cd_equipamento IS NULL"
	        ).executeQuery());
	        
	        while (rsm.next()) {
	        	Ait ait = AitDAO.get(rsm.getInt("cd_ait"));
	            System.out.println("<----------- REGISTRO ATUAL: ----------->");
	            System.out.println("código do AIT: " + ait.getCdAit() + " | " + "ID do AIT: " + ait.getIdAit() + " | " + "CD do equipamento: " + ait.getCdEquipamento());

	            // Atualizar o valor do cd_equipamento para 467
	            
	            ait.setCdEquipamento(467);
	            AitDAO.update(ait, customConnection.getConnection());
	            System.out.println("<----------- REGISTRO ATUALIZADO: ----------->");
	            System.out.println("código do AIT: " + ait.getCdAit() + " | " + "ID do AIT: " + ait.getIdAit() + " | " + "CD do equipamento: " + ait.getCdEquipamento());
	            System.out.println("total: " + rsm.size());
	        }

	        System.out.println("----------------------------------- FINALIZADO ---------------------------------------");
	        customConnection.finishConnection();
	    } finally {
	        customConnection.closeConnection();
	    }
	}
	
	public static void fixMovimentoAit() throws Exception {
	    CustomConnection customConnection = new CustomConnection();
	    try {
	        customConnection.initConnection(true);
	        System.out.println("----------------------------------- INICIADO ---------------------------------------");
	        ResultSetMap rsm = new ResultSetMap(customConnection.getConnection().prepareStatement(
	                "SELECT " 
		                + "A.id_ait, " 
		                + "A.cd_ait, " 
		                + "A.tp_status, " 
		                + "A.cd_movimento_atual, " 
		                + "B.tp_status, " 
		                + "(select B1.cd_movimento"  
		                + "		from  mob_ait_movimento B1"  
		                + "		where "  
		                + "			B1.tp_status not in (25, 26, 82) "  
		                + "	 		and B1.cd_ait = A.cd_ait "  
		                + "		group by "  
		                + "			B1.cd_movimento, "  
		                + "			B1.cd_ait "  
		                + "		ORDER BY "  
		                + "			B1.dt_movimento desc limit 1) as cd_movimento " 
	               + "FROM "
	                    + "mob_ait A "
	                    + "LEFT OUTER JOIN mob_ait_movimento B ON (A.cd_ait = B.cd_ait AND A.cd_movimento_atual = B.cd_movimento) " 
	                    + "LEFT OUTER JOIN mob_ait_movimento C ON (C.cd_ait = A.cd_ait AND C.tp_status = A.tp_status) "
	               + "WHERE "
	               		+ "B.tp_status IN ("
	               		+ TipoStatusEnum.CANCELAMENTO_PONTUACAO.getKey() + ","
	               		+ TipoStatusEnum.CANCELAMENTO_AUTUACAO.getKey() + ","
	               		+ TipoStatusEnum.CANCELAMENTO_MULTA.getKey() + ","
	               		+ TipoStatusEnum.CANCELAMENTO_DEFESA_PREVIA.getKey() + ","
	               		+ TipoStatusEnum.CANCELAMENTO_RECURSO_JARI.getKey() + ","
	               		+ TipoStatusEnum.CANCELAMENTO_RECURSO_CETRAN.getKey() + ","
						+ TipoStatusEnum.CANCELAMENTO_JARI_COM_PROVIMENTO.getKey() + ","
						+ TipoStatusEnum.CANCELAMENTO_CETRAN_COM_PROVIMENTO.getKey() + ","
						+ TipoStatusEnum.CANCELAMENTO_JARI_SEM_ACOLHIMENTO.getKey() + ","
						+ TipoStatusEnum.CANCELAMENTO_DEFESA_INDEFERIDA.getKey() + ","
						+ TipoStatusEnum.CANCELAMENTO_ADVERTENCIA_DEFESA_PREVIA.getKey() + ","
						+ TipoStatusEnum.CANCELAMENTO_ADVERTENCIA_DEFESA_DEFERIDA.getKey() + ","
						+ TipoStatusEnum.CANCELAMENTO_ADVERTENCIA_DEFESA_INDEFERIDA.getKey()
	               		+ ")"
	        ).executeQuery());
	        
	        while (rsm.next()) {
	        	Ait ait = AitDAO.get(rsm.getInt("cd_ait"));
	        	System.out.println("<----------- REGISTRO ATUAL: ----------->");
		        System.out.println("CD do AIT: " + ait.getCdAit() + " | " + "código do movimento atual: " + ait.getCdMovimentoAtual() + " | " + "código do movimento " + rsm.getInt("cd_movimento") + " | " );

		        // Atualizar o cd_movimento_atual com o cd_movimento se A.tp_status for igual B.tp_status
		        if(ait.getCdMovimentoAtual() != rsm.getInt("cd_movimento"))
		            ait.setCdMovimentoAtual(rsm.getInt("cd_movimento"));
		            
		        AitDAO.update(ait, customConnection.getConnection());
		        System.out.println("<----------- REGISTRO ATUALIZADO: ----------->");
		        System.out.println("CD do AIT: " + ait.getCdAit() + " | " + "código do movimento atual: " + ait.getCdMovimentoAtual() + " | " );
		        	        		            
	        }
	        System.out.println("total: " + rsm.size());
	        System.out.println("----------------------------------- FINALIZADO ---------------------------------------");
	        customConnection.finishConnection();
	    } finally {
	        customConnection.closeConnection();
	    }
	}
	
	public static void fixNrCpfCnpjProprietario() throws Exception {
		CustomConnection customConnection = new CustomConnection();
		try {
			customConnection.initConnection(true);
			System.out.println("----------------------------------- INICIADO ---------------------------------------");
				ResultSetMap rsm = new ResultSetMap(customConnection.getConnection().prepareStatement(
						"SELECT A.ds_saida -> 'numeroIdentificacaoPossuidor' AS cpf_cnpj_proprietario, A.cd_arquivo_movimento, "
						+ " A.cd_movimento, A.cd_ait, B.nr_cpf_cnpj_proprietario, D.cd_lote_impressao"
						+ " FROM mob_arquivo_movimento A"
						+ " 	JOIN mob_ait B ON (A.cd_ait = B.cd_ait) "
						+ " 	JOIN mob_lote_impressao_ait C ON (A.cd_ait = C.cd_ait) "
						+ " 	JOIN mob_lote_impressao D ON (D.cd_lote_impressao = C.cd_lote_impressao)"
						+ " WHERE D.cd_lote_impressao = 303 "
						+ " AND B.nr_cpf_cnpj_proprietario ilike '00000000000000' AND A.nr_erro is null " 
						+ "	AND A.tp_status = " + TipoStatusEnum.REGISTRO_INFRACAO.getKey()
				).executeQuery());
				while (rsm.next()) {
					
					String nrCpfCnpjProprietario = rsm.getString("cpf_cnpj_proprietario");
										
					Ait ait = AitDAO.get(rsm.getInt("cd_ait"));
					LoteImpressao loteImpressao = LoteImpressaoDAO.get(rsm.getInt("cd_lote_impressao"));
					
					System.out.println("<----------- SITUAÇÃO ATUAL: ----------->");
		            System.out.println("código do AIT: " + ait.getCdAit() + " | " + "Nº CPF/CNPJ Proprietário " + ait.getNrCpfCnpjProprietario() + " | " + "CD do lote: " + loteImpressao.getCdLoteImpressao());

		            ait.setNrCpfCnpjProprietario(nrCpfCnpjProprietario.replace("\"", ""));
		            AitDAO.update(ait, customConnection.getConnection());
		            
		            System.out.println("<----------- REGISTRO ATUALIZADO: ----------->");
		            System.out.println("código do AIT: " + ait.getCdAit() + " | " + "Nº CPF/CNPJ Proprietário " + ait.getNrCpfCnpjProprietario() + " | " + "CD do lote: " + loteImpressao.getCdLoteImpressao());

				}
		    System.out.println("Total: " + rsm.size());
			System.out.println("----------------------------------- FINALIZADO ---------------------------------------");
			customConnection.finishConnection();
		} finally {
			customConnection.closeConnection();
		}
	}

	public static void fixCdCidadeProprietarioAitPrimeiraSituacao() throws Exception {
        // Faz a busca de id_cidade com zero à esquerda
		CustomConnection customConnection = new CustomConnection();
		try {
			customConnection.initConnection(true);
			System.out.println("----------------------------------- INICIADO ---------------------------------------");
				ResultSetMap rsm = new ResultSetMap(customConnection.getConnection().prepareStatement(
						"SELECT A.ds_saida ->> 'codigoMunicipioPossuidor' AS id_cidade_proprietario, "
						+ "	A.cd_arquivo_movimento, A.cd_movimento, A.cd_ait, C.id_cidade, C.cd_cidade, B.cd_cidade_proprietario "
						+ " FROM mob_arquivo_movimento A "
						+ " 	JOIN mob_ait B ON (A.cd_ait = B.cd_ait) "
						+ "     JOIN grl_cidade C ON (CAST(C.id_cidade AS text) = A.ds_saida ->> 'codigoMunicipioPossuidor') "
						+ "	WHERE B.cd_ait = A.cd_ait "
						+ " AND A.tp_status = " + TipoStatusEnum.REGISTRO_INFRACAO.getKey()
						+ "	AND DATE(B.dt_infracao) >  '2022-08-10'" 
				).executeQuery());
				while (rsm.size() == 0) {
				    System.out.println("Nenhum resultado encontrado para o cd_ait: " + rsm.getInt("cd_ait"));
				    continue; 
				}
				while (rsm.next()) {
					Ait ait = AitDAO.get(rsm.getInt("cd_ait"));
					
					Cidade cidade = FixLoteImpressaoAit.getByIdCidade(rsm.getString("ID_CIDADE_PROPRIETARIO"));
					ait.setCdCidadeProprietario(cidade.getCdCidade());

					AitDAO.update(ait, customConnection.getConnection());
					
					System.out.println("<----------- AIT: ----------->");
					System.out.println("CD AIT: " + ait.getCdAit());
					System.out.println("\tCD_CIDADE NOVO: " + ait.getCdCidadeProprietario());
				}
		    System.out.println("Total: " + rsm.size());
			System.out.println("----------------------------------- FINALIZADO ---------------------------------------");
			customConnection.finishConnection();
		} finally {
			customConnection.closeConnection();
		}
	}
	
	public static void fixCdCidadeProprietarioAitSegundaSituacao() throws Exception {
        // Faz a busca de id_cidade removendo zero à esquerda
		CustomConnection customConnection = new CustomConnection();
		try {
			customConnection.initConnection(true);
			System.out.println("----------------------------------- INICIADO ---------------------------------------");
				ResultSetMap rsm = new ResultSetMap(customConnection.getConnection().prepareStatement(
						"SELECT ltrim(A.ds_saida ->> 'codigoMunicipioPossuidor', '0')  AS id_cidade_proprietario, "
						+ "	A.cd_arquivo_movimento, A.cd_movimento, A.cd_ait, C.id_cidade, C.cd_cidade, B.cd_cidade_proprietario "
						+ " FROM mob_arquivo_movimento A "
						+ " 	JOIN mob_ait B ON (A.cd_ait = B.cd_ait) "
						+ "     JOIN grl_cidade C ON (C.id_cidade = ltrim(A.ds_saida ->> 'codigoMunicipioPossuidor', '0')) "
						+ "	WHERE B.cd_ait = A.cd_ait "
						+ " AND A.tp_status = " + TipoStatusEnum.REGISTRO_INFRACAO.getKey()
						+ "	AND DATE(B.dt_infracao) >  '2022-08-10'" 
				).executeQuery());
				while (rsm.size() == 0) {
				    System.out.println("Nenhum resultado encontrado para o cd_ait: " + rsm.getInt("cd_ait"));
				    continue; 
				}
				while (rsm.next()) {
					Ait ait = AitDAO.get(rsm.getInt("cd_ait"));
					
					Cidade cidade = FixLoteImpressaoAit.getByIdCidade(rsm.getString("ID_CIDADE_PROPRIETARIO"));
					ait.setCdCidadeProprietario(cidade.getCdCidade());

					AitDAO.update(ait, customConnection.getConnection());
					
					System.out.println("<----------- AIT: ----------->");
					System.out.println("CD AIT: " + ait.getCdAit());
					System.out.println("\tCD_CIDADE NOVO: " + ait.getCdCidadeProprietario());
				}
		    System.out.println("Total: " + rsm.size());
			System.out.println("----------------------------------- FINALIZADO ---------------------------------------");
			customConnection.finishConnection();
		} finally {
			customConnection.closeConnection();
		}
	}
	
	public static void fixFiciArquivoMovimento() throws Exception {
		CustomConnection customConnection = new CustomConnection();
		try {
			customConnection.initConnection(true);
			System.out.println("----------------------------------- INICIADO ---------------------------------------");
			ResultSetMap rsm = new ResultSetMap(customConnection.getConnection().prepareStatement(
					" SELECT DISTINCT(B.cd_ait), A.ds_saida ->> 'mensagemRetorno' AS mensagemRetorno, "
							+ " A.cd_arquivo_movimento, A.cd_movimento, A.dt_arquivo, A.nr_erro "
							+ " FROM mob_arquivo_movimento A "
							+ " 	JOIN mob_ait B ON (A.cd_ait = B.cd_ait) "
							+ " 	JOIN mob_ait_movimento C ON (A.cd_ait = C.cd_ait) "
							+ " WHERE EXISTS ( "
							+ "		SELECT A1.ds_saida ->> 'mensagemRetorno' AS mensagemRetorno, A1.cd_ait "
							+ "			FROM mob_arquivo_movimento A1 "
							+ "			WHERE A1.tp_status = 19 "
							+ "			AND A1.ds_saida ->> 'mensagemRetorno' = 'Solicitacao efetuada com sucesso'"
							+ "			AND A1.cd_ait = A.cd_ait "
							+ "	)"
							+ " AND DATE(B.dt_infracao) >= '2022-07-11' "
							+ "	AND A.tp_status = 19 "
							+ " AND A.nr_erro = '168' "
					).executeQuery());
			while (rsm.next()) {
				String nrErroNulo = null;
				Ait ait = AitDAO.get(rsm.getInt("cd_ait"));
				com.tivic.manager.mob.AitMovimento aitMovimento = AitMovimentoDAO.get(rsm.getInt("cd_movimento"), ait.getCdAit());
				ArquivoMovimento arquivoMovimento = ArquivoMovimentoDAO.get(rsm.getInt("cd_arquivo_movimento"), rsm.getInt("cd_movimento"), ait.getCdAit(), customConnection.getConnection());
				
				System.out.println("<----------- Registros: ----------->");
				System.out.println("código do AIT: " + ait.getCdAit() + " | " + "Cód. Erro: " + arquivoMovimento.getNrErro() + " | " + "Status: " + arquivoMovimento.getTpStatus());
				
				ArquivoMovimentoDAO.delete(arquivoMovimento.getCdArquivoMovimento(), arquivoMovimento.getCdMovimento(), ait.getCdAit());
				ait.setNrErro(nrErroNulo);
				AitDAO.update(ait, customConnection.getConnection());		
				aitMovimento.setNrErro(nrErroNulo);
				AitMovimentoDAO.update(aitMovimento, customConnection.getConnection());
			}
			System.out.println("Total: " + rsm.size());
			System.out.println("----------------------------------- FINALIZADO ---------------------------------------");
			customConnection.finishConnection();
		} finally {
			customConnection.closeConnection();
		}
	}
	
	
	public static void fixPublicacaoNaiArquivoMovimento() throws Exception {
		CustomConnection customConnection = new CustomConnection();
		try {
			customConnection.initConnection(true);
			System.out.println("----------------------------------- INICIADO ---------------------------------------");
			ResultSetMap rsm = new ResultSetMap(customConnection.getConnection().prepareStatement(
					" SELECT DISTINCT(B.cd_ait), A.ds_saida ->> 'mensagemRetorno' AS mensagemRetorno, "
							+ " A.cd_arquivo_movimento, A.cd_movimento, A.dt_arquivo, A.nr_erro "
							+ " FROM mob_arquivo_movimento A "
							+ " 	JOIN mob_ait B ON (A.cd_ait = B.cd_ait) "
							+ " 	JOIN mob_ait_movimento C ON (A.cd_ait = C.cd_ait) "
							+ " WHERE EXISTS ( "
							+ "		SELECT A1.ds_saida ->> 'mensagemRetorno' AS mensagemRetorno, A1.cd_ait "
							+ "			FROM mob_arquivo_movimento A1 "
							+ "			WHERE A1.tp_status = " + TipoStatusEnum.PUBLICACAO_NAI.getKey()
							+ "			AND A1.ds_saida ->> 'mensagemRetorno' = 'Solicitacao efetuada com sucesso'"
							+ "			AND A1.cd_ait = A.cd_ait)"
							+ " AND DATE(B.dt_infracao) >= '2022-07-11' "
							+ "	AND A.tp_status = " + TipoStatusEnum.PUBLICACAO_NAI.getKey()
							+ " AND A.nr_erro = '54'"
					).executeQuery());
			while (rsm.next()) {
				String nrErroNulo = null;
				Ait ait = AitDAO.get(rsm.getInt("cd_ait"));
				com.tivic.manager.mob.AitMovimento aitMovimento = AitMovimentoDAO.get(rsm.getInt("cd_movimento"), ait.getCdAit());
				ArquivoMovimento arquivoMovimento = ArquivoMovimentoDAO.get(rsm.getInt("cd_arquivo_movimento"), rsm.getInt("cd_movimento"), ait.getCdAit(), customConnection.getConnection());
				
				System.out.println("<----------- Registros: ----------->");
				System.out.println("código do AIT: " + ait.getCdAit() + " | " + "Cód. Erro: " + arquivoMovimento.getNrErro() + " | " + "Status: " + arquivoMovimento.getTpStatus());
				
				ArquivoMovimentoDAO.delete(arquivoMovimento.getCdArquivoMovimento(), arquivoMovimento.getCdMovimento(), ait.getCdAit());
				ait.setNrErro(nrErroNulo);
				AitDAO.update(ait, customConnection.getConnection());		
				aitMovimento.setNrErro(nrErroNulo);
				AitMovimentoDAO.update(aitMovimento, customConnection.getConnection());
			}
			System.out.println("Total: " + rsm.size());
			System.out.println("----------------------------------- FINALIZADO ---------------------------------------");
			customConnection.finishConnection();
		} finally {
			customConnection.closeConnection();
		}
	}
	
	public static void fixPublicacaoNipArquivoMovimento() throws Exception {
		CustomConnection customConnection = new CustomConnection();
		try {
			customConnection.initConnection(true);
			System.out.println("----------------------------------- INICIADO ---------------------------------------");
			ResultSetMap rsm = new ResultSetMap(customConnection.getConnection().prepareStatement(
					" SELECT DISTINCT(B.cd_ait), A.ds_saida ->> 'mensagemRetorno' AS mensagemRetorno, "
							+ " A.cd_arquivo_movimento, A.cd_movimento, A.dt_arquivo, A.nr_erro "
							+ " FROM mob_arquivo_movimento A "
							+ " 	JOIN mob_ait B ON (A.cd_ait = B.cd_ait) "
							+ " 	JOIN mob_ait_movimento C ON (A.cd_ait = C.cd_ait) "
							+ " WHERE EXISTS ( "
							+ "		SELECT A1.ds_saida ->> 'mensagemRetorno' AS mensagemRetorno, A1.cd_ait "
							+ "			FROM mob_arquivo_movimento A1 "
							+ "			WHERE A1.tp_status = " + TipoStatusEnum.PUBLICACAO_NIP.getKey()
							+ "			AND A1.ds_saida ->> 'mensagemRetorno' = 'Solicitacao efetuada com sucesso'"
							+ "			AND A1.cd_ait = A.cd_ait)"
							+ " AND DATE(B.dt_infracao) >= '2022-07-11' "
							+ "	AND A.tp_status = " + TipoStatusEnum.PUBLICACAO_NIP.getKey()
							+ " AND A.nr_erro = '54'"
					).executeQuery());
			while (rsm.next()) {
				String nrErroNulo = null;
				Ait ait = AitDAO.get(rsm.getInt("cd_ait"));
				com.tivic.manager.mob.AitMovimento aitMovimento = AitMovimentoDAO.get(rsm.getInt("cd_movimento"), ait.getCdAit());
				ArquivoMovimento arquivoMovimento = ArquivoMovimentoDAO.get(rsm.getInt("cd_arquivo_movimento"), rsm.getInt("cd_movimento"), ait.getCdAit(), customConnection.getConnection());
				
				System.out.println("<----------- Registros: ----------->");
				System.out.println("código do AIT: " + ait.getCdAit() + " | " + "Cód. Erro: " + arquivoMovimento.getNrErro() + " | " + "Status: " + arquivoMovimento.getTpStatus());
				
				ArquivoMovimentoDAO.delete(arquivoMovimento.getCdArquivoMovimento(), arquivoMovimento.getCdMovimento(), ait.getCdAit());
				ait.setNrErro(nrErroNulo);
				AitDAO.update(ait, customConnection.getConnection());		
				aitMovimento.setNrErro(nrErroNulo);
				AitMovimentoDAO.update(aitMovimento, customConnection.getConnection());
			}
			System.out.println("Total: " + rsm.size());
			System.out.println("----------------------------------- FINALIZADO ---------------------------------------");
			customConnection.finishConnection();
		} finally {
			customConnection.closeConnection();
		}
	}
		
	public static void fixRecursoJariArquivoMovimento() throws Exception {
		CustomConnection customConnection = new CustomConnection();
		try {
			customConnection.initConnection(true);
			System.out.println("----------------------------------- INICIADO ---------------------------------------");
			ResultSetMap rsm = new ResultSetMap(customConnection.getConnection().prepareStatement(
					" SELECT DISTINCT(B.cd_ait), A.ds_saida ->> 'mensagemRetorno' AS mensagemRetorno, "
							+ " A.cd_arquivo_movimento, A.cd_movimento, A.dt_arquivo, A.nr_erro "
							+ " FROM mob_arquivo_movimento A "
							+ " 	JOIN mob_ait B ON (A.cd_ait = B.cd_ait) "
							+ " 	JOIN mob_ait_movimento C ON (A.cd_ait = C.cd_ait) "
							+ " WHERE EXISTS ( "
							+ "		SELECT A1.ds_saida ->> 'mensagemRetorno' AS mensagemRetorno, A1.cd_ait "
							+ "			FROM mob_arquivo_movimento A1 "
							+ "			WHERE A1.tp_status = " + TipoStatusEnum.RECURSO_JARI.getKey()
							+ "			AND A1.ds_saida ->> 'mensagemRetorno' = 'Solicitacao efetuada com sucesso'"
							+ "			AND A1.cd_ait = A.cd_ait)"
							+ " AND DATE(B.dt_infracao) >= '2022-07-11' "
							+ "	AND A.tp_status = " + TipoStatusEnum.RECURSO_JARI.getKey()
							+ " AND A.nr_erro = '55'"
					).executeQuery());
			while (rsm.next()) {
				String nrErroNulo = null;
				Ait ait = AitDAO.get(rsm.getInt("cd_ait"));
				com.tivic.manager.mob.AitMovimento aitMovimento = AitMovimentoDAO.get(rsm.getInt("cd_movimento"), ait.getCdAit());
				ArquivoMovimento arquivoMovimento = ArquivoMovimentoDAO.get(rsm.getInt("cd_arquivo_movimento"), rsm.getInt("cd_movimento"), ait.getCdAit(), customConnection.getConnection());
				
				System.out.println("<----------- Registros: ----------->");
				System.out.println("código do AIT: " + ait.getCdAit() + " | " + "Cód. Erro: " + arquivoMovimento.getNrErro() + " | " + "Status: " + arquivoMovimento.getTpStatus());
				
				ArquivoMovimentoDAO.delete(arquivoMovimento.getCdArquivoMovimento(), arquivoMovimento.getCdMovimento(), ait.getCdAit());
				ait.setNrErro(nrErroNulo);
				AitDAO.update(ait, customConnection.getConnection());		
				aitMovimento.setNrErro(nrErroNulo);
				AitMovimentoDAO.update(aitMovimento, customConnection.getConnection());
			}
			System.out.println("Total: " + rsm.size());
			System.out.println("----------------------------------- FINALIZADO ---------------------------------------");
			customConnection.finishConnection();
		} finally {
			customConnection.closeConnection();
		}
	}
	
	public static void fixIdMovimentacao(Integer a){
		Connection connection = Conexao.conectar();
		try {
			connection.setAutoCommit(false);
			//Atualiza o id_movimentacao = 999 de ds_ocorrencia= 'PRAZO DECORRIDO' para o id_movimentacao = 006;
			updateIdMovimentacao(connection, 999, "PRAZO DECORRIDO", 006);
			System.out.println("Fim.");
			connection.commit();
		} catch (Exception e) {
			Conexao.rollback(connection);
			System.out.println(e.getMessage());
		} finally {
			Conexao.desconectar(connection);
		}
	}

	public static void updateIdMovimentacao(Connection connection, int idMovimentacaoAntigo, String dsOcorrencia, int idMovimentacaoNovo) throws Exception {
		try {
			PreparedStatement pstmt = connection.prepareStatement("UPDATE mob_ocorrencia "+
					" SET id_movimentacao = ?" +
					" WHERE ds_ocorrencia = ?" +
					" AND id_movimentacao = ?");
			pstmt.setInt(1, idMovimentacaoNovo);
			pstmt.setString(2, dsOcorrencia);
			pstmt.setInt(3, idMovimentacaoAntigo);
			System.out.println("Descricao de ocorrencia: "+ dsOcorrencia);
			System.out.println("Codigo de movimentacao antigo: "+ idMovimentacaoAntigo);
			System.out.println("Codigo de movimentacao novo: " + idMovimentacaoNovo);
			int result = pstmt.executeUpdate();
			if(result < 0) {
				Conexao.rollback(connection);
				throw new Exception("Erro ao atualizar registro");
			}
			System.out.println("Atualização concluída para ds_ocorrencia de PRAZO DECORRIDO para o id_movimentacao: " + idMovimentacaoNovo);
			 System.out.println("--------------------------------------------------------");
        } catch (SQLException e) {
            throw new Exception("Erro ao atualizar registro: " + e.getMessage());
		}
	}
	
	public static void fixCorrecaoDataPrazoDefesa(Integer a){
		Connection connection = Conexao.conectar();
		try {
			connection.setAutoCommit(false);
			fixCorrecaoFormatoDataPrazoDefesa();
			System.out.println("Fim.");
			connection.commit();
		} catch (Exception e) {
			Conexao.rollback(connection);
			System.out.println(e.getMessage());
		} finally {
			Conexao.desconectar(connection);
		}
	}
	
	public static void fixCorrecaoFormatoDataPrazoDefesa() throws Exception {
	    CustomConnection customConnection = new CustomConnection();
	    try {
	    	
	    	String resultado = "CORRECAO DATA PRAZO DEFESA\n";
	    	
	        customConnection.initConnection(true);
	        System.out.println("----------------------------------- INICIADO CORREÇÃO DO FORMATO DE DATA---------------------------------------");
	        ResultSetMap rsm = new ResultSetMap(customConnection.getConnection().prepareStatement(
	        		"select "  
	    	                + "A.cd_ait, " 
	    	                + "A.id_ait, " 
	    	                + "A.dt_infracao, " 
	    	                + "A.dt_vencimento, " 
	    	                + "A.dt_prazo_defesa, " 
	    	                + "B.dt_movimento, " 
	    	                + "B.cd_movimento "
	    	                + "from " 
	    	                + "mob_ait A " 
	    	                + "Left outer join mob_ait_movimento B on (A.cd_ait = B.cd_ait) "  
	    	                + "where " 
	    	                + "B.tp_status = 3 "
	    	                + "and A.dt_prazo_defesa < '0001-01-01' " 
	    	                + "order by " 
	    	                + "A.dt_infracao desc"
	        ).executeQuery());
	        while (rsm.next()) {
	        	Ait ait = AitDAO.get(rsm.getInt("cd_ait"));
	        	com.tivic.manager.mob.AitMovimento aitMovimento = AitMovimentoDAO.get(rsm.getInt("cd_movimento"), ait.getCdAit());
	        	resultado += "<----------- REGISTRO ATUAL: ----------->\n";
	        	resultado += "código do AIT: " + ait.getCdAit() + " | " + "ID do AIT: " + ait.getIdAit() + " | " 
	            		+ "Data do movimento de NAI: " + sol.util.Util.formatDateTime(aitMovimento.getDtMovimento(), "yyyy-MM-dd'T'HH:mm:ss.SSS", "") + " | " 
	            		+ "Data do prazo defesa: " + sol.util.Util.formatDateTime(ait.getDtPrazoDefesa(), "yyyy-MM-dd'T'HH:mm:ss.SSS", "") + "\n";
	            
	            AitDetranObject aitDetranObject = new AitDetranObject(ait);
	            ConsultaAutoBaseEstadual consultaAutoBaseEstadual = new ConsultaAutoBaseEstadual();
	            consultaAutoBaseEstadual.setArquivoConfiguracao(new ArquivoConfiguracaoMg());
	            ServicoDetranObjeto servicoDetranObjeto = consultaAutoBaseEstadual.executar(aitDetranObject);
	            ConsultaAutoBaseEstadualDadosRetorno consultaAutoBaseEstadualDadosRetorno = (ConsultaAutoBaseEstadualDadosRetorno) servicoDetranObjeto.getDadosRetorno();
	            
	            GregorianCalendar atualizaPrazoDefesa = new GregorianCalendar();
	            atualizaPrazoDefesa.setTime(aitMovimento.getDtMovimento().getTime());
	            ait.setDtPrazoDefesa(atualizaPrazoDefesa);
	            ait.getDtPrazoDefesa().set(Calendar.YEAR, Integer.parseInt(consultaAutoBaseEstadualDadosRetorno.getDataLimiteDefesa().substring(6, 10)));
	            ait.getDtPrazoDefesa().set(Calendar.MONTH, Integer.parseInt(consultaAutoBaseEstadualDadosRetorno.getDataLimiteDefesa().substring(3, 5))-1);
	            ait.getDtPrazoDefesa().set(Calendar.DAY_OF_MONTH, Integer.parseInt(consultaAutoBaseEstadualDadosRetorno.getDataLimiteDefesa().substring(0, 2)));
	            AitDAO.update(ait, customConnection.getConnection());
	            
	            resultado += "<----------- REGISTRO ATUALIZADO: ----------->\n";
	            resultado += "código do AIT: " + ait.getCdAit() + " | " + "ID do AIT: " + ait.getIdAit() + " | " 
	            		+ "Data do movimento de NAI: " + sol.util.Util.formatDateTime(aitMovimento.getDtMovimento(), "yyyy-MM-dd'T'HH:mm:ss.SSS", "") + " | "
	            		+ "Data do prazo defesa: " + sol.util.Util.formatDateTime(ait.getDtPrazoDefesa(), "yyyy-MM-dd'T'HH:mm:ss.SSS", "") + "\n\n";
	            
	        }
	        resultado += "total: " + rsm.size();
	        System.out.println("----------------------------------- FINALIZADO ---------------------------------------");
	        customConnection.finishConnection();
	        
	        File file = new File("CORRECAO_DATA_PRAZO_DEFESA_TEOFILO.txt");
			if(!file.exists()) {
				file.createNewFile();
			}
			FileOutputStream fileOutputStream = new FileOutputStream(file);
			fileOutputStream.write(resultado.getBytes());
			fileOutputStream.close();
	        
	    } finally {
	        customConnection.closeConnection();
	    }
	}
	
	public static void fixCorrecaoDataVencimento(Integer a){
		Connection connection = Conexao.conectar();
		try {
			System.out.println("Chegou");
			connection.setAutoCommit(false);
			fixCorrecaoFormatoDataVencimento();
			System.out.println("Fim.");
			connection.commit();
		} catch (Exception e) {
			Conexao.rollback(connection);
			System.out.println(e.getMessage());
		} finally {
			Conexao.desconectar(connection);
		}
	}

	public static void fixCorrecaoFormatoDataVencimento() throws Exception {
	    CustomConnection customConnection = new CustomConnection();
	    try {
	    	String resultado = "CORRECAO DATA VENCIMENTO\n";
	    	
	        customConnection.initConnection(true);
	        System.out.println("----------------------------------- INICIADO CORREÇÃO DO FORMATO DE DATA---------------------------------------");
	        ResultSetMap rsm = new ResultSetMap(customConnection.getConnection().prepareStatement(
	        		"select "  
	    	                + "A.cd_ait, " 
	    	                + "A.id_ait, " 
	    	                + "A.dt_infracao, " 
	    	                + "A.dt_vencimento, " 
	    	                + "A.dt_prazo_defesa, " 
	    	                + "B.dt_movimento, " 
	    	                + "B.cd_movimento "
	    	                + "from " 
	    	                + "mob_ait A " 
	    	                + "Left outer join mob_ait_movimento B on (A.cd_ait = B.cd_ait) "  
	    	                + "where " 
	    	                + "B.tp_status = 5 "
	    	                + "and A.dt_vencimento < '0001-01-01' " 
	    	                + "order by " 
	    	                + "A.dt_infracao desc"
	        ).executeQuery());
	        while (rsm.next()) {
	        	Ait ait = AitDAO.get(rsm.getInt("cd_ait"));
	        	com.tivic.manager.mob.AitMovimento aitMovimento = AitMovimentoDAO.get(rsm.getInt("cd_movimento"), ait.getCdAit());
	        	resultado += "<----------- REGISTRO ATUAL: ----------->\n";
	        	resultado += "código do AIT: " + ait.getCdAit() + " | " + "ID do AIT: " + ait.getIdAit() + " | " 
	            		+ "Data do movimento de NIP: " + sol.util.Util.formatDateTime(aitMovimento.getDtMovimento(), "yyyy-MM-dd'T'HH:mm:ss.SSS", "") + " | " 
	            		+ "Data do vencimento: " + sol.util.Util.formatDateTime(ait.getDtVencimento(), "yyyy-MM-dd'T'HH:mm:ss.SSS", "") + "\n";
	            
	            AitDetranObject aitDetranObject = new AitDetranObject(ait);
	            ConsultaAutoBaseEstadual consultaAutoBaseEstadual = new ConsultaAutoBaseEstadual();
	            consultaAutoBaseEstadual.setArquivoConfiguracao(new ArquivoConfiguracaoMg());
	            ServicoDetranObjeto servicoDetranObjeto = consultaAutoBaseEstadual.executar(aitDetranObject);
	            ConsultaAutoBaseEstadualDadosRetorno consultaAutoBaseEstadualDadosRetorno = (ConsultaAutoBaseEstadualDadosRetorno) servicoDetranObjeto.getDadosRetorno();
	            
	            if(consultaAutoBaseEstadualDadosRetorno.getDataLimiteRecurso() == null) {
	            	resultado += "AIT NAO ATUALIZADO - DATA LIMITE JARI NULA\n\n";
	            	continue;
	            }
	            	
	            
	            GregorianCalendar atualizaPrazoDefesa = new GregorianCalendar();
	            atualizaPrazoDefesa.setTime(aitMovimento.getDtMovimento().getTime());
	            ait.setDtVencimento(atualizaPrazoDefesa);
	            ait.getDtVencimento().set(Calendar.YEAR, Integer.parseInt(consultaAutoBaseEstadualDadosRetorno.getDataLimiteRecurso().substring(6, 10)));
	            ait.getDtVencimento().set(Calendar.MONTH, Integer.parseInt(consultaAutoBaseEstadualDadosRetorno.getDataLimiteRecurso().substring(3, 5))-1);
	            ait.getDtVencimento().set(Calendar.DAY_OF_MONTH, Integer.parseInt(consultaAutoBaseEstadualDadosRetorno.getDataLimiteRecurso().substring(0, 2)));
	            AitDAO.update(ait, customConnection.getConnection());
	            
	            resultado += "<----------- REGISTRO ATUALIZADO: ----------->\n";
	            resultado += "código do AIT: " + ait.getCdAit() + " | " + "ID do AIT: " + ait.getIdAit() + " | " 
	            		+ "Data do movimento de NIP: " + sol.util.Util.formatDateTime(aitMovimento.getDtMovimento(), "yyyy-MM-dd'T'HH:mm:ss.SSS", "") + " | "
	            		+ "Data do vencimento: " + sol.util.Util.formatDateTime(ait.getDtVencimento(), "yyyy-MM-dd'T'HH:mm:ss.SSS", "") + "\n\n";
	        }
	        resultado += "total: " + rsm.size();
	        System.out.println("----------------------------------- FINALIZADO ---------------------------------------");
	        customConnection.finishConnection();
	        
	        File file = new File("CORRECAO_DATA_VENCIMENTO_TEOFILO.txt");
			if(!file.exists()) {
				file.createNewFile();
			}
			FileOutputStream fileOutputStream = new FileOutputStream(file);
			fileOutputStream.write(resultado.getBytes());
			fileOutputStream.close();
	    } finally {
	        customConnection.closeConnection();
	    }
	}
	
	public static void fixVigenciaRegistroInfracao() throws Exception {
		CustomConnection customConnection = new CustomConnection();
		try {
			int cdInfracaoAntiga = 637;
			int cdInfracaoAtualizada = 1141;
			customConnection.initConnection(true);
			System.out.println("----------------------------------- INICIADO ---------------------------------------");
				ResultSetMap rsm = new ResultSetMap(customConnection.getConnection().prepareStatement(
						" SELECT DISTINCT(A.cd_ait), A.id_ait, A.cd_infracao "
						+ " FROM mob_ait A "
						+ " 	JOIN mob_infracao B ON(A.cd_infracao = B.cd_infracao) "
						+ " 	JOIN mob_ait_movimento C ON(A.cd_ait = C.cd_ait) "
						+ " WHERE C.tp_status = " + TipoStatusEnum.REGISTRO_INFRACAO.getKey() 
						+ "	AND A.cd_infracao = " + cdInfracaoAntiga 
						+ "	AND (DATE(B.dt_fim_vigencia ) <= '2023-11-01' or DATE(B.dt_fim_vigencia ) is not null) "  
						+ "	AND DATE(A.dt_infracao) >= '2023-10-04' "
				).executeQuery());
				while (rsm.next()) {
					Ait ait = AitDAO.get(rsm.getInt("cd_ait"));
					
					System.out.println("<----------- Registros: ----------->");
		            System.out.println("Identificador do AIT: " + ait.getIdAit() + " | código do AIT: " + ait.getCdAit() + " | " + "Cód. Infração: " + ait.getCdInfracao() + "\n");
		            ait.setCdInfracao(cdInfracaoAtualizada);
		            AitDAO.update(ait, customConnection.getConnection());		
				}
		    System.out.println("Total: " + rsm.size());
			System.out.println("----------------------------------- FINALIZADO ---------------------------------------");
			customConnection.finishConnection();
		} finally {
			customConnection.closeConnection();
		}
	}
	
	public static void fixLgEnviadoDetranRegistroInfracao(Integer a) {
		CustomConnection customConnection = null;
		try {
			customConnection = new CustomConnection();
			customConnection.initConnection(true);
			System.out.println("----------------------------------- INICIADO ---------------------------------------");
				ResultSetMap rsm = new ResultSetMap(customConnection.getConnection().prepareStatement(
						"SELECT A.cd_ait, B.cd_movimento "
								+ " FROM mob_ait A"
				 			    + " LEFT OUTER JOIN mob_ait_movimento B ON (A.cd_ait = B.cd_ait AND A.cd_movimento_atual = B.cd_movimento)"
								+ " WHERE B.tp_status = " + TipoStatusEnum.REGISTRO_INFRACAO.getKey()
								+ " AND B.lg_enviado_detran = " + TipoStatusEnum.ENVIADO_AO_DETRAN.getKey()
								+ " AND A.dt_infracao <= '2022-01-31' and B.dt_registro_detran IS null "
								+ " AND A.id_ait LIKE 'T%'" 
								+ " AND NOT EXISTS "
								+ " ( "
								+ " 	SELECT tp_status FROM mob_ait_movimento K "
								+ "		WHERE tp_status IN "
								+ "			( "
								+ "			" + TipoStatusEnum.NIP_ENVIADA.getKey()
								+ "			, " + TipoStatusEnum.CADASTRO_CANCELADO.getKey()
								+ "			, " + TipoStatusEnum.CANCELA_REGISTRO_MULTA.getKey()
								+ "			, " + TipoStatusEnum.CANCELAMENTO_AUTUACAO.getKey()
								+ " 		, " + TipoStatusEnum.CANCELAMENTO_MULTA.getKey()
								+ " 		, " + TipoStatusEnum.CANCELAMENTO_PONTUACAO.getKey()
								+ "			, " + TipoStatusEnum.DEVOLUCAO_PAGAMENTO.getKey()
								+ "			, " + TipoStatusEnum.NAI_ENVIADO.getKey()
								+ "			)"
								+ " AND K.cd_ait = A.cd_ait "
								+ " )"
								+ "	AND NOT EXISTS "
								+ " ( "
								+ "		SELECT B.nr_cod_detran, B.tp_competencia FROM mob_infracao B "
								+ " 	WHERE "
								+ "			( "
								+ " 		B.nr_cod_detran = 50002 "
								+ "			OR B.nr_cod_detran = 50020 "
								+ "         AND tp_competencia = " + TipoCompetenciaEnum.ESTADUAL.getKey()
								+ " 		)"
								+ " AND A.cd_infracao = B.cd_infracao "
								+ " )"
								+ "	AND NOT EXISTS "
								+ " ( "
								+ "		SELECT C.* FROM MOB_AIT_INCONSISTENCIA C "
								+ "		JOIN mob_inconsistencia Z on (Z.cd_inconsistencia = C.cd_inconsistencia)"
								+ " 	WHERE "
								+ "			( "
								+ " 			Z.tp_inconsistencia = " + TipoInconsistenciaEnum.TP_INDEFERIMENTO.getKey()
								+ " 		)"
								+ " AND A.cd_ait = C.cd_ait "
								+ " )"
								+ "	AND NOT EXISTS "
								+ " ( "
								+ "		SELECT D.cd_lote_impressao, D.tp_lote_impressao, E.cd_ait FROM mob_lote_impressao D "
								+ "		JOIN mob_lote_impressao_ait E ON (D.cd_lote_impressao = E.cd_lote_Impressao) "	
								+ " 	WHERE "
								+ "			( "
								+ " 			D.tp_documento = " + TipoLoteNotificacaoEnum.LOTE_NAI.getKey()
								+ " 		)"
								+ " AND E.cd_ait = A.cd_ait "
								+ " )"
				).executeQuery());
				while (rsm.next()) {
					Ait ait = AitDAO.get(rsm.getInt("cd_ait"));
					com.tivic.manager.mob.AitMovimento aitMovimento = AitMovimentoDAO.get(rsm.getInt("cd_movimento"), ait.getCdAit());

					System.out.println("<----------- Registros: ----------->");
		            System.out.println("Identificador do AIT: " + ait.getIdAit() + " | código do AIT: " + ait.getCdAit() + " | " + "Data da Infração: " 
		            		+ ait.getDtInfracao() + "Envio Detran: " + aitMovimento.getLgEnviadoDetran()+ "\n");
		            
		            aitMovimento.setLgEnviadoDetran(TipoLgEnviadoDetranEnum.NAO_ENVIAR_CANCELADO.getKey());
		            AitMovimentoDAO.update(aitMovimento, customConnection.getConnection());
				}
		    System.out.println("Total: " + rsm.size());
			System.out.println("----------------------------------- FINALIZADO ---------------------------------------");
			customConnection.finishConnection();
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			try {
				customConnection.closeConnection();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public static void fixCdCidadeProprietarioAits(Integer a) {
		CustomConnection customConnection = null;
		try {
			customConnection = new CustomConnection();
			customConnection.initConnection(true);
				System.out.println("----------------------------------- INICIADO ---------------------------------------");
				ResultSetMap rsm = new ResultSetMap(customConnection.getConnection().prepareStatement(
						" SELECT A.cd_ait, A.dt_infracao, A.cd_cidade_proprietario, A.nr_placa FROM mob_ait A "
						+ "	WHERE A.tp_status =  " + TipoStatusEnum.NAI_ENVIADO.getKey()
						+ "	AND A.cd_cidade_proprietario IS null  "
						+ "	AND NOT EXISTS ( " 
						+ "		SELECT B.cd_ait FROM mob_ait_movimento B "
						+ "		WHERE B.tp_status IN ( "  
						+ "		 	" + TipoStatusEnum.CADASTRO_CANCELADO.getKey()
						+ "			, " + TipoStatusEnum.CANCELA_REGISTRO_MULTA.getKey()
						+ "			, " + TipoStatusEnum.CANCELAMENTO_AUTUACAO.getKey()
						+ "		) "  
						+ " 	AND B.cd_ait = A.cd_ait " 
						+ " ) " 
						+ " ORDER BY A.dt_infracao DESC " 
				).executeQuery());
				if (rsm.size() == 0) {
				    System.out.println("Nenhum resultado encontrado para o cd_ait: " + rsm.getInt("cd_ait"));
				    return; 
				}
				while (rsm.next()) {
					Ait ait = AitDAO.get(rsm.getInt("cd_ait"));
					System.out.println("<----------- AIT COM ERRO: ----------->");
					System.out.println("CD AIT: " + ait.getCdAit() + " ...CD CIDADE PROPRIETÁRIO: " + ait.getCdCidadeProprietario() + " ...PLACA: " + ait.getNrPlaca());

					ServicoDetranConsultaServices servicoDetranConsultaServices = new ServicoDetranConsultaServices();
					ServicoDetranObjeto servicoDetranObjeto = servicoDetranConsultaServices.consultarPlaca(ait.getNrPlaca());
					ConsultarPlacaDadosRetorno consultarPlacaDadosRetorno = (ConsultarPlacaDadosRetorno) servicoDetranObjeto.getDadosRetorno();

					CorretorIDCidade cidadeProprietario = new CorretorIDCidade();
					Cidade cidade = cidadeProprietario.getCidadeById(String.valueOf(consultarPlacaDadosRetorno.getCodigoMunicipio()));

					ait.setCdCidadeProprietario(cidade.getCdCidade());
					AitDAO.update(ait, customConnection.getConnection());

					System.out.println("<----------- AIT ATUALIZADO: ----------->");
					System.out.println("CD AIT: " + ait.getCdAit() + " ...CD CIDADE PROPRIETÁRIO: " + ait.getCdCidadeProprietario() + " ...PLACA: " + ait.getNrPlaca());
				}
		    System.out.println("Total: " + rsm.size());
			System.out.println("----------------------------------- FINALIZADO ---------------------------------------");
			customConnection.finishConnection();
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			try {
				customConnection.closeConnection();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	public static void fixTpCnhCondutorComErro(Integer a) {
		CustomConnection customConnection = null;
		try {
			customConnection = new CustomConnection();
			customConnection.initConnection(true);
			System.out.println("----------------------------------- INICIADO ---------------------------------------");
			ResultSetMap rsm = new ResultSetMap(customConnection.getConnection().prepareStatement(
					" SELECT DISTINCT(A.cd_ait), B.ds_saida ->> 'mensagemRetorno' AS mensagemRetorno, B.cd_arquivo_movimento ,"
					+ " B.cd_movimento, B.nr_erro, A.id_ait, A.dt_infracao, A.tp_cnh_condutor, A.nr_cnh_autuacao, A.uf_cnh_autuacao, A.cd_equipamento "
					+ " FROM mob_ait A "
					+ " 	LEFT OUTER JOIN mob_arquivo_movimento B ON (A.cd_ait = B.cd_ait) "
					+ " 	JOIN mob_ait_movimento C ON (A.cd_ait = C.cd_ait) "
					+ " WHERE A.cd_ait IN ( "
					+ "		SELECT B1.cd_ait FROM mob_arquivo_movimento B1 "
					+ " 	WHERE B1.nr_erro = '104' "
					+ "		AND	B1.ds_saida ->> 'mensagemRetorno' = 'Informacoes de CNH do condutor incompletas' "
					+ " 	AND B1.tp_status =  " + TipoStatusEnum.REGISTRO_INFRACAO.getKey() 
					+ " )" 
					+ " AND A.dt_infracao >= '2024-01-01' "
					+ " AND C.tp_status =  " + TipoStatusEnum.REGISTRO_INFRACAO.getKey() 
					+ " AND C.lg_enviado_detran = " + TipoLgEnviadoDetranEnum.NAO_ENVIADO.getKey()
			).executeQuery());
			while (rsm.next()) {
				Ait ait = AitDAO.get(rsm.getInt("cd_ait"));
				com.tivic.manager.mob.AitMovimento aitMovimento = AitMovimentoDAO.get(rsm.getInt("cd_movimento"), ait.getCdAit());
				ArquivoMovimento arquivoMovimento = ArquivoMovimentoDAO.get(rsm.getInt("cd_arquivo_movimento"), rsm.getInt("cd_movimento"), ait.getCdAit());
				
				System.out.println("<----------- Registros: ----------->");
	            System.out.println("código do AIT: " + ait.getCdAit() + " | " + " | " + " CNH " + ait.getNrCnhAutuacao() 
	            + " | " + "Tipo CNH: " + ait.getTpCnhCondutor() + " | " + "Cód. Erro: " + arquivoMovimento.getNrErro());

	            ait.setTpCnhCondutor(0);
	            AitDAO.update(ait, customConnection.getConnection());		
	            AitMovimentoDAO.update(aitMovimento, customConnection.getConnection());
	            
				System.out.println("<----------- AIT ATUALIZADO: ----------->");
	            System.out.println("código do AIT: " + ait.getCdAit() + " | " + " | " + " CNH " + ait.getNrCnhAutuacao() 
	            + " | " + "Tipo CNH: " + ait.getTpCnhCondutor() + " | " + "Cód. Erro: " + arquivoMovimento.getNrErro());
			}
		    System.out.println("Total: " + rsm.size());
			System.out.println("----------------------------------- FINALIZADO ---------------------------------------");
			customConnection.finishConnection();
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			try {
				customConnection.closeConnection();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public static void fixTpCnhCondutorRegistro(Integer a) {
		CustomConnection customConnection = null;
		try {
			customConnection = new CustomConnection();
			customConnection.initConnection(true);
			System.out.println("----------------------------------- INICIADO ---------------------------------------");
				ResultSetMap rsm = new ResultSetMap(customConnection.getConnection().prepareStatement(
						" SELECT id_ait, dt_infracao, cd_ait, tp_cnh_condutor, nr_cnh_autuacao, uf_cnh_autuacao, cd_equipamento "
						+ " FROM mob_ait "
						+ " 	WHERE tp_cnh_condutor = " + TipoCnhEnum.TP_CNH_RENACH.getKey()
						+ " 	AND nr_cnh_autuacao = '' AND uf_cnh_autuacao = '' "
						+ " 	AND cd_movimento_atual = 1 AND dt_infracao >= '2024-01-01' "
				).executeQuery());
				while (rsm.next()) {
					Ait ait = AitDAO.get(rsm.getInt("cd_ait"));
					
		            System.out.println("<----------- Registros: ----------->");
		            System.out.println("código do AIT: " + ait.getCdAit() + " | " + " CNH " + ait.getNrCnhAutuacao() 
		            + " | " + "Tipo CNH: " + ait.getTpCnhCondutor());
		            
		            ait.setTpCnhCondutor(0);
		            ait.setUfCnhAutuacao(null);
		            AitDAO.update(ait, customConnection.getConnection());	
		            
					System.out.println("<----------- AIT ATUALIZADO: ----------->");
		            System.out.println("código do AIT: " + ait.getCdAit() + " | " + " CNH " + ait.getNrCnhAutuacao() 
		            + " | " + "Tipo CNH: " + ait.getTpCnhCondutor());
				}
		    System.out.println("Total: " + rsm.size());
			System.out.println("----------------------------------- FINALIZADO ---------------------------------------");
			customConnection.finishConnection();
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			try {
				customConnection.closeConnection();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	public static void fixPessoa(Integer a){
		Connection connection = Conexao.conectar();
		try {
			connection.setAutoCommit(false);
			System.out.println("Iniciando...");
			//Atualiza o tipo de pessoa de acordo com a quantidade de caracteres;
			updatePessoa(connection, TipoPessoaEnum.FISICA.getKey(), 11);
			updatePessoa(connection, TipoPessoaEnum.JURIDICA.getKey(), 14);
			System.out.println("Fim.");
			connection.commit();
		} catch (Exception e) {
			Conexao.rollback(connection);
			System.out.println(e.getMessage());
		} finally {
			Conexao.desconectar(connection);
		}
	}

	public static void updatePessoa(Connection connection, int tpPessoa, int qtdCaracteres) throws Exception {
		try {
			PreparedStatement pstmt = connection.prepareStatement(
					"UPDATE mob_ait SET tp_pessoa_proprietario = ? WHERE nr_cpf_cnpj_proprietario IS NOT NULL AND LENGTH(nr_cpf_cnpj_proprietario) = ?");
			pstmt.setInt(1, tpPessoa);
			pstmt.setInt(2, qtdCaracteres);
			int result = pstmt.executeUpdate();
			if (result < 0) {
				Conexao.rollback(connection);
				throw new Exception("Erro ao atualizar registro");
			}
		} catch (SQLException e) {
			throw new Exception("Erro ao atualizar registro: " + e.getMessage());
		}
	}

	public void gerarRelatorioTXTNmMunicipio() throws Exception {
		CustomConnection customConnection = new CustomConnection();
		try {
			ManagerLog managerLog = (ManagerLog) BeansFactory.get(ManagerLog.class);
			customConnection = new CustomConnection();
			customConnection.initConnection(false);
			managerLog.info("================================== FAZENDO BUSCA ========================================",
					"");
			ResultSetMap rsm = new ResultSetMap(customConnection.getConnection().prepareStatement(
					"SELECT nm_municipio, nm_uf, STRING_AGG(cod_municipio::TEXT, ',') AS duplicate_codes, COUNT(*) AS count "
							+ "FROM municipio " + "GROUP BY nm_municipio, nm_uf " + "HAVING COUNT(*) > 1")
					.executeQuery());
			managerLog.info(rsm.toString(), "");
			managerLog.info(
					"================================== GERANDO ARQUIVO ========================================", "");
			String nmOrgao = ParametroServices.getValorOfParametro("NM_ORGAO");
			Path filePath = Paths.get("relatorioCidadesDuplicadas" + nmOrgao + ".txt");
			try (BufferedWriter writer = Files.newBufferedWriter(filePath, StandardCharsets.ISO_8859_1)) {
				while (rsm.next()) {
					String nmMunicipio = rsm.getString("nm_municipio");
					String uf = rsm.getString("nm_uf");
					String cdDuplicados = rsm.getString("duplicate_codes");
					int count = rsm.getInt("count");
					writer.write(String.format("Cidade: %s%nUF: %s%nContagem: %d%nCod.: %s%n%n", nmMunicipio, uf, count,
							cdDuplicados));
				}
			}
			byte[] fileContent = Files.readAllBytes(filePath);
			managerLog.info(
					"================================== ARQUIVO GERADO ========================================", "");
			String fileContentString = new String(fileContent, StandardCharsets.ISO_8859_1);
			managerLog.info("Conteúdo do Arquivo:\n" + fileContentString, "");
			customConnection.finishConnection();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			customConnection.closeConnection();
		}
	}
	
	public static void alterarPrazoVencimentoAit(Integer a) {
		GregorianCalendar novoPrazo = new GregorianCalendar(2024, 3, 15);
		int cdUsuario = 39;
		CustomConnection customConnection = null;
		System.out.println("----------------------------------- INICIADO ---------------------------------------");
		try {
			customConnection = new CustomConnection();
			customConnection.initConnection(true);
			PreparedStatement pst = customConnection.getConnection().prepareStatement(
					" SELECT DISTINCT(A.cd_ait), B.ds_saida ->> 'mensagemRetorno' AS mensagemRetorno, B.cd_arquivo_movimento, "
						   + " B.cd_movimento, B.nr_erro, A.id_ait, A.dt_infracao, A.tp_cnh_condutor, A.nr_cnh_autuacao, A.uf_cnh_autuacao, A.cd_equipamento " 
						   + "	FROM mob_ait A " 
						   + " 	LEFT OUTER JOIN mob_arquivo_movimento B ON (A.cd_ait = B.cd_ait) "   
						   + " 	JOIN mob_ait_movimento C ON (A.cd_ait = C.cd_ait) "
						   + " 		WHERE A.cd_ait NOT IN ( "
						   + "			SELECT C.cd_ait FROM mob_ait_movimento C" 
						   + "				WHERE C.tp_status IN (" + TipoStatusEnum.CADASTRO_CANCELADO.getKey()
						   + "					, " + TipoStatusEnum.CANCELA_REGISTRO_MULTA.getKey()
						   + "			    	, " + TipoStatusEnum.CANCELAMENTO_AUTUACAO.getKey()
						   + "			   		," + TipoStatusEnum.CANCELAMENTO_MULTA.getKey()
						   + "			    	, " + TipoStatusEnum.MULTA_PAGA.getKey()
						   + "				)"
						   + "				AND C.lg_enviado_detran = " + TipoLgEnviadoDetranEnum.REGISTRADO.getKey()
						   + "				AND C.cd_ait = A.cd_ait"
						   + "	)"
						   + "	AND C.tp_status = " + TipoStatusEnum.NIP_ENVIADA.getKey()
						   + "  AND C.lg_enviado_detran = " + TipoLgEnviadoDetranEnum.NAO_ENVIADO.getKey()
						   + " 	AND	B.ds_saida ->> 'mensagemRetorno' = '3322   Data emissAo notificaCAo penalidade   ou   data vencimento' ");
			System.out.println("SQL : " + pst);
			ResultSetMap rsm = new ResultSetMap(pst.executeQuery());
			System.out.println("Resultado consulta quantidade: " + rsm.getLines().size());
			while (rsm.next()) {
				try {
					System.out.println("Alterando data CD AIT: " + rsm.getString("CD_AIT"));
					System.out.println("Alterando aata ID AIT: " + rsm.getString("ID_AIT"));
					AlteraPrazoRecursoDTO alterarPrazoRecurso = new AlteraPrazoRecursoDTO();
					alterarPrazoRecurso.setCdAit(rsm.getInt("CD_AIT"));
					alterarPrazoRecurso.setCdUsuario(cdUsuario);
					alterarPrazoRecurso.setNovoPrazoRecurso(novoPrazo);
					alterarPrazoRecurso.setTipoRecurso(TipoNotificacaoEnum.NOVO_PRAZO_JARI.getKey());
					new ServicoDetranServicesMG().alterarPrazoRecurso(alterarPrazoRecurso);
				}
				catch (Exception e) {
					System.out.println("XXXXXXXXXXXXXXXXXX ERROR XXXXXXXXXXXXXXXXXXXXX");
					System.out.println("Não foi possível atualizar o AIT.");
				}
			}
			System.out.println("Total de AITs: " + rsm.size());
			System.out.println("----------------------------------- FINALIZADO ---------------------------------------");
			customConnection.finishConnection();
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			try {
				customConnection.closeConnection();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	public static void corrigirLacre(Integer a) {
		CustomConnection customConnection = null;
		System.out.println("----------------------------------- INICIADO ---------------------------------------");
		try {
			customConnection = new CustomConnection();
			customConnection.initConnection(true);
			System.out.println(customConnection.getConnection());
			PreparedStatement pst = customConnection.getConnection().prepareStatement(
					" select ma.cd_ait, ma.dt_infracao, ma.id_ait, ma.dt_afericao, ma.cd_equipamento, ma.nr_lacre  from mob_ait ma "
					+ "	join grl_equipamento ge ON (ma.cd_equipamento = ge.cd_equipamento)"
					+ "	where (ma.dt_afericao is null or ma.nr_lacre is null) "
					+ "	and ma.cd_equipamento is not null  "
					+ "	and ge.tp_equipamento = 2"
					+ "	order by ma.dt_infracao desc");
			System.out.println("SQL : " + pst);
			ResultSetMap rsm = new ResultSetMap(pst.executeQuery());
			System.out.println("Resultado consulta quantidade: " + rsm.getLines().size());
			while (rsm.next()) {
				try {
					Ait ait = AitDAO.get(rsm.getInt("cd_ait"), customConnection.getConnection());
					Equipamento equipamento = EquipamentoDAO.get(rsm.getInt("cd_equipamento"), customConnection.getConnection());
					ait.setDtAfericao(equipamento.getDtAfericao());
					ait.setNrLacre(equipamento.getNrLacre());
					AitDAO.update(ait, customConnection.getConnection());
					System.out.println("Ait atualizado: " + ait.getIdAit() + " - " + com.tivic.sol.util.date.DateUtil.formatDate(ait.getDtAfericao(), "dd/MM/yyyy") + " - " + ait.getNrLacre());
				}
				catch (Exception e) {
					System.out.println("XXXXXXXXXXXXXXXXXX ERROR XXXXXXXXXXXXXXXXXXXXX");
					System.out.println("Não foi possível atualizar o AIT.");
				}
			}
			System.out.println("Total de AITs: " + rsm.size());
      System.out.println("----------------------------------- FINALIZADO ---------------------------------------");
			customConnection.finishConnection();
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			try {
				customConnection.closeConnection();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public static void fixRegistroInfracaoArquivoMovimento(Integer a) {
		CustomConnection customConnection = null;
		try {
			customConnection = new CustomConnection();
			customConnection.initConnection(true);
			System.out.println("----------------------------------- INICIADO ---------------------------------------");
				ResultSetMap rsm = new ResultSetMap(customConnection.getConnection().prepareStatement(
						" SELECT DISTINCT(B.cd_ait), A.ds_saida ->> 'mensagemRetorno' AS mensagemRetorno, "
						+ " A.cd_arquivo_movimento, A.cd_movimento, A.dt_arquivo, A.nr_erro "
						+ " FROM mob_arquivo_movimento A "
						+ " 	JOIN mob_ait B ON (A.cd_ait = B.cd_ait) "
						+ " 	JOIN mob_ait_movimento C ON (A.cd_ait = C.cd_ait) "
						+ " WHERE A.tp_status = " + TipoStatusEnum.REGISTRO_INFRACAO.getKey()
						+ " AND A.nr_erro = '9' "
						+ "	AND A.ds_saida ->> 'mensagemRetorno' = 'AIT ja cadastrado' "
						+ " AND B.cd_movimento_atual = 1 "
						+ "	AND DATE(B.dt_infracao) >= '2024-01-01' "
				).executeQuery());
				if (rsm.size() == 0) {
				    System.out.println("Nenhum registro de infração para corrigir.");
				    return; 
				}
				while (rsm.next()) {
				    try {
				        Ait ait = AitDAO.get(rsm.getInt("cd_ait"), customConnection.getConnection());
				        com.tivic.manager.mob.AitMovimento aitMovimento = AitMovimentoDAO.get(rsm.getInt("cd_movimento"), ait.getCdAit(), customConnection.getConnection());
				        ArquivoMovimento arquivoMovimento = ArquivoMovimentoDAO.get(rsm.getInt("cd_arquivo_movimento"), rsm.getInt("cd_movimento"), ait.getCdAit(), customConnection.getConnection());

				        System.out.println("<----------- Registros: ----------->");
				        System.out.println("código do AIT: " + ait.getCdAit() + " | " + "Cód. Erro: " + arquivoMovimento.getNrErro() + " | " + "Status: " + arquivoMovimento.getTpStatus());

				        ArquivoMovimentoDAO.delete(arquivoMovimento.getCdArquivoMovimento(), arquivoMovimento.getCdMovimento(), ait.getCdAit(), customConnection.getConnection());
				        ait.setNrErro(null);
				        AitDAO.update(ait, customConnection.getConnection());     
				        aitMovimento.setNrErro(null);
				        aitMovimento.setLgEnviadoDetran(TipoLgEnviadoDetranEnum.REGISTRADO.getKey());
				        aitMovimento.setDtRegistroDetran(arquivoMovimento.getDtArquivo());
				        AitMovimentoDAO.update(aitMovimento, customConnection.getConnection());
				    } catch (Exception e) {
				        System.out.println("Erro ao processar para o AIT: " + rsm.getInt("cd_ait"));
				        e.printStackTrace();
				        continue;
				    }
				}
		    System.out.println("Total: " + rsm.size());
			  System.out.println("----------------------------------- FINALIZADO ---------------------------------------");
			  customConnection.finishConnection();
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			try {
				customConnection.closeConnection();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public static void importarAutosNipGct(Integer a) {
		Connection connection = Conexao.conectar();
		
		try {
			connection.setAutoCommit(false);
			ResultSetMap rsm = ResultSetMap.getResultsetMapFromCSVFile("/home/gabriel/Documentos/migracao_itauna/gct/itauna_backup_2024020210000/autos.csv", ";", true);
			int x = 0;
			int autosEncontrados = 0;
			int autosEncontradosComNip = 0;
			int autosEncontradosFaltandoNip = 0;
			int autosEncontradosSemNip = 0;
			int autosNaoEncontrados = 0;
			String aitsComNipInserida = "";
			HashMap<String, Integer> map = new HashMap<>();
			ResultSetMap rsmAits = AitDAO.getAll();
			while (rsm.next()) {
				
				System.out.println(rsm.getString("SERIE") + " - " + rsm.getString("AUTO") + " - " + rsm.getString("DATA_HORA_INFRACAO"));
				System.out.println((x++) + " || " + autosEncontrados + "/" + autosNaoEncontrados);
				String AIT = null;
				
				if(rsm.getString("SERIE").equals("AG"))
					continue;
				else {
					int quantidadeCaracteres0 = 10 - rsm.getString("SERIE").length() - rsm.getString("AUTO").length();
					String idAit = rsm.getString("SERIE");
					for(int i = 0; i < quantidadeCaracteres0; i++) {
						idAit += "0";
					}
					idAit += rsm.getString("AUTO");
					System.out.println("ID ait pesquisado: " + idAit);
					AIT = idAit;
				}
				
				boolean encontrado = false;
				Ait ait = null;
				while(rsmAits.next()) {
					if(rsmAits.getString("nr_ait").equals(AIT) || rsmAits.getString("id_ait").equals(AIT)) {
						encontrado = true;
						ait = AitDAO.get(rsmAits.getInt("cd_ait"), connection);
						break;
					}
					
				}
				rsmAits.beforeFirst();
				
				if(encontrado) {
					autosEncontrados++;
					
					if(rsm.getString("DATA_IMPRESSAO_PENALIDADE") != null && !rsm.getString("DATA_IMPRESSAO_PENALIDADE").trim().equals("")) {
						System.out.println("---tenho NIP: " + rsm.getString("DATA_IMPRESSAO_PENALIDADE"));
						autosEncontradosComNip++;
						
						ResultSetMap rsmMovimentoNip = new ResultSetMap(connection.prepareStatement("SELECT * FROM mob_ait_movimento WHERE cd_ait = " + ait.getCdAit() + " AND tp_status = " + AitMovimentoServices.NIP_ENVIADA).executeQuery());
						if(!rsmMovimentoNip.next()) {
							autosEncontradosFaltandoNip++;
							aitsComNipInserida += ait.getIdAit() + ", ";
							System.out.println(rsm.getString("DATA_IMPRESSAO_PENALIDADE"));
							System.out.println(rsm.getString("DATA_IMPRESSAO_PENALIDADE").substring(1, 4));
							System.out.println(rsm.getString("DATA_IMPRESSAO_PENALIDADE").substring(5, 7));
							System.out.println(rsm.getString("DATA_IMPRESSAO_PENALIDADE").substring(8, 10));
							GregorianCalendar dtNip = new GregorianCalendar(Integer.parseInt(rsm.getString("DATA_IMPRESSAO_PENALIDADE").substring(1, 4)), Integer.parseInt(rsm.getString("DATA_IMPRESSAO_PENALIDADE").substring(5, 7))-1, Integer.parseInt(rsm.getString("DATA_IMPRESSAO_PENALIDADE").substring(8, 10)));
				            com.tivic.manager.mob.AitMovimento aitMovimento = new com.tivic.manager.mob.AitMovimento();
							aitMovimento.setCdAit(ait.getCdAit());
							aitMovimento.setTpStatus(AitMovimentoServices.NIP_ENVIADA);
							aitMovimento.setDtMovimento(dtNip);
							aitMovimento.setDtDigitacao(com.tivic.sol.util.date.DateUtil.getDataAtual());
							aitMovimento.setLgEnviadoDetran(1);
							System.out.println("Data do Movimento de NIP = " + aitMovimento.getDtMovimento());
							AitMovimentoDAO.insert(aitMovimento, connection);
						}
					}
					else {
						autosEncontradosSemNip++;
					}
					
				}
				else {
					autosNaoEncontrados++;		
				}
				
			}
			System.out.println("Auto encontrados: " + autosEncontrados);
			System.out.println("Auto não encontrados: " + autosNaoEncontrados);
			System.out.println("Auto encontrados com NIP: " + autosEncontradosComNip);
			System.out.println("Auto com NIP faltando: " + autosEncontradosFaltandoNip + " - " + aitsComNipInserida);
			System.out.println("Auto encontrados sem NIP: " + autosEncontradosSemNip);
			System.out.println(map);
			
			connection.commit();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void importarPublicacoesGct(Integer a) {
		Connection connection = Conexao.conectar();
		
		try {
			connection.setAutoCommit(false);
			ResultSetMap rsm = ResultSetMap.getResultsetMapFromCSVFile("/home/gabriel/Documentos/migracao_itauna/gct/itauna_backup_2024020210000/publicacao.csv", ";", true);
			int x = 0;
			int autosEncontrados = 0;
			int autosEncontradosComNai = 0;
			int autosEncontradosComNip = 0;
			int autosEncontradosFaltandoPublicacaoNai = 0;
			int autosEncontradosFaltandoPublicacaoNip = 0;
			int autosNaoEncontrados = 0;
			String aitsComPublicacaoNaiInserida = "";
			String aitsComPublicacaoNipInserida = "";
			HashMap<String, Integer> map = new HashMap<>();
			ResultSetMap rsmAits = AitDAO.getAll();
			while (rsm.next()) {
				
				System.out.println(rsm.getString("FAR_AIT") + " - " + rsm.getString("TIPO") + " - " + rsm.getString("DATA_EMISSAO"));
				System.out.println((x++) + " || " + autosEncontrados + "/" + autosNaoEncontrados);
				String idAit = rsm.getString("FAR_AIT");
				boolean encontrado = false;
				Ait ait = null;
				while(rsmAits.next()) {
					if(rsmAits.getString("nr_ait").equals(idAit) || rsmAits.getString("id_ait").equals(idAit)) {
						encontrado = true;
						ait = AitDAO.get(rsmAits.getInt("cd_ait"), connection);
						break;
					}
					
				}
				rsmAits.beforeFirst();
				
				if(encontrado) {
					autosEncontrados++;
					
					if(rsm.getString("TIPO").equals("NIP")) {
						autosEncontradosComNip++;
						
						ResultSetMap rsmMovimentoPublicacaoNip = new ResultSetMap(connection.prepareStatement("SELECT * FROM mob_ait_movimento WHERE cd_ait = " + ait.getCdAit() + " AND tp_status = " + AitMovimentoServices.PUBLICACAO_NIP).executeQuery());
						if(!rsmMovimentoPublicacaoNip.next()) {
							autosEncontradosFaltandoPublicacaoNip++;
							aitsComPublicacaoNipInserida += ait.getIdAit() + ", ";
							GregorianCalendar dtPublicacaoNip = new GregorianCalendar(Integer.parseInt(rsm.getString("DATA_EMISSAO").substring(1, 4)), Integer.parseInt(rsm.getString("DATA_EMISSAO").substring(5, 7))-1, Integer.parseInt(rsm.getString("DATA_EMISSAO").substring(8, 10)));
				            com.tivic.manager.mob.AitMovimento aitMovimento = new com.tivic.manager.mob.AitMovimento();
							aitMovimento.setCdAit(ait.getCdAit());
							aitMovimento.setTpStatus(AitMovimentoServices.PUBLICACAO_NIP);
							aitMovimento.setDtMovimento(dtPublicacaoNip);
							aitMovimento.setDtDigitacao(com.tivic.sol.util.date.DateUtil.getDataAtual());
							aitMovimento.setLgEnviadoDetran(1);
							AitMovimentoDAO.insert(aitMovimento, connection);
							
							ResultSetMap rsmMovimentoNip = new ResultSetMap(connection.prepareStatement("SELECT * FROM mob_ait_movimento WHERE cd_ait = " + ait.getCdAit() + " AND tp_status = " + AitMovimentoServices.NIP_ENVIADA).executeQuery());
							if(rsmMovimentoNip.next()) {
								com.tivic.manager.mob.AitMovimento movimentoNip = AitMovimentoDAO.get(rsmMovimentoNip.getInt("cd_movimento"), rsmMovimentoNip.getInt("cd_ait"), connection);
								movimentoNip.setDtPublicacaoDo(dtPublicacaoNip);
								AitMovimentoDAO.update(movimentoNip, connection);
							}
						}
					}
					else if(rsm.getString("TIPO").equals("NAI")) {
						autosEncontradosComNai++;
					
						ResultSetMap rsmMovimentoPublicacaoNai = new ResultSetMap(connection.prepareStatement("SELECT * FROM mob_ait_movimento WHERE cd_ait = " + ait.getCdAit() + " AND tp_status = " + AitMovimentoServices.PUBLICACAO_NAI).executeQuery());
						if(!rsmMovimentoPublicacaoNai.next()) {
							autosEncontradosFaltandoPublicacaoNai++;
							aitsComPublicacaoNaiInserida += ait.getIdAit() + ", ";
							GregorianCalendar dtPublicacaoNai = new GregorianCalendar(Integer.parseInt(rsm.getString("DATA_EMISSAO").substring(1, 4)), Integer.parseInt(rsm.getString("DATA_EMISSAO").substring(5, 7))-1, Integer.parseInt(rsm.getString("DATA_EMISSAO").substring(8, 10)));
				            com.tivic.manager.mob.AitMovimento aitMovimento = new com.tivic.manager.mob.AitMovimento();
							aitMovimento.setCdAit(ait.getCdAit());
							aitMovimento.setTpStatus(AitMovimentoServices.PUBLICACAO_NAI);
							aitMovimento.setDtMovimento(dtPublicacaoNai);
							aitMovimento.setDtDigitacao(com.tivic.sol.util.date.DateUtil.getDataAtual());
							aitMovimento.setLgEnviadoDetran(1);
							AitMovimentoDAO.insert(aitMovimento, connection);
							
							ResultSetMap rsmMovimentoNai = new ResultSetMap(connection.prepareStatement("SELECT * FROM mob_ait_movimento WHERE cd_ait = " + ait.getCdAit() + " AND tp_status = " + AitMovimentoServices.NAI_ENVIADO).executeQuery());
							if(rsmMovimentoNai.next()) {
								com.tivic.manager.mob.AitMovimento movimentoNai = AitMovimentoDAO.get(rsmMovimentoNai.getInt("cd_movimento"), rsmMovimentoNai.getInt("cd_ait"), connection);
								movimentoNai.setDtPublicacaoDo(dtPublicacaoNai);
								AitMovimentoDAO.update(movimentoNai, connection);
							}
						}
					}
					
				}
				else {
					autosNaoEncontrados++;		
				}
				
			}
			System.out.println("Auto encontrados: " + autosEncontrados);
			System.out.println("Auto não encontrados: " + autosNaoEncontrados);
			System.out.println("Auto encontrados com NAI: " + autosEncontradosComNai);
			System.out.println("Auto com Publicação de NAI faltando: " + autosEncontradosFaltandoPublicacaoNai + " - " + aitsComPublicacaoNaiInserida);
			System.out.println("Auto encontrados com NIP: " + autosEncontradosComNip);
			System.out.println("Auto com Publicação de NIP faltando: " + autosEncontradosFaltandoPublicacaoNip + " - " + aitsComPublicacaoNipInserida);
			System.out.println(map);
			
			connection.commit();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	
	public static void importarAdvertenciasGct(Integer a) {
		Connection connection = Conexao.conectar();
		
		try {
			connection.setAutoCommit(false);
			ResultSetMap rsm = ResultSetMap.getResultsetMapFromCSVFile("/home/gabriel/Documentos/migracao_itauna/gct/itauna_backup_2024020210000/advertencia.csv", ";", true);
			int x = 0;
			int autosEncontrados = 0;
			int autosFaltandoAdvertencia = 0;
			int autosAdvertenciaEntrada = 0;
			int autosAdvertenciaDeferida = 0;
			int autosAdvertenciaIndeferida = 0;
			int autosNaoEncontrados = 0;
			String aitsComAdvertenciaInserida = "";
			ResultSetMap rsmAits = AitDAO.getAll();
			while (rsm.next()) {
				
				System.out.println(rsm.getString("ANT_SERIE") + " - " + rsm.getString("ANT_AUTO") + " - " + rsm.getString("DATA_ENTRADA"));
				System.out.println((x++) + " || " + autosEncontrados + "/" + autosNaoEncontrados + "/" + autosAdvertenciaEntrada + "/" + autosAdvertenciaDeferida + "/" + autosAdvertenciaIndeferida);
				String AIT = null;
				
				int quantidadeCaracteres0 = 10 - rsm.getString("ANT_SERIE").length() - rsm.getString("ANT_AUTO").length();
				String idAit = rsm.getString("ANT_SERIE");
				for(int i = 0; i < quantidadeCaracteres0; i++) {
					idAit += "0";
				}
				idAit += rsm.getString("ANT_AUTO");
				System.out.println("ID ait pesquisado: " + idAit);
				AIT = idAit;
				
				boolean encontrado = false;
				Ait ait = null;
				while(rsmAits.next()) {
					if(rsmAits.getString("nr_ait").equals(AIT) || rsmAits.getString("id_ait").equals(AIT)) {
						encontrado = true;
						ait = AitDAO.get(rsmAits.getInt("cd_ait"), connection);
						break;
					}
					
				}
				rsmAits.beforeFirst();
				
				if(encontrado) {
					autosEncontrados++;
					
					if(rsm.getString("CODIGO_MOVIMENTACAO").equals("104") || rsm.getString("CODIGO_MOVIMENTACAO").equals("105") || rsm.getString("CODIGO_MOVIMENTACAO").equals("106")) {
						
						aitsComAdvertenciaInserida += ait.getIdAit() + ", ";
						
						ResultSetMap rsmMovimentoAdvertenciaEntrada = new ResultSetMap(connection.prepareStatement("SELECT * FROM mob_ait_movimento WHERE cd_ait = " + ait.getCdAit() + " AND tp_status IN (" + AitMovimentoServices.ADVERTENCIA_DEFESA_ENTRADA + ")").executeQuery());
						if(!rsmMovimentoAdvertenciaEntrada.next()) {
						
							autosAdvertenciaEntrada++;
							GregorianCalendar dtEntrada = new GregorianCalendar(Integer.parseInt(rsm.getString("DATA_ENTRADA").substring(1, 4)), Integer.parseInt(rsm.getString("DATA_ENTRADA").substring(5, 7))-1, Integer.parseInt(rsm.getString("DATA_ENTRADA").substring(8, 10)));
							com.tivic.manager.mob.AitMovimento aitMovimentoEntrada = new com.tivic.manager.mob.AitMovimento();
							aitMovimentoEntrada.setCdAit(ait.getCdAit());
							aitMovimentoEntrada.setTpStatus( AitMovimentoServices.ADVERTENCIA_DEFESA_ENTRADA);
							aitMovimentoEntrada.setDtMovimento(dtEntrada);
							aitMovimentoEntrada.setDtDigitacao(com.tivic.sol.util.date.DateUtil.getDataAtual());
							aitMovimentoEntrada.setLgEnviadoDetran(1);
							AitMovimentoDAO.insert(aitMovimentoEntrada, connection);
						}						
						
						if(rsm.getString("CODIGO_MOVIMENTACAO").equals("105")) {
							ResultSetMap rsmMovimentoAdvertenciaDeferido = new ResultSetMap(connection.prepareStatement("SELECT * FROM mob_ait_movimento WHERE cd_ait = " + ait.getCdAit() + " AND tp_status IN (" + AitMovimentoServices.ADVERTENCIA_DEFESA_DEFERIDA + ")").executeQuery());
							if(!rsmMovimentoAdvertenciaDeferido.next()) {
								autosAdvertenciaDeferida++;
								
								GregorianCalendar dtDeferida = new GregorianCalendar(Integer.parseInt(rsm.getString("DATA_DECISAO").substring(1, 4)), Integer.parseInt(rsm.getString("DATA_DECISAO").substring(5, 7))-1, Integer.parseInt(rsm.getString("DATA_DECISAO").substring(8, 10)));
								com.tivic.manager.mob.AitMovimento aitMovimentoDeferida = new com.tivic.manager.mob.AitMovimento();
								aitMovimentoDeferida.setCdAit(ait.getCdAit());
								aitMovimentoDeferida.setTpStatus( AitMovimentoServices.ADVERTENCIA_DEFESA_DEFERIDA);
								aitMovimentoDeferida.setDtMovimento(dtDeferida);
								aitMovimentoDeferida.setDtDigitacao(com.tivic.sol.util.date.DateUtil.getDataAtual());
								aitMovimentoDeferida.setLgEnviadoDetran(1);
								AitMovimentoDAO.insert(aitMovimentoDeferida, connection);
						
								ait.setTpStatus(AitMovimentoServices.ADVERTENCIA_DEFESA_DEFERIDA );
								ait.setDtMovimento(dtDeferida);
								
								AitDAO.update(ait, connection);
							}
						} 
						
						if(rsm.getString("CODIGO_MOVIMENTACAO").equals("106")) {
							ResultSetMap rsmMovimentoAdvertenciaIndeferido = new ResultSetMap(connection.prepareStatement("SELECT * FROM mob_ait_movimento WHERE cd_ait = " + ait.getCdAit() + " AND tp_status IN (" + AitMovimentoServices.ADVERTENCIA_DEFESA_INDEFERIDA + ")").executeQuery());
							if(!rsmMovimentoAdvertenciaIndeferido.next()) {
								autosAdvertenciaIndeferida++;	
								
								GregorianCalendar dtIndeferida = new GregorianCalendar(Integer.parseInt(rsm.getString("DATA_DECISAO").substring(1, 4)), Integer.parseInt(rsm.getString("DATA_DECISAO").substring(5, 7))-1, Integer.parseInt(rsm.getString("DATA_DECISAO").substring(8, 10)));
								com.tivic.manager.mob.AitMovimento aitMovimentoIndeferida = new com.tivic.manager.mob.AitMovimento();
								aitMovimentoIndeferida.setCdAit(ait.getCdAit());
								aitMovimentoIndeferida.setTpStatus( AitMovimentoServices.ADVERTENCIA_DEFESA_INDEFERIDA);
								aitMovimentoIndeferida.setDtMovimento(dtIndeferida);
								aitMovimentoIndeferida.setDtDigitacao(com.tivic.sol.util.date.DateUtil.getDataAtual());
								aitMovimentoIndeferida.setLgEnviadoDetran(1);
								AitMovimentoDAO.insert(aitMovimentoIndeferida, connection);
							}
						}
					}
						
					
				}
				else {
					autosNaoEncontrados++;		
				}
				
			}
			System.out.println("Auto encontrados: " + autosEncontrados);
			System.out.println("Auto não encontrados: " + autosNaoEncontrados);
			System.out.println("Auto com NIP faltando: " + autosFaltandoAdvertencia + " - " + aitsComAdvertenciaInserida);
			System.out.println("Auto com advertencia entrada: " + autosAdvertenciaEntrada);
			System.out.println("Auto com advertencia deferida: " + autosAdvertenciaDeferida);
			System.out.println("Auto com advertencia indeferida: " + autosAdvertenciaIndeferida);
			
			//connection.commit();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void enderecoBoat(Integer a) {
	    CustomConnection customConnection = null;
	    try {
	        customConnection = new CustomConnection();
	        customConnection.initConnection(true);
	        System.out.println("----------------------------------- INICIADO ---------------------------------------");

	        Connection connection = customConnection.getConnection();
	        connection.setAutoCommit(false);

	        String selectQuery = "SELECT cd_boat, cd_cidade, ds_local_ocorrencia, ds_ponto_referencia FROM mob_boat";

	        try (PreparedStatement selectStatement = connection.prepareStatement(selectQuery);
	             ResultSet resultSet = selectStatement.executeQuery()) {

	            while (resultSet.next()) {
	                int cdBoat = resultSet.getInt("cd_boat");
	                int cdCidade = resultSet.getInt("cd_cidade");
	                String dsLocalOcorrencia = resultSet.getString("ds_local_ocorrencia");
	                String dsPontoReferencia = resultSet.getString("ds_ponto_referencia");

	                if (cdCidade == 0) {
	                    System.err.println("Registro ignorado: cd_cidade inválido (0) para cd_boat = " + cdBoat);
	                    continue;
	                }

	                String dsLogradouro = dsLocalOcorrencia;
	                String nrEndereco = null;
	                String nmBairro = null;
	                String nrCep = null;
	                String dsComplemento = dsPontoReferencia;

	                if (dsLocalOcorrencia.matches(".*\\d+.*")) {
	                    String[] localInfo = dsLocalOcorrencia.split(" ");
	                    int lastIndex = localInfo.length - 1;
	                    nrEndereco = localInfo[lastIndex];
	                    dsLogradouro = dsLocalOcorrencia.substring(0, dsLocalOcorrencia.lastIndexOf(nrEndereco)).trim();
	                }

	                String insertQuery = "INSERT INTO grl_endereco (cd_cidade, ds_logradouro, nr_endereco, nm_bairro, nr_cep, ds_complemento) " +
	                                     "VALUES (?, ?, ?, ?, ?, ?) RETURNING cd_endereco";
	                try (PreparedStatement insertStatement = connection.prepareStatement(insertQuery)) {
	                    insertStatement.setInt(1, cdCidade);
	                    insertStatement.setString(2, dsLogradouro);
	                    insertStatement.setString(3, nrEndereco);
	                    insertStatement.setString(4, nmBairro);
	                    insertStatement.setString(5, nrCep);
	                    insertStatement.setString(6, dsComplemento);
	                    ResultSet generatedKeys = insertStatement.executeQuery();

	                    if (generatedKeys.next()) {
	                        int cdEndereco = generatedKeys.getInt("cd_endereco");
	                        System.out.println("Inserido na grl_endereco: cd_endereco = " + cdEndereco);

	                        String insertIntoGrlEnderecoBoatQuery = "INSERT INTO grl_endereco_boat (cd_endereco, cd_boat) VALUES (?, ?)";
	                        try (PreparedStatement insertIntoGrlEnderecoBoatStatement = connection.prepareStatement(insertIntoGrlEnderecoBoatQuery)) {
	                            insertIntoGrlEnderecoBoatStatement.setInt(1, cdEndereco);
	                            insertIntoGrlEnderecoBoatStatement.setInt(2, cdBoat);
	                            insertIntoGrlEnderecoBoatStatement.executeUpdate();
	                            System.out.println("Inserido na grl_endereco_boat: cd_endereco = " + cdEndereco + ", cd_boat = " + cdBoat);
	                        }
	                    } else {
	                        throw new SQLException("Falha ao inserir registro na tabela grl_endereco, nenhum cd_endereco gerado.");
	                    }
	                }
	            }
	        }

	        connection.commit();
	        System.out.println("----------------------------------- FINALIZADO ---------------------------------------");

	    } catch (Exception e) {
	        e.printStackTrace();
	        if (customConnection != null) {
	            try {
	                customConnection.getConnection().rollback();
	            } catch (Exception ex) {
	                ex.printStackTrace();
	            }
	        }
	    } finally {
	        if (customConnection != null) {
	            try {
	                customConnection.finishConnection();
	            } catch (Exception ex) {
	                ex.printStackTrace();
	            }
	        }
	    }
	}

	public static void importarNrImovelArquivoCsv(Integer a) {
	    Connection connection = Conexao.conectar();

	    try (BufferedReader br = new BufferedReader(new FileReader("C:\\Users\\Ricardo Almeida\\Documents\\importacao_ouro_preto\\retorno-inclusao-aits.csv"))) {
	        connection.setAutoCommit(false);

	        String linha;
	        int encontrados = 0;
	        int naoEncontrados = 0;
	        int ignorados = 0;

	        String cabecalho = br.readLine();
	        if (cabecalho == null) {
	            System.out.println("Arquivo CSV vazio.");
	            return;
	        }

	        String[] colunas = cabecalho.split(",");
	        Map<String, Integer> indices = new HashMap<>();
	        for (int i = 0; i < colunas.length; i++) {
	            indices.put(colunas[i].trim().toLowerCase(), i);
	        }

	        Integer idxIdAit = indices.get("id_ait"); 
	        Integer idxNrImovel = indices.get("ds_nr_imovel"); 

	        if (idxIdAit == null || idxNrImovel == null) {
	            System.out.println("Colunas obrigatórias não encontradas no cabeçalho.");
	            return;
	        }

	        while ((linha = br.readLine()) != null) {
	            String[] valores = linha.split(",");

	            String idAitCsv = valores.length > idxIdAit ? valores[idxIdAit].trim() : null;
	            String nrImovelCsv = valores.length > idxNrImovel ? valores[idxNrImovel].trim() : null;

	            if (nrImovelCsv == null || nrImovelCsv.isEmpty() || nrImovelCsv.equals("\\N")) {
	                ignorados++;
	                continue;
	            }

	            if (nrImovelCsv.length() > 8) {
	                System.out.println("Truncando Nº Imóvel: " + nrImovelCsv);
	                nrImovelCsv = nrImovelCsv.substring(0, 8);
	            }

	            PreparedStatement ps = connection.prepareStatement(
	                "SELECT cd_ait, id_ait, ds_nr_imovel FROM mob_ait "
	              + "WHERE id_ait = ? "
	            );
	            ps.setString(1, idAitCsv);
	            ResultSet rs = ps.executeQuery();

	            if (rs.next()) {
	            	String dsNrImovelAtual = rs.getString("ds_nr_imovel");
	                
	                if (dsNrImovelAtual == null || dsNrImovelAtual.trim().isEmpty()) {
	                    System.out.println("Encontrado AIT no banco. id_ait_csv: " + idAitCsv +
	                                       " | id_ait_banco: " + rs.getString("ID_AIT") +
	                                       " | nr_imovel_csv: " + nrImovelCsv);
	                    
	                    Ait ait = AitDAO.get(rs.getInt("CD_AIT"), connection);
	                    ait.setDsNrImovel(nrImovelCsv);
	                    AitDAO.update(ait, connection);
	                    encontrados++;
	                } else {
	                    System.out.println("Registro ignorado: AIT " + idAitCsv + " já possui Nº Imóvel preenchido: " + dsNrImovelAtual);
	                    ignorados++;
	                }
	            } else {
	                System.out.println("AIT NÃO encontrado no banco: " + idAitCsv);
	                naoEncontrados++;
	            }
	            rs.close();
	            ps.close();
	        }

	        System.out.println("Total encontrados: " + encontrados);
	        System.out.println("Total não encontrados: " + naoEncontrados);
	        System.out.println("Total ignorados por estar vazio: " + ignorados);

	        connection.commit();

	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}
	
	public static void importarECriarNicrquivoCsv(Integer a) {
	    Connection connection = Conexao.conectar();
	    int usuarioMaster = ParametroServices.getValorOfParametroAsInteger("CD_USUARIO_MASTER", 1);
	    try (BufferedReader br = new BufferedReader(new FileReader("C:\\Users\\Ricardo Almeida\\Documents\\importacao_ouro_preto\\nics.csv"))) {
	        connection.setAutoCommit(false);
	        
	        String linha;
	        int encontrados = 0;
	        int naoEncontrados = 0;
	        int ignorados = 0;

	        while ((linha = br.readLine()) != null) {	

	        	String[] indice = linha.split(",");

	            String prefixoIdAit = indice.length > 3 ? indice[3].trim() : null;
	            String sufixoIdAit = indice.length > 4 ? indice[4].trim() : null;
	        	
	        	String idAitCsv = (prefixoIdAit == null ? "" : prefixoIdAit.trim()) + (sufixoIdAit  == null ? "" : sufixoIdAit.trim());
	            if (idAitCsv == null || idAitCsv.trim().isEmpty()) {
	                ignorados++;
	                continue;
	            }

	            PreparedStatement ps = connection.prepareStatement(
	                    " SELECT A.cd_ait, A.id_ait, C.nr_cod_detran, B.cd_movimento FROM mob_ait A "
	    	            + " JOIN mob_ait_movimento B ON (A.cd_ait = B.cd_ait) "
	                    + " JOIN mob_infracao C ON (A.cd_infracao = C.cd_infracao) "
	                    + " WHERE A.id_ait = ? "
	                    + " AND B.tp_status = " + TipoStatusEnum.REGISTRO_INFRACAO.getKey()
	                    + " AND C.nr_cod_detran = 50020"
	                    + " AND NOT EXISTS ("
	                    + " 	SELECT B2.cd_ait FROM mob_ait_movimento B2 WHERE B2.cd_ait = A.cd_ait "
	                    + " 	AND B2.tp_status = " + TipoStatusEnum.NIC_ENVIADA.getKey() + ""
	                    + " ); "
	            );
	            ps.setString(1, idAitCsv.trim());
	            ResultSet rs = ps.executeQuery();

	            if (rs.next()) {
	                System.out.println("Encontrado AIT no banco. id_ait_csv: " + idAitCsv + " | id_ait_banco: " + rs.getString("ID_AIT") + " | nr_cod_detran: " + rs.getString("NR_COD_DETRAN"));
	                com.tivic.manager.mob.AitMovimento aitmovimento = AitMovimentoDAO.get(rs.getInt("CD_MOVIMENTO"), rs.getInt("CD_AIT"), connection);
	                com.tivic.manager.mob.AitMovimento movimentoNic = new AitMovimentoBuilder()
	        				.setCdAit(aitmovimento.getCdAit())
	        				.setDtMovimento(aitmovimento.getDtMovimento())
	        				.setTpStatus(TipoStatusEnum.NIC_ENVIADA.getKey())
	        				.setLgEnviadoDetran(TipoLgEnviadoDetranEnum.REGISTRADO.getKey())
	        				.setDtDigitacao(com.tivic.sol.util.date.DateUtil.getDataAtual())
	        				.setDsObservacao("MOVIMENTO CRIADO POR IMPORTACAÇÃO")
	        				.setCdUsuario(usuarioMaster)
	        			.build();
					AitMovimentoDAO.insert(movimentoNic, connection);
	                encontrados++;
	            } else {
	                System.out.println("AIT NÃO encontrado no banco: " + idAitCsv);
	                naoEncontrados++;
	            }

	            rs.close();
	            ps.close();
	        }

	        System.out.println("Total encontrados: " + encontrados);
	        System.out.println("Total não encontrados: " + naoEncontrados);
	        System.out.println("Total ignorados por ter valor nulo ou vázio: " + ignorados);

	        connection.commit();
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}
	
	public static void corrigirPlacasMinusculas(Integer a) {
		CustomConnection customConnection = null;
		try {
			customConnection = new CustomConnection();
			customConnection.initConnection(true);
			System.out.println("----------------------------------- INICIADO ---------------------------------------");
			ResultSetMap rsm = new ResultSetMap(customConnection.getConnection().prepareStatement(
					" SELECT nr_placa, cd_ait "
					+ " FROM mob_ait "
					+ " WHERE nr_placa ~ '***:(?c)[a-z]'"
			).executeQuery());
			if (rsm.size() == 0) {
			    System.out.println("Nenhuma placa em AITs para corrigir.");
			    return; 
			}
			while (rsm.next()) {
			    try {
			        Ait ait = AitDAO.get(rsm.getInt("cd_ait"), customConnection.getConnection());
	
			        System.out.println("<----------- Registros: ----------->");
			        System.out.println("Código do AIT: " + ait.getCdAit() + " | " + "Número da placa: " + ait.getNrPlaca());
			        
			        ait.setNrPlaca(ait.getNrPlaca().toUpperCase());
			        
			        AitDAO.update(ait, customConnection.getConnection());     
			    } catch (Exception e) {
			        System.out.println("Erro ao processar para o AIT: " + rsm.getInt("cd_ait"));
			        e.printStackTrace();
			        continue;
			    }
			}
			System.out.println("Total: " + rsm.size());
			System.out.println("----------------------------------- FINALIZADO ---------------------------------------");
			customConnection.finishConnection();
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			try {
				customConnection.closeConnection();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
}