package com.tivic.manager.acd;

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

public class TipoRecursoServices {

	public static Result save(TipoRecurso tipoRecurso){
		return save(tipoRecurso, null, null);
	}

	public static Result save(TipoRecurso tipoRecurso, AuthData authData){
		return save(tipoRecurso, authData, null);
	}

	public static Result save(TipoRecurso tipoRecurso, AuthData authData, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			if(tipoRecurso==null)
				return new Result(-1, "Erro ao salvar. TipoRecurso é nulo");

			int retorno;
			if(tipoRecurso.getCdTipoRecurso()==0){
				retorno = TipoRecursoDAO.insert(tipoRecurso, connect);
				tipoRecurso.setCdTipoRecurso(retorno);
			}
			else {
				retorno = TipoRecursoDAO.update(tipoRecurso, connect);
			}

			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();

			return new Result(retorno, (retorno<=0)?"Erro ao salvar...":"Salvo com sucesso...", "TIPORECURSO", tipoRecurso);
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
	public static Result remove(TipoRecurso tipoRecurso) {
		return remove(tipoRecurso.getCdTipoRecurso());
	}
	public static Result remove(int cdTipoRecurso){
		return remove(cdTipoRecurso, false, null, null);
	}
	public static Result remove(int cdTipoRecurso, boolean cascade){
		return remove(cdTipoRecurso, cascade, null, null);
	}
	public static Result remove(int cdTipoRecurso, boolean cascade, AuthData authData){
		return remove(cdTipoRecurso, cascade, authData, null);
	}
	public static Result remove(int cdTipoRecurso, boolean cascade, AuthData authData, Connection connect){
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
			retorno = TipoRecursoDAO.delete(cdTipoRecurso, connect);
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
			pstmt = connect.prepareStatement("SELECT * FROM acd_tipo_recurso");
				return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TipoRecursoServices.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! TipoRecursoServices.getAll: " + e);
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
		return Search.find("SELECT * FROM acd_tipo_recurso", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}