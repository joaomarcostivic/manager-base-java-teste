package com.tivic.manager.grl;

import java.sql.*;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.GregorianCalendar;

import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.util.Result;

import com.tivic.sol.connection.Conexao;
import com.tivic.manager.util.Util;

public class IndicadorVariacaoServices {

	public static Result save(IndicadorVariacao indicadorVariacao){
		return save(indicadorVariacao, null);
	}

	public static Result save(IndicadorVariacao indicadorVariacao, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			if(indicadorVariacao==null)
				return new Result(-1, "Erro ao salvar. IndicadorVariacao é nulo");

			int retorno;
			
			IndicadorVariacao i = IndicadorVariacaoDAO.get(indicadorVariacao.getCdIndicador(), indicadorVariacao.getDtInicio(), connect);
			
			if(i==null){
				retorno = IndicadorVariacaoDAO.insert(indicadorVariacao, connect);
				indicadorVariacao.setCdIndicador(retorno);
			}
			else {
				retorno = IndicadorVariacaoDAO.update(indicadorVariacao, indicadorVariacao.getCdIndicador(), indicadorVariacao.getDtInicio(), connect);
			}

			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();

			return new Result(retorno, (retorno<=0)?"Erro ao salvar...":"Salvo com sucesso...", "INDICADORVARIACAO", indicadorVariacao);
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
	public static Result remove(int cdIndicador, GregorianCalendar dtInicio){
		return remove(cdIndicador, dtInicio, false, null);
	}
	public static Result remove(int cdIndicador, GregorianCalendar dtInicio, boolean cascade){
		return remove(cdIndicador, dtInicio, cascade, null);
	}
	public static Result remove(int cdIndicador, GregorianCalendar dtInicio, boolean cascade, Connection connect){
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
			retorno = IndicadorVariacaoDAO.delete(cdIndicador, dtInicio, connect);
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
	
	public static ResultSetMap getAllByIndicador(int cdIndicador) {
		return getAllByIndicador(cdIndicador, null);
	}

	public static ResultSetMap getAllByIndicador(int cdIndicador, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM grl_indicador_variacao "
												+ "WHERE CD_INDICADOR=?"
												+ " ORDER BY dt_inicio");
			
				pstmt.setInt(1, cdIndicador);
				
				return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! IndicadorVariacaoServices.getAllByIndicador: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! IndicadorVariacaoServices.getAllByIndicador: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static ResultSetMap getAllByDtInicio(GregorianCalendar dtInicio) {
		return getAllByDtInicio(dtInicio, null);
	}

	public static ResultSetMap getAllByDtInicio(GregorianCalendar dtInicio, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM grl_indicador_variacao "
												+ "WHERE dt_inicio=?");
			
				pstmt.setTimestamp(1, Util.convCalendarToTimestamp(dtInicio));
				
				return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! IndicadorVariacaoServices.getAllByDtInicio: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! IndicadorVariacaoServices.getAllByDtInicio: " + e);
			return null;
		}
		finally {
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
			pstmt = connect.prepareStatement("SELECT * FROM grl_indicador_variacao");
				return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! IndicadorVariacaoServices.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! IndicadorVariacaoServices.getAll: " + e);
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
		return Search.find("SELECT * FROM grl_indicador_variacao", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
