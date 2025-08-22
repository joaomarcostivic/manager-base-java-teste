package com.tivic.manager.prc;

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

public class OrigemAndamentoServices {

	public static Result save(OrigemAndamento origemAndamento){
		return save(origemAndamento, null, null);
	}

	public static Result save(OrigemAndamento origemAndamento, AuthData authData){
		return save(origemAndamento, authData, null);
	}

	public static Result save(OrigemAndamento origemAndamento, AuthData authData, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			if(origemAndamento==null)
				return new Result(-1, "Erro ao salvar. OrigemAndamento é nulo");

			int retorno;
			if(origemAndamento.getCdOrigemAndamento()==0){
				retorno = OrigemAndamentoDAO.insert(origemAndamento, connect);
				origemAndamento.setCdOrigemAndamento(retorno);
			}
			else {
				retorno = OrigemAndamentoDAO.update(origemAndamento, connect);
			}

			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();

			return new Result(retorno, (retorno<=0)?"Erro ao salvar...":"Salvo com sucesso...", "ORIGEMANDAMENTO", origemAndamento);
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
	public static Result remove(OrigemAndamento origemAndamento) {
		return remove(origemAndamento.getCdOrigemAndamento());
	}
	public static Result remove(int cdOrigemAndamento){
		return remove(cdOrigemAndamento, false, null, null);
	}
	public static Result remove(int cdOrigemAndamento, boolean cascade){
		return remove(cdOrigemAndamento, cascade, null, null);
	}
	public static Result remove(int cdOrigemAndamento, boolean cascade, AuthData authData){
		return remove(cdOrigemAndamento, cascade, authData, null);
	}
	public static Result remove(int cdOrigemAndamento, boolean cascade, AuthData authData, Connection connect){
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
			retorno = OrigemAndamentoDAO.delete(cdOrigemAndamento, connect);
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
			pstmt = connect.prepareStatement("SELECT * FROM prc_origem_andamento");
				return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! OrigemAndamentoServices.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! OrigemAndamentoServices.getAll: " + e);
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
		return Search.find("SELECT * FROM prc_origem_andamento", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
