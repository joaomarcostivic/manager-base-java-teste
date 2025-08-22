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

public class LinhaPassageiroServices {

	public static Result save(LinhaPassageiro linhaPassageiro){
		return save(linhaPassageiro, null, null);
	}

	public static Result save(LinhaPassageiro linhaPassageiro, AuthData authData){
		return save(linhaPassageiro, authData, null);
	}

	public static Result save(LinhaPassageiro linhaPassageiro, AuthData authData, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			if(linhaPassageiro==null)
				return new Result(-1, "Erro ao salvar. LinhaPassageiro é nulo");

			int retorno;
			if(linhaPassageiro.getCdLinhaPassageiro()==0){
				retorno = LinhaPassageiroDAO.insert(linhaPassageiro, connect);
				linhaPassageiro.setCdLinhaPassageiro(retorno);
			}
			else {
				retorno = LinhaPassageiroDAO.update(linhaPassageiro, connect);
			}

			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();

			return new Result(retorno, (retorno<=0)?"Erro ao salvar...":"Salvo com sucesso...", "LINHAPASSAGEIRO", linhaPassageiro);
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
	public static Result remove(LinhaPassageiro linhaPassageiro) {
		return remove(linhaPassageiro.getCdLinhaPassageiro());
	}
	public static Result remove(int cdLinhaPassageiro){
		return remove(cdLinhaPassageiro, false, null, null);
	}
	public static Result remove(int cdLinhaPassageiro, boolean cascade){
		return remove(cdLinhaPassageiro, cascade, null, null);
	}
	public static Result remove(int cdLinhaPassageiro, boolean cascade, AuthData authData){
		return remove(cdLinhaPassageiro, cascade, authData, null);
	}
	public static Result remove(int cdLinhaPassageiro, boolean cascade, AuthData authData, Connection connect){
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
			retorno = LinhaPassageiroDAO.delete(cdLinhaPassageiro, connect);
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
			pstmt = connect.prepareStatement("SELECT * FROM mob_linha_passageiro");
				return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! LinhaPassageiroServices.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! LinhaPassageiroServices.getAll: " + e);
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
		return Search.find("SELECT * FROM mob_linha_passageiro", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}