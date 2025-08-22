package com.tivic.manager.acd;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import java.util.ArrayList;

public class EsgotoSanitarioDAO{

	public static int insert(EsgotoSanitario objeto) {
		return insert(objeto, null);
	}

	public static int insert(EsgotoSanitario objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			int code = Conexao.getSequenceCode("acd_esgoto_sanitario", connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdEsgotoSanitario(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO acd_esgoto_sanitario (cd_esgoto_sanitario,"+
			                                  "nm_esgoto_sanitario,"+
			                                  "id_esgoto_sanitario) VALUES (?, ?, ?)");
			pstmt.setInt(1, code);
			pstmt.setString(2,objeto.getNmEsgotoSanitario());
			pstmt.setString(3,objeto.getIdEsgotoSanitario());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! EsgotoSanitarioDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! EsgotoSanitarioDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(EsgotoSanitario objeto) {
		return update(objeto, 0, null);
	}

	public static int update(EsgotoSanitario objeto, int cdEsgotoSanitarioOld) {
		return update(objeto, cdEsgotoSanitarioOld, null);
	}

	public static int update(EsgotoSanitario objeto, Connection connect) {
		return update(objeto, 0, connect);
	}

	public static int update(EsgotoSanitario objeto, int cdEsgotoSanitarioOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE acd_esgoto_sanitario SET cd_esgoto_sanitario=?,"+
												      		   "nm_esgoto_sanitario=?,"+
												      		   "id_esgoto_sanitario=? WHERE cd_esgoto_sanitario=?");
			pstmt.setInt(1,objeto.getCdEsgotoSanitario());
			pstmt.setString(2,objeto.getNmEsgotoSanitario());
			pstmt.setString(3,objeto.getIdEsgotoSanitario());
			pstmt.setInt(4, cdEsgotoSanitarioOld!=0 ? cdEsgotoSanitarioOld : objeto.getCdEsgotoSanitario());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! EsgotoSanitarioDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! EsgotoSanitarioDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdEsgotoSanitario) {
		return delete(cdEsgotoSanitario, null);
	}

	public static int delete(int cdEsgotoSanitario, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM acd_esgoto_sanitario WHERE cd_esgoto_sanitario=?");
			pstmt.setInt(1, cdEsgotoSanitario);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! EsgotoSanitarioDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! EsgotoSanitarioDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static EsgotoSanitario get(int cdEsgotoSanitario) {
		return get(cdEsgotoSanitario, null);
	}

	public static EsgotoSanitario get(int cdEsgotoSanitario, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM acd_esgoto_sanitario WHERE cd_esgoto_sanitario=?");
			pstmt.setInt(1, cdEsgotoSanitario);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new EsgotoSanitario(rs.getInt("cd_esgoto_sanitario"),
						rs.getString("nm_esgoto_sanitario"),
						rs.getString("id_esgoto_sanitario"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! EsgotoSanitarioDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! EsgotoSanitarioDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM acd_esgoto_sanitario");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! EsgotoSanitarioDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! EsgotoSanitarioDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<EsgotoSanitario> getList() {
		return getList(null);
	}

	public static ArrayList<EsgotoSanitario> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<EsgotoSanitario> list = new ArrayList<EsgotoSanitario>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				EsgotoSanitario obj = EsgotoSanitarioDAO.get(rsm.getInt("cd_esgoto_sanitario"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! EsgotoSanitarioDAO.getList: " + e);
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
		return Search.find("SELECT * FROM acd_esgoto_sanitario", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
