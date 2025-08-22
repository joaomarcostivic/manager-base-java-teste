package com.tivic.manager.acd;

import java.sql.*;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import java.util.ArrayList;

import com.tivic.sol.connection.Conexao;

public class TipoMantenedoraDAO{

	public static int insert(TipoMantenedora objeto) {
		return insert(objeto, null);
	}

	public static int insert(TipoMantenedora objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			int code = Conexao.getSequenceCode("acd_tipo_mantenedora", connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdTipoMantenedora(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO acd_tipo_mantenedora (cd_tipo_mantenedora,"+
			                                  "nm_tipo_mantenedora,"+
			                                  "id_tipo_mantenedora) VALUES (?, ?, ?)");
			pstmt.setInt(1, code);
			pstmt.setString(2,objeto.getNmTipoMantenedora());
			pstmt.setString(3,objeto.getIdTipoMantenedora());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TipoMantenedoraDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! TipoMantenedoraDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(TipoMantenedora objeto) {
		return update(objeto, 0, null);
	}

	public static int update(TipoMantenedora objeto, int cdTipoMantenedoraOld) {
		return update(objeto, cdTipoMantenedoraOld, null);
	}

	public static int update(TipoMantenedora objeto, Connection connect) {
		return update(objeto, 0, connect);
	}

	public static int update(TipoMantenedora objeto, int cdTipoMantenedoraOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE acd_tipo_mantenedora SET cd_tipo_mantenedora=?,"+
												      		   "nm_tipo_mantenedora=?,"+
												      		   "id_tipo_mantenedora=? WHERE cd_tipo_mantenedora=?");
			pstmt.setInt(1,objeto.getCdTipoMantenedora());
			pstmt.setString(2,objeto.getNmTipoMantenedora());
			pstmt.setString(3,objeto.getIdTipoMantenedora());
			pstmt.setInt(4, cdTipoMantenedoraOld!=0 ? cdTipoMantenedoraOld : objeto.getCdTipoMantenedora());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TipoMantenedoraDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! TipoMantenedoraDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdTipoMantenedora) {
		return delete(cdTipoMantenedora, null);
	}

	public static int delete(int cdTipoMantenedora, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM acd_tipo_mantenedora WHERE cd_tipo_mantenedora=?");
			pstmt.setInt(1, cdTipoMantenedora);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TipoMantenedoraDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! TipoMantenedoraDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static TipoMantenedora get(int cdTipoMantenedora) {
		return get(cdTipoMantenedora, null);
	}

	public static TipoMantenedora get(int cdTipoMantenedora, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM acd_tipo_mantenedora WHERE cd_tipo_mantenedora=?");
			pstmt.setInt(1, cdTipoMantenedora);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new TipoMantenedora(rs.getInt("cd_tipo_mantenedora"),
						rs.getString("nm_tipo_mantenedora"),
						rs.getString("id_tipo_mantenedora"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TipoMantenedoraDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! TipoMantenedoraDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM acd_tipo_mantenedora");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TipoMantenedoraDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! TipoMantenedoraDAO.getAll: " + e);
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
		return Search.find("SELECT * FROM acd_tipo_mantenedora", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
