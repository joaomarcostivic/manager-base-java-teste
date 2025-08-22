package com.tivic.manager.mob;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.util.GregorianCalendar;

import com.tivic.sol.connection.Conexao;
import com.tivic.manager.conf.ManagerConf;
import com.tivic.manager.grl.ParametroServices;
import com.tivic.manager.util.Util;

import sol.dao.ResultSetMap;

public class MedicaoTrafegoBIServices {


	public static ResultSetMap statsTrafegoPistaHora(int cdOrgao, GregorianCalendar dtInicial, GregorianCalendar dtFinal) {
		return statsTrafegoPistaHora(cdOrgao, dtInicial, dtFinal, null);
	}
	
	public static ResultSetMap statsTrafegoPistaHora(int cdOrgao, GregorianCalendar dtInicial, GregorianCalendar dtFinal, Connection connection) {
		boolean isConnectionNull = connection==null;
		try {		
			
			if(isConnectionNull)
				connection = getRadarServerConnection();
			
			if(cdOrgao == 0)
				cdOrgao = getOrgaoAutuadorRadar();
				
			String sqlOrgao = cdOrgao > 0 ? " and B.cd_orgao = "+cdOrgao : "";
			
			PreparedStatement ps = connection
					.prepareStatement(
							" select dia_hora, id_equipamento, nm_via, nr_faixa, total_veiculos, total_medicoes, "+
							" 	   total_velocidade/total_medicoes as velocidade_media, "+
							" 	   velocidade_maxima, velocidade_minima "+
							" from (SELECT date_trunc('hour', A.dt_inicial) as dia_hora, "+
							" 	  	B.id_equipamento, C.nm_via, D.nr_faixa, "+
							" 		sum(A.qt_veiculos) as total_veiculos, "+
							" 		count(*) as total_medicoes, "+
							" 		sum(A.vl_velocidade_medida) as total_velocidade, "+
							" 	  	max(A.vl_velocidade_medida) as velocidade_maxima,"+
						  	"   	min(A.vl_velocidade_medida) as velocidade_minima "+
						  	" 	FROM mob_medicao_trafego A "+
						  	" 	join grl_equipamento B ON (A.cd_equipamento = B.cd_equipamento " + sqlOrgao + ") "+
						  	" 	join mob_via C ON (A.cd_via = C.cd_via) "+
						  	" join mob_faixa D ON (A.cd_via = D.cd_via AND A.cd_faixa = D.cd_faixa) "+
						  	" 	WHERE A.dt_inicial BETWEEN ? AND ? "+
						  	" 	  and A.vl_velocidade_medida > 10 "+ //elimina medições com velocidade menor que 10km/h
						  	" 	GROUP BY dia_hora, B.id_equipamento, C.nm_via, D.nr_faixa "+
							" 	ORDER BY dia_hora, B.id_equipamento, C.nm_via, D.nr_faixa) as medicoes");
			
			ps.setTimestamp(1, Util.convCalendarToTimestamp(dtInicial));
			ps.setTimestamp(2, Util.convCalendarToTimestamp(dtFinal));

			return new ResultSetMap(ps.executeQuery());
				
		} catch(Exception e) {
			System.out.println("Erro! MedicaoTrafegoBIServices.statsTrafegoPistaHora");
			e.printStackTrace(System.out);
			return null;
		} finally {
			if(isConnectionNull)
				try { connection.close(); } catch(Exception e) { e.printStackTrace(); }
		}

	}


	public static ResultSetMap statsTrafegoVia(int cdOrgao, GregorianCalendar dtInicial, GregorianCalendar dtFinal) {
		return statsTrafegoVia(cdOrgao, dtInicial, dtFinal, null);
	}
	
	public static ResultSetMap statsTrafegoVia(int cdOrgao, GregorianCalendar dtInicial, GregorianCalendar dtFinal, Connection connection) {
		boolean isConnectionNull = connection==null;
		try {		
			
			if(isConnectionNull)
				connection = getRadarServerConnection();

			if(cdOrgao == 0)
				cdOrgao = getOrgaoAutuadorRadar();
				
			String sqlOrgao = cdOrgao > 0 ? " and B.cd_orgao = "+cdOrgao : "";
			
			PreparedStatement ps = connection
					.prepareStatement(
							" SELECT B.id_equipamento, B.cd_equipamento, C.nm_via, "+
							" 		sum(A.qt_veiculos) as total_veiculos, "+
							" 		avg(A.vl_velocidade_medida) as velocidade_media, "+
							" 	  	max(A.vl_velocidade_medida) as velocidade_maxima,"+
						  	"   	min(A.vl_velocidade_medida) as velocidade_minima "+
						  	" 	FROM mob_medicao_trafego A "+
						  	" 	join grl_equipamento B ON (A.cd_equipamento = B.cd_equipamento " + sqlOrgao + ") "+
						  	" 	join mob_via C ON (A.cd_via = C.cd_via) "+
						  	" 	WHERE A.dt_inicial BETWEEN ? AND ? "+
						  	" 	  and A.vl_velocidade_medida > 10 "+ //elimina medições com velocidade menor que 10km/h
						  	" 	GROUP BY B.cd_equipamento, C.cd_via "+
							" 	ORDER BY B.id_equipamento, C.nm_via");
			
			ps.setTimestamp(1, Util.convCalendarToTimestamp(dtInicial));
			ps.setTimestamp(2, Util.convCalendarToTimestamp(dtFinal));

			return new ResultSetMap(ps.executeQuery());
				
		} catch(Exception e) {
			System.out.println("Erro! MedicaoTrafegoBIServices.statsTrafegoVia");
			e.printStackTrace(System.out);
			return null;
		} finally {
			if(isConnectionNull)
				try { connection.close(); } catch(Exception e) { e.printStackTrace(); }
		}

	}
	
	public static ResultSetMap statsTrafegoViaHora(int cdOrgao, int cdEquipamento, GregorianCalendar dtInicial, GregorianCalendar dtFinal) {
		return statsTrafegoViaHora(cdOrgao, cdEquipamento, dtInicial, dtFinal, null);
	}
	
	public static ResultSetMap statsTrafegoViaHora(int cdOrgao, int cdEquipamento, GregorianCalendar dtInicial, GregorianCalendar dtFinal, Connection connection) {
		boolean isConnectionNull = connection==null;
		try {		
			
			if(isConnectionNull)
				connection = getRadarServerConnection();

			if(cdOrgao == 0)
				cdOrgao = getOrgaoAutuadorRadar();
				
			String sqlOrgao = cdOrgao > 0 ? " and B.cd_orgao = "+cdOrgao : "";
			
			String sqlEquipamento = cdEquipamento > 0 ? " and B.cd_equipamento = " + cdEquipamento : "";
			
			PreparedStatement ps = connection
					.prepareStatement(
							" SELECT date_trunc('hour', A.dt_inicial) as dia_hora, "+
							" 	  	B.id_equipamento, C.nm_via, "+
							" 		sum(A.qt_veiculos) as total_veiculos, "+
							" 		avg(A.vl_velocidade_medida) as velocidade_media, "+
							" 	  	max(A.vl_velocidade_medida) as velocidade_maxima,"+
						  	"   	min(A.vl_velocidade_medida) as velocidade_minima "+
						  	" 	FROM mob_medicao_trafego A "+
						  	" 	join grl_equipamento B ON (A.cd_equipamento = B.cd_equipamento " + sqlOrgao + sqlEquipamento + ") "+
						  	" 	join mob_via C ON (A.cd_via = C.cd_via) "+
						  	" 	WHERE A.dt_inicial BETWEEN ? AND ? "+
						  	" 	  and A.vl_velocidade_medida > 10 "+ //elimina medições com velocidade menor que 10km/h
						  	" 	GROUP BY dia_hora, B.cd_equipamento, C.cd_via "+
							" 	ORDER BY dia_hora, B.id_equipamento, C.nm_via");
			
			ps.setTimestamp(1, Util.convCalendarToTimestamp(dtInicial));
			ps.setTimestamp(2, Util.convCalendarToTimestamp(dtFinal));

			return new ResultSetMap(ps.executeQuery());
				
		} catch(Exception e) {
			System.out.println("Erro! MedicaoTrafegoBIServices.statsTrafegoViaHora");
			e.printStackTrace(System.out);
			return null;
		} finally {
			if(isConnectionNull)
				try { connection.close(); } catch(Exception e) { e.printStackTrace(); }
		}

	}
	

	public static ResultSetMap statsTrafegoHora(int cdOrgao, GregorianCalendar dtInicial, GregorianCalendar dtFinal) {
		return statsTrafegoHora(cdOrgao, dtInicial, dtFinal, null);
	}
	
	public static ResultSetMap statsTrafegoHora(int cdOrgao, GregorianCalendar dtInicial, GregorianCalendar dtFinal, Connection connection) {
		boolean isConnectionNull = connection==null;
		try {		
			
			if(isConnectionNull)
				connection = getRadarServerConnection();

			if(cdOrgao == 0)
				cdOrgao = getOrgaoAutuadorRadar();
				
			String sqlOrgao = cdOrgao > 0 ? " and B.cd_orgao = "+cdOrgao : "";
			
			PreparedStatement ps = connection
					.prepareStatement(
							" SELECT date_trunc('hour', A.dt_inicial) as dia_hora, "+
							"	  sum(A.qt_veiculos) as total_veiculos,  "+
							"	  avg(A.vl_velocidade_medida) as velocidade_media, "+
							"	  max(A.vl_velocidade_medida) as velocidade_maxima, "+
							"	  min(A.vl_velocidade_medida) as velocidade_minima  "+
							" FROM mob_medicao_trafego A  "+
							" join grl_equipamento B ON (A.cd_equipamento = B.cd_equipamento " + sqlOrgao + ")  "+
							" WHERE A.dt_inicial BETWEEN ? AND ?  "+
							" and A.vl_velocidade_medida > 10  "+
							" GROUP BY dia_hora  "+
							" ORDER BY dia_hora");
			
			ps.setTimestamp(1, Util.convCalendarToTimestamp(dtInicial));
			ps.setTimestamp(2, Util.convCalendarToTimestamp(dtFinal));

			return new ResultSetMap(ps.executeQuery());
				
		} catch(Exception e) {
			System.out.println("Erro! MedicaoTrafegoBIServices.statsTrafegoHora");
			e.printStackTrace(System.out);
			return null;
		} finally {
			if(isConnectionNull)
				try { connection.close(); } catch(Exception e) { e.printStackTrace(); }
		}

	}

	public static ResultSetMap statsTrafegoSemana(int cdOrgao, GregorianCalendar dtInicial, GregorianCalendar dtFinal) {
		return statsTrafegoSemana(cdOrgao, dtInicial, dtFinal, null);
	}
	
	public static ResultSetMap statsTrafegoSemana(int cdOrgao, GregorianCalendar dtInicial, GregorianCalendar dtFinal, Connection connection) {
		boolean isConnectionNull = connection==null;
		try {		
			
			if(isConnectionNull)
				connection = getRadarServerConnection();

			if(cdOrgao == 0)
				cdOrgao = getOrgaoAutuadorRadar();
				
			String sqlOrgao = cdOrgao > 0 ? " and B.cd_orgao = "+cdOrgao : "";
			
			PreparedStatement ps = connection
					.prepareStatement(
							" SELECT extract(dow from A.dt_inicial) as dia_semana, " +
							"	CAST(A.dt_inicial::date as timestamp) as dt_medicao, " +
							"	sum(A.qt_veiculos) as total_veiculos, " +
							"	avg(A.vl_velocidade_medida) as velocidade_media, " +
							"	max(A.vl_velocidade_medida) as velocidade_maxima, " +
							"	min(A.vl_velocidade_medida) as velocidade_minima " +
							"	FROM mob_medicao_trafego A " +
							"	join grl_equipamento B ON (A.cd_equipamento = B.cd_equipamento " + sqlOrgao + ") " +
							"	WHERE A.dt_inicial BETWEEN ? AND ? " +
							"	and A.vl_velocidade_medida > 10 " +
							"	GROUP BY dia_semana, dt_medicao " +
							"	ORDER BY dt_medicao");
			
			ps.setTimestamp(1, Util.convCalendarToTimestamp(dtInicial));
			ps.setTimestamp(2, Util.convCalendarToTimestamp(dtFinal));

			return new ResultSetMap(ps.executeQuery());
				
		} catch(Exception e) {
			System.out.println("Erro! MedicaoTrafegoBIServices.statsTrafegoSemana");
			e.printStackTrace(System.out);
			return null;
		} finally {
			if(isConnectionNull)
				try { connection.close(); } catch(Exception e) { e.printStackTrace(); }
		}

	}
	
	public static ResultSetMap statsTrafegoTipoVeiculo(int cdOrgao, GregorianCalendar dtInicial, GregorianCalendar dtFinal) {
		return statsTrafegoTipoVeiculo(cdOrgao, dtInicial, dtFinal, null);
	}
	
	public static ResultSetMap statsTrafegoTipoVeiculo(int cdOrgao, GregorianCalendar dtInicial, GregorianCalendar dtFinal, Connection connection) {
		boolean isConnectionNull = connection==null;
		try {		
			
			if(isConnectionNull)
				connection = getRadarServerConnection();

			if(cdOrgao == 0)
				cdOrgao = getOrgaoAutuadorRadar();
				
			String sqlOrgao = cdOrgao > 0 ? " and B.cd_orgao = "+cdOrgao : "";
			
			PreparedStatement ps = connection
					.prepareStatement(
							" SELECT A.tp_veiculo, " +
							"	sum(A.qt_veiculos) as total_veiculos, " +
							"	avg(A.vl_velocidade_medida) as velocidade_media, " +
							"	max(A.vl_velocidade_medida) as velocidade_maxima, " +
							"	min(A.vl_velocidade_medida) as velocidade_minima " +
							"	FROM mob_medicao_trafego A " +
							"	join grl_equipamento B ON (A.cd_equipamento = B.cd_equipamento " + sqlOrgao + ") " +
							"	WHERE A.dt_inicial BETWEEN ? AND ? " +
							"	and A.vl_velocidade_medida > 10 " +
							"	GROUP BY A.tp_veiculo " +
							"	ORDER BY A.tp_veiculo");
			
			ps.setTimestamp(1, Util.convCalendarToTimestamp(dtInicial));
			ps.setTimestamp(2, Util.convCalendarToTimestamp(dtFinal));

			return new ResultSetMap(ps.executeQuery());
				
		} catch(Exception e) {
			System.out.println("Erro! MedicaoTrafegoBIServices.statsTrafegoTipo");
			e.printStackTrace(System.out);
			return null;
		} finally {
			if(isConnectionNull)
				try { connection.close(); } catch(Exception e) { e.printStackTrace(); }
		}

	}
	
	private static int getOrgaoAutuadorRadar() {
		int cdOrgaoAutuador = ManagerConf.getInstance().getAsInteger("RADAR_CD_ORGAO_AUTUADOR", 0);
		
		if(cdOrgaoAutuador == 0)
			cdOrgaoAutuador = ParametroServices.getValorOfParametroAsInteger("MOB_RADAR_CD_ORGAO_AUTUADOR", 0);
			
		return cdOrgaoAutuador;
	}
	
	private static Connection getRadarServerConnection() throws Exception {
		String driver = ManagerConf.getInstance().get("RADAR_DRIVER");
		String url = ManagerConf.getInstance().get("RADAR_DBPATH");
		String login = ManagerConf.getInstance().get("RADAR_LOGIN");
		String senha = ManagerConf.getInstance().get("RADAR_PASS");
		
		
		Class.forName(driver).newInstance();
  		DriverManager.setLoginTimeout(Conexao.CONNECTION_TIMEOUT);
  		return DriverManager.getConnection(url, login, senha);
	}

}
