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

public class GrupoPessoaServices {

	public static Result save(GrupoPessoa grupoPessoa){
		return save(grupoPessoa, null, null);
	}

	public static Result save(GrupoPessoa grupoPessoa, AuthData authData){
		return save(grupoPessoa, authData, null);
	}

	public static Result save(GrupoPessoa grupoPessoa, AuthData authData, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			if(grupoPessoa==null)
				return new Result(-1, "Erro ao salvar. GrupoPessoa é nulo");

			int retorno;
			if(GrupoPessoaDAO.get(grupoPessoa.getCdPessoa(), grupoPessoa.getCdGrupo(), connect)==null){
				retorno = GrupoPessoaDAO.insert(grupoPessoa, connect);
				grupoPessoa.setCdPessoa(retorno);
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
	public static Result remove(GrupoPessoa grupoPessoa) {
		return remove(grupoPessoa.getCdPessoa(), grupoPessoa.getCdGrupo());
	}
	public static Result remove(int cdPessoa, int cdGrupo){
		return remove(cdPessoa, cdGrupo, false, null, null);
	}
	public static Result remove(int cdPessoa, int cdGrupo, boolean cascade){
		return remove(cdPessoa, cdGrupo, cascade, null, null);
	}
	public static Result remove(int cdPessoa, int cdGrupo, boolean cascade, AuthData authData){
		return remove(cdPessoa, cdGrupo, cascade, authData, null);
	}
	public static Result remove(int cdPessoa, int cdGrupo, boolean cascade, AuthData authData, Connection connect){
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
			pstmt = connect.prepareStatement("SELECT * FROM grl_grupo_pessoa");
				return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! GrupoPessoaServices.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! GrupoPessoaServices.getAll: " + e);
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
		return Search.find(
				  " SELECT A.*,"
			    + " B.nm_grupo,"
			    + " C.nm_pessoa, C.gn_pessoa, C.st_cadastro,"
			    + " E.nm_agente"
				+ " FROM grl_grupo_pessoa 		A"
				+ " JOIN grl_grupo 			    B ON (A.cd_grupo = B.cd_grupo)"
				+ " LEFT OUTER JOIN grl_pessoa  C ON (A.cd_pessoa = C.cd_pessoa)"
				+ " LEFT OUTER JOIN seg_usuario D ON (A.cd_pessoa = D.cd_pessoa)"
				+ " LEFT OUTER JOIN str_agente 	E ON (D.cd_usuario = E.cd_usuario)", 
				criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
