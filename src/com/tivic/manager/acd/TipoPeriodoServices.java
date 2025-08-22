package com.tivic.manager.acd;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ArrayList;

import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;
import sol.dao.Search;
import sol.util.Result;

import com.tivic.sol.connection.Conexao;

public class TipoPeriodoServices {

	/* situações de tipos de períodos letivos */
	public static final int ST_INATIVO = 0;
	public static final int ST_ATIVO =1;
	
	public static final String[] situacoes = {"Inativo", 
		"Ativo"};
	
	public static Result save(TipoPeriodo tipoPeriodo){
		return save(tipoPeriodo, null);
	}
	
	public static Result save(TipoPeriodo tipoPeriodo, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			if(tipoPeriodo==null)
				return new Result(-1, "Erro ao salvar. Tipo de período é nulo");
			
			int retorno;
			if(tipoPeriodo.getCdTipoPeriodo()==0){
				retorno = TipoPeriodoDAO.insert(tipoPeriodo, connect);
				tipoPeriodo.setCdTipoPeriodo(retorno);
			}
			else {
				retorno = TipoPeriodoDAO.update(tipoPeriodo, connect);
			}
			
			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();
			
			return new Result(retorno, (retorno<=0)?"Erro ao salvar...":"Salvo com sucesso...", "TIPOPERIODO", tipoPeriodo);
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
	
	public static Result remove(int cdTipoPeriodo){
		return remove(cdTipoPeriodo, false, null);
	}
	
	public static Result remove(int cdTipoPeriodo, boolean cascade){
		return remove(cdTipoPeriodo, cascade, null);
	}
	
	public static Result remove(int cdTipoPeriodo, boolean cascade, Connection connect){
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
				retorno = TipoPeriodoDAO.delete(cdTipoPeriodo, connect);
			
			if(retorno<=0){
				Conexao.rollback(connect);
				return new Result(-2, "Este tipo de período está vinculado a outros registros e não pode ser excluído!");
			}
			else if (isConnectionNull)
				connect.commit();
			
			return new Result(1, "tipo de período excluído com sucesso!");
		}
		catch(Exception e){
			e.printStackTrace();
			if (isConnectionNull)
				Conexao.rollback(connect);
			return new Result(-1, "Erro ao excluir tipo de período!");
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
			pstmt = connect.prepareStatement("SELECT * FROM acd_tipo_periodo ORDER BY cd_tipo_periodo");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! TipoPeriodoDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static TipoPeriodo getByName(String nmTipoPeriodo) {
		return getByName(nmTipoPeriodo, null);
	}

	public static TipoPeriodo getByName(String nmTipoPeriodo, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM acd_tipo_periodo " +
											 " where nm_tipo_periodo = ? " +
											 " ORDER BY cd_tipo_periodo");
			pstmt.setString(1, nmTipoPeriodo);
			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
			if(rsm.next()){
				return TipoPeriodoDAO.get(rsm.getInt("cd_tipo_periodo"), connect);
			}
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! TipoPeriodoDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static TipoPeriodo getById(String idTipoPeriodo) {
		return getById(idTipoPeriodo, null);
	}

	public static TipoPeriodo getById(String idTipoPeriodo, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM acd_tipo_periodo " +
											 " where id_tipo_periodo = ? " +
											 " ORDER BY cd_tipo_periodo");
			pstmt.setString(1, idTipoPeriodo);
			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
			if(rsm.next()){
				return TipoPeriodoDAO.get(rsm.getInt("cd_tipo_periodo"), connect);
			}
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! TipoPeriodoDAO.getById: " + e);
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
		ResultSetMap  _rsm = Search.find("SELECT * FROM acd_tipo_periodo", "ORDER BY nm_tipo_periodo" +limit, criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	    return _rsm;
	}
		
}
