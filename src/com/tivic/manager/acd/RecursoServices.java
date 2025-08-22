package com.tivic.manager.acd;

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

public class RecursoServices {

	public static Result save(Recurso recurso){
		return save(recurso, null, null);
	}

	public static Result save(Recurso recurso, AuthData authData){
		return save(recurso, authData, null);
	}

	public static Result save(Recurso recurso, AuthData authData, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			if(recurso==null)
				return new Result(-1, "Erro ao salvar. Recurso é nulo");

			int retorno;
			if(recurso.getCdRecurso()==0){
				retorno = RecursoDAO.insert(recurso, connect);
				recurso.setCdRecurso(retorno);
			}
			else {
				retorno = RecursoDAO.update(recurso, connect);
			}

			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();

			return new Result(retorno, (retorno<=0)?"Erro ao salvar...":"Salvo com sucesso...", "RECURSO", recurso);
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
	public static Result remove(Recurso recurso) {
		return remove(recurso.getCdRecurso(), recurso.getCdTipoRecurso());
	}
	public static Result remove(int cdRecurso, int cdTipoRecurso){
		return remove(cdRecurso, cdTipoRecurso, false, null, null);
	}
	public static Result remove(int cdRecurso, int cdTipoRecurso, boolean cascade){
		return remove(cdRecurso, cdTipoRecurso, cascade, null, null);
	}
	public static Result remove(int cdRecurso, int cdTipoRecurso, boolean cascade, AuthData authData){
		return remove(cdRecurso, cdTipoRecurso, cascade, authData, null);
	}
	public static Result remove(int cdRecurso, int cdTipoRecurso, boolean cascade, AuthData authData, Connection connect){
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
			retorno = RecursoDAO.delete(cdRecurso, cdTipoRecurso, connect);
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
			pstmt = connect.prepareStatement("SELECT * FROM acd_recurso");
				return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! RecursoServices.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! RecursoServices.getAll: " + e);
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
		return Search.find("SELECT * FROM acd_recurso", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}