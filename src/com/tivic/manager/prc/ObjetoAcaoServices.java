package com.tivic.manager.prc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ArrayList;

import com.tivic.sol.connection.Conexao;

import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;
import sol.dao.Search;
import sol.util.Result;

public class ObjetoAcaoServices {
	public static Result save(ObjetoAcao objetoAcao){
		return save(objetoAcao, null);
	}
	
	public static Result save(ObjetoAcao objetoAcao, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			if(objetoAcao==null)
				return new Result(-1, "Erro ao salvar. Objeto da ação é nulo");
			
			int retorno;
			if(objetoAcao.getCdObjetoAcao()==0){
				retorno = ObjetoAcaoDAO.insert(objetoAcao, connect);
				objetoAcao.setCdObjetoAcao(retorno);
			}
			else {
				retorno = ObjetoAcaoDAO.update(objetoAcao, connect);
			}
			
			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();
			
			return new Result(retorno, (retorno<=0)?"Erro ao salvar...":"Salvo com sucesso...", "OBJETOACAO", objetoAcao);
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
	
	public static Result remove(int cdObjetoAcao){
		return remove(cdObjetoAcao, false, null);
	}
	
	public static Result remove(int cdObjetoAcao, boolean cascade){
		return remove(cdObjetoAcao, cascade, null);
	}
	
	public static Result remove(int cdObjetoAcao, boolean cascade, Connection connect){
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
				retorno = ObjetoAcaoDAO.delete(cdObjetoAcao, connect);
			
			if(retorno<=0){
				Conexao.rollback(connect);
				return new Result(-2, "Este objeto da ação está vinculado a outros registros e não pode ser excluído!");
			}
			else if (isConnectionNull)
				connect.commit();
			
			return new Result(1, "Objeto da ação excluído com sucesso!");
		}
		catch(Exception e){
			e.printStackTrace();
			if (isConnectionNull)
				Conexao.rollback(connect);
			return new Result(-1, "Erro ao excluir objeto da ação!");
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
			pstmt = connect.prepareStatement("SELECT * FROM prc_objeto_acao ORDER BY nm_objeto_acao");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ObjetoAcaoDAO.getAll: " + e);
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
		return Search.find("SELECT * FROM prc_objeto_acao ORDER BY nm_objeto_acao", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}
}
