package com.tivic.manager.acd;

import java.sql.*;
import java.util.ArrayList;

import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.util.Result;

import com.tivic.sol.connection.Conexao;

public class MatriculaCurriculoServices {

	public static Result save(MatriculaCurriculo matriculaCurriculo){
		return save(matriculaCurriculo, null);
	}

	public static Result save(MatriculaCurriculo matriculaCurriculo, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			if(matriculaCurriculo==null)
				return new Result(-1, "Erro ao salvar. MatriculaCurriculo é nulo");

			int retorno;
			if(MatriculaCurriculoDAO.get(matriculaCurriculo.getCdMatricula(), matriculaCurriculo.getCdMatriz(), matriculaCurriculo.getCdCurso(), matriculaCurriculo.getCdCursoModulo(), matriculaCurriculo.getCdDisciplina(), connect) == null){
				retorno = MatriculaCurriculoDAO.insert(matriculaCurriculo, connect);
				matriculaCurriculo.setCdMatricula(retorno);
			}
			else {
				retorno = MatriculaCurriculoDAO.update(matriculaCurriculo, connect);
			}

			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();

			return new Result(retorno, (retorno<=0)?"Erro ao salvar...":"Salvo com sucesso...", "MATRICULACURRICULO", matriculaCurriculo);
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
	
	public static Result remove(int cdMatricula, int cdMatriz, int cdCurso, int cdCursoModulo, int cdDisciplina){
		return remove(cdMatricula, cdMatriz, cdCurso, cdCursoModulo, cdDisciplina, false, null);
	}
	
	public static Result remove(int cdMatricula, int cdMatriz, int cdCurso, int cdCursoModulo, int cdDisciplina, boolean cascade){
		return remove(cdMatricula, cdMatriz, cdCurso, cdCursoModulo, cdDisciplina, cascade, null);
	}
	
	public static Result remove(int cdMatricula, int cdMatriz, int cdCurso, int cdCursoModulo, int cdDisciplina, boolean cascade, Connection connect){
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
			retorno = MatriculaCurriculoDAO.delete(cdMatricula, cdMatriz, cdCurso, cdCursoModulo, cdDisciplina, connect);
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

	public static Result removeByMatricula(int cdMatricula) {
		return removeByMatricula(cdMatricula, null);
	}
	
	public static Result removeByMatricula(int cdMatricula, Connection connect) {
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			int retorno = 0;
			ResultSetMap rsm = getAllByMatricula(cdMatricula, connect);
			while (rsm.next()) {
				if (rsm.getInt("cd_matricula") == cdMatricula) {
					retorno = remove(cdMatricula, rsm.getInt("cd_matriz"), rsm.getInt("cd_curso"), rsm.getInt("cd_curso_modulo"), rsm.getInt("cd_disciplina"), true, connect).getCode();
					if(retorno<=0){
						Conexao.rollback(connect);
						return new Result(-2, "Esta matrícula está vinculada a outros registros e não pode ser excluída!");
					}
				}
			}
			
			if (isConnectionNull)
				connect.commit();
			
			return new Result(1, "Matrícula excluída com sucesso!");
		}
		catch(Exception e){
			e.printStackTrace();
			if (isConnectionNull)
				Conexao.rollback(connect);
			return new Result(-1, "Erro ao excluir matrícula!");
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static MatriculaCurriculo get(MatriculaCurriculo matriculaCurriculo) {
		return get(matriculaCurriculo, null);
	}
	
	public static MatriculaCurriculo get(MatriculaCurriculo matriculaCurriculo, Connection connect) {
		return MatriculaCurriculoDAO.get(matriculaCurriculo.getCdMatricula(), matriculaCurriculo.getCdMatriz(), matriculaCurriculo.getCdCurso(), matriculaCurriculo.getCdCursoModulo(), matriculaCurriculo.getCdDisciplina(), connect);
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
			pstmt = connect.prepareStatement("SELECT * FROM acd_matricula_curriculo");
				return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! MatriculaCurriculoServices.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! MatriculaCurriculoServices.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static ResultSetMap getAllByMatricula(int cdMatricula) {
		return getAllByMatricula(cdMatricula, null);
	}

	public static ResultSetMap getAllByMatricula(int cdMatricula, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM acd_matricula_curriculo WHERE cd_matricula = " + cdMatricula);
				return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! MatriculaCurriculoServices.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! MatriculaCurriculoServices.getAll: " + e);
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
		return Search.find("SELECT * FROM acd_matricula_curriculo", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

	public static ResultSetMap getCurriculosByMatricula(int cdMatricula) {
		return getCurriculosByMatricula(cdMatricula, null);
	}

	public static ResultSetMap getCurriculosByMatricula(int cdMatricula, Connection connection) {
		boolean isConnectionNull = connection==null;
		try {
			if (isConnectionNull)
				connection = Conexao.conectar();

			PreparedStatement pstmt = connection.prepareStatement(
					"SELECT A.* " +
					"FROM acd_matricula_curriculo A " +
					"WHERE A.cd_matricula = " +cdMatricula);
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}
}