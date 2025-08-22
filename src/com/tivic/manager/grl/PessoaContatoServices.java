package com.tivic.manager.grl;

import java.sql.*;
import java.sql.PreparedStatement;
import java.util.ArrayList;

import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.util.Result;

import com.tivic.sol.connection.Conexao;

public class PessoaContatoServices {

	/*Tipos de Telefone*/
	public static final int TP_TELEFONE_RESIDENCIAL = 0;
	public static final int TP_TELEFONE_COMERCIAL 	= 1;	
	public static String[] tpTelefones = {"Residencial", "Comercial"};
	
	/*Tipo de Celular - Operadora*/
	public static final int TP_CELULAR_OPERADORA_OI 	= 0;
	public static final int TP_CELULAR_OPERADORA_VIVO 	= 1;
	public static final int TP_CELULAR_OPERADORA_TIM 	= 2;
	public static final int TP_CELULAR_OPERADORA_CLARO 	= 3;
	public static String[] tpCelulares = {"Oi", "Tim", "Vivo", "Claro"};
	
	public static Result save(PessoaContato pessoaContato){
		return save(pessoaContato, null);
	}

	public static Result save(PessoaContato pessoaContato, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			if(pessoaContato==null)
				return new Result(-1, "Erro ao salvar. PessoaContato é nulo");

			int retorno;
			if(pessoaContato.getCdPessoaContato()==0){
				retorno = PessoaContatoDAO.insert(pessoaContato, connect);
				pessoaContato.setCdPessoaContato(retorno);
			}
			else {
				retorno = PessoaContatoDAO.update(pessoaContato, connect);
			}

			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();

			return new Result(retorno, (retorno<=0)?"Erro ao salvar...":"Salvo com sucesso...", "PESSOACONTATO", pessoaContato);
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
	public static Result remove(int cdPessoaContato, int cdPessoa){
		return remove(cdPessoaContato, cdPessoa, false, null);
	}
	public static Result remove(int cdPessoaContato, int cdPessoa, boolean cascade){
		return remove(cdPessoaContato, cdPessoa, cascade, null);
	}
	public static Result remove(int cdPessoaContato, int cdPessoa, boolean cascade, Connection connect){
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
			retorno = PessoaContatoDAO.delete(cdPessoaContato, cdPessoa, connect);
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
			pstmt = connect.prepareStatement("SELECT * FROM grl_pessoa_contato");
				return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! PessoaContatoServices.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! PessoaContatoServices.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static ResultSetMap getAllByPessoa(int cdPessoa) {
		return getAllByPessoa(cdPessoa, null);
	}

	public static ResultSetMap getAllByPessoa(int cdPessoa, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM grl_pessoa_contato WHERE cd_pessoa = ?");
			pstmt.setInt(1, cdPessoa);
			
				return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! PessoaContatoServices.getAllByPessoa: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! PessoaContatoServices.getAllByPessoa: " + e);
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
		return Search.find("SELECT A.*, B.nm_funcao " +
				"FROM grl_pessoa_contato A " +
				"LEFT OUTER JOIN srh_funcao B ON (A.cd_funcao = B.cd_funcao) ", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
