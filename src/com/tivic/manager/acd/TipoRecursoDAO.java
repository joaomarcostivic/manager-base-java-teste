package com.tivic.manager.acd;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import java.util.ArrayList;

public class TipoRecursoDAO{

	public static int insert(TipoRecurso objeto) {
		return insert(objeto, null);
	}

	public static int insert(TipoRecurso objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			int code = Conexao.getSequenceCode("acd_tipo_recurso", connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdTipoRecurso(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO acd_tipo_recurso (cd_tipo_recurso,"+
			                                  "nm_tipo_recurso,"+
			                                  "st_tipo_recurso) VALUES (?, ?, ?)");
			pstmt.setInt(1, code);
			pstmt.setString(2,objeto.getNmTipoRecurso());
			pstmt.setInt(3,objeto.getStTipoRecurso());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TipoRecursoDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! TipoRecursoDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(TipoRecurso objeto) {
		return update(objeto, 0, null);
	}

	public static int update(TipoRecurso objeto, int cdTipoRecursoOld) {
		return update(objeto, cdTipoRecursoOld, null);
	}

	public static int update(TipoRecurso objeto, Connection connect) {
		return update(objeto, 0, connect);
	}

	public static int update(TipoRecurso objeto, int cdTipoRecursoOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE acd_tipo_recurso SET cd_tipo_recurso=?,"+
												      		   "nm_tipo_recurso=?,"+
												      		   "st_tipo_recurso=? WHERE cd_tipo_recurso=?");
			pstmt.setInt(1,objeto.getCdTipoRecurso());
			pstmt.setString(2,objeto.getNmTipoRecurso());
			pstmt.setInt(3,objeto.getStTipoRecurso());
			pstmt.setInt(4, cdTipoRecursoOld!=0 ? cdTipoRecursoOld : objeto.getCdTipoRecurso());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TipoRecursoDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! TipoRecursoDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdTipoRecurso) {
		return delete(cdTipoRecurso, null);
	}

	public static int delete(int cdTipoRecurso, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM acd_tipo_recurso WHERE cd_tipo_recurso=?");
			pstmt.setInt(1, cdTipoRecurso);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TipoRecursoDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! TipoRecursoDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static TipoRecurso get(int cdTipoRecurso) {
		return get(cdTipoRecurso, null);
	}

	public static TipoRecurso get(int cdTipoRecurso, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM acd_tipo_recurso WHERE cd_tipo_recurso=?");
			pstmt.setInt(1, cdTipoRecurso);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new TipoRecurso(rs.getInt("cd_tipo_recurso"),
						rs.getString("nm_tipo_recurso"),
						rs.getInt("st_tipo_recurso"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TipoRecursoDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! TipoRecursoDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM acd_tipo_recurso");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TipoRecursoDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! TipoRecursoDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<TipoRecurso> getList() {
		return getList(null);
	}

	public static ArrayList<TipoRecurso> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<TipoRecurso> list = new ArrayList<TipoRecurso>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				TipoRecurso obj = TipoRecursoDAO.get(rsm.getInt("cd_tipo_recurso"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! TipoRecursoDAO.getList: " + e);
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
		return Search.find("SELECT * FROM acd_tipo_recurso", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}