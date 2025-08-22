package com.tivic.manager.mob;

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

public class TipoAcidenteServices {

	public static Result save(TipoAcidente tipoAcidente){
		return save(tipoAcidente, null, null);
	}

	public static Result save(TipoAcidente tipoAcidente, AuthData authData){
		return save(tipoAcidente, authData, null);
	}

	public static Result save(TipoAcidente tipoAcidente, AuthData authData, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			if(tipoAcidente==null)
				return new Result(-1, "Erro ao salvar. TipoAcidente é nulo");

			int retorno;
			if(tipoAcidente.getCdTipoAcidente()==0){
				retorno = TipoAcidenteDAO.insert(tipoAcidente, connect);
				tipoAcidente.setCdTipoAcidente(retorno);
			}
			else {
				retorno = TipoAcidenteDAO.update(tipoAcidente, connect);
			}

			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();

			return new Result(retorno, (retorno<=0)?"Erro ao salvar...":"Salvo com sucesso...", "TIPOACIDENTE", tipoAcidente);
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
	public static Result remove(TipoAcidente tipoAcidente) {
		return remove(tipoAcidente.getCdTipoAcidente());
	}
	public static Result remove(int cdTipoAcidente){
		return remove(cdTipoAcidente, false, null, null);
	}
	public static Result remove(int cdTipoAcidente, boolean cascade){
		return remove(cdTipoAcidente, cascade, null, null);
	}
	public static Result remove(int cdTipoAcidente, boolean cascade, AuthData authData){
		return remove(cdTipoAcidente, cascade, authData, null);
	}
	public static Result remove(int cdTipoAcidente, boolean cascade, AuthData authData, Connection connect){
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
			retorno = TipoAcidenteDAO.delete(cdTipoAcidente, connect);
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
			pstmt = connect.prepareStatement("SELECT * FROM mob_tipo_acidente ORDER BY id_tipo_acidente");
				return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TipoAcidenteServices.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! TipoAcidenteServices.getAll: " + e);
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
		return Search.find("SELECT * FROM mob_tipo_acidente", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
