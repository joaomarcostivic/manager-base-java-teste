package com.tivic.manager.bdv;

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

public class RestricaoServices {

	public static Result save(Restricao restricao){
		return save(restricao, null, null);
	}

	public static Result save(Restricao restricao, AuthData authData){
		return save(restricao, authData, null);
	}

	public static Result save(Restricao restricao, AuthData authData, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			if(restricao==null)
				return new Result(-1, "Erro ao salvar. Restricao é nulo");

			int retorno;
			if(restricao.getCdRestricao()==0){
				retorno = RestricaoDAO.insert(restricao, connect);
				restricao.setCdRestricao(retorno);
			}
			else {
				retorno = RestricaoDAO.update(restricao, connect);
			}

			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();

			return new Result(retorno, (retorno<=0)?"Erro ao salvar...":"Salvo com sucesso...", "RESTRICAO", restricao);
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
	public static Result remove(Restricao restricao) {
		return remove(restricao.getCdRestricao(), restricao.getCdVeiculo());
	}
	public static Result remove(int cdRestricao, int cdVeiculo){
		return remove(cdRestricao, cdVeiculo, false, null, null);
	}
	public static Result remove(int cdRestricao, int cdVeiculo, boolean cascade){
		return remove(cdRestricao, cdVeiculo, cascade, null, null);
	}
	public static Result remove(int cdRestricao, int cdVeiculo, boolean cascade, AuthData authData){
		return remove(cdRestricao, cdVeiculo, cascade, authData, null);
	}
	public static Result remove(int cdRestricao, int cdVeiculo, boolean cascade, AuthData authData, Connection connect){
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
			retorno = RestricaoDAO.delete(cdRestricao, cdVeiculo, connect);
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
			pstmt = connect.prepareStatement("SELECT * FROM bdv_restricao");
				return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! RestricaoServices.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! RestricaoServices.getAll: " + e);
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
		return Search.find("SELECT * FROM bdv_restricao", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
