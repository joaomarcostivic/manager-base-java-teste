package com.tivic.manager.prc;

import java.sql.*;
import java.util.ArrayList;

import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.util.Result;

import com.tivic.sol.connection.Conexao;

public class UsuarioGrupoServices {

	public static Result save(UsuarioGrupo usuarioGrupo){
		return save(usuarioGrupo, null);
	}

	public static Result save(UsuarioGrupo usuarioGrupo, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			if(usuarioGrupo==null)
				return new Result(-1, "Erro ao salvar. UsuarioGrupo é nulo");

			int retorno;
			
			if(UsuarioGrupoDAO.get(usuarioGrupo.getCdGrupoProcesso(), usuarioGrupo.getCdUsuario(), connect)==null) {
				retorno = UsuarioGrupoDAO.insert(usuarioGrupo, connect);
			}
			else {
				retorno = UsuarioGrupoDAO.update(usuarioGrupo, connect);
			}

			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();

			return new Result(retorno, (retorno<=0)?"Erro ao salvar...":"Salvo com sucesso...", "USUARIOGRUPO", usuarioGrupo);
		}
		catch(Exception e){
			e.printStackTrace();
			if (isConnectionNull)
				Conexao.rollback(connect);
			return new Result(-1, e.getMessage());
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	public static Result remove(int cdGrupoProcesso, int cdUsuario){
		return remove(cdGrupoProcesso, cdUsuario, false, null);
	}
	public static Result remove(int cdGrupoProcesso, int cdUsuario, boolean cascade){
		return remove(cdGrupoProcesso, cdUsuario, cascade, null);
	}
	public static Result remove(int cdGrupoProcesso, int cdUsuario, boolean cascade, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			int retorno = 0;
			if(cascade){
				/** SE HOUVER, REMOVER TABELAS ASSOCIADAS **/
				retorno = 1;
			}
			if(!cascade || retorno>0)
			retorno = UsuarioGrupoDAO.delete(cdGrupoProcesso, cdUsuario, connect);
			if(retorno<=0){
				Conexao.rollback(connect);
				return new Result(-2, "Este registro está vinculado a outros e não pode ser excluído!");
			}
			else if (isConnectionNull)
				connect.commit();
			return new Result(1, "Registro excluído com sucesso!");
		}
		catch(Exception e){
			e.printStackTrace();
			if (isConnectionNull)
				Conexao.rollback(connect);
			return new Result(-1, "Erro ao excluir registro!");
		}
		finally{
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
			pstmt = connect.prepareStatement("SELECT * FROM PRC_USUARIO_GRUPO");
				return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! UsuarioGrupoServices.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! UsuarioGrupoServices.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static ResultSetMap getAllByUsuario(int cdUsuario) {
		return getAllByUsuario(cdUsuario, null);
	}

	public static ResultSetMap getAllByUsuario(int cdUsuario, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement("SELECT A.*, B.nm_grupo_processo, C.nm_login, D.cd_pessoa, D.nm_pessoa " +
					"FROM prc_usuario_grupo A " +
					"LEFT OUTER JOIN prc_grupo_processo B ON (B.cd_grupo_processo = A.cd_grupo_processo) " +
					"LEFT OUTER JOIN seg_usuario C ON (C.cd_usuario = A.cd_usuario) " +
					"LEFT OUTER JOIN grl_pessoa D ON (D.cd_pessoa = C.cd_pessoa) " +
					"WHERE A.cd_usuario = ? " +
					" ORDER BY B.nm_grupo_processo");
			pstmt.setInt(1, cdUsuario);
						
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static boolean hasGrupoProcesso(int cdUsuario) {
		return hasGrupoProcesso(cdUsuario, null);
	}

	public static boolean hasGrupoProcesso(int cdUsuario, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement("SELECT cd_grupo_processo " +
					"FROM prc_usuario_grupo "+
					"WHERE cd_usuario = ? ");
			pstmt.setInt(1, cdUsuario);
			
			ResultSet rs = pstmt.executeQuery();
			
			return rs.next();
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! UsuarioGrupoServices.hasGrupoProcesso: " + sqlExpt);
			return false;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! UsuarioGrupoServices.hasGrupoProcesso: " + e);
			return false;
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
		return Search.find("SELECT * FROM PRC_USUARIO_GRUPO", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
