package com.tivic.manager.ptc;

import java.sql.*;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import java.util.ArrayList;

import com.tivic.sol.connection.Conexao;

public class TipoAnexoDAO{

	public static int insert(TipoAnexo objeto) {
		return insert(objeto, null);
	}

	public static int insert(TipoAnexo objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			int code = Conexao.getSequenceCode("ptc_tipo_anexo", connect);
			if (code <= 0) {
				if (isConnectionNull)
					sol.dao.Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdTipoAnexo(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO ptc_tipo_anexo (cd_tipo_anexo,"+
			                                  "nm_tipo_anexo) VALUES (?, ?)");
			pstmt.setInt(1, code);
			pstmt.setString(2,objeto.getNmTipoAnexo());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TipoAnexoDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! TipoAnexoDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(TipoAnexo objeto) {
		return update(objeto, 0, null);
	}

	public static int update(TipoAnexo objeto, int cdTipoAnexoOld) {
		return update(objeto, cdTipoAnexoOld, null);
	}

	public static int update(TipoAnexo objeto, Connection connect) {
		return update(objeto, 0, connect);
	}

	public static int update(TipoAnexo objeto, int cdTipoAnexoOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE ptc_tipo_anexo SET cd_tipo_anexo=?,"+
												      		   "nm_tipo_anexo=? WHERE cd_tipo_anexo=?");
			pstmt.setInt(1,objeto.getCdTipoAnexo());
			pstmt.setString(2,objeto.getNmTipoAnexo());
			pstmt.setInt(3, cdTipoAnexoOld!=0 ? cdTipoAnexoOld : objeto.getCdTipoAnexo());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TipoAnexoDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! TipoAnexoDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdTipoAnexo) {
		return delete(cdTipoAnexo, null);
	}

	public static int delete(int cdTipoAnexo, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM ptc_tipo_anexo WHERE cd_tipo_anexo=?");
			pstmt.setInt(1, cdTipoAnexo);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TipoAnexoDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! TipoAnexoDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static TipoAnexo get(int cdTipoAnexo) {
		return get(cdTipoAnexo, null);
	}

	public static TipoAnexo get(int cdTipoAnexo, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM ptc_tipo_anexo WHERE cd_tipo_anexo=?");
			pstmt.setInt(1, cdTipoAnexo);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new TipoAnexo(rs.getInt("cd_tipo_anexo"),
						rs.getString("nm_tipo_anexo"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TipoAnexoDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! TipoAnexoDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM ptc_tipo_anexo");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TipoAnexoDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! TipoAnexoDAO.getAll: " + e);
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
		return Search.find("SELECT * FROM ptc_tipo_anexo", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
