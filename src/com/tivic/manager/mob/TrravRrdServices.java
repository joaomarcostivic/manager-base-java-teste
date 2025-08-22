package com.tivic.manager.mob;

import java.sql.*;
import java.sql.PreparedStatement;
import java.util.ArrayList;

import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.util.Result;

import com.tivic.sol.connection.Conexao;
import com.tivic.manager.seg.AuthData;

public class TrravRrdServices {

	public static Result save(TrravRrd trravRrd){
		return save(trravRrd, null, null);
	}

	public static Result save(TrravRrd trravRrd, AuthData authData){
		return save(trravRrd, authData, null);
	}

	public static Result save(TrravRrd trravRrd, AuthData authData, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			if(trravRrd==null)
				return new Result(-1, "Erro ao salvar. TrravRrd é nulo");

			int retorno;
			if(trravRrd.getCdRrd()==0){
				retorno = TrravRrdDAO.insert(trravRrd, connect);
				trravRrd.setCdRrd(retorno);
			}
			else {
				retorno = TrravRrdDAO.update(trravRrd, connect);
			}

			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();

			return new Result(retorno, (retorno<=0)?"Erro ao salvar...":"Salvo com sucesso...", "TRRAVRRD", trravRrd);
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
	public static Result remove(int cdRrd, int cdTrrav){
		return remove(cdRrd, cdTrrav, false, null, null);
	}
	public static Result remove(int cdRrd, int cdTrrav, boolean cascade){
		return remove(cdRrd, cdTrrav, cascade, null, null);
	}
	public static Result remove(int cdRrd, int cdTrrav, boolean cascade, AuthData authData){
		return remove(cdRrd, cdTrrav, cascade, authData, null);
	}
	public static Result remove(int cdRrd, int cdTrrav, boolean cascade, AuthData authData, Connection connect){
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
			retorno = TrravRrdDAO.delete(cdRrd, cdTrrav, connect);
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
			pstmt = connect.prepareStatement("SELECT * FROM mob_trrav_rrd");
				return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TrravRrdServices.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! TrravRrdServices.getAll: " + e);
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
		return Search.find("SELECT * FROM mob_trrav_rrd", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}