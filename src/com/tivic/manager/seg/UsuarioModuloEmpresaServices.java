package com.tivic.manager.seg;

import java.sql.*;
import java.sql.PreparedStatement;
import java.util.ArrayList;

import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.util.Result;

import com.tivic.sol.connection.Conexao;

public class UsuarioModuloEmpresaServices {

	public static Result save(UsuarioModuloEmpresa usuarioModuloEmpresa){
		return save(usuarioModuloEmpresa, null);
	}

	public static Result save(UsuarioModuloEmpresa usuarioModuloEmpresa, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			if(usuarioModuloEmpresa==null)
				return new Result(-1, "Erro ao salvar. UsuarioModuloEmpresa é nulo");

			int retorno;
			UsuarioModuloEmpresa ume = UsuarioModuloEmpresaDAO.get(usuarioModuloEmpresa.getCdUsuario(), 
					usuarioModuloEmpresa.getCdEmpresa(), usuarioModuloEmpresa.getCdModulo(), usuarioModuloEmpresa.getCdSistema(), connect);
			if(ume==null){
				retorno = UsuarioModuloEmpresaDAO.insert(usuarioModuloEmpresa, connect);
				usuarioModuloEmpresa.setCdUsuario(retorno);
			}
			else {
				retorno = UsuarioModuloEmpresaDAO.update(usuarioModuloEmpresa, connect);
			}

			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();

			return new Result(retorno, (retorno<=0)?"Erro ao salvar...":"Salvo com sucesso...", "USUARIOMODULOEMPRESA", usuarioModuloEmpresa);
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
	public static Result remove(int cdUsuario, int cdEmpresa, int cdModulo, int cdSistema){
		return remove(cdUsuario, cdEmpresa, cdModulo, cdSistema, false, null);
	}
	public static Result remove(int cdUsuario, int cdEmpresa, int cdModulo, int cdSistema, boolean cascade){
		return remove(cdUsuario, cdEmpresa, cdModulo, cdSistema, cascade, null);
	}
	public static Result remove(int cdUsuario, int cdEmpresa, int cdModulo, int cdSistema, boolean cascade, Connection connect){
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
			retorno = UsuarioModuloEmpresaDAO.delete(cdUsuario, cdEmpresa, cdModulo, cdSistema, connect);
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
			pstmt = connect.prepareStatement("SELECT * FROM seg_usuario_modulo_empresa");
				return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! UsuarioModuloEmpresaServices.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! UsuarioModuloEmpresaServices.getAll: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM seg_usuario_modulo_empresa WHERE cd_usuario="+cdUsuario);
				return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! UsuarioModuloEmpresaServices.getAllByUsuario: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! UsuarioModuloEmpresaServices.getAllByUsuario: " + e);
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
		return Search.find("SELECT * FROM seg_usuario_modulo_empresa", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
