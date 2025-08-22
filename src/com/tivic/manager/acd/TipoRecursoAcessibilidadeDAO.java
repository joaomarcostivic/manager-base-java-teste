package com.tivic.manager.acd;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import java.util.ArrayList;

public class TipoRecursoAcessibilidadeDAO{

	public static int insert(TipoRecursoAcessibilidade objeto) {
		return insert(objeto, null);
	}

	public static int insert(TipoRecursoAcessibilidade objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			int code = Conexao.getSequenceCode("acd_tipo_recurso_acessibilidade", connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdTipoRecursoAcessibilidade(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO acd_tipo_recurso_acessibilidade (cd_tipo_recurso_acessibilidade,"+
			                                  "nm_tipo_recurso_acessibilidade,"+
			                                  "id_tipo_recurso_acessibilidade,"+
			                                  "st_tipo_recurso_acessibilidade) VALUES (?, ?, ?, ?)");
			pstmt.setInt(1, code);
			pstmt.setString(2,objeto.getNmTipoRecursoAcessibilidade());
			pstmt.setString(3,objeto.getIdTipoRecursoAcessibilidade());
			pstmt.setInt(4,objeto.getStTipoRecursoAcessibilidade());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TipoRecursoAcessibilidadeDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! TipoRecursoAcessibilidadeDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(TipoRecursoAcessibilidade objeto) {
		return update(objeto, 0, null);
	}

	public static int update(TipoRecursoAcessibilidade objeto, int cdTipoRecursoAcessibilidadeOld) {
		return update(objeto, cdTipoRecursoAcessibilidadeOld, null);
	}

	public static int update(TipoRecursoAcessibilidade objeto, Connection connect) {
		return update(objeto, 0, connect);
	}

	public static int update(TipoRecursoAcessibilidade objeto, int cdTipoRecursoAcessibilidadeOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE acd_tipo_recurso_acessibilidade SET cd_tipo_recurso_acessibilidade=?,"+
												      		   "nm_tipo_recurso_acessibilidade=?,"+
												      		   "id_tipo_recurso_acessibilidade=?,"+
												      		   "st_tipo_recurso_acessibilidade=? WHERE cd_tipo_recurso_acessibilidade=?");
			pstmt.setInt(1,objeto.getCdTipoRecursoAcessibilidade());
			pstmt.setString(2,objeto.getNmTipoRecursoAcessibilidade());
			pstmt.setString(3,objeto.getIdTipoRecursoAcessibilidade());
			pstmt.setInt(4,objeto.getStTipoRecursoAcessibilidade());
			pstmt.setInt(5, cdTipoRecursoAcessibilidadeOld!=0 ? cdTipoRecursoAcessibilidadeOld : objeto.getCdTipoRecursoAcessibilidade());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TipoRecursoAcessibilidadeDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! TipoRecursoAcessibilidadeDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdTipoRecursoAcessibilidade) {
		return delete(cdTipoRecursoAcessibilidade, null);
	}

	public static int delete(int cdTipoRecursoAcessibilidade, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM acd_tipo_recurso_acessibilidade WHERE cd_tipo_recurso_acessibilidade=?");
			pstmt.setInt(1, cdTipoRecursoAcessibilidade);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TipoRecursoAcessibilidadeDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! TipoRecursoAcessibilidadeDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static TipoRecursoAcessibilidade get(int cdTipoRecursoAcessibilidade) {
		return get(cdTipoRecursoAcessibilidade, null);
	}

	public static TipoRecursoAcessibilidade get(int cdTipoRecursoAcessibilidade, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM acd_tipo_recurso_acessibilidade WHERE cd_tipo_recurso_acessibilidade=?");
			pstmt.setInt(1, cdTipoRecursoAcessibilidade);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new TipoRecursoAcessibilidade(rs.getInt("cd_tipo_recurso_acessibilidade"),
						rs.getString("nm_tipo_recurso_acessibilidade"),
						rs.getString("id_tipo_recurso_acessibilidade"),
						rs.getInt("st_tipo_recurso_acessibilidade"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TipoRecursoAcessibilidadeDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! TipoRecursoAcessibilidadeDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM acd_tipo_recurso_acessibilidade");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TipoRecursoAcessibilidadeDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! TipoRecursoAcessibilidadeDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<TipoRecursoAcessibilidade> getList() {
		return getList(null);
	}

	public static ArrayList<TipoRecursoAcessibilidade> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<TipoRecursoAcessibilidade> list = new ArrayList<TipoRecursoAcessibilidade>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				TipoRecursoAcessibilidade obj = TipoRecursoAcessibilidadeDAO.get(rsm.getInt("cd_tipo_recurso_acessibilidade"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! TipoRecursoAcessibilidadeDAO.getList: " + e);
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
		return Search.find("SELECT * FROM acd_tipo_recurso_acessibilidade", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}