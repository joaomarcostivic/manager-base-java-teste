package com.tivic.manager.ctb;

import java.sql.*;
import java.sql.PreparedStatement;
import java.util.ArrayList;

import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.util.Result;

import com.tivic.sol.connection.Conexao;

public class PrestacaoContasServices {
	
	/*** Situações durante o processo de prepara para envio **/
	public static final int ST_EM_ANDAMENTO     = 0;
	public static final int ST_EM_CONFERENCIA   = 1;
	public static final int ST_EM_CORRECAO      = 2;
	public static final int ST_CONCLUIDA        = 3;
	/***** Situações após envio e avaliação da entidade fiscalizadora ****/
	public static final int ST_APROVADA         = 4;
	public static final int ST_REPROVADA        = 5;
	
	
	public static final String[] situacao = {"Em Andamento","Em Conferência", "Em Correção",
											"Concluída", "Aprovada", "Reprovada" };
	
	public static Result save(PrestacaoContas prestacaoContas){
		return save(prestacaoContas, null);
	}

	public static Result save(PrestacaoContas prestacaoContas, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			if(prestacaoContas==null)
				return new Result(-1, "Erro ao salvar. PrestacaoContas é nulo");

			int retorno;
			if(prestacaoContas.getCdPrestacaoContas()==0){
				retorno = PrestacaoContasDAO.insert(prestacaoContas, connect);
				prestacaoContas.setCdPrestacaoContas(retorno);
			}
			else {
				retorno = PrestacaoContasDAO.update(prestacaoContas, connect);
			}

			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();

			return new Result(retorno, (retorno<=0)?"Erro ao salvar...":"Salvo com sucesso...", "PRESTACAOCONTAS", prestacaoContas);
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
	public static Result remove(int cdPrestacaoContas){
		return remove(cdPrestacaoContas, false, null);
	}
	public static Result remove(int cdPrestacaoContas, boolean cascade){
		return remove(cdPrestacaoContas, cascade, null);
	}
	public static Result remove(int cdPrestacaoContas, boolean cascade, Connection connect){
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
			retorno = PrestacaoContasDAO.delete(cdPrestacaoContas, connect);
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
			pstmt = connect.prepareStatement("SELECT * FROM ctb_prestacao_contas");
				return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! PrestacaoContasServices.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! PrestacaoContasServices.getAll: " + e);
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
		return Search.find("SELECT * FROM ctb_prestacao_contas", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
