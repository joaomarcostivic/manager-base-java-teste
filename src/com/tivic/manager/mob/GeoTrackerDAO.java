package com.tivic.manager.mob;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.dao.Util;
import java.util.ArrayList;

public class GeoTrackerDAO{

	public static int insert(GeoTracker objeto) {
		return insert(objeto, null);
	}

	public static int insert(GeoTracker objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			int code = Conexao.getSequenceCode("mob_geo_tracker", connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdTracker(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO mob_geo_tracker (cd_tracker,"+
			                                  "id_orgao,"+
			                                  "id_equipamento,"+
			                                  "nr_matricula_agente,"+
			                                  "vl_latitude,"+
			                                  "vl_longitude,"+
			                                  "dt_historico,"+
			                                  "cd_orgao," +
			                                  "cd_agente) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)");
			pstmt.setInt(1, code);
			pstmt.setString(2,objeto.getIdOrgao());
			pstmt.setString(3,objeto.getIdEquipamento());
			pstmt.setString(4,objeto.getNrMatriculaAgente());
			pstmt.setDouble(5,objeto.getVlLatitude());
			pstmt.setDouble(6,objeto.getVlLongitude());
			if(objeto.getDtHistorico()==null)
				pstmt.setNull(7, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(7,new Timestamp(objeto.getDtHistorico().getTimeInMillis()));
			if(objeto.getCdOrgao()==0)
				pstmt.setNull(8, Types.INTEGER);
			else
				pstmt.setInt(8, objeto.getCdOrgao());
			if(objeto.getCdAgente()==0)
				pstmt.setNull(9, Types.INTEGER);
			else
				pstmt.setInt(9, objeto.getCdAgente());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! GeoTrackerDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! GeoTrackerDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(GeoTracker objeto) {
		return update(objeto, 0, null);
	}

	public static int update(GeoTracker objeto, int cdTrackerOld) {
		return update(objeto, cdTrackerOld, null);
	}

	public static int update(GeoTracker objeto, Connection connect) {
		return update(objeto, 0, connect);
	}

	public static int update(GeoTracker objeto, int cdTrackerOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE mob_geo_tracker SET cd_tracker=?,"+
												      		   "id_orgao=?,"+
												      		   "id_equipamento=?,"+
												      		   "nr_matricula_agente=?,"+
												      		   "vl_latitude=?,"+
												      		   "vl_longitude=?,"+
												      		   "dt_historico=?,"+
												      		   "cd_orgao=?,"+
												      		   "cd_agente=? WHERE cd_tracker=?");
			pstmt.setInt(1,objeto.getCdTracker());
			pstmt.setString(2,objeto.getIdOrgao());
			pstmt.setString(3,objeto.getIdEquipamento());
			pstmt.setString(4,objeto.getNrMatriculaAgente());
			pstmt.setDouble(5,objeto.getVlLatitude());
			pstmt.setDouble(6,objeto.getVlLongitude());
			if(objeto.getDtHistorico()==null)
				pstmt.setNull(7, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(7,new Timestamp(objeto.getDtHistorico().getTimeInMillis()));
			pstmt.setInt(8, cdTrackerOld!=0 ? cdTrackerOld : objeto.getCdTracker());
			if(objeto.getCdOrgao()==0)
				pstmt.setNull(8, Types.INTEGER);
			else
				pstmt.setInt(8, objeto.getCdOrgao());
			if(objeto.getCdAgente()==0)
				pstmt.setNull(9, Types.INTEGER);
			else
				pstmt.setInt(9, objeto.getCdAgente());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! GeoTrackerDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! GeoTrackerDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdTracker) {
		return delete(cdTracker, null);
	}

	public static int delete(int cdTracker, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM mob_geo_tracker WHERE cd_tracker=?");
			pstmt.setInt(1, cdTracker);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! GeoTrackerDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! GeoTrackerDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static GeoTracker get(int cdTracker) {
		return get(cdTracker, null);
	}

	public static GeoTracker get(int cdTracker, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM mob_geo_tracker WHERE cd_tracker=?");
			pstmt.setInt(1, cdTracker);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new GeoTracker(rs.getInt("cd_tracker"),
						rs.getString("id_orgao"),
						rs.getString("id_equipamento"),
						rs.getString("nr_matricula_agente"),
						rs.getDouble("vl_latitude"),
						rs.getDouble("vl_longitude"),
						(rs.getTimestamp("dt_historico")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_historico").getTime()),
						rs.getInt("cd_orgao"),
						rs.getInt("cd_agente"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! GeoTrackerDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! GeoTrackerDAO.get: " + e);
			return null;
		}
		finally {
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
			pstmt = connect.prepareStatement("SELECT * FROM mob_geo_tracker");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! GeoTrackerDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! GeoTrackerDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<GeoTracker> getList() {
		return getList(null);
	}

	public static ArrayList<GeoTracker> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<GeoTracker> list = new ArrayList<GeoTracker>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				GeoTracker obj = GeoTrackerDAO.get(rsm.getInt("cd_tracker"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! GeoTrackerDAO.getList: " + e);
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
		return Search.find("SELECT * FROM mob_geo_tracker", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
