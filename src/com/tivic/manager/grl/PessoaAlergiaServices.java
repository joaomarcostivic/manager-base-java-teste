package com.tivic.manager.grl;

import java.sql.*;
import java.util.ArrayList;

import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.util.Result;

import com.tivic.sol.connection.Conexao;

public class PessoaAlergiaServices {

	public static Result save(PessoaAlergia pessoaAlergia){
		return save(pessoaAlergia, null);
	}

	public static Result save(PessoaAlergia pessoaAlergia, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			if(pessoaAlergia==null)
				return new Result(-1, "Erro ao salvar. PessoaAlergia é nulo");

			int retorno;
			if(PessoaAlergiaDAO.get(pessoaAlergia.getCdPessoa(), pessoaAlergia.getCdAlergia())==null){
				retorno = PessoaAlergiaDAO.insert(pessoaAlergia, connect);
				pessoaAlergia.setCdPessoa(retorno);
			}
			else {
				retorno = PessoaAlergiaDAO.update(pessoaAlergia, connect);
			}

			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();

			return new Result(retorno, (retorno<=0)?"Erro ao salvar...":"Salvo com sucesso...", "PESSOAALERGIA", pessoaAlergia);
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
	public static Result remove(int cdPessoa, int cdAlergia){
		return remove(cdPessoa, cdAlergia, false, null);
	}
	public static Result remove(int cdPessoa, int cdAlergia, boolean cascade){
		return remove(cdPessoa, cdAlergia, cascade, null);
	}
	public static Result remove(int cdPessoa, int cdAlergia, boolean cascade, Connection connect){
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
			retorno = PessoaAlergiaDAO.delete(cdPessoa, cdAlergia, connect);
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
			pstmt = connect.prepareStatement("SELECT * FROM grl_pessoa_alergia A, grl_alergia B WHERE A.cd_alergia = B.cd_alergia");
				return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! PessoaAlergiaServices.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! PessoaAlergiaServices.getAll: " + e);
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
		return Search.find("SELECT * FROM grl_pessoa_alergia A, grl_pessoa B, grl_alergia C WHERE A.cd_pessoa = B.cd_pessoa AND A.cd_alergia = C.cd_alergia",  "ORDER BY C.NM_ALERGIA", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}
	
	public static ResultSetMap getAlergiaByPessoa(int cdPessoa) {
		return getAlergiaByPessoa(cdPessoa, null);
	}

	public static ResultSetMap getAlergiaByPessoa(int cdPessoa, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM grl_pessoa_alergia A, grl_alergia B WHERE A.cd_alergia = B.cd_alergia AND A.cd_pessoa = ?");
			pstmt.setInt(1, cdPessoa);
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! PessoaAlergiaServices.getAlergiaByPessoa: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! PessoaAlergiaServices.getAlergiaByPessoa: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

}
