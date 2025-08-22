package com.tivic.manager.agd;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;

import com.tivic.sol.connection.Conexao;
import com.tivic.manager.util.Util;

import sol.dao.ResultSetMap;

public class RecorrenciaServices {

	public static final String[] ordensRecorrencia = {"primeiro(a)",
		"segundo(a)",
		"terceiro(a)",
		"quarto(a)",
		"último(a)"};

	public static final String[] tiposOrdemRecorrencia = {"dia",
		"dia da semana",
		"dia do fim da semana",
		"domingo",
		"segunda-feira(a)",
		"terça-feira",
		"quarta-feira",
		"quinta-feira",
		"sexta-feira",
		"sábado"};

	public static final String[] meses = {"janeiro",
		"fevereiro",
		"março",
		"abril",
		"maio",
		"junho",
		"julho",
		"agosto",
		"setembro",
		"outubro",
		"novembro",
		"dezembro"};

	/* tipos de recorrencias */
	public static final int TP_NENHUMA_REC = -1;
	public static final int TP_REC_DIARIA = 0;
	public static final int TP_REC_SEMANAL = 1;
	public static final int TP_REC_MENSAL = 2;
	public static final int TP_REC_ANUAL = 3;

	/* tipos de recorrencias diarias */
	public static final int TP_REC_DIARIA_TODOS_DIAS = 0;
	public static final int TP_REC_DIARIA_TODOS_DIAS_UTEIS = 1;
	public static final int TP_REC_DIARIA_TODOS_DIAS_NAO_UTEIS = 2;
	public static final int TP_REC_DIARIA_CADA_N_DIAS = 3;

	/* tipos de recorrencias semanais */
	public static final int TP_REC_SEMANAL_TODOS_DIAS = 0;
	public static final int TP_REC_SEMANAL_CADA_N_SEMANAS = 1;

	/* tipos de recorrencias mensais */
	public static final int TP_REC_MENSAL_DIA_ESPECIFICO = 0;
	public static final int TP_REC_MENSAL_ENESIMO_DIA = 1;

	/* tipos de recorrencias anuais */
	public static final int TP_REC_ANUAL_DIA_ESPECIFICO = 0;
	public static final int TP_REC_ANUAL_ENESIMO_DIA = 1;

	/* tipo de termino de recorrencia */
	public static final int TP_SEM_DATA_TERMINO = 0;
	public static final int TP_TERMINO_N_OCORRENCIAS = 1;
	public static final int TP_TERMINO_DATA_ESPECIFICA = 2;

	/* enesimo dia de recorrencia */
	public static final int ORD_PRIMEIRO = 0;
	public static final int ORD_SEGUNDO = 1;
	public static final int ORD_TERCEIRO = 2;
	public static final int ORD_QUARTO = 3;
	public static final int ORD_ULTIMO = 4;

	/* tipo de enesimo dia de recorrencia */
	public static final int ENES_DIA = 0;
	public static final int ENES_DIA_SEMANA = 1;
	public static final int ENES_DIA_FIM_SEMANA = 2;
	public static final int ENES_DOMINGO = 3;
	public static final int ENES_SEGUNDA = 4;
	public static final int ENES_TERCA = 5;
	public static final int ENES_QUARTA = 6;
	public static final int ENES_QUINTA = 7;
	public static final int ENES_SEXTA = 8;
	public static final int ENES_SABADO = 9;

	public static HashMap<String, ArrayList<HashMap<String, Object>>> updateAgendamentosRecorrentes(Recorrencia recorrencia,
			Agendamento agendamento, int cdTipoLembrete, int cdUsuarioResponsavel, Connection connection) {
		boolean isConnectionNull = connection==null;
		try {
			if (isConnectionNull) {
				connection = Conexao.conectar();
				connection.setAutoCommit(false);
			}

			PreparedStatement pstmt = connection.prepareStatement("SELECT A.cd_usuario FROM agd_agendamento_usuario A " +
					"WHERE A.cd_agendamento = ? " +
					"  AND (A.tp_nivel_usuario = " + AgendamentoUsuarioServices.TP_CRIADOR + " OR" +
					"		A.tp_nivel_usuario = " + AgendamentoUsuarioServices.TP_CRIADOR_RESPONSAVEL + ")");
			pstmt.setInt(1, agendamento.getCdAgendamento());
			ResultSet rs = pstmt.executeQuery();
			HashMap<String, ArrayList<HashMap<String, Object>>> hashRecorrencias = createAgendamentosRecorrentes(recorrencia, agendamento,
					cdTipoLembrete, rs.next() ? rs.getInt(1) : 0, cdUsuarioResponsavel, true, connection);

			if (hashRecorrencias==null) {
				if (isConnectionNull)
					Conexao.rollback(connection);
				return null;
			}

			if (isConnectionNull)
				connection.commit();

			return hashRecorrencias;
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

	public static HashMap<String, ArrayList<HashMap<String, Object>>> createAgendamentosRecorrentes(Recorrencia recorrencia, Agendamento agendamento,
			int cdTipoLembrete, int cdUsuarioCriador, int cdUsuarioResponsavel, boolean updateAgendamentos,
			Connection connection) {
		boolean isConnectionNull = connection==null;
		try {
			connection = isConnectionNull ? Conexao.conectar() : connection;
			connection.setAutoCommit(isConnectionNull ? false : connection.getAutoCommit());

			int qtRecorrenciasOld = 0;
			if (updateAgendamentos) {
				PreparedStatement pstmt = connection.prepareStatement("SELECT COUNT(*) " +
						"FROM agd_agendamento A " +
						"WHERE A.cd_recorrencia = ?");
				pstmt.setInt(1, recorrencia.getCdRecorrencia());
				ResultSet rs = pstmt.executeQuery();
				qtRecorrenciasOld = rs.next() ? rs.getInt(1) : 0;
			}

			/* consulta de participantes ativos */
			ResultSetMap rsmParticipantes = null;
			if (recorrencia.getCdAgenda() > 0) {
				PreparedStatement pstmt = connection.prepareStatement("SELECT * " +
						"FROM agd_agenda_participante A " +
						"WHERE A.cd_agenda = ? " +
						"  AND A.st_participante = ?");
				pstmt.setInt(1, recorrencia.getCdAgenda());
				pstmt.setInt(2, AgendaParticipanteServices.ST_ATIVO);
				rsmParticipantes = new ResultSetMap(pstmt.executeQuery());
			}

			HashMap<String, ArrayList<HashMap<String,Object>>> hashRecorrencias = new HashMap<String, ArrayList<HashMap<String,Object>>>();
			hashRecorrencias.put("inserted", new ArrayList<HashMap<String,Object>>());
			hashRecorrencias.put("updated", new ArrayList<HashMap<String,Object>>());
			hashRecorrencias.put("deleted", new ArrayList<HashMap<String,Object>>());
			GregorianCalendar dtInicialRecorrencia = recorrencia.getDtInicio();

			GregorianCalendar dtInicialTemp = (GregorianCalendar)dtInicialRecorrencia.clone();
			GregorianCalendar dtFinalTemp = recorrencia.getTpTermino()==TP_TERMINO_DATA_ESPECIFICA ? recorrencia.getDtTermino() : null;
			if (recorrencia.getTpTermino() == TP_SEM_DATA_TERMINO) {
				dtFinalTemp = new GregorianCalendar();
				dtFinalTemp.add(Calendar.YEAR, 1);
				dtFinalTemp.set(Calendar.HOUR_OF_DAY, 0);
				dtFinalTemp.set(Calendar.MINUTE, 0);
				dtFinalTemp.set(Calendar.SECOND, 0);
				dtFinalTemp.set(Calendar.MILLISECOND, 0);
			}

			int nrRecorrenciasTemp = 0;
			while((dtFinalTemp != null && !dtInicialTemp.after(dtFinalTemp)) || nrRecorrenciasTemp < recorrencia.getNrRecorrencias()) {
				GregorianCalendar dtTemp = null;
				switch(recorrencia.getTpRecorrencia()) {
					/* recorrencia diaria */
					case TP_REC_DIARIA:
						switch(recorrencia.getTpEspecificidadeRecorrencia()) {
							case TP_REC_DIARIA_CADA_N_DIAS:
							case TP_REC_DIARIA_TODOS_DIAS:
								dtTemp = (GregorianCalendar)dtInicialTemp.clone();
								break;
							case TP_REC_DIARIA_TODOS_DIAS_UTEIS:
								dtTemp = dtInicialTemp.get(Calendar.DAY_OF_WEEK)==Calendar.SATURDAY || dtInicialTemp.get(Calendar.DAY_OF_WEEK)==Calendar.SUNDAY ? null :
									(GregorianCalendar)dtInicialTemp.clone();
								break;
							case TP_REC_DIARIA_TODOS_DIAS_NAO_UTEIS:
								dtTemp = !(dtInicialTemp.get(Calendar.DAY_OF_WEEK)==Calendar.SATURDAY || dtInicialTemp.get(Calendar.DAY_OF_WEEK)==Calendar.SUNDAY) ? null :
									(GregorianCalendar)dtInicialTemp.clone();
								break;
						}
						int qtDiasIntervalo = recorrencia.getTpEspecificidadeRecorrencia()==TP_REC_DIARIA_CADA_N_DIAS && recorrencia.getQtIntervaloRecorrencia()>0 ?
								recorrencia.getQtIntervaloRecorrencia() : 1;
						dtInicialTemp.add(Calendar.DAY_OF_MONTH, qtDiasIntervalo);
						break;
					/* recorrencia semanal */
					case TP_REC_SEMANAL:
						switch(recorrencia.getTpEspecificidadeRecorrencia()) {
							case TP_REC_SEMANAL_TODOS_DIAS:
								dtTemp = (GregorianCalendar)dtInicialTemp.clone();
								break;
							case TP_REC_SEMANAL_CADA_N_SEMANAS:
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
						qtDiasIntervalo = recorrencia.getTpEspecificidadeRecorrencia()==TP_REC_SEMANAL_CADA_N_SEMANAS && recorrencia.getQtIntervaloRecorrencia()>0 ?
								recorrencia.getQtIntervaloRecorrencia() : 1;
						dtInicialTemp.add(Calendar.DAY_OF_MONTH, qtDiasIntervalo);
						break;
					/* recorrencia mensal */
					case TP_REC_MENSAL:
						switch(recorrencia.getTpEspecificidadeRecorrencia()) {
							case TP_REC_MENSAL_DIA_ESPECIFICO:
								int nrDia = recorrencia.getNrDiaRecorrenciaMensal()<=Util.getQuantidadeDias(dtInicialTemp.get(Calendar.MONTH), dtInicialTemp.get(Calendar.YEAR)) ?
										recorrencia.getNrDiaRecorrenciaMensal() : Util.getQuantidadeDias(dtInicialTemp.get(Calendar.MONTH), dtInicialTemp.get(Calendar.YEAR));
								dtTemp = new GregorianCalendar(dtInicialTemp.get(Calendar.YEAR), dtInicialTemp.get(Calendar.MONTH), nrDia);
								break;
							case TP_REC_MENSAL_ENESIMO_DIA:
								dtTemp = getEnesimoDiaMes(dtInicialTemp.get(Calendar.MONTH), dtInicialTemp.get(Calendar.YEAR),
										recorrencia.getTpOrdemRecorrenciaMensal(), recorrencia.getNrOrdemRecorrenciaMensal());
								break;
						}
						if (dtTemp.before(recorrencia.getDtInicio()) || dtTemp.after(dtFinalTemp))
							dtTemp = null;
						qtDiasIntervalo = recorrencia.getQtIntervaloRecorrencia()>0 ? recorrencia.getQtIntervaloRecorrencia() : 1;
						for (int j=0; j<qtDiasIntervalo; j++)
							dtInicialTemp = new GregorianCalendar(dtInicialTemp.get(Calendar.YEAR) + (dtInicialTemp.get(Calendar.MONTH)==Calendar.DECEMBER ? 1 : 0),
									(dtInicialTemp.get(Calendar.MONTH) + 1) % 12, 1);
						break;
					/* recorrencia anual */
					case TP_REC_ANUAL:
						switch(recorrencia.getTpEspecificidadeRecorrencia()) {
							case TP_REC_ANUAL_DIA_ESPECIFICO:
								int nrMes = recorrencia.getNrMesRecorrenciaAnual();
								int nrDia = recorrencia.getNrDiaRecorrenciaAnual()<=Util.getQuantidadeDias(nrMes, dtInicialTemp.get(Calendar.YEAR)) ?
										recorrencia.getNrDiaRecorrenciaAnual() : Util.getQuantidadeDias(nrMes, dtInicialTemp.get(Calendar.YEAR));
								dtTemp = new GregorianCalendar(dtInicialTemp.get(Calendar.YEAR), nrMes, nrDia);
							case TP_REC_ANUAL_ENESIMO_DIA:
								dtTemp = getEnesimoDiaMes(recorrencia.getNrMesRecorrenciaAnual(), dtInicialTemp.get(Calendar.YEAR),
										recorrencia.getTpOrdemRecorrenciaAnual(), recorrencia.getNrOrdemRecorrenciaAnual());
						}
						if (dtTemp.before(recorrencia.getDtInicio()) || dtTemp.after(dtFinalTemp))
							dtTemp = null;
						dtInicialTemp = new GregorianCalendar(dtInicialTemp.get(Calendar.YEAR) + 1, Calendar.JANUARY, 1);
						break;
				}

				/* criacao do agendamento */
				if (dtTemp != null) {
					Agendamento agendTemp = (Agendamento)agendamento.clone();
					agendTemp.setLgOriginal(1);
					dtTemp.set(Calendar.HOUR_OF_DAY, agendamento.getDtInicial().get(Calendar.HOUR_OF_DAY));
					dtTemp.set(Calendar.MINUTE, agendamento.getDtInicial().get(Calendar.MINUTE));
					dtTemp.set(Calendar.SECOND, agendamento.getDtInicial().get(Calendar.SECOND));
					dtTemp.set(Calendar.MILLISECOND, agendamento.getDtInicial().get(Calendar.MILLISECOND));
					agendTemp.setDtInicial(dtTemp);
					nrRecorrenciasTemp++;
					agendTemp.setNrRecorrencia(nrRecorrenciasTemp);
					if (nrRecorrenciasTemp==1 && agendamento.getCdAgendamento()<=0 && agendamento.getDtInicial().before(dtTemp))
						agendamento.setDtInicial(dtTemp);
					if (Util.equalsDate(agendamento.getDtInicial(), dtTemp))
						agendamento.setNrRecorrencia(nrRecorrenciasTemp);
					else if (updateAgendamentos) {
						 PreparedStatement pstmt = connection.prepareStatement("SELECT cd_agendamento FROM agd_agendamento A " +
						 		"WHERE A.cd_recorrencia = ? " +
						 		"  AND A.nr_recorrencia = ?");
						 pstmt.setInt(1, recorrencia.getCdRecorrencia());
						 pstmt.setInt(2, nrRecorrenciasTemp);
						 ResultSet rs = pstmt.executeQuery();
						 Agendamento agendOld = rs.next() ? AgendamentoDAO.get(rs.getInt("cd_agendamento"), connection) : null;
						 GregorianCalendar dtInicialOld = agendOld==null ? (GregorianCalendar)agendTemp.getDtInicial().clone() :
							 (GregorianCalendar)agendOld.getDtInicial().clone();
						 if (agendOld==null || (agendOld.getLgOriginal() == 1)) {
							 if (agendOld != null) {
								 agendOld.setDtInicial(dtTemp);
								 if (AgendamentoServices.update(agendOld, cdTipoLembrete, cdUsuarioResponsavel, null, rsmParticipantes, connection) == null) {
									if (isConnectionNull)
										Conexao.rollback(connection);
									return null;
								 }
							 }
							 else {
								if (AgendamentoServices.insert(agendTemp, cdTipoLembrete, cdUsuarioCriador, cdUsuarioResponsavel, null, rsmParticipantes, connection) == null) {
									if (isConnectionNull)
										Conexao.rollback(connection);
									return null;
								}
							 }
							 HashMap<String, Object> hash = new HashMap<String,Object>();
							 hash.put("agendamento", agendOld!=null ? agendOld : agendTemp);
							 hash.put("dtInicialOld", dtInicialOld);
							 hashRecorrencias.get(agendOld!=null ? "updated" : "inserted").add(hash);
						 }
					}
					else {
						if (AgendamentoServices.insert(agendTemp, cdTipoLembrete, cdUsuarioCriador, cdUsuarioResponsavel, null, rsmParticipantes, connection) == null) {
							if (isConnectionNull)
								Conexao.rollback(connection);
							return null;
						}
						HashMap<String, Object> hash = new HashMap<String,Object>();
						hash.put("agendamento", agendTemp);
						hash.put("dtInicialOld", agendTemp.getDtInicial().clone());
						hashRecorrencias.get("inserted").add(hash);
					}
				}
			}

			if (updateAgendamentos && qtRecorrenciasOld>nrRecorrenciasTemp) {
				PreparedStatement pstmt = connection.prepareStatement("SELECT cd_agendamento FROM agd_agendamento A " +
				 		"WHERE A.cd_recorrencia = ? " +
				 		"  AND A.nr_recorrencia > ?");
				 pstmt.setInt(1, recorrencia.getCdRecorrencia());
				 pstmt.setInt(2, nrRecorrenciasTemp);
				 ResultSet rs = pstmt.executeQuery();
				 while (rs.next()) {
					 Agendamento agendTemp = AgendamentoDAO.get(rs.getInt("cd_agendamento"), connection);
					 if (AgendamentoServices.delete(agendTemp.getCdAgendamento(), connection) <= 0) {
						 if (isConnectionNull)
							 Conexao.rollback(connection);
						 return null;
					 }
					 HashMap<String, Object> hash = new HashMap<String,Object>();
					 hash.put("agendamento", agendTemp);
					 hashRecorrencias.get("deleted").add(hash);
				 }
			}

			if (isConnectionNull)
				connection.commit();

			return hashRecorrencias;
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

	public static GregorianCalendar getEnesimoDiaMes(int mes, int ano, int tpEnesimoDia, int nrOrdem) {
		GregorianCalendar dtTemp = null;
		switch(tpEnesimoDia) {
			case ENES_DIA:
				dtTemp = new GregorianCalendar(ano, mes, nrOrdem==ORD_ULTIMO ? Util.getQuantidadeDias(mes, ano) : nrOrdem + 1);
				break;
			case ENES_DIA_SEMANA:
				if (nrOrdem == ORD_ULTIMO) {
					dtTemp = Util.getUltimoDiaMes(mes, ano);
					while (dtTemp.get(Calendar.DAY_OF_WEEK)==Calendar.SATURDAY || dtTemp.get(Calendar.DAY_OF_WEEK)==Calendar.SUNDAY) {
						dtTemp.add(Calendar.DAY_OF_MONTH, -1);
					}
				}
				else {
					dtTemp = new GregorianCalendar(ano, mes, 1);
					while (dtTemp.get(Calendar.DAY_OF_WEEK)==Calendar.SATURDAY || dtTemp.get(Calendar.DAY_OF_WEEK)==Calendar.SUNDAY)
						dtTemp.add(Calendar.DAY_OF_MONTH, 1);
					for (int j=0; j<nrOrdem; ) {
						dtTemp.add(Calendar.DAY_OF_MONTH, 1);
						if (!(dtTemp.get(Calendar.DAY_OF_WEEK)==Calendar.SATURDAY || dtTemp.get(Calendar.DAY_OF_WEEK)==Calendar.SUNDAY))
							j++;
					}
				}
				break;
			case ENES_DIA_FIM_SEMANA:
				if (nrOrdem == ORD_ULTIMO) {
					dtTemp = Util.getUltimoDiaMes(mes, ano);
					while (!(dtTemp.get(Calendar.DAY_OF_WEEK)==Calendar.SATURDAY || dtTemp.get(Calendar.DAY_OF_WEEK)==Calendar.SUNDAY))
						dtTemp.add(Calendar.DAY_OF_MONTH, -1);
				}
				else {
					dtTemp = new GregorianCalendar(ano, mes, 1);
					while (!(dtTemp.get(Calendar.DAY_OF_WEEK)==Calendar.SATURDAY || dtTemp.get(Calendar.DAY_OF_WEEK)==Calendar.SUNDAY))
						dtTemp.add(Calendar.DAY_OF_MONTH, 1);
					for (int j=0; j<nrOrdem; ) {
						dtTemp.add(Calendar.DAY_OF_MONTH, 1);
						if (dtTemp.get(Calendar.DAY_OF_WEEK)==Calendar.SATURDAY || dtTemp.get(Calendar.DAY_OF_WEEK)==Calendar.SUNDAY)
							j++;
					}
				}
				break;
			default:
				if (nrOrdem == ORD_ULTIMO) {
					dtTemp = Util.getUltimoDiaMes(mes, ano);
					while (dtTemp.get(Calendar.DAY_OF_WEEK) != tpEnesimoDia - 2)
						dtTemp.add(Calendar.DAY_OF_MONTH, -1);
				}
				else {
					dtTemp = new GregorianCalendar(ano, mes, 1);
					while (dtTemp.get(Calendar.DAY_OF_WEEK) != tpEnesimoDia - 2)
						dtTemp.add(Calendar.DAY_OF_MONTH, 1);
					if (nrOrdem !=  ORD_PRIMEIRO)
						dtTemp.add(Calendar.DAY_OF_MONTH, 7 * nrOrdem);
				}
				break;
		}
		return dtTemp;
	}

}
