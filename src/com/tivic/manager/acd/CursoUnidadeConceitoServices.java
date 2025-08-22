package com.tivic.manager.acd;

import java.sql.*;
import java.sql.PreparedStatement;
import java.util.ArrayList;

import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.util.Result;

import com.tivic.sol.connection.Conexao;
import com.tivic.manager.seg.AuthData;

public class CursoUnidadeConceitoServices {

	public static Result save(CursoUnidadeConceito cursoUnidadeConceito){
		return save(cursoUnidadeConceito, null, null);
	}

	public static Result save(CursoUnidadeConceito cursoUnidadeConceito, AuthData authData){
		return save(cursoUnidadeConceito, authData, null);
	}

	public static Result save(CursoUnidadeConceito cursoUnidadeConceito, AuthData authData, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			if(cursoUnidadeConceito==null)
				return new Result(-1, "Erro ao salvar. CursoUnidadeConceito é nulo");
			
			int retorno;
			
			CursoUnidadeConceito c = CursoUnidadeConceitoDAO.get(cursoUnidadeConceito.getCdUnidade(), cursoUnidadeConceito.getCdCurso(), cursoUnidadeConceito.getCdMatriculaDisciplina(), connect);

			if(c==null){
				retorno = CursoUnidadeConceitoDAO.insert(cursoUnidadeConceito, connect);
				cursoUnidadeConceito.setCdUnidade(retorno);
			}
			else {
				retorno = CursoUnidadeConceitoDAO.update(cursoUnidadeConceito, connect);
			}

			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();

			return new Result(retorno, (retorno<=0)?"Erro ao salvar...":"Salvo com sucesso...", "CURSOUNIDADECONCEITO", cursoUnidadeConceito);
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
	public static Result remove(int cdUnidade, int cdCurso, int cdMatriculaDisciplina){
		return remove(cdUnidade, cdCurso, cdMatriculaDisciplina, false, null);
	}
	public static Result remove(int cdUnidade, int cdCurso, int cdMatriculaDisciplina, boolean cascade){
		return remove(cdUnidade, cdCurso, cdMatriculaDisciplina, cascade, null);
	}
	public static Result remove(int cdUnidade, int cdCurso, int cdMatriculaDisciplina, boolean cascade, Connection connect){
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
			retorno = CursoUnidadeConceitoDAO.delete(cdUnidade, cdCurso, cdMatriculaDisciplina, connect);
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
			pstmt = connect.prepareStatement("SELECT * FROM acd_curso_unidade_conceito");
				return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! CursoUnidadeConceitoServices.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! CursoUnidadeConceitoServices.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	/**
	 * Método que busca todos os alunos da disciplina/turma com seus conceitos
	 * @param cdTurma
	 * @param cdDisciplina
	 * @return rsmAlunos
	 */
	public static ResultSetMap getByTurmaDisciplina(int cdTurma, int cdDisciplina) {
		return getByTurmaDisciplina(cdTurma, cdDisciplina, null);
	}

	public static ResultSetMap getByTurmaDisciplina(int cdTurma, int cdDisciplina, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			
			//Busca dos alunos
			ResultSetMap rsmAlunos = TurmaServices.getAlunosByTurmaDisciplina(cdTurma, cdDisciplina, connect);
			
			while(rsmAlunos.next()){
				// Sql para buscar os conceitos de cada aluno
				pstmt = connect.prepareStatement("SELECT A.* FROM acd_curso_unidade_conceito A" +
												" JOIN acd_curso_unidade B ON (A.cd_unidade = B.cd_unidade AND B.cd_curso = " + rsmAlunos.getInt("CD_CURSO") + ")" +
												" WHERE A.cd_curso = " + rsmAlunos.getInt("CD_CURSO") +
												" AND A.cd_matricula_disciplina = " + rsmAlunos.getInt("CD_MATRICULA_DISCIPLINA"));
				
				ResultSetMap rsmConceitos = new ResultSetMap(pstmt.executeQuery());
				
				while (rsmConceitos.next()) {
					String nmConceito = new String("VL_NOTA_" + rsmConceitos.getInt("CD_UNIDADE"));
					rsmAlunos.setValueToField(nmConceito, rsmConceitos.getDouble("VL_CONCEITO"));
				}
			}
			
			rsmAlunos.beforeFirst();
			return rsmAlunos;
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! CursoUnidadeConceitoServices.getByTurmaDisciplina: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! CursoUnidadeConceitoServices.getByTurmaDisciplina: " + e);
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
		return Search.find("SELECT * FROM acd_curso_unidade_conceito", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}