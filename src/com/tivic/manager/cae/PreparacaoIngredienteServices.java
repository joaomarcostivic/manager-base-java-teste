package com.tivic.manager.cae;

import java.sql.*;
import java.sql.PreparedStatement;
import java.util.ArrayList;

import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.util.Result;

import com.tivic.manager.acd.InstituicaoCirculoDAO;
import com.tivic.sol.connection.Conexao;

public class PreparacaoIngredienteServices {

	public static Result save(PreparacaoIngrediente preparacaoIngrediente){
		return save(preparacaoIngrediente, null);
	}

	public static Result save(PreparacaoIngrediente preparacaoIngrediente, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			if(preparacaoIngrediente==null)
				return new Result(-1, "Erro ao salvar. PreparacaoIngrediente Ã© nulo");

			int retorno;
			if(InstituicaoCirculoDAO.get(preparacaoIngrediente.getCdPreparacao(), preparacaoIngrediente.getCdIngrediente(), connect) == null){
				retorno = PreparacaoIngredienteDAO.insert(preparacaoIngrediente, connect);
			}
			else {
				retorno = PreparacaoIngredienteDAO.update(preparacaoIngrediente, connect);
			}

			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();

			return new Result(retorno, (retorno<=0)?"Erro ao salvar...":"Salvo com sucesso...", "PREPARACAOINGREDIENTE", preparacaoIngrediente);
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

	public static Result remove(int cdPreparacao, int cdIngrediente){
		return remove(cdPreparacao, cdIngrediente, false, null);
	}
	public static Result remove(int cdPreparacao, int cdIngrediente, boolean cascade){
		return remove(cdPreparacao, cdIngrediente, cascade, null);
	}
	public static Result remove(int cdPreparacao, int cdIngrediente, boolean cascade, Connection connect){
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
			retorno = PreparacaoIngredienteDAO.delete(cdPreparacao, cdIngrediente, connect);
			if(retorno<=0){
				Conexao.rollback(connect);
				return new Result(-2, "Este registro estÃ¡ vinculado a outros e nÃ£o pode ser excluÃ­do!");
			}
			else if (isConnectionNull)
				connect.commit();
			return new Result(1, "Registro excluÃ­do com sucesso!");
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
			pstmt = connect.prepareStatement("SELECT * FROM cae_preparacao_ingrediente");
				return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! PreparacaoIngredienteServices.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! PreparacaoIngredienteServices.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static ResultSetMap getAllByPreparacao(int cdPreparacao) {
		return getAllByPreparacao(cdPreparacao, null);
	}

	public static ResultSetMap getAllByPreparacao(int cdPreparacao, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement("SELECT A.*, B.nm_ingrediente" + 
											" FROM cae_preparacao_ingrediente A" +
											" JOIN cae_ingrediente B ON (A.cd_ingrediente = B.cd_ingrediente)" +
											" WHERE A.cd_preparacao = " + cdPreparacao);
				return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! PreparacaoIngredienteServices.getAllByPreparacao: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! PreparacaoIngredienteServices.getAllByPreparacao: " + e);
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
		return Search.find("SELECT * FROM cae_preparacao_ingrediente", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
