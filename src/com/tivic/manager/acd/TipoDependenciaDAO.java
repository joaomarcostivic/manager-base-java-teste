package com.tivic.manager.acd;

import java.sql.*;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import java.util.ArrayList;

import com.tivic.sol.connection.Conexao;

public class TipoDependenciaDAO{

	public static int insert(TipoDependencia objeto) {
		return insert(objeto, null);
	}

	public static int insert(TipoDependencia objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			int code = Conexao.getSequenceCode("acd_tipo_dependencia", connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdTipoDependencia(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO acd_tipo_dependencia (cd_tipo_dependencia,"+
			                                  "nm_tipo_dependencia,"+
			                                  "id_tipo_dependencia) VALUES (?, ?, ?)");
			pstmt.setInt(1, code);
			pstmt.setString(2,objeto.getNmTipoDependencia());
			pstmt.setString(3,objeto.getIdTipoDependencia());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TipoDependenciaDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! TipoDependenciaDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(TipoDependencia objeto) {
		return update(objeto, 0, null);
	}

	public static int update(TipoDependencia objeto, int cdTipoDependenciaOld) {
		return update(objeto, cdTipoDependenciaOld, null);
	}

	public static int update(TipoDependencia objeto, Connection connect) {
		return update(objeto, 0, connect);
	}

	public static int update(TipoDependencia objeto, int cdTipoDependenciaOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE acd_tipo_dependencia SET cd_tipo_dependencia=?,"+
												      		   "nm_tipo_dependencia=?,"+
												      		   "id_tipo_dependencia=? WHERE cd_tipo_dependencia=?");
			pstmt.setInt(1,objeto.getCdTipoDependencia());
			pstmt.setString(2,objeto.getNmTipoDependencia());
			pstmt.setString(3,objeto.getIdTipoDependencia());
			pstmt.setInt(4, cdTipoDependenciaOld!=0 ? cdTipoDependenciaOld : objeto.getCdTipoDependencia());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TipoDependenciaDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! TipoDependenciaDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdTipoDependencia) {
		return delete(cdTipoDependencia, null);
	}

	public static int delete(int cdTipoDependencia, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM acd_tipo_dependencia WHERE cd_tipo_dependencia=?");
			pstmt.setInt(1, cdTipoDependencia);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TipoDependenciaDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! TipoDependenciaDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static TipoDependencia get(int cdTipoDependencia) {
		return get(cdTipoDependencia, null);
	}

	public static TipoDependencia get(int cdTipoDependencia, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM acd_tipo_dependencia WHERE cd_tipo_dependencia=?");
			pstmt.setInt(1, cdTipoDependencia);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new TipoDependencia(rs.getInt("cd_tipo_dependencia"),
						rs.getString("nm_tipo_dependencia"),
						rs.getString("id_tipo_dependencia"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TipoDependenciaDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! TipoDependenciaDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM acd_tipo_dependencia");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TipoDependenciaDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! TipoDependenciaDAO.getAll: " + e);
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
		return Search.find("SELECT * FROM acd_tipo_dependencia", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
