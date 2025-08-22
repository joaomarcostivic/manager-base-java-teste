package com.tivic.manager.ptc;

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

public class WorkflowRegraServices {
	
	public static final int ST_INATIVO = 0;
	public static final int ST_ATIVO = 1;

	public static Result save(WorkflowRegra workflowRegra){
		return save(workflowRegra, null, null, null);
	}
	
	public static Result save(WorkflowRegra workflowRegra, WorkflowGatilho workflowGatilho, WorkflowAcao workflowAcao){
		return save(workflowRegra, workflowGatilho, workflowAcao, null);
	}

	public static Result save(WorkflowRegra workflowRegra, WorkflowGatilho workflowGatilho, WorkflowAcao workflowAcao, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			if(workflowRegra==null)
				return new Result(-1, "Erro ao salvar. WorkflowRegra é nulo");

			int retorno;
			if(workflowRegra.getCdRegra()==0){
				retorno = WorkflowRegraDAO.insert(workflowRegra, connect);
				workflowRegra.setCdRegra(retorno);
			}
			else {
				retorno = WorkflowRegraDAO.update(workflowRegra, connect);
			}
			
			Result result = null;
			//GATILHO
			System.out.println("workflowGatilho: "+workflowGatilho);
			if(workflowGatilho!=null) {
				workflowGatilho.setCdRegra(workflowRegra.getCdRegra());
				
				result = WorkflowGatilhoServices.save(workflowGatilho, connect);
				if(result.getCode()<=0) {
					if(isConnectionNull)
						connect.rollback();
					return result;
				}
			}
			
			//AÇÃO
			System.out.println("workflowAcao: "+workflowAcao);
			if(workflowAcao!=null) {
				workflowAcao.setCdRegra(workflowRegra.getCdRegra());
				
				result = WorkflowAcaoServices.save(workflowAcao, connect);
				if(result.getCode()<=0) {
					if(isConnectionNull)
						connect.rollback();
					return result;
				}
			}

			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();

			return new Result(retorno, (retorno<=0)?"Erro ao salvar...":"Salvo com sucesso...", "WORKFLOWREGRA", workflowRegra);
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
	public static Result remove(int cdRegra){
		return remove(cdRegra, false, null);
	}
	public static Result remove(int cdRegra, boolean cascade){
		return remove(cdRegra, cascade, null);
	}
	public static Result remove(int cdRegra, boolean cascade, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			int retorno = 0;
			
			if(cascade){
				/** SE HOUVER, REMOVER TABELAS ASSOCIADAS **/
				retorno = WorkflowGatilhoServices.removeAllByRegra(cdRegra, connect).getCode();
				if(retorno>0)
					retorno = WorkflowAcaoServices.removeAllByRegra(cdRegra, connect).getCode();
			}
			
			if(!cascade || retorno>0)
				retorno = WorkflowRegraDAO.delete(cdRegra, connect);
			
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
			pstmt = connect.prepareStatement("SELECT A.*, B.*, C.* "
					+ " FROM ptc_workflow_regra A"
					+ " JOIN ptc_workflow_gatilho B ON (A.cd_regra = B.cd_regra)"
					+ " JOIN ptc_workflow_acao C on (A.cd_regra = C.cd_regra)");
				return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! WorkflowRegraServices.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! WorkflowRegraServices.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	/**
	 * Busca as regras correspondentes ao gatilhos passados.
	 * 
	 * @param gatilhos ArrayList<Integer> lista de TP_GATILHO
	 * @return
	 * @since 08/06/2016
	 * @author Maurício
	 */
	public static ResultSetMap getAllByGatilhos(ArrayList<Integer> gatilhos) {
		return getAllByGatilhos(gatilhos, null);
	}

	public static ResultSetMap getAllByGatilhos(ArrayList<Integer> gatilhos, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
//			GregorianCalendar hoje  = new GregorianCalendar();
			
			pstmt = connect.prepareStatement("SELECT A.*, B.*, C.* "
											+ " FROM ptc_workflow_regra A"
											+ " JOIN ptc_workflow_gatilho B ON (A.cd_regra = B.cd_regra)"
											+ " JOIN ptc_workflow_acao C ON (A.cd_regra = C.cd_regra)"
											+ " WHERE B.tp_gatilho IN ("+Util.join(gatilhos)+")"
											+ " AND A.st_regra=?");
//											+ " AND A.dt_inicial <= ?"
//											+ " AND A.dt_final >= ?");
			pstmt.setInt(1, ST_ATIVO);
//			pstmt.setTimestamp(2, Util.convCalendarToTimestamp(hoje));
//			pstmt.setTimestamp(3, Util.convCalendarToTimestamp(hoje));
			
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! WorkflowRegraServices.getAllByGatilhos: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! WorkflowRegraServices.getAllByGatilhos: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	/**
	 * Busca as regras de determinada entidade
	 * @param nmEntidade nome da entidade
	 * @return
	 */
	public static ResultSetMap getAllByNmEntidade(String nmEntidade) {
		return getAllByNmEntidade(nmEntidade, null);
	}

	public static ResultSetMap getAllByNmEntidade(String nmEntidade, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement("SELECT A.*, B.*, C.* "
											+ " FROM ptc_workflow_regra A"
											+ " JOIN ptc_workflow_gatilho B ON (A.cd_regra = B.cd_regra)"
											+ " JOIN ptc_workflow_acao C on (A.cd_regra = C.cd_regra)"
											+ " LEFT OUTER JOIN ptc_workflow_entidade D ON (B.cd_entidade = D.cd_entidade)"
											+ " WHERE D.nm_entidade = '"+nmEntidade.toUpperCase()+"'"
											+ " AND A.st_regra = "+ST_ATIVO);
				return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! WorkflowRegraServices.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! WorkflowRegraServices.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static ResultSetMap getAllByCdEntidade(int cdEntidade) {
		return getAllByCdEntidade(cdEntidade, null);
	}

	public static ResultSetMap getAllByCdEntidade(int cdEntidade, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement("SELECT A.*, B.*, C.* "
											+ " FROM ptc_workflow_regra A"
											+ " JOIN ptc_workflow_gatilho B ON (A.cd_regra = B.cd_regra)"
											+ " JOIN ptc_workflow_acao C on (A.cd_regra = C.cd_regra)"
											+ " WHERE B.cd_entidade = "+cdEntidade);
				return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! WorkflowRegraServices.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! WorkflowRegraServices.getAll: " + e);
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
		return Search.find("SELECT * FROM ptc_workflow_regra", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
