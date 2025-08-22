package com.tivic.manager.grl;

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

public class CidReferenciaServices {

	public static Result save(CidReferencia cidReferencia){
		return save(cidReferencia, null, null);
	}

	public static Result save(CidReferencia cidReferencia, AuthData authData){
		return save(cidReferencia, authData, null);
	}

	public static Result save(CidReferencia cidReferencia, AuthData authData, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			if(cidReferencia==null)
				return new Result(-1, "Erro ao salvar. CidReferencia é nulo");

			int retorno;
			if(cidReferencia.getCdCid()==0){
				retorno = CidReferenciaDAO.insert(cidReferencia, connect);
				cidReferencia.setCdCid(retorno);
			}
			else {
				retorno = CidReferenciaDAO.update(cidReferencia, connect);
			}

			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();

			return new Result(retorno, (retorno<=0)?"Erro ao salvar...":"Salvo com sucesso...", "CIDREFERENCIA", cidReferencia);
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
	public static Result remove(CidReferencia cidReferencia) {
		return remove(cidReferencia.getCdCid(), cidReferencia.getCdCidReferencia());
	}
	public static Result remove(int cdCid, int cdCidReferencia){
		return remove(cdCid, cdCidReferencia, false, null, null);
	}
	public static Result remove(int cdCid, int cdCidReferencia, boolean cascade){
		return remove(cdCid, cdCidReferencia, cascade, null, null);
	}
	public static Result remove(int cdCid, int cdCidReferencia, boolean cascade, AuthData authData){
		return remove(cdCid, cdCidReferencia, cascade, authData, null);
	}
	public static Result remove(int cdCid, int cdCidReferencia, boolean cascade, AuthData authData, Connection connect){
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
			retorno = CidReferenciaDAO.delete(cdCid, cdCidReferencia, connect);
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
			pstmt = connect.prepareStatement("SELECT * FROM grl_cid_referencia");
				return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! CidReferenciaServices.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! CidReferenciaServices.getAll: " + e);
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
		return Search.find("SELECT * FROM grl_cid_referencia", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}