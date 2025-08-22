package com.tivic.manager.ptc;

import java.sql.*;
import java.sql.PreparedStatement;
import java.util.ArrayList;

import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.util.Result;

import com.tivic.sol.connection.Conexao;

public class WorkflowEntidadeServices {

	public static Result save(WorkflowEntidade workflowEntidade){
		return save(workflowEntidade, null);
	}

	public static Result save(WorkflowEntidade workflowEntidade, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			if(workflowEntidade==null)
				return new Result(-1, "Erro ao salvar. WorkflowEntidade é nulo");

			int retorno;
			if(workflowEntidade.getCdEntidade()==0){
				retorno = WorkflowEntidadeDAO.insert(workflowEntidade, connect);
				workflowEntidade.setCdEntidade(retorno);
			}
			else {
				retorno = WorkflowEntidadeDAO.update(workflowEntidade, connect);
			}

			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();

			return new Result(retorno, (retorno<=0)?"Erro ao salvar...":"Salvo com sucesso...", "WORKFLOWENTIDADE", workflowEntidade);
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
	public static Result remove(int cdEntidade){
		return remove(cdEntidade, false, null);
	}
	public static Result remove(int cdEntidade, boolean cascade){
		return remove(cdEntidade, cascade, null);
	}
	public static Result remove(int cdEntidade, boolean cascade, Connection connect){
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
			retorno = WorkflowEntidadeDAO.delete(cdEntidade, connect);
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
			pstmt = connect.prepareStatement("SELECT * FROM ptc_workflow_entidade");
				return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! WorkflowEntidadeServices.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! WorkflowEntidadeServices.getAll: " + e);
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
		return Search.find("SELECT * FROM ptc_workflow_entidade", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
