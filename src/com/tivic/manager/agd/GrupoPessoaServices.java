package com.tivic.manager.agd;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.GregorianCalendar;

import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;
import sol.util.Result;

import com.tivic.sol.connection.Conexao;

public class GrupoPessoaServices {

	public static final int ST_INATIVO = 0;
	public static final int ST_ATIVO = 1;
	
	public static Result save(GrupoPessoa grupoPessoa){
		return save(grupoPessoa, null);
	}
	
	public static Result save(GrupoPessoa grupoPessoa, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			if(grupoPessoa==null)
				return new Result(-1, "Erro ao salvar. Dados enviados são nulos");
			
			int retorno;
			
			if(GrupoPessoaDAO.get(grupoPessoa.getCdPessoa(), grupoPessoa.getCdGrupo(), connect) == null){
				grupoPessoa.setDtInclusao(new GregorianCalendar());
				retorno = GrupoPessoaDAO.insert(grupoPessoa, connect);
			}
			else {
				retorno = GrupoPessoaDAO.update(grupoPessoa, connect);
			}
			
			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();
			
			return new Result(retorno, (retorno<=0)?"Erro ao salvar...":"Salvo com sucesso...", "GRUPOPESSOA", grupoPessoa);
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
	
	public static Result remove(int cdPessoa, int cdGrupo){
		return remove(cdPessoa, cdGrupo, false, null);
	}
	
	public static Result remove(int cdPessoa, int cdGrupo, boolean cascade){
		return remove(cdPessoa, cdGrupo, cascade, null);
	}
	
	public static Result remove(int cdPessoa, int cdGrupo, boolean cascade, Connection connect){
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
				retorno = GrupoPessoaDAO.delete(cdPessoa, cdGrupo, connect);
			
			if(retorno<=0){
				Conexao.rollback(connect);
				return new Result(-2, "Este registro está vinculado a outros registros e não pode ser excluído!");
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

	public static ResultSetMap getAllByGrupo(int cdGrupo) {
		return getAllByGrupo(cdGrupo, null);
	}

	public static ResultSetMap getAllByGrupo(int cdGrupo, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement("SELECT A.*, B.nm_pessoa FROM agd_grupo_pessoa A " +
					"LEFT OUTER JOIN grl_pessoa B ON (B.cd_pessoa = A.cd_pessoa) " +
					"WHERE A.cd_grupo = ? ORDER BY B.nm_pessoa");
			pstmt.setInt(1, cdGrupo);
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static ResultSetMap getAllAtivosByGrupo(int cdGrupo) {
		return getAllAtivosByGrupo(cdGrupo, null);
	}

	public static ResultSetMap getAllAtivosByGrupo(int cdGrupo, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement("SELECT A.*, B.nm_pessoa FROM agd_grupo_pessoa A " +
					"LEFT OUTER JOIN grl_pessoa B ON (B.cd_pessoa = A.cd_pessoa) " +
					"WHERE A.cd_grupo = ? AND B.st_cadastro=1 "+ 
					"ORDER BY B.nm_pessoa");
			pstmt.setInt(1, cdGrupo);
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static ResultSetMap getGrupos(int cdPessoa) {
		return getGrupos(cdPessoa, null);
	}

	public static ResultSetMap getGrupos(int cdPessoa, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement(
					  " SELECT A.*, "
					+ " B.nm_grupo "
					+ " FROM agd_grupo_pessoa A " 
					+ "LEFT OUTER JOIN agd_grupo B ON (A.cd_grupo = B.cd_grupo) " 
					+ "WHERE A.cd_pessoa = ? "
					+ "ORDER BY B.nm_grupo");
			pstmt.setInt(1, cdPessoa);
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
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
		return GrupoPessoaDAO.find(criterios, connect);
	}
}
