package com.tivic.manager.prc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ArrayList;

import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;
import sol.dao.Search;
import sol.util.Result;

import com.tivic.sol.connection.Conexao;

public class AndamentoPrazoServices {
	
	public static final int TP_CONTAGEM_CORRIDO = 0;
	public static final int TP_CONTAGEM_UTIL = 1;
	
	public static Result save(AndamentoPrazo andamentoPrazo){
		return save(andamentoPrazo, null);
	}
	
	public static Result save(AndamentoPrazo andamentoPrazo, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			if(andamentoPrazo==null)
				return new Result(-1, "Erro ao salvar. Registro é nulo");
			
			int retorno;
			if(andamentoPrazo.getCdAndamentoPrazo()==0){
				retorno = AndamentoPrazoDAO.insert(andamentoPrazo, connect);
				andamentoPrazo.setCdAndamentoPrazo(retorno);
			}
			else {
				retorno = AndamentoPrazoDAO.update(andamentoPrazo, connect);
			}
			
			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();
			
			return new Result(retorno, (retorno<=0)?"Erro ao salvar...":"Salvo com sucesso...", "ANDAMENTOPRAZO", andamentoPrazo);
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
	
	public static Result remove(int cdAndamentoPrazo){
		return remove(cdAndamentoPrazo, false, null);
	}
	
	public static Result remove(int cdAndamentoPrazo, boolean cascade){
		return remove(cdAndamentoPrazo, cascade, null);
	}
	
	public static Result remove(int cdAndamentoPrazo, boolean cascade, Connection connect){
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
				retorno = AndamentoPrazoDAO.delete(cdAndamentoPrazo, connect);
			
			if(retorno<=0){
				Conexao.rollback(connect);
				return new Result(-2, "Este registro está vinculado a outros registros e não pode ser excluído!");
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
			pstmt = connect.prepareStatement("SELECT * FROM prc_andamento_prazo ORDER BY qt_dias");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! AndamentoPrazoServices.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ResultSetMap getAllByTipoAndamento(int cdTipoAndamento) {
		return getAllByTipoAndamento(cdTipoAndamento, null);
	}

	public static ResultSetMap getAllByTipoAndamento(int cdTipoAndamento, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement("SELECT A.*, B.nm_tipo_prazo, B.tp_agenda_item, C.nm_tipo_processo, D.nm_tipo_andamento " +
					" FROM prc_andamento_prazo A " +
					" LEFT OUTER JOIN prc_tipo_prazo B ON (B.cd_tipo_prazo = A.cd_tipo_prazo) " +
					" LEFT OUTER JOIN prc_tipo_processo C ON (C.cd_tipo_processo = A.cd_tipo_processo) " +
					" LEFT OUTER JOIN prc_tipo_andamento D ON (D.cd_tipo_andamento = A.cd_tipo_andamento) " +
					"WHERE A.cd_tipo_andamento = ? ORDER BY A.qt_dias");
			pstmt.setInt(1, cdTipoAndamento);
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
	public static ResultSetMap find(ArrayList<ItemComparator> criterios) {
		return find(criterios, null);
	}

	public static ResultSetMap find(ArrayList<ItemComparator> criterios, Connection connect) {
		return Search.find("SELECT * FROM prc_andamento_prazo", "ORDER BY qt_dias", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}
}
