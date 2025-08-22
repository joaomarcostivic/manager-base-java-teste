package com.tivic.manager.acd;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ArrayList;

import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;
import sol.dao.Search;
import sol.util.Result;

import com.tivic.sol.connection.Conexao;

public class TipoMantenedoraServices {
	public static Result save(TipoMantenedora tipoMantenedora){
		return save(tipoMantenedora, null);
	}
	
	public static Result save(TipoMantenedora tipoMantenedora, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			if(tipoMantenedora==null)
				return new Result(-1, "Erro ao salvar. Tipo de mantenedora é nulo");
			
			int retorno;
			if(tipoMantenedora.getCdTipoMantenedora()==0){
				retorno = TipoMantenedoraDAO.insert(tipoMantenedora, connect);
				tipoMantenedora.setCdTipoMantenedora(retorno);
			}
			else {
				retorno = TipoMantenedoraDAO.update(tipoMantenedora, connect);
			}
			
			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();
			
			return new Result(retorno, (retorno<=0)?"Erro ao salvar...":"Salvo com sucesso...", "TIPOMANTENEDORA", tipoMantenedora);
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
	
	public static Result remove(int cdTipoMantenedora){
		return remove(cdTipoMantenedora, false, null);
	}
	
	public static Result remove(int cdTipoMantenedora, boolean cascade){
		return remove(cdTipoMantenedora, cascade, null);
	}
	
	public static Result remove(int cdTipoMantenedora, boolean cascade, Connection connect){
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
				retorno = TipoMantenedoraDAO.delete(cdTipoMantenedora, connect);
			
			if(retorno<=0){
				Conexao.rollback(connect);
				return new Result(-2, "Este tipo de mantenedora está vinculado a outros registros e não pode ser excluído!");
			}
			else if (isConnectionNull)
				connect.commit();
			
			return new Result(1, "Tipo de mantenedora excluído com sucesso!");
		}
		catch(Exception e){
			e.printStackTrace();
			if (isConnectionNull)
				Conexao.rollback(connect);
			return new Result(-1, "Erro ao excluir tipo de mantenedora!");
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
			pstmt = connect.prepareStatement("SELECT * FROM acd_tipo_mantenedora ORDER BY nm_tipo_mantenedora");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! TipoMantenedoraDAO.getAll: " + e);
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
		String limit = "";
		for (int i=0; criterios!=null && i<criterios.size(); i++) {
			if(criterios.get(i).getColumn().equalsIgnoreCase("limit")) {
				limit += " LIMIT "+ criterios.get(i).getValue().toString().trim();
				criterios.remove(i);
				i--;
			}
		}
		ResultSetMap  _rsm = Search.find("SELECT * FROM acd_tipo_mantenedora ", " ORDER BY nm_tipo_mantenedora " +limit, criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
		return _rsm;
	}
}
