package com.tivic.manager.acd;

import java.sql.*;
import java.sql.PreparedStatement;
import java.util.ArrayList;

import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.util.Result;

import com.tivic.sol.connection.Conexao;
import com.tivic.manager.grl.PessoaJuridica;
import com.tivic.manager.grl.PessoaJuridicaDAO;
import com.tivic.manager.grl.PessoaJuridicaServices;
import com.tivic.manager.util.Util;

public class UnidadeExecutoraServices {
	

	public static final int TP_UNIDADE_EXECUTORA     = 0; //Própria entidade administra o recurso
	public static final int TP_ENTIDADE_EXECUTORA    = 1; //Recurso administrado por terceiro, ex. Prefeitura, Secretaria, etc.
	public static final int TP_ENTIDADE_MANTENEDORA  = 2; //Recurso gerido por entidade privada
	
	public static final String[] tipoUnidadeExecutora = {"UEx","EEx","EM"};
	public static final String[] tipoUnidadeExecutoraExtenso = {"Unidade Executora", "Entidade Executora", "Entidade Mantenedora"};

	public static Result save(UnidadeExecutora unidadeExecutora, PessoaJuridica pessoaJuridica){
		return save(unidadeExecutora, pessoaJuridica, null);
	}

	public static Result save(UnidadeExecutora unidadeExecutora, PessoaJuridica pessoaJuridica, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
				
			if(unidadeExecutora==null || pessoaJuridica == null )
				return new Result(-1, "Erro ao salvar. UnidadeExecutora é nulo");
			Result resultPessoa;
			int retorno;
			if( unidadeExecutora.getCdUnidadeExecutora() == 0 ){
				
				resultPessoa = PessoaJuridicaServices.save(pessoaJuridica, null/*pessoaEndereco*/, connect);
				if( resultPessoa.getCode() <= 0 ){
					if(isConnectionNull)
						Conexao.rollback(connect);
					System.err.println("Erro! UnidadeExecutoraServices.save: " + resultPessoa.getMessage());
					return new Result(-1, "Erro ao salvar Unidade Executora");
				}
				unidadeExecutora.setCdUnidadeExecutora( resultPessoa.getCode() );
				retorno = UnidadeExecutoraDAO.insert(unidadeExecutora, connect);
			}else {
				int retornoPessoa = PessoaJuridicaDAO.update(pessoaJuridica, connect);
				if(retornoPessoa <= 0){
					if(isConnectionNull)
						Conexao.rollback(connect);
					System.err.println("Erro! UnidadeExecutoraServices.save: Atualizar pessoa jurídica. " );
					return new Result(-1, "Erro ao atualizar Unidade Executora");
				}
				retorno = UnidadeExecutoraDAO.update(unidadeExecutora, connect);
			}

			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();

			return new Result(unidadeExecutora.getCdUnidadeExecutora(), (retorno<=0)?"Erro ao salvar...":"Salvo com sucesso...", "UNIDADEEXECUTORA", unidadeExecutora);
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
	public static Result remove(int cdUnidadeExecutora){
		return remove(cdUnidadeExecutora, false, null);
	}
	public static Result remove(int cdUnidadeExecutora, boolean cascade){
		return remove(cdUnidadeExecutora, cascade, null);
	}
	public static Result remove(int cdUnidadeExecutora, boolean cascade, Connection connect){
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
			retorno = UnidadeExecutoraDAO.delete(cdUnidadeExecutora, connect);
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
			pstmt = connect.prepareStatement("SELECT * FROM acd_unidade_executora A "+
					 					" JOIN GRL_PESSOA B ON ( A.CD_UNIDADE_EXECUTORA = B.CD_PESSOA )  ");
				return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! UnidadeExecutoraServices.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! UnidadeExecutoraServices.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	public static ResultSetMap getAllInstituicoes( int cdUnidadeExecutora ) {
		return getAllInstituicoes(cdUnidadeExecutora, null);
	}
	
	public static ResultSetMap getAllInstituicoes(int cdUnidadeExecutora, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement(" SELECT * FROM acd_unidade_executora_instituicao A "+
											 " JOIN acd_instituicao  B ON ( A.CD_INSTITUICAO = B.CD_INSTITUICAO )"+
											 " JOIN grl_pessoa 		 C ON(  B.CD_INSTITUICAO = C.CD_PESSOA )"+
											 " WHERE A.CD_UNIDADE_EXECUTORA =  "+cdUnidadeExecutora);
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! UnidadeExecutoraServices.getAllInstituicoes: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! UnidadeExecutoraServices.getAll: " + e);
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
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) 
				connect = Conexao.conectar();
				
			return Search.find("  SELECT A.*, B.*, "+
					" C2.nm_pessoa as nm_dirigente, C.nr_cpf, C2.nr_telefone1, C2.nr_telefone2 "+
					" FROM ACD_UNIDADE_EXECUTORA A "+
					" JOIN GRL_PESSOA_JURIDICA   B ON  ( A.cd_unidade_executora = B.cd_pessoa) "+
					" LEFT JOIN GRL_PESSOA_FISICA     C ON  ( A.cd_dirigente = C.cd_pessoa)         "+
					" LEFT JOIN GRL_PESSOA            C2 ON ( C.cd_pessoa = C2.cd_pessoa)           ",
					"",
					criterios, connect, connect==null);
		}catch(Exception e){
			e.printStackTrace();
			if (isConnectionNull)
				Conexao.rollback(connect);
			return null;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

}
