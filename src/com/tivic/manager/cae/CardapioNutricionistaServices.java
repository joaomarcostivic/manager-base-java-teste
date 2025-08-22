package com.tivic.manager.cae;

import java.sql.*;
import java.sql.PreparedStatement;
import java.util.ArrayList;

import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.util.Result;

import com.tivic.sol.connection.Conexao;
import com.tivic.manager.grl.ParametroServices;

public class CardapioNutricionistaServices {

	public static Result save(CardapioNutricionista cardapioNutricionista){
		return save(cardapioNutricionista, null);
	}

	public static Result save(CardapioNutricionista cardapioNutricionista, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			if(cardapioNutricionista==null)
				return new Result(-1, "Erro ao salvar. CardapioNutricionista é nulo");

			int retorno;
			if(cardapioNutricionista.getCdCardapio()==0){
				retorno = CardapioNutricionistaDAO.insert(cardapioNutricionista, connect);
				cardapioNutricionista.setCdCardapio(retorno);
			}
			else {
				retorno = CardapioNutricionistaDAO.update(cardapioNutricionista, connect);
			}

			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();

			return new Result(retorno, (retorno<=0)?"Erro ao salvar...":"Salvo com sucesso...", "CARDAPIONUTRICIONISTA", cardapioNutricionista);
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

	public static Result remove(int cdCardapio, int cdNutricionista){
		return remove(cdCardapio, cdNutricionista, false, null);
	}
	public static Result remove(int cdCardapio, int cdNutricionista, boolean cascade){
		return remove(cdCardapio, cdNutricionista, cascade, null);
	}
	public static Result remove(int cdCardapio, int cdNutricionista, boolean cascade, Connection connect){
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
			retorno = CardapioNutricionistaDAO.delete(cdCardapio, cdNutricionista, connect);
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
			pstmt = connect.prepareStatement("SELECT * FROM cae_cardapio_nutricionista");
				return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! CardapioNutricionistaServices.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! CardapioNutricionistaServices.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static ResultSetMap getAllByCardapio(int cdCardapio) {
		return getAllByCardapio(cdCardapio, null);
	}

	public static ResultSetMap getAllByCardapio(int cdCardapio, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement("SELECT B.cd_pessoa, B.nm_pessoa " + 
											" FROM cae_cardapio_nutricionista A" +
											" LEFT OUTER JOIN grl_pessoa B ON (B.cd_pessoa = A.cd_nutricionista)" +
											" WHERE A.cd_cardapio = " + cdCardapio +
											" ORDER BY B.nm_pessoa");
				return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! CardapioNutricionistaServices.getAllByCardapio: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! CardapioNutricionistaServices.getAllByCardapio: " + e);
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
		return Search.find("SELECT * FROM cae_cardapio_nutricionista", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
