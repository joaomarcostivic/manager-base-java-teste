package com.tivic.manager.evt;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import java.util.ArrayList;

public class LocalDAO{

	public static int insert(Local objeto) {
		return insert(objeto, null);
	}

	public static int insert(Local objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			int code = Conexao.getSequenceCode("evt_local", connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdLocal(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO evt_local (cd_local,"+
			                                  "nm_local,"+
			                                  "ds_local,"+
			                                  "id_local) VALUES (?, ?, ?, ?)");
			pstmt.setInt(1, code);
			pstmt.setString(2,objeto.getNmLocal());
			pstmt.setString(3,objeto.getDsLocal());
			pstmt.setString(4,objeto.getIdLocal());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! LocalDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! LocalDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(Local objeto) {
		return update(objeto, 0, null);
	}

	public static int update(Local objeto, int cdLocalOld) {
		return update(objeto, cdLocalOld, null);
	}

	public static int update(Local objeto, Connection connect) {
		return update(objeto, 0, connect);
	}

	public static int update(Local objeto, int cdLocalOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE evt_local SET cd_local=?,"+
												      		   "nm_local=?,"+
												      		   "ds_local=?,"+
												      		   "id_local=? WHERE cd_local=?");
			pstmt.setInt(1,objeto.getCdLocal());
			pstmt.setString(2,objeto.getNmLocal());
			pstmt.setString(3,objeto.getDsLocal());
			pstmt.setString(4,objeto.getIdLocal());
			pstmt.setInt(5, cdLocalOld!=0 ? cdLocalOld : objeto.getCdLocal());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! LocalDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! LocalDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdLocal) {
		return delete(cdLocal, null);
	}

	public static int delete(int cdLocal, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM evt_local WHERE cd_local=?");
			pstmt.setInt(1, cdLocal);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! LocalDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! LocalDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static Local get(int cdLocal) {
		return get(cdLocal, null);
	}

	public static Local get(int cdLocal, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM evt_local WHERE cd_local=?");
			pstmt.setInt(1, cdLocal);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new Local(rs.getInt("cd_local"),
						rs.getString("nm_local"),
						rs.getString("ds_local"),
						rs.getString("id_local"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! LocalDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! LocalDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM evt_local");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! LocalDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! LocalDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<Local> getList() {
		return getList(null);
	}

	public static ArrayList<Local> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<Local> list = new ArrayList<Local>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				Local obj = LocalDAO.get(rsm.getInt("cd_local"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! LocalDAO.getList: " + e);
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
		return Search.find("SELECT * FROM evt_local", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
