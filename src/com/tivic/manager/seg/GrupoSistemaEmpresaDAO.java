package com.tivic.manager.seg;

import java.sql.*;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import java.util.ArrayList;

import com.tivic.sol.connection.Conexao;

public class GrupoSistemaEmpresaDAO{

	public static int insert(GrupoSistemaEmpresa objeto) {
		return insert(objeto, null);
	}

	public static int insert(GrupoSistemaEmpresa objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO seg_grupo_modulo_empresa (cd_modulo,"+
			                                  "cd_sistema,"+
			                                  "cd_grupo,"+
			                                  "cd_empresa) VALUES (?, ?, ?, ?)");
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
			if(objeto.getCdEmpresa()==0)
				pstmt.setNull(4, Types.INTEGER);
			else
				pstmt.setInt(4,objeto.getCdEmpresa());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! GrupoModuloEmpresaDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! GrupoModuloEmpresaDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(GrupoSistemaEmpresa objeto) {
		return update(objeto, 0, 0, 0, 0, null);
	}

	public static int update(GrupoSistemaEmpresa objeto, int cdModuloOld, int cdSistemaOld, int cdGrupoOld, int cdEmpresaOld) {
		return update(objeto, cdModuloOld, cdSistemaOld, cdGrupoOld, cdEmpresaOld, null);
	}

	public static int update(GrupoSistemaEmpresa objeto, Connection connect) {
		return update(objeto, 0, 0, 0, 0, connect);
	}

	public static int update(GrupoSistemaEmpresa objeto, int cdModuloOld, int cdSistemaOld, int cdGrupoOld, int cdEmpresaOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE seg_grupo_modulo_empresa SET cd_modulo=?,"+
												      		   "cd_sistema=?,"+
												      		   "cd_grupo=?,"+
												      		   "cd_empresa=? WHERE cd_modulo=? AND cd_sistema=? AND cd_grupo=? AND cd_empresa=?");
			pstmt.setInt(1,objeto.getCdModulo());
			pstmt.setInt(2,objeto.getCdSistema());
			pstmt.setInt(3,objeto.getCdGrupo());
			pstmt.setInt(4,objeto.getCdEmpresa());
			pstmt.setInt(5, cdModuloOld!=0 ? cdModuloOld : objeto.getCdModulo());
			pstmt.setInt(6, cdSistemaOld!=0 ? cdSistemaOld : objeto.getCdSistema());
			pstmt.setInt(7, cdGrupoOld!=0 ? cdGrupoOld : objeto.getCdGrupo());
			pstmt.setInt(8, cdEmpresaOld!=0 ? cdEmpresaOld : objeto.getCdEmpresa());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! GrupoModuloEmpresaDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! GrupoModuloEmpresaDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdModulo, int cdSistema, int cdGrupo, int cdEmpresa) {
		return delete(cdModulo, cdSistema, cdGrupo, cdEmpresa, null);
	}

	public static int delete(int cdModulo, int cdSistema, int cdGrupo, int cdEmpresa, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM seg_grupo_modulo_empresa WHERE cd_modulo=? AND cd_sistema=? AND cd_grupo=? AND cd_empresa=?");
			pstmt.setInt(1, cdModulo);
			pstmt.setInt(2, cdSistema);
			pstmt.setInt(3, cdGrupo);
			pstmt.setInt(4, cdEmpresa);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! GrupoModuloEmpresaDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! GrupoModuloEmpresaDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static GrupoSistemaEmpresa get(int cdModulo, int cdSistema, int cdGrupo, int cdEmpresa) {
		return get(cdModulo, cdSistema, cdGrupo, cdEmpresa, null);
	}

	public static GrupoSistemaEmpresa get(int cdModulo, int cdSistema, int cdGrupo, int cdEmpresa, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM seg_grupo_modulo_empresa WHERE cd_modulo=? AND cd_sistema=? AND cd_grupo=? AND cd_empresa=?");
			pstmt.setInt(1, cdModulo);
			pstmt.setInt(2, cdSistema);
			pstmt.setInt(3, cdGrupo);
			pstmt.setInt(4, cdEmpresa);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new GrupoSistemaEmpresa(rs.getInt("cd_modulo"),
						rs.getInt("cd_sistema"),
						rs.getInt("cd_grupo"),
						rs.getInt("cd_empresa"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! GrupoModuloEmpresaDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! GrupoModuloEmpresaDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM seg_grupo_modulo_empresa");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! GrupoModuloEmpresaDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! GrupoModuloEmpresaDAO.getAll: " + e);
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
		return Search.find("SELECT * FROM seg_grupo_modulo_empresa", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
