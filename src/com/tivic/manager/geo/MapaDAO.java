package com.tivic.manager.geo;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import java.util.ArrayList;

public class MapaDAO{

	public static int insert(Mapa objeto) {
		return insert(objeto, null);
	}

	public static int insert(Mapa objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			int code = Conexao.getSequenceCode("geo_mapa", connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdMapa(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO geo_mapa (cd_mapa,"+
			                                  "nm_mapa,"+
			                                  "txt_mapa,"+
			                                  "vl_latitude_inicial,"+
			                                  "vl_longitude_inicial,"+
			                                  "vl_zoom,"+
			                                  "tp_mapa) VALUES (?, ?, ?, ?, ?, ?, ?)");
			pstmt.setInt(1, code);
			pstmt.setString(2,objeto.getNmMapa());
			pstmt.setString(3,objeto.getTxtMapa());
			pstmt.setFloat(4,objeto.getVlLatitudeInicial());
			pstmt.setFloat(5,objeto.getVlLongitudeInicial());
			pstmt.setInt(6,objeto.getVlZoom());
			pstmt.setInt(7,objeto.getTpMapa());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! MapaDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! MapaDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(Mapa objeto) {
		return update(objeto, 0, null);
	}

	public static int update(Mapa objeto, int cdMapaOld) {
		return update(objeto, cdMapaOld, null);
	}

	public static int update(Mapa objeto, Connection connect) {
		return update(objeto, 0, connect);
	}

	public static int update(Mapa objeto, int cdMapaOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE geo_mapa SET cd_mapa=?,"+
												      		   "nm_mapa=?,"+
												      		   "txt_mapa=?,"+
												      		   "vl_latitude_inicial=?,"+
												      		   "vl_longitude_inicial=?,"+
												      		   "vl_zoom=?,"+
												      		   "tp_mapa=? WHERE cd_mapa=?");
			pstmt.setInt(1,objeto.getCdMapa());
			pstmt.setString(2,objeto.getNmMapa());
			pstmt.setString(3,objeto.getTxtMapa());
			pstmt.setFloat(4,objeto.getVlLatitudeInicial());
			pstmt.setFloat(5,objeto.getVlLongitudeInicial());
			pstmt.setInt(6,objeto.getVlZoom());
			pstmt.setInt(7,objeto.getTpMapa());
			pstmt.setInt(8, cdMapaOld!=0 ? cdMapaOld : objeto.getCdMapa());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! MapaDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! MapaDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdMapa) {
		return delete(cdMapa, null);
	}

	public static int delete(int cdMapa, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM geo_mapa WHERE cd_mapa=?");
			pstmt.setInt(1, cdMapa);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! MapaDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! MapaDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static Mapa get(int cdMapa) {
		return get(cdMapa, null);
	}

	public static Mapa get(int cdMapa, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM geo_mapa WHERE cd_mapa=?");
			pstmt.setInt(1, cdMapa);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new Mapa(rs.getInt("cd_mapa"),
						rs.getString("nm_mapa"),
						rs.getString("txt_mapa"),
						rs.getFloat("vl_latitude_inicial"),
						rs.getFloat("vl_longitude_inicial"),
						rs.getInt("vl_zoom"),
						rs.getInt("tp_mapa"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! MapaDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! MapaDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM geo_mapa");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! MapaDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! MapaDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<Mapa> getList() {
		return getList(null);
	}

	public static ArrayList<Mapa> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<Mapa> list = new ArrayList<Mapa>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				Mapa obj = MapaDAO.get(rsm.getInt("cd_mapa"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! MapaDAO.getList: " + e);
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
		return Search.find("SELECT * FROM geo_mapa", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
