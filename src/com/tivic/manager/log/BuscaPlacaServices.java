package com.tivic.manager.log;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;

import com.tivic.sol.connection.Conexao;
import com.tivic.manager.seg.AuthData;

import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;
import sol.dao.Search;
import sol.util.Result;

public class BuscaPlacaServices {
	
	public static final int TP_DETRAN = 1;
	public static final int TP_SINESP = 2;
	public static final int TP_TIVIC = 3;

	public static final int TP_LOG_SUCESSO = 1;
	public static final int TP_LOG_ERRO = 2;

	public static Result save(BuscaPlaca buscaPlaca){
		return save(buscaPlaca, null, null);
	}

	public static Result save(BuscaPlaca buscaPlaca, AuthData authData){
		return save(buscaPlaca, authData, null);
	}

	public static Result save(BuscaPlaca buscaPlaca, AuthData authData, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			if(buscaPlaca==null)
				return new Result(-1, "Erro ao salvar. BuscaPlaca é nulo");

			int retorno;
			if(buscaPlaca.getCdLog()==0){
				retorno = BuscaPlacaDAO.insert(buscaPlaca, connect);
				buscaPlaca.setCdLog(retorno);
			}
			else {
				retorno = BuscaPlacaDAO.update(buscaPlaca, connect);
			}

			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();

			return new Result(retorno, (retorno<=0)?"Erro ao salvar...":"Salvo com sucesso...", "BUSCAPLACA", buscaPlaca);
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
	public static Result remove(BuscaPlaca buscaPlaca) {
		return remove(buscaPlaca.getCdLog());
	}
	public static Result remove(int cdLog){
		return remove(cdLog, false, null, null);
	}
	public static Result remove(int cdLog, boolean cascade){
		return remove(cdLog, cascade, null, null);
	}
	public static Result remove(int cdLog, boolean cascade, AuthData authData){
		return remove(cdLog, cascade, authData, null);
	}
	public static Result remove(int cdLog, boolean cascade, AuthData authData, Connection connect){
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
			retorno = BuscaPlacaDAO.delete(cdLog, connect);
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
			pstmt = connect.prepareStatement("SELECT * FROM log_busca_placa");
				return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! BuscaPlacaServices.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! BuscaPlacaServices.getAll: " + e);
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
		return Search.find("SELECT * FROM log_busca_placa", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
