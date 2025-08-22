package com.tivic.manager.seg;

import java.sql.*;
import java.sql.PreparedStatement;
import java.util.ArrayList;

import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.util.Result;

import com.tivic.sol.connection.Conexao;
import com.tivic.manager.grl.ParametroServices;
import com.tivic.manager.seg.AuthData;

public class UsuarioEmpresaServices {

	public static Result save(UsuarioEmpresa usuarioEmpresa){
		return save(usuarioEmpresa, null, null);
	}

	public static Result save(UsuarioEmpresa usuarioEmpresa, AuthData authData){
		return save(usuarioEmpresa, authData, null);
	}

	public static Result save(UsuarioEmpresa usuarioEmpresa, AuthData authData, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			if(usuarioEmpresa==null)
				return new Result(-1, "Erro ao salvar. UsuarioEmpresa é nulo");

			int retorno;
			if(usuarioEmpresa.getCdEmpresa()==0){
				retorno = UsuarioEmpresaDAO.insert(usuarioEmpresa, connect);
				usuarioEmpresa.setCdEmpresa(retorno);
			}
			else {
				retorno = UsuarioEmpresaDAO.update(usuarioEmpresa, connect);
			}

			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();

			return new Result(retorno, (retorno<=0)?"Erro ao salvar...":"Salvo com sucesso...", "USUARIOEMPRESA", usuarioEmpresa);
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
	public static Result remove(UsuarioEmpresa usuarioEmpresa) {
		return remove(usuarioEmpresa.getCdEmpresa(), usuarioEmpresa.getCdUsuario(), usuarioEmpresa.getNrHorario());
	}
	public static Result remove(int cdEmpresa, int cdUsuario, int nrHorario){
		return remove(cdEmpresa, cdUsuario, nrHorario, false, null, null);
	}
	public static Result remove(int cdEmpresa, int cdUsuario, int nrHorario, boolean cascade){
		return remove(cdEmpresa, cdUsuario, nrHorario, cascade, null, null);
	}
	public static Result remove(int cdEmpresa, int cdUsuario, int nrHorario, boolean cascade, AuthData authData){
		return remove(cdEmpresa, cdUsuario, nrHorario, cascade, authData, null);
	}
	public static Result remove(int cdEmpresa, int cdUsuario, int nrHorario, boolean cascade, AuthData authData, Connection connect){
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
			retorno = UsuarioEmpresaDAO.delete(cdEmpresa, cdUsuario, nrHorario, connect);
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
			pstmt = connect.prepareStatement("SELECT * FROM seg_usuario_empresa");
				return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! UsuarioEmpresaServices.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! UsuarioEmpresaServices.getAll: " + e);
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
		return Search.find("SELECT * FROM seg_usuario_empresa", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}