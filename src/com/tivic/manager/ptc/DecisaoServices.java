package com.tivic.manager.ptc;

import java.sql.Connection;
import java.util.ArrayList;

import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;
import sol.dao.Search;
import sol.util.Result;

import com.tivic.sol.connection.Conexao;

public class DecisaoServices {
	public static Result save(Decisao decisao){
		return save(decisao, null);
	}
	
	public static Result save(Decisao decisao, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			if(decisao==null)
				return new Result(-1, "Erro ao salvar. Decisão é nula");
			
			int retorno;
			if(decisao.getCdDecisao()==0){
				retorno = DecisaoDAO.insert(decisao, connect);
				decisao.setCdDecisao(retorno);
			}
			else {
				retorno = DecisaoDAO.update(decisao, connect);
			}
			
			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();
			
			return new Result(retorno, (retorno<=0)?"Erro ao salvar...":"Salvo com sucesso...", "DECISAO", decisao);
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
	
	public static Result remove(int cdDecisao, int cdFluxo){
		return remove(cdDecisao, cdFluxo, false, null);
	}
	
	public static Result remove(int cdDecisao, int cdFluxo, boolean cascade){
		return remove(cdDecisao, cdFluxo, cascade, null);
	}
	
	public static Result remove(int cdDecisao, int cdFluxo, boolean cascade, Connection connect){
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
				retorno = DecisaoDAO.delete(cdDecisao, cdFluxo, connect);
			
			if(retorno<=0){
				Conexao.rollback(connect);
				return new Result(-2, "Esta decisao está vinculada a outros registros e não pode ser excluída!");
			}
			else if (isConnectionNull)
				connect.commit();
			
			return new Result(1, "Decisao excluída com sucesso!");
		}
		catch(Exception e){
			e.printStackTrace();
			if (isConnectionNull)
				Conexao.rollback(connect);
			return new Result(-1, "Erro ao excluir decisao!");
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
		try {
			return new ResultSetMap(connect.prepareStatement("SELECT * FROM ptc_decisao ORDER BY nm_decisao").executeQuery());
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
	
	public static ResultSetMap getAllByFluxo(int cdFluxo) {
		return getAllByFluxo(cdFluxo, null);
	}

	public static ResultSetMap getAllByFluxo(int cdFluxo, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			return new ResultSetMap(connect.prepareStatement("SELECT * FROM ptc_decisao A " +
					" JOIN ptc_fluxo B ON (A.cd_fluxo = B.cd_fluxo)" +
					" WHERE A.cd_fluxo = "+cdFluxo+
					" ORDER BY A.nm_decisao").executeQuery());
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

	public static ResultSetMap find(ArrayList<ItemComparator> criterios) {
		return find(criterios, null);
	}

	public static ResultSetMap find(ArrayList<ItemComparator> criterios, Connection connect) {
		return Search.find("SELECT * FROM ptc_decisao ", "ORDER BY nm_decisao", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}
}
