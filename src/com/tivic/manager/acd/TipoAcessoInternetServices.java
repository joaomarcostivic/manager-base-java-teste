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

public class TipoAcessoInternetServices {

	public static Result save(TipoAcessoInternet tipoAcessoInternet){
		return save(tipoAcessoInternet, null, null);
	}

	public static Result save(TipoAcessoInternet tipoAcessoInternet, AuthData authData){
		return save(tipoAcessoInternet, authData, null);
	}

	public static Result save(TipoAcessoInternet tipoAcessoInternet, AuthData authData, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			if(tipoAcessoInternet==null)
				return new Result(-1, "Erro ao salvar. TipoAcessoInternet é nulo");

			int retorno;
			if(tipoAcessoInternet.getCdTipoAcessoInternet()==0){
				retorno = TipoAcessoInternetDAO.insert(tipoAcessoInternet, connect);
				tipoAcessoInternet.setCdTipoAcessoInternet(retorno);
			}
			else {
				retorno = TipoAcessoInternetDAO.update(tipoAcessoInternet, connect);
			}

			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();

			return new Result(retorno, (retorno<=0)?"Erro ao salvar...":"Salvo com sucesso...", "TIPOACESSOINTERNET", tipoAcessoInternet);
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
	public static Result remove(TipoAcessoInternet tipoAcessoInternet) {
		return remove(tipoAcessoInternet.getCdTipoAcessoInternet());
	}
	public static Result remove(int cdTipoAcessoInternet){
		return remove(cdTipoAcessoInternet, false, null, null);
	}
	public static Result remove(int cdTipoAcessoInternet, boolean cascade){
		return remove(cdTipoAcessoInternet, cascade, null, null);
	}
	public static Result remove(int cdTipoAcessoInternet, boolean cascade, AuthData authData){
		return remove(cdTipoAcessoInternet, cascade, authData, null);
	}
	public static Result remove(int cdTipoAcessoInternet, boolean cascade, AuthData authData, Connection connect){
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
			retorno = TipoAcessoInternetDAO.delete(cdTipoAcessoInternet, connect);
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
			pstmt = connect.prepareStatement("SELECT * FROM acd_tipo_acesso_internet");
				return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TipoAcessoInternetServices.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! TipoAcessoInternetServices.getAll: " + e);
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
		return Search.find("SELECT * FROM acd_tipo_acesso_internet", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}