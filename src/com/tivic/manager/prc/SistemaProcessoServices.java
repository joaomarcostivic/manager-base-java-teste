package com.tivic.manager.prc;

import java.sql.*;
import java.sql.PreparedStatement;
import java.util.ArrayList;

import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.util.Result;

import com.tivic.sol.connection.Conexao;
import com.tivic.manager.grl.ParametroServices;
import com.tivic.manager.seg.AuthData;

public class SistemaProcessoServices {

	public static Result save(SistemaProcesso sistemaProcesso){
		return save(sistemaProcesso, null, null);
	}

	public static Result save(SistemaProcesso sistemaProcesso, AuthData authData){
		return save(sistemaProcesso, authData, null);
	}

	public static Result save(SistemaProcesso sistemaProcesso, AuthData authData, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			if(sistemaProcesso==null)
				return new Result(-1, "Erro ao salvar. SistemaProcesso é nulo");

			int retorno;
			if(sistemaProcesso.getCdSistemaProcesso()==0){
				retorno = SistemaProcessoDAO.insert(sistemaProcesso, connect);
				sistemaProcesso.setCdSistemaProcesso(retorno);
			}
			else {
				retorno = SistemaProcessoDAO.update(sistemaProcesso, connect);
			}

			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();

			return new Result(retorno, (retorno<=0)?"Erro ao salvar...":"Salvo com sucesso...", "SISTEMAPROCESSO", sistemaProcesso);
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
	public static Result remove(SistemaProcesso sistemaProcesso) {
		return remove(sistemaProcesso.getCdSistemaProcesso());
	}
	public static Result remove(int cdSistemaProcesso){
		return remove(cdSistemaProcesso, false, null, null);
	}
	public static Result remove(int cdSistemaProcesso, boolean cascade){
		return remove(cdSistemaProcesso, cascade, null, null);
	}
	public static Result remove(int cdSistemaProcesso, boolean cascade, AuthData authData){
		return remove(cdSistemaProcesso, cascade, authData, null);
	}
	public static Result remove(int cdSistemaProcesso, boolean cascade, AuthData authData, Connection connect){
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
			retorno = SistemaProcessoDAO.delete(cdSistemaProcesso, connect);
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
			pstmt = connect.prepareStatement("SELECT * FROM prc_sistema_processo");
				return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! SistemaProcessoServices.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! SistemaProcessoServices.getAll: " + e);
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
		return Search.find("SELECT * FROM prc_sistema_processo", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
