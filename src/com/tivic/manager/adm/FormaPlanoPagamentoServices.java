package com.tivic.manager.adm;

import java.sql.*;
import java.sql.PreparedStatement;
import java.util.ArrayList;

import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.util.Result;

import com.tivic.sol.connection.Conexao;

public class FormaPlanoPagamentoServices {

	public static Result save(FormaPlanoPagamento formaPlanoPagamento){
		return save(formaPlanoPagamento, null);
	}

	public static Result save(FormaPlanoPagamento formaPlanoPagamento, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			if(formaPlanoPagamento==null)
				return new Result(-1, "Erro ao salvar. FormaPlanoPagamento é nulo");

			int retorno;
			
			FormaPlanoPagamento f = FormaPlanoPagamentoDAO.get(formaPlanoPagamento.getCdFormaPagamento(), formaPlanoPagamento.getCdEmpresa(), formaPlanoPagamento.getCdPlanoPagamento(), connect);
			
			if(f==null){
				retorno = FormaPlanoPagamentoDAO.insert(formaPlanoPagamento, connect);
				formaPlanoPagamento.setCdFormaPagamento(retorno);
			}
			else {
				retorno = FormaPlanoPagamentoDAO.update(formaPlanoPagamento, connect);
			}

			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();

			return new Result(retorno, (retorno<=0)?"Erro ao salvar...":"Salvo com sucesso...", "FORMAPLANOPAGAMENTO", formaPlanoPagamento);
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
	public static Result remove(int cdFormaPagamento, int cdEmpresa, int cdPlanoPagamento){
		return remove(cdFormaPagamento, cdEmpresa, cdPlanoPagamento, false, null);
	}
	public static Result remove(int cdFormaPagamento, int cdEmpresa, int cdPlanoPagamento, boolean cascade){
		return remove(cdFormaPagamento, cdEmpresa, cdPlanoPagamento, cascade, null);
	}
	public static Result remove(int cdFormaPagamento, int cdEmpresa, int cdPlanoPagamento, boolean cascade, Connection connect){
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
			retorno = FormaPlanoPagamentoDAO.delete(cdFormaPagamento, cdEmpresa, cdPlanoPagamento, connect);
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
			pstmt = connect.prepareStatement("SELECT * FROM adm_forma_plano_pagamento");
				return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! FormaPlanoPagamentoServices.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! FormaPlanoPagamentoServices.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ResultSetMap getAllByEmpresa(int cdFormaPagamento, int cdEmpresa) {
		return getAllByEmpresa(cdFormaPagamento, cdEmpresa, null);
	}

	public static ResultSetMap getAllByEmpresa(int cdFormaPagamento, int cdEmpresa, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM adm_forma_plano_pagamento A " +
					 							"LEFT OUTER JOIN adm_plano_pagamento B ON (B.cd_plano_pagamento = A.cd_plano_pagamento) " + 
												"WHERE A.cd_forma_pagamento = ? AND A.cd_empresa = ?");
			
				pstmt.setInt(1, cdFormaPagamento);
				pstmt.setInt(2, cdEmpresa);
				return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! FormaPlanoPagamentoServices.getAllByEmpresa: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! FormaPlanoPagamentoServices.getAllByEmpresa: " + e);
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
		return Search.find("SELECT * FROM adm_forma_plano_pagamento", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
