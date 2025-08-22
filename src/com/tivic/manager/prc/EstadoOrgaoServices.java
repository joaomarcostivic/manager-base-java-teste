package com.tivic.manager.prc;

import com.tivic.sol.connection.Conexao;

import java.sql.*;
import java.util.ArrayList;

import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.util.Result;

public class EstadoOrgaoServices {

	public static Result save(EstadoOrgao estadoOrgao){
		return save(estadoOrgao, null);
	}

	public static Result save(EstadoOrgao estadoOrgao, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			if(estadoOrgao==null)
				return new Result(-1, "Erro ao salvar. EstadoOrgao é nulo");

			int retorno;
			
			if(EstadoOrgaoDAO.get(estadoOrgao.getCdEstado(), estadoOrgao.getCdOrgao(), connect)==null){
				retorno = EstadoOrgaoDAO.insert(estadoOrgao, connect);
				estadoOrgao.setCdEstado(retorno);
			}
			else {
				retorno = EstadoOrgaoDAO.update(estadoOrgao, connect);
			}

			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();

			return new Result(retorno, (retorno<=0)?"Erro ao salvar...":"Salvo com sucesso...", "ESTADOORGAO", estadoOrgao);
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
	public static Result remove(int cdEstado, int cdOrgao){
		return remove(cdEstado, cdOrgao, false, null);
	}
	public static Result remove(int cdEstado, int cdOrgao, boolean cascade){
		return remove(cdEstado, cdOrgao, cascade, null);
	}
	public static Result remove(int cdEstado, int cdOrgao, boolean cascade, Connection connect){
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
			retorno = EstadoOrgaoDAO.delete(cdEstado, cdOrgao, connect);
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
			pstmt = connect.prepareStatement("SELECT * FROM PRC_ESTADO_ORGAO");
				return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! EstadoOrgaoServices.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! EstadoOrgaoServices.getAll: " + e);
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
		return Search.find("SELECT * FROM PRC_ESTADO_ORGAO", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}
	
	/**
	 * Busca todos os orgãos que atendem um estado.
	 * 
	 * @param cdEstado código do estado
	 * @return ResultSetMap Lista dos orgãos que atendem ao estado
	 * @author Maurício
	 * @since 24/03/2015
	 */
	public static ResultSetMap getAllOrgaoByEstado(int cdEstado) {
		return getAllOrgaoByEstado(cdEstado, null);
	}

	public static ResultSetMap getAllOrgaoByEstado(int cdEstado, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement(
					"SELECT A.*, C.cd_estado, C.nm_estado "
					+ " FROM prc_orgao A"
					+ " JOIN prc_estado_orgao B ON (A.cd_orgao = B.cd_orgao)"
					+ " JOIN grl_estado C ON (B.cd_estado = C.cd_estado AND C.cd_estado = "+cdEstado+")"
			);
			
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! EstadoOrgaoServices.getAllOrgaoByEstado: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! EstadoOrgaoServices.getAllOrgaoByEstado: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	/**
	 * Busca todos os estados atendidos por um determinado orgão.
	 *  
	 * @param cdOrgao código do estado
	 * @return ResultSetMap lista de estados atendidos
	 * @author Maurício
	 * @since 24/03/2015
	 */
	public static ResultSetMap getAllEstadoByOrgao(int cdOrgao) {
		return getAllEstadoByOrgao(cdOrgao, null);
	}

	public static ResultSetMap getAllEstadoByOrgao(int cdOrgao, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
		
			pstmt = connect.prepareStatement(
					"SELECT A.*, C.cd_orgao, C.nm_orgao"
					+ " FROM grl_estado A"
					+ " JOIN prc_estado_orgao B ON (A.cd_estado = B.cd_estado)"
					+ " JOIN prc_orgao C ON (B.cd_orgao = C.cd_orgao AND C.cd_orgao = "+cdOrgao+")"
			);
			
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! EstadoOrgaoServices.getAllEstadoByOrgao: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! EstadoOrgaoServices.getAllEstadoByOrgao: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

}
