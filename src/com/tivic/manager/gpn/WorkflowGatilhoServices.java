package com.tivic.manager.gpn;

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

public class WorkflowGatilhoServices {
	
	public static final int TP_GATILHO_LANCAMENTO_PROCESSO = 0;
	public static final int TP_GATILHO_LANCAMENTO_ANDAMENTO = 1;
	public static final int TP_GATILHO_LANCAMENTO_AGENDA = 2;
	public static final int TP_GATILHO_CUMPRIMENTO_AGENDA = 3;
	
	public static final int TP_GATILHO_MUDANCA_CAMPO = 4;
	public static final int TP_GATILHO_LANCAMENTO_RECEITA = 5;
	public static final int TP_GATILHO_LANCAMENTO_DESPESA = 6;

	public static Result save(WorkflowGatilho workflowGatilho){
		return save(workflowGatilho, null, null);
	}

	public static Result save(WorkflowGatilho workflowGatilho, AuthData authData){
		return save(workflowGatilho, authData, null);
	}

	public static Result save(WorkflowGatilho workflowGatilho, AuthData authData, Connection connect){
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
		return remove(cdGatilho, cdRegra, false, null, null);
	}
	public static Result remove(int cdGatilho, int cdRegra, boolean cascade){
		return remove(cdGatilho, cdRegra, cascade, null, null);
	}
	public static Result remove(int cdGatilho, int cdRegra, boolean cascade, AuthData authData){
		return remove(cdGatilho, cdRegra, cascade, authData, null);
	}
	public static Result remove(int cdGatilho, int cdRegra, boolean cascade, AuthData authData, Connection connect){
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
	
	public static Result remove(int cdRegra) {
		return remove(cdRegra, null);
	}
	
	public static Result remove(int cdRegra, Connection connect) {
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			PreparedStatement ps = connect.prepareStatement("DELETE FROM gpn_workflow_gatilho WHERE cd_regra = ?");
			ps.setInt(1, cdRegra);
			
			int retorno = ps.executeUpdate();
			
			if(retorno<0) {
				if(isConnectionNull)
					connect.rollback();
				return new Result(-2, "Erro ao excluir gatilhos da regra.");
			}
			
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
			pstmt = connect.prepareStatement("SELECT * FROM gpn_workflow_gatilho");
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
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			String sql = 
					  " SELECT A.*,"
//					+ " B.*, C.*,"
					+ " D.nm_tipo_prazo,"
					+ " E.nm_produto_servico,"
					+ " F.nm_tipo_andamento,"
					+ " G.nm_pessoa"
					+ " FROM gpn_workflow_gatilho A"
//					+ " LEFT OUTER JOIN gpn_workflow_entidade B ON (A.cd_entidade = B.cd_entidade)"
//					+ " LEFT OUTER JOIN gpn_workflow_atributo C ON (A.cd_atributo = C.cd_atributo)"
					+ " LEFT OUTER JOIN prc_tipo_prazo 		  D ON (A.cd_tipo_prazo = D.cd_tipo_prazo)"
					+ " LEFT OUTER JOIN grl_produto_servico	  E ON (A.cd_produto_servico = E.cd_produto_servico)"
					+ " LEFT OUTER JOIN prc_tipo_andamento	  F ON (A.cd_tipo_andamento = F.cd_tipo_andamento)"
					+ " LEFT OUTER JOIN grl_pessoa			  G ON (A.cd_pessoa = G.cd_pessoa)";
			
			return Search.find(sql, criterios, connect, connect==null);
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! WorkflowGatilhoServices.find: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

}