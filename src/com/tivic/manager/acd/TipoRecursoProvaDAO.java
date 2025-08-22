package com.tivic.manager.acd;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import java.util.ArrayList;

public class TipoRecursoProvaDAO{

	public static int insert(TipoRecursoProva objeto) {
		return insert(objeto, null);
	}

	public static int insert(TipoRecursoProva objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			int code = Conexao.getSequenceCode("acd_tipo_recurso_prova", connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdTipoRecursoProva(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO acd_tipo_recurso_prova (cd_tipo_recurso_prova,"+
			                                  "nm_tipo_recurso,"+
			                                  "id_tipo_recurso) VALUES (?, ?, ?)");
			pstmt.setInt(1, code);
			pstmt.setString(2,objeto.getNmTipoRecurso());
			pstmt.setString(3,objeto.getIdTipoRecurso());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TipoRecursoProvaDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! TipoRecursoProvaDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(TipoRecursoProva objeto) {
		return update(objeto, 0, null);
	}

	public static int update(TipoRecursoProva objeto, int cdTipoRecursoProvaOld) {
		return update(objeto, cdTipoRecursoProvaOld, null);
	}

	public static int update(TipoRecursoProva objeto, Connection connect) {
		return update(objeto, 0, connect);
	}

	public static int update(TipoRecursoProva objeto, int cdTipoRecursoProvaOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE acd_tipo_recurso_prova SET cd_tipo_recurso_prova=?,"+
												      		   "nm_tipo_recurso=?,"+
												      		   "id_tipo_recurso=? WHERE cd_tipo_recurso_prova=?");
			pstmt.setInt(1,objeto.getCdTipoRecursoProva());
			pstmt.setString(2,objeto.getNmTipoRecurso());
			pstmt.setString(3,objeto.getIdTipoRecurso());
			pstmt.setInt(4, cdTipoRecursoProvaOld!=0 ? cdTipoRecursoProvaOld : objeto.getCdTipoRecursoProva());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TipoRecursoProvaDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! TipoRecursoProvaDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdTipoRecursoProva) {
		return delete(cdTipoRecursoProva, null);
	}

	public static int delete(int cdTipoRecursoProva, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM acd_tipo_recurso_prova WHERE cd_tipo_recurso_prova=?");
			pstmt.setInt(1, cdTipoRecursoProva);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TipoRecursoProvaDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! TipoRecursoProvaDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static TipoRecursoProva get(int cdTipoRecursoProva) {
		return get(cdTipoRecursoProva, null);
	}

	public static TipoRecursoProva get(int cdTipoRecursoProva, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM acd_tipo_recurso_prova WHERE cd_tipo_recurso_prova=?");
			pstmt.setInt(1, cdTipoRecursoProva);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new TipoRecursoProva(rs.getInt("cd_tipo_recurso_prova"),
						rs.getString("nm_tipo_recurso"),
						rs.getString("id_tipo_recurso"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TipoRecursoProvaDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! TipoRecursoProvaDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM acd_tipo_recurso_prova");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TipoRecursoProvaDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! TipoRecursoProvaDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<TipoRecursoProva> getList() {
		return getList(null);
	}

	public static ArrayList<TipoRecursoProva> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<TipoRecursoProva> list = new ArrayList<TipoRecursoProva>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				TipoRecursoProva obj = TipoRecursoProvaDAO.get(rsm.getInt("cd_tipo_recurso_prova"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! TipoRecursoProvaDAO.getList: " + e);
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
		return Search.find("SELECT * FROM acd_tipo_recurso_prova", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
