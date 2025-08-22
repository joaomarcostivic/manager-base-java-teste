package com.tivic.manager.acd;

import java.sql.*;
import java.util.ArrayList;

import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.util.Result;

import com.tivic.sol.connection.Conexao;

public class PessoaOfertaServices {

	public static final int ST_ATIVO   = 0;
	public static final int ST_INATIVO = 1;
	
	public static Result save(PessoaOferta pessoaOferta){
		return save(pessoaOferta, null);
	}

	public static Result save(PessoaOferta pessoaOferta, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			if(pessoaOferta==null)
				return new Result(-1, "Erro ao salvar. PessoaOferta é nulo");

			int retorno;
			if(PessoaOfertaDAO.get(pessoaOferta.getCdPessoa(), pessoaOferta.getCdOferta(), pessoaOferta.getCdFuncao())==null){
				retorno = PessoaOfertaDAO.insert(pessoaOferta, connect);
				pessoaOferta.setCdPessoa(retorno);
			}
			else {
				retorno = PessoaOfertaDAO.update(pessoaOferta, connect);
			}

			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();

			return new Result(retorno, (retorno<=0)?"Erro ao salvar...":"Salvo com sucesso...", "PESSOAOFERTA", pessoaOferta);
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
	public static Result remove(int cdPessoa, int cdOferta, int cdFuncao){
		return remove(cdPessoa, cdOferta, cdFuncao, false, null);
	}
	public static Result remove(int cdPessoa, int cdOferta, int cdFuncao, boolean cascade){
		return remove(cdPessoa, cdOferta, cdFuncao, cascade, null);
	}
	public static Result remove(int cdPessoa, int cdOferta, int cdFuncao, boolean cascade, Connection connect){
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
			retorno = PessoaOfertaDAO.delete(cdPessoa, cdOferta, cdFuncao, connect);
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
			pstmt = connect.prepareStatement("SELECT * FROM acd_pessoa_oferta");
				return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! PessoaOfertaServices.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! PessoaOfertaServices.getAll: " + e);
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
		return Search.find("SELECT * FROM acd_pessoa_oferta A, grl_pessoa B WHERE A.cd_pessoa = B.cd_pessoa", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

	/**
	 * Método para retornar as funçoes por oferta
	 * @return horarios por instituicao
	 */
	public static ResultSetMap getAllByOferta(int cdOferta) {
		return getAllByOferta(cdOferta, null);
	}

	public static ResultSetMap getAllByOferta(int cdOferta, Connection connect) {
		boolean isConnectionNull = connect == null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM acd_pessoa_oferta" + 
											" WHERE cd_oferta = " + cdOferta +
											"   AND st_pessoa_oferta = " + ST_ATIVO);
				return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! PessoaOfertaServices.getAllByOferta: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! PessoaOfertaServices.getAllByOferta: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	/**
	* Método para retornar as funçoes por oferta
	* @return horarios por instituicao
	*/
	public static ResultSetMap getAllByPessoa(int cdPessoa) {
		return getAllByOferta(cdPessoa, null);
	}

	public static ResultSetMap getAllByPessoa(int cdPessoa, Connection connect) {
		boolean isConnectionNull = connect == null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM acd_pessoa_oferta" + 
											 " WHERE cd_pessoa = " + cdPessoa);
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! PessoaOfertaServices.getAllByPessoa: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! PessoaOfertaServices.getAllByPessoa: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
}
