package com.tivic.manager.mob;

import java.sql.*;

import sol.dao.ResultSetMap;

import com.tivic.sol.connection.Conexao;
import com.tivic.manager.util.Util;


import java.util.ArrayList;

import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.util.Result;

import com.tivic.manager.grl.ParametroServices;
import com.tivic.manager.seg.AuthData;

public class InfracaoObservacaoServices {
	public static ResultSetMap getSyncData() {
		return getSyncData(null);
	}

	public static ResultSetMap getSyncData(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			
			String sql = "SELECT * FROM mob_infracao_observacao";
			
			if(Util.getConfManager().getProps().getProperty("STR_BASE_ANTIGA").equals("1"))
				sql = "SELECT A.*, A.cod_infracao as cd_infracao FROM str_infracao_observacao A";
			pstmt = connect.prepareStatement(sql);
			
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static Result save(InfracaoObservacao infracaoObservacao){
		return save(infracaoObservacao, null, null);
	}

	public static Result save(InfracaoObservacao infracaoObservacao, AuthData authData){
		return save(infracaoObservacao, authData, null);
	}

	public static Result save(InfracaoObservacao infracaoObservacao, AuthData authData, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			if(infracaoObservacao==null)
				return new Result(-1, "Erro ao salvar. InfracaoObservacao é nulo");

			int retorno;
			
			if(infracaoObservacao.getCdObservacao() <= 0){
				System.out.println("Insert");
				retorno = InfracaoObservacaoDAO.insert(infracaoObservacao, connect);
				infracaoObservacao.setCdInfracao(retorno);
			} else {
				System.out.println("Update");
				retorno = InfracaoObservacaoDAO.update(infracaoObservacao, connect);
			}

			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();

			return new Result(retorno, (retorno<=0)?"Erro ao salvar...":"Salvo com sucesso...", "INFRACAOOBSERVACAO", infracaoObservacao);
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
	public static Result remove(InfracaoObservacao infracaoObservacao) {
		return remove(infracaoObservacao.getCdInfracao(), infracaoObservacao.getCdObservacao());
	}
	public static Result remove(int cdInfracao, int cdObservacao){
		return remove(cdInfracao, cdObservacao, false, null, null);
	}
	public static Result remove(int cdInfracao, int cdObservacao, boolean cascade){
		return remove(cdInfracao, cdObservacao, cascade, null, null);
	}
	public static Result remove(int cdInfracao, int cdObservacao, boolean cascade, AuthData authData){
		return remove(cdInfracao, cdObservacao, cascade, authData, null);
	}
	public static Result remove(int cdInfracao, int cdObservacao, boolean cascade, AuthData authData, Connection connect){
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
			retorno = InfracaoObservacaoDAO.delete(cdInfracao, cdObservacao, connect);
			if(retorno<=0){
				Conexao.rollback(connect);
				return new Result(-2, "Este registro estÃ¡ vinculado a outros e nÃ£o pode ser excluÃ­do!");
			}
			else if (isConnectionNull)
				connect.commit();
			return new Result(1, "Registro excluÃ­do com sucesso!");
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
			pstmt = connect.prepareStatement("SELECT * FROM mob_infracao_observacao");
				return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! InfracaoObservacaoServices.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! InfracaoObservacaoServices.getAll: " + e);
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
		return Search.find("SELECT * FROM mob_infracao_observacao", "ORDER BY st_observacao DESC", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}
}
