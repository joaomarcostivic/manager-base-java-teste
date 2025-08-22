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

public class TipoRecursoAcessibilidadeServices {

	public static Result save(TipoRecursoAcessibilidade tipoRecursoAcessibilidade){
		return save(tipoRecursoAcessibilidade, null, null);
	}

	public static Result save(TipoRecursoAcessibilidade tipoRecursoAcessibilidade, AuthData authData){
		return save(tipoRecursoAcessibilidade, authData, null);
	}

	public static Result save(TipoRecursoAcessibilidade tipoRecursoAcessibilidade, AuthData authData, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			if(tipoRecursoAcessibilidade==null)
				return new Result(-1, "Erro ao salvar. TipoRecursoAcessibilidade é nulo");

			int retorno;
			if(tipoRecursoAcessibilidade.getCdTipoRecursoAcessibilidade()==0){
				retorno = TipoRecursoAcessibilidadeDAO.insert(tipoRecursoAcessibilidade, connect);
				tipoRecursoAcessibilidade.setCdTipoRecursoAcessibilidade(retorno);
			}
			else {
				retorno = TipoRecursoAcessibilidadeDAO.update(tipoRecursoAcessibilidade, connect);
			}

			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();

			return new Result(retorno, (retorno<=0)?"Erro ao salvar...":"Salvo com sucesso...", "TIPORECURSOACESSIBILIDADE", tipoRecursoAcessibilidade);
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
	public static Result remove(TipoRecursoAcessibilidade tipoRecursoAcessibilidade) {
		return remove(tipoRecursoAcessibilidade.getCdTipoRecursoAcessibilidade());
	}
	public static Result remove(int cdTipoRecursoAcessibilidade){
		return remove(cdTipoRecursoAcessibilidade, false, null, null);
	}
	public static Result remove(int cdTipoRecursoAcessibilidade, boolean cascade){
		return remove(cdTipoRecursoAcessibilidade, cascade, null, null);
	}
	public static Result remove(int cdTipoRecursoAcessibilidade, boolean cascade, AuthData authData){
		return remove(cdTipoRecursoAcessibilidade, cascade, authData, null);
	}
	public static Result remove(int cdTipoRecursoAcessibilidade, boolean cascade, AuthData authData, Connection connect){
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
			retorno = TipoRecursoAcessibilidadeDAO.delete(cdTipoRecursoAcessibilidade, connect);
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
			pstmt = connect.prepareStatement("SELECT * FROM acd_tipo_recurso_acessibilidade");
				return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TipoRecursoAcessibilidadeServices.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! TipoRecursoAcessibilidadeServices.getAll: " + e);
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
		return Search.find("SELECT * FROM acd_tipo_recurso_acessibilidade", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}