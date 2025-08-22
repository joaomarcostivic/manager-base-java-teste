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

public class CidExcluidosServices {

	public static Result save(CidExcluidos cidExcluidos){
		return save(cidExcluidos, null, null);
	}

	public static Result save(CidExcluidos cidExcluidos, AuthData authData){
		return save(cidExcluidos, authData, null);
	}

	public static Result save(CidExcluidos cidExcluidos, AuthData authData, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			if(cidExcluidos==null)
				return new Result(-1, "Erro ao salvar. CidExcluidos é nulo");

			int retorno;
			if(cidExcluidos.getCdCidExcluido()==0){
				retorno = CidExcluidosDAO.insert(cidExcluidos, connect);
				cidExcluidos.setCdCidExcluido(retorno);
			}
			else {
				retorno = CidExcluidosDAO.update(cidExcluidos, connect);
			}

			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();

			return new Result(retorno, (retorno<=0)?"Erro ao salvar...":"Salvo com sucesso...", "CIDEXCLUIDOS", cidExcluidos);
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
	public static Result remove(CidExcluidos cidExcluidos) {
		return remove(cidExcluidos.getCdCidExcluido(), cidExcluidos.getCdCid());
	}
	public static Result remove(int cdCidExcluido, int cdCid){
		return remove(cdCidExcluido, cdCid, false, null, null);
	}
	public static Result remove(int cdCidExcluido, int cdCid, boolean cascade){
		return remove(cdCidExcluido, cdCid, cascade, null, null);
	}
	public static Result remove(int cdCidExcluido, int cdCid, boolean cascade, AuthData authData){
		return remove(cdCidExcluido, cdCid, cascade, authData, null);
	}
	public static Result remove(int cdCidExcluido, int cdCid, boolean cascade, AuthData authData, Connection connect){
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
			retorno = CidExcluidosDAO.delete(cdCidExcluido, cdCid, connect);
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
			pstmt = connect.prepareStatement("SELECT * FROM grl_cid_excluidos");
				return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! CidExcluidosServices.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! CidExcluidosServices.getAll: " + e);
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
		return Search.find("SELECT * FROM grl_cid_excluidos", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}