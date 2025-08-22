package com.tivic.manager.grl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;

import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;
import sol.dao.Search;
import sol.util.Result;

import com.tivic.sol.connection.Conexao;
import com.tivic.manager.util.LogUtils;

public class PessoaContaBancariaServices {

	public static String[] situacaoConta = {"Inativa", "Ativa"};

	public static Result save(PessoaContaBancaria pessoaContaBancaria){
		return save(pessoaContaBancaria, null);
	}

	public static Result save(PessoaContaBancaria pessoaContaBancaria, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			if(pessoaContaBancaria==null)
				return new Result(-1, "Erro ao salvar. PessoaContaBancaria é nulo");

			int retorno;
			
			/**
			 * Caso a conta bancária enviada esteja configurada como principal,
			 * retira a flag de outra conta bancária, que por ventura esteja configurada,
			 * mantendo, desta forma, apenas uma conta principal.
			 */
			LogUtils.debug(">>> HERE <<<" + pessoaContaBancaria.toString());
			if( pessoaContaBancaria.getCdPessoa() > 0 && pessoaContaBancaria.getLgPrincipal()>0){
				LogUtils.debug(">>> HERE <<<");
				connect.prepareStatement(" UPDATE GRL_PESSOA_CONTA_BANCARIA SET LG_PRINCIPAL = 0 "+
										" WHERE CD_PESSOA = "+pessoaContaBancaria.getCdPessoa()
						).executeUpdate();
			}
			
			PessoaContaBancaria contaTmp = PessoaContaBancariaDAO.get(pessoaContaBancaria.getCdContaBancaria(), pessoaContaBancaria.getCdPessoa());
			if(contaTmp==null){
				retorno = PessoaContaBancariaDAO.insert(pessoaContaBancaria, connect);
			}else {
				retorno = PessoaContaBancariaDAO.update(pessoaContaBancaria, connect);
			}

			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();

			return new Result(retorno, (retorno<=0)?"Erro ao salvar...":"Salvo com sucesso...", "PESSOACONTABANCARIA", pessoaContaBancaria);
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
	public static Result remove(int cdContaBancaria, int cdPessoa){
		return remove(cdContaBancaria, cdPessoa, false, null);
	}
	public static Result remove(int cdContaBancaria, int cdPessoa, boolean cascade){
		return remove(cdContaBancaria, cdPessoa, cascade, null);
	}
	public static Result remove(int cdContaBancaria, int cdPessoa, boolean cascade, Connection connect){
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
			retorno = PessoaContaBancariaDAO.delete(cdContaBancaria, cdPessoa, connect);
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
			pstmt = connect.prepareStatement("SELECT * FROM grl_pessoa_conta_bancaria");
				return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! PessoaContaBancariaServices.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! PessoaContaBancariaServices.getAll: " + e);
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
		return Search.find( " SELECT A.*, B.*, C.*, (A.cd_conta_bancaria || '-' || A.cd_pessoa || '-' || A.cd_banco) AS COD_CONTA_BANCARIA, (B.nm_pessoa || '(Conta:' || A.nr_conta || ' - ' || C.nm_banco || ')') AS NM_CONTA_BANCARIA "+
				            " FROM grl_pessoa_conta_bancaria A "+
							" JOIN grl_pessoa B ON ( A.cd_pessoa = B.cd_pessoa )  "+
							" JOIN grl_banco  C ON ( A.cd_banco  = C.cd_banco) " +
							" WHERE 1=1 ",
							criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}
	
	public static ResultSetMap getBancoCliente(int cdPessoa) {
		return getBancoCliente(cdPessoa,null);
	}
	
	public static ResultSetMap getBancoCliente(int cdPessoa,Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement("SELECT PCB.*,B.nm_banco FROM grl_pessoa_conta_bancaria PCB" +
											 " JOIN grl_banco B ON(PCB.cd_banco = B.cd_banco) " +
											 "WHERE PCB.cd_pessoa = " + cdPessoa);
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! PessoaContaBancariaDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! PessoaContaBancariaDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
}