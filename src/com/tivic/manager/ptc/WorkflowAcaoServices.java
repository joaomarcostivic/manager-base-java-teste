package com.tivic.manager.ptc;

import java.sql.*;
import java.util.ArrayList;

import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.util.Result;

import com.tivic.sol.connection.Conexao;

public class WorkflowAcaoServices {
	
	public static final int TP_ACAO_TRAMITAR = 0;
	public static final int TP_ACAO_LANCAR_AGENDA = 1;
	public static final int TP_ACAO_MUDAR_FASE = 2;
	public static final int TP_ACAO_LANCAR_OCORRENCIA = 3;
	public static final int TP_ACAO_LANCAR_PENDENCIA = 4;
	public static final int TP_ACAO_ANEXAR_DOCUMENTO = 5;
	public static final int TP_ACAO_GERAR_DOCUMENTO = 6;

	public static Result save(WorkflowAcao workflowAcao){
		return save(workflowAcao, null);
	}

	public static Result save(WorkflowAcao workflowAcao, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			if(workflowAcao==null)
				return new Result(-1, "Erro ao salvar. WorkflowAcao é nulo");

			int retorno;
			if(workflowAcao.getCdAcao()==0){
				retorno = WorkflowAcaoDAO.insert(workflowAcao, connect);
				workflowAcao.setCdAcao(retorno);
			}
			else {
				retorno = WorkflowAcaoDAO.update(workflowAcao, connect);
			}

			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();

			return new Result(retorno, (retorno<=0)?"Erro ao salvar...":"Salvo com sucesso...", "WORKFLOWACAO", workflowAcao);
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
	public static Result remove(int cdAcao, int cdRegra){
		return remove(cdAcao, cdRegra, false, null);
	}
	public static Result remove(int cdAcao, int cdRegra, boolean cascade){
		return remove(cdAcao, cdRegra, cascade, null);
	}
	public static Result remove(int cdAcao, int cdRegra, boolean cascade, Connection connect){
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
			retorno = WorkflowAcaoDAO.delete(cdAcao, cdRegra, connect);
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
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM ptc_workflow_acao WHERE cd_regra=?");
			pstmt.setInt(1, cdRegra);
			pstmt.executeUpdate();
			return new Result(1, "Ações removidas com sucesso");
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! WorkflowAcaoServices.removeAllByRegra: " +  e);
			return new Result(-1, "Erro ao remover ações");
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
			pstmt = connect.prepareStatement("SELECT * FROM ptc_workflow_acao");
				return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! WorkflowAcaoServices.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! WorkflowAcaoServices.getAll: " + e);
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
		return Search.find("SELECT * FROM ptc_workflow_acao", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
