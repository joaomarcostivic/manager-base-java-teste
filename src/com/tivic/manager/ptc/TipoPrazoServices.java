package com.tivic.manager.ptc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ArrayList;

import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;
import sol.dao.Search;
import sol.util.Result;

import com.tivic.sol.connection.Conexao;

public class TipoPrazoServices {

	
	public static final String[] tiposAgendaItem  = {"Audiência", "Prazo", "Tarefa", "Diligência"};
	
	public static Result save(TipoPrazo tipoPrazo){
		return save(tipoPrazo, null);
	}
	
	public static Result save(TipoPrazo tipoPrazo, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			if(tipoPrazo==null)
				return new Result(-1, "Erro ao salvar. Tipo de prazo é nulo");
			
			int retorno;
			if(tipoPrazo.getCdTipoPrazo()==0){
				retorno = TipoPrazoDAO.insert(tipoPrazo, connect);
				tipoPrazo.setCdTipoPrazo(retorno);
			}
			else {
				retorno = TipoPrazoDAO.update(tipoPrazo, connect);
			}
			
			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();
			
			return new Result(retorno, (retorno<=0)?"Erro ao salvar...":"Salvo com sucesso...", "TIPOPRAZO", tipoPrazo);
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
	
	public static Result remove(int cdTipoPrazo){
		return remove(cdTipoPrazo, false, null);
	}
	
	public static Result remove(int cdTipoPrazo, boolean cascade){
		return remove(cdTipoPrazo, cascade, null);
	}
	
	public static Result remove(int cdTipoPrazo, boolean cascade, Connection connect){
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
				retorno = TipoPrazoDAO.delete(cdTipoPrazo, connect);
			
			if(retorno<=0){
				Conexao.rollback(connect);
				return new Result(-2, "Este tipo de prazo está vinculado a outros registros e não pode ser excluído!");
			}
			else if (isConnectionNull)
				connect.commit();
			
			return new Result(1, "Tipo de prazo excluído com sucesso!");
		}
		catch(Exception e){
			e.printStackTrace();
			if (isConnectionNull)
				Conexao.rollback(connect);
			return new Result(-1, "Erro ao excluir tipo de prazo!");
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
			pstmt = connect.prepareStatement("SELECT * FROM ptc_tipo_prazo ORDER BY nm_tipo_prazo");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! TipoPrazoDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static ResultSetMap getAllByTipoAgenda(int tpAgendaItem) {
		return getAllByTipoAgenda(tpAgendaItem, null);
	}

	public static ResultSetMap getAllByTipoAgenda(int tpAgendaItem, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM ptc_tipo_prazo " +
					"WHERE tp_agenda_item = ? ORDER BY nm_tipo_prazo");
			pstmt.setInt(1, tpAgendaItem);
			
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! TipoPrazoServices.getAllByTipoAgenda: " + e);
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
		return Search.find("SELECT * FROM ptc_tipo_prazo", "ORDER BY nm_tipo_prazo", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}
}
