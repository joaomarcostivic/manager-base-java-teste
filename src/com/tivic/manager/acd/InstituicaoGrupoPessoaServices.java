package com.tivic.manager.acd;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;
import sol.dao.Search;
import sol.util.Result;

import com.tivic.sol.connection.Conexao;

public class InstituicaoGrupoPessoaServices {
	
	public static Result save(InstituicaoGrupoPessoa pessoa){
		return save(pessoa, null);
	}
	
	public static Result save(InstituicaoGrupoPessoa pessoa, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			if(pessoa==null)
				return new Result(-1, "Erro ao salvar. Pessoa é nula");

			if(pessoa.getDtIngresso().getTimeInMillis() > pessoa.getDtSaida().getTimeInMillis())
				return new Result(-1, "Erro ao Adicionar Pessoa ao Grupo! A data de Ingresso não pode ser posterior a data de Saída");

			int retorno;
			ResultSet rs = connect.prepareStatement("SELECT * FROM acd_instituicao_grupo_pessoa " +
                    "WHERE cd_pessoa = "+pessoa.getCdPessoa()+" AND cd_grupo = "+pessoa.getCdGrupo()+" AND cd_instituicao = "+pessoa.getCdInstituicao()).executeQuery();

			if (rs.next()){
				retorno = InstituicaoGrupoPessoaDAO.update(pessoa, connect);
			}
			else {
				retorno = InstituicaoGrupoPessoaDAO.insert(pessoa, connect);
			}
			
			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();
			
			return new Result(retorno, (retorno<=0)?"Erro ao salvar...":"Salvo com sucesso...", "INSTITUICAOGRUPOPESSOA", pessoa);
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
	
	public static Result remove(int cdPessoa, int cdGrupo, int cdInstituicao){
		return remove(cdPessoa, cdGrupo, cdInstituicao, false, null);
	}
	
	public static Result remove(int cdPessoa, int cdGrupo, int cdInstituicao, boolean cascade){
		return remove(cdPessoa, cdGrupo, cdInstituicao, cascade, null);
	}
	
	public static Result remove(int cdPessoa, int cdGrupo, int cdInstituicao, boolean cascade, Connection connect){
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
				retorno = InstituicaoGrupoPessoaDAO.delete(cdPessoa, cdGrupo, cdInstituicao, connect);
			
			if(retorno<=0){
				Conexao.rollback(connect);
				return new Result(-2, "Esta pessoa está vinculada a outros registros e não pode ser excluído!");
			}
			else if (isConnectionNull)
				connect.commit();
			
			return new Result(1, "Pessoa excluída com sucesso!");
		}
		catch(Exception e){
			e.printStackTrace();
			if (isConnectionNull)
				Conexao.rollback(connect);
			return new Result(-1, "Erro ao excluir pessoa!");
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static ResultSetMap getAllByGrupoInstituicao(int cdGrupo, int cdInstituicao) {
		return getAllByGrupoInstituicao(cdGrupo, cdInstituicao, null);
	}

	public static ResultSetMap getAllByGrupoInstituicao(int cdGrupo, int cdInstituicao, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement("SELECT A.*, B.nm_pessoa FROM acd_instituicao_grupo_pessoa A " +
					"JOIN grl_pessoa B ON (A.cd_pessoa = B.cd_pessoa) " +
					"WHERE A.cd_grupo = ? " +
					"  AND A.cd_instituicao = ? " +
					"ORDER BY nm_pessoa");
			pstmt.setInt(1, cdGrupo);
			pstmt.setInt(2, cdInstituicao);
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! InstituicaoGrupoPessoaServices.getAll: " + e);
			return null;
		}
		finally {
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
			pstmt = connect.prepareStatement("SELECT A.*, B.nm_pessoa FROM acd_instituicao_grupo_pessoa A " +
					"JOIN grl_pessoa B ON (A.cd_pessoa = B.cd_pessoa) " +
					"ORDER BY nm_pessoa");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! InstituicaoGrupoPessoaDAO.getAll: " + e);
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
		return Search.find("SELECT A.*, B.nm_pessoa FROM acd_instituicao_grupo_pessoa A " +
				"JOIN grl_pessoa B ON (A.cd_pessoa = B.cd_pessoa) ", "ORDER BY nm_pessoa", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}
}
