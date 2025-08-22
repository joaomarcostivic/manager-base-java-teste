package com.tivic.manager.mob;

import java.sql.*;
import java.sql.PreparedStatement;
import java.util.ArrayList;

import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.util.Result;

import com.tivic.sol.connection.Conexao;
//import com.tivic.manager.grl.ParametroServices;
import com.tivic.manager.seg.AuthData;

public class PontoChecagemServices {
	
	public static final int ST_INATIVO   = 0;
	public static final int ST_ATIVO = 1;

	public static String[] situacaoPontoChecagem = {"Inativo", "Ativo"};
	
	public static final int TP_GUARITA = 0;
	public static final int TP_PEDAGIO = 1;
	
	public static String[] tipoPontoChecagem = {"Guarita"};

	public static Result save(PontoChecagem pontoChecagem){
		return save(pontoChecagem, null, null);
	}

	public static Result save(PontoChecagem pontoChecagem, AuthData authData){
		return save(pontoChecagem, authData, null);
	}

	public static Result save(PontoChecagem pontoChecagem, AuthData authData, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			if(pontoChecagem==null)
				return new Result(-1, "Erro ao salvar. PontoChecagem é nulo");

			int retorno;
			if(pontoChecagem.getCdPontoChecagem()==0){
				retorno = PontoChecagemDAO.insert(pontoChecagem, connect);
				pontoChecagem.setCdPontoChecagem(retorno);
			}
			else {
				retorno = PontoChecagemDAO.update(pontoChecagem, connect);
			}

			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();

			return new Result(retorno, (retorno<=0)?"Erro ao salvar...":"Salvo com sucesso...", "PONTOCHECAGEM", pontoChecagem);
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
	public static Result remove(int cdPontoChecagem){
		return remove(cdPontoChecagem, false, null, null);
	}
	public static Result remove(int cdPontoChecagem, boolean cascade){
		return remove(cdPontoChecagem, cascade, null, null);
	}
	public static Result remove(int cdPontoChecagem, boolean cascade, AuthData authData){
		return remove(cdPontoChecagem, cascade, authData, null);
	}
	public static Result remove(int cdPontoChecagem, boolean cascade, AuthData authData, Connection connect){
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
			retorno = PontoChecagemDAO.delete(cdPontoChecagem, connect);
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
			pstmt = connect.prepareStatement("SELECT A.*, B.cd_pessoa, B.nm_pessoa FROM mob_ponto_checagem A " + 
									         " LEFT OUTER JOIN grl_pessoa B ON (A.cd_pessoa = B.cd_pessoa)");
				return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! PontoChecagemServices.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! PontoChecagemServices.getAll: " + e);
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
		return Search.find("SELECT A.*, B.nm_pessoa FROM mob_ponto_checagem A " + 
						   " LEFT OUTER JOIN grl_pessoa B ON (A.cd_pessoa = B.cd_pessoa)", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}