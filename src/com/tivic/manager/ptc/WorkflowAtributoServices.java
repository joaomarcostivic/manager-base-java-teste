package com.tivic.manager.ptc;

import java.sql.*;
import java.sql.PreparedStatement;
import java.util.ArrayList;

import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.util.Result;

import com.tivic.sol.connection.Conexao;

public class WorkflowAtributoServices {

	public static Result save(WorkflowAtributo workflowAtributo){
		return save(workflowAtributo, null);
	}

	public static Result save(WorkflowAtributo workflowAtributo, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			if(workflowAtributo==null)
				return new Result(-1, "Erro ao salvar. WorkflowAtributo é nulo");

			int retorno;
			if(workflowAtributo.getCdAtributo()==0){
				retorno = WorkflowAtributoDAO.insert(workflowAtributo, connect);
				workflowAtributo.setCdAtributo(retorno);
			}
			else {
				retorno = WorkflowAtributoDAO.update(workflowAtributo, connect);
			}

			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();

			return new Result(retorno, (retorno<=0)?"Erro ao salvar...":"Salvo com sucesso...", "WORKFLOWATRIBUTO", workflowAtributo);
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
	public static Result remove(int cdAtributo, int cdEntidade){
		return remove(cdAtributo, cdEntidade, false, null);
	}
	public static Result remove(int cdAtributo, int cdEntidade, boolean cascade){
		return remove(cdAtributo, cdEntidade, cascade, null);
	}
	public static Result remove(int cdAtributo, int cdEntidade, boolean cascade, Connection connect){
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
			retorno = WorkflowAtributoDAO.delete(cdAtributo, cdEntidade, connect);
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
			pstmt = connect.prepareStatement("SELECT A.*, B.nm_entidade "
					+ " FROM ptc_workflow_atributo A "
					+ " JOIN ptc_workflow_entidade B ON (A.cd_entidade = B.cd_entidade)"
					+ " ORDER BY nm_entidade, nm_atributo");
				return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! WorkflowAtributoServices.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! WorkflowAtributoServices.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static ResultSetMap getAllByEntidade(int cdEntidade) {
		return getAllByEntidade(cdEntidade, null);
	}

	public static ResultSetMap getAllByEntidade(int cdEntidade, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement("SELECT A.*, B.nm_entidade "
					+ " FROM ptc_workflow_atributo A "
					+ " JOIN ptc_workflow_entidade B ON (A.cd_entidade = B.cd_entidade)"
					+ " WHERE A.cd_entidade="+cdEntidade
					+ " ORDER BY nm_entidade, nm_atributo");
				return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! WorkflowAtributoServices.getAllByEntidade: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! WorkflowAtributoServices.getAllByEntidade: " + e);
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
		return Search.find("SELECT * FROM ptc_workflow_atributo", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
