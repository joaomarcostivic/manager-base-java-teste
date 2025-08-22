package com.tivic.manager.prc;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import java.util.ArrayList;

public class GrupoProcessoDAO{

	public static int insert(GrupoProcesso objeto) {
		return insert(objeto, null);
	}

	public static int insert(GrupoProcesso objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			int code = Conexao.getSequenceCode("prc_grupo_processo", connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdGrupoProcesso(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO prc_grupo_processo (cd_grupo_processo,"+
			                                  "nm_grupo_processo,"+
			                                  "id_grupo_processo,"+
			                                  "nm_email,"+
			                                  "cd_categoria_economica,"+
			                                  "st_grupo_processo) VALUES (?, ?, ?, ?, ?, ?)");
			pstmt.setInt(1, code);
			pstmt.setString(2,objeto.getNmGrupoProcesso());
			pstmt.setString(3,objeto.getIdGrupoProcesso());
			pstmt.setString(4,objeto.getNmEmail());
			if(objeto.getCdCategoriaEconomica()==0)
				pstmt.setNull(5, Types.INTEGER);
			else
				pstmt.setInt(5,objeto.getCdCategoriaEconomica());
			pstmt.setInt(6,objeto.getStGrupoProcesso());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! GrupoProcessoDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! GrupoProcessoDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(GrupoProcesso objeto) {
		return update(objeto, 0, null);
	}

	public static int update(GrupoProcesso objeto, int cdGrupoProcessoOld) {
		return update(objeto, cdGrupoProcessoOld, null);
	}

	public static int update(GrupoProcesso objeto, Connection connect) {
		return update(objeto, 0, connect);
	}

	public static int update(GrupoProcesso objeto, int cdGrupoProcessoOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE prc_grupo_processo SET cd_grupo_processo=?,"+
												      		   "nm_grupo_processo=?,"+
												      		   "id_grupo_processo=?,"+
												      		   "nm_email=?,"+
												      		   "cd_categoria_economica=?,"+
												      		   "st_grupo_processo=? WHERE cd_grupo_processo=?");
			pstmt.setInt(1,objeto.getCdGrupoProcesso());
			pstmt.setString(2,objeto.getNmGrupoProcesso());
			pstmt.setString(3,objeto.getIdGrupoProcesso());
			pstmt.setString(4,objeto.getNmEmail());
			if(objeto.getCdCategoriaEconomica()==0)
				pstmt.setNull(5, Types.INTEGER);
			else
				pstmt.setInt(5,objeto.getCdCategoriaEconomica());
			pstmt.setInt(6,objeto.getStGrupoProcesso());
			pstmt.setInt(7, cdGrupoProcessoOld!=0 ? cdGrupoProcessoOld : objeto.getCdGrupoProcesso());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! GrupoProcessoDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! GrupoProcessoDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdGrupoProcesso) {
		return delete(cdGrupoProcesso, null);
	}

	public static int delete(int cdGrupoProcesso, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM prc_grupo_processo WHERE cd_grupo_processo=?");
			pstmt.setInt(1, cdGrupoProcesso);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! GrupoProcessoDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! GrupoProcessoDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static GrupoProcesso get(int cdGrupoProcesso) {
		return get(cdGrupoProcesso, null);
	}

	public static GrupoProcesso get(int cdGrupoProcesso, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM prc_grupo_processo WHERE cd_grupo_processo=?");
			pstmt.setInt(1, cdGrupoProcesso);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new GrupoProcesso(rs.getInt("cd_grupo_processo"),
						rs.getString("nm_grupo_processo"),
						rs.getString("id_grupo_processo"),
						rs.getString("nm_email"),
						rs.getInt("cd_categoria_economica"),
						rs.getInt("st_grupo_processo"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! GrupoProcessoDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! GrupoProcessoDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM prc_grupo_processo");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! GrupoProcessoDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! GrupoProcessoDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<GrupoProcesso> getList() {
		return getList(null);
	}

	public static ArrayList<GrupoProcesso> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<GrupoProcesso> list = new ArrayList<GrupoProcesso>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				GrupoProcesso obj = GrupoProcessoDAO.get(rsm.getInt("cd_grupo_processo"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! GrupoProcessoDAO.getList: " + e);
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
		return Search.find("SELECT * FROM prc_grupo_processo", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}