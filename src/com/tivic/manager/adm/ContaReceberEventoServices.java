package com.tivic.manager.adm;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;

import com.tivic.sol.connection.Conexao;

import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;
import sol.dao.Search;
import sol.util.Result;

public class ContaReceberEventoServices {

	public static final int ST_INATIVO = 0;
	public static final int ST_ATIVO = 1;

	public static final String[] situacaoEvento = new String[] {"Inativo",
		"Ativo"};

	public static Result save(ContaReceberEvento contaReceberEvento){
		return save(contaReceberEvento, null);
	}

	public static Result save(ContaReceberEvento contaReceberEvento, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			if(contaReceberEvento==null)
				return new Result(-1, "Erro ao salvar. ContaReceberEvento é nulo");

			int retorno;
			if(contaReceberEvento.getCdContaReceber()==0){
				retorno = ContaReceberEventoDAO.insert(contaReceberEvento, connect);
				contaReceberEvento.setCdContaReceber(retorno);
			}
			else {
				retorno = ContaReceberEventoDAO.update(contaReceberEvento, connect);
			}

			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();

			return new Result(retorno, (retorno<=0)?"Erro ao salvar...":"Salvo com sucesso...", "CONTARECEBEREVENTO", contaReceberEvento);
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
	public static Result remove(int cdContaReceber, int cdEventoFinanceiro, int cdContaReceberEvento){
		return remove(cdContaReceber, cdEventoFinanceiro, cdContaReceberEvento, false, null);
	}
	
	public static Result remove(int cdContaReceber, int cdEventoFinanceiro, int cdContaReceberEvento, boolean cascade){
		return remove(cdContaReceber, cdEventoFinanceiro, cdContaReceberEvento, cascade, null);
	}
	
	public static Result remove(int cdContaReceber, int cdEventoFinanceiro,int cdContaReceberEvento, boolean cascade, Connection connect){
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
			retorno = ContaReceberEventoDAO.delete(cdContaReceber, cdEventoFinanceiro, cdContaReceberEvento, connect);
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
	
	public static ResultSetMap find(ArrayList<ItemComparator> criterios) {
		return find(criterios, null);
	}

	public static ResultSetMap find(ArrayList<ItemComparator> criterios, Connection connect) {
		return Search.find("SELECT A.*, B.id_evento_financeiro, B.nm_evento_financeiro, nm_pessoa " +
				           "FROM adm_conta_receber_evento A " +
				           "JOIN adm_evento_financeiro B ON (A.cd_evento_financeiro = B.cd_evento_financeiro) " +
				           "LEFT OUTER JOIN grl_pessoa C ON (A.cd_pessoa = C.cd_pessoa) ", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

	public static int deleteAll(int cdContaReceber) {
		return deleteAll(cdContaReceber, null);
	}

	public static int deleteAll(int cdContaReceber, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM adm_conta_receber_evento WHERE cd_conta_receber=?");
			pstmt.setInt(1, cdContaReceber);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ContaReceberEventoServices.deleteAll: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ContaReceberEventoServices.deleteAll: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
}