package com.tivic.manager.acd;

import java.sql.*;
import java.util.ArrayList;

import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.util.Result;

import com.tivic.sol.connection.Conexao;
import com.tivic.manager.grl.ParametroServices;

public class AlunoRecursoProvaServices {

	public static Result save(AlunoRecursoProva alunoRecursoProva){
		return save(alunoRecursoProva, null);
	}

	public static Result save(AlunoRecursoProva alunoRecursoProva, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			if(alunoRecursoProva==null)
				return new Result(-1, "Erro ao salvar. AlunoRecursoProva é nulo");

			int retorno;
			if(AlunoRecursoProvaDAO.get(alunoRecursoProva.getCdAluno(), alunoRecursoProva.getCdTipoRecursoProva())==null){
				retorno = AlunoRecursoProvaDAO.insert(alunoRecursoProva, connect);
				alunoRecursoProva.setCdAluno(retorno);
			}
			else {
				retorno = AlunoRecursoProvaDAO.update(alunoRecursoProva, connect);
			}

			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();

			return new Result(retorno, (retorno<=0)?"Erro ao salvar...":"Salvo com sucesso...", "ALUNORECURSOPROVA", alunoRecursoProva);
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
	public static Result remove(int cdAluno, int cdTipoRecursoProva){
		return remove(cdAluno, cdTipoRecursoProva, false, null);
	}
	public static Result remove(int cdAluno, int cdTipoRecursoProva, boolean cascade){
		return remove(cdAluno, cdTipoRecursoProva, cascade, null);
	}
	public static Result remove(int cdAluno, int cdTipoRecursoProva, boolean cascade, Connection connect){
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
			retorno = AlunoRecursoProvaDAO.delete(cdAluno, cdTipoRecursoProva, connect);
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
	
	public static ResultSetMap getRecursoProvaByAluno(int cdAluno) {
		return getRecursoProvaByAluno(cdAluno, null);
	}
	
	public static ResultSetMap getRecursoProvaByAluno(int cdAluno, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM acd_aluno_recurso_prova A, acd_tipo_recurso_prova B "
					+ " WHERE A.cd_tipo_recurso_prova = B.cd_tipo_recurso_prova "
					+ "   AND A.cd_aluno = ? ");
			pstmt.setInt(1, cdAluno);
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AlunoRecursoProvaServices.getRecursoProvaByAluno: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! AlunoRecursoProvaServices.getRecursoProvaByAluno: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM acd_aluno_recurso_prova A, acd_tipo_recurso_prova B WHERE A.cd_tipo_recurso_prova = B.cd_tipo_recurso_prova");
				return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AlunoRecursoProvaServices.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! AlunoRecursoProvaServices.getAll: " + e);
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
		return Search.find("SELECT * FROM acd_aluno_recurso_prova A, acd_tipo_recurso_prova B WHERE A.cd_tipo_recurso_prova = B.cd_tipo_recurso_prova", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
