package com.tivic.manager.ptc;

import java.sql.Connection;
import java.util.ArrayList;

import com.tivic.sol.connection.Conexao;

import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;
import sol.dao.Search;
import sol.util.Result;

public class TipoPendenciaServices {
	public static Result save(TipoPendencia tipoPendencia){
		return save(tipoPendencia, null);
	}
	
	public static Result save(TipoPendencia tipoPendencia, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			if(tipoPendencia==null)
				return new Result(-1, "Erro ao salvar. Tipo de pendência é nulo");
			
			int retorno;
			if(tipoPendencia.getCdTipoPendencia()==0){
				retorno = TipoPendenciaDAO.insert(tipoPendencia, connect);
				tipoPendencia.setCdTipoPendencia(retorno);
			}
			else {
				retorno = TipoPendenciaDAO.update(tipoPendencia, connect);
			}
			
			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();
			
			return new Result(retorno, (retorno<=0)?"Erro ao salvar...":"Salvo com sucesso...", "TIPOPENDENCIA", tipoPendencia);
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
	
	public static Result remove(int cdTipoPendencia){
		return remove(cdTipoPendencia, false, null);
	}
	
	public static Result remove(int cdTipoPendencia, boolean cascade){
		return remove(cdTipoPendencia, cascade, null);
	}
	
	public static Result remove(int cdTipoPendencia, boolean cascade, Connection connect){
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
				retorno = TipoPendenciaDAO.delete(cdTipoPendencia, connect);
			
			if(retorno<=0){
				Conexao.rollback(connect);
				return new Result(-2, "Este tipo de pendência está vinculado a outros registros e não pode ser excluído!");
			}
			else if (isConnectionNull)
				connect.commit();
			
			return new Result(1, "Tipo de pendência excluído com sucesso!");
		} 
		catch(Exception e){
			e.printStackTrace();
			if (isConnectionNull)
				Conexao.rollback(connect);
			return new Result(-1, "Erro ao excluir tipo de pendência!");
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
			return new ResultSetMap(connect.prepareStatement("SELECT * FROM ptc_tipo_pendencia ORDER BY nm_tipo_pendencia").executeQuery());
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
		return Search.find("SELECT * FROM ptc_tipo_pendencia ", "ORDER BY nm_tipo_pendencia", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}
}
