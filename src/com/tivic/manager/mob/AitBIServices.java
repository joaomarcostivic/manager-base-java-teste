package com.tivic.manager.mob;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;

import com.tivic.sol.connection.Conexao;
import com.tivic.manager.grl.equipamento.EquipamentoServices;
import com.tivic.manager.util.Util;

import sol.dao.ResultSetMap;
import sol.util.Result;

public class AitBIServices {


	public static ResultSetMap statsRankingInfracao(GregorianCalendar dtInicial, GregorianCalendar dtFinal, int qtLimite) {
		return statsRankingInfracao(dtInicial, dtFinal, qtLimite, null);
	}
	
	public static ResultSetMap statsRankingInfracao(GregorianCalendar dtInicial, GregorianCalendar dtFinal, int qtLimite, Connection connect) {
		
		/*
		 * TL;DR
		 * Alguns modulos novos do eTransito Angular precisaram ser utilizados na base antiga (ex: eTransito BI). 
		 * Manter este desvio de fluxo no momento
		 */
		if(Util.isStrBaseAntiga())
			return statsRankingInfracaoOld(dtInicial, dtFinal, qtLimite, connect);
		
		try {
			connect = connect==null ? Conexao.conectar() : connect;

			
			PreparedStatement ps = connect.prepareStatement(
						" SELECT COUNT(*) AS qtd, cd_infracao"
						+ " FROM mob_ait"
						+ " WHERE dt_infracao BETWEEN ? AND ?"
						+ " GROUP BY cd_infracao"
						+ " ORDER BY qtd DESC"
						+ " LIMIT ?");
			
			ps.setTimestamp(1, Util.convCalendarToTimestamp(dtInicial));
			ps.setTimestamp(2, Util.convCalendarToTimestamp(dtFinal));
			ps.setInt(3, qtLimite);
			
			ResultSet rs = ps.executeQuery();
			
			ResultSetMap resultados = new ResultSetMap();
			
			while(rs.next()) {
				com.tivic.manager.mob.Infracao infracao = com.tivic.manager.mob.InfracaoDAO.get(rs.getInt("cd_infracao"), connect);
				
				HashMap<String, Object> register = new HashMap<>();
				
				String dsArtigo = "art. "+infracao.getNrArtigo()+" ";
				dsArtigo += infracao.getNrParagrafo()!=null ? ", P. "+infracao.getNrParagrafo()+"o" : ", P. unico";
				dsArtigo += infracao.getNrInciso()!=null ? ", "+infracao.getNrInciso() : "";
				dsArtigo += infracao.getNrAlinea()!=null ? ", "+infracao.getNrAlinea() : "";
				
				register.put("artigo", dsArtigo);
				register.put("descricao", infracao.getDsInfracao());
				register.put("codigo_detran", infracao.getNrCodDetran());
				register.put("cd_infracao", infracao.getCdInfracao());
				register.put("quantidade", rs.getInt("qtd"));
				
				resultados.addRegister(register);
			}
			
			return resultados;
		}
		catch(Exception e) {
			System.out.println("Erro! AitServices.statsRankingInfracao.");
			e.printStackTrace(System.out);
			return null;
		}
		finally {
			Conexao.desconectar(connect);
		}
	}
	
	private static ResultSetMap statsRankingInfracaoOld(GregorianCalendar dtInicial, GregorianCalendar dtFinal, int qtLimite, Connection connect) {
		try {
			connect = connect==null ? Conexao.conectar() : connect;

			
			PreparedStatement ps = connect.prepareStatement(
						" SELECT COUNT(*) AS qtd, cod_infracao"
						+ " FROM AIT"
						+ " WHERE dt_infracao BETWEEN ? AND ?"
						+ " GROUP BY cod_infracao"
						+ " ORDER BY qtd DESC"
						+ " LIMIT ?");
			
			ps.setTimestamp(1, Util.convCalendarToTimestamp(dtInicial));
			ps.setTimestamp(2, Util.convCalendarToTimestamp(dtFinal));
			ps.setInt(3, qtLimite);
			
			ResultSet rs = ps.executeQuery();
			
			ResultSetMap resultados = new ResultSetMap();
			
			while(rs.next()) {
				com.tivic.manager.str.Infracao infracao = com.tivic.manager.str.InfracaoDAO.get(rs.getInt("cod_infracao"), connect);
				
				HashMap<String, Object> register = new HashMap<>();
				
				String dsArtigo = "art. "+infracao.getNrArtigo()+" ";
				dsArtigo += infracao.getNrParagrafo()!=null ? ", P. "+infracao.getNrParagrafo()+"o" : ", P. unico";
				dsArtigo += infracao.getNrInciso()!=null ? ", "+infracao.getNrInciso() : "";
				dsArtigo += infracao.getNrAlinea()!=null ? ", "+infracao.getNrAlinea() : "";
				
				register.put("artigo", dsArtigo);
				register.put("descricao", infracao.getDsInfracao());
				register.put("codigo_detran", infracao.getNrCodDetran());
				register.put("cd_infracao", infracao.getCdInfracao());
				register.put("quantidade", rs.getInt("qtd"));
				
				resultados.addRegister(register);
			}
			
			return resultados;
		}
		catch(Exception e) {
			System.out.println("Erro! AitServices.statsRankingInfracaoOld.");
			e.printStackTrace(System.out);
			return null;
		}
		finally {
			Conexao.desconectar(connect);
		}
	}
	

	/*
	 * STATS
	 */
	@Deprecated
	public static ResultSetMap statsAitTipo() {
		GregorianCalendar hoje = new GregorianCalendar();
		GregorianCalendar dtInicial = new GregorianCalendar(hoje.get(Calendar.YEAR)-1, Calendar.JANUARY, 1, 0, 0, 0);
		GregorianCalendar dtFinal = new GregorianCalendar(hoje.get(Calendar.YEAR), Calendar.JANUARY, 31, 0, 0, 0);
		return statsAitTipo(dtInicial, dtFinal, 5, null);		
	}
	@Deprecated
	public static ResultSetMap statsAitTipo(GregorianCalendar dtInicial, GregorianCalendar dtFinal, int qtLimite) {
		return statsAitTipo(dtInicial, dtFinal, qtLimite, null);
	}
	@Deprecated
	public static ResultSetMap statsAitTipo(GregorianCalendar dtInicial, GregorianCalendar dtFinal, int qtLimite, Connection connect) {
		try {
			connect = connect==null ? Conexao.conectar() : connect;

			boolean lgBaseAntiga = Util.getConfManager().getProps().getProperty("STR_BASE_ANTIGA").equals("1");
			
			PreparedStatement ps = connect.prepareStatement(
					" SELECT COUNT(*) AS qtd, cod_infracao"
					+ " FROM ait"
					+ " WHERE dt_infracao BETWEEN ? AND ?"
					+ " GROUP BY cod_infracao"
					+ " ORDER BY qtd DESC"
					+ " LIMIT ?");
			
			if(!lgBaseAntiga) {
				ps = connect.prepareStatement(
						" SELECT COUNT(*) AS qtd, cd_infracao"
						+ " FROM mob_ait"
						+ " WHERE dt_infracao BETWEEN ? AND ?"
						+ " GROUP BY cd_infracao"
						+ " ORDER BY qtd DESC"
						+ " LIMIT ?");
			}
			
			ps.setTimestamp(1, Util.convCalendarToTimestamp(dtInicial));
			ps.setTimestamp(2, Util.convCalendarToTimestamp(dtFinal));
			ps.setInt(3, qtLimite);
			
			ResultSetMap rsm = new ResultSetMap(ps.executeQuery());
			
			while(rsm.next()) {
				if(lgBaseAntiga) {
					Infracao infracao = InfracaoDAO.get(rsm.getInt("cod_infracao"), connect);
					String dsInfracao = "art. "+infracao.getNrArtigo()+" nï¿½ ";
					dsInfracao += infracao.getNrParagrafo()!=null ? ", nï¿½ "+infracao.getNrParagrafo()+" nï¿½ " : ", P ï¿½nico";
					dsInfracao += infracao.getNrInciso()!=null ? ", "+infracao.getNrInciso() : "";
					dsInfracao += infracao.getNrAlinea()!=null ? ", "+infracao.getNrAlinea() : "";
					
					rsm.setValueToField("DS_INFRACAO", dsInfracao);
					rsm.setValueToField("TXT_INFRACAO", infracao.getDsInfracao());
					rsm.setValueToField("NR_DETRAN", infracao.getNrCodDetran());
				} else {
					com.tivic.manager.mob.Infracao infracao = com.tivic.manager.mob.InfracaoDAO.get(rsm.getInt("cd_infracao"), connect);
					String dsInfracao = "art. "+infracao.getNrArtigo()+" nï¿½ ";
					dsInfracao += infracao.getNrParagrafo()!=null ? ", nï¿½ "+infracao.getNrParagrafo()+"ï¿½" : ", P. ï¿½nico";
					dsInfracao += infracao.getNrInciso()!=null ? ", "+infracao.getNrInciso() : "";
					dsInfracao += infracao.getNrAlinea()!=null ? ", "+infracao.getNrAlinea() : "";
					
					rsm.setValueToField("DS_INFRACAO", dsInfracao);
					rsm.setValueToField("TXT_INFRACAO", infracao.getDsInfracao());
					rsm.setValueToField("NR_DETRAN", infracao.getNrCodDetran());
				}
				
			}
			rsm.beforeFirst();
			
			return rsm;
		}
		catch(Exception e) {
			System.out.println("Erro! AitServices.statsAitTipo.");
			e.printStackTrace(System.out);
			return null;
		}
		finally {
			Conexao.desconectar(connect);
		}
	}
	
	
	@Deprecated
	public static ResultSetMap statsAitAgente(int limit) {
		GregorianCalendar hoje = new GregorianCalendar();
		GregorianCalendar dtInicial = new GregorianCalendar(hoje.get(Calendar.YEAR)-1, Calendar.JANUARY, 1, 0, 0, 0);
		GregorianCalendar dtFinal = new GregorianCalendar(hoje.get(Calendar.YEAR), Calendar.JANUARY, 31, 0, 0, 0);
		return statsAitAgente(dtInicial, dtFinal, limit, null);		
	}
	@Deprecated
	public static ResultSetMap statsAitAgente(GregorianCalendar dtInicial, GregorianCalendar dtFinal, int qtLimite) {
		return statsAitAgente(dtInicial, dtFinal, qtLimite, null);
	}
	@Deprecated
	public static ResultSetMap statsAitAgente(GregorianCalendar dtInicial, GregorianCalendar dtFinal, int qtLimite, Connection connect) {
		try {
			connect = connect==null ? Conexao.conectar() : connect;			

			boolean lgBaseAntiga = Util.getConfManager().getProps().getProperty("STR_BASE_ANTIGA").equals("1");
			
			PreparedStatement ps = connect.prepareStatement(
					" SELECT COUNT(*) AS qtd, cod_agente"
					+ " FROM ait"
					+ " WHERE dt_infracao BETWEEN ? AND ?"
					+ " GROUP BY cod_agente"
					+ " ORDER BY qtd DESC"
					+ " LIMIT ?");
			
			if(!lgBaseAntiga) {
				ps = connect.prepareStatement(
						" SELECT COUNT(*) AS qtd, cd_agente"
						+ " FROM mob_ait"
						+ " WHERE dt_infracao BETWEEN ? AND ?"
						+ " GROUP BY cd_agente"
						+ " ORDER BY qtd DESC"
						+ " LIMIT ?");
			}
			
			ps.setTimestamp(1, Util.convCalendarToTimestamp(dtInicial));
			ps.setTimestamp(2, Util.convCalendarToTimestamp(dtFinal));
			ps.setInt(3, qtLimite);
			
			ResultSetMap rsm = new ResultSetMap(ps.executeQuery());
			
			while(rsm.next()) {
				if(lgBaseAntiga) {
					com.tivic.manager.str.Agente agente = com.tivic.manager.str.AgenteDAO.get(rsm.getInt("cod_agente"), connect);
					
					rsm.setValueToField("NM_AGENTE", agente.getNmAgente());
					rsm.setValueToField("NM_PRE_AGENTE", agente.getNmAgente().split(" ")[0]);
					rsm.setValueToField("NR_MATRICULA", agente.getNrMatricula());
				} else {
					Agente agente = AgenteDAO.get(rsm.getInt("cd_agente"), connect);

					rsm.setValueToField("NM_AGENTE", agente.getNmAgente());
					rsm.setValueToField("NM_PRE_AGENTE", agente.getNmAgente().split(" ")[0]);
					rsm.setValueToField("NR_MATRICULA", agente.getNrMatricula());
				}
			}
			rsm.beforeFirst();
			
			return rsm;
		}
		catch(Exception e) {
			System.out.println("Erro! AitServices.statsAitAgente.");
			e.printStackTrace(System.out);
			return null;
		}
		finally {
			Conexao.desconectar(connect);
		}
	}
	
	@Deprecated
	public static ResultSetMap statsAitEquipamento(int tpEquipamento) {
		GregorianCalendar hoje = new GregorianCalendar();
		GregorianCalendar dtInicial = new GregorianCalendar(hoje.get(Calendar.YEAR)-1, Calendar.JANUARY, 1, 0, 0, 0);
		GregorianCalendar dtFinal = new GregorianCalendar(hoje.get(Calendar.YEAR), Calendar.JANUARY, 31, 0, 0, 0);
		return statsAitEquipamento(tpEquipamento, dtInicial, dtFinal, 10, null);		
	}
	public static ResultSetMap statsAitEquipamento(int tpEquipamento, GregorianCalendar dtInicial, GregorianCalendar dtFinal, int qtLimite) {
		return statsAitEquipamento(tpEquipamento, dtInicial, dtFinal, qtLimite, null);
	}
	public static ResultSetMap statsAitEquipamento(int tpEquipamento, GregorianCalendar dtInicial, GregorianCalendar dtFinal, int qtLimite, Connection connect) {
		try {
			connect = connect==null ? Conexao.conectar() : connect;
			
			boolean lgBaseAntiga = Util.isStrBaseAntiga();
			
			PreparedStatement ps = connect.prepareStatement(
					" SELECT COUNT(A.*) AS qtd, A.cod_infracao "
					+ " FROM ait A"
					+ " JOIN grl_equipamento B ON (A.cd_equipamento = B.cd_equipamento)"
					+ " WHERE dt_infracao BETWEEN ? AND ?"
					+ " AND B.tp_equipamento = ?"
					+ " GROUP BY A.cod_infracao"
					+ " ORDER BY qtd DESC"
					+ " LIMIT ?");
			
			if(!lgBaseAntiga) {
				ps = connect.prepareStatement(
						" SELECT COUNT(A.*) AS qtd, A.cd_infracao "
						+ " FROM mob_ait A"
						+ " JOIN grl_equipamento B ON (A.cd_equipamento = B.cd_equipamento)"
						+ " WHERE dt_infracao BETWEEN ? AND ?"
						+ " AND B.tp_equipamento = ?"
						+ " GROUP BY A.cd_infracao"
						+ " ORDER BY qtd DESC"
						+ " LIMIT ?");
			}			
			ps.setTimestamp(1, Util.convCalendarToTimestamp(dtInicial));
			ps.setTimestamp(2, Util.convCalendarToTimestamp(dtFinal));
			ps.setInt(3, tpEquipamento);
			ps.setInt(4, qtLimite);
			
			ResultSetMap rsm = new ResultSetMap(ps.executeQuery());
			
			while(rsm.next()) {
				if(lgBaseAntiga) {
					Infracao infracao = InfracaoDAO.get(rsm.getInt("cod_infracao"), connect);
					rsm.setValueToField("TXT_INFRACAO", infracao.getDsInfracao());
					rsm.setValueToField("NR_DETRAN", infracao.getNrCodDetran());
				} else {
					
				}
				
				rsm.setValueToField("NM_TP_EQUIPAMENTO", EquipamentoServices.tiposEquipamento[tpEquipamento]);
			}
			rsm.beforeFirst();
			
			return rsm;
		}
		catch(Exception e) {
			System.out.println("Erro! AitServices.statsAitEquipamento.");
			e.printStackTrace(System.out);
			return null;
		}
		finally {
			Conexao.desconectar(connect);
		}
	}
	
	@Deprecated
	public static ResultSetMap statsAitEquipamentoAgrupado() {
		GregorianCalendar hoje = new GregorianCalendar();
		GregorianCalendar dtInicial = new GregorianCalendar(hoje.get(Calendar.YEAR)-1, Calendar.JANUARY, 1, 0, 0, 0);
		GregorianCalendar dtFinal = new GregorianCalendar(hoje.get(Calendar.YEAR), Calendar.JANUARY, 31, 0, 0, 0);
		return statsAitEquipamentoAgrupado(dtInicial, dtFinal, null);		
	}
	@Deprecated
	public static ResultSetMap statsAitEquipamentoAgrupado(GregorianCalendar dtInicial, GregorianCalendar dtFinal) {
		return statsAitEquipamentoAgrupado(dtInicial, dtFinal, null);
	}
	@Deprecated
	public static ResultSetMap statsAitEquipamentoAgrupado(GregorianCalendar dtInicial, GregorianCalendar dtFinal, Connection connect) {
		try {
			connect = connect==null ? Conexao.conectar() : connect;
			
			boolean lgBaseAntiga = Util.getConfManager().getProps().getProperty("STR_BASE_ANTIGA").equals("1");
			
			PreparedStatement ps = connect.prepareStatement(
					" SELECT COUNT(A.*) AS qtd, B.tp_equipamento "
					+ " FROM ait A"
					+ " LEFT OUTER JOIN grl_equipamento B ON (A.cd_equipamento = B.cd_equipamento)"
					+ " WHERE dt_infracao BETWEEN ? AND ?"
					+ " GROUP BY B.tp_equipamento"
					+ " ORDER BY qtd DESC");
			
			if(!lgBaseAntiga) {
				ps = connect.prepareStatement(
						" SELECT COUNT(A.*) AS qtd, B.tp_equipamento "
						+ " FROM mob_ait A"
						+ " LEFT OUTER JOIN grl_equipamento B ON (A.cd_equipamento = B.cd_equipamento)"
						+ " WHERE dt_infracao BETWEEN ? AND ?"
						+ " GROUP BY B.tp_equipamento"
						+ " ORDER BY qtd DESC");
			}
			
			ps.setTimestamp(1, Util.convCalendarToTimestamp(dtInicial));
			ps.setTimestamp(2, Util.convCalendarToTimestamp(dtFinal));
			
			ResultSetMap rsm = new ResultSetMap(ps.executeQuery());
			
			while(rsm.next()) {				
				rsm.setValueToField("NM_TP_EQUIPAMENTO", EquipamentoServices.tiposEquipamento[rsm.getInt("tp_equipamento")]);
			}
			rsm.beforeFirst();
			
			return rsm;
		}
		catch(Exception e) {
			System.out.println("Erro! AitServices.statsAitEquipamentoAgrupado.");
			e.printStackTrace(System.out);
			return null;
		}
		finally {
			Conexao.desconectar(connect);
		}
	}
	
	@Deprecated
	public static ResultSetMap statsAitEquipamentoAgrupadoPeriodo() {
		GregorianCalendar hoje = new GregorianCalendar();
		GregorianCalendar dtInicial = new GregorianCalendar(hoje.get(Calendar.YEAR)-1, Calendar.JANUARY, 1, 0, 0, 0);
		GregorianCalendar dtFinal = new GregorianCalendar(hoje.get(Calendar.YEAR), Calendar.JANUARY, 31, 0, 0, 0);
		return statsAitEquipamentoAgrupadoPeriodo(dtInicial, dtFinal, null);		
	}
	@Deprecated
	public static ResultSetMap statsAitEquipamentoAgrupadoPeriodo(GregorianCalendar dtInicial, GregorianCalendar dtFinal) {
		return statsAitEquipamentoAgrupadoPeriodo(dtInicial, dtFinal, null);
	}
	@Deprecated
	public static ResultSetMap statsAitEquipamentoAgrupadoPeriodo(GregorianCalendar dtInicial, GregorianCalendar dtFinal, Connection connect) {
		try {
			connect = connect==null ? Conexao.conectar() : connect;
			
			boolean lgBaseAntiga = Util.getConfManager().getProps().getProperty("STR_BASE_ANTIGA").equals("1");
			
			GregorianCalendar dtBase = (GregorianCalendar)dtInicial.clone();
			
			//Lista de equipamentos
			int[] equipamentos = {EquipamentoServices.TALONARIO_ELETRONICO, EquipamentoServices.CAMERA, EquipamentoServices.RADAR_FIXO};			
					
			ResultSetMap rsmLabels = new ResultSetMap();
			while(dtInicial.compareTo(dtFinal) <= 0) {	
				
				HashMap<String, Object> register = new HashMap<String, Object>();
				register.put("NM_MES", Util.formatDate(dtInicial, "MMM/yyyy"));
				register.put("NR_MES", dtInicial.get(Calendar.MONTH));
				rsmLabels.addRegister(register);
				
				dtInicial.add(Calendar.MONTH, 1);			
			} 
			rsmLabels.beforeFirst();
			
			ResultSetMap rsm = new ResultSetMap();
			
			for (int tpEquipamento : equipamentos) {
				
				HashMap<String, Object> register = new HashMap<String, Object>();
				register.put("RSM_LABELS", rsmLabels);
				
				dtInicial = (GregorianCalendar)dtBase.clone();
				
				register.put("NM_TP_EQUIPAMENTO", EquipamentoServices.tiposEquipamento[tpEquipamento]);
				
				int[] data = new int[rsmLabels.size()];
				int i = 0;
				
				while(dtInicial.compareTo(dtFinal) <= 0) {	
					GregorianCalendar dtAux = (GregorianCalendar)dtInicial.clone();
					dtAux.add(Calendar.MONTH, 1);					

					PreparedStatement ps = connect.prepareStatement(
							" SELECT COUNT(A.*) AS qtd"
							+ " FROM ait A"
							+ " JOIN grl_equipamento B ON (A.cd_equipamento = B.cd_equipamento)"
							+ " WHERE dt_infracao BETWEEN ? AND ?"
							+ " AND B.tp_equipamento = ?");
					
					if(!lgBaseAntiga) {
						ps = connect.prepareStatement(
							" SELECT COUNT(A.*) AS qtd"
							+ " FROM mob_ait A"
							+ " JOIN grl_equipamento B ON (A.cd_equipamento = B.cd_equipamento)"
							+ " WHERE A.dt_infracao BETWEEN ? AND ?"
							+ " AND B.tp_equipamento = ?");
					}				

					ps.setTimestamp(1, Util.convCalendarToTimestamp(dtInicial));
					ps.setTimestamp(2, Util.convCalendarToTimestamp(dtAux));
					ps.setInt(3, tpEquipamento);				

					ResultSetMap rsmAux = new ResultSetMap(ps.executeQuery());
					if(rsmAux.next()) {
						data[i] = rsmAux.getInt("qtd");
					} else {
						data[i] = 0;
					}

					i++;
					dtInicial.add(Calendar.MONTH, 1);
				}

				register.put("ARRAY_DATA", data);				
				rsm.addRegister(register);
			}			
			
			return rsm;
		}
		catch(Exception e) {
			System.out.println("Erro! AitServices.statsAitEquipamentoAgrupadoPeriodo.");
			e.printStackTrace(System.out);
			return null;
		}
		finally {
			Conexao.desconectar(connect);
		}
	}
	
	public static ResultSetMap statsProducaoMensal(GregorianCalendar dtInicial, GregorianCalendar dtFinal) {
		return statsProducaoMensal(dtInicial, dtFinal, null);
	}
	
	public static ResultSetMap statsProducaoMensal(GregorianCalendar dtInicial, GregorianCalendar dtFinal, Connection connect) {
		
		/*
		 * TL;DR
		 * Alguns modulos novos do eTransito Angular precisaram ser utilizados na base antiga (ex: eTransito BI). 
		 * Manter este desvio de fluxo no momento
		 */
		if(Util.isStrBaseAntiga())
			return statsProducaoMensalOld(dtInicial, dtFinal, connect); 
			
		try {
			connect = connect==null ? Conexao.conectar() : connect;
			
			GregorianCalendar dtBase = (GregorianCalendar)dtInicial.clone();
			
			//Lista de equipamentos
			int[] tipos = {EquipamentoServices.TALONARIO_ELETRONICO, EquipamentoServices.CAMERA, EquipamentoServices.RADAR_FIXO, -1};// -1 = OUTROS			
					
			ArrayList<String> labels = new ArrayList<>();
			
			while(dtInicial.compareTo(dtFinal) <= 0) {	
				labels.add(Util.formatDate(dtInicial, "MMM/yyyy"));
				dtInicial.add(Calendar.MONTH, 1);			
			} 
			
			ResultSetMap resultados = new ResultSetMap();
			
			for (int tp : tipos) {
				
				HashMap<String, Object> register = new HashMap<String, Object>();
				register.put("labels", labels);
				
				dtInicial = (GregorianCalendar)dtBase.clone();
				
				if(tp != -1)
					register.put("tipo", EquipamentoServices.tiposEquipamento[tp]);
				else
					register.put("tipo", "Outros registros");
				
				int[] data = new int[labels.size()];
				int i = 0;
				
				while(dtInicial.compareTo(dtFinal) <= 0) {	
					GregorianCalendar dtAux = (GregorianCalendar)dtInicial.clone();
					dtAux.add(Calendar.MONTH, 1);					

					PreparedStatement ps;
					if(tp != -1) {
						ps = connect.prepareStatement(
								" SELECT COUNT(A.*) AS qtd"
										+ " FROM mob_ait A"
										+ " JOIN grl_equipamento B ON (A.cd_equipamento = B.cd_equipamento)"
										+ " WHERE A.dt_infracao BETWEEN ? AND ?"
										+ " AND B.tp_equipamento = ?");			
	
						ps.setTimestamp(1, Util.convCalendarToTimestamp(dtInicial));
						ps.setTimestamp(2, Util.convCalendarToTimestamp(dtAux));
						ps.setInt(3, tp);		
					}
					else {
						ps = connect.prepareStatement(
								" SELECT COUNT(A.*) AS qtd"
										+ " FROM mob_ait A"
										+ " WHERE A.dt_infracao BETWEEN ? AND ?"
										+ " AND A.cd_equipamento is null");			
	
						ps.setTimestamp(1, Util.convCalendarToTimestamp(dtInicial));
						ps.setTimestamp(2, Util.convCalendarToTimestamp(dtAux));
					}

					ResultSetMap rsmAux = new ResultSetMap(ps.executeQuery());
					if(rsmAux.next()) {
						data[i] = rsmAux.getInt("qtd");
					} else {
						data[i] = 0;
					}

					i++;
					dtInicial.add(Calendar.MONTH, 1);
				}

				register.put("data", data);				
				resultados.addRegister(register);
			}			
			
			return resultados;
		}
		catch(Exception e) {
			System.out.println("Erro! AitServices.statsProducaoMensal.");
			e.printStackTrace(System.out);
			return null;
		}
		finally {
			Conexao.desconectar(connect);
		}
	}
	
	private static ResultSetMap statsProducaoMensalOld(GregorianCalendar dtInicial, GregorianCalendar dtFinal) {
		return statsProducaoMensalOld(dtInicial, dtFinal, null);
	}
	
	private static ResultSetMap statsProducaoMensalOld(GregorianCalendar dtInicial, GregorianCalendar dtFinal, Connection connect) {
		try {
			connect = connect==null ? Conexao.conectar() : connect;
			
			GregorianCalendar dtBase = (GregorianCalendar)dtInicial.clone();
			
			//Lista de equipamentos
			int[] tipos = {EquipamentoServices.TALONARIO_ELETRONICO, EquipamentoServices.CAMERA, EquipamentoServices.RADAR_FIXO, -1};// -1 = OUTROS			
					
			ArrayList<String> labels = new ArrayList<>();
			
			while(dtInicial.compareTo(dtFinal) <= 0) {	
				labels.add(Util.formatDate(dtInicial, "MMM/yyyy"));
				dtInicial.add(Calendar.MONTH, 1);			
			} 
			
			ResultSetMap resultados = new ResultSetMap();
			
			for (int tp : tipos) {
				
				HashMap<String, Object> register = new HashMap<String, Object>();
				register.put("labels", labels);
				
				dtInicial = (GregorianCalendar)dtBase.clone();
				
				if(tp != -1)
					register.put("tipo", EquipamentoServices.tiposEquipamento[tp]);
				else
					register.put("tipo", "Outros registros");
				
				int[] data = new int[labels.size()];
				int i = 0;
				
				while(dtInicial.compareTo(dtFinal) <= 0) {	
					GregorianCalendar dtAux = (GregorianCalendar)dtInicial.clone();
					dtAux.add(Calendar.MONTH, 1);					

					PreparedStatement ps;
					if(tp != -1) {
						ps = connect.prepareStatement(
								" SELECT COUNT(A.*) AS qtd"
										+ " FROM AIT A"
										+ " JOIN grl_equipamento B ON (A.cd_equipamento = B.cd_equipamento)"
										+ " WHERE A.dt_infracao BETWEEN ? AND ?"
										+ " AND B.tp_equipamento = ?");			
	
						ps.setTimestamp(1, Util.convCalendarToTimestamp(dtInicial));
						ps.setTimestamp(2, Util.convCalendarToTimestamp(dtAux));
						ps.setInt(3, tp);		
					}
					else {
						ps = connect.prepareStatement(
								" SELECT COUNT(A.*) AS qtd"
										+ " FROM AIT A"
										+ " WHERE A.dt_infracao BETWEEN ? AND ?"
										+ " AND A.cd_equipamento is null");			
	
						ps.setTimestamp(1, Util.convCalendarToTimestamp(dtInicial));
						ps.setTimestamp(2, Util.convCalendarToTimestamp(dtAux));
					}

					ResultSetMap rsmAux = new ResultSetMap(ps.executeQuery());
					if(rsmAux.next()) {
						data[i] = rsmAux.getInt("qtd");
					} else {
						data[i] = 0;
					}

					i++;
					dtInicial.add(Calendar.MONTH, 1);
				}

				register.put("data", data);				
				resultados.addRegister(register);
			}			
			
			return resultados;
		}
		catch(Exception e) {
			System.out.println("Erro! AitServices.statsProducaoMensalOld.");
			e.printStackTrace(System.out);
			return null;
		}
		finally {
			Conexao.desconectar(connect);
		}
	}
	
	public static ResultSetMap statsProducaoDiario(GregorianCalendar dtInicial, GregorianCalendar dtFinal) {
		return statsProducaoDiario(dtInicial, dtFinal, null);
	}
	
	public static ResultSetMap statsProducaoDiario(GregorianCalendar dtInicial, GregorianCalendar dtFinal, Connection connect) {
		
		/*
		 * TL;DR
		 * Alguns modulos novos do eTransito Angular precisaram ser utilizados na base antiga (ex: eTransito BI). 
		 * Manter este desvio de fluxo no momento
		 */
		if(Util.isStrBaseAntiga())
			return statsProducaoDiarioOld(dtInicial, dtFinal, connect); 
		
		try {
			connect = connect==null ? Conexao.conectar() : connect;
			
			GregorianCalendar dtBase = (GregorianCalendar)dtInicial.clone();
			
			//Lista de equipamentos
			int[] tipos = {EquipamentoServices.TALONARIO_ELETRONICO, EquipamentoServices.CAMERA, EquipamentoServices.RADAR_FIXO, -1};// -1 = OUTROS			
					
			ArrayList<String> labels = new ArrayList<>();
			
			while(dtInicial.compareTo(dtFinal) <= 0) {	
				labels.add(Util.formatDate(dtInicial, "dd/MM"));
				dtInicial.add(Calendar.DATE, 1);			
			} 
			
			ResultSetMap resultados = new ResultSetMap();
			
			for (int tp : tipos) {
				
				HashMap<String, Object> register = new HashMap<String, Object>();
				register.put("labels", labels);
				
				dtInicial = (GregorianCalendar)dtBase.clone();
				
				if(tp != -1)
					register.put("tipo", EquipamentoServices.tiposEquipamento[tp]);
				else
					register.put("tipo", "Outros registros");
				
				int[] data = new int[labels.size()];
				int i = 0;
				
				while(dtInicial.compareTo(dtFinal) <= 0) {	
					GregorianCalendar dtAux = (GregorianCalendar)dtInicial.clone();
					dtAux.add(Calendar.DATE, 1);					

					PreparedStatement ps;
					if(tp != -1) {
						ps = connect.prepareStatement(
								" SELECT COUNT(A.*) AS qtd"
										+ " FROM mob_ait A"
										+ " JOIN grl_equipamento B ON (A.cd_equipamento = B.cd_equipamento)"
										+ " WHERE A.dt_infracao BETWEEN ? AND ?"
										+ " AND B.tp_equipamento = ?");			
	
						ps.setTimestamp(1, Util.convCalendarToTimestamp(dtInicial));
						ps.setTimestamp(2, Util.convCalendarToTimestamp(dtAux));
						ps.setInt(3, tp);		
					}
					else {
						ps = connect.prepareStatement(
								" SELECT COUNT(A.*) AS qtd"
										+ " FROM mob_ait A"
										+ " WHERE A.dt_infracao BETWEEN ? AND ?"
										+ " AND A.cd_equipamento is null");			
	
						ps.setTimestamp(1, Util.convCalendarToTimestamp(dtInicial));
						ps.setTimestamp(2, Util.convCalendarToTimestamp(dtAux));
					}

					ResultSetMap rsmAux = new ResultSetMap(ps.executeQuery());
					if(rsmAux.next()) {
						data[i] = rsmAux.getInt("qtd");
					} else {
						data[i] = 0;
					}

					i++;
					dtInicial.add(Calendar.DATE, 1);
				}

				register.put("data", data);				
				resultados.addRegister(register);
			}			
			
			return resultados;
		}
		catch(Exception e) {
			System.out.println("Erro! AitServices.statsAitEquipamentoAgrupadoPeriodo.");
			e.printStackTrace(System.out);
			return null;
		}
		finally {
			Conexao.desconectar(connect);
		}
	}
	
	private static ResultSetMap statsProducaoDiarioOld(GregorianCalendar dtInicial, GregorianCalendar dtFinal) {
		return statsProducaoDiarioOld(dtInicial, dtFinal, null);
	}
	
	private static ResultSetMap statsProducaoDiarioOld(GregorianCalendar dtInicial, GregorianCalendar dtFinal, Connection connect) {
		try {
			connect = connect==null ? Conexao.conectar() : connect;
			
			GregorianCalendar dtBase = (GregorianCalendar)dtInicial.clone();
			
			//Lista de equipamentos
			int[] tipos = {EquipamentoServices.TALONARIO_ELETRONICO, EquipamentoServices.CAMERA, EquipamentoServices.RADAR_FIXO, -1};// -1 = OUTROS			
					
			ArrayList<String> labels = new ArrayList<>();
			
			while(dtInicial.compareTo(dtFinal) <= 0) {	
				labels.add(Util.formatDate(dtInicial, "dd/MM"));
				dtInicial.add(Calendar.DATE, 1);			
			} 
			
			ResultSetMap resultados = new ResultSetMap();
			
			for (int tp : tipos) {
				
				HashMap<String, Object> register = new HashMap<String, Object>();
				register.put("labels", labels);
				
				dtInicial = (GregorianCalendar)dtBase.clone();
				
				if(tp != -1)
					register.put("tipo", EquipamentoServices.tiposEquipamento[tp]);
				else
					register.put("tipo", "Outros registros");
				
				int[] data = new int[labels.size()];
				int i = 0;
				
				while(dtInicial.compareTo(dtFinal) <= 0) {	
					GregorianCalendar dtAux = (GregorianCalendar)dtInicial.clone();
					dtAux.add(Calendar.DATE, 1);					

					PreparedStatement ps;
					if(tp != -1) {
						ps = connect.prepareStatement(
								" SELECT COUNT(A.*) AS qtd"
										+ " FROM AIT A"
										+ " JOIN grl_equipamento B ON (A.cd_equipamento = B.cd_equipamento)"
										+ " WHERE A.dt_infracao BETWEEN ? AND ?"
										+ " AND B.tp_equipamento = ?");			
	
						ps.setTimestamp(1, Util.convCalendarToTimestamp(dtInicial));
						ps.setTimestamp(2, Util.convCalendarToTimestamp(dtAux));
						ps.setInt(3, tp);		
					}
					else {
						ps = connect.prepareStatement(
								" SELECT COUNT(A.*) AS qtd"
										+ " FROM AIT A"
										+ " WHERE A.dt_infracao BETWEEN ? AND ?"
										+ " AND A.cd_equipamento is null");			
	
						ps.setTimestamp(1, Util.convCalendarToTimestamp(dtInicial));
						ps.setTimestamp(2, Util.convCalendarToTimestamp(dtAux));
					}

					ResultSetMap rsmAux = new ResultSetMap(ps.executeQuery());
					if(rsmAux.next()) {
						data[i] = rsmAux.getInt("qtd");
					} else {
						data[i] = 0;
					}

					i++;
					dtInicial.add(Calendar.DATE, 1);
				}

				register.put("data", data);				
				resultados.addRegister(register);
			}			
			
			return resultados;
		}
		catch(Exception e) {
			System.out.println("Erro! AitServices.statsProducaoDiarioOld.");
			e.printStackTrace(System.out);
			return null;
		}
		finally {
			Conexao.desconectar(connect);
		}
	}
	
	@Deprecated
	public static ResultSetMap statsAitNatureza() {		
		GregorianCalendar hoje = new GregorianCalendar();
		GregorianCalendar dtInicial = new GregorianCalendar(hoje.get(Calendar.YEAR)-1, Calendar.JANUARY, 1, 0, 0, 0);
		GregorianCalendar dtFinal = new GregorianCalendar(hoje.get(Calendar.YEAR), Calendar.JANUARY, 31, 0, 0, 0);
		return statsAitNatureza(dtInicial, dtFinal);		
	}
	@Deprecated
	public static ResultSetMap statsAitNatureza(GregorianCalendar dtInicial, GregorianCalendar dtFinal) {
		return statsAitNatureza(dtInicial, dtFinal, null);		
	}
	@Deprecated
	public static ResultSetMap statsAitNatureza(GregorianCalendar dtInicial, GregorianCalendar dtFinal, Connection connect) {
		try {
			connect = connect==null ? Conexao.conectar() : connect;
			
			boolean lgBaseAntiga = Util.getConfManager().getProps().getProperty("STR_BASE_ANTIGA").equals("1");
			
			PreparedStatement ps = connect.prepareStatement(
					" SELECT COUNT(*) AS qtd, B.nm_natureza "
					+ " FROM ait A, infracao B "
					+ " WHERE A.cod_infracao = B.cod_infracao "
					+ " AND B.nm_natureza IS NOT NULL "
					+ " AND A.st_ait = ? "
					+ " AND A.dt_infracao BETWEEN ? AND ?"
					+ "GROUP BY B.nm_natureza");
			
			if(!lgBaseAntiga) {
				ps = connect.prepareStatement(
						" SELECT COUNT(*) AS qtd, B.nm_natureza "
						+ " FROM mob_ait A, mob_infracao B "
						+ " WHERE A.cd_infracao = B.cd_infracao "
						+ " AND B.nm_natureza IS NOT NULL "
						+ " AND A.st_ait = ? "
						+ " AND A.dt_infracao BETWEEN ? AND ?"
						+ "GROUP BY B.nm_natureza");
			}
			
			ps.setInt(1, AitServices.ST_CONFIRMADO);
			ps.setTimestamp(2, Util.convCalendarToTimestamp(dtInicial));
			ps.setTimestamp(3, Util.convCalendarToTimestamp(dtFinal));
			
			ResultSetMap rsm = new ResultSetMap(ps.executeQuery());
			
			return rsm;
		}
		catch(Exception e) {
			System.out.println("Erro! AitServices.statsAitNatureza.");
			e.printStackTrace(System.out);
			return null;
		}
		finally {
			Conexao.desconectar(connect);
		}
	}
	
	public static ResultSetMap statsRankingNatureza(GregorianCalendar dtInicial, GregorianCalendar dtFinal) {
		return statsRankingNatureza(dtInicial, dtFinal, null);		
	}

	public static ResultSetMap statsRankingNatureza(GregorianCalendar dtInicial, GregorianCalendar dtFinal, Connection connect) {
		
		/*
		 * TL;DR
		 * Alguns modulos novos do eTransito Angular precisaram ser utilizados na base antiga (ex: eTransito BI). 
		 * Manter este desvio de fluxo no momento
		 */
		if(Util.isStrBaseAntiga())
			return statsRankingNaturezaOld(dtInicial, dtFinal, connect);
		
		try {
			connect = connect==null ? Conexao.conectar() : connect;
			
			PreparedStatement ps = connect.prepareStatement(
					" SELECT COUNT(*) AS quantidade, B.nm_natureza "
							+ " FROM mob_ait A, mob_infracao B "
							+ " WHERE A.cd_infracao = B.cd_infracao "
							+ " AND B.nm_natureza IS NOT NULL "
							//+ " AND A.st_ait = ? "
							+ " AND A.dt_infracao BETWEEN ? AND ?"
							+ "GROUP BY B.nm_natureza");
			
			//ps.setInt(1, ST_CONFIRMADO);
			ps.setTimestamp(1, Util.convCalendarToTimestamp(dtInicial));
			ps.setTimestamp(2, Util.convCalendarToTimestamp(dtFinal));
			
			ResultSetMap rsm = new ResultSetMap(ps.executeQuery());
			
			return rsm;
		}
		catch(Exception e) {
			System.out.println("Erro! AitServices.statsRankingNatureza.");
			e.printStackTrace(System.out);
			return null;
		}
		finally {
			Conexao.desconectar(connect);
		}
	}
	
	private static ResultSetMap statsRankingNaturezaOld(GregorianCalendar dtInicial, GregorianCalendar dtFinal, Connection connect) {
		try {
			connect = connect==null ? Conexao.conectar() : connect;
			
			PreparedStatement ps = connect.prepareStatement(
					" SELECT COUNT(*) AS quantidade, B.nm_natureza "
							+ " FROM AIT A, infracao B "
							+ " WHERE A.cod_infracao = B.cod_infracao "
							+ " AND B.nm_natureza IS NOT NULL "
							//+ " AND A.st_ait = ? "
							+ " AND A.dt_infracao BETWEEN ? AND ?"
							+ "GROUP BY B.nm_natureza");
			
			//ps.setInt(1, ST_CONFIRMADO);
			ps.setTimestamp(1, Util.convCalendarToTimestamp(dtInicial));
			ps.setTimestamp(2, Util.convCalendarToTimestamp(dtFinal));
			
			ResultSetMap rsm = new ResultSetMap(ps.executeQuery());
			
			return rsm;
		}
		catch(Exception e) {
			System.out.println("Erro! AitServices.statsRankingNaturezaOld.");
			e.printStackTrace(System.out);
			return null;
		}
		finally {
			Conexao.desconectar(connect);
		}
	}
	
	
	
	/*
	 * 
	 * ESTATÍSTICAS DE RADAR
	 * 
	 */
	 
	public static ResultSetMap statsInfracaoRadarVelocidade(GregorianCalendar dtInicial, GregorianCalendar dtFinal) {
		return statsInfracaoRadarVelocidade(dtInicial, dtFinal, null);		
	}

	public static ResultSetMap statsInfracaoRadarVelocidade(GregorianCalendar dtInicial, GregorianCalendar dtFinal, Connection connect) {
		
		/*
		 * TL;DR
		 * Alguns modulos novos do eTransito Angular precisaram ser utilizados na base antiga (ex: eTransito BI). 
		 * Manter este desvio de fluxo no momento
		 */
		if(Util.isStrBaseAntiga())
			return statsInfracaoRadarVelocidadeOld(dtInicial, dtFinal, connect);
		
		try {
			connect = connect==null ? Conexao.conectar() : connect;
			
			PreparedStatement ps = connect.prepareStatement(
					" SELECT C.id_equipamento, C.ds_local, " + 
					"    count(*) filter (where (100 - (CAST (vl_velocidade_permitida as FLOAT) / CAST (vl_velocidade_penalidade as FLOAT)* 100)) <= 20) as menor20, " + 
					"    count(*) filter (where (100 - (CAST (vl_velocidade_permitida as FLOAT) / CAST (vl_velocidade_penalidade as FLOAT)* 100)) > 20 AND " + 
					"	                        (100 - (CAST (vl_velocidade_permitida as FLOAT) / CAST (vl_velocidade_penalidade as FLOAT) * 100)) <= 50) as entre2050, " + 
					"	count(*) filter (where (100 - (CAST (vl_velocidade_permitida as FLOAT) / CAST (vl_velocidade_penalidade as FLOAT)* 100)) > 50) as maior50, " + 
					"    count(*) as total_velocidade " + 
					" FROM mob_ait A " + 
					" JOIN grl_equipamento C ON (A.cd_equipamento = C.cd_equipamento AND C.tp_equipamento = ?) " + 
					" WHERE A.dt_infracao >= ? AND A.dt_infracao <= ? " + 
					" GROUP BY C.cd_equipamento " + 
					" ORDER BY C.id_equipamento");
			
			ps.setInt(1, EquipamentoServices.RADAR_FIXO);
			ps.setTimestamp(2, Util.convCalendarToTimestamp(dtInicial));
			ps.setTimestamp(3, Util.convCalendarToTimestamp(dtFinal));
			
			ResultSetMap rsm = new ResultSetMap(ps.executeQuery());
			
			return rsm;
		}
		catch(Exception e) {
			System.out.println("Erro! AitServices.statsInfracaoRadarVelocidade");
			e.printStackTrace(System.out);
			return null;
		}
		finally {
			Conexao.desconectar(connect);
		}
	}
	
	public static ResultSetMap statsInfracaoRadarVelocidadeOld(GregorianCalendar dtInicial, GregorianCalendar dtFinal, Connection connect) {
		
		try {
			connect = connect==null ? Conexao.conectar() : connect;
			
			PreparedStatement ps = connect.prepareStatement(
					" SELECT C.id_equipamento, C.ds_local, " + 
					"    count(*) filter (where (100 - (CAST (vl_velocidade_permitida as FLOAT) / CAST (vl_velocidade_penalidade as FLOAT)* 100)) <= 20) as menor20, " + 
					"    count(*) filter (where (100 - (CAST (vl_velocidade_permitida as FLOAT) / CAST (vl_velocidade_penalidade as FLOAT)* 100)) > 20 AND " + 
					"	                        (100 - (CAST (vl_velocidade_permitida as FLOAT) / CAST (vl_velocidade_penalidade as FLOAT) * 100)) <= 50) as entre2050, " + 
					"	count(*) filter (where (100 - (CAST (vl_velocidade_permitida as FLOAT) / CAST (vl_velocidade_penalidade as FLOAT)* 100)) > 50) as maior50, " + 
					"    count(*) as total_velocidade " + 
					" FROM AIT A " + 
					" JOIN grl_equipamento C ON (A.cd_equipamento = C.cd_equipamento AND C.tp_equipamento = ?) " + 
					" WHERE A.dt_infracao >= ? AND A.dt_infracao <= ? " + 
					" GROUP BY C.cd_equipamento " + 
					" ORDER BY C.id_equipamento");
			
			ps.setInt(1, EquipamentoServices.RADAR_FIXO);
			ps.setTimestamp(2, Util.convCalendarToTimestamp(dtInicial));
			ps.setTimestamp(3, Util.convCalendarToTimestamp(dtFinal));
			
			ResultSetMap rsm = new ResultSetMap(ps.executeQuery());
			return rsm;
		}
		catch(Exception e) {
			System.out.println("Erro! AitServices.statsInfracaoRadarVelocidadeOld");
			e.printStackTrace(System.out);
			return null;
		}
		finally {
			Conexao.desconectar(connect);
		}
	}
	

	public static ResultSetMap statsInfracaoRadarFaixa(GregorianCalendar dtInicial, GregorianCalendar dtFinal) {
		return statsInfracaoRadarFaixa(dtInicial, dtFinal, null);		
	}

	public static ResultSetMap statsInfracaoRadarFaixa(GregorianCalendar dtInicial, GregorianCalendar dtFinal, Connection connect) {
		
		/*
		 * TL;DR
		 * Alguns modulos novos do eTransito Angular precisaram ser utilizados na base antiga (ex: eTransito BI). 
		 * Manter este desvio de fluxo no momento
		 */
		if(Util.isStrBaseAntiga())
			return statsInfracaoRadarFaixaOld(dtInicial, dtFinal, connect);
		
		try {
			connect = connect==null ? Conexao.conectar() : connect;
			
			PreparedStatement ps = connect.prepareStatement(
					"select C.id_equipamento, C.ds_local, B1.nm_sentido_rodovia, " + 
					"    count(*) filter (where B1.nr_pista = 1 OR B1.nr_pista = 3) as pista_esquerda, " + 
					"    count(*) filter (where B1.nr_pista = 2 OR B1.nr_pista = 4) as pista_direita, " + 
					"    count(*) as total_sentido " + 
					"from mob_ait A " + 
					"join mob_ait_evento B ON (A.cd_ait = B.cd_ait) " + 
					"join mob_evento_equipamento B1 ON (B.cd_evento = B1.cd_evento) " + 
					"join grl_equipamento C ON (A.cd_equipamento = C.cd_equipamento AND C.tp_equipamento = ?) " + 
					"where A.dt_infracao >= ? and A.dt_infracao <= ? " + 
					"group by C.cd_equipamento, B1.nm_sentido_rodovia " + 
					"order by C.id_equipamento");
			
			ps.setInt(1, EquipamentoServices.RADAR_FIXO);
			ps.setTimestamp(2, Util.convCalendarToTimestamp(dtInicial));
			ps.setTimestamp(3, Util.convCalendarToTimestamp(dtFinal));
			
			ResultSetMap rsm = new ResultSetMap(ps.executeQuery());
			
			return rsm;
		}
		catch(Exception e) {
			System.out.println("Erro! AitServices.statsInfracaoRadarFaixa");
			e.printStackTrace(System.out);
			return null;
		}
		finally {
			Conexao.desconectar(connect);
		}
	}
	
	public static ResultSetMap statsInfracaoRadarFaixaOld(GregorianCalendar dtInicial, GregorianCalendar dtFinal, Connection connect) {
			
		try {
			connect = connect==null ? Conexao.conectar() : connect;
			
			PreparedStatement ps = connect.prepareStatement(
					"select C.id_equipamento, C.ds_local, B1.nm_sentido_rodovia, " + 
					"    count(*) filter (where B1.nr_pista = 1 OR B1.nr_pista = 3) as pista_esquerda, " + 
					"    count(*) filter (where B1.nr_pista = 2 OR B1.nr_pista = 4) as pista_direita, " + 
					"    count(*) as total_sentido " + 
					"from AIT A " + 
					"join mob_ait_evento B ON (A.codigo_ait = B.cd_ait) " + 
					"join mob_evento_equipamento B1 ON (B.cd_evento = B1.cd_evento) " + 
					"join grl_equipamento C ON (A.cd_equipamento = C.cd_equipamento AND C.tp_equipamento = ?) " + 
					"where A.dt_infracao >= ? and A.dt_infracao <= ? " + 
					"group by C.cd_equipamento, B1.nm_sentido_rodovia " + 
					"order by C.id_equipamento");
			
			ps.setInt(1, EquipamentoServices.RADAR_FIXO);
			ps.setTimestamp(2, Util.convCalendarToTimestamp(dtInicial));
			ps.setTimestamp(3, Util.convCalendarToTimestamp(dtFinal));
			
			ResultSetMap rsm = new ResultSetMap(ps.executeQuery());
			
			return rsm;
		}
		catch(Exception e) {
			System.out.println("Erro! AitServices.statsInfracaoRadarFaixaOld");
			e.printStackTrace(System.out);
			return null;
		}
		finally {
			Conexao.desconectar(connect);
		}
	}
	


	public static ResultSetMap statsInfracaoRadarEspecie(GregorianCalendar dtInicial, GregorianCalendar dtFinal) {
		return statsInfracaoRadarEspecie(dtInicial, dtFinal, null);		
	}

	public static ResultSetMap statsInfracaoRadarEspecie(GregorianCalendar dtInicial, GregorianCalendar dtFinal, Connection connect) {
		
		/*
		 * TL;DR
		 * Alguns modulos novos do eTransito Angular precisaram ser utilizados na base antiga (ex: eTransito BI). 
		 * Manter este desvio de fluxo no momento
		 */
		if(Util.isStrBaseAntiga())
			return statsInfracaoRadarEspecieOld(dtInicial, dtFinal, connect);
		
		try {
			connect = connect==null ? Conexao.conectar() : connect;
			
			PreparedStatement ps = connect.prepareStatement(
					" select B.ds_especie, count(*) as total_especie " + 
					" from mob_ait A " + 
					" join fta_especie_veiculo B ON (B.cd_especie = A.cd_especie) " + 
					" join grl_equipamento C ON (A.cd_equipamento = C.cd_equipamento AND C.tp_equipamento = ?) " + 
					" where A.dt_infracao >= ? and A.dt_infracao <= ? " + 
					" group by B.cd_especie " + 
					" order by B.ds_especie");
			
			ps.setInt(1, EquipamentoServices.RADAR_FIXO);
			ps.setTimestamp(2, Util.convCalendarToTimestamp(dtInicial));
			ps.setTimestamp(3, Util.convCalendarToTimestamp(dtFinal));
			
			ResultSetMap rsm = new ResultSetMap(ps.executeQuery());
			
			return rsm;
		}
		catch(Exception e) {
			System.out.println("Erro! AitServices.statsInfracaoRadarEspecie");
			e.printStackTrace(System.out);
			return null;
		}
		finally {
			Conexao.desconectar(connect);
		}
	}
	
	public static ResultSetMap statsInfracaoRadarEspecieOld(GregorianCalendar dtInicial, GregorianCalendar dtFinal, Connection connect) {
		
		try {
			connect = connect==null ? Conexao.conectar() : connect;
			
			PreparedStatement ps = connect.prepareStatement(
					" select B.ds_especie, count(*) as total_especie " + 
					" from AIT A " + 
					" join especie_veiculo B ON (B.cod_especie = A.cod_especie) " + 
					" join grl_equipamento C ON (A.cd_equipamento = C.cd_equipamento AND C.tp_equipamento = ?) " + 
					" where A.dt_infracao >= ? and A.dt_infracao <= ? " + 
					" group by B.cod_especie " + 
					" order by B.ds_especie");
			
			ps.setInt(1, EquipamentoServices.RADAR_FIXO);
			ps.setTimestamp(2, Util.convCalendarToTimestamp(dtInicial));
			ps.setTimestamp(3, Util.convCalendarToTimestamp(dtFinal));
			
			ResultSetMap rsm = new ResultSetMap(ps.executeQuery());
			
			return rsm;
		}
		catch(Exception e) {
			System.out.println("Erro! AitServices.statsInfracaoRadarEspecieOld");
			e.printStackTrace(System.out);
			return null;
		}
		finally {
			Conexao.desconectar(connect);
		}
	}
	
	
	/*
	 * 
	 * ESTATÍSTICAS DE TALONÁRIO ELETRÔNICO
	 * 
	 */
	 
	public static ResultSetMap statsInfracaoTalonarioAgente(GregorianCalendar dtInicial, GregorianCalendar dtFinal) {
		return statsInfracaoTalonarioAgente(dtInicial, dtFinal, null);		
	}

	public static ResultSetMap statsInfracaoTalonarioAgente(GregorianCalendar dtInicial, GregorianCalendar dtFinal, Connection connect) {
		
		/*
		 * TL;DR
		 * Alguns modulos novos do eTransito Angular precisaram ser utilizados na base antiga (ex: eTransito BI). 
		 * Manter este desvio de fluxo no momento
		 */
		if(Util.isStrBaseAntiga())
			return statsInfracaoTalonarioAgenteOld(dtInicial, dtFinal, connect);
		
		try {
			connect = connect==null ? Conexao.conectar() : connect;
			
			PreparedStatement ps = connect.prepareStatement(
					" with top as "+
					"    (select B.nm_agente, B.nr_matricula, count(*) as total_agente "+
					"		from mob_ait A "+
					"		join mob_agente B ON (B.cd_agente = A.cd_agente) "+
					"		join grl_equipamento C ON (A.cd_equipamento = C.cd_equipamento AND C.tp_equipamento = ?) "+
					"		where A.dt_infracao >= ? and A.dt_infracao <= ? "+
					"		group by B.nm_agente, B.nr_matricula "+
					"		order by total_agente DESC, B.nm_agente "+
					"    limit 5) "+
					" select * "+
					" from top "+
					" union all "+
					" select 'OUTROS' as nm_agente, '-' as nr_matricula, ( "+
					"			select count(*) as total_agente from mob_ait A "+
					"		join mob_agente B ON (B.cd_agente = A.cd_agente) "+
					"		join grl_equipamento C ON (A.cd_equipamento = C.cd_equipamento AND C.tp_equipamento = ?) "+
					"		where A.dt_infracao >= ? and A.dt_infracao <= ? "+
					"		  and B.nm_agente not in (select nm_agente from top)) as total_agente");
			
			ps.setInt(1, EquipamentoServices.TALONARIO_ELETRONICO);
			ps.setTimestamp(2, Util.convCalendarToTimestamp(dtInicial));
			ps.setTimestamp(3, Util.convCalendarToTimestamp(dtFinal));
			
			ps.setInt(4, EquipamentoServices.TALONARIO_ELETRONICO);
			ps.setTimestamp(5, Util.convCalendarToTimestamp(dtInicial));
			ps.setTimestamp(6, Util.convCalendarToTimestamp(dtFinal));
						
			ResultSetMap rsm = new ResultSetMap(ps.executeQuery());
			
			return rsm;
		}
		catch(Exception e) {
			System.out.println("Erro! AitServices.statsInfracaoTalonarioAgente");
			e.printStackTrace(System.out);
			return null;
		}
		finally {
			Conexao.desconectar(connect);
		}
	}
	
	public static ResultSetMap statsInfracaoTalonarioAgenteOld(GregorianCalendar dtInicial, GregorianCalendar dtFinal, Connection connect) {

		try {
			connect = connect==null ? Conexao.conectar() : connect;
			
			PreparedStatement ps = connect.prepareStatement(
					" with top as "+
					"    (select B.nm_agente, B.nr_matricula, count(*) as total_agente "+
					"		from AIT A "+
					"		join agente B ON (B.cod_agente = A.cod_agente) "+
					"		join grl_equipamento C ON (A.cd_equipamento = C.cd_equipamento AND C.tp_equipamento = ?) "+
					"		where A.dt_infracao >= ? and A.dt_infracao <= ? "+
					"		group by B.nm_agente, B.nr_matricula "+
					"		order by total_agente DESC, B.nm_agente "+
					"    limit 5) "+
					" select * "+
					" from top "+
					" union all "+
					" select 'OUTROS' as nm_agente, '-' as nr_matricula, ( "+
					"			select count(*) as total_agente from AIT A "+
					"		join agente B ON (B.cod_agente = A.cod_agente) "+
					"		join grl_equipamento C ON (A.cd_equipamento = C.cd_equipamento AND C.tp_equipamento = ?) "+
					"		where A.dt_infracao >= ? and A.dt_infracao <= ? "+
					"		  and B.nm_agente not in (select nm_agente from top)) as total_agente");
			
			ps.setInt(1, EquipamentoServices.TALONARIO_ELETRONICO);
			ps.setTimestamp(2, Util.convCalendarToTimestamp(dtInicial));
			ps.setTimestamp(3, Util.convCalendarToTimestamp(dtFinal));
			
			ps.setInt(4, EquipamentoServices.TALONARIO_ELETRONICO);
			ps.setTimestamp(5, Util.convCalendarToTimestamp(dtInicial));
			ps.setTimestamp(6, Util.convCalendarToTimestamp(dtFinal));
						
			
			ResultSetMap rsm = new ResultSetMap(ps.executeQuery());
			
			return rsm;
		}
		catch(Exception e) {
			System.out.println("Erro! AitServices.statsInfracaoTalonarioAgenteOld");
			e.printStackTrace(System.out);
			return null;
		}
		finally {
			Conexao.desconectar(connect);
		}
	}
	
	
	public static ResultSetMap statsInfracaoTalonarioCidade(GregorianCalendar dtInicial, GregorianCalendar dtFinal) {
		return statsInfracaoTalonarioCidade(dtInicial, dtFinal, null);		
	}

	public static ResultSetMap statsInfracaoTalonarioCidade(GregorianCalendar dtInicial, GregorianCalendar dtFinal, Connection connect) {
		
		/*
		 * TL;DR
		 * Alguns modulos novos do eTransito Angular precisaram ser utilizados na base antiga (ex: eTransito BI). 
		 * Manter este desvio de fluxo no momento
		 */
		if(Util.isStrBaseAntiga())
			return statsInfracaoTalonarioCidadeOld(dtInicial, dtFinal, connect);
		
		try {
			connect = connect==null ? Conexao.conectar() : connect;
			
			PreparedStatement ps = connect.prepareStatement(
					" with top as "+
					" (select B.nm_cidade, count(*) as total_cidade "+
					" 		from mob_ait A "+
					" 		join grl_cidade B ON (B.cd_cidade = A.cd_cidade) "+
					" 		join grl_equipamento C ON (A.cd_equipamento = C.cd_equipamento AND C.tp_equipamento = ?) "+
					" 		where A.dt_infracao >= ? and A.dt_infracao <= ? "+
					" 		group by B.nm_cidade "+
					" 		order by total_cidade DESC, B.nm_cidade "+
					"     limit 5) "+
					" select * "+
					" from top "+
					" union all "+
					" select 'OUTROS' as nm_cidade, ( "+
					" 			select count(*) as total_cidade from mob_ait A "+
					" 		join grl_cidade B ON (B.cd_cidade = A.cd_cidade) "+
					" 		join grl_equipamento C ON (A.cd_equipamento = C.cd_equipamento AND C.tp_equipamento = ?) "+
					" 		where A.dt_infracao >= ? and A.dt_infracao <= ? "+
					" 		  and B.nm_cidade not in (select nm_cidade from top)) as total_cidade");
			
			ps.setInt(1, EquipamentoServices.TALONARIO_ELETRONICO);
			ps.setTimestamp(2, Util.convCalendarToTimestamp(dtInicial));
			ps.setTimestamp(3, Util.convCalendarToTimestamp(dtFinal));
			
			ps.setInt(4, EquipamentoServices.TALONARIO_ELETRONICO);
			ps.setTimestamp(5, Util.convCalendarToTimestamp(dtInicial));
			ps.setTimestamp(6, Util.convCalendarToTimestamp(dtFinal));
						
			ResultSetMap rsm = new ResultSetMap(ps.executeQuery());
			
			return rsm;
		}
		catch(Exception e) {
			System.out.println("Erro! AitServices.statsInfracaoTalonarioCidade");
			e.printStackTrace(System.out);
			return null;
		}
		finally {
			Conexao.desconectar(connect);
		}
	}
	

	public static ResultSetMap statsInfracaoTalonarioCidadeOld(GregorianCalendar dtInicial, GregorianCalendar dtFinal, Connection connect) {
		
		try {
			connect = connect==null ? Conexao.conectar() : connect;
			
			PreparedStatement ps = connect.prepareStatement(
					" with top as "+
					" (select B.nm_municipio as nm_cidade, count(*) as total_cidade "+
					" 		from AIT A "+
					" 		join municipio B ON (B.cod_municipio = A.cod_municipio) "+
					" 		join grl_equipamento C ON (A.cd_equipamento = C.cd_equipamento AND C.tp_equipamento = ?) "+
					" 		where A.dt_infracao >= ? and A.dt_infracao <= ? "+
					" 		group by B.nm_municipio "+
					" 		order by total_cidade DESC, B.nm_municipio "+
					"     limit 5) "+
					" select * "+
					" from top "+
					" union all "+
					" select 'OUTROS' as nm_cidade, ( "+
					" 			select count(*) as total_cidade from AIT A "+
					" 		join municipio B ON (B.cod_municipio = A.cod_municipio) "+
					" 		join grl_equipamento C ON (A.cd_equipamento = C.cd_equipamento AND C.tp_equipamento = ?) "+
					" 		where A.dt_infracao >= ? and A.dt_infracao <= ? "+
					" 		  and B.nm_municipio not in (select nm_cidade from top)) as total_cidade");
			
			ps.setInt(1, EquipamentoServices.TALONARIO_ELETRONICO);
			ps.setTimestamp(2, Util.convCalendarToTimestamp(dtInicial));
			ps.setTimestamp(3, Util.convCalendarToTimestamp(dtFinal));
			
			ps.setInt(4, EquipamentoServices.TALONARIO_ELETRONICO);
			ps.setTimestamp(5, Util.convCalendarToTimestamp(dtInicial));
			ps.setTimestamp(6, Util.convCalendarToTimestamp(dtFinal));
						
			ResultSetMap rsm = new ResultSetMap(ps.executeQuery());
			
			return rsm;
		}
		catch(Exception e) {
			System.out.println("Erro! AitServices.statsInfracaoTalonarioCidadeOld");
			e.printStackTrace(System.out);
			return null;
		}
		finally {
			Conexao.desconectar(connect);
		}
	}
	
	  
	public static ResultSetMap statsInfracaoTalonarioEspecie(GregorianCalendar dtInicial, GregorianCalendar dtFinal) {
		return statsInfracaoTalonarioEspecie(dtInicial, dtFinal, null);		
	}

	public static ResultSetMap statsInfracaoTalonarioEspecie(GregorianCalendar dtInicial, GregorianCalendar dtFinal, Connection connect) {
		
		/*
		 * TL;DR
		 * Alguns modulos novos do eTransito Angular precisaram ser utilizados na base antiga (ex: eTransito BI). 
		 * Manter este desvio de fluxo no momento
		 */
		if(Util.isStrBaseAntiga())
			return statsInfracaoTalonarioEspecieOld(dtInicial, dtFinal, connect);
		
		try {
			connect = connect==null ? Conexao.conectar() : connect;
			
			PreparedStatement ps = connect.prepareStatement(
					" select B.ds_especie, count(*) as total_especie " + 
					" from mob_ait A " + 
					" join fta_especie_veiculo B ON (B.cd_especie = A.cd_especie) " + 
					" join grl_equipamento C ON (A.cd_equipamento = C.cd_equipamento AND C.tp_equipamento = ?) " + 
					" where A.dt_infracao >= ? and A.dt_infracao <= ? " + 
					" group by B.cd_especie " + 
					" order by B.ds_especie");
			
			ps.setInt(1, EquipamentoServices.TALONARIO_ELETRONICO);
			ps.setTimestamp(2, Util.convCalendarToTimestamp(dtInicial));
			ps.setTimestamp(3, Util.convCalendarToTimestamp(dtFinal));
			
			ResultSetMap rsm = new ResultSetMap(ps.executeQuery());
			
			return rsm;
		}
		catch(Exception e) {
			System.out.println("Erro! AitServices.statsInfracaoRadarEspecie");
			e.printStackTrace(System.out);
			return null;
		}
		finally {
			Conexao.desconectar(connect);
		}
	}
	
	public static ResultSetMap statsInfracaoTalonarioEspecieOld(GregorianCalendar dtInicial, GregorianCalendar dtFinal, Connection connect) {
		
		try {
			connect = connect==null ? Conexao.conectar() : connect;
			
			PreparedStatement ps = connect.prepareStatement(
					" select B.ds_especie, count(*) as total_especie " + 
					" from AIT A " + 
					" join especie_veiculo B ON (B.cod_especie = A.cod_especie) " + 
					" join grl_equipamento C ON (A.cd_equipamento = C.cd_equipamento AND C.tp_equipamento = ?) " + 
					" where A.dt_infracao >= ? and A.dt_infracao <= ? " + 
					" group by B.cod_especie " + 
					" order by B.ds_especie");
			
			ps.setInt(1, EquipamentoServices.TALONARIO_ELETRONICO);
			ps.setTimestamp(2, Util.convCalendarToTimestamp(dtInicial));
			ps.setTimestamp(3, Util.convCalendarToTimestamp(dtFinal));
			
			ResultSetMap rsm = new ResultSetMap(ps.executeQuery());
			
			return rsm;
		}
		catch(Exception e) {
			System.out.println("Erro! AitServices.statsInfracaoTalonarioEspecieOld");
			e.printStackTrace(System.out);
			return null;
		}
		finally {
			Conexao.desconectar(connect);
		}
	}
	
	

	/*
	 * 
	 * ESTATÍSTICAS GEOLOCALIZADAS
	 * 
	 */
	 
	public static ResultSetMap statsInfracaoCluster(GregorianCalendar dtInicial, GregorianCalendar dtFinal) {
		return statsInfracaoCluster(dtInicial, dtFinal, null);		
	}

	public static ResultSetMap statsInfracaoCluster(GregorianCalendar dtInicial, GregorianCalendar dtFinal, Connection connect) {
		
		/*
		 * TL;DR
		 * Alguns modulos novos do eTransito Angular precisaram ser utilizados na base antiga (ex: eTransito BI). 
		 * Manter este desvio de fluxo no momento
		 */
		if(Util.isStrBaseAntiga())
			return statsInfracaoClusterOld(dtInicial, dtFinal, connect);
		
		try {
			connect = connect==null ? Conexao.conectar() : connect;
			
			PreparedStatement ps = connect.prepareStatement(
					" select A.id_ait, A.dt_infracao, A.cd_infracao, A.vl_latitude, A.vl_longitude "+ 
							" from mob_ait A  "+
							" where A.dt_infracao >= ? and A.dt_infracao <= ? "+
							"   and A.vl_latitude <> 0 "+
							"   and A.vl_longitude <> 0 "+
							" order by A.vl_latitude, A.vl_longitude, A.dt_infracao");
			
			ps.setTimestamp(1, Util.convCalendarToTimestamp(dtInicial));
			ps.setTimestamp(2, Util.convCalendarToTimestamp(dtFinal));
						
			ResultSetMap rsm = new ResultSetMap(ps.executeQuery());
			
			return rsm;
		}
		catch(Exception e) {
			System.out.println("Erro! AitServices.statsInfracaoCluster");
			e.printStackTrace(System.out);
			return null;
		}
		finally {
			Conexao.desconectar(connect);
		}
	}
	
	public static ResultSetMap statsInfracaoClusterOld(GregorianCalendar dtInicial, GregorianCalendar dtFinal, Connection connect) {

		try {
			connect = connect==null ? Conexao.conectar() : connect;
			
			PreparedStatement ps = connect.prepareStatement(
					" select A.nr_ait as id_ait, A.dt_infracao, A.cod_infracao, A.vl_latitude, A.vl_longitude "+ 
					" from AIT A  "+
					" where A.dt_infracao >= ? and A.dt_infracao <= ? "+
					"   and A.vl_latitude <> 0 "+
					"   and A.vl_longitude <> 0 "+
					" order by A.vl_latitude, A.vl_longitude, A.dt_infracao");
			
			ps.setTimestamp(1, Util.convCalendarToTimestamp(dtInicial));
			ps.setTimestamp(2, Util.convCalendarToTimestamp(dtFinal));
			
			ResultSetMap rsm = new ResultSetMap(ps.executeQuery());
			
			return rsm;
		}
		catch(Exception e) {
			System.out.println("Erro! AitServices.statsInfracaoClusterOld");
			e.printStackTrace(System.out);
			return null;
		}
		finally {
			Conexao.desconectar(connect);
		}
	}
	
	
	
	/**************************
	 * 
	 * DEPRECIADOS
	 *
	 **************************/
	 
	
	@Deprecated
	public static Result statsAitValor() {
		GregorianCalendar hoje = new GregorianCalendar();
		GregorianCalendar dtInicial = new GregorianCalendar(hoje.get(Calendar.YEAR)-1, Calendar.JANUARY, 1, 0, 0, 0);
		GregorianCalendar dtFinal = new GregorianCalendar(hoje.get(Calendar.YEAR), Calendar.JANUARY, 31, 0, 0, 0);
		return statsAitValor(dtInicial, dtFinal, null);		
	}
	@Deprecated
	public static Result statsAitValor(GregorianCalendar dtInicial, GregorianCalendar dtFinal) {
		return statsAitValor(dtInicial, dtFinal, null);		
	}
	@Deprecated
	public static Result statsAitValor(GregorianCalendar dtInicial, GregorianCalendar dtFinal, Connection connect) {
		try {
			connect = connect==null ? Conexao.conectar() : connect;			
			boolean lgBaseAntiga = Util.getConfManager().getProps().getProperty("STR_BASE_ANTIGA").equals("1");
			
			Result result = new Result(1, "");
			
			PreparedStatement ps = connect.prepareStatement(
					" SELECT SUM(vl_multa) AS vl_total"
					+ " FROM ait"
					+ " WHERE st_ait = ?"
					+ " AND dt_infracao BETWEEN ? AND ?");			
			if(!lgBaseAntiga) {
				ps = connect.prepareStatement(
						" SELECT SUM(vl_multa) AS vl_total"
						+ " FROM mob_ait"
						+ " WHERE st_ait = ?"
						+ " AND dt_infracao BETWEEN ? AND ?");
			}			
			ps.setInt(1, AitServices.ST_CONFIRMADO);
			ps.setTimestamp(2, Util.convCalendarToTimestamp(dtInicial));
			ps.setTimestamp(3, Util.convCalendarToTimestamp(dtFinal));
			
			// 1 =========================
			ResultSetMap rsm = new ResultSetMap(ps.executeQuery());
			if(rsm.next()) {				
				result.addObject("VL_TOTAL_AIT", rsm.getDouble("vl_total"));
				result.addObject("DS_VL_TOTAL_AIT", Util.formatCurrency(rsm.getDouble("vl_total")));
			}
			
			// 2 =========================
			ps = connect.prepareStatement(
					" SELECT SUM(vl_repasse) AS vl_repasse "
					+ " FROM ait_pagamento"
					+ " WHERE dt_pagamento BETWEEN ? AND ?");		
			
			if(!lgBaseAntiga) {
				ps = connect.prepareStatement(
						" SELECT SUM(vl_repasse) AS vl_repasse "
						+ " FROM mob_ait_pagamento"
						+ " WHERE dt_pagamento BETWEEN ? AND ?");
			}

			ps.setTimestamp(1, Util.convCalendarToTimestamp(dtInicial));
			ps.setTimestamp(2, Util.convCalendarToTimestamp(dtFinal));
			
			// 3 =========================
			rsm = new ResultSetMap(ps.executeQuery());
			if(rsm.next()) {				
				result.addObject("VL_REPASSE_AIT", rsm.getDouble("vl_repasse"));
				result.addObject("DS_VL_REPASSE_AIT", Util.formatCurrency(rsm.getDouble("vl_repasse")));
			}
			
			ps = connect.prepareStatement(
					"SELECT SUM(vl_pago) AS vl_pago "
					+ " FROM ait_pagamento"
					+ " WHERE dt_pagamento BETWEEN ? AND ?");			
			if(!lgBaseAntiga) {
				ps = connect.prepareStatement(
						"SELECT SUM(vl_pago) AS vl_pago "
						+ " FROM mob_ait_pagamento"
						+ " WHERE dt_pagamento BETWEEN ? AND ?");
			}

			ps.setTimestamp(1, Util.convCalendarToTimestamp(dtInicial));
			ps.setTimestamp(2, Util.convCalendarToTimestamp(dtFinal));
			
			rsm = new ResultSetMap(ps.executeQuery());
			if(rsm.next()) {				
				result.addObject("VL_PAGO_AIT", rsm.getDouble("vl_pago"));
				result.addObject("DS_VL_PAGO_AIT", Util.formatCurrency(rsm.getDouble("vl_pago")));
			}
			
			
			return result;
		}
		catch(Exception e) {
			System.out.println("Erro! AitServices.statsAitValor.");
			e.printStackTrace(System.out);
			return null;
		}
		finally {
			Conexao.desconectar(connect);
		}
	}
	
	@Deprecated
	public static ResultSetMap statsAitValorMensal() {
		GregorianCalendar hoje = new GregorianCalendar();
		GregorianCalendar dtInicial = new GregorianCalendar(hoje.get(Calendar.YEAR)-1, Calendar.JANUARY, 1, 0, 0, 0);
		GregorianCalendar dtFinal = new GregorianCalendar(hoje.get(Calendar.YEAR), Calendar.JANUARY, 31, 0, 0, 0);
		return statsAitValorMensal(dtInicial, dtFinal, null);		
	}
	@Deprecated
	public static ResultSetMap statsAitValorMensal(GregorianCalendar dtInicial, GregorianCalendar dtFinal) {
		return statsAitValorMensal(dtInicial, dtFinal, null);		
	}
	@Deprecated
	public static ResultSetMap statsAitValorMensal(GregorianCalendar dtInicial, GregorianCalendar dtFinal, Connection connect) {
		try {
			connect = connect==null ? Conexao.conectar() : connect;			
			boolean lgBaseAntiga = Util.getConfManager().getProps().getProperty("STR_BASE_ANTIGA").equals("1");			

			GregorianCalendar dtBase = (GregorianCalendar)dtInicial.clone();
			
			ResultSetMap rsmLabels = new ResultSetMap();
			while(dtInicial.compareTo(dtFinal) <= 0) {	
				
				HashMap<String, Object> register = new HashMap<String, Object>();
				register.put("NM_MES", Util.formatDate(dtInicial, "MMM/yyyy"));
				register.put("NR_MES", dtInicial.get(Calendar.MONTH));
				rsmLabels.addRegister(register);
				
				dtInicial.add(Calendar.MONTH, 1);
			} 
			rsmLabels.beforeFirst();	
			
			ResultSetMap rsm = new ResultSetMap();
			
			// EMITIDOS ================================================================================
			HashMap<String, Object> register = new HashMap<String, Object>();
			register.put("RSM_LABELS", rsmLabels);
			
			dtInicial = (GregorianCalendar)dtBase.clone();
			
			register.put("NM_LEGENDA", "Emitidos");
			
			Double[] data = new Double[rsmLabels.size()];
			int i = 0;
			
			while(dtInicial.compareTo(dtFinal) <= 0) {	
				GregorianCalendar dtAux = (GregorianCalendar)dtInicial.clone();
				dtAux.add(Calendar.MONTH, 1);					

				PreparedStatement ps = connect.prepareStatement(
						" SELECT SUM(vl_multa) AS vl_total"
								+ " FROM ait"
								+ " WHERE st_ait = ?"
								+ " AND dt_infracao BETWEEN ? AND ?");
				
				if(!lgBaseAntiga) {
					ps = connect.prepareStatement(
							" SELECT SUM(vl_multa) AS vl_total"
							+ " FROM mob_ait"
							+ " WHERE st_ait = ?"
							+ " AND dt_infracao BETWEEN ? AND ?");
				}				
				ps.setInt(1, AitServices.ST_CONFIRMADO);
				ps.setTimestamp(2, Util.convCalendarToTimestamp(dtInicial));
				ps.setTimestamp(3, Util.convCalendarToTimestamp(dtAux));				

				ResultSetMap rsmAux = new ResultSetMap(ps.executeQuery());
				if(rsmAux.next()) {
					data[i] = Util.roundDouble(rsmAux.getDouble("vl_total"), 2);
				} else {
					data[i] = 0.0d;
				}

				i++;
				dtInicial.add(Calendar.MONTH, 1);
			}

			register.put("ARRAY_DATA", data);				
			rsm.addRegister(register);					

			// RECEBIDOS ================================================================================
			dtInicial = (GregorianCalendar) dtBase.clone();
			
			register = new HashMap<String, Object>();
			register.put("RSM_LABELS", rsmLabels);
			
			register.put("NM_LEGENDA", "Arrecadado");
			
			data = new Double[rsmLabels.size()];
			i = 0;
			
			while(dtInicial.compareTo(dtFinal) <= 0) {	
				GregorianCalendar dtAux = (GregorianCalendar)dtInicial.clone();
				dtAux.add(Calendar.MONTH, 1);					

				PreparedStatement ps = connect.prepareStatement(
						" SELECT SUM(vl_repasse) AS vl_repasse "
						+ " FROM ait_pagamento"
						+ " WHERE dt_pagamento BETWEEN ? AND ?");
				
				if(!lgBaseAntiga) {
					ps = connect.prepareStatement(
							" SELECT SUM(vl_repasse) AS vl_repasse "
							+ " FROM mob_ait_pagamento"
							+ " WHERE dt_pagamento BETWEEN ? AND ?");
				}
				ps.setTimestamp(1, Util.convCalendarToTimestamp(dtInicial));
				ps.setTimestamp(2, Util.convCalendarToTimestamp(dtAux));				

				ResultSetMap rsmAux = new ResultSetMap(ps.executeQuery());
				if(rsmAux.next()) {
					data[i] = Util.roundDouble(rsmAux.getDouble("vl_repasse"), 2);
				} else {
					data[i] = 0.0d;
				}

				i++;
				dtInicial.add(Calendar.MONTH, 1);
			}

			register.put("ARRAY_DATA", data);				
			rsm.addRegister(register);				
			
			return rsm;
		}
		catch(Exception e) {
			System.out.println("Erro! AitServices.statsAitValor.");
			e.printStackTrace(System.out);
			return null;
		}
		finally {
			Conexao.desconectar(connect);
		}
	}

}
