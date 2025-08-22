package com.tivic.manager.prc;

import java.sql.*;
import java.sql.PreparedStatement;
import java.util.ArrayList;

import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.util.Result;

import com.tivic.sol.connection.Conexao;
import com.tivic.manager.seg.AuthData;

public class CalculoServices {

	public static Result save(Calculo calculo){
		return save(calculo, null, null);
	}

	public static Result save(Calculo calculo, AuthData authData){
		return save(calculo, authData, null);
	}

	public static Result save(Calculo calculo, AuthData authData, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			if(calculo==null)
				return new Result(-1, "Erro ao salvar. Calculo é nulo");

			int retorno;
			Calculo c = CalculoDAO.get(calculo.getCdProcesso(), calculo.getNrParcela(), connect);
			if(c==null){
				retorno = CalculoDAO.insert(calculo, connect);
				calculo.setCdProcesso(retorno);
			}
			else {
				retorno = CalculoDAO.update(calculo, connect);
			}

			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();

			return new Result(retorno, (retorno<=0)?"Erro ao salvar...":"Salvo com sucesso...", "CALCULO", calculo);
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
	public static Result remove(int cdProcesso, int nrParcela){
		return remove(cdProcesso, nrParcela, false, null, null);
	}
	public static Result remove(int cdProcesso, int nrParcela, boolean cascade){
		return remove(cdProcesso, nrParcela, cascade, null, null);
	}
	public static Result remove(int cdProcesso, int nrParcela, boolean cascade, AuthData authData){
		return remove(cdProcesso, nrParcela, cascade, authData, null);
	}
	public static Result remove(int cdProcesso, int nrParcela, boolean cascade, AuthData authData, Connection connect){
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
			retorno = CalculoDAO.delete(cdProcesso, nrParcela, connect);
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
			pstmt = connect.prepareStatement("SELECT * FROM prc_calculo");
				return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! CalculoServices.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! CalculoServices.getAll: " + e);
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
		return Search.find("SELECT A.* FROM prc_calculo A", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}