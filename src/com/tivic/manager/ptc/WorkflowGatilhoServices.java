package com.tivic.manager.ptc;

import java.sql.*;
import java.util.ArrayList;

import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.util.Result;

import com.tivic.sol.connection.Conexao;

public class WorkflowGatilhoServices {
	
	public static final int TP_GATILHO_LANCAMENTO_OCORRENCIA = 0;
	public static final int TP_GATILHO_LANCAMENTO_PENDENCIA = 1;
	public static final int TP_GATILHO_BAIXA_PENDENCIA = 2;
	public static final int TP_GATILHO_LANCAMENTO_AGENDA = 3;
	public static final int TP_GATILHO_CUMPRIMENTO_AGENDA = 4;
	public static final int TP_GATILHO_MUDANCA_CAMPO = 5;

	public static Result save(WorkflowGatilho workflowGatilho){
		return save(workflowGatilho, null);
	}

	public static Result save(WorkflowGatilho workflowGatilho, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			if(workflowGatilho==null)
				return new Result(-1, "Erro ao salvar. WorkflowGatilho é nulo");

			int retorno;
			if(workflowGatilho.getCdGatilho()==0){
				retorno = WorkflowGatilhoDAO.insert(workflowGatilho, connect);
				workflowGatilho.setCdGatilho(retorno);
			}
			else {
				retorno = WorkflowGatilhoDAO.update(workflowGatilho, connect);
			}

			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();

			return new Result(retorno, (retorno<=0)?"Erro ao salvar...":"Salvo com sucesso...", "WORKFLOWGATILHO", workflowGatilho);
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
	public static Result remove(int cdGatilho, int cdRegra){
		return remove(cdGatilho, cdRegra, false, null);
	}
	public static Result remove(int cdGatilho, int cdRegra, boolean cascade){
		return remove(cdGatilho, cdRegra, cascade, null);
	}
	public static Result remove(int cdGatilho, int cdRegra, boolean cascade, Connection connect){
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
			retorno = WorkflowGatilhoDAO.delete(cdGatilho, cdRegra, connect);
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
	
	public static Result removeAllByRegra(int cdRegra, Connection connect) {
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM ptc_workflow_gatilho WHERE cd_regra=?");
			pstmt.setInt(1, cdRegra);
			pstmt.executeUpdate();
			return new Result(1, "Gatilhos removidos com sucesso");
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! WorkflowGatilhoServices.removeAllByRegra: " +  e);
			return new Result(-1, "Erro ao remover gatilhos");
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
			pstmt = connect.prepareStatement("SELECT * FROM ptc_workflow_gatilho");
				return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! WorkflowGatilhoServices.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! WorkflowGatilhoServices.getAll: " + e);
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
		return Search.find("SELECT * FROM ptc_workflow_gatilho", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}
	
}
