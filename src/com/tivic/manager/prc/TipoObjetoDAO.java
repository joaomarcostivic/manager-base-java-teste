package com.tivic.manager.prc;

import java.sql.*;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import java.util.ArrayList;

import com.tivic.sol.connection.Conexao;

public class TipoObjetoDAO{

	public static int insert(TipoObjeto objeto) {
		return insert(objeto, null);
	}

	public static int insert(TipoObjeto objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			int code = Conexao.getSequenceCode("prc_tipo_objeto", connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			if(objeto.getCdTipoObjeto()<=0)
				objeto.setCdTipoObjeto(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO prc_tipo_objeto (cd_tipo_objeto,"+
			                                  "nm_tipo_objeto,"+
			                                  "id_tipo_objeto) VALUES (?, ?, ?)");
			pstmt.setInt(1, objeto.getCdTipoObjeto());
			pstmt.setString(2,objeto.getNmTipoObjeto());
			pstmt.setString(3,objeto.getIdTipoObjeto());
			pstmt.executeUpdate();
			return objeto.getCdTipoObjeto();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! TipoObjetoDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(TipoObjeto objeto) {
		return update(objeto, 0, null);
	}

	public static int update(TipoObjeto objeto, int cdTipoObjetoOld) {
		return update(objeto, cdTipoObjetoOld, null);
	}

	public static int update(TipoObjeto objeto, Connection connect) {
		return update(objeto, 0, connect);
	}

	public static int update(TipoObjeto objeto, int cdTipoObjetoOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE prc_tipo_objeto SET cd_tipo_objeto=?,"+
												      		   "nm_tipo_objeto=?,"+
												      		   "id_tipo_objeto=? WHERE cd_tipo_objeto=?");
			pstmt.setInt(1,objeto.getCdTipoObjeto());
			pstmt.setString(2,objeto.getNmTipoObjeto());
			pstmt.setString(3,objeto.getIdTipoObjeto());
			pstmt.setInt(4, cdTipoObjetoOld!=0 ? cdTipoObjetoOld : objeto.getCdTipoObjeto());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TipoObjetoDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! TipoObjetoDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdTipoObjeto) {
		return delete(cdTipoObjeto, null);
	}

	public static int delete(int cdTipoObjeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM prc_tipo_objeto WHERE cd_tipo_objeto=?");
			pstmt.setInt(1, cdTipoObjeto);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TipoObjetoDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! TipoObjetoDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static TipoObjeto get(int cdTipoObjeto) {
		return get(cdTipoObjeto, null);
	}

	public static TipoObjeto get(int cdTipoObjeto, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM prc_tipo_objeto WHERE cd_tipo_objeto=?");
			pstmt.setInt(1, cdTipoObjeto);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new TipoObjeto(rs.getInt("cd_tipo_objeto"),
						rs.getString("nm_tipo_objeto"),
						rs.getString("id_tipo_objeto"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TipoObjetoDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! TipoObjetoDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM prc_tipo_objeto");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TipoObjetoDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! TipoObjetoDAO.getAll: " + e);
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
		return Search.find("SELECT * FROM prc_tipo_objeto", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
