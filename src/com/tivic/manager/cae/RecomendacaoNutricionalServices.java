package com.tivic.manager.cae;

import java.sql.*;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.HashMap;

import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.util.Result;

import com.tivic.sol.connection.Conexao;

public class RecomendacaoNutricionalServices {

	public static Result save(RecomendacaoNutricional recomendacaoNutricional){
		return save(recomendacaoNutricional, null);
	}

	public static Result save(RecomendacaoNutricional recomendacaoNutricional, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			if(recomendacaoNutricional==null)
				return new Result(-1, "Erro ao salvar. RecomendacaoNutricional é nulo");

			int retorno;
			if(recomendacaoNutricional.getCdRecomendacaoNutricional()==0){
				retorno = RecomendacaoNutricionalDAO.insert(recomendacaoNutricional, connect);
				recomendacaoNutricional.setCdRecomendacaoNutricional(retorno);
			}
			else {
				retorno = RecomendacaoNutricionalDAO.update(recomendacaoNutricional, connect);
			}

			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();

			return new Result(retorno, (retorno<=0)?"Erro ao salvar...":"Salvo com sucesso...", "RECOMENDACAONUTRICIONAL", recomendacaoNutricional);
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

	public static Result remove(int cdRecomendacaoNutricional){
		return remove(cdRecomendacaoNutricional, false, null);
	}
	public static Result remove(int cdRecomendacaoNutricional, boolean cascade){
		return remove(cdRecomendacaoNutricional, cascade, null);
	}
	public static Result remove(int cdRecomendacaoNutricional, boolean cascade, Connection connect){
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
			retorno = RecomendacaoNutricionalDAO.delete(cdRecomendacaoNutricional, connect);
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
			pstmt = connect.prepareStatement("SELECT * FROM cae_recomendacao_nutricional");
			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
			
			while(rsm.next()) {
				rsm.setValueToField("NM_FAIXA_ETARIA", ("De " + rsm.getInt("IDADE_INICIO") + " a " + rsm.getInt("IDADE_FIM") + " anos"));
			}
			
			rsm.beforeFirst();
			return rsm;
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! RecomendacaoNutricionalServices.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! RecomendacaoNutricionalServices.getAll: " + e);
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
		return Search.find("SELECT * FROM cae_recomendacao_nutricional", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
