package com.tivic.manager.grl;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import java.util.ArrayList;

public class AlergiaDAO{

	public static int insert(Alergia objeto) {
		return insert(objeto, null);
	}

	public static int insert(Alergia objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			int code = Conexao.getSequenceCode("grl_alergia", connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdAlergia(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO grl_alergia (cd_alergia,"+
			                                  "nm_alergia,"+
			                                  "id_alergia,"+
			                                  "tp_alergia) VALUES (?, ?, ?, ?)");
			pstmt.setInt(1, code);
			pstmt.setString(2,objeto.getNmAlergia());
			pstmt.setString(3,objeto.getIdAlergia());
			pstmt.setInt(4,objeto.getTpAlergia());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AlergiaDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! AlergiaDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(Alergia objeto) {
		return update(objeto, 0, null);
	}

	public static int update(Alergia objeto, int cdAlergiaOld) {
		return update(objeto, cdAlergiaOld, null);
	}

	public static int update(Alergia objeto, Connection connect) {
		return update(objeto, 0, connect);
	}

	public static int update(Alergia objeto, int cdAlergiaOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE grl_alergia SET cd_alergia=?,"+
												      		   "nm_alergia=?,"+
												      		   "id_alergia=?,"+
												      		   "tp_alergia=? WHERE cd_alergia=?");
			pstmt.setInt(1,objeto.getCdAlergia());
			pstmt.setString(2,objeto.getNmAlergia());
			pstmt.setString(3,objeto.getIdAlergia());
			pstmt.setInt(4,objeto.getTpAlergia());
			pstmt.setInt(5, cdAlergiaOld!=0 ? cdAlergiaOld : objeto.getCdAlergia());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AlergiaDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! AlergiaDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdAlergia) {
		return delete(cdAlergia, null);
	}

	public static int delete(int cdAlergia, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM grl_alergia WHERE cd_alergia=?");
			pstmt.setInt(1, cdAlergia);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AlergiaDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! AlergiaDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static Alergia get(int cdAlergia) {
		return get(cdAlergia, null);
	}

	public static Alergia get(int cdAlergia, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM grl_alergia WHERE cd_alergia=?");
			pstmt.setInt(1, cdAlergia);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new Alergia(rs.getInt("cd_alergia"),
						rs.getString("nm_alergia"),
						rs.getString("id_alergia"),
						rs.getInt("tp_alergia"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AlergiaDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! AlergiaDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM grl_alergia");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AlergiaDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! AlergiaDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<Alergia> getList() {
		return getList(null);
	}

	public static ArrayList<Alergia> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<Alergia> list = new ArrayList<Alergia>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				Alergia obj = AlergiaDAO.get(rsm.getInt("cd_alergia"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! AlergiaDAO.getList: " + e);
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
		return Search.find("SELECT * FROM grl_alergia", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
