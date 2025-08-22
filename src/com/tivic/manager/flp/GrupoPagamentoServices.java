package com.tivic.manager.flp;

import java.sql.*;
import java.sql.PreparedStatement;
import java.util.ArrayList;

import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.util.Result;

import com.tivic.sol.connection.Conexao;

public class GrupoPagamentoServices {

	public static Result save(GrupoPagamento grupoPagamento){
		return save(grupoPagamento, null);
	}

	public static Result save(GrupoPagamento grupoPagamento, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			if(grupoPagamento==null)
				return new Result(-1, "Erro ao salvar. GrupoPagamento é nulo");

			int retorno;
			if(grupoPagamento.getCdGrupoPagamento()==0){
				retorno = GrupoPagamentoDAO.insert(grupoPagamento, connect);
				grupoPagamento.setCdGrupoPagamento(retorno);
			}
			else {
				retorno = GrupoPagamentoDAO.update(grupoPagamento, connect);
			}

			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();

			return new Result(retorno, (retorno<=0)?"Erro ao salvar...":"Salvo com sucesso...", "GRUPOPAGAMENTO", grupoPagamento);
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
	public static Result remove(int cdGrupoPagamento){
		return remove(cdGrupoPagamento, false, null);
	}
	public static Result remove(int cdGrupoPagamento, boolean cascade){
		return remove(cdGrupoPagamento, cascade, null);
	}
	public static Result remove(int cdGrupoPagamento, boolean cascade, Connection connect){
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
			retorno = GrupoPagamentoDAO.delete(cdGrupoPagamento, connect);
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
		return getAll(0, null);
	}
	
	public static ResultSetMap getAll(int cdEmpresa) {
		return getAll(cdEmpresa, null);
	}

	public static ResultSetMap getAll(int cdEmpresa, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM flp_grupo_pagamento"
					+ " WHERE 1=1 "
					+ (cdEmpresa>0 ? " AND cd_empresa="+cdEmpresa : ""));
				return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! GrupoPagamentoServices.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! GrupoPagamentoServices.getAll: " + e);
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
		return Search.find("SELECT * FROM flp_grupo_pagamento", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
