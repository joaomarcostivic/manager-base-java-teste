package com.tivic.manager.acd;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import java.util.ArrayList;

public class TipoGrupoEscolarDAO{

	public static int insert(TipoGrupoEscolar objeto) {
		return insert(objeto, null);
	}

	public static int insert(TipoGrupoEscolar objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			int code = Conexao.getSequenceCode("acd_tipo_grupo_escolar", connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdTipoGrupo(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO acd_tipo_grupo_escolar (cd_tipo_grupo,"+
			                                  "nm_grupo,"+
			                                  "id_grupo) VALUES (?, ?, ?)");
			pstmt.setInt(1, code);
			pstmt.setString(2,objeto.getNmGrupo());
			pstmt.setString(3,objeto.getIdGrupo());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TipoGrupoEscolarDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! TipoGrupoEscolarDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(TipoGrupoEscolar objeto) {
		return update(objeto, 0, null);
	}

	public static int update(TipoGrupoEscolar objeto, int cdTipoGrupoOld) {
		return update(objeto, cdTipoGrupoOld, null);
	}

	public static int update(TipoGrupoEscolar objeto, Connection connect) {
		return update(objeto, 0, connect);
	}

	public static int update(TipoGrupoEscolar objeto, int cdTipoGrupoOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE acd_tipo_grupo_escolar SET cd_tipo_grupo=?,"+
												      		   "nm_grupo=?,"+
												      		   "id_grupo=? WHERE cd_tipo_grupo=?");
			pstmt.setInt(1,objeto.getCdTipoGrupo());
			pstmt.setString(2,objeto.getNmGrupo());
			pstmt.setString(3,objeto.getIdGrupo());
			pstmt.setInt(4, cdTipoGrupoOld!=0 ? cdTipoGrupoOld : objeto.getCdTipoGrupo());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TipoGrupoEscolarDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! TipoGrupoEscolarDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdTipoGrupo) {
		return delete(cdTipoGrupo, null);
	}

	public static int delete(int cdTipoGrupo, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM acd_tipo_grupo_escolar WHERE cd_tipo_grupo=?");
			pstmt.setInt(1, cdTipoGrupo);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TipoGrupoEscolarDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! TipoGrupoEscolarDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static TipoGrupoEscolar get(int cdTipoGrupo) {
		return get(cdTipoGrupo, null);
	}

	public static TipoGrupoEscolar get(int cdTipoGrupo, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM acd_tipo_grupo_escolar WHERE cd_tipo_grupo=?");
			pstmt.setInt(1, cdTipoGrupo);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new TipoGrupoEscolar(rs.getInt("cd_tipo_grupo"),
						rs.getString("nm_grupo"),
						rs.getString("id_grupo"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TipoGrupoEscolarDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! TipoGrupoEscolarDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM acd_tipo_grupo_escolar");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TipoGrupoEscolarDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! TipoGrupoEscolarDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<TipoGrupoEscolar> getList() {
		return getList(null);
	}

	public static ArrayList<TipoGrupoEscolar> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<TipoGrupoEscolar> list = new ArrayList<TipoGrupoEscolar>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				TipoGrupoEscolar obj = TipoGrupoEscolarDAO.get(rsm.getInt("cd_tipo_grupo"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! TipoGrupoEscolarDAO.getList: " + e);
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
		return Search.find("SELECT * FROM acd_tipo_grupo_escolar", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
