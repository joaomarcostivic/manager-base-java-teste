package com.tivic.manager.cae;

import java.sql.*;
import java.sql.PreparedStatement;
import java.util.ArrayList;

import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.util.Result;

import com.tivic.manager.acd.CursoUnidadeConceito;
import com.tivic.manager.acd.CursoUnidadeConceitoDAO;
import com.tivic.sol.connection.Conexao;
import com.tivic.manager.seg.AuthData;

public class IngredienteRecomendacaoServices {

	public static Result save(IngredienteRecomendacao ingredienteRecomendacao){
		return save(ingredienteRecomendacao, null, null);
	}

	public static Result save(IngredienteRecomendacao ingredienteRecomendacao, AuthData authData){
		return save(ingredienteRecomendacao, authData, null);
	}

	public static Result save(IngredienteRecomendacao ingredienteRecomendacao, AuthData authData, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			if(ingredienteRecomendacao==null)
				return new Result(-1, "Erro ao salvar. IngredienteRecomendacao é nulo");

			int retorno;
			
			IngredienteRecomendacao ir = IngredienteRecomendacaoDAO.get(ingredienteRecomendacao.getCdIngrediente(), ingredienteRecomendacao.getCdRecomendacaoNutricional());
			
			if(ir==null){
				retorno = IngredienteRecomendacaoDAO.insert(ingredienteRecomendacao, connect);
				ingredienteRecomendacao.setCdIngrediente(retorno);
			}
			else {
				retorno = IngredienteRecomendacaoDAO.update(ingredienteRecomendacao, connect);
			}

			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();

			return new Result(retorno, (retorno<=0)?"Erro ao salvar...":"Salvo com sucesso...", "INGREDIENTERECOMENDACAO", ingredienteRecomendacao);
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
	public static Result remove(int cdIngrediente, int cdRecomendacaoNutricional){
		return remove(cdIngrediente, cdRecomendacaoNutricional, false, null, null);
	}
	public static Result remove(int cdIngrediente, int cdRecomendacaoNutricional, boolean cascade){
		return remove(cdIngrediente, cdRecomendacaoNutricional, cascade, null, null);
	}
	public static Result remove(int cdIngrediente, int cdRecomendacaoNutricional, boolean cascade, AuthData authData){
		return remove(cdIngrediente, cdRecomendacaoNutricional, cascade, authData, null);
	}
	public static Result remove(int cdIngrediente, int cdRecomendacaoNutricional, boolean cascade, AuthData authData, Connection connect){
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
			retorno = IngredienteRecomendacaoDAO.delete(cdIngrediente, cdRecomendacaoNutricional, connect);
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
			pstmt = connect.prepareStatement("SELECT * FROM cae_ingrediente_recomendacao");
				return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! IngredienteRecomendacaoServices.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! IngredienteRecomendacaoServices.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static ResultSetMap getByIngrediente(int cdIngrediente) {
		return getByIngrediente(cdIngrediente, null);
	}
	
	public static ResultSetMap getByIngrediente(int cdIngrediente, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement("SELECT A.*, B.* FROM cae_ingrediente_recomendacao A" +
											" JOIN cae_recomendacao_nutricional B ON (A.cd_recomendacao_nutricional = B.cd_recomendacao_nutricional)" +
											" WHERE A.cd_ingrediente = " + cdIngrediente);
				return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! IngredienteRecomendacaoServices.getByIngrediente: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! IngredienteRecomendacaoServices.getByIngrediente: " + e);
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
		return Search.find("SELECT * FROM cae_ingrediente_recomendacao", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}