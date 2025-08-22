package com.tivic.manager.agd;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;

import com.tivic.sol.connection.Conexao;
import com.tivic.manager.crm.MailingConta;
import com.tivic.manager.crm.MailingContaDAO;
import com.tivic.manager.grl.Parametro;
import com.tivic.manager.grl.ParametroServices;
import com.tivic.manager.grl.ParametroValor;
import com.tivic.manager.seg.Usuario;
import com.tivic.manager.seg.UsuarioDAO;
import com.tivic.manager.util.Util;

import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;
import sol.dao.Search;
import sol.util.Result;

public class AgendaServices {

	public static Result saveMailingContaForConvites(MailingConta mailingConta) {
		return saveMailingContaForConvites(mailingConta, null);
	}

	public static Result saveMailingContaForConvites(MailingConta mailingConta, Connection connection) {
		boolean isConnectionNull = connection==null;
		try {
			connection = isConnectionNull ? Conexao.conectar() : connection;
			connection.setAutoCommit(isConnectionNull ? false : connection.getAutoCommit());

			int cdConta = mailingConta.getCdConta();
			if (cdConta<=0 && (cdConta = MailingContaDAO.insert(mailingConta, connection)) <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connection);
				return new Result(-1, "Erros reportados ao incluir registro de conta de mailing para envio de convites aos participantes " +
						"de reuniões agendadas. Entre em contato com o suporte técnico.");
			}
			else if (MailingContaDAO.update(mailingConta, connection) <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connection);
				return new Result(-1, "Erros reportados ao atualizar registro de conta de mailing para envio de convites aos participantes " +
						"de reuniões agendadas. Entre em contato com o suporte técnico.");
			}

			Parametro param = ParametroServices.getByName("CD_MAILING_CONTA_ENVIO_CONVITE_REUNIAO", connection);
			if (param==null) {
				if (isConnectionNull)
					Conexao.rollback(connection);
				return new Result(-1, "Não é possível prosseguir com o registro de conta de mailing para envio de convites, devido à não " +
						"identificação de parâmetro relacionado. Certifique-se de que o parâmetro esteja registrado " +
						"ou entre em contato com o suporte técnico.");
			}

			if (ParametroServices.getValorOfParametroAsInteger("CD_MAILING_CONTA_ENVIO_CONVITE_REUNIAO", 0 /*defaultValue*/,
					0 /*cdEmpresa*/, connection) != cdConta) {
				ArrayList<ParametroValor> values = new ArrayList<ParametroValor>();
				values.add(new ParametroValor(param.getCdParametro(),
						0 /*cdValor*/,
						0 /*cdOpcao*/,
						0 /*cdEmpresa*/,
						0 /*cdPessoa*/,
						null /*blbValor*/,
						Integer.toString(cdConta) /*vlInicial*/,
						"" /*vlFinal*/));
				if (ParametroServices.setValoresOfParametro(param.getCdParametro(), 0 /*cdEmpresa*/, 0 /*cdPessoa*/, values, connection) <= 0) {
					if (isConnectionNull)
						Conexao.rollback(connection);
					return new Result(-1, "Erros reportados ao configurar parâmetro de conta de mailing para envio de convites aos participantes " +
					"de reuniões agendadas. Entre em contato com o suporte técnico.");
				}
			}


			if (isConnectionNull)
				connection.commit();

			return new Result(1);
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			return new Result(-1, "Erros reportados ao registrar ou atualizar conta de mailing para envio de convites aos participantes " +
					"de reuniões agendadas. Anote a mensagem de erro e entre em contato com o suporte técnico.\n" +
					e.getLocalizedMessage());
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}

	public static Result saveMailingContaForAtasReunioes(MailingConta mailingConta) {
		return saveMailingContaForAtasReunioes(mailingConta, null);
	}

	public static Result saveMailingContaForAtasReunioes(MailingConta mailingConta, Connection connection) {
		boolean isConnectionNull = connection==null;
		try {
			connection = isConnectionNull ? Conexao.conectar() : connection;
			connection.setAutoCommit(isConnectionNull ? false : connection.getAutoCommit());

			int cdConta = mailingConta.getCdConta();
			if (cdConta<=0 && (cdConta = MailingContaDAO.insert(mailingConta, connection)) <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connection);
				return new Result(-1, "Erros reportados ao incluir registro de conta de mailing para envio de atas de reuniões aos participantes. " +
						"Entre em contato com o suporte técnico.");
			}
			else if (MailingContaDAO.update(mailingConta, connection) <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connection);
				return new Result(-1, "Erros reportados ao atualizar registro de conta de mailing para envio de atas de reuniões aos participantes. " +
						"Entre em contato com o suporte técnico.");
			}

			Parametro param = ParametroServices.getByName("CD_MAILING_CONTA_ENVIO_ATA_REUNIAO", connection);
			if (param==null) {
				if (isConnectionNull)
					Conexao.rollback(connection);
				return new Result(-1, "Não é possível prosseguir com o registro de conta de mailing para envio de atas de reuniões, devido à não " +
						"identificação de parâmetro relacionado. Certifique-se de que o parâmetro esteja registrado " +
						"ou entre em contato com o suporte técnico.");
			}

			if (ParametroServices.getValorOfParametroAsInteger("CD_MAILING_CONTA_ENVIO_ATA_REUNIAO", 0 /*defaultValue*/,
					0 /*cdEmpresa*/, connection) != cdConta) {
				ArrayList<ParametroValor> values = new ArrayList<ParametroValor>();
				values.add(new ParametroValor(param.getCdParametro(),
						0 /*cdValor*/,
						0 /*cdOpcao*/,
						0 /*cdEmpresa*/,
						0 /*cdPessoa*/,
						null /*blbValor*/,
						Integer.toString(cdConta) /*vlInicial*/,
						"" /*vlFinal*/));
				if (ParametroServices.setValoresOfParametro(param.getCdParametro(), 0 /*cdEmpresa*/, 0 /*cdPessoa*/, values, connection) <= 0) {
					if (isConnectionNull)
						Conexao.rollback(connection);
					return new Result(-1, "Erros reportados ao configurar parâmetro de conta de mailing para envio de atas de reuniões aos participantes. " +
							"Entre em contato com o suporte técnico.");
				}
			}


			if (isConnectionNull)
				connection.commit();

			return new Result(1);
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			return new Result(-1, "Erros reportados ao registrar ou atualizar conta de mailing para envio de convites aos participantes " +
					"de reuniões agendadas. Anote a mensagem de erro e entre em contato com o suporte técnico.\n" +
					e.getLocalizedMessage());
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}

	public static MailingConta getMailingContaForAtasReunioes() {
		return getMailingContaForAtasReunioes(null);
	}

	public static MailingConta getMailingContaForAtasReunioes(Connection connection) {
		boolean isConnectionNull = connection==null;
		try {
			connection = isConnectionNull ? Conexao.conectar() : connection;
			int cdMailingConta = ParametroServices.getValorOfParametroAsInteger("CD_MAILING_CONTA_ENVIO_ATA_REUNIAO",
					0 /*defaultValue*/, 0 /*cdEmpresa*/, connection);
			return cdMailingConta<=0 ? null : MailingContaDAO.get(cdMailingConta, connection);
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

	public static MailingConta getMailingContaForConvites() {
		return getMailingContaForConvites(null);
	}

	public static MailingConta getMailingContaForConvites(Connection connection) {
		boolean isConnectionNull = connection==null;
		try {
			connection = isConnectionNull ? Conexao.conectar() : connection;
			int cdMailingConta = ParametroServices.getValorOfParametroAsInteger("CD_MAILING_CONTA_ENVIO_CONVITE_REUNIAO",
					0 /*defaultValue*/, 0 /*cdEmpresa*/, connection);
			return cdMailingConta<=0 ? null : MailingContaDAO.get(cdMailingConta, connection);
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

	public static ResultSetMap getAllAssuntosDisponiveis(int cdAgenda, int cdAgendamento) {
		return getAllAssuntosDisponiveis(cdAgenda, cdAgendamento, null);
	}

	public static ResultSetMap getAllAssuntosDisponiveis(int cdAgenda, int cdAgendamento, Connection connection) {
		boolean isConnectionNull = connection==null;
		try {
			connection = isConnectionNull ? Conexao.conectar() : connection;
			Agendamento agendamento = cdAgenda>0 ? null : AgendamentoDAO.get(cdAgendamento, connection);
			cdAgenda = cdAgenda>0 ? cdAgenda : agendamento.getCdAgenda();
			PreparedStatement pstmt = connection.prepareStatement("SELECT * " +
					"FROM agd_assunto A " +
					"WHERE cd_agenda = ? " +
					"  AND (A.tp_assunto = ? OR " +
					"		NOT EXISTS (SELECT cd_assunto " +
					"					FROM agd_agendamento_assunto " +
					"					WHERE cd_assunto = A.cd_assunto " +
					"				  	  AND st_final = ?)) " +
					(cdAgendamento<=0 ? "" : "  AND NOT A.cd_assunto IN (SELECT cd_assunto " +
					"							FROM agd_agendamento_assunto " +
					"							WHERE cd_agendamento = ?) "));
			pstmt.setInt(1, cdAgenda);
			pstmt.setInt(2, AssuntoServices.TP_REPETITIVO);
			pstmt.setInt(3, AgendamentoAssuntoServices.ST_FECHADO);
			if (cdAgendamento>0)
				pstmt.setInt(4, cdAgendamento);
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

	public static Result getNextDateAgendamento(int cdAgenda) {
		return getNextDateAgendamento(cdAgenda, null);
	}

	public static Result getNextDateAgendamento(int cdAgenda, Connection connection) {
		boolean isConnectionNull = connection==null;
		try {
			connection = isConnectionNull ? Conexao.conectar() : connection;

			PreparedStatement pstmt = connection.prepareStatement("SELECT * " +
					"FROM agd_recorrencia " +
					"WHERE cd_agenda = ?");
			pstmt.setInt(1, cdAgenda);
			ResultSet rs = pstmt.executeQuery();

			if (!rs.next() || rs.getInt("tp_recorrencia")==RecorrenciaServices.TP_NENHUMA_REC)
				return new Result(-1, "Nenhum padrão de recorrência identificado para o grupo.");
			else {
				Recorrencia recorrencia = RecorrenciaDAO.get(rs.getInt("cd_recorrencia"), connection);

				GregorianCalendar dtInicialTemp = (GregorianCalendar)recorrencia.getDtInicio().clone();
				dtInicialTemp.set(Calendar.HOUR_OF_DAY, 0);
				dtInicialTemp.set(Calendar.MINUTE, 0);
				dtInicialTemp.set(Calendar.SECOND, 0);
				dtInicialTemp.set(Calendar.MILLISECOND, 0);
				GregorianCalendar dtTemp = null;

				pstmt = connection.prepareStatement("SELECT MAX(dt_inicial) AS dt_last " +
						"FROM agd_agendamento " +
						"WHERE cd_agenda = ? " +
						"  AND cd_tipo_agendamento = ?");
				pstmt.setInt(1, cdAgenda);
				pstmt.setInt(2, ParametroServices.getValorOfParametroAsInteger("CD_TIPO_AGENDAMENTO_REUNIAO_ORD", 0, 0, connection));
				ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
				GregorianCalendar dtUltimoAgendamento = !rsm.next() ? null : rsm.getGregorianCalendar("dt_last");
				if (dtUltimoAgendamento != null) {
					dtUltimoAgendamento.set(Calendar.HOUR_OF_DAY, 0);
					dtUltimoAgendamento.set(Calendar.MINUTE, 0);
					dtUltimoAgendamento.set(Calendar.SECOND, 0);
					dtUltimoAgendamento.set(Calendar.MILLISECOND, 0);
					if (dtUltimoAgendamento.after(dtInicialTemp)) {
						switch(recorrencia.getTpRecorrencia()) {
							case RecorrenciaServices.TP_REC_DIARIA:
								dtUltimoAgendamento.add(Calendar.DAY_OF_MONTH,
										recorrencia.getTpEspecificidadeRecorrencia()==RecorrenciaServices.TP_REC_DIARIA_CADA_N_DIAS ?
												recorrencia.getQtIntervaloRecorrencia() : 1);
								break;
							case RecorrenciaServices.TP_REC_SEMANAL:
								if (recorrencia.getTpEspecificidadeRecorrencia()==RecorrenciaServices.TP_REC_SEMANAL_CADA_N_SEMANAS &&
										recorrencia.getQtIntervaloRecorrencia()>1 && dtUltimoAgendamento.get(Calendar.DAY_OF_WEEK)==Calendar.SATURDAY) {
									dtUltimoAgendamento.add(Calendar.DAY_OF_MONTH, 1 + ((recorrencia.getQtIntervaloRecorrencia()-1) * 7));
								}
								else
									dtUltimoAgendamento.add(Calendar.DAY_OF_MONTH, 1);
								break;
							case RecorrenciaServices.TP_REC_MENSAL:
								for (int j=0; j<recorrencia.getQtIntervaloRecorrencia(); j++)
									dtUltimoAgendamento = new GregorianCalendar(dtUltimoAgendamento.get(Calendar.YEAR) + (dtUltimoAgendamento.get(Calendar.MONTH)==Calendar.DECEMBER ? 1 : 0),
											(dtUltimoAgendamento.get(Calendar.MONTH) + 1) % 12, 1);
								break;
							case RecorrenciaServices.TP_REC_ANUAL:
								dtUltimoAgendamento = new GregorianCalendar(dtUltimoAgendamento.get(Calendar.YEAR)+1, Calendar.JANUARY, 1);
								break;
						}
						dtUltimoAgendamento.set(Calendar.HOUR_OF_DAY, 0);
						dtUltimoAgendamento.set(Calendar.MINUTE, 0);
						dtUltimoAgendamento.set(Calendar.SECOND, 0);
						dtUltimoAgendamento.set(Calendar.MILLISECOND, 0);
						dtInicialTemp = dtUltimoAgendamento;
					}
				}

				GregorianCalendar dtAtual = new GregorianCalendar();
				dtAtual.set(Calendar.HOUR_OF_DAY, 0);
				dtAtual.set(Calendar.MINUTE, 0);
				dtAtual.set(Calendar.SECOND, 0);
				dtAtual.set(Calendar.MILLISECOND, 0);
				while (dtTemp==null || dtTemp.before(dtAtual)) {
					switch(recorrencia.getTpRecorrencia()) {
						case RecorrenciaServices.TP_REC_DIARIA:
							switch(recorrencia.getTpEspecificidadeRecorrencia()) {
								case RecorrenciaServices.TP_REC_DIARIA_CADA_N_DIAS:
								case RecorrenciaServices.TP_REC_DIARIA_TODOS_DIAS:
									dtTemp = (GregorianCalendar)dtInicialTemp.clone();
									break;
								case RecorrenciaServices.TP_REC_DIARIA_TODOS_DIAS_UTEIS:
									dtTemp = dtInicialTemp.get(Calendar.DAY_OF_WEEK)==Calendar.SATURDAY || dtInicialTemp.get(Calendar.DAY_OF_WEEK)==Calendar.SUNDAY ? null :
										(GregorianCalendar)dtInicialTemp.clone();
									break;
								case RecorrenciaServices.TP_REC_DIARIA_TODOS_DIAS_NAO_UTEIS:
									dtTemp = !(dtInicialTemp.get(Calendar.DAY_OF_WEEK)==Calendar.SATURDAY || dtInicialTemp.get(Calendar.DAY_OF_WEEK)==Calendar.SUNDAY) ? null :
										(GregorianCalendar)dtInicialTemp.clone();
									break;
							}
							int qtDiasIntervalo = recorrencia.getTpEspecificidadeRecorrencia()==RecorrenciaServices.TP_REC_DIARIA_CADA_N_DIAS &&
													recorrencia.getQtIntervaloRecorrencia()>0 ? recorrencia.getQtIntervaloRecorrencia() : 1;
							dtInicialTemp.add(Calendar.DAY_OF_MONTH, qtDiasIntervalo);
							break;
						/* recorrencia semanal */
						case RecorrenciaServices.TP_REC_SEMANAL:
							switch(recorrencia.getTpEspecificidadeRecorrencia()) {
								case RecorrenciaServices.TP_REC_SEMANAL_TODOS_DIAS:
									dtTemp = (GregorianCalendar)dtInicialTemp.clone();
									break;
								case RecorrenciaServices.TP_REC_SEMANAL_CADA_N_SEMANAS:
									if (recorrencia.getLgDomingo()==1 && dtInicialTemp.get(Calendar.DAY_OF_WEEK)==Calendar.SUNDAY)
										dtTemp = (GregorianCalendar)dtInicialTemp.clone();
									if (recorrencia.getLgSegunda()==1 && dtInicialTemp.get(Calendar.DAY_OF_WEEK)==Calendar.MONDAY)
										dtTemp = (GregorianCalendar)dtInicialTemp.clone();
									if (recorrencia.getLgTerca()==1 && dtInicialTemp.get(Calendar.DAY_OF_WEEK)==Calendar.TUESDAY)
										dtTemp = (GregorianCalendar)dtInicialTemp.clone();
									if (recorrencia.getLgQuarta()==1 && dtInicialTemp.get(Calendar.DAY_OF_WEEK)==Calendar.WEDNESDAY)
										dtTemp = (GregorianCalendar)dtInicialTemp.clone();
									if (recorrencia.getLgQuinta()==1 && dtInicialTemp.get(Calendar.DAY_OF_WEEK)==Calendar.THURSDAY)
										dtTemp = (GregorianCalendar)dtInicialTemp.clone();
									if (recorrencia.getLgSexta()==1 && dtInicialTemp.get(Calendar.DAY_OF_WEEK)==Calendar.FRIDAY)
										dtTemp = (GregorianCalendar)dtInicialTemp.clone();
									if (recorrencia.getLgSabado()==1 && dtInicialTemp.get(Calendar.DAY_OF_WEEK)==Calendar.SATURDAY)
										dtTemp = (GregorianCalendar)dtInicialTemp.clone();
									break;
							}
							qtDiasIntervalo = recorrencia.getTpEspecificidadeRecorrencia()==RecorrenciaServices.TP_REC_SEMANAL_CADA_N_SEMANAS &&
												recorrencia.getQtIntervaloRecorrencia()>0 ? recorrencia.getQtIntervaloRecorrencia() : 1;
							dtInicialTemp.add(Calendar.DAY_OF_MONTH, qtDiasIntervalo);
							break;
						/* recorrencia mensal */
						case RecorrenciaServices.TP_REC_MENSAL:
							switch(recorrencia.getTpEspecificidadeRecorrencia()) {
								case RecorrenciaServices.TP_REC_MENSAL_DIA_ESPECIFICO:
									int nrDia = recorrencia.getNrDiaRecorrenciaMensal()<=Util.getQuantidadeDias(dtInicialTemp.get(Calendar.MONTH), dtInicialTemp.get(Calendar.YEAR)) ?
											recorrencia.getNrDiaRecorrenciaMensal() : Util.getQuantidadeDias(dtInicialTemp.get(Calendar.MONTH), dtInicialTemp.get(Calendar.YEAR));
									dtTemp = new GregorianCalendar(dtInicialTemp.get(Calendar.YEAR), dtInicialTemp.get(Calendar.MONTH), nrDia);
									break;
								case RecorrenciaServices.TP_REC_MENSAL_ENESIMO_DIA:
									dtTemp = RecorrenciaServices.getEnesimoDiaMes(dtInicialTemp.get(Calendar.MONTH), dtInicialTemp.get(Calendar.YEAR),
											recorrencia.getTpOrdemRecorrenciaMensal(), recorrencia.getNrOrdemRecorrenciaMensal());
									break;
							}
							if (dtTemp.before(recorrencia.getDtInicio()))
								dtTemp = null;
							qtDiasIntervalo = recorrencia.getQtIntervaloRecorrencia()>0 ? recorrencia.getQtIntervaloRecorrencia() : 1;
							for (int j=0; j<qtDiasIntervalo; j++)
								dtInicialTemp = new GregorianCalendar(dtInicialTemp.get(Calendar.YEAR) + (dtInicialTemp.get(Calendar.MONTH)==Calendar.DECEMBER ? 1 : 0),
										(dtInicialTemp.get(Calendar.MONTH) + 1) % 12, 1);
							break;
						/* recorrencia anual */
						case RecorrenciaServices.TP_REC_ANUAL:
							switch(recorrencia.getTpEspecificidadeRecorrencia()) {
								case RecorrenciaServices.TP_REC_ANUAL_DIA_ESPECIFICO:
									int nrMes = recorrencia.getNrMesRecorrenciaAnual();
									int nrDia = recorrencia.getNrDiaRecorrenciaAnual()<=Util.getQuantidadeDias(nrMes, dtInicialTemp.get(Calendar.YEAR)) ?
											recorrencia.getNrDiaRecorrenciaAnual() : Util.getQuantidadeDias(nrMes, dtInicialTemp.get(Calendar.YEAR));
									dtTemp = new GregorianCalendar(dtInicialTemp.get(Calendar.YEAR), nrMes, nrDia);
								case RecorrenciaServices.TP_REC_ANUAL_ENESIMO_DIA:
									dtTemp = RecorrenciaServices.getEnesimoDiaMes(recorrencia.getNrMesRecorrenciaAnual(), dtInicialTemp.get(Calendar.YEAR),
											recorrencia.getTpOrdemRecorrenciaAnual(), recorrencia.getNrOrdemRecorrenciaAnual());
							}
							if (dtTemp.before(recorrencia.getDtInicio()))
								dtTemp = null;
							dtInicialTemp = new GregorianCalendar(dtInicialTemp.get(Calendar.YEAR) + 1, Calendar.JANUARY, 1);
							break;
					}
				}

				dtTemp.set(Calendar.HOUR_OF_DAY, recorrencia.getDtInicio().get(Calendar.HOUR_OF_DAY));
				dtTemp.set(Calendar.MINUTE, recorrencia.getDtInicio().get(Calendar.MINUTE));

				return new Result(1, "", "nextDate", dtTemp);
			}
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! AgendaDAO.AgendaServices: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}

	public static ResultSetMap getAll(int cdEmpresa) {
		return getAll(cdEmpresa, false, null);
	}

	public static ResultSetMap getAll(int cdEmpresa, boolean returnParticipantes) {
		return getAll(cdEmpresa, returnParticipantes, null);
	}

	public static ResultSetMap getAll(int cdEmpresa, boolean returnParticipantes, Connection connect) {
		boolean isConnectionNull = connect==null;
		try {
			connect = isConnectionNull ? Conexao.conectar() : connect;
			PreparedStatement pstmt = connect.prepareStatement("SELECT A.*, B.tp_recorrencia, B.tp_especificidade_recorrencia, " +
					"B.lg_domingo, B.lg_segunda, B.lg_terca, B.lg_quarta, B.lg_quinta, B.lg_sexta, B.lg_sabado, " +
					"B.qt_intervalo_recorrencia, B.nr_dia_recorrencia_mensal, B.nr_ordem_recorrencia_mensal, " +
					"B.tp_ordem_recorrencia_mensal, B.nr_dia_recorrencia_anual, B.nr_mes_recorrencia_anual, B.tp_ordem_recorrencia_anual, " +
					"B.nr_ordem_recorrencia_anual, B.dt_inicio  " +
					"FROM agd_agenda A " +
					"LEFT OUTER JOIN agd_recorrencia B ON (A.cd_agenda = B.cd_agenda AND " +
					"									   B.cd_recorrencia = (SELECT MAX(cd_recorrencia) " +
					"														   FROM agd_recorrencia " +
					"														   WHERE cd_agenda = A.cd_agenda)) " +
					"WHERE A.cd_empresa = ? " +
					"ORDER BY A.nm_agenda");
			pstmt.setInt(1, cdEmpresa);
			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
			while (returnParticipantes && rsm!=null && rsm.next()) {
				pstmt = connect.prepareStatement("SELECT A.cd_participante, B.nm_pessoa AS nm_participante " +
						"FROM agd_agenda_participante A , grl_pessoa B " +
						"WHERE A.cd_participante = B.cd_pessoa " +
						"  AND A.st_participante = ? " +
						"  AND A.cd_agenda = ? ");
				pstmt.setInt(1, AgendaParticipanteServices.ST_ATIVO);
				pstmt.setInt(2, rsm.getInt("cd_agenda"));
				rsm.getRegister().put("rsmParticipantes", new ResultSetMap(pstmt.executeQuery()));
			}
			rsm.beforeFirst();
			return rsm;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! AgendaDAO.AgendaServices: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ResultSetMap getAllOfUsuario(int cdEmpresa, int cdUsuario) {
		return getAllOfUsuario(cdEmpresa, cdUsuario, null);
	}

	public static ResultSetMap getAllOfUsuario(int cdEmpresa, int cdUsuario, Connection connect) {
		boolean isConnectionNull = connect==null;
		try {
			connect = isConnectionNull ? Conexao.conectar() : connect;
			Usuario usuario = UsuarioDAO.get(cdUsuario, connect);
			PreparedStatement pstmt = connect.prepareStatement("SELECT A.*, B.tp_recorrencia, B.tp_especificidade_recorrencia, " +
					"B.lg_domingo, B.lg_segunda, B.lg_terca, B.lg_quarta, B.lg_quinta, B.lg_sexta, B.lg_sabado, " +
					"B.qt_intervalo_recorrencia, B.nr_dia_recorrencia_mensal, B.nr_ordem_recorrencia_mensal, " +
					"B.tp_ordem_recorrencia_mensal, B.nr_dia_recorrencia_anual, B.nr_mes_recorrencia_anual, B.tp_ordem_recorrencia_anual, " +
					"B.nr_ordem_recorrencia_anual, B.dt_inicio  " +
					"FROM agd_agenda A " +
					"JOIN agd_agenda_participante C ON (A.cd_agenda = C.cd_agenda AND C.cd_participante = ? AND " +
					"									C.st_participante = ?) " +
					"LEFT OUTER JOIN agd_recorrencia B ON (A.cd_agenda = B.cd_agenda AND " +
					"									   B.cd_recorrencia = (SELECT MAX(cd_recorrencia) " +
					"														   FROM agd_recorrencia " +
					"														   WHERE cd_agenda = A.cd_agenda)) " +
					"WHERE A.cd_empresa = ? " +
					"ORDER BY A.nm_agenda");
			pstmt.setInt(1, usuario.getCdPessoa());
			pstmt.setInt(2, AgendaParticipanteServices.ST_ATIVO);
			pstmt.setInt(3, cdEmpresa);
			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
			return rsm;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! AgendaServices.getAllOfUsuario: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ResultSetMap getAllParticipantes(int cdAgenda, int tpParticipante) {
		return getAllParticipantes(cdAgenda, tpParticipante, null);
	}

	public static ResultSetMap getAllParticipantes(int cdAgenda, int tpParticipante, Connection connection) {
		boolean isConnectionNull = connection==null;
		try {
			connection = isConnectionNull ? Conexao.conectar() : connection;
			PreparedStatement pstmt = connection.prepareStatement("SELECT A.*, B.nm_pessoa " +
					"FROM agd_agenda_participante A, grl_pessoa B " +
					"WHERE A.cd_participante = B.cd_pessoa " +
					"  AND A.cd_agenda = ? " +
					"  AND st_participante = ?" +
					(tpParticipante > AgendaParticipanteServices.TP_TODOS ? "  AND A.tp_participante = ? " : ""));
			pstmt.setInt(1, cdAgenda);
			pstmt.setInt(2, AgendaParticipanteServices.ST_ATIVO);
			if (tpParticipante > AgendaParticipanteServices.TP_TODOS)
				pstmt.setInt(3, tpParticipante);
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

	public static HashMap<String, Object> insert(Agenda objeto, Recorrencia recorrencia) {
		return insert(objeto, recorrencia, null);
	}

	public static HashMap<String, Object> insert(Agenda agenda, Recorrencia recorrencia, Connection connection){
		boolean isConnectionNull = connection==null;
		try {
			connection = isConnectionNull ? Conexao.conectar() : connection;
			connection.setAutoCommit(isConnectionNull ? false : connection.getAutoCommit());

			/* inclusao de agenda */
			int cdAgenda = AgendaDAO.insert(agenda, connection);
			if (cdAgenda <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connection);
				return null;
			}
			agenda.setCdAgenda(cdAgenda);
			recorrencia.setCdAgenda(cdAgenda);

			int cdRecorrencia = 0;
			if ((cdRecorrencia = RecorrenciaDAO.insert(recorrencia, connection)) <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connection);
				return null;
			}
			recorrencia.setCdRecorrencia(cdRecorrencia);

			if (isConnectionNull)
				connection.commit();

			HashMap<String, Object> hash = new HashMap<String, Object>();
			hash.put("agenda", agenda);
			hash.put("recorrencia", recorrencia);
			return  hash;
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! AgendaServices.insert: " +  e);
			if (isConnectionNull)
				Conexao.rollback(connection);
			return null;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}

	public static HashMap<String, Object> update(Agenda objeto, Recorrencia recorrencia) {
		return update(objeto, recorrencia, null);
	}

	public static HashMap<String, Object> update(Agenda agenda, Recorrencia recorrencia, Connection connection){
		boolean isConnectionNull = connection==null;
		try {
			connection = isConnectionNull ? Conexao.conectar() : connection;
			connection.setAutoCommit(isConnectionNull ? false : connection.getAutoCommit());

			/* atualizacao de agenda */
			if (AgendaDAO.update(agenda, connection) <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connection);
				return null;
			}

			if (RecorrenciaDAO.update(recorrencia, connection) <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connection);
				return null;
			}

			if (isConnectionNull)
				connection.commit();

			HashMap<String, Object> hash = new HashMap<String, Object>();
			hash.put("agenda", agenda);
			hash.put("recorrencia", recorrencia);
			return  hash;
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! AgendaServices.insert: " +  e);
			if (isConnectionNull)
				Conexao.rollback(connection);
			return null;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}

	public static ResultSetMap find(ArrayList<ItemComparator> criterios) {
		return find(criterios, null);
	}

	public static ResultSetMap find(ArrayList<ItemComparator> criterios, Connection connect) {
		return Search.find("SELECT A.*, B.cd_recorrencia, B.dt_inicio, B.dt_termino, B.tp_termino, B.nr_recorrencias, " +
				"B.tp_recorrencia, B.tp_especificidade_recorrencia, B.lg_domingo, B.lg_segunda, B.lg_terca, B.lg_quarta, " +
				"B.lg_quinta, B.lg_sexta, B.lg_sabado, B.qt_intervalo_recorrencia, B.nr_dia_recorrencia_mensal, " +
				"B.nr_ordem_recorrencia_mensal, B.tp_ordem_recorrencia_mensal, B.nr_dia_recorrencia_anual, B.nr_mes_recorrencia_anual, " +
				"B.tp_ordem_recorrencia_anual, B.nr_ordem_recorrencia_anual " +
				"FROM agd_agenda A " +
				"LEFT OUTER JOIN agd_recorrencia B ON (A.cd_agenda = B.cd_agenda AND" +
				"									   B.cd_recorrencia = (SELECT MIN(C.cd_recorrencia) " +
				"														   FROM agd_recorrencia C " +
				"														   WHERE C.cd_agenda = A.cd_agenda)) " +
				"WHERE 1 = 1",
				"", criterios, null, connect!=null ? connect : Conexao.conectar(), connect==null, false, false);
	}

	public static int delete(int cdAgenda) {
		return delete(cdAgenda, null);
	}

	public static int delete(int cdAgenda, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			connect = isConnectionNull ? Conexao.conectar() : connect;
			connect.setAutoCommit(isConnectionNull ? false : connect.getAutoCommit());

			PreparedStatement pstmt = connect.prepareStatement("DELETE " +
					"FROM agd_recorrencia " +
					"WHERE cd_agenda=?");
			pstmt.setInt(1, cdAgenda);
			pstmt.execute();

			pstmt = connect.prepareStatement("DELETE " +
					"FROM agd_agenda_participante " +
					"WHERE cd_agenda=?");
			pstmt.setInt(1, cdAgenda);
			pstmt.execute();

			if (AgendaDAO.delete(cdAgenda, connect) <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}

			if (isConnectionNull)
				connect.commit();

			return 1;
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! AgendaServices.delete: " +  e);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

}
