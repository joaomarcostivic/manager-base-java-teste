package com.tivic.manager.acd;

import java.sql.*;
import java.util.ArrayList;

import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.util.Result;

import com.tivic.sol.connection.Conexao;

public class PlanoSecaoServices {


	public static final int TP_PLANO_CURSO = 0;
	public static final int TP_PLANO_AULA  = 1;
	
	public static Result save(PlanoSecao planoSecao){
		return save(planoSecao, null);
	}

	public static Result save(PlanoSecao planoSecao, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			if(planoSecao==null)
				return new Result(-1, "Erro ao salvar. PlanoSecao é nulo");

			int retorno;
			if(planoSecao.getCdSecao()==0){
				retorno = PlanoSecaoDAO.insert(planoSecao, connect);
				planoSecao.setCdSecao(retorno);
			}
			else {
				retorno = PlanoSecaoDAO.update(planoSecao, connect);
			}

			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();

			return new Result(retorno, (retorno<=0)?"Erro ao salvar...":"Salvo com sucesso...", "PLANOSECAO", planoSecao);
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
	public static Result remove(int cdSecao){
		return remove(cdSecao, false, null);
	}
	public static Result remove(int cdSecao, boolean cascade){
		return remove(cdSecao, cascade, null);
	}
	public static Result remove(int cdSecao, boolean cascade, Connection connect){
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
			retorno = PlanoSecaoDAO.delete(cdSecao, connect);
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
			pstmt = connect.prepareStatement("SELECT * FROM acd_plano_secao");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! PlanoSecaoServices.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! PlanoSecaoServices.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static ResultSetMap getByTipo(int tpPlano) {
		return getByTipo(tpPlano, null);
	}

	public static ResultSetMap getByTipo(int tpPlano, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM acd_plano_secao WHERE tp_plano = " + tpPlano + " ORDER BY id_secao");
			ResultSetMap rsm =  new ResultSetMap(pstmt.executeQuery());
			return rsm;
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! PlanoSecaoServices.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! PlanoSecaoServices.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static ResultSetMap getConteudosByTipo(int tpPlano) {
		return getConteudosByTipo(tpPlano, null);
	}

	public static ResultSetMap getConteudosByTipo(int tpPlano, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM acd_plano_secao "
					+ "							WHERE tp_plano = " + tpPlano
					+ "							  AND (nm_secao like '%CONTEUDO%'"
					+ "							        OR nm_secao like '%CONTEÚDO%')"
					+ " ORDER BY id_secao");
			ResultSetMap rsm =  new ResultSetMap(pstmt.executeQuery());
			return rsm;
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! PlanoSecaoServices.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! PlanoSecaoServices.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	/**
	 * Retorna as seções do plano pelo tipo (curso ou aula)
	 * 
	 * @param tpPlano
	 * @return ResultSetMap
	 */
	public static ResultSetMap getSecoesByTipo(int tpPlano) {
		return getSecoesByTipo(tpPlano, null);
	}

	public static ResultSetMap getSecoesByTipo(int tpPlano, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM acd_plano_secao WHERE tp_plano = "+tpPlano);
				return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! PlanoSecaoServices.getSecoesByTipo: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! PlanoSecaoServices.getSecoesByTipo: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static ResultSetMap findSecaoAula(ArrayList<ItemComparator> criterios) {
		
		criterios.add(new ItemComparator("tp_plano", String.valueOf(PlanoServices.AULA), ItemComparator.EQUAL, Types.INTEGER));
		return find(criterios, null);
	}
	
	public static ResultSetMap findSecaoAula(ArrayList<ItemComparator> criterios, Connection connect) {
		
		criterios.add(new ItemComparator("tp_plano", String.valueOf(PlanoServices.AULA), ItemComparator.EQUAL, Types.INTEGER));
		return find(criterios, connect);
	}
	
	public static ResultSetMap findSecaoCurso(ArrayList<ItemComparator> criterios) {
		
		criterios.add(new ItemComparator("tp_plano", String.valueOf(PlanoServices.CURSO), ItemComparator.EQUAL, Types.INTEGER));
		return find(criterios, null);
	}
	
	public static ResultSetMap findSecaoCurso(ArrayList<ItemComparator> criterios, Connection connect) {
		
		criterios.add(new ItemComparator("tp_plano", String.valueOf(PlanoServices.CURSO), ItemComparator.EQUAL, Types.INTEGER));
		return find(criterios, connect);
	}

	public static ResultSetMap find(ArrayList<ItemComparator> criterios) {
		return find(criterios, null);
	}

	public static ResultSetMap find(ArrayList<ItemComparator> criterios, Connection connect) {
		return Search.find("SELECT * FROM acd_plano_secao", " ORDER BY id_secao", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}