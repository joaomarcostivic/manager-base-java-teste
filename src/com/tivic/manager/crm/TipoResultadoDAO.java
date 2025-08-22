package com.tivic.manager.crm;

import java.sql.*;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import java.util.ArrayList;

import com.tivic.sol.connection.Conexao;

public class TipoResultadoDAO{

	public static int insert(TipoResultado objeto) {
		return insert(objeto, null);
	}

	public static int insert(TipoResultado objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			int code = Conexao.getSequenceCode("crm_tipo_resultado", connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdTipoResultado(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO crm_tipo_resultado (cd_tipo_resultado,"+
			                                  "nm_tipo_resultado,"+
			                                  "id_tipo_resultado,"+
			                                  "st_tipo_resultado) VALUES (?, ?, ?, ?)");
			pstmt.setInt(1, code);
			pstmt.setString(2,objeto.getNmTipoResultado());
			pstmt.setString(3,objeto.getIdTipoResultado());
			pstmt.setInt(4,objeto.getStTipoResultado());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TipoResultadoDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! TipoResultadoDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(TipoResultado objeto) {
		return update(objeto, 0, null);
	}

	public static int update(TipoResultado objeto, int cdTipoResultadoOld) {
		return update(objeto, cdTipoResultadoOld, null);
	}

	public static int update(TipoResultado objeto, Connection connect) {
		return update(objeto, 0, connect);
	}

	public static int update(TipoResultado objeto, int cdTipoResultadoOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE crm_tipo_resultado SET cd_tipo_resultado=?,"+
												      		   "nm_tipo_resultado=?,"+
												      		   "id_tipo_resultado=?,"+
												      		   "st_tipo_resultado=? WHERE cd_tipo_resultado=?");
			pstmt.setInt(1,objeto.getCdTipoResultado());
			pstmt.setString(2,objeto.getNmTipoResultado());
			pstmt.setString(3,objeto.getIdTipoResultado());
			pstmt.setInt(4,objeto.getStTipoResultado());
			pstmt.setInt(5, cdTipoResultadoOld!=0 ? cdTipoResultadoOld : objeto.getCdTipoResultado());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TipoResultadoDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! TipoResultadoDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdTipoResultado) {
		return delete(cdTipoResultado, null);
	}

	public static int delete(int cdTipoResultado, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM crm_tipo_resultado WHERE cd_tipo_resultado=?");
			pstmt.setInt(1, cdTipoResultado);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TipoResultadoDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! TipoResultadoDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static TipoResultado get(int cdTipoResultado) {
		return get(cdTipoResultado, null);
	}

	public static TipoResultado get(int cdTipoResultado, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM crm_tipo_resultado WHERE cd_tipo_resultado=?");
			pstmt.setInt(1, cdTipoResultado);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new TipoResultado(rs.getInt("cd_tipo_resultado"),
						rs.getString("nm_tipo_resultado"),
						rs.getString("id_tipo_resultado"),
						rs.getInt("st_tipo_resultado"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TipoResultadoDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! TipoResultadoDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM crm_tipo_resultado ORDER BY nm_tipo_resultado");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! TipoResultadoDAO.getAll: " + e);
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
		return Search.find("SELECT * FROM crm_tipo_resultado", "ORDER BY nm_tipo_resultado", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
