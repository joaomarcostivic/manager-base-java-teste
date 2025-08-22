package com.tivic.manager.agd;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;

import com.tivic.sol.connection.Conexao;
import com.tivic.manager.crm.Mailing;
import com.tivic.manager.crm.MailingDAO;
import com.tivic.manager.crm.MailingDestino;
import com.tivic.manager.crm.MailingDestinoDAO;
import com.tivic.manager.crm.MailingEnvioServices;
import com.tivic.manager.crm.MailingPlanejamento;
import com.tivic.manager.crm.MailingPlanejamentoDAO;
import com.tivic.manager.crm.MailingPlanejamentoServices;
import com.tivic.manager.crm.MailingServices;
import com.tivic.manager.grl.EquipamentoPessoa;
import com.tivic.manager.grl.EquipamentoPessoaServices;
import com.tivic.manager.grl.FeriadoServices;
import com.tivic.manager.grl.NumeracaoDocumentoServices;
import com.tivic.manager.grl.ParametroServices;
import com.tivic.manager.ptc.Documento;
import com.tivic.manager.ptc.DocumentoDAO;
import com.tivic.manager.ptc.DocumentoServices;
import com.tivic.manager.seg.AuthData;
import com.tivic.manager.seg.Usuario;
import com.tivic.manager.seg.UsuarioDAO;
import com.tivic.manager.seg.UsuarioServices;
import com.tivic.manager.util.Recursos;
import com.tivic.manager.util.SendMail;
import com.tivic.manager.util.Util;

import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;
import sol.dao.Search;
import sol.util.Result;

public class AgendamentoServices {

	public static final int TMP_MINUTO = 0;
	public static final int TMP_HORA   = 1;
	public static final int TMP_DIA    = 2;
	public static final int TMP_SEMANA = 3;

	/* situacao de agendamentos */
	public static final int ST_TODOS     = -1;
	public static final int ST_PENDENTE  = 0;
	public static final int ST_CONCLUIDO = 1;
	public static final int ST_CANCELADO = 2;

	/* tipos de ordenamento */
	public static final int ORD_CRESCENTE    = 0;
	public static final int ORD_DESCRESCENTE = 1;

	/* tipos de reuniões */
	public static final int TP_REUNIAO_ORDINARIA      = 0;
	public static final int TP_REUNIAO_EXTRAORDINARIA = 1;

	public static final String[] situacao = {"Em andamento","Concluído","Cancelado"};

	public static final String[] opcoesLembrete = {"Nenhum","0 minutos","5 minutos","10 minutos","15 minutos","1 hora","2 horas","3 horas","4 horas",
		   	                                       "5 horas","6 horas","7 horas","8 horas","9 horas","10 horas","11 horas","0,5 dia",
		   	                                       "18 horas","1 dia","2 dias","3 dias","4 dias","1 semana","2 semanas","Dia especifíco"};

	public static final int[][] correlacoesOpcoesLembrete = {new int[] {0, TMP_MINUTO},{5, TMP_MINUTO},{10, TMP_MINUTO},{15, TMP_MINUTO},
																	   {1, TMP_HORA}, {2, TMP_HORA}, {3, TMP_HORA}, {4, TMP_HORA}, {5, TMP_HORA},
																	   {6, TMP_HORA}, {7, TMP_HORA}, {8, TMP_HORA}, {9, TMP_HORA}, {10, TMP_HORA},
																	   {11, TMP_HORA}, {12, TMP_HORA}, {18, TMP_HORA}, {1, TMP_DIA}, {2, TMP_DIA},
																	   {3, TMP_DIA}, {4, TMP_DIA}, {1, TMP_SEMANA}, {2, TMP_SEMANA}, {-1, TMP_MINUTO}};

	/* formas de visualizacao de agenda */
	public static final int AGD_REPORT   = 0;
	public static final int AGD_CALENDAR = 1;

	@Deprecated
	public static ResultSetMap getAllParticipantes(int cdAgendamento) {
		return getAllParticipantes(cdAgendamento, null);
	}

	@Deprecated
	public static ResultSetMap getAllParticipantes(int cdAgendamento, Connection connection) {
		boolean isConnectionNull = connection==null;
		try {
			if (isConnectionNull)
				connection = Conexao.conectar();
			PreparedStatement pstmt = connection.prepareStatement("SELECT A.*, B.nm_login, C.nm_email, C.nm_pessoa AS nm_usuario " +
					"FROM agd_agendamento_usuario A " +
					"JOIN seg_usuario B ON (A.cd_usuario = B.cd_usuario) " +
					"LEFT OUTER JOIN grl_pessoa C ON (B.cd_pessoa = C.cd_pessoa) " +
					"WHERE A.cd_agendamento = ? " +
					"  AND A.tp_nivel_usuario = " + AgendamentoUsuarioServices.TP_PARTICIPANTE);
			pstmt.setInt(1, cdAgendamento);
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}

	@Deprecated
	public static ResultSetMap getAllOcorrencias(int cdAgendamento) {
		return getAllOcorrencias(cdAgendamento, null);
	}

	@Deprecated
	public static ResultSetMap getAllOcorrencias(int cdAgendamento, Connection connection) {
		boolean isConnectionNull = connection==null;
		try {
			if (isConnectionNull)
				connection = Conexao.conectar();
			PreparedStatement pstmt = connection.prepareStatement("SELECT A.*, B.nm_login, C.nm_pessoa AS nm_usuario, " +
					"D.nm_tipo_ocorrencia, E.lg_repetivel, E.cd_agendamento " +
					"FROM agd_ocorrencia E " +
					"JOIN grl_ocorrencia A ON (E.cd_ocorrencia = A.cd_ocorrencia) " +
					"LEFT OUTER JOIN seg_usuario B ON (A.cd_usuario = B.cd_usuario) " +
					"LEFT OUTER JOIN grl_pessoa C ON (B.cd_pessoa = C.cd_pessoa) " +
					"LEFT OUTER JOIN grl_tipo_ocorrencia D ON (A.cd_tipo_ocorrencia = D.cd_tipo_ocorrencia)" +
					"WHERE E.cd_agendamento = ?");
			pstmt.setInt(1, cdAgendamento);
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}

	@Deprecated
	public static ResultSetMap getAllArquivos(int cdAgendamento) {
		return getAllArquivos(cdAgendamento, null);
	}

	@Deprecated
	public static ResultSetMap getAllArquivos(int cdAgendamento, Connection connection) {
		boolean isConnectionNull = connection==null;
		try {
			if (isConnectionNull)
				connection = Conexao.conectar();
			PreparedStatement pstmt = connection.prepareStatement("SELECT B.*, A.cd_usuario, " +
					"C.nm_tipo_arquivo " +
					"FROM agd_agendamento_arquivo A " +
					"LEFT OUTER JOIN grl_arquivo B ON (A.cd_arquivo = B.cd_arquivo) " +
					"LEFT OUTER JOIN grl_tipo_arquivo C ON (B.cd_tipo_arquivo = C.cd_tipo_arquivo) " +
					"WHERE A.cd_agendamento = ?");
			pstmt.setInt(1, cdAgendamento);
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}

	@Deprecated
	public static final HashMap<String, Object> insert(String nmAgendamento, GregorianCalendar dtInicial, int cdTipoLembrete, int tpAgendamento,
			int cdUsuario, Connection connection) {
		boolean isConnectionNull = connection==null;
		try {
			connection = isConnectionNull ? Conexao.conectar() : connection;
			connection.setAutoCommit(isConnectionNull ? false : connection.getAutoCommit());

			int cdTipoAgendamento = tpAgendamento==TipoAgendamentoServices.TP_COMPROMISSO ?
					ParametroServices.getValorOfParametroAsInteger("CD_TIPO_AGENDAMENTO_COMP_DEFAULT", 0, 0, connection) :
					tpAgendamento==TipoAgendamentoServices.TP_TAREFA ?
					ParametroServices.getValorOfParametroAsInteger("CD_TIPO_AGENDAMENTO_TAF_DEFAULT", 0, 0, connection) : 0;
			if (cdTipoAgendamento <= 0) {
				PreparedStatement pstmt = connection.prepareStatement("SELECT * " +
						"FROM agd_tipo_agendamento");
				ResultSet rs = pstmt.executeQuery();
				cdTipoAgendamento = rs.next() ? rs.getInt("cd_tipo_agendamento") : cdTipoAgendamento;
			}

			if (cdTipoAgendamento<=0) {
				if (isConnectionNull)
					Conexao.rollback(connection);
				return null;
			}

			Agendamento agendamento = new Agendamento(0 /*cdTipoAgendamento*/,
					nmAgendamento,
					"" /*nmLocal*/,
					dtInicial,
					null /*dtFinal*/,
					AgendamentoServices.ST_PENDENTE /*stAgendamento*/,
					"" /*txtAgendamento*/,
					0 /*lgLembrete*/,
					0 /*qtTempoLembrete*/,
					AgendamentoServices.TMP_MINUTO /*tpUnidadeTempoLembrete*/,
					0 /*lgAnexos*/,
					new GregorianCalendar() /*dtCadastro*/,
					0 /*cdRecorrencia*/,
					"" /*idAgendamento*/,
					0 /*nrRecorrencia*/,
					cdTipoAgendamento,
					1 /*lgOriginal*/,
					0 /*cdAgenda*/,
					0 /*cdMailing*/,
					0 /*cdDocumento*/,
					null /*dtLemebrete*/);

			HashMap<String, Object> hash = insert(agendamento, cdTipoLembrete, cdUsuario, cdUsuario, null, connection);
			if (hash==null) {
				if (isConnectionNull)
					Conexao.rollback(connection);
				return null;
			}

			if (isConnectionNull)
				connection.commit();

			return hash;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			if (isConnectionNull)
				Conexao.rollback(connection);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}

	@Deprecated
	public static Result updateDateAssuntos(int cdAgendamento, GregorianCalendar dtInicial, ArrayList<Integer> assuntos) {
		return updateDateAssuntos(cdAgendamento, dtInicial, assuntos, null);
	}

	@Deprecated
	public static Result updateDateAssuntos(int cdAgendamento, GregorianCalendar dtInicial, ArrayList<Integer> assuntos, Connection connection) {
		boolean isConnectionNull = connection==null;
		try {
			connection = isConnectionNull ? Conexao.conectar() : connection;
			connection.setAutoCommit(isConnectionNull ? false : connection.getAutoCommit());

			if (dtInicial!=null) {
				Agendamento agendamento = AgendamentoDAO.get(cdAgendamento, connection);
				agendamento.setDtInicial(dtInicial);
				if (AgendamentoDAO.update(agendamento, connection) <= 0) {
					if (isConnectionNull)
						Conexao.rollback(connection);
					return new Result(-1, "Erros verificados ao atualizar data de agendamento de reunião. " +
							"Entre em contato com o suporte técnico.");
				}
			}

			if (assuntos==null || assuntos.size()==0) {
				PreparedStatement pstmt = connection.prepareStatement("DELETE " +
						"FROM agd_agendamento_assunto " +
						"WHERE cd_agendamento = ?");
				pstmt.setInt(1, cdAgendamento);
				pstmt.execute();
			}
			else {
				String statement = "DELETE " +
								   "FROM agd_agendamento_assunto " +
								   "WHERE cd_agendamento = ? " +
								   "  AND NOT cd_assunto IN (";
				for (int i=0; assuntos!=null && i<assuntos.size(); i++)
					statement += (i>0 ? ", " : "") + assuntos.get(i);
				statement += ")";
				PreparedStatement pstmt = connection.prepareStatement(statement);
				pstmt.setInt(1, cdAgendamento);
				pstmt.execute();
			}

			for (int i=0; assuntos!=null && i<assuntos.size(); i++) {
				AgendamentoAssunto assunto = AgendamentoAssuntoDAO.get(assuntos.get(i), cdAgendamento);
				boolean insert = assunto==null;
				assunto = assunto!=null ? assunto : new AgendamentoAssunto(assuntos.get(i) /*cdAssunto*/,
						cdAgendamento,
						i+1 /*nrOrdem*/,
						0 /*cdResponsavel*/,
						-1 /*stFinal*/,
						"" /*txtMemo*/,
						"" /*txtFechamento*/,
						-1 /*tpPrioridade*/,
						null /*dtPrevisaoConclusao*/);
				assunto.setNrOrdem(i+1);
				if ((insert ? AgendamentoAssuntoDAO.insert(assunto, connection) : AgendamentoAssuntoDAO.update(assunto, connection)) <= 0) {
					if (isConnectionNull)
						Conexao.rollback(connection);
					return new Result(-1, "Erros verificados ao agendar reunião. Entre em contato com o suporte técnico.");
				}
			}

			if (isConnectionNull)
				connection.commit();

			return new Result(1);
		}
		catch(Exception e){
			if (isConnectionNull)
				Conexao.rollback(connection);
			e.printStackTrace(System.out);
			System.err.println("Erro! AgendamentoServices.updateDateAssuntos: " +  e);
			return new Result(-1, e.getLocalizedMessage());
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}

	@Deprecated
	public static Result insertOfAgenda(int cdAgenda, int cdUsuario, GregorianCalendar dtInicial, int tpReuniao,
			ArrayList<Integer> assuntos) {
		return insertOfAgenda(cdAgenda, cdUsuario, dtInicial, tpReuniao, assuntos, null);
	}

	@Deprecated
	public static Result insertOfAgenda(int cdAgenda, int cdUsuario, GregorianCalendar dtInicial, int tpReuniao, ArrayList<Integer> assuntos, Connection connection) {
		boolean isConnectionNull = connection==null;
		try {
			if (cdAgenda<=0)
				return new Result(-1, "Erros reportados ao agendar reunião. Anote a mensagem de erro e entre em contato com o " +
						"suporte técnico.\n" +
						"Código de agenda/grupo não identificado...");

			connection = isConnectionNull ? Conexao.conectar() : connection;
			connection.setAutoCommit(isConnectionNull ? false : connection.getAutoCommit());

			GregorianCalendar dtInicialTemp = Util.convStringToCalendar(Util.formatDate(dtInicial, "dd/MM/yyyy HH:mm"));
			GregorianCalendar dtFinalTemp = Util.convStringToCalendar(Util.formatDate(dtInicial, "dd/MM/yyyy HH:mm") + ":59:999");
			PreparedStatement pstmt = connection.prepareStatement("SELECT cd_agendamento " +
					"FROM agd_agendamento " +
					"WHERE cd_agenda = ? " +
					"  AND (dt_inicial >= ? AND dt_inicial <= ?)");
			pstmt.setInt(1, cdAgenda);
			pstmt.setTimestamp(2, Util.convCalendarToTimestamp(dtInicialTemp));
			pstmt.setTimestamp(3, Util.convCalendarToTimestamp(dtFinalTemp));
			ResultSet rs = pstmt.executeQuery();
			if (rs.next()) {
				if (isConnectionNull)
					Conexao.rollback(connection);
				return new Result(-1, "Não é possível agendar reunião na data e/ou horário informados. Certifique-se que não exista " +
						"nenhuma reunião agendada na data e/ou horário informados.");
			}

			int cdDocumento = 0;
			Result result = DocumentoServices.insert(new Documento(0 /*cdDocumento*/, 0 /*cdArquivo*/, 0 /*cdSetor*/, cdUsuario /*cdUsuario*/,
					   "" /*nmLocalOrigem*/, dtInicial /*dtProtocolo*/, DocumentoServices.TP_PUBLICO /*tpDocumento*/,
					   "" /*txtObservacao*/, "" /*idDocumento*/, "" /*nrDocumento*/,
					   ParametroServices.getValorOfParametroAsInteger("CD_TIPO_DOCUMENTO_ATA_REUNIAO", 0, 0, connection) /*cdTipoDocumento*/,
					   0 /*cdServico*/, 0 /*cdAtendimento*/, "" /*txtDocumento*/, 0 /*cdSetorAtual*/,
					   0 /*cdSituacaoDocumento*/, 0 /*cdFase*/, 0 /*cdEmpresa*/, 0/*cdProcesso*/, 0 /*tpPrioridade*/, 0/*cdDocumentoSuperior*/, 
					   null /*dsAssunto*/, null /*nrAtendimento*/, 0/*lgNotificacao*/, 0 /*cdTipoDocumentoAnterior*/, null/*nrDocumentoExterno*/,
					   null/*nrAssunto*/,null, null, 0, 1), null /*solicitantes*/, connection, null);
			if (result.getCode() <= 0) {
				if (isConnectionNull) {
					Conexao.rollback(connection);
				return new Result(-1, "Erros reportados ao registrar documento referente à ata de reunião. Certifique-se de que os " +
						"parâmetros referentes ao registro de ata de reunião estão configurados corretamente ou entre em contato com " +
						"o suporte técnico.");

				}
			}

			cdDocumento = result.getCode();

			int cdTipoAgendamento = tpReuniao==TP_REUNIAO_ORDINARIA ? ParametroServices.getValorOfParametroAsInteger("CD_TIPO_AGENDAMENTO_REUNIAO_ORD", 0, 0, connection) :
				ParametroServices.getValorOfParametroAsInteger("CD_TIPO_AGENDAMENTO_REUNIAO_EXTRAORD", 0, 0, connection);
			Agenda agenda = AgendaDAO.get(cdAgenda, connection);

			Agendamento agendamento = new Agendamento(0 /*cdAgendamento*/, agenda.getNmAgenda(), "" /*nmLocal*/, dtInicial, null /*dtFinal*/,
													  ST_PENDENTE /*stAgendamento*/, "" /*txtAgendamento*/, 0 /*lgLembrete*/, 0 /*qtTempoLembrete*/,
													  0 /*tpUnidadeTempoLembrete*/, 0 /*lgAnexos*/, new GregorianCalendar(), 0 /*cdRecorrencia*/,
													  "" /*idAgendamento*/, 0 /*nrRecorrencia*/, cdTipoAgendamento, 0 /*lgOriginal*/, cdAgenda,
													  0 /*cdMailing*/, cdDocumento, null /*dtLemebrete*/);

			pstmt = connection.prepareStatement("SELECT * FROM agd_agenda_participante A " +
												"WHERE A.cd_agenda       = " + cdAgenda +
												"  AND A.st_participante = " + AgendaParticipanteServices.ST_ATIVO);
			ResultSetMap rsmParticipantes = new ResultSetMap(pstmt.executeQuery());

			HashMap<String,Object> hash = insert(agendamento, 0 /*cdTipoLembrete*/, cdUsuario /*cdUsuarioCriador*/,
					cdUsuario /*cdUsuarioResponsavel*/, null /*recorrencia*/, rsmParticipantes, connection);
			if (hash==null) {
				if (isConnectionNull)
					Conexao.rollback(connection);
				return new Result(-1, "Erros verificados ao agendar reunião. Entre em contato com o suporte técnico.");
			}

			Agendamento agendamentoDefault = (Agendamento)hash.get("agendamentoDefault");

			pstmt = connection.prepareStatement("SELECT DISTINCT B.cd_usuario " +
					"FROM agd_agenda_participante A " +
					"JOIN seg_usuario B ON (A.cd_participante = B.cd_pessoa) " +
					"WHERE A.cd_agenda = ? " +
					"  AND A.tp_participante = ? " +
					"  AND B.st_usuario = ? " +
					"  AND B.cd_usuario <> ?");
			pstmt.setInt(1, cdAgenda);
			pstmt.setInt(2, AgendaParticipanteServices.TP_COLABORADOR);
			pstmt.setInt(3, UsuarioServices.ST_ATIVO);
			pstmt.setInt(4, cdUsuario);
			ResultSet rsUsuariosPart = pstmt.executeQuery();
			while (rsUsuariosPart.next()) {
				if (AgendamentoUsuarioDAO.insert(new AgendamentoUsuario(rsUsuariosPart.getInt("cd_usuario"),
						agendamentoDefault.getCdAgendamento(),
						AgendamentoUsuarioServices.TP_PARTICIPANTE), connection) <= 0) {
					if (isConnectionNull)
						Conexao.rollback(connection);
					return new Result(-1, "Erros verificados ao agendar reunião. Entre em contato com o suporte técnico.");
				}
			}

			for (int i=0; assuntos!=null && i<assuntos.size(); i++) {
				if (AgendamentoAssuntoDAO.insert(new AgendamentoAssunto(assuntos.get(i) /*cdAssunto*/,
						agendamento.getCdAgendamento(),
						i+1 /*nrOrdem*/,
						0 /*cdResponsavel*/,
						-1 /*stFinal*/,
						"" /*txtMemo*/,
						"" /*txtFechamento*/,
						-1 /*tpPrioridade*/,
						null /*dtPrevisaoConclusao*/), connection) <= 0) {
					if (isConnectionNull)
						Conexao.rollback(connection);
					return new Result(-1, "Erros verificados ao agendar reunião. Entre em contato com o suporte técnico.");
				}
			}

			pstmt = connection.prepareStatement("SELECT MAX(A.cd_agendamento) AS last_agendamento " +
					"FROM agd_agendamento A " +
					"WHERE NOT A.cd_agenda IS NULL " +
					"  AND A.cd_agendamento <> ?");
			pstmt.setInt(1, agendamentoDefault.getCdAgendamento());
			rs = pstmt.executeQuery();
			int cdAgendamentoLast = !rs.next() ? 0 : rs.getInt("last_agendamento");
			if (cdAgendamentoLast > 0) {
				Agendamento lastAgendamento = AgendamentoDAO.get(cdAgendamentoLast, connection);
				if (lastAgendamento.getCdMailing()>0) {
					Mailing lastMailing = MailingDAO.get(lastAgendamento.getCdMailing(), connection);
					if (saveMailing(agendamentoDefault.getCdAgendamento(), lastAgendamento.getNmLocal(), lastMailing.getTxtMailing(), null, connection).getCode()<=0) {
						if (isConnectionNull)
							Conexao.rollback(connection);
						return new Result(-1, "Erros verificados ao agendar reunião. Entre em contato com o suporte técnico.");
					}
				}
			}

			if (isConnectionNull)
				connection.commit();

			HashMap<String, Object> hashFinal = new HashMap<String, Object>();
			hashFinal.put("agendamento", agendamentoDefault);
			hashFinal.put("documento", hash.get("documento"));
			result = new Result(1, "", "hash", hashFinal);
			return result;
		}
		catch(Exception e){
			if (isConnectionNull)
				Conexao.rollback(connection);
			e.printStackTrace(System.out);
			return new Result(-1, e.getLocalizedMessage(), e);
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}

	@Deprecated
	public static HashMap<String, Object> insert(Agendamento agendamento, int cdTipoLembrete, int cdUsuarioCriador, int cdUsuarioResponsavel) {
		return insert(agendamento, cdTipoLembrete, cdUsuarioCriador, cdUsuarioResponsavel, null, null);
	}

	@Deprecated
	public static HashMap<String, Object> insert(Agendamento agendamento, int cdTipoLembrete, int cdUsuarioCriador, int cdUsuarioResponsavel,
			Recorrencia recorrencia) {
		return insert(agendamento, cdTipoLembrete, cdUsuarioCriador, cdUsuarioResponsavel, recorrencia, null);
	}

	@Deprecated
	public static HashMap<String, Object> insert(Agendamento agendamento, int cdTipoLembrete, int cdUsuarioCriador, int cdUsuarioResponsavel,
			Recorrencia recorrencia, Connection connection) {
		return insert(agendamento, cdTipoLembrete, cdUsuarioCriador, cdUsuarioResponsavel, recorrencia, null, connection);
	}

	@Deprecated
	public static HashMap<String, Object> insert(Agendamento agendamento, int cdTipoLembrete, int cdUsuarioCriador, int cdUsuarioResponsavel,
			Recorrencia recorrencia, ResultSetMap rsmParticipantes, Connection connection){
		boolean isConnectionNull = connection==null;
		try {
			connection = isConnectionNull ? Conexao.conectar() : connection;
			connection.setAutoCommit(isConnectionNull ? false : connection.getAutoCommit());

			if (agendamento.getDtInicial()==null)
				agendamento.setDtInicial(new GregorianCalendar());
			if (agendamento.getIdAgendamento()==null || agendamento.getIdAgendamento().trim().equals("")) {
				int idAgendamento = NumeracaoDocumentoServices.getProximoNumero("AGENDAMENTO", -1, 0, connection);
				agendamento.setIdAgendamento(idAgendamento + "");
			}

			/* recorrencia */
			HashMap<String, ArrayList<HashMap<String, Object>>> updatesRecorrencia = null;
			int qtRecorrencias = 0;
			if (recorrencia!=null) {
				int codeRecorrencia = recorrencia.getCdRecorrencia();
				if (codeRecorrencia <= 0) {
					if ((codeRecorrencia = RecorrenciaDAO.insert(recorrencia, connection)) <= 0) {
						if (isConnectionNull)
							Conexao.rollback(connection);
						return null;
					}
					agendamento.setCdRecorrencia(codeRecorrencia);
				}
				else if (RecorrenciaDAO.update(recorrencia, connection) <= 0) {
					if (isConnectionNull)
						Conexao.rollback(connection);
					return null;
				}
				if ((updatesRecorrencia = RecorrenciaServices.createAgendamentosRecorrentes(recorrencia, agendamento,
						cdTipoLembrete, cdUsuarioCriador, cdUsuarioResponsavel, false, connection)) == null) {
					if (isConnectionNull)
						Conexao.rollback(connection);
					return null;
				}
				qtRecorrencias = updatesRecorrencia.get("inserted").size();
			}

			agendamento.setDtCadastro(new GregorianCalendar());
			if (cdTipoLembrete != -1) {
				agendamento.setLgLembrete(cdTipoLembrete>0 && cdTipoLembrete<=opcoesLembrete.length ? 1 : 0);
				agendamento.setTpUnidadeTempoLembrete(agendamento.getLgLembrete()==0 ? TMP_MINUTO : correlacoesOpcoesLembrete[cdTipoLembrete - 1][1]);
				agendamento.setQtTempoLembrete(agendamento.getLgLembrete()==0 ? 0 : correlacoesOpcoesLembrete[cdTipoLembrete - 1][0]);
			}
			int code = 0;
			if (recorrencia != null && agendamento.getNrRecorrencia()==0) {
				ArrayList<HashMap<String, Object>> agdsInserted = updatesRecorrencia.get("inserted");
				for (int i=0; agdsInserted!=null && i<agdsInserted.size(); i++) {
					Agendamento agendamentoTemp = (Agendamento)agdsInserted.get(i).get("agendamento");
					if (agendamentoTemp.getNrRecorrencia() == 1) {
						agendamento = agendamentoTemp;
						code = agendamento.getCdAgendamento();

						agendamento.setLgOriginal(0);
						if (AgendamentoDAO.update(agendamento, connection) <= 0) {
							if (isConnectionNull)
								Conexao.rollback(connection);
							return null;
						}
						break;
					}
				}
			}
			else {
				code = AgendamentoDAO.insert(agendamento, connection);
				qtRecorrencias++;
				if (code <= 0) {
					if (isConnectionNull)
						Conexao.rollback(connection);
					return null;
				}
				else
					agendamento.setCdAgendamento(code);

				if (cdUsuarioCriador > 0 && cdUsuarioCriador != cdUsuarioResponsavel) {
					if (AgendamentoUsuarioDAO.insert(new AgendamentoUsuario(cdUsuarioCriador, code,
							AgendamentoUsuarioServices.TP_CRIADOR), connection) <= 0) {
						if (isConnectionNull)
							Conexao.rollback(connection);
						return null;
					}
				}

				if (cdUsuarioResponsavel > 0) {
					if (AgendamentoUsuarioDAO.insert(new AgendamentoUsuario(cdUsuarioResponsavel, code,
							cdUsuarioCriador==cdUsuarioResponsavel ? AgendamentoUsuarioServices.TP_CRIADOR_RESPONSAVEL : AgendamentoUsuarioServices.TP_RESPONSAVEL), connection) <= 0) {
						if (isConnectionNull)
							Conexao.rollback(connection);
						return null;
					}
				}
			}

			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connection);
				return null;
			}

			/* atualizacao de participantes */
			if (rsmParticipantes != null) {
				rsmParticipantes.beforeFirst();
				while (rsmParticipantes.next()) {
					if (AgendamentoParticipanteDAO.insert(new AgendamentoParticipante(code /*cdAgendamento*/,
							rsmParticipantes.getInt("CD_AGENDA"),
							rsmParticipantes.getInt("cd_participante"),
							0 /*lgPresenca*/), connection) <= 0) {
						if (isConnectionNull)
							Conexao.rollback(connection);
					}
				}
			}

			if (isConnectionNull)
				connection.commit();

			HashMap<String, Object> hash = new HashMap<String, Object>();
			hash.put("agendamentoDefault", agendamento);
			hash.put("updatesRecorrencia", updatesRecorrencia);
			hash.put("qtRecorrencias", qtRecorrencias);
			return hash;

		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! AgendamentoServices.insert: " +  e);
			return null;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}

	@Deprecated
	public static HashMap<String, Object> update(Agendamento agendamento, int cdTipoLembrete, int cdUsuarioResponsavel) {
		return update(agendamento, cdTipoLembrete, cdUsuarioResponsavel, null);
	}

	@Deprecated
	public static HashMap<String, Object> update(Agendamento agendamento, int cdTipoLembrete, int cdUsuarioResponsavel, Recorrencia recorrencia){
		return update(agendamento, cdTipoLembrete, cdUsuarioResponsavel, recorrencia, null);
	}

	@Deprecated
	public static HashMap<String, Object> update(Agendamento agendamento, int cdTipoLembrete, int cdUsuarioResponsavel,
			Recorrencia recorrencia, Connection connection){
		return update(agendamento, cdTipoLembrete, cdUsuarioResponsavel, recorrencia, null, connection);
	}

	@Deprecated
	public static HashMap<String, Object> update(Agendamento agendamento, int cdTipoLembrete, int cdUsuarioResponsavel,
			Recorrencia recorrencia, ResultSetMap rsmParticipantes, Connection connection){
		boolean isConnectionNull = connection==null;
		try {
			connection = isConnectionNull ? Conexao.conectar() : connection;
			connection.setAutoCommit(isConnectionNull ? false : connection.getAutoCommit());

			Agendamento agendamentoOld = AgendamentoDAO.get(agendamento.getCdAgendamento(), connection);
			agendamento.setIdAgendamento(agendamentoOld.getIdAgendamento());

			/* recorrencia */
			HashMap<String, ArrayList<HashMap<String, Object>>> updatesRecorrencia = null;
			if (recorrencia!=null) {
				int codeRecorrencia = 0;
				if ((codeRecorrencia = (recorrencia.getCdRecorrencia()<=0 ? RecorrenciaDAO.insert(recorrencia, connection) :
					RecorrenciaDAO.update(recorrencia, connection))) <= 0) {
					if (isConnectionNull)
						Conexao.rollback(connection);
					return null;
				}
				if (agendamento.getCdRecorrencia() <= 0) {
					agendamento.setCdRecorrencia(codeRecorrencia);
					PreparedStatement pstmt = connection.prepareStatement("SELECT A.cd_usuario FROM agd_agendamento_usuario A " +
							"WHERE A.cd_agendamento = ? " +
							"  AND (A.tp_nivel_usuario = " + AgendamentoUsuarioServices.TP_CRIADOR + " OR" +
							"		A.tp_nivel_usuario = " + AgendamentoUsuarioServices.TP_CRIADOR_RESPONSAVEL + ")");
					pstmt.setInt(1, agendamento.getCdAgendamento());
					ResultSet rs = pstmt.executeQuery();
					int cdUsuarioCriador = rs.next() ? rs.getInt(1) : 0;
					if ((updatesRecorrencia = RecorrenciaServices.createAgendamentosRecorrentes(recorrencia, agendamento,
							cdTipoLembrete, cdUsuarioCriador, cdUsuarioResponsavel, false, connection)) == null) {
						if (isConnectionNull)
							Conexao.rollback(connection);
						return null;
					}
				}
				else {
					if ((updatesRecorrencia = RecorrenciaServices.updateAgendamentosRecorrentes(recorrencia, agendamento,
							cdTipoLembrete, cdUsuarioResponsavel, connection)) == null) {
						if (isConnectionNull)
							Conexao.rollback(connection);
						return null;
					}
				}
			}
			else if (agendamento.getCdRecorrencia()<=0) {
				PreparedStatement pstmt = connection.prepareStatement("SELECT cd_agendamento FROM agd_agendamento A " +
						"WHERE cd_recorrencia = ? " +
						"  AND cd_agendamento <> ?");
				pstmt.setInt(1, agendamentoOld.getCdRecorrencia());
				pstmt.setInt(2, agendamento.getCdAgendamento());
				ResultSet rs = pstmt.executeQuery();
				if (!rs.next() && agendamentoOld.getCdRecorrencia() > 0) {
					if (RecorrenciaDAO.delete(agendamentoOld.getCdAgendamento(), connection) <= 0) {
						if (isConnectionNull)
							Conexao.rollback(connection);
						return null;
					}
				}
			}

			if (agendamento.getDtInicial()==null)
				agendamento.setDtInicial(new GregorianCalendar());
			if (agendamento.getDtCadastro()==null)
				agendamento.setDtCadastro(new GregorianCalendar());
			agendamento.setLgLembrete(cdTipoLembrete>0 && cdTipoLembrete<=opcoesLembrete.length ? 1 : 0);
			agendamento.setTpUnidadeTempoLembrete(agendamento.getLgLembrete()==0 ? TMP_MINUTO : correlacoesOpcoesLembrete[cdTipoLembrete - 1][1]);
			agendamento.setQtTempoLembrete(agendamento.getLgLembrete()==0 ? 0 : correlacoesOpcoesLembrete[cdTipoLembrete - 1][0]);
			int code = AgendamentoDAO.update(agendamento, connection);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connection);
				return null;
			}

			if (cdUsuarioResponsavel > 0) {
				PreparedStatement pstmt = connection.prepareStatement("DELETE FROM agd_agendamento_usuario " +
						"WHERE cd_agendamento = ? " +
						"  AND cd_usuario = ? " +
						"  AND (tp_nivel_usuario <> " + AgendamentoUsuarioServices.TP_CRIADOR + " AND tp_nivel_usuario <> " + AgendamentoUsuarioServices.TP_CRIADOR_RESPONSAVEL + ")");
				pstmt.setInt(1, agendamento.getCdAgendamento());
				pstmt.setInt(2, cdUsuarioResponsavel);
				pstmt.execute();

				/* localiza o criador do agendamento */
				pstmt = connection.prepareStatement("SELECT cd_usuario FROM agd_agendamento_usuario " +
						"WHERE cd_agendamento = ? " +
						"  AND (tp_nivel_usuario = " + AgendamentoUsuarioServices.TP_CRIADOR_RESPONSAVEL + " OR " +
						"  		tp_nivel_usuario = " + AgendamentoUsuarioServices.TP_CRIADOR + ")");
				pstmt.setInt(1, agendamento.getCdAgendamento());
				ResultSet rs = pstmt.executeQuery();
				int cdUsuarioCriador = rs.next() ? rs.getInt(1) : 0;

				pstmt = connection.prepareStatement("SELECT * FROM agd_agendamento_usuario " +
						"WHERE cd_agendamento = ? " +
						"  AND (tp_nivel_usuario = " + AgendamentoUsuarioServices.TP_CRIADOR_RESPONSAVEL + " OR " +
						"  		tp_nivel_usuario = " + AgendamentoUsuarioServices.TP_RESPONSAVEL + ")");
				pstmt.setInt(1, agendamento.getCdAgendamento());
				rs = pstmt.executeQuery();
				int cdUsuarioResponsavelOld = rs.next() ? rs.getInt("cd_usuario") : 0;
				int tpUsuarioResponsavelOld = cdUsuarioResponsavelOld==0 ? 0 : rs.getInt("tp_nivel_usuario");
				if (cdUsuarioResponsavel != cdUsuarioResponsavelOld) {
					/* usuario responsavel anterior nao configurado */
					if (cdUsuarioResponsavelOld == 0) {
						if (AgendamentoUsuarioDAO.insert(new AgendamentoUsuario(cdUsuarioResponsavel, agendamento.getCdAgendamento(),
								AgendamentoUsuarioServices.TP_RESPONSAVEL), connection) <= 0) {
							if (isConnectionNull)
								Conexao.rollback(connection);
							return null;
						}
					}
					else {
						/* usuario responsavel anterior eh o criador do agendamento */
						if (tpUsuarioResponsavelOld == AgendamentoUsuarioServices.TP_CRIADOR_RESPONSAVEL) {
							if (AgendamentoUsuarioDAO.update(new AgendamentoUsuario(cdUsuarioResponsavelOld, agendamento.getCdAgendamento(),
									AgendamentoUsuarioServices.TP_CRIADOR), connection) <= 0) {
								if (isConnectionNull)
									Conexao.rollback(connection);
								return null;
							}
						}
						/* alteracao de usuario responsavel */
						if (cdUsuarioResponsavel == cdUsuarioCriador) {
							if (AgendamentoUsuarioDAO.update(new AgendamentoUsuario(cdUsuarioResponsavel, agendamento.getCdAgendamento(),
									AgendamentoUsuarioServices.TP_CRIADOR_RESPONSAVEL), cdUsuarioResponsavel, agendamento.getCdAgendamento(), connection) <= 0) {
								if (isConnectionNull)
									Conexao.rollback(connection);
								return null;
							}
							if (AgendamentoUsuarioDAO.delete(cdUsuarioResponsavelOld, agendamento.getCdAgendamento(), connection) <= 0) {
								if (isConnectionNull)
									Conexao.rollback(connection);
								return null;
							}
						}
						else {
							if (AgendamentoUsuarioDAO.insert(new AgendamentoUsuario(cdUsuarioResponsavel, agendamento.getCdAgendamento(),
									AgendamentoUsuarioServices.TP_RESPONSAVEL), connection) <= 0) {
								if (isConnectionNull)
									Conexao.rollback(connection);
								return null;
							}
							if (cdUsuarioResponsavelOld != cdUsuarioCriador) {
								if (AgendamentoUsuarioDAO.delete(cdUsuarioResponsavelOld, agendamento.getCdAgendamento(), connection) <= 0) {
									if (isConnectionNull)
										Conexao.rollback(connection);
									return null;
								}
							}
						}
					}
				}
			}

			int qtRecorrencias = 0;
			if (agendamento.getCdRecorrencia() > 0) {
				PreparedStatement pstmt = connection.prepareStatement("SELECT COUNT(*) AS qt_recorrencias " +
						"FROM agd_agendamento " +
						"WHERE cd_recorrencia = ?");
				pstmt.setInt(1, agendamento.getCdRecorrencia());
				ResultSet rs = pstmt.executeQuery();
				qtRecorrencias = rs.next() ? rs.getInt(1) : 0;
			}

			/* atualizacao de participantes */
			if (rsmParticipantes != null) {
				PreparedStatement pstmt = connection.prepareStatement("DELETE FROM agd_agendamento_participante " +
						"WHERE cd_agendamento = ?");
				pstmt.setInt(1, agendamento.getCdAgendamento());
				pstmt.execute();

				rsmParticipantes.beforeFirst();
				while (rsmParticipantes.next()) {
					if (AgendamentoParticipanteDAO.insert(new AgendamentoParticipante(agendamento.getCdAgendamento(),
							rsmParticipantes.getInt("CD_AGENDA"),
							rsmParticipantes.getInt("cd_participante"),
							0 /*lgPresenca*/), connection) <= 0) {
						if (isConnectionNull)
							Conexao.rollback(connection);
					}
				}
			}

			if (isConnectionNull)
				connection.commit();

			HashMap<String, Object> hash = new HashMap<String,Object>();
			hash.put("agendamentoDefault", agendamento);
			hash.put("updatesRecorrencia", updatesRecorrencia);
			hash.put("qtRecorrencias", qtRecorrencias);
			return hash;
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! AgendamentoServices.update: " +  e);
			return null;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}

	public static ResultSetMap getAsResultSet(int cdAgendamento) {
		ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
		criterios.add(new ItemComparator("A.cd_agendamento", Integer.toString(cdAgendamento), ItemComparator.EQUAL, Types.INTEGER));
		return find(criterios);
	}

	public static ResultSetMap find(int cdUsuario) {
		return find(cdUsuario, null);
	}

	public static ResultSetMap findLembretes(int cdUsuario) {
		ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
		criterios.add(new ItemComparator("justUsuario", Integer.toString(cdUsuario), ItemComparator.EQUAL, Types.INTEGER));
		criterios.add(new ItemComparator("findComLembrete", Integer.toString(cdUsuario), ItemComparator.EQUAL, Types.INTEGER));
		criterios.add(new ItemComparator("A.st_agendamento", Integer.toString(ST_PENDENTE), ItemComparator.EQUAL, Types.SMALLINT));
		return find(cdUsuario, criterios);
	}

	public static ResultSetMap find(int cdUsuario, GregorianCalendar dtInicial, GregorianCalendar dtFinal) {
		ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
		if (dtInicial != null)
			criterios.add(new ItemComparator("A.dt_inicial", Util.formatDateTime(dtInicial, "dd/MM/yyyy HH:mm:ss"), ItemComparator.GREATER_EQUAL, Types.TIMESTAMP));
		if (dtFinal != null)
			criterios.add(new ItemComparator("A.dt_inicial", Util.formatDateTime(dtFinal, "dd/MM/yyyy HH:mm:ss"), ItemComparator.MINOR_EQUAL, Types.TIMESTAMP));
		return (ResultSetMap)find(cdUsuario, criterios, AGD_REPORT, false, null).getObjects().get("rsmAgendamentos");
	}

	public static ResultSetMap find(int cdUsuario, ArrayList<ItemComparator> criterios) {
		return (ResultSetMap)find(cdUsuario, criterios, AGD_REPORT, false, null).getObjects().get("rsmAgendamentos");
	}

	public static ResultSetMap find(int cdUsuario, ArrayList<ItemComparator> criterios, int tpViewRegisters) {
		return (ResultSetMap)find(cdUsuario, criterios, tpViewRegisters, false, null).getObjects().get("rsmAgendamentos");
	}

	public static ResultSetMap find(int cdUsuario, ArrayList<ItemComparator> criterios, int tpViewRegisters, int tpOrdenamento) {
		return (ResultSetMap)find(cdUsuario, criterios, tpViewRegisters, tpOrdenamento, false, null).getObjects().get("rsmAgendamentos");
	}

	public static Result find(int cdUsuario, ArrayList<ItemComparator> criterios, int tpViewRegisters, boolean returnFeriados) {
		return find(cdUsuario, criterios, tpViewRegisters, returnFeriados, null);
	}

	public static Result find(int cdUsuario, ArrayList<ItemComparator> criterios, int tpViewRegisters, boolean returnFeriados, Connection connection) {
		return find(cdUsuario, criterios, tpViewRegisters, ORD_DESCRESCENTE, returnFeriados, connection);
	}

	public static Result find(int cdUsuario, ArrayList<ItemComparator> criterios, int tpViewRegisters,
			int tpOrdenamento, boolean returnFeriados, Connection connection) {
		ArrayList<ItemComparator> criteriosTemp = new ArrayList<ItemComparator>();
		criteriosTemp.add(new ItemComparator("justUsuario", Integer.toString(cdUsuario), ItemComparator.EQUAL, Types.INTEGER));
		for (int i=0; criterios!=null && i<criterios.size(); i++) {
			if (criterios.get(i) instanceof ItemComparator)
				criteriosTemp.add(criterios.get(i));
		}
		if (!returnFeriados)
			return new Result(1, "", "rsmAgendamentos", olderfind(criteriosTemp, tpViewRegisters, tpOrdenamento, connection));
		else {
			GregorianCalendar dtInicial = null;
			GregorianCalendar dtFinal = null;
			for (int i=0; criterios!=null && i<criterios.size(); i++)
				if (criterios.get(i).getColumn().equalsIgnoreCase("A.dt_inicial") && criterios.get(i).getTypeComparation() == ItemComparator.GREATER_EQUAL)
					dtInicial = Util.convStringCalendar(criterios.get(i).getValue());
				else if (criterios.get(i).getColumn().equalsIgnoreCase("A.dt_inicial") && criterios.get(i).getTypeComparation() == ItemComparator.MINOR_EQUAL)
					dtFinal = Util.convStringToCalendar(criterios.get(i).getValue());
			Result result = new Result(1);
			result.addObject("rsmAgendamentos", olderfind(criteriosTemp, tpViewRegisters, tpOrdenamento, connection));
			result.addObject("rsmFeriados", FeriadoServices.getAll(dtInicial, dtFinal, connection));
			return result;
		}
	}

	@Deprecated
	public static ResultSetMap findAgendamentosOfAgenda(ArrayList<ItemComparator> criterios) {
		return findAgendamentosOfAgenda(criterios, null);
	}

	@Deprecated
	public static ResultSetMap findAgendamentosOfAgenda(ArrayList<ItemComparator> criterios, Connection connection) {
		int cdUsuario = 0;
		for (int i=0; criterios!=null && i<criterios.size(); i++) {
			if (criterios.get(i).getColumn().equalsIgnoreCase("cdUsuario")) {
				cdUsuario = Integer.parseInt(criterios.get(i).getValue());
				criterios.remove(i);
				break;
			}
		}
		Usuario usuario = cdUsuario<=0 ? null : UsuarioDAO.get(cdUsuario, connection);
		int cdPessoa = usuario==null ? 0 : usuario.getCdPessoa();
		ResultSetMap rsm = Search.find("SELECT A.*, B.nm_agenda, B.cd_empresa, B.txt_abertura_ata, " +
				"B.txt_agenda, C.nr_documento, C.txt_documento, D.nm_mailing, D.nm_assunto, D.txt_mailing, E.nm_tipo_agendamento " +
				"FROM agd_agendamento A " +
				"JOIN agd_agenda B ON (A.cd_agenda = B.cd_agenda) " +
				"LEFT OUTER JOIN ptc_documento C ON (A.cd_documento = C.cd_documento) " +
				"LEFT OUTER JOIN crm_mailing D ON (A.cd_mailing = D.cd_mailing) " +
				"LEFT OUTER JOIN agd_tipo_agendamento E ON (A.cd_tipo_agendamento = E.cd_tipo_agendamento) " +
				"WHERE 1=1 " +
				(cdPessoa>0 ? "  AND " + cdPessoa + " IN (SELECT cd_participante " +
				"			 FROM agd_agenda_participante " +
				"			 WHERE cd_agenda = B.cd_agenda " +
				"			   AND st_participante = " + AgendaParticipanteServices.ST_ATIVO + ")" : ""), criterios, true, connection!=null ? connection : Conexao.conectar(), connection==null);
		while (rsm.next()) {
			rsm.getRegister().put("DS_DT_AGENDAMENTO", Recursos.diasSemana[rsm.getGregorianCalendar("DT_INICIAL").get(Calendar.DAY_OF_WEEK) - 1] +
					", " + Util.formatDateTime(rsm.getTimestamp("DT_INICIAL"), "dd/MM/yyyy HH:mm:ss"));
			rsm.getRegister().put("NR_AGENDAMENTO", rsm.getString("ID_AGENDAMENTO") + " - " + (rsm.getInt("cd_recorrencia")>0 ? rsm.getInt("nr_recorrencia") +
					"/" + rsm.getInt("qt_recorrencias") : "1/1"));
		}
		rsm.beforeFirst();
		ArrayList<String> fields = new ArrayList<String>();
		fields.add("DT_INICIAL");
		rsm.orderBy(fields);
		return rsm;
	}

	@Deprecated
	public static ResultSetMap olderfind(ArrayList<ItemComparator> criterios) {
		return olderfind(criterios, null);
	}

	@Deprecated
	public static ResultSetMap olderfind(ArrayList<ItemComparator> criterios, Connection connection) {
		return olderfind(criterios, AGD_REPORT, connection);
	}

	@Deprecated
	public static ResultSetMap olderfind(ArrayList<ItemComparator> criterios, int tpViewRegisters, Connection connection) {
		return olderfind(criterios, tpViewRegisters, ORD_DESCRESCENTE, connection);
	}

	@Deprecated
	public static ResultSetMap olderfind(ArrayList<ItemComparator> criterios, int tpViewRegisters, int tpOrdenamento, Connection connection) {
		int cdUsuario = 0;
		String txtOcorrencia = null;
		ArrayList<ItemComparator> criteriosAdicionais = new ArrayList<ItemComparator>();
		boolean findAtrasados = false;
		boolean findComLembrete = false;
		GregorianCalendar dtFinalAtrasados = null;

		for (int i=0; criterios!=null && i<criterios.size(); i++) {
			if (((ItemComparator)criterios.get(i)).getColumn().equals("justUsuario")) {
				cdUsuario = Integer.parseInt((String)((ItemComparator)criterios.get(i)).getValue());
				criterios.remove(i);
				i--;
			}
			else if (((ItemComparator)criterios.get(i)).getColumn().equals("findComLembrete")) {
				findComLembrete = true;
				criterios.remove(i);
				i--;
			}
			else if (((ItemComparator)criterios.get(i)).getColumn().equals("findAtrasados")) {
				dtFinalAtrasados = Util.convStringCalendar((String)((ItemComparator)criterios.get(i)).getValue());
				if (dtFinalAtrasados == null) {
					dtFinalAtrasados = new GregorianCalendar();
					dtFinalAtrasados.set(Calendar.HOUR_OF_DAY, 23);
					dtFinalAtrasados.set(Calendar.MINUTE, 59);
					dtFinalAtrasados.set(Calendar.SECOND, 59);
					dtFinalAtrasados.set(Calendar.MILLISECOND, 999);
				}
				findAtrasados = true;
				criterios.remove(i);
				i--;
			}
			else if (((ItemComparator)criterios.get(i)).getColumn().equalsIgnoreCase("TXT_OCORRENCIA")) {
				txtOcorrencia = (String)((ItemComparator)criterios.get(i)).getValue();
				if (txtOcorrencia != null)
					txtOcorrencia = txtOcorrencia.trim();
				if (txtOcorrencia.trim().equals(""))
					txtOcorrencia = null;
				criterios.remove(i);
				i--;
			}
			else if (((ItemComparator)criterios.get(i)).getColumn().equalsIgnoreCase("A.id_agendamento")) {
				String keySearch = (String)((ItemComparator)criterios.get(i)).getValue();
				if (keySearch != null)
					keySearch = keySearch.trim();
				if (keySearch == null || keySearch.equals(""))
					criterios.remove(i);
				else if (keySearch.indexOf('-')==0)
					return null;
				else {
					String idAgendamento = keySearch;
					String nrRecorrencia = null;
					if (keySearch.indexOf('-')>0) {
						idAgendamento = keySearch.substring(0, keySearch.indexOf('-')).trim();
						nrRecorrencia = keySearch.indexOf('-') == keySearch.length() - 1 ? null : keySearch.substring(keySearch.indexOf('-') + 1).trim();
					}
					if (!Util.isWellFormattedIntegerValue(idAgendamento))
						return null;
					((ItemComparator)criterios.get(i)).setValue(idAgendamento);
					if (nrRecorrencia!=null && !nrRecorrencia.equals("")) {
						if (nrRecorrencia.indexOf('/') == 0)
							return null;
						if (nrRecorrencia.indexOf('/') > 0)
							nrRecorrencia = nrRecorrencia.substring(0, nrRecorrencia.indexOf('/')).trim();
						if (nrRecorrencia.equals("") || !Util.isWellFormattedIntegerValue(nrRecorrencia))
							return null;
						else
							criteriosAdicionais.add(new ItemComparator("A.nr_recorrencia", nrRecorrencia, ItemComparator.EQUAL, Types.INTEGER));
					}
				}
			}
		}

		/* acrescimo dos criterios adicionais */
		for (int i=0; i<criteriosAdicionais.size(); i++)
			criterios.add(criteriosAdicionais.get(i));

		String relations = criterios.size()==0 ? "" : "(";
		for (int i=0; i<criterios.size(); i++)
			relations += (i==0 ? "" : " AND ") + (i + 1);
		if (criterios.size() != 0)
			relations += ")";

		if (!relations.equals("") && findAtrasados) {
			String relationsTemp = "(" + (criterios.size()+1) + " AND " + (criterios.size()+2) + ")";
			criterios.add(new ItemComparator("A.st_agendamento", Integer.toString(ST_PENDENTE), ItemComparator.EQUAL, Types.SMALLINT));
			criterios.add(new ItemComparator("A.dt_inicial", Util.formatDateTime(dtFinalAtrasados, "dd/MM/yyyy HH:mm:ss"), ItemComparator.MINOR_EQUAL, Types.TIMESTAMP));
			relations = "(" + relations + " OR " + relationsTemp + ")";
		}
		else if (findComLembrete) {
			int indexParam = criterios.size();
			indexParam++;
			GregorianCalendar dtAtual = new GregorianCalendar();
			criterios.add(new ItemComparator("A.lg_lembrete", "1", ItemComparator.EQUAL, Types.SMALLINT));
			String relationsTemp = "(" + indexParam + " AND (";
			for (int i=0; i<correlacoesOpcoesLembrete.length; i++) {
				criterios.add(new ItemComparator("A.tp_unidade_tempo_lembrete", Integer.toString(correlacoesOpcoesLembrete[i][1]), ItemComparator.EQUAL, Types.SMALLINT));
				criterios.add(new ItemComparator("A.qt_tempo_lembrete", Integer.toString(correlacoesOpcoesLembrete[i][0]), ItemComparator.EQUAL, Types.SMALLINT));
				GregorianCalendar dtTemp = (GregorianCalendar)dtAtual.clone();
				int field = correlacoesOpcoesLembrete[i][1]==TMP_MINUTO ? Calendar.MINUTE :
					correlacoesOpcoesLembrete[i][1]==TMP_HORA ? Calendar.HOUR_OF_DAY :
					correlacoesOpcoesLembrete[i][1]==TMP_DIA ? Calendar.DAY_OF_MONTH : Calendar.WEEK_OF_MONTH;
				dtTemp.add(field, correlacoesOpcoesLembrete[i][0]);
				dtTemp.set(Calendar.SECOND, 59);
				dtTemp.set(Calendar.MILLISECOND, 999);
				criterios.add(new ItemComparator("A.dt_inicial", Util.formatDate(dtTemp, "dd/MM/yyyy HH:mm:ss:SSS"), ItemComparator.MINOR_EQUAL, Types.TIMESTAMP));
				relationsTemp += (i==0 ? "" : "OR") + "(" + (indexParam+1) + " AND " + (indexParam+2) + " AND " + (indexParam+3) + ")";
				indexParam += 3;
			}
			relationsTemp += "))";
			relations = "(" + relations + " AND " + relationsTemp + ")";
		}

		ResultSetMap rsm = Search.find("SELECT " + (tpViewRegisters==AGD_REPORT ? ("A.*, B.cd_usuario AS cd_usuario_criador, " +
				"C.cd_usuario AS cd_usuario_responsavel, " +
				"D.dt_inicio, D.dt_termino, D.tp_termino, D.nr_recorrencias, D.tp_recorrencia, D.tp_especificidade_recorrencia, " +
				"D.lg_domingo, D.lg_segunda, D.lg_terca, D.lg_quarta, D.lg_quinta, D.lg_sexta, D.lg_sabado, D.qt_intervalo_recorrencia, " +
				"D.nr_dia_recorrencia_mensal, D.nr_ordem_recorrencia_mensal, D.tp_ordem_recorrencia_mensal, D.nr_dia_recorrencia_anual, " +
				"D.nr_mes_recorrencia_anual, D.tp_ordem_recorrencia_anual, D.nr_ordem_recorrencia_anual, F.nm_tipo_agendamento, " +
				"F.tp_agendamento, F.lg_negrito_texto, F.id_cor_background, F.id_cor_texto, G.nm_login AS nm_login_criador, " +
				"I.nm_pessoa AS nm_usuario_criador, H.nm_login AS nm_login_responsavel, J.nm_pessoa AS nm_usuario_responsavel, " +
				"(SELECT COUNT(*) FROM agd_agendamento L WHERE L.cd_recorrencia = A.cd_recorrencia) AS qt_recorrencias, " +
				"(SELECT COUNT(*) " +
				" FROM agd_agendamento_usuario " +
				" WHERE cd_agendamento = A.cd_agendamento " +
				"   AND NOT cd_usuario IN (SELECT cd_usuario " +
				"						   FROM agd_agendamento_usuario " +
				"						   WHERE cd_agendamento = A.cd_agendamento " +
				"							 AND (tp_nivel_usuario = " + AgendamentoUsuarioServices.TP_CRIADOR_RESPONSAVEL + " OR " +
				"								  tp_nivel_usuario = " + AgendamentoUsuarioServices.TP_RESPONSAVEL + " OR " +
				"								  tp_nivel_usuario = " + AgendamentoUsuarioServices.TP_CRIADOR + "))) AS qt_participantes ") :
				"CAST(A.dt_inicial AS DATE) AS DT_AGENDAMENTO, COUNT(*) AS qt_agendamentos ") +
				"FROM agd_agendamento A " +
				"LEFT OUTER JOIN agd_recorrencia D ON (A.cd_recorrencia = D.cd_recorrencia) " +
				"LEFT OUTER JOIN agd_tipo_agendamento F ON (A.cd_tipo_agendamento = F.cd_tipo_agendamento) " +
				"LEFT OUTER JOIN agd_agendamento_usuario B ON (A.cd_agendamento = B.cd_agendamento AND " +
				"												(B.tp_nivel_usuario = " + AgendamentoUsuarioServices.TP_CRIADOR_RESPONSAVEL + " OR B.tp_nivel_usuario = " + AgendamentoUsuarioServices.TP_CRIADOR + ")) " +
				"LEFT OUTER JOIN agd_agendamento_usuario C ON (A.cd_agendamento = C.cd_agendamento AND " +
				"													(C.tp_nivel_usuario = " + AgendamentoUsuarioServices.TP_CRIADOR_RESPONSAVEL + " OR C.tp_nivel_usuario = " + AgendamentoUsuarioServices.TP_RESPONSAVEL + ")) " +
				"LEFT OUTER JOIN seg_usuario G ON (G.cd_usuario = B.cd_usuario) " +
				"LEFT OUTER JOIN grl_pessoa I ON (I.cd_pessoa = G.cd_pessoa) " +
				"LEFT OUTER JOIN seg_usuario H ON (H.cd_usuario = C.cd_usuario) " +
				"LEFT OUTER JOIN grl_pessoa J ON (J.cd_pessoa = H.cd_pessoa) " +
				"WHERE 1=1 " +
				(cdUsuario > 0 ? " AND EXISTS (SELECT E.tp_nivel_usuario FROM agd_agendamento_usuario E " +
				"			 WHERE E.cd_agendamento = A.cd_agendamento " +
				"			   AND E.cd_usuario = " + cdUsuario + ") " : "") +
				(txtOcorrencia != null ? " AND EXISTS (SELECT L.cd_ocorrencia FROM grl_ocorrencia L, agd_ocorrencia M " +
						"			 WHERE L.cd_ocorrencia = M.cd_ocorrencia " +
						"			   AND M.cd_agendamento = A.cd_agendamento " +
						"			   AND UPPER(L.txt_ocorrencia) LIKE '%" + txtOcorrencia.toUpperCase() + "%') " : ""),
						tpViewRegisters==AGD_CALENDAR ? " GROUP BY CAST(A.dt_inicial AS DATE) " : null, criterios, relations,
				connection!=null ? connection : Conexao.conectar(), connection==null, false/*lgSearchLog*/, true/*mapToHash*/);
		if (tpViewRegisters == AGD_REPORT) {
			GregorianCalendar dtAtual = new GregorianCalendar();
			while (rsm != null && rsm.next()) {
				int lgLembrete = rsm.getInt("lg_lembrete");
				int qtTempoLembrete = rsm.getInt("qt_tempo_lembrete");
				int tpUnidadeTempoLembrete = rsm.getInt("tp_unidade_tempo_lembrete");
				int cdTipoLembrete = 0;
				for (int i=0; lgLembrete==1 && i<correlacoesOpcoesLembrete.length; i++) {
					if (correlacoesOpcoesLembrete[i][0] == qtTempoLembrete && correlacoesOpcoesLembrete[i][1] == tpUnidadeTempoLembrete) {
						cdTipoLembrete = i+1;
						break;
					}
				}
				rsm.getRegister().put("LG_ATRASADO", rsm.getTimestamp("DT_INICIAL")!=null && Util.convTimestampToCalendar(rsm.getTimestamp("DT_INICIAL")).before(dtAtual) ? "1" : "0");
				rsm.getRegister().put("DS_DT_AGENDAMENTO", Recursos.diasSemana[rsm.getGregorianCalendar("DT_INICIAL").get(Calendar.DAY_OF_WEEK) - 1] +
						", " + Util.formatDateTime(rsm.getTimestamp("DT_INICIAL"), "dd/MM/yyyy HH:mm:ss"));
				rsm.getRegister().put("NR_AGENDAMENTO", rsm.getString("ID_AGENDAMENTO") + " - " + (rsm.getInt("cd_recorrencia")>0 ? rsm.getInt("nr_recorrencia") +
						"/" + rsm.getInt("qt_recorrencias") : "1/1"));
				rsm.getRegister().put("CD_TIPO_LEMBRETE", new Integer(cdTipoLembrete));
				if (findComLembrete) {
					if (lgLembrete == 0 && findAtrasados && dtFinalAtrasados != null && rsm.getTimestamp("DT_INICIAL")!=null &&
							rsm.getGregorianCalendar("DT_INICIAL").before(dtFinalAtrasados))
						continue;
					else {
						int segundos = 0;
						switch (tpUnidadeTempoLembrete) {
							case TMP_MINUTO: segundos = 60; break;
							case TMP_HORA: segundos = 60 * 60; break;
							case TMP_DIA: segundos = 60 * 60 * 24; break;
							case TMP_SEMANA: segundos = 60 * 60 * 24 * 7; break;
						}
						GregorianCalendar dtLembreteTemp = (GregorianCalendar)rsm.getGregorianCalendar("DT_INICIAL").clone();
						dtLembreteTemp.add(Calendar.SECOND, -(segundos * qtTempoLembrete + 1));
						if (!dtAtual.after(dtLembreteTemp)) {
							rsm.deleteRow();
							if (rsm.getPointer() == 0)
								rsm.beforeFirst();
							else
								rsm.previous();
						}
					}
				}
			}
		}
		if (rsm != null) {
			rsm.beforeFirst();
			ArrayList<String> criteriosOrderBy = new ArrayList<String>();
			String clTipoOrder = tpOrdenamento==ORD_CRESCENTE ? "ASC" : "DESC";
			criteriosOrderBy.add(tpViewRegisters == AGD_REPORT ? ("DT_INICIAL " + clTipoOrder) : ("DT_AGENDAMENTO " + clTipoOrder));
			rsm.orderBy(criteriosOrderBy);
		}

		return rsm;
	}

	@Deprecated
	public static int delete(int cdAgendamento) {
		return delete(cdAgendamento, null);
	}

	@Deprecated
	public static int delete(int cdAgendamento, Connection connection){
		boolean isConnectionNull = connection==null;
		try {
			connection = isConnectionNull ? Conexao.conectar() : connection;
			connection.setAutoCommit(isConnectionNull ? false : connection.getAutoCommit());

			Agendamento agendamento = AgendamentoDAO.get(cdAgendamento, connection);
			int cdRecorrencia = agendamento.getCdRecorrencia();

			/* assuntos */
			PreparedStatement pstmt = connection.prepareStatement("DELETE FROM agd_assunto_posicionamento " +
					"WHERE cd_agendamento = ?");
			pstmt.setInt(1, cdAgendamento);
			pstmt.execute();

			pstmt = connection.prepareStatement("DELETE FROM agd_agendamento_assunto " +
					"WHERE cd_agendamento = ?");
			pstmt.setInt(1, cdAgendamento);
			pstmt.execute();

			pstmt = connection.prepareStatement("DELETE FROM agd_agendamento_convidado " +
					"WHERE cd_agendamento = ?");
			pstmt.setInt(1, cdAgendamento);
			pstmt.execute();

			/* participantes */
			pstmt = connection.prepareStatement("DELETE FROM agd_agendamento_participante " +
					"WHERE cd_agendamento = ?");
			pstmt.setInt(1, cdAgendamento);
			pstmt.execute();

			/* usuarios */
			pstmt = connection.prepareStatement("DELETE FROM agd_agendamento_usuario " +
					"WHERE cd_agendamento = ?");
			pstmt.setInt(1, cdAgendamento);
			pstmt.execute();

			/* ocorrencias */
			pstmt = connection.prepareStatement("SELECT * FROM agd_ocorrencia " +
					"WHERE cd_agendamento = ?");
			pstmt.setInt(1, cdAgendamento);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				if (OcorrenciaDAO.delete(rs.getInt("cd_ocorrencia"), connection) <= 0) {
					if (isConnectionNull)
						Conexao.rollback(connection);
					return -1;
				}
			}

			/* arquivos */
			ResultSetMap rsmArquivos = getAllArquivos(cdAgendamento, connection);
			while (rsmArquivos.next())
				if (AgendamentoArquivoServices.delete(cdAgendamento, rsmArquivos.getInt("cd_arquivo"), connection) <= 0) {
					if (isConnectionNull)
						Conexao.rollback(connection);
					return -1;
				}

			pstmt = connection.prepareStatement("DELETE " +
					"FROM crm_mailing_envio " +
					"WHERE (cd_destino, cd_mailing, cd_planejamento) IN (SELECT cd_destino, cd_mailing, cd_planejamento " +
					"													 FROM crm_mailing_destino " +
					"													 WHERE cd_agendamento = ?)");
			pstmt.setInt(1, cdAgendamento);
			pstmt.execute();

			pstmt = connection.prepareStatement("DELETE " +
					"FROM crm_mailing_destino " +
					"WHERE cd_agendamento = ?");
			pstmt.setInt(1, cdAgendamento);
			pstmt.execute();

			if (AgendamentoDAO.delete(cdAgendamento, connection) <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connection);
				return -1;
			}

			if (agendamento.getCdAgenda()>0 && agendamento.getCdMailing()>0) {
				if (MailingServices.delete(agendamento.getCdMailing(), connection) <= 0) {
					if (isConnectionNull)
						Conexao.rollback(connection);
					return -1;
				}
			}

			if (cdRecorrencia > 0) {
				pstmt = connection.prepareStatement("SELECT cd_agendamento FROM agd_agendamento A " +
						"WHERE cd_recorrencia = ? " +
						"  AND cd_agendamento <> ?");
				pstmt.setInt(1, cdRecorrencia);
				pstmt.setInt(2, cdAgendamento);
				rs = pstmt.executeQuery();
				if (!rs.next() && RecorrenciaDAO.delete(cdRecorrencia, connection) <= 0) {
					if (isConnectionNull)
						Conexao.rollback(connection);
					return -1;
				}
			}

			if (isConnectionNull)
				connection.commit();

			return 1;
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! AgendamentoServices.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}

	@Deprecated
	public static int update(int cdAgendamento, GregorianCalendar dtInicial, int cdTipoLembrete) {
		return update(cdAgendamento, dtInicial, cdTipoLembrete, null, null);
	}

	@Deprecated
	public static int update(int cdAgendamento, GregorianCalendar dtInicial, int cdTipoLembrete, GregorianCalendar dtLembrete) {
		return update(cdAgendamento, dtInicial, cdTipoLembrete, dtLembrete, null);
	}

	@Deprecated
	public static int update(int cdAgendamento, GregorianCalendar dtInicial, int cdTipoLembrete, GregorianCalendar dtLembrete,
			Connection connection) {
		boolean isConnectionNull = connection==null;
		try {
			connection = isConnectionNull ? Conexao.conectar() : connection;
			Agendamento agendamento = AgendamentoDAO.get(cdAgendamento, connection);
			agendamento.setDtInicial(dtInicial);
			return AgendamentoServices.update(agendamento, cdTipoLembrete, 0, null, connection) != null ? 1 : 0;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			return -1;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}

	@Deprecated
	public static int update(int cdAgendamento, int stAgendamento) {
		return update(cdAgendamento, stAgendamento, null);
	}

	@Deprecated
	public static int update(int cdAgendamento, int stAgendamento, Connection connection) {
		boolean isConnectionNull = connection==null;
		try {
			if (isConnectionNull)
				connection = Conexao.conectar();

			Agendamento agendamento = AgendamentoDAO.get(cdAgendamento, connection);
			agendamento.setStAgendamento(stAgendamento);
			return AgendamentoDAO.update(agendamento, connection);
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			return -1;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}

	@Deprecated
	public static int registerRecebProcesso(int cdAgendamento) {
		return registerRecebProcesso(cdAgendamento, null);
	}

	@Deprecated
	public static int registerRecebProcesso(int cdAgendamento, Connection connection) {
		boolean isConnectionNull = connection==null;
		try {
			if (isConnectionNull)
				connection = Conexao.conectar();

			PreparedStatement pstmt = connection.prepareStatement("UPDATE grl_processo_atividade_item " +
					"SET dt_recebimento = ? " +
					"WHERE cd_agendamento = ? " +
					"  AND dt_recebimento IS NULL");
			pstmt.setTimestamp(1, Util.convCalendarToTimestamp(new GregorianCalendar()));
			pstmt.setInt(2, cdAgendamento);
			pstmt.execute();

			return 1;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			return -1;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}

	@Deprecated
	public static int disableStatusOriginal(int cdAgendamento) {
		return disableStatusOriginal(cdAgendamento, null);
	}

	@Deprecated
	public static int disableStatusOriginal(int cdAgendamento, Connection connection) {
		boolean isConnectionNull = connection==null;
		try {
			if (isConnectionNull) {
				connection = Conexao.conectar();
				connection.setAutoCommit(false);
			}

			PreparedStatement pstmt = connection.prepareStatement("UPDATE grl_processo_atividade_item " +
					"SET dt_recebimento = ? " +
					"WHERE cd_agendamento = ?");
			pstmt.setTimestamp(1, Util.convCalendarToTimestamp(new GregorianCalendar()));
			pstmt.setInt(2, cdAgendamento);
			pstmt.execute();

			pstmt = connection.prepareStatement("UPDATE agd_agendamento " +
					"SET lg_original = 0 " +
					"WHERE cd_agendamento = ?");
			pstmt.setInt(1, cdAgendamento);
			pstmt.execute();

			if (isConnectionNull)
				connection.commit();

			return 1;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			if (isConnectionNull)
				Conexao.rollback(connection);
			return -1;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}

	@Deprecated
	public static ArrayList<HashMap<String, Object>> getParams() {
		return getParams(null);
	}

	@Deprecated
	public static ArrayList<HashMap<String, Object>> getParams(Connection connection) {
		boolean isConnectionNull = connection==null;
		try {
			if (isConnectionNull)
				connection = Conexao.conectar();

			PreparedStatement pstmt = connection.prepareStatement("SELECT * FROM SEG_MODULO " +
					"WHERE ID_MODULO = ?");
			pstmt.setString(1, "agd");
			ResultSet rs = pstmt.executeQuery();
			int cdModulo = rs.next() ? rs.getInt("CD_MODULO") : 0;
			if (cdModulo <= 0)
				return null;

			ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
			criterios.add(new ItemComparator("A.cd_modulo", Integer.toString(cdModulo), ItemComparator.EQUAL, Types.INTEGER));
			ResultSetMap rsm = ParametroServices.find(criterios, connection);

			ArrayList<HashMap<String, Object>> params = new ArrayList<HashMap<String,Object>>();
			while (rsm.next()) {
				if (rsm.getInt("tp_dado") != ParametroServices.IMAGEM) {
					HashMap<String, Object> hashMap = new HashMap<String, Object>();
					hashMap.put("NM_PARAMETRO", rsm.getString("nm_parametro"));
					hashMap.put("VL_PARAMETRO", ParametroServices.getValorOfParametro(rsm.getString("nm_parametro"), "", connection));
					params.add(hashMap);
				}
			}

			return params;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}

	@Deprecated
	public static ResultSetMap getAllAssuntos(int cdAgendamento) {
		return getAllAssuntos(cdAgendamento, false, null);
	}

	@Deprecated
	public static ResultSetMap getAllAssuntos(int cdAgendamento, boolean loadVotosContra) {
		return getAllAssuntos(cdAgendamento, loadVotosContra, null);
	}

	@Deprecated
	public static ResultSetMap getAllAssuntos(int cdAgendamento, boolean loadVotosContra, Connection connection) {
		boolean isConnectionNull = connection==null;
		try {
			connection = isConnectionNull ? Conexao.conectar() : connection;
			PreparedStatement pstmt = connection.prepareStatement("SELECT A.*, B.nm_assunto, B.tp_assunto, " +
					"C.cd_pessoa, C.nm_pessoa AS nm_autor, D.nm_pessoa AS nm_responsavel, B.txt_assunto  " +
					"FROM agd_agendamento_assunto A " +
					"JOIN agd_assunto B ON (A.cd_assunto = B.cd_assunto) " +
					"LEFT OUTER JOIN grl_pessoa C ON (B.cd_pessoa = C.cd_pessoa) " +
					"LEFT OUTER JOIN grl_pessoa D ON (A.cd_responsavel = D.cd_pessoa) " +
					"WHERE A.cd_agendamento = ?");
			pstmt.setInt(1, cdAgendamento);
			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
			if (loadVotosContra) {
				pstmt = connection.prepareStatement("SELECT A.cd_agendamento, A.cd_assunto, B.nm_pessoa " +
						"FROM agd_agend_assunto_fechamento A, grl_pessoa B " +
						"WHERE A.cd_participante = B.cd_pessoa " +
						"  AND A.cd_agendamento = ? " +
						"  AND A.lg_posicionamento_contrario = 1");
				pstmt.setInt(1, cdAgendamento);
				ResultSet rs = pstmt.executeQuery();
				while (rs.next()) {
					boolean locate = rsm.locate("cd_assunto", rs.getInt("cd_assunto"), false);
					String nmParticipantesContra = !locate ? "" : (String)rsm.getRegister().get("NM_PARTICIPANTES_CONTRA");
					nmParticipantesContra = nmParticipantesContra==null ? "" : nmParticipantesContra;
					nmParticipantesContra += (nmParticipantesContra.equals("") ? "" : "; ") + rs.getString("NM_PESSOA");
					if (locate)
						rsm.getRegister().put("NM_PARTICIPANTES_CONTRA", nmParticipantesContra);
				}
			}

			ArrayList<String> fields = new ArrayList<String>();
			fields.add("nr_ordem");
			rsm.orderBy(fields);
			rsm.beforeFirst();

			return rsm;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}

	@Deprecated
	public static Result sendAtaReuniao(int cdAgendamento) {
		return sendAtaReuniao(cdAgendamento, null);
	}

	@Deprecated
	public static Result sendAtaReuniao(int cdAgendamento, Connection connection) {
		boolean isConnectionNull = connection==null;
		try {
			connection = isConnectionNull ? Conexao.conectar() : connection;
			connection.setAutoCommit(isConnectionNull ? false : connection.getAutoCommit());

			int cdContaEnvio = ParametroServices.getValorOfParametroAsInteger("CD_MAILING_CONTA_ENVIO_ATA_REUNIAO", 0 /*defaultValue*/, 0 /*cdEmpresa*/, connection);
			if (cdContaEnvio <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connection);
				return new Result(-1, "Não é possível enviar e-mail com o conteúdo da ata aos participantes da reunião. Certifique-se de que a conta " +
						"para envio de e-mails de atas esteja configurada.");
			}

			int cdModelo = ParametroServices.getValorOfParametroAsInteger("CD_MODELO_DOCUMENTO_ATA_REUNIAO", 0 /*defaultValue*/, 0 /*cdEmpresa*/, connection);
			if (cdModelo <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connection);
				return new Result(-1, "Não é possível enviar e-mail com o conteúdo da ata aos participantes da reunião. Certifique-se de que o template " +
						"para geração de ata de reunião esteja configurado.");
			}

			Agendamento agendamento = AgendamentoDAO.get(cdAgendamento, connection);
			int cdMailingOld = agendamento.getCdMailing();
			int cdMailing = cdMailingOld;
			Mailing mailing = cdMailing<=0 ? new Mailing(0 /*cdMailing*/,
					"" /*nmMailing*/,
					"" /*txtMailing*/,
					cdModelo,
					0 /*cdGrupo*/,
					0 /*cdContaEnvio*/,
					"" /*nmAssunto*/) :
				MailingDAO.get(agendamento.getCdMailing(), connection);
			if (mailing.getCdMailing()<=0 && (cdMailing = MailingDAO.insert(mailing, connection)) <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connection);
				return new Result(-1, "Erros reportados ao preparar mailing para envio de ata de reunião aos participantes. Entre " +
						"em contato com o suporte técnico.");
			}
			if (cdMailingOld<=0) {
				agendamento.setCdMailing(cdMailing);
				if (AgendamentoDAO.update(agendamento, connection) <= 0) {
					if (isConnectionNull)
						Conexao.rollback(connection);
					return new Result(-1, "Erros reportados ao atualizar parâmetros de reunião durante rotina de preparação para envio de ata de reunião " +
							"aos participantes. Entre em contato com o suporte técnico.");
				}
			}
			else {
				mailing.setCdModelo(cdModelo);
				if (MailingDAO.update(mailing, connection) <= 0) {
					throw new Exception("Exceção reportada ao atualizar registro de configuração de mailing de reunião/agendamento.");
				}
			}

			MailingPlanejamento planejamento = new MailingPlanejamento(0 /*cdPlanejamento*/,
					cdMailing,
					new GregorianCalendar() /*dtPlanejamento*/,
					null /*dtEnvio*/,
					MailingPlanejamentoServices.ST_PENDENTE /*stPlanejamento*/,
					0 /*cdUsuario*/,
					cdContaEnvio /*cdContaEnvio*/,
					"Ata de Reunião" /*nmAssunto*/,
					"" /*txtParametros*/);

			int cdPlanejamento = 0;
			if ((cdPlanejamento = MailingPlanejamentoDAO.insert(planejamento, connection)) <= 0)
				throw new Exception("Exceção reportada ao inserir registro de planejamento de mailing de reunião/agendamento.");

			PreparedStatement pstmt = connection.prepareStatement("SELECT cd_destino " +
					"FROM crm_mailing_destino " +
					"WHERE cd_mailing = ? " +
					"  AND cd_agendamento = ?");
			pstmt.setInt(1, mailing.getCdMailing());
			pstmt.setInt(2, cdAgendamento);
			ResultSet rs = pstmt.executeQuery();
			int cdDestino = !rs.next() ? 0 : rs.getInt("cd_destino");

			if (cdDestino<=0 && (cdDestino = MailingDestinoDAO.insert(new MailingDestino(0 /*cdDestino*/,
					mailing.getCdMailing(),
					0 /*cdGrupo*/,
					0 /*cdPessoa*/,
					0 /*cdFonte*/,
					cdAgendamento), connection)) <= 0) {
				throw new Exception("Exceção reportada ao inserir registro de destino de mailing de reunião/agendamento.");
			}

			HashMap<String, String> parametros = new HashMap<String, String>();
			parametros.put("#CD_AGENDAMENTO", Integer.toString(cdAgendamento));
			if (MailingEnvioServices.executeEnvio(mailing.getCdMailing(), cdPlanejamento,
					0 /*cdUsuario*/,
					parametros /*parametros*/, connection) <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connection);
				return new Result(-1, "Problemas técnicos reportados ao enviar ata de reunião, por e-mail, aos participantes. " +
						"Certifique-se de a conta de e-mail usada para envio dos atas esteja configurada corretamente.");
			}

			if (isConnectionNull)
				connection.commit();

			return new Result(1, "Ata de reunião enviada com sucesso aos participantes da reunião.");
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			if (isConnectionNull)
				Conexao.rollback(connection);
			return new Result(-1, "Erros reportados ao enviar ata, por e-mail, aos participantes da reunião selecionada. Anote " +
					"a mensagem de erro abaixo e entre em contato com o suporte técnico.\n" +
					e.getLocalizedMessage());
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}

	@Deprecated
	public static Result sendConvite(int cdAgendamento) {
		return sendConvite(cdAgendamento, null);
	}

	@Deprecated
	public static Result sendConvite(int cdAgendamento, Connection connection) {
		boolean isConnectionNull = connection==null;
		try {
			connection = isConnectionNull ? Conexao.conectar() : connection;
			connection.setAutoCommit(isConnectionNull ? false : connection.getAutoCommit());

			Agendamento agendamento = AgendamentoDAO.get(cdAgendamento, connection);
			System.out.println("MAILINGF: " + agendamento.getCdMailing());
			if (agendamento.getCdMailing()<=0) {
				if (isConnectionNull)
					Conexao.rollback(connection);
				return new Result(-1, "Certifique-se de que os dados de envio de convite (assunto, local, mensagem) estejam configurados " +
						"antes de proceder com o envio de e-mails aos participantes.");
			}

			Mailing mailing = MailingDAO.get(agendamento.getCdMailing(), connection);
			int cdContaEnvio = ParametroServices.getValorOfParametroAsInteger("CD_MAILING_CONTA_ENVIO_CONVITE_REUNIAO", 0 /*defaultValue*/, 0 /*cdEmpresa*/, connection);
			if (cdContaEnvio <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connection);
				return new Result(-1, "Não é possível enviar e-mails de convite aos participantes da reunião. Certifique-se de que a conta " +
						"para envio de e-mails de convite esteja configurada.");
			}

			int cdModelo = ParametroServices.getValorOfParametroAsInteger("CD_MODELO_DOCUMENTO_CONVITE_REUNIAO", 0 /*defaultValue*/, 0 /*cdEmpresa*/, connection);
			if (cdModelo <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connection);
				return new Result(-1, "Não é possível enviar e-mails de convite aos participantes da reunião. Certifique-se de que o template " +
						"para envio de e-mails de convite esteja configurado.");
			}

			mailing.setCdModelo(cdModelo);
			mailing.setCdContaEnvio(cdContaEnvio);
			if (MailingDAO.update(mailing, connection) <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connection);
				throw new Exception("Exceção reportada ao atualizar registro de configuração de mailing de reunião/agendamento.");
			}

			MailingPlanejamento planejamento = new MailingPlanejamento(0 /*cdPlanejamento*/,
					agendamento.getCdMailing(),
					new GregorianCalendar() /*dtPlanejamento*/,
					null /*dtEnvio*/,
					MailingPlanejamentoServices.ST_PENDENTE /*stPlanejamento*/,
					0 /*cdUsuario*/,
					cdContaEnvio /*cdContaEnvio*/,
					mailing.getNmAssunto() /*nmAssunto*/,
					"" /*txtParametros*/);

			int cdPlanejamento = 0;
			if ((cdPlanejamento = MailingPlanejamentoDAO.insert(planejamento, connection)) <= 0)
				throw new Exception("Exceção reportada ao inserir registro de planejamento de mailing de reunião/agendamento.");

			PreparedStatement pstmt = connection.prepareStatement("SELECT cd_destino " +
					"FROM crm_mailing_destino " +
					"WHERE cd_mailing = ? " +
					"  AND cd_agendamento = ?");
			pstmt.setInt(1, mailing.getCdMailing());
			pstmt.setInt(2, cdAgendamento);
			ResultSet rs = pstmt.executeQuery();
			int cdDestino = !rs.next() ? 0 : rs.getInt("cd_destino");

			if (cdDestino<=0 && (cdDestino = MailingDestinoDAO.insert(new MailingDestino(0 /*cdDestino*/,
					mailing.getCdMailing(),
					0 /*cdGrupo*/,
					0 /*cdPessoa*/,
					0 /*cdFonte*/,
					cdAgendamento), connection)) <= 0) {
				throw new Exception("Exceção reportada ao inserir registro de destino de mailing de reunião/agendamento.");
			}

			HashMap<String, String> parametros = new HashMap<String, String>();
			parametros.put("#CD_AGENDAMENTO", Integer.toString(cdAgendamento));
			if (MailingEnvioServices.executeEnvio(mailing.getCdMailing(), cdPlanejamento,
					0 /*cdUsuario*/,
					parametros /*parametros*/, connection) <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connection);
				return new Result(-1, "Problemas técnicos reportados ao enviar convite, por e-mail, aos participantes da reunião selecionada. " +
						"Certifique-se de a conta de e-mail usada para envio dos convites esteja configurada corretamente.");
			}

			if (isConnectionNull)
				connection.commit();

			return new Result(1, "Convite enviado com sucesso aos participantes da reunião.");
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			if (isConnectionNull)
				Conexao.rollback(connection);
			return new Result(-1, "Erros reportados ao enviar convite, por e-mail, aos participantes da reunião selecionada. Anote " +
					"a mensagem de erro abaixo e entre em contato com o suporte técnico.\n" +
					e.getLocalizedMessage());
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}

	@Deprecated
	public static Result saveMailing(int cdAgendamento, String nmLocal, String txtMailing) {
		return saveMailing(cdAgendamento, nmLocal, txtMailing, null, null);
	}

	@Deprecated
	public static Result saveMailing(int cdAgendamento, String nmLocal, String txtMailing,
			ArrayList<HashMap<String, String>> convidados) {
		return saveMailing(cdAgendamento, nmLocal, txtMailing, convidados, null);
	}

	@Deprecated
	public static Result saveMailing(int cdAgendamento, String nmLocal, String txtMailing,
			ArrayList<HashMap<String, String>> convidados, Connection connection) {
		boolean isConnectionNull = connection==null;
		try {
			connection = isConnectionNull ? Conexao.conectar() : connection;
			connection.setAutoCommit(isConnectionNull ? false : connection.getAutoCommit());

			Agendamento agendamento = AgendamentoDAO.get(cdAgendamento, connection);
			Agenda agenda = AgendaDAO.get(agendamento.getCdAgenda(), connection);
			int cdMailing = agendamento.getCdMailing();
			String nmMailing = "Configuração de Mailing - " + agendamento.getNmAgendamento().toString();
			nmMailing = nmMailing.length()>50 ? nmMailing.substring(0, 50) : nmMailing;
			String nmAssunto = "Reunião do(a) Grupo " + agenda.getNmAgenda();
			nmAssunto = nmAssunto.length()>256 ? nmAssunto.substring(0, 256) : nmAssunto;
			if (cdMailing<=0) {
				if ((cdMailing = MailingDAO.insert(new Mailing(0 /*cdMailing*/,
						nmMailing /*nmMailing*/,
						txtMailing,
						ParametroServices.getValorOfParametroAsInteger("CD_MODELO_DOCUMENTO_CONVITE_REUNIAO", 0 /*defaultValue*/, 0 /*cdEmpresa*/, connection) /*cdModelo*/,
						0 /*cdGrupo*/,
						ParametroServices.getValorOfParametroAsInteger("CD_MAILING_CONTA_ENVIO_CONVITE_REUNIAO", 0 /*defaultValue*/, 0 /*cdEmpresa*/, connection) /*cdContaEnvio*/,
						nmAssunto), connection)) <= 0) {
					if (isConnectionNull)
						Conexao.rollback(connection);
					return new Result(-1, "Erros reportados ao registrar configuração de mailing para a reunião selecionada.");
				}
			}
			else if (MailingDAO.update(new Mailing(cdMailing /*cdMailing*/,
					nmMailing /*nmMailing*/,
					txtMailing,
					ParametroServices.getValorOfParametroAsInteger("CD_MODELO_DOCUMENTO_CONVITE_REUNIAO", 0 /*defaultValue*/, 0 /*cdEmpresa*/, connection) /*cdModelo*/,
					0 /*cdGrupo*/,
					ParametroServices.getValorOfParametroAsInteger("CD_MAILING_CONTA_ENVIO_CONVITE_REUNIAO", 0 /*defaultValue*/, 0 /*cdEmpresa*/, connection) /*cdContaEnvio*/,
					nmAssunto), connection) <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connection);
				return new Result(-1, "Erros reportados ao atualizar configuração de mailing para a reunião selecionada.");
			}

			agendamento.setCdMailing(cdMailing);
			agendamento.setNmLocal(nmLocal);
			if (AgendamentoDAO.update(agendamento, connection) <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connection);
				return new Result(-1, "Erros reportados ao atualizar dados da reunião selecionada.");
			}

			if (convidados != null) {
				PreparedStatement pstmt = connection.prepareStatement("DELETE " +
						"FROM AGD_AGENDAMENTO_CONVIDADO " +
						"WHERE cd_agendamento = ?");
				pstmt.setInt(1, cdAgendamento);
				pstmt.execute();

				for (int i=0; convidados!=null && i<convidados.size(); i++)
					if (AgendamentoConvidadoDAO.insert(new AgendamentoConvidado(0 /*cdConvidado*/,
							cdAgendamento,
							convidados.get(i).get("nmConvidado"),
							convidados.get(i).get("nmEmail"),
							0 /*lgPresenca*/), connection) <= 0) {
						if (isConnectionNull)
							Conexao.rollback(connection);
						return new Result(-1, "Erros reportados ao atualizar relação de convidados da reunião selecionada.");
					}
			}

			if (isConnectionNull)
				connection.commit();

			return new Result(1);
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			if (isConnectionNull)
				Conexao.rollback(connection);
			return new Result(-1, "Erros reportados ao registrar configuração de mailing para a reunião selecionada. Anote " +
					"a mensagem de erro abaixo e entre em contato com o suporte técnico.\n" +
					e.getLocalizedMessage());
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}

	@Deprecated
	public static ResultSetMap getAllParticipantes(int cdAgendamento, int tpParticipante) {
		return getAllParticipantes(cdAgendamento, tpParticipante, null);
	}

	@Deprecated
	public static ResultSetMap getAllParticipantes(int cdAgendamento, int tpParticipante, Connection connection) {
		boolean isConnectionNull = connection==null;
		try {
			connection = isConnectionNull ? Conexao.conectar() : connection;
			PreparedStatement pstmt = tpParticipante==AgendamentoParticipanteServices.TP_TODOS ?
					connection.prepareStatement("SELECT A.cd_participante, A.lg_presenca, B.nm_pessoa AS nm_participante, 0 AS cd_convidado " +
							"FROM agd_agendamento_participante A, grl_pessoa B " +
							"WHERE A.cd_participante = B.cd_pessoa " +
							"  AND A.cd_agendamento = ? " +
							"UNION " +
							"SELECT 0 AS cd_participante, A.lg_presenca, A.nm_convidado AS nm_participante, A.cd_convidado " +
							"FROM agd_agendamento_convidado A " +
							"WHERE A.cd_agendamento = ?") :
					tpParticipante==AgendamentoParticipanteServices.TP_CONVIDADO ?
					connection.prepareStatement("SELECT A.nm_convidado AS nm_pessoa, A.nm_email, A.lg_presenca, A.cd_convidado " +
							"FROM AGD_AGENDAMENTO_CONVIDADO A " +
							"WHERE cd_agendamento = ?") :
					connection.prepareStatement("SELECT A.*, B.nm_pessoa, B.nm_email " +
					"FROM agd_agendamento_participante A, grl_pessoa B, agd_agenda_participante C " +
					"WHERE A.cd_participante = B.cd_pessoa " +
					"  AND A.cd_agendamento = ? " +
					"  AND A.cd_agenda = C.cd_agenda " +
					"  AND A.cd_participante = C.cd_participante " +
					(tpParticipante > AgendamentoParticipanteServices.TP_TODOS ? "  AND C.tp_participante = ? " : ""));
			pstmt.setInt(1, cdAgendamento);
			if (tpParticipante > AgendamentoParticipanteServices.TP_TODOS && tpParticipante!=AgendamentoParticipanteServices.TP_CONVIDADO)
				pstmt.setInt(2, tpParticipante);
			else if (tpParticipante == AgendamentoParticipanteServices.TP_TODOS)
				pstmt.setInt(2, cdAgendamento);
			
			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
			pstmt.close();
			
			return rsm;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}

	@Deprecated
	public static final Result setPresencas(int cdAgendamento, ArrayList<HashMap<String, Object>> presencas, String txtAberturaAta) {
		return setPresencas(cdAgendamento, presencas, txtAberturaAta, null);
	}

	@Deprecated
	public static final Result setPresencas(int cdAgendamento, ArrayList<HashMap<String, Object>> presencas,
			String txtAberturaAta, Connection connection) {
		boolean isConnectionNull = connection==null;
		try {
			connection = isConnectionNull ? Conexao.conectar() : connection;
			connection.setAutoCommit(isConnectionNull ? false : connection.getAutoCommit());

			PreparedStatement pstmt = connection.prepareStatement("UPDATE agd_agendamento_participante " +
					"SET lg_presenca = 0 " +
					"WHERE cd_agendamento = ?");
			pstmt.setInt(1, cdAgendamento);
			pstmt.execute();

			pstmt = connection.prepareStatement("UPDATE agd_agendamento_convidado " +
					"SET lg_presenca = 0 " +
					"WHERE cd_agendamento = ?");
			pstmt.setInt(1, cdAgendamento);
			pstmt.execute();

			Agendamento agendamento = AgendamentoDAO.get(cdAgendamento, connection);
			int cdAgenda = agendamento.getCdAgenda();

			for (int i=0; presencas!=null && i<presencas.size(); i++)
				if (presencas.get(i).get("cdParticipante") != null && AgendamentoParticipanteDAO.update(new AgendamentoParticipante(cdAgendamento,
							cdAgenda,
							Integer.parseInt(presencas.get(i).get("cdParticipante").toString()) /*cdParticipante*/,
							1 /*lgPresencao*/), connection) <= 0) {
						if (isConnectionNull)
							Conexao.rollback(connection);
						return new Result(-1, "Erros reportados ao registrar presença de participante de reunião.");
					}
				else if (presencas.get(i).get("cdConvidado") != null) {
					AgendamentoConvidado convidado = AgendamentoConvidadoDAO.get(Integer.parseInt(presencas.get(i).get("cdConvidado").toString()),
							cdAgendamento, connection);
					convidado.setLgPresenca(1);
					if (AgendamentoConvidadoDAO.update(convidado, connection) <= 0) {
						if (isConnectionNull)
							Conexao.rollback(connection);
						return new Result(-1, "Erros reportados ao registrar presença de convidado de reunião.");
					}
				}

			Agenda agenda = AgendaDAO.get(cdAgenda, connection);
			Documento documento = DocumentoDAO.get(agendamento.getCdDocumento(), connection);
			documento.setTxtDocumento(txtAberturaAta!=null && !txtAberturaAta.trim().equals("") ? txtAberturaAta :
				"Pela presente iniciamos a ata de nº " + documento.getNrDocumento() +
					" no dia " + Util.formatDate(agendamento.getDtInicial(), "dd/MM/yyyy") + " às " +
					Util.formatDate(agendamento.getDtInicial(), "HH:mm") + ".\n" +
					agenda.getTxtAberturaAta());
			if (DocumentoDAO.update(documento, connection) <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connection);
				return new Result(-1, "Erros reportados ao registrar ata de abertura de reunião. Entre em contato com o suporte técnico.");
			}

			pstmt = connection.prepareStatement("DELETE " +
					"FROM agd_assunto_posicionamento " +
					"WHERE cd_agendamento = ? " +
					"  AND (cd_participante IN (SELECT cd_participante " +
					"							FROM agd_agendamento_participante " +
					"							WHERE cd_agendamento = ? " +
					"							  AND lg_presenca = 0) OR " +
					"		cd_convidado IN (SELECT cd_convidado " +
					"						 FROM agd_agendamento_convidado " +
					"						 WHERE cd_agendamento = ? " +
					"						   AND lg_presenca = 0))");
			pstmt.setInt(1, cdAgendamento);
			pstmt.setInt(2, cdAgendamento);
			pstmt.setInt(3, cdAgendamento);
			pstmt.execute();

			if (isConnectionNull)
				connection.commit();

			HashMap<String, String> hash = new HashMap<String, String>();
			hash.put("txtAberturaAta", documento.getTxtDocumento());
			return new Result(1, "Ata registrada com sucesso", "hash", hash);
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			if (isConnectionNull)
				Conexao.rollback(connection);
			return new Result(-1, "Erros reportados ao registrar presença de participantes em reunião. Anote a mensagem de erro que " +
					"se segue e entre em contato com o suporte técnico.\n" +
					e.getLocalizedMessage());
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}

	@Deprecated
	public static String getDefaultTextoAbertura(int cdAgendamento) {
		return getDefaultTextoAbertura(cdAgendamento, null);
	}

	@Deprecated
	public static String getDefaultTextoAbertura(int cdAgendamento, Connection connection) {
		boolean isConnectionNull = connection==null;
		try {
			connection = isConnectionNull ? Conexao.conectar() : connection;

			Agendamento agendamento = AgendamentoDAO.get(cdAgendamento, connection);
			Agenda agenda = agendamento==null || agendamento.getCdAgenda()<=0 ? null : AgendaDAO.get(agendamento.getCdAgenda(), connection);
			Documento documento = agendamento==null || agendamento.getCdDocumento()<=0 ? null :
				DocumentoDAO.get(agendamento.getCdDocumento(), connection);

			return "Pela presente iniciamos a ata de nº " + documento.getNrDocumento() +
			" no dia " + Util.formatDate(agendamento.getDtInicial(), "dd/MM/yyyy") + " às " +
			Util.formatDate(agendamento.getDtInicial(), "HH:mm") + ".\n" +
			agenda.getTxtAberturaAta();
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}

	@Deprecated
	public static int sendEmail(int cdAgendamento) {
		return sendEmail(cdAgendamento, null);
	}

	@Deprecated
	public static int sendEmail(int cdAgendamento, Connection connection) {
		boolean isConnectionNull = connection==null;
		try {
			connection = isConnectionNull ? Conexao.conectar() : connection;

			String nmServidor = ParametroServices.getValorOfParametro("NM_SERVIDOR_SMTP", null, connection);
			String nmLogin = ParametroServices.getValorOfParametro("NM_LOGIN_SERVIDOR_SMTP", null, connection);
			String nmSenha = ParametroServices.getValorOfParametro("NM_SENHA_SERVIDOR_SMTP", null, connection);
			String nmEmailRemetente = ParametroServices.getValorOfParametro("NM_EMAIL_REMETENTE_SMTP", null, connection);

			if (nmServidor!=null && !nmServidor.trim().equals("") && nmLogin!=null && !nmLogin.trim().equals("") &&
				nmSenha!=null && !nmSenha.trim().equals("")) {
				Agendamento agendamento = AgendamentoDAO.get(cdAgendamento, connection);
				String nrAgendamento = agendamento.getIdAgendamento() + "/" + (agendamento.getNrRecorrencia()<=0 ? "1" : agendamento.getNrRecorrencia());
				String nmAgendamento = agendamento.getNmAgendamento();

				StringBuffer buffer = new StringBuffer();
				buffer.append("Reunião: " + nrAgendamento + " - " + nmAgendamento + "\n");
				buffer.append("Data: " + Util.formatDateTime(agendamento.getDtInicial(), "dd/MM/yyyy") + "\n");
				buffer.append("\nAssuntos e Tópicos a serem tratados:\n");
				ResultSetMap rsmAssuntos = getAllAssuntos(cdAgendamento, false, connection);
				int i = 1;
				while (rsmAssuntos!=null && rsmAssuntos.next()) {
					buffer.append(i++ + ". " + rsmAssuntos.getString("nm_assunto") + "\n");
				}
				buffer.append("\nColaboradores:\n");
				ResultSetMap rsmParticipantes = getAllParticipantes(cdAgendamento, AgendaParticipanteServices.TP_COLABORADOR, connection);
				i = 1;
				while (rsmParticipantes!=null && rsmParticipantes.next()) {
					buffer.append(i++ + ". " + rsmParticipantes.getString("nm_pessoa") + "\n");
				}
				buffer.append("\nClientes:\n");
				rsmParticipantes = getAllParticipantes(cdAgendamento, AgendaParticipanteServices.TP_CLIENTE, connection);
				i = 1;
				while (rsmParticipantes!=null && rsmParticipantes.next()) {
					buffer.append(i++ + ". " + rsmParticipantes.getString("nm_pessoa") + "\n");
				}
				buffer.append("\nConvidados:\n");
				rsmParticipantes = getAllParticipantes(cdAgendamento, AgendaParticipanteServices.TP_CONVIDADO, connection);
				i = 1;
				while (rsmParticipantes!=null && rsmParticipantes.next()) {
					buffer.append(i++ + ". " + rsmParticipantes.getString("nm_pessoa") + "\n");
				}

				ResultSetMap rsm = AgendamentoServices.getAllParticipantes(cdAgendamento, AgendaParticipanteServices.TP_TODOS, connection);
				while (rsm!=null && rsm.next()) {
					if (rsm.getString("NM_EMAIL")!=null && !rsm.getString("NM_EMAIL").trim().equals("")) {
						if (rsm.getString("NM_EMAIL").equalsIgnoreCase("alexandro@solsolucoes.com.br")) {
							SendMail mail = new SendMail(nmServidor, nmLogin, nmSenha);
							mail.send(nmEmailRemetente, rsm.getString("NM_EMAIL").trim(), "Reunião: " + nrAgendamento + " - " + nmAgendamento,
									buffer.toString(), "text/plain", "ISO8859-1");
						}
					}
				}
			}

			return 1;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			if (isConnectionNull)
				Conexao.rollback(connection);
			return -1;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}
	
	//XXX: BRAND_NEW %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%|
	public static Result save(Agendamento agendamento){
		return save(agendamento, null, null, null, null, null);
	}

	public static Result save(Agendamento agendamento, AuthData authData){
		return save(agendamento, null, null, null, authData, null);
	}
	
	public static Result save(Agendamento agendamento, ArrayList<HashMap<String, Object>> listParticipantes, ArrayList<HashMap<String, Object>> listEquipamentos, ResultSetMap rsmOcorrências, 
			AuthData authData) {
		return save(agendamento, listParticipantes, listEquipamentos, rsmOcorrências, authData, null);
	}

	public static Result save(Agendamento agendamento, ArrayList<HashMap<String, Object>> listParticipantes, ArrayList<HashMap<String, Object>> listEquipamentos, ResultSetMap rsmOcorrências, 
			AuthData authData, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			if(agendamento==null)
				return new Result(-1, "Erro ao salvar. Agendamento é nulo");

			int retorno;
			
			// AGENDAMENTO ====================================================|
			if(agendamento.getCdAgendamento()==0){
				retorno = AgendamentoDAO.insert(agendamento, connect);
				agendamento.setCdAgendamento(retorno);
			} else {
				retorno = AgendamentoDAO.update(agendamento, connect);
			}
			
			if(retorno <= 0) {
				if(isConnectionNull)
					connect.rollback();
				return new Result(retorno, "Erro ao lançar agendamento.");
			}
						
			// PARTICIPANTES ==================================================|
			if(listParticipantes!=null && listParticipantes.size()>0) {
				for(HashMap<String, Object> reg : listParticipantes) {	
					AgendamentoParticipante part = new AgendamentoParticipante();					
					part.setCdAgendamento(agendamento.getCdAgendamento());
					part.setCdAgenda(agendamento.getCdAgenda());
					part.setLgPresenca(1);					
					part.setCdParticipante((int)reg.get("CD_PESSOA"));
					
					Result r = AgendamentoParticipanteServices.save(part, authData, connect);
					if(r.getCode() < 0) {
						if(isConnectionNull)
							connect.rollback();
						return r;
					}
				}
			}
			
			// EMPRESTIMOS ====================================================|
			if(listEquipamentos!=null && listEquipamentos.size()>0) {
				for(HashMap<String, Object> reg : listEquipamentos) {
					EquipamentoPessoa emprestimo = new EquipamentoPessoa(0,
							(int)reg.get("CD_EQUIPAMENTO"), 
							(int)reg.get("CD_PESSOA"), 
							Util.convStringToCalendar((String)reg.get("DT_INICIAL")), 
							Util.convStringToCalendar((String)reg.get("DT_FINAL")),
							(int)reg.get("ST_EMPRESTIMO"), 
							(String)reg.get("TXT_OBSERVACAO"),
							agendamento.getCdAgendamento());
					
					Result r = EquipamentoPessoaServices.save(emprestimo, authData, connect);
					if(r.getCode() < 0) {
						if(isConnectionNull)
							connect.rollback();
						return r;
					}
				}
			}

			// TODO: OCORRÊNCIAS ==============================================|			
			
			if (isConnectionNull)
				connect.commit();

			return new Result(retorno, (retorno<=0)?"Erro ao salvar...":"Salvo com sucesso...", "AGENDAMENTO", agendamento);
		}
		catch(Exception e){
			e.printStackTrace();
			if (isConnectionNull)
				Conexao.rollback(connect);
			return new Result(-1, e.getMessage());
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	public static Result remove(Agendamento agendamento) {
		return remove(agendamento.getCdAgendamento());
	}
	public static Result remove(int cdAgendamento){
		return remove(cdAgendamento, false, null, null);
	}
	public static Result remove(int cdAgendamento, boolean cascade){
		return remove(cdAgendamento, cascade, null, null);
	}
	public static Result remove(int cdAgendamento, boolean cascade, AuthData authData){
		return remove(cdAgendamento, cascade, authData, null);
	}
	public static Result remove(int cdAgendamento, boolean cascade, AuthData authData, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			int retorno = 0;
			if(cascade){
				/** SE HOUVER, REMOVER TABELAS ASSOCIADAS **/
				retorno = 1;
			}
			if(!cascade || retorno>0)
			retorno = AgendamentoDAO.delete(cdAgendamento, connect);
			if(retorno<=0){
				Conexao.rollback(connect);
				return new Result(-2, "Este registro está vinculado a outros e não pode ser excluído!");
			}
			else if (isConnectionNull)
				connect.commit();
			return new Result(1, "Registro excluído com sucesso!");
		}
		catch(Exception e){
			e.printStackTrace();
			if (isConnectionNull)
				Conexao.rollback(connect);
			return new Result(-1, "Erro ao excluir registro!");
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ResultSetMap getAll() {
		return getAll(null);
	}

	public static ResultSetMap getAll(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM agd_agendamento");
				return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AgendamentoServices.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! AgendamentoServices.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ResultSetMap find(ArrayList<ItemComparator> criterios) {
		return find(criterios, null);
	}

	public static ResultSetMap find(ArrayList<ItemComparator> criterios, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ResultSetMap rsm = Search.find(
					" SELECT A.*,"
				  + " B.nm_tipo_agendamento"
				  + " FROM agd_agendamento A"
				  + " JOIN agd_tipo_agendamento B ON (A.cd_tipo_agendamento = B.cd_tipo_agendamento)"
				  + "", 
				  " ORDER BY A.dt_final, A.st_agendamento ",
				  criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
			ArrayList<ItemComparator> crt = null;
			while(rsm.next()) {
				rsm.setValueToField("nm_st_agendamento", situacao[rsm.getInt("st_agendamento")].toUpperCase());
			}
			rsm.beforeFirst();
			return rsm;
		} catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! AgendamentoServices.getAll: " + e);
			return null;
		} finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static ResultSetMap loadPessoaEquipe(ArrayList<ItemComparator> criterios) {
		return loadPessoaEquipe(criterios, null);
	}

	public static ResultSetMap loadPessoaEquipe(ArrayList<ItemComparator> criterios, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			
			// extrai critério de busca
			String nmPessoa = null;
			for (int i=0; criterios!=null && i<criterios.size(); i++) {
				if (criterios.get(i).getColumn().equalsIgnoreCase("NM_PESSOA")) {
					nmPessoa = criterios.get(i).getValue().toString().trim();
					criterios.remove(i);
					i--;
				}
			}
			
			// monta critério personalisado
			String addFilter = null;
			if(nmPessoa!=null && nmPessoa.trim()!=null) {
				addFilter = " AND ("
						+ "		A.nm_pessoa iLike '%"+nmPessoa.trim()+"%' "
						+ " 		OR"
						+ "		C.nm_grupo iLike '%"+nmPessoa.trim()+"%' "
						+ ")";
			}
			
			// executa a consulta
			pstmt = connect.prepareStatement(
					" SELECT A.cd_pessoa, A.nm_pessoa, "
					+ " C.cd_grupo, C.nm_grupo"
					+ " FROM grl_pessoa A"
					+ " LEFT OUTER JOIN grl_grupo_pessoa B ON (A.cd_pessoa = B.cd_pessoa)"
					+ " LEFT OUTER JOIN grl_grupo 		 C ON (B.cd_grupo = C.cd_grupo)"
					+ " WHERE 1=1 "
					+ (addFilter!=null ? addFilter : "")
					+ " LIMIT 50 ");
			
			return new ResultSetMap(pstmt.executeQuery());
			
		} catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! AgendamentoServices.loadPessoaEquipe: " + e);
			return null;
		} finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static ResultSetMap getOcorrencias(int cdAgendamento) {
		return getOcorrencias(cdAgendamento, null);
	}

	public static ResultSetMap getOcorrencias(int cdAgendamento, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<ItemComparator> crt = new ArrayList<>();
			crt.add(new ItemComparator("A.cd_agendamento", Integer.toString(cdAgendamento), ItemComparator.EQUAL, Types.INTEGER));
			
			ResultSetMap rsm = AgendamentoOcorrenciaServices.find(crt, connect);
						
			return rsm;
		} catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! AgendamentoServices.getOcorrencias: " + e);
			return null;
		} finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	/**
	 * @category mobilidade/operação 
	 */
	public static Result finalizar(int cdAgendamento) {
		return finalizar(cdAgendamento, null, null);
	}
	
	public static Result finalizar(int cdAgendamento, AuthData auth) {
		return finalizar(cdAgendamento, null, null);
	}
	
	public static Result finalizar(int cdAgendamento, AuthData auth, Connection connect) {
		boolean isConnectionNull = connect == null;
		try {
			if(isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			PreparedStatement ps = null;
			
			Agendamento agendamento = AgendamentoDAO.get(cdAgendamento, connect);
			if(agendamento==null) {
				if(isConnectionNull)
					Conexao.rollback(connect);
				return new Result(-1, "Erro ao finalizar. Item não encontrado.");
			}
			
			// devolver todos os equipamentos
//			ps = connect.prepareStatement("UPDATE grl_equipamento_pessoa SET st_emprestimo=? WHERE cd_agendamento=?");
//			ps.setInt(1, EquipamentoPessoaServices.ST_CONCLUIDO);
//			ps.setInt(2, cdAgendamento);
//			if(ps.executeUpdate() < 0) {
//				if(isConnectionNull)
//					Conexao.rollback(connect);
//				return new Result(-2, "Erro ao finalizar. Devolução de equipamentos não realizada.");
//			}			
			
			// desativar o agendamento
			agendamento.setStAgendamento(ST_CONCLUIDO); 
			if(AgendamentoDAO.update(agendamento) < 0) {
				if(isConnectionNull)
					Conexao.rollback(connect);
				return new Result(-3, "Erro ao finalizar item.");
			}
						
			// TODO: lançar ocorrencia de finalizado
			
			if(isConnectionNull)
				connect.commit();
			
			return new Result(1, "Item finalizado com sucesso!");
		}
		catch(Exception e) {
			System.out.println("Erro! AgendamentoServices.finalizar. "+e.getMessage());
			e.printStackTrace(System.out);
			if(isConnectionNull) {
				Conexao.rollback(connect);
			}
			return null;
		}
		finally {
			if(isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
}