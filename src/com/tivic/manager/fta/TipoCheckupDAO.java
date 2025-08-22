package com.tivic.manager.fta;

import java.sql.*;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import java.util.ArrayList;

import com.tivic.sol.connection.Conexao;

public class TipoCheckupDAO{

	public static int insert(TipoCheckup objeto) {
		return insert(objeto, null);
	}

	public static int insert(TipoCheckup objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			int code = Conexao.getSequenceCode("fta_tipo_checkup", connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdTipoCheckup(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO fta_tipo_checkup (cd_tipo_checkup,"+
			                                  "nm_tipo_checkup) VALUES (?, ?)");
			pstmt.setInt(1, code);
			pstmt.setString(2,objeto.getNmTipoCheckup());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TipoCheckupDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! TipoCheckupDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(TipoCheckup objeto) {
		return update(objeto, 0, null);
	}

	public static int update(TipoCheckup objeto, int cdTipoCheckupOld) {
		return update(objeto, cdTipoCheckupOld, null);
	}

	public static int update(TipoCheckup objeto, Connection connect) {
		return update(objeto, 0, connect);
	}

	public static int update(TipoCheckup objeto, int cdTipoCheckupOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE fta_tipo_checkup SET cd_tipo_checkup=?,"+
												      		   "nm_tipo_checkup=? WHERE cd_tipo_checkup=?");
			pstmt.setInt(1,objeto.getCdTipoCheckup());
			pstmt.setString(2,objeto.getNmTipoCheckup());
			pstmt.setInt(3, cdTipoCheckupOld!=0 ? cdTipoCheckupOld : objeto.getCdTipoCheckup());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TipoCheckupDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! TipoCheckupDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdTipoCheckup) {
		return delete(cdTipoCheckup, null);
	}

	public static int delete(int cdTipoCheckup, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM fta_tipo_checkup WHERE cd_tipo_checkup=?");
			pstmt.setInt(1, cdTipoCheckup);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TipoCheckupDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! TipoCheckupDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static TipoCheckup get(int cdTipoCheckup) {
		return get(cdTipoCheckup, null);
	}

	public static TipoCheckup get(int cdTipoCheckup, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM fta_tipo_checkup WHERE cd_tipo_checkup=?");
			pstmt.setInt(1, cdTipoCheckup);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new TipoCheckup(rs.getInt("cd_tipo_checkup"),
						rs.getString("nm_tipo_checkup"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TipoCheckupDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! TipoCheckupDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM fta_tipo_checkup");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TipoCheckupDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! TipoCheckupDAO.getAll: " + e);
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
		return Search.find("SELECT * FROM fta_tipo_checkup", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
