package com.tivic.manager.grl;

import java.sql.*;
import java.sql.PreparedStatement;
import java.util.ArrayList;

import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.util.Result;

import com.tivic.sol.connection.Conexao;
import com.tivic.manager.grl.ParametroServices;
import com.tivic.manager.seg.AuthData;

public class PessoaOcorrenciaServices {

	public static Result save(PessoaOcorrencia pessoaOcorrencia){
		return save(pessoaOcorrencia, null, null);
	}

	public static Result save(PessoaOcorrencia pessoaOcorrencia, AuthData authData){
		return save(pessoaOcorrencia, authData, null);
	}

	public static Result save(PessoaOcorrencia pessoaOcorrencia, AuthData authData, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			if(pessoaOcorrencia==null)
				return new Result(-1, "Erro ao salvar. PessoaOcorrencia é nulo");

			int retorno;
			if(pessoaOcorrencia.getCdOcorrencia()==0){
				retorno = PessoaOcorrenciaDAO.insert(pessoaOcorrencia, connect);
				pessoaOcorrencia.setCdOcorrencia(retorno);
			}
			else {
				retorno = PessoaOcorrenciaDAO.update(pessoaOcorrencia, connect);
			}

			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();

			return new Result(retorno, (retorno<=0)?"Erro ao salvar...":"Salvo com sucesso...", "PESSOAOCORRENCIA", pessoaOcorrencia);
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
	public static Result remove(PessoaOcorrencia pessoaOcorrencia) {
		return remove(pessoaOcorrencia.getCdOcorrencia(), pessoaOcorrencia.getCdPessoa());
	}
	public static Result remove(int cdOcorrencia, int cdPessoa){
		return remove(cdOcorrencia, cdPessoa, false, null, null);
	}
	public static Result remove(int cdOcorrencia, int cdPessoa, boolean cascade){
		return remove(cdOcorrencia, cdPessoa, cascade, null, null);
	}
	public static Result remove(int cdOcorrencia, int cdPessoa, boolean cascade, AuthData authData){
		return remove(cdOcorrencia, cdPessoa, cascade, authData, null);
	}
	public static Result remove(int cdOcorrencia, int cdPessoa, boolean cascade, AuthData authData, Connection connect){
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
			retorno = PessoaOcorrenciaDAO.delete(cdOcorrencia, cdPessoa, connect);
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
			pstmt = connect.prepareStatement("SELECT * FROM grl_pessoa_ocorrencia");
				return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! PessoaOcorrenciaServices.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! PessoaOcorrenciaServices.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static ResultSetMap getAll(int cdPessoa) {
		return getAll(cdPessoa, null);
	}

	public static ResultSetMap getAll(int cdPessoa, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement(
					"SELECT A.*,"
					+ " B.nm_tipo_ocorrencia "
					+ " FROM grl_pessoa_ocorrencia A"
					+ " JOIN grl_tipo_ocorrencia_pessoa B ON (A.cd_tipo_ocorrencia = B.cd_tipo_ocorrencia)"
					+ " WHERE A.cd_pessoa = "+cdPessoa);
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! PessoaOcorrenciaServices.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! PessoaOcorrenciaServices.getAll: " + e);
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
		return Search.find("SELECT * FROM grl_pessoa_ocorrencia", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
