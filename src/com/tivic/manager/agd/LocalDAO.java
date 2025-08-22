package com.tivic.manager.agd;

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
			int code = Conexao.getSequenceCode("AGD_LOCAL", connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdLocal(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO AGD_LOCAL (CD_LOCAL,"+
															   " NM_LOCAL,"+
															   " CD_TIPO_LOCAL) VALUES (?, ?, ?)");
			pstmt.setInt(1, code);
			pstmt.setString(2,objeto.getNmLocal());
			if(objeto.getCdTipoLocal()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdTipoLocal());
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
			PreparedStatement pstmt = connect.prepareStatement("UPDATE AGD_LOCAL SET CD_LOCAL=?,"+
												      		   "NM_LOCAL=?,"+ 
												      		   "CD_TIPO_LOCAL=? WHERE CD_LOCAL=?");
			pstmt.setInt(1,objeto.getCdLocal());
			pstmt.setString(2,objeto.getNmLocal());
			if(objeto.getCdTipoLocal()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdTipoLocal());
			pstmt.setInt(4, cdLocalOld!=0 ? cdLocalOld : objeto.getCdLocal());
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
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM AGD_LOCAL WHERE CD_LOCAL=?");
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
			pstmt = connect.prepareStatement("SELECT * FROM AGD_LOCAL WHERE CD_LOCAL=?");
			pstmt.setInt(1, cdLocal);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new Local(rs.getInt("CD_LOCAL"),
						rs.getString("NM_LOCAL"),
						rs.getInt("CD_TIPO_LOCAL"));
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
			pstmt = connect.prepareStatement("SELECT * FROM AGD_LOCAL");
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
				Local obj = LocalDAO.get(rsm.getInt("CD_LOCAL"), connect);
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
		return Search.find("SELECT * FROM AGD_LOCAL", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
