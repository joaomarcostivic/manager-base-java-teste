package com.tivic.manager.adm;

import java.sql.*;
import java.sql.PreparedStatement;
import java.util.ArrayList;

import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.util.Result;

import com.tivic.sol.connection.Conexao;

public class ContaMovimentoOrigemServices {

	public static Result save(ContaMovimentoOrigem contaMovimentoOrigem){
		return save(contaMovimentoOrigem, null);
	}

	public static Result save(ContaMovimentoOrigem contaMovimentoOrigem, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			if(contaMovimentoOrigem==null)
				return new Result(-1, "Erro ao salvar. ContaMovimentoOrigem é nulo");

			int retorno;
			if(contaMovimentoOrigem.getCdContaMovimentoOrigem()==0){
				retorno = ContaMovimentoOrigemDAO.insert(contaMovimentoOrigem, connect);
				contaMovimentoOrigem.setCdContaMovimentoOrigem(retorno);
			}
			else {
				retorno = ContaMovimentoOrigemDAO.update(contaMovimentoOrigem, connect);
			}

			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();

			return new Result(retorno, (retorno<=0)?"Erro ao salvar...":"Salvo com sucesso...", "CONTAMOVIMENTOORIGEM", contaMovimentoOrigem);
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
	public static Result remove(int cdContaMovimentoOrigem){
		return remove(cdContaMovimentoOrigem, false, null);
	}
	public static Result remove(int cdContaMovimentoOrigem, boolean cascade){
		return remove(cdContaMovimentoOrigem, cascade, null);
	}
	public static Result remove(int cdContaMovimentoOrigem, boolean cascade, Connection connect){
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
			retorno = ContaMovimentoOrigemDAO.delete(cdContaMovimentoOrigem, connect);
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
	public static Result removeAll(int cdMovimentoConta, int cdConta ){
		return removeAll(cdMovimentoConta, cdConta, null);
	}
	public static Result removeAll(int cdMovimentoConta, int cdConta, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			int retorno = 1;
			
			ResultSetMap rsmContaMovOrigem = new ResultSetMap( connect.prepareStatement(
									" SELECT * FROM ADM_CONTA_MOVIMENTO_ORIGEM "+
									" WHERE CD_MOVIMENTO_CONTA = "+cdMovimentoConta+  
									" AND CD_CONTA = "+cdConta
							).executeQuery() );
			
			
			if( rsmContaMovOrigem != null ){
				rsmContaMovOrigem.beforeFirst();
				while( rsmContaMovOrigem.next() ){
					Result res = remove(rsmContaMovOrigem.getInt("CD_CONTA_MOVIMENTO_ORIGEM"), true, connect);
					Result resConta = ContaReceberServices.remove( rsmContaMovOrigem.getInt("CD_CONTA_RECEBER"), true, true, connect);
					if( res.getCode() <= 0 || resConta.getCode() <= 0 ){
						if(isConnectionNull)
							Conexao.rollback(connect);
						return new Result(-1, "Erro ao excluir conta associada.");
					}
				}
			}
			
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
			pstmt = connect.prepareStatement("SELECT * FROM adm_conta_movimento_origem");
				return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ContaMovimentoOrigemServices.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ContaMovimentoOrigemServices.getAll: " + e);
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
		return Search.find("SELECT * FROM adm_conta_movimento_origem", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
