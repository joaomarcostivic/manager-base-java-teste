package com.tivic.manager.agd;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.GregorianCalendar;

import com.tivic.sol.connection.Conexao;
import com.tivic.manager.grl.ParametroServices;
import com.tivic.manager.util.SendMail;
import com.tivic.manager.util.Util;

import sol.dao.ResultSetMap;

public class OcorrenciaServices {

	public static Ocorrencia insert(Ocorrencia ocorrencia) {
		return insert(ocorrencia, null, 0, null);
	}

	public static Ocorrencia insert(Ocorrencia ocorrencia, GregorianCalendar dtInicial, int cdTipoLembrete) {
		return insert(ocorrencia, dtInicial, cdTipoLembrete, null, false, null);
	}

	public static Ocorrencia insert(Ocorrencia ocorrencia, GregorianCalendar dtInicial, int cdTipoLembrete, GregorianCalendar dtLembrete) {
		return insert(ocorrencia, dtInicial, cdTipoLembrete, dtLembrete, false, null);
	}

	public static Ocorrencia insert(Ocorrencia ocorrencia, GregorianCalendar dtInicial, int cdTipoLembrete, GregorianCalendar dtLembrete,
			boolean verifySendEmail) {
		return insert(ocorrencia, dtInicial, cdTipoLembrete, dtLembrete, verifySendEmail, null);
	}

	public static Ocorrencia insert(Ocorrencia ocorrencia, GregorianCalendar dtInicial, int cdTipoLembrete, GregorianCalendar dtLembrete,
			boolean verifySendEmail, Connection connection) {
		boolean isConnectionNull = connection==null;
		try {
			connection = isConnectionNull ? Conexao.conectar() : connection;
			connection.setAutoCommit(isConnectionNull ? false : connection.getAutoCommit());

			ocorrencia.setDtOcorrencia(new GregorianCalendar());
			int cdOcorrencia = OcorrenciaDAO.insert(ocorrencia, connection);
			if (cdOcorrencia <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connection);
				return null;
			}

			if (ocorrencia.getCdAgendamento() > 0 && dtInicial != null) {
				Agendamento agendamento = AgendamentoDAO.get(ocorrencia.getCdAgendamento(), connection);
				agendamento.setDtInicial(dtInicial);
				agendamento.setDtLembrete(dtLembrete);
				if (AgendamentoServices.update(agendamento, cdTipoLembrete, 0, null, connection) == null) {
					if (isConnectionNull)
						Conexao.rollback(connection);
					return null;
				}
			}

			if (isConnectionNull)
				connection.commit();

			if (verifySendEmail && ocorrencia.getCdAgendamento() > 0) {
				verifySendEmail(ocorrencia, connection);
			}

			ocorrencia.setCdOcorrencia(cdOcorrencia);
			return ocorrencia;
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

	public static int setRepetivel(int cdOcorrencia, int lgRepetivel) {
		return setRepetivel(cdOcorrencia, lgRepetivel, null);
	}

	public static int setRepetivel(int cdOcorrencia, int lgRepetivel, Connection connection) {
		boolean isConnectionNull = connection==null;
		try {
			connection = isConnectionNull ? Conexao.conectar() : connection;

			Ocorrencia ocorrencia = OcorrenciaDAO.get(cdOcorrencia, connection);
			ocorrencia.setLgRepetivel(lgRepetivel);
			return OcorrenciaDAO.update(ocorrencia, connection);
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

	public static Ocorrencia update(Ocorrencia ocorrencia) {
		return update(ocorrencia, null, 0, null);
	}

	public static Ocorrencia update(Ocorrencia ocorrencia, GregorianCalendar dtInicial, int cdTipoLembrete) {
		return update(ocorrencia, dtInicial, cdTipoLembrete, null, null);
	}

	public static Ocorrencia update(Ocorrencia ocorrencia, GregorianCalendar dtInicial, int cdTipoLembrete, GregorianCalendar dtLembrete) {
		return update(ocorrencia, dtInicial, cdTipoLembrete, dtLembrete, null);
	}

	public static Ocorrencia update(Ocorrencia ocorrencia, GregorianCalendar dtInicial, int cdTipoLembrete, GregorianCalendar dtLembrete,
			Connection connection) {
		boolean isConnectionNull = connection==null;
		try {
			if (ocorrencia.getDtOcorrencia()==null)
				return null;

			connection = isConnectionNull ? Conexao.conectar() : connection;
			connection.setAutoCommit(isConnectionNull ? false : connection.getAutoCommit());

			int cdRetorno = OcorrenciaDAO.update(ocorrencia, connection);

			if (cdRetorno <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connection);
				return null;
			}

			if (ocorrencia.getCdAgendamento() > 0 && dtInicial != null) {
				Agendamento agendamento = AgendamentoDAO.get(ocorrencia.getCdAgendamento(), connection);
				agendamento.setDtInicial(dtInicial);
				agendamento.setDtLembrete(dtLembrete);
				if (AgendamentoServices.update(agendamento, cdTipoLembrete, 0, null, connection) == null) {
					if (isConnectionNull)
						Conexao.rollback(connection);
					return null;
				}
			}

			if (isConnectionNull)
				connection.commit();

			return ocorrencia;
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

	public static int verifySendEmail(Ocorrencia ocorrencia, Connection connection) {
		boolean isConnectionNull = connection==null;
		try {
			if (isConnectionNull) {
				connection = Conexao.conectar();
				connection.setAutoCommit(false);
			}

			int lgEnvio = ParametroServices.getValorOfParametroAsInteger("LG_ENVIO_EMAIL", 0, 0, connection);
			String nmServidor = ParametroServices.getValorOfParametro("NM_SERVIDOR_SMTP", null, connection);
			String nmLogin = ParametroServices.getValorOfParametro("NM_LOGIN_SERVIDOR_SMTP", null, connection);
			String nmSenha = ParametroServices.getValorOfParametro("NM_SENHA_SERVIDOR_SMTP", null, connection);
			String nmEmailRemetente = ParametroServices.getValorOfParametro("NM_EMAIL_REMETENTE_SMTP", null, connection);
			if (lgEnvio == 1 && nmServidor!=null && !nmServidor.trim().equals("") && nmLogin!=null && !nmLogin.trim().equals("") &&
				nmSenha!=null && !nmSenha.trim().equals("")) {
				Agendamento agendamento = AgendamentoDAO.get(ocorrencia.getCdAgendamento(), connection);
				String nrAgendamento = agendamento.getIdAgendamento() + " - " + (agendamento.getNrRecorrencia()<=0 ? "1" : agendamento.getNrRecorrencia());
				String nmAgendamento = agendamento.getNmAgendamento();
				if (agendamento.getCdRecorrencia() > 0) {
					Recorrencia recorrencia = RecorrenciaDAO.get(agendamento.getCdRecorrencia(), connection);
					if (recorrencia.getTpTermino() != RecorrenciaServices.TP_SEM_DATA_TERMINO) {
						PreparedStatement pstmt = connection.prepareStatement("SELECT COUNT(*) AS COUNT " +
								"FROM agd_agendamento " +
								"WHERE cd_recorrencia = ?");
						pstmt.setInt(1, recorrencia.getCdRecorrencia());
						ResultSet rs = pstmt.executeQuery();
						nrAgendamento = rs.next() ? nrAgendamento + "/" + rs.getInt(1) : nrAgendamento;
					}
				}
				ResultSetMap rsm = AgendamentoServices.getAllParticipantes(ocorrencia.getCdAgendamento(), connection);
				while (rsm!=null && rsm.next()) {
					if (rsm.getString("NM_EMAIL")!=null && !rsm.getString("NM_EMAIL").trim().equals("")) {
						SendMail mail = new SendMail(nmServidor, nmLogin, nmSenha);
						StringBuffer buffer = new StringBuffer();
						buffer.append("A seguinte ocorrência foi acrescentada ao agendamento " + nrAgendamento + " - " + nmAgendamento + "\n");
						buffer.append("\n");
						buffer.append("Data/Horário: " + Util.formatDateTime(ocorrencia.getDtOcorrencia(), "dd/MM/yyyy HH:mm:ss") + "\n");
						buffer.append("Conteúdo:\n");
						buffer.append(ocorrencia.getTxtOcorrencia());
						mail.send(nmEmailRemetente, rsm.getString("NM_EMAIL").trim(), "Inclusão de nova ocorrência - Agendamento " + nrAgendamento,
								buffer.toString(), "text/plain", "ISO8859-1");
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

}
