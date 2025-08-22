package com.tivic.manager.grl;

import java.sql.*;
import java.sql.PreparedStatement;
import java.util.ArrayList;

import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.util.Result;

import com.tivic.sol.connection.Conexao;

public class PessoaNecessidadeEspecialServices {

	public static Result save(PessoaNecessidadeEspecial pessoaNecessidadeEspecial){
		return save(pessoaNecessidadeEspecial, null);
	}

	public static Result save(PessoaNecessidadeEspecial pessoaNecessidadeEspecial, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			if(pessoaNecessidadeEspecial==null)
				return new Result(-1, "Erro ao salvar. PessoaNecessidadeEspecial é nulo");

			int retorno;
			if(PessoaNecessidadeEspecialDAO.get(pessoaNecessidadeEspecial.getCdPessoa(), pessoaNecessidadeEspecial.getCdTipoNecessidadeEspecial())==null){
				retorno = PessoaNecessidadeEspecialDAO.insert(pessoaNecessidadeEspecial, connect);
				pessoaNecessidadeEspecial.setCdPessoa(retorno);
			}
			else {
				retorno = PessoaNecessidadeEspecialDAO.update(pessoaNecessidadeEspecial, connect);
			}

			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();

			return new Result(retorno, (retorno<=0)?"Erro ao salvar...":"Salvo com sucesso...", "PESSOANECESSIDADEESPECIAL", pessoaNecessidadeEspecial);
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
	public static Result remove(int cdPessoa, int cdTipoNecessidadeEspecial){
		return remove(cdPessoa, cdTipoNecessidadeEspecial, false, null);
	}
	public static Result remove(int cdPessoa, int cdTipoNecessidadeEspecial, boolean cascade){
		return remove(cdPessoa, cdTipoNecessidadeEspecial, cascade, null);
	}
	public static Result remove(int cdPessoa, int cdTipoNecessidadeEspecial, boolean cascade, Connection connect){
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
			retorno = PessoaNecessidadeEspecialDAO.delete(cdPessoa, cdTipoNecessidadeEspecial, connect);
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
			pstmt = connect.prepareStatement("SELECT * FROM grl_pessoa_necessidade_especial");
				return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! PessoaNecessidadeEspecialServices.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! PessoaNecessidadeEspecialServices.getAll: " + e);
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
		return Search.find("SELECT * FROM grl_pessoa_necessidade_especial A, grl_pessoa B, grl_tipo_necessidade_especial C WHERE A.cd_pessoa = B.cd_pessoa AND A.cd_tipo_necessidade_especial = C.cd_tipo_necessidade_especial", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

	
	public static ResultSetMap getNecessidadeEspecialByPessoa(int cdPessoa) {
		return getNecessidadeEspecialByPessoa(cdPessoa, null);
	}

	public static ResultSetMap getNecessidadeEspecialByPessoa(int cdPessoa, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM grl_pessoa_necessidade_especial A, grl_tipo_necessidade_especial B WHERE A.cd_tipo_necessidade_especial = B.cd_tipo_necessidade_especial AND A.cd_pessoa = ?");
			pstmt.setInt(1, cdPessoa);
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! PessoaNecessidadeEspecialServices.getNecessidadeEspecialByPessoa: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! PessoaNecessidadeEspecialServices.getNecessidadeEspecialByPessoa: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
}
