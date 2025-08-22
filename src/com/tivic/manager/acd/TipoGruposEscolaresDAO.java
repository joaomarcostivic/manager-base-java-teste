package com.tivic.manager.acd;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import java.util.ArrayList;

public class TipoGruposEscolaresDAO{

	public static int insert(TipoGruposEscolares objeto) {
		return insert(objeto, null);
	}

	public static int insert(TipoGruposEscolares objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			int code = Conexao.getSequenceCode("acd_tipo_grupos_escolares", connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdTipoGruposEscolares(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO acd_tipo_grupos_escolares (cd_tipo_grupos_escolares,"+
			                                  "nm_tipo_grupos_escolares,"+
			                                  "id_tipo_grupos_escolares,"+
			                                  "st_tipo_grupos_escolares) VALUES (?, ?, ?, ?)");
			pstmt.setInt(1, code);
			pstmt.setString(2,objeto.getNmTipoGruposEscolares());
			pstmt.setString(3,objeto.getIdTipoGruposEscolares());
			pstmt.setInt(4,objeto.getStTipoGruposEscolares());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TipoGruposEscolaresDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! TipoGruposEscolaresDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(TipoGruposEscolares objeto) {
		return update(objeto, 0, null);
	}

	public static int update(TipoGruposEscolares objeto, int cdTipoGruposEscolaresOld) {
		return update(objeto, cdTipoGruposEscolaresOld, null);
	}

	public static int update(TipoGruposEscolares objeto, Connection connect) {
		return update(objeto, 0, connect);
	}

	public static int update(TipoGruposEscolares objeto, int cdTipoGruposEscolaresOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE acd_tipo_grupos_escolares SET cd_tipo_grupos_escolares=?,"+
												      		   "nm_tipo_grupos_escolares=?,"+
												      		   "id_tipo_grupos_escolares=?,"+
												      		   "st_tipo_grupos_escolares=? WHERE cd_tipo_grupos_escolares=?");
			pstmt.setInt(1,objeto.getCdTipoGruposEscolares());
			pstmt.setString(2,objeto.getNmTipoGruposEscolares());
			pstmt.setString(3,objeto.getIdTipoGruposEscolares());
			pstmt.setInt(4,objeto.getStTipoGruposEscolares());
			pstmt.setInt(5, cdTipoGruposEscolaresOld!=0 ? cdTipoGruposEscolaresOld : objeto.getCdTipoGruposEscolares());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TipoGruposEscolaresDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! TipoGruposEscolaresDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdTipoGruposEscolares) {
		return delete(cdTipoGruposEscolares, null);
	}

	public static int delete(int cdTipoGruposEscolares, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM acd_tipo_grupos_escolares WHERE cd_tipo_grupos_escolares=?");
			pstmt.setInt(1, cdTipoGruposEscolares);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TipoGruposEscolaresDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! TipoGruposEscolaresDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static TipoGruposEscolares get(int cdTipoGruposEscolares) {
		return get(cdTipoGruposEscolares, null);
	}

	public static TipoGruposEscolares get(int cdTipoGruposEscolares, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM acd_tipo_grupos_escolares WHERE cd_tipo_grupos_escolares=?");
			pstmt.setInt(1, cdTipoGruposEscolares);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new TipoGruposEscolares(rs.getInt("cd_tipo_grupos_escolares"),
						rs.getString("nm_tipo_grupos_escolares"),
						rs.getString("id_tipo_grupos_escolares"),
						rs.getInt("st_tipo_grupos_escolares"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TipoGruposEscolaresDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! TipoGruposEscolaresDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM acd_tipo_grupos_escolares");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TipoGruposEscolaresDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! TipoGruposEscolaresDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<TipoGruposEscolares> getList() {
		return getList(null);
	}

	public static ArrayList<TipoGruposEscolares> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<TipoGruposEscolares> list = new ArrayList<TipoGruposEscolares>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				TipoGruposEscolares obj = TipoGruposEscolaresDAO.get(rsm.getInt("cd_tipo_grupos_escolares"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! TipoGruposEscolaresDAO.getList: " + e);
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
		return Search.find("SELECT * FROM acd_tipo_grupos_escolares", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}