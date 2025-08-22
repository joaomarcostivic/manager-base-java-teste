package com.tivic.manager.ctb;

import java.sql.*;
import java.sql.PreparedStatement;
import java.util.ArrayList;

import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.util.Result;

import com.tivic.sol.connection.Conexao;

public class PlanoCentroCustoServices {

	public static Result save(PlanoCentroCusto planoCentroCusto){
		return save(planoCentroCusto, null);
	}

	public static Result save(PlanoCentroCusto planoCentroCusto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			if(planoCentroCusto==null)
				return new Result(-1, "Erro ao salvar. PlanoCentroCusto é nulo");

			int retorno;
			if(planoCentroCusto.getCdPlanoCentroCusto()==0){
				retorno = PlanoCentroCustoDAO.insert(planoCentroCusto, connect);
				planoCentroCusto.setCdPlanoCentroCusto(retorno);
			}
			else {
				retorno = PlanoCentroCustoDAO.update(planoCentroCusto, connect);
			}

			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();

			return new Result(retorno, (retorno<=0)?"Erro ao salvar...":"Salvo com sucesso...", "PLANOCENTROCUSTO", planoCentroCusto);
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
	public static Result remove(int cdPlanoCentroCusto){
		return remove(cdPlanoCentroCusto, false, null);
	}
	public static Result remove(int cdPlanoCentroCusto, boolean cascade){
		return remove(cdPlanoCentroCusto, cascade, null);
	}
	public static Result remove(int cdPlanoCentroCusto, boolean cascade, Connection connect){
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
			retorno = PlanoCentroCustoDAO.delete(cdPlanoCentroCusto, connect);
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
			pstmt = connect.prepareStatement("SELECT * FROM ctb_plano_centro_custo A "+
											 " LEFT JOIN ctb_exercicio B on ( A.cd_exercicio = B.cd_exercicio ) ");
				return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! PlanoCentroCustoServices.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! PlanoCentroCustoServices.getAll: " + e);
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
		return Search.find("SELECT * FROM ctb_plano_centro_custo", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
