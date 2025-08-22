package com.tivic.manager.mob;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import java.util.ArrayList;

public class GrupoParadaInstituicaoDAO{

	public static int insert(GrupoParadaInstituicao objeto) {
		return insert(objeto, null);
	}

	public static int insert(GrupoParadaInstituicao objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO mob_grupo_parada_instituicao (cd_grupo_parada,"+
			                                  "cd_parada,"+
			                                  "cd_instituicao) VALUES (?, ?, ?)");
			if(objeto.getCdGrupoParada()==0)
				pstmt.setNull(1, Types.INTEGER);
			else
				pstmt.setInt(1,objeto.getCdGrupoParada());
			if(objeto.getCdParada()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdParada());
			if(objeto.getCdInstituicao()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdInstituicao());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! GrupoParadaInstituicaoDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! GrupoParadaInstituicaoDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(GrupoParadaInstituicao objeto) {
		return update(objeto, 0, 0, 0, null);
	}

	public static int update(GrupoParadaInstituicao objeto, int cdGrupoParadaOld, int cdParadaOld, int cdInstituicaoOld) {
		return update(objeto, cdGrupoParadaOld, cdParadaOld, cdInstituicaoOld, null);
	}

	public static int update(GrupoParadaInstituicao objeto, Connection connect) {
		return update(objeto, 0, 0, 0, connect);
	}

	public static int update(GrupoParadaInstituicao objeto, int cdGrupoParadaOld, int cdParadaOld, int cdInstituicaoOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE mob_grupo_parada_instituicao SET cd_grupo_parada=?,"+
												      		   "cd_parada=?,"+
												      		   "cd_instituicao=? WHERE cd_grupo_parada=? AND cd_parada=? AND cd_instituicao=?");
			pstmt.setInt(1,objeto.getCdGrupoParada());
			pstmt.setInt(2,objeto.getCdParada());
			pstmt.setInt(3,objeto.getCdInstituicao());
			pstmt.setInt(4, cdGrupoParadaOld!=0 ? cdGrupoParadaOld : objeto.getCdGrupoParada());
			pstmt.setInt(5, cdParadaOld!=0 ? cdParadaOld : objeto.getCdParada());
			pstmt.setInt(6, cdInstituicaoOld!=0 ? cdInstituicaoOld : objeto.getCdInstituicao());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! GrupoParadaInstituicaoDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! GrupoParadaInstituicaoDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdGrupoParada, int cdParada, int cdInstituicao) {
		return delete(cdGrupoParada, cdParada, cdInstituicao, null);
	}

	public static int delete(int cdGrupoParada, int cdParada, int cdInstituicao, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM mob_grupo_parada_instituicao WHERE cd_grupo_parada=? AND cd_parada=? AND cd_instituicao=?");
			pstmt.setInt(1, cdGrupoParada);
			pstmt.setInt(2, cdParada);
			pstmt.setInt(3, cdInstituicao);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! GrupoParadaInstituicaoDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! GrupoParadaInstituicaoDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static GrupoParadaInstituicao get(int cdGrupoParada, int cdParada, int cdInstituicao) {
		return get(cdGrupoParada, cdParada, cdInstituicao, null);
	}

	public static GrupoParadaInstituicao get(int cdGrupoParada, int cdParada, int cdInstituicao, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM mob_grupo_parada_instituicao WHERE cd_grupo_parada=? AND cd_parada=? AND cd_instituicao=?");
			pstmt.setInt(1, cdGrupoParada);
			pstmt.setInt(2, cdParada);
			pstmt.setInt(3, cdInstituicao);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new GrupoParadaInstituicao(rs.getInt("cd_grupo_parada"),
						rs.getInt("cd_parada"),
						rs.getInt("cd_instituicao"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! GrupoParadaInstituicaoDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! GrupoParadaInstituicaoDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM mob_grupo_parada_instituicao");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! GrupoParadaInstituicaoDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! GrupoParadaInstituicaoDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<GrupoParadaInstituicao> getList() {
		return getList(null);
	}

	public static ArrayList<GrupoParadaInstituicao> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<GrupoParadaInstituicao> list = new ArrayList<GrupoParadaInstituicao>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				GrupoParadaInstituicao obj = GrupoParadaInstituicaoDAO.get(rsm.getInt("cd_grupo_parada"), rsm.getInt("cd_parada"), rsm.getInt("cd_instituicao"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! GrupoParadaInstituicaoDAO.getList: " + e);
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
		return Search.find("SELECT * FROM mob_grupo_parada_instituicao", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}