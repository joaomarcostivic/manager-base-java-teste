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

public class PessoaCidServices {

	public static Result save(PessoaCid pessoaCid){
		return save(pessoaCid, null, null);
	}

	public static Result save(PessoaCid pessoaCid, AuthData authData){
		return save(pessoaCid, authData, null);
	}

	public static Result save(PessoaCid pessoaCid, AuthData authData, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			if(pessoaCid==null)
				return new Result(-1, "Erro ao salvar. PessoaCid é nulo");

			int retorno;
			if(PessoaCidDAO.get(pessoaCid.getCdPessoa(), pessoaCid.getCdCid(), connect)==null){
				retorno = PessoaCidDAO.insert(pessoaCid, connect);
				pessoaCid.setCdPessoa(retorno);
			}
			else {
				retorno = PessoaCidDAO.update(pessoaCid, connect);
			}

			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();

			return new Result(retorno, (retorno<=0)?"Erro ao salvar...":"Salvo com sucesso...", "PESSOACID", pessoaCid);
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
	public static Result remove(PessoaCid pessoaCid) {
		return remove(pessoaCid.getCdPessoa(), pessoaCid.getCdCid());
	}
	public static Result remove(int cdPessoa, int cdCid){
		return remove(cdPessoa, cdCid, false, null, null);
	}
	public static Result remove(int cdPessoa, int cdCid, boolean cascade){
		return remove(cdPessoa, cdCid, cascade, null, null);
	}
	public static Result remove(int cdPessoa, int cdCid, boolean cascade, AuthData authData){
		return remove(cdPessoa, cdCid, cascade, authData, null);
	}
	public static Result remove(int cdPessoa, int cdCid, boolean cascade, AuthData authData, Connection connect){
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
			retorno = PessoaCidDAO.delete(cdPessoa, cdCid, connect);
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
			pstmt = connect.prepareStatement("SELECT * FROM grl_pessoa_cid");
				return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! PessoaCidServices.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! PessoaCidServices.getAll: " + e);
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
		ResultSetMap rsm = Search.find("SELECT * FROM grl_pessoa_cid A, grl_cid B WHERE A.cd_cid = B.cd_cid ", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
		while(rsm.next()){
			rsm.setValueToField("CL_CID", rsm.getString("ID_CID") + " - " + rsm.getString("NM_CID"));
		}
		rsm.beforeFirst();
		
		return rsm;
	}

}