package com.tivic.manager.mob;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import java.util.ArrayList;

public class LocalRemocaoDAO{

	public static int insert(LocalRemocao objeto) {
		return insert(objeto, null);
	}

	public static int insert(LocalRemocao objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			int code = Conexao.getSequenceCode("mob_local_remocao", connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdLocalRemocao(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO mob_local_remocao (cd_local_remocao,"+
			                                  "nm_local_remocao,"+
			                                  "id_local_remocao) VALUES (?, ?, ?)");
			pstmt.setInt(1, code);
			pstmt.setString(2,objeto.getNmLocalRemocao());
			pstmt.setString(3,objeto.getIdLocalRemocao());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! LocalRemocaoDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! LocalRemocaoDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(LocalRemocao objeto) {
		return update(objeto, 0, null);
	}

	public static int update(LocalRemocao objeto, int cdLocalRemocaoOld) {
		return update(objeto, cdLocalRemocaoOld, null);
	}

	public static int update(LocalRemocao objeto, Connection connect) {
		return update(objeto, 0, connect);
	}

	public static int update(LocalRemocao objeto, int cdLocalRemocaoOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE mob_local_remocao SET cd_local_remocao=?,"+
												      		   "nm_local_remocao=?,"+
												      		   "id_local_remocao=? WHERE cd_local_remocao=?");
			pstmt.setInt(1,objeto.getCdLocalRemocao());
			pstmt.setString(2,objeto.getNmLocalRemocao());
			pstmt.setString(3,objeto.getIdLocalRemocao());
			pstmt.setInt(4, cdLocalRemocaoOld!=0 ? cdLocalRemocaoOld : objeto.getCdLocalRemocao());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! LocalRemocaoDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! LocalRemocaoDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdLocalRemocao) {
		return delete(cdLocalRemocao, null);
	}

	public static int delete(int cdLocalRemocao, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM mob_local_remocao WHERE cd_local_remocao=?");
			pstmt.setInt(1, cdLocalRemocao);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! LocalRemocaoDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! LocalRemocaoDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static LocalRemocao get(int cdLocalRemocao) {
		return get(cdLocalRemocao, null);
	}

	public static LocalRemocao get(int cdLocalRemocao, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM mob_local_remocao WHERE cd_local_remocao=?");
			pstmt.setInt(1, cdLocalRemocao);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new LocalRemocao(rs.getInt("cd_local_remocao"),
						rs.getString("nm_local_remocao"),
						rs.getString("id_local_remocao"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! LocalRemocaoDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! LocalRemocaoDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM mob_local_remocao");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! LocalRemocaoDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! LocalRemocaoDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<LocalRemocao> getList() {
		return getList(null);
	}

	public static ArrayList<LocalRemocao> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<LocalRemocao> list = new ArrayList<LocalRemocao>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				LocalRemocao obj = LocalRemocaoDAO.get(rsm.getInt("cd_local_remocao"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! LocalRemocaoDAO.getList: " + e);
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
		return Search.find("SELECT * FROM mob_local_remocao", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}