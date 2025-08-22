package com.tivic.manager.adm;

import java.sql.*;
import java.sql.PreparedStatement;
import java.util.ArrayList;

import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.util.Result;

import com.tivic.sol.connection.Conexao;

public class EventoFinanceiroServices {

	public static final int tpPROVENTO = 0; 
	public static final int tpDESCONTO = 1; 
	// Tipo de Lançamento
	public static final int tlHORAS = 0; 
	public static final int tlDIAS = 1; 
	public static final int tlPERCENTUAL = 2; 
	public static final int tlVALOR = 3; 
	public static final int tlPERC_SAL_MINIMO = 4; 
	public static final int tlPERC_BRUTO   = 5; 
	public static final int tlPERC_LIQUIDO = 6;
	public static final int tlHORA_AULA_P1 = 7; 
	public static final int tlHORA_AULA_P2 = 8; 
	public static final int tlHORA_AULA_P3 = 9; 
	public static final int tlHORA_AULA_P4 = 10;
	public static final int tlHORA_AULA_P5 = 11;
	
	public static Result save(EventoFinanceiro eventoFinanceiro){
		return save(eventoFinanceiro, null);
	}

	public static Result save(EventoFinanceiro eventoFinanceiro, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			if(eventoFinanceiro==null)
				return new Result(-1, "Erro ao salvar. EventoFinanceiro é nulo");

			int retorno;
			if(eventoFinanceiro.getCdEventoFinanceiro()==0){
				retorno = EventoFinanceiroDAO.insert(eventoFinanceiro, connect);
				eventoFinanceiro.setCdEventoFinanceiro(retorno);
			}
			else {
				retorno = EventoFinanceiroDAO.update(eventoFinanceiro, connect);
			}

			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();

			return new Result(retorno, (retorno<=0)?"Erro ao salvar...":"Salvo com sucesso...", "EVENTOFINANCEIRO", eventoFinanceiro);
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
	public static Result remove(int cdEventoFinanceiro){
		return remove(cdEventoFinanceiro, false, null);
	}
	public static Result remove(int cdEventoFinanceiro, boolean cascade){
		return remove(cdEventoFinanceiro, cascade, null);
	}
	public static Result remove(int cdEventoFinanceiro, boolean cascade, Connection connect){
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
			retorno = EventoFinanceiroDAO.delete(cdEventoFinanceiro, connect);
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
			pstmt = connect.prepareStatement("SELECT * FROM adm_evento_financeiro");
				return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! EventoFinanceiroServices.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! EventoFinanceiroServices.getAll: " + e);
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
		return Search.find("SELECT * FROM adm_evento_financeiro", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
