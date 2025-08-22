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

public class TipoGruposEscolaresServices {

	public static Result save(TipoGruposEscolares tipoGruposEscolares){
		return save(tipoGruposEscolares, null, null);
	}

	public static Result save(TipoGruposEscolares tipoGruposEscolares, AuthData authData){
		return save(tipoGruposEscolares, authData, null);
	}

	public static Result save(TipoGruposEscolares tipoGruposEscolares, AuthData authData, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			if(tipoGruposEscolares==null)
				return new Result(-1, "Erro ao salvar. TipoGruposEscolares é nulo");

			int retorno;
			if(tipoGruposEscolares.getCdTipoGruposEscolares()==0){
				retorno = TipoGruposEscolaresDAO.insert(tipoGruposEscolares, connect);
				tipoGruposEscolares.setCdTipoGruposEscolares(retorno);
			}
			else {
				retorno = TipoGruposEscolaresDAO.update(tipoGruposEscolares, connect);
			}

			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();

			return new Result(retorno, (retorno<=0)?"Erro ao salvar...":"Salvo com sucesso...", "TIPOGRUPOSESCOLARES", tipoGruposEscolares);
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
	public static Result remove(TipoGruposEscolares tipoGruposEscolares) {
		return remove(tipoGruposEscolares.getCdTipoGruposEscolares());
	}
	public static Result remove(int cdTipoGruposEscolares){
		return remove(cdTipoGruposEscolares, false, null, null);
	}
	public static Result remove(int cdTipoGruposEscolares, boolean cascade){
		return remove(cdTipoGruposEscolares, cascade, null, null);
	}
	public static Result remove(int cdTipoGruposEscolares, boolean cascade, AuthData authData){
		return remove(cdTipoGruposEscolares, cascade, authData, null);
	}
	public static Result remove(int cdTipoGruposEscolares, boolean cascade, AuthData authData, Connection connect){
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
			retorno = TipoGruposEscolaresDAO.delete(cdTipoGruposEscolares, connect);
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
			pstmt = connect.prepareStatement("SELECT * FROM acd_tipo_grupos_escolares");
				return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TipoGruposEscolaresServices.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! TipoGruposEscolaresServices.getAll: " + e);
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
		return Search.find("SELECT * FROM acd_tipo_grupos_escolares", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}