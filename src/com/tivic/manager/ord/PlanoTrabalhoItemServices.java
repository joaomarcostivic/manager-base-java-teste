package com.tivic.manager.ord;

import java.sql.*;
import java.sql.PreparedStatement;
import java.util.ArrayList;

import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.util.Result;

import com.tivic.sol.connection.Conexao;
import com.tivic.manager.seg.AuthData;

public class PlanoTrabalhoItemServices {
	
	public static final int TP_MOVIMENTO_NAO_CONSIGNADO = 0;
	public static final int TP_MOVIMENTO_CONSIGNADO = 1;
	public static final int TP_MOVIMENTO_AMBOS = 2;

	public static Result save(PlanoTrabalhoItem planoTrabalhoItem){
		return save(planoTrabalhoItem, null, null);
	}

	public static Result save(PlanoTrabalhoItem planoTrabalhoItem, AuthData authData){
		return save(planoTrabalhoItem, authData, null);
	}

	public static Result save(PlanoTrabalhoItem planoTrabalhoItem, AuthData authData, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			if(planoTrabalhoItem==null)
				return new Result(-1, "Erro ao salvar. PlanoTrabalhoItem é nulo");

			int retorno;
			if(planoTrabalhoItem.getCdItem()==0){
				retorno = PlanoTrabalhoItemDAO.insert(planoTrabalhoItem, connect);
				planoTrabalhoItem.setCdItem(retorno);
			}
			else {
				retorno = PlanoTrabalhoItemDAO.update(planoTrabalhoItem, connect);
			}

			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();

			return new Result(retorno, (retorno<=0)?"Erro ao salvar...":"Salvo com sucesso...", "PLANOTRABALHOITEM", planoTrabalhoItem);
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
	public static Result remove(PlanoTrabalhoItem planoTrabalhoItem) {
		return remove(planoTrabalhoItem.getCdPlanoTrabalho(), planoTrabalhoItem.getCdItem());
	}
	public static Result remove(int cdPlanoTrabalho, int cdItem){
		return remove(cdPlanoTrabalho, cdItem, false, null, null);
	}
	public static Result remove(int cdPlanoTrabalho, int cdItem, boolean cascade){
		return remove(cdPlanoTrabalho, cdItem, cascade, null, null);
	}
	public static Result remove(int cdPlanoTrabalho, int cdItem, boolean cascade, AuthData authData){
		return remove(cdPlanoTrabalho, cdItem, cascade, authData, null);
	}
	public static Result remove(int cdPlanoTrabalho, int cdItem, boolean cascade, AuthData authData, Connection connect){
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
			retorno = PlanoTrabalhoItemDAO.delete(cdPlanoTrabalho, cdItem, connect);
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
			pstmt = connect.prepareStatement("SELECT * FROM ord_plano_trabalho_item");
				return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! PlanoTrabalhoItemServices.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! PlanoTrabalhoItemServices.getAll: " + e);
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
		return Search.find("SELECT * FROM ord_plano_trabalho_item", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}