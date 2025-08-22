package com.tivic.manager.bpm;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import java.util.ArrayList;

public class GrupoMarcaDAO{

	public static int insert(GrupoMarca objeto) {
		return insert(objeto, null);
	}

	public static int insert(GrupoMarca objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			int code = Conexao.getSequenceCode("bpm_grupo_marca", connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdGrupo(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO bpm_grupo_marca (cd_grupo,"+
			                                  "nm_grupo) VALUES (?, ?)");
			pstmt.setInt(1, code);
			pstmt.setString(2,objeto.getNmGrupo());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! GrupoMarcaDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! GrupoMarcaDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(GrupoMarca objeto) {
		return update(objeto, 0, null);
	}

	public static int update(GrupoMarca objeto, int cdGrupoOld) {
		return update(objeto, cdGrupoOld, null);
	}

	public static int update(GrupoMarca objeto, Connection connect) {
		return update(objeto, 0, connect);
	}

	public static int update(GrupoMarca objeto, int cdGrupoOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE bpm_grupo_marca SET cd_grupo=?,"+
												      		   "nm_grupo=? WHERE cd_grupo=?");
			pstmt.setInt(1,objeto.getCdGrupo());
			pstmt.setString(2,objeto.getNmGrupo());
			pstmt.setInt(3, cdGrupoOld!=0 ? cdGrupoOld : objeto.getCdGrupo());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! GrupoMarcaDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! GrupoMarcaDAO.update: " +  e);
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
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM bpm_grupo_marca WHERE cd_grupo=?");
			pstmt.setInt(1, cdGrupo);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! GrupoMarcaDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! GrupoMarcaDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static GrupoMarca get(int cdGrupo) {
		return get(cdGrupo, null);
	}

	public static GrupoMarca get(int cdGrupo, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM bpm_grupo_marca WHERE cd_grupo=?");
			pstmt.setInt(1, cdGrupo);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new GrupoMarca(rs.getInt("cd_grupo"),
						rs.getString("nm_grupo"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! GrupoMarcaDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! GrupoMarcaDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM bpm_grupo_marca");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! GrupoMarcaDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! GrupoMarcaDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<GrupoMarca> getList() {
		return getList(null);
	}

	public static ArrayList<GrupoMarca> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<GrupoMarca> list = new ArrayList<GrupoMarca>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				GrupoMarca obj = GrupoMarcaDAO.get(rsm.getInt("cd_grupo"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! GrupoMarcaDAO.getList: " + e);
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
		return Search.find("SELECT * FROM bpm_grupo_marca", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}