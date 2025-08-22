package com.tivic.manager.seg;

import java.sql.*;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import java.util.ArrayList;

import com.tivic.sol.connection.Conexao;

public class GrupoSistemaDAO{

	public static int insert(GrupoSistema objeto) {
		return insert(objeto, null);
	}

	public static int insert(GrupoSistema objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO seg_grupo_modulo (cd_modulo,"+
			                                  "cd_sistema,"+
			                                  "cd_grupo,"+
			                                  "lg_natureza) VALUES (?, ?, ?, ?)");
			if(objeto.getCdModulo()==0)
				pstmt.setNull(1, Types.INTEGER);
			else
				pstmt.setInt(1,objeto.getCdModulo());
			if(objeto.getCdSistema()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdSistema());
			if(objeto.getCdGrupo()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdGrupo());
			pstmt.setInt(4,objeto.getLgNatureza());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! GrupoModuloDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! GrupoModuloDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(GrupoSistema objeto) {
		return update(objeto, 0, 0, 0, null);
	}

	public static int update(GrupoSistema objeto, int cdModuloOld, int cdSistemaOld, int cdGrupoOld) {
		return update(objeto, cdModuloOld, cdSistemaOld, cdGrupoOld, null);
	}

	public static int update(GrupoSistema objeto, Connection connect) {
		return update(objeto, 0, 0, 0, connect);
	}

	public static int update(GrupoSistema objeto, int cdModuloOld, int cdSistemaOld, int cdGrupoOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE seg_grupo_modulo SET cd_modulo=?,"+
												      		   "cd_sistema=?,"+
												      		   "cd_grupo=?,"+
												      		   "lg_natureza=? WHERE cd_modulo=? AND cd_sistema=? AND cd_grupo=?");
			pstmt.setInt(1,objeto.getCdModulo());
			pstmt.setInt(2,objeto.getCdSistema());
			pstmt.setInt(3,objeto.getCdGrupo());
			pstmt.setInt(4,objeto.getLgNatureza());
			pstmt.setInt(5, cdModuloOld!=0 ? cdModuloOld : objeto.getCdModulo());
			pstmt.setInt(6, cdSistemaOld!=0 ? cdSistemaOld : objeto.getCdSistema());
			pstmt.setInt(7, cdGrupoOld!=0 ? cdGrupoOld : objeto.getCdGrupo());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! GrupoModuloDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! GrupoModuloDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdModulo, int cdSistema, int cdGrupo) {
		return delete(cdModulo, cdSistema, cdGrupo, null);
	}

	public static int delete(int cdModulo, int cdSistema, int cdGrupo, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM seg_grupo_modulo WHERE cd_modulo=? AND cd_sistema=? AND cd_grupo=?");
			pstmt.setInt(1, cdModulo);
			pstmt.setInt(2, cdSistema);
			pstmt.setInt(3, cdGrupo);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! GrupoModuloDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! GrupoModuloDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static GrupoSistema get(int cdModulo, int cdSistema, int cdGrupo) {
		return get(cdModulo, cdSistema, cdGrupo, null);
	}

	public static GrupoSistema get(int cdModulo, int cdSistema, int cdGrupo, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM seg_grupo_modulo WHERE cd_modulo=? AND cd_sistema=? AND cd_grupo=?");
			pstmt.setInt(1, cdModulo);
			pstmt.setInt(2, cdSistema);
			pstmt.setInt(3, cdGrupo);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new GrupoSistema(rs.getInt("cd_modulo"),
						rs.getInt("cd_sistema"),
						rs.getInt("cd_grupo"),
						rs.getInt("lg_natureza"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! GrupoModuloDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! GrupoModuloDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM seg_grupo_modulo");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! GrupoModuloDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! GrupoModuloDAO.getAll: " + e);
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
		return Search.find("SELECT * FROM seg_grupo_modulo", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
