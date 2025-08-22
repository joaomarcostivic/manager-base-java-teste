package com.tivic.manager.log;

import java.sql.*;
import java.sql.PreparedStatement;
import java.util.ArrayList;

import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.util.Result;

import com.tivic.sol.connection.Conexao;

public class SistemaServices {

	public static final int TP_LOG_GERAL=0;
	public static final int TP_LOG_ACESSO=1;
	public static final int TP_LOG_ACAO=2;
	
	public static final int TP_LOGIN=3;
	public static final int TP_LOGOUT=4;
	
	public static Result save(Sistema sistema){
		return save(sistema, null);
	}

	public static Result save(Sistema sistema, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			if(sistema==null)
				return new Result(-1, "Erro ao salvar. Sistema é nulo");

			int retorno;
			if(sistema.getCdLog()==0){
				retorno = SistemaDAO.insert(sistema, connect);
				sistema.setCdLog(retorno);
			}
			else {
				retorno = SistemaDAO.update(sistema, connect);
			}

			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();

			return new Result(retorno, (retorno<=0)?"Erro ao salvar...":"Salvo com sucesso...", "SISTEMA", sistema);
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
	public static Result remove(int cdLog){
		return remove(cdLog, false, null);
	}
	public static Result remove(int cdLog, boolean cascade){
		return remove(cdLog, cascade, null);
	}
	public static Result remove(int cdLog, boolean cascade, Connection connect){
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
			retorno = SistemaDAO.delete(cdLog, connect);
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
			pstmt = connect.prepareStatement("SELECT * FROM log_sistema");
				return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! SistemaServices.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! SistemaServices.getAll: " + e);
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
		int limit = 50;
		for(int i=0; i<criterios.size(); i++)	{
			if(criterios.get(i).getColumn().equalsIgnoreCase("limit"))	{
				limit = Integer.valueOf(criterios.get(i).getValue().toString().trim());
				criterios.remove(i);
				i--;
			}
		}
		
		return Search.find("SELECT * FROM log_sistema", " ORDER BY dt_log DESC LIMIT "+limit, criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}

