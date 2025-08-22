package com.tivic.manager.grl;

import java.sql.*;
import java.util.ArrayList;

import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.util.Result;

import com.tivic.manager.adm.Cliente;
import com.tivic.manager.adm.ClienteDAO;
import com.tivic.sol.connection.Conexao;
import com.tivic.manager.util.ResultSetMapper;

public class PessoaEmpresaServices {
	
	/* situação de vinculos */
	public static final int ST_INATIVO = 0;
	public static final int ST_ATIVO = 1;
	
	public static String[] situacaoVinculo = {"Inativo", "Ativo"};
	
	public static Result save(PessoaEmpresa pessoaEmpresa){
		return save(pessoaEmpresa, 0, 0, 0, null);
	}

	public static Result save(PessoaEmpresa pessoaEmpresa, Connection connect){
		return save(pessoaEmpresa, 0, 0, 0, connect);
	}

	public static Result save(PessoaEmpresa pessoaEmpresa, int cdEmpresa, int cdPessoa, int cdVinculo){
		return save(pessoaEmpresa, cdEmpresa, cdPessoa, cdVinculo, null);
	}

	public static Result save(PessoaEmpresa pessoaEmpresa, int cdEmpresaOld, int cdPessoaOld, int cdVinculoOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			if(pessoaEmpresa==null)
				return new Result(-1, "Erro ao salvar. PessoaEmpresa é nulo");

			int retorno;
			if(PessoaEmpresaDAO.get(pessoaEmpresa.getCdEmpresa(), pessoaEmpresa.getCdPessoa(),connect) == null){
				retorno = PessoaEmpresaDAO.insert(pessoaEmpresa, connect);
			}
			else {
				retorno = PessoaEmpresaDAO.update(pessoaEmpresa, cdEmpresaOld, cdPessoaOld, cdVinculoOld, connect);
			}

			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();

			return new Result(retorno, (retorno<=0)?"Erro ao salvar...":"Salvo com sucesso...", "PESSOAEMPRESA", pessoaEmpresa);
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
	public static Result remove(int cdEmpresa, int cdPessoa, int cdVinculo){
		return remove(cdEmpresa, cdPessoa, cdVinculo, false, null);
	}
	public static Result remove(int cdEmpresa, int cdPessoa, int cdVinculo, boolean cascade){
		return remove(cdEmpresa, cdPessoa, cdVinculo, cascade, null);
	}
	public static Result remove(int cdEmpresa, int cdPessoa, int cdVinculo, boolean cascade, Connection connect){
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
			retorno = PessoaEmpresaDAO.delete(cdEmpresa, cdPessoa, cdVinculo, connect);
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
			pstmt = connect.prepareStatement("SELECT * FROM grl_pessoa_empresa");
				return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! PessoaEmpresaServices.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! PessoaEmpresaServices.getAll: " + e);
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
			pstmt = connect.prepareStatement(" SELECT *, B2.nm_pessoa AS nm_fantasia FROM grl_pessoa_empresa A "+
										     " JOIN grl_empresa B ON ( A.cd_empresa = B.cd_empresa ) "+
										 	 " JOIN grl_pessoa  B2 ON (B.cd_empresa = B2.cd_pessoa) " +
										 	 " WHERE A.cd_pessoa = "+cdPessoa);
				return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! PessoaEmpresaServices.getAllByPessoa: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! PessoaEmpresaServices.getAllByPessoa: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static PessoaEmpresa getPessoaEmpresaByPessoa(int cdPessoa) {
		return getPessoaEmpresaByPessoa(cdPessoa, null);
	}

	public static PessoaEmpresa getPessoaEmpresaByPessoa(int cdPessoa, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM GRL_PESSOA_EMPRESA WHERE CD_PESSOA = ?");
			pstmt.setInt(1, cdPessoa);
			
			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
			
			if(rsm.next()) {
				ResultSetMapper<PessoaEmpresa> _conv = new ResultSetMapper<PessoaEmpresa>(rsm, PessoaEmpresa.class);				
				return _conv.toList().get(0);
			}
			return null;
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! PessoaEmpresaServices.getAllByPessoa: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! PessoaEmpresaServices.getAllByPessoa: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static ResultSetMap getAllByVinculo(int cdVinculo) {
		return getAllByVinculo(cdVinculo, null);
	}

	public static ResultSetMap getAllByVinculo(int cdVinculo, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement(" SELECT * FROM grl_pessoa_empresa A "+
										 	 " JOIN grl_pessoa P ON (A.cd_pessoa = P.cd_pessoa) " +
										 	 " WHERE A.cd_vinculo = " + cdVinculo);
				return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! PessoaEmpresaServices.getAllByVinculo: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! PessoaEmpresaServices.getAllByVinculo: " + e);
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
		return Search.find("SELECT * FROM grl_pessoa_empresa", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}