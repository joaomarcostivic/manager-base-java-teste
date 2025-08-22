package com.tivic.manager.cae;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import java.util.ArrayList;

public class GrupoIngredienteDAO{

	public static int insert(GrupoIngrediente objeto) {
		return insert(objeto, null);
	}

	public static int insert(GrupoIngrediente objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			int code = Conexao.getSequenceCode("cae_grupo_ingrediente", connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdGrupo(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO cae_grupo_ingrediente (cd_grupo,"+
			                                  "nm_grupo,"+
			                                  "sg_grupo) VALUES (?, ?, ?)");
			pstmt.setInt(1, code);
			pstmt.setString(2,objeto.getNmGrupo());
			pstmt.setString(3,objeto.getSgGrupo());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! GrupoIngredienteDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! GrupoIngredienteDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(GrupoIngrediente objeto) {
		return update(objeto, 0, null);
	}

	public static int update(GrupoIngrediente objeto, int cdGrupoOld) {
		return update(objeto, cdGrupoOld, null);
	}

	public static int update(GrupoIngrediente objeto, Connection connect) {
		return update(objeto, 0, connect);
	}

	public static int update(GrupoIngrediente objeto, int cdGrupoOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE cae_grupo_ingrediente SET cd_grupo=?,"+
												      		   "nm_grupo=?,"+
												      		   "sg_grupo=? WHERE cd_grupo=?");
			pstmt.setInt(1,objeto.getCdGrupo());
			pstmt.setString(2,objeto.getNmGrupo());
			pstmt.setString(3,objeto.getSgGrupo());
			pstmt.setInt(4, cdGrupoOld!=0 ? cdGrupoOld : objeto.getCdGrupo());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! GrupoIngredienteDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! GrupoIngredienteDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdGrupo) {
		return delete(cdGrupo, null);
	}

	public static int delete(int cdGrupo, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM cae_grupo_ingrediente WHERE cd_grupo=?");
			pstmt.setInt(1, cdGrupo);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! GrupoIngredienteDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! GrupoIngredienteDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static GrupoIngrediente get(int cdGrupo) {
		return get(cdGrupo, null);
	}

	public static GrupoIngrediente get(int cdGrupo, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM cae_grupo_ingrediente WHERE cd_grupo=?");
			pstmt.setInt(1, cdGrupo);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new GrupoIngrediente(rs.getInt("cd_grupo"),
						rs.getString("nm_grupo"),
						rs.getString("sg_grupo"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! GrupoIngredienteDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! GrupoIngredienteDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM cae_grupo_ingrediente");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! GrupoIngredienteDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! GrupoIngredienteDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<GrupoIngrediente> getList() {
		return getList(null);
	}

	public static ArrayList<GrupoIngrediente> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<GrupoIngrediente> list = new ArrayList<GrupoIngrediente>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				GrupoIngrediente obj = GrupoIngredienteDAO.get(rsm.getInt("cd_grupo"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! GrupoIngredienteDAO.getList: " + e);
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
		return Search.find("SELECT * FROM cae_grupo_ingrediente", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
