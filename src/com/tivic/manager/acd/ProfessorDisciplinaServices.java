package com.tivic.manager.acd;

import java.sql.*;
import java.util.ArrayList;

import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.util.Result;

import com.tivic.sol.connection.Conexao;

public class ProfessorDisciplinaServices {

	public static Result saveDisciplinas(int cdProfessor, ArrayList<Integer> codigosDisciplinas){
		return saveDisciplinas(cdProfessor, codigosDisciplinas, null);
	}

	public static Result saveDisciplinas(int cdProfessor, ArrayList<Integer> codigosDisciplinas, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			for(int cdDisciplina : codigosDisciplinas){
				Result result = save(new ProfessorDisciplina(cdProfessor, cdDisciplina), connect);
				if(result.getCode() < 0){
					if(isConnectionNull)
						Conexao.rollback(connect);
					return result;
				}
			}
			
			
			if (isConnectionNull)
				connect.commit();

			return new Result(1, "Salvo com sucesso...");
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
	
	public static Result save(ProfessorDisciplina professorDisciplina){
		return save(professorDisciplina, null);
	}

	public static Result save(ProfessorDisciplina professorDisciplina, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			if(professorDisciplina==null)
				return new Result(-1, "Erro ao salvar. ProfessorDisciplina é nulo");

			int retorno;
			
			ProfessorDisciplina p = ProfessorDisciplinaDAO.get(professorDisciplina.getCdProfessor(), professorDisciplina.getCdDisciplina(), connect);
			
			if(p==null){
				retorno = ProfessorDisciplinaDAO.insert(professorDisciplina, connect);
			}
			else {
				retorno = ProfessorDisciplinaDAO.update(professorDisciplina, connect);
			}

			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();

			return new Result(retorno, (retorno<=0)?"Erro ao salvar...":"Salvo com sucesso...", "PROFESSORDISCIPLINA", professorDisciplina);
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
	public static Result remove(int cdProfessor, int cdDisciplina){
		return remove(cdProfessor, cdDisciplina, false, null);
	}
	public static Result remove(int cdProfessor, int cdDisciplina, boolean cascade){
		return remove(cdProfessor, cdDisciplina, cascade, null);
	}
	public static Result remove(int cdProfessor, int cdDisciplina, boolean cascade, Connection connect){
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
			retorno = ProfessorDisciplinaDAO.delete(cdProfessor, cdDisciplina, connect);
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
			pstmt = connect.prepareStatement("SELECT * FROM acd_professor_disciplina");
				return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ProfessorDisciplinaServices.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ProfessorDisciplinaServices.getAll: " + e);
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
		return Search.find("SELECT * FROM acd_professor_disciplina A " +
					"JOIN grl_produto_servico B ON (A.cd_disciplina = B.cd_produto_servico) ", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

	/**
	 * Busca todas as disciplinas que o professor esta apto a ministrar
	 * 
	 * @param cdPessoa
	 * @return ResultSetMap
	 */
	public static ResultSetMap getDisciplinasByProfessor(int cdProfessor) {
		return getDisciplinasByProfessor(cdProfessor, null);
	}
	
	public static ResultSetMap getDisciplinasByProfessor(int cdProfessor, Connection connect)	{
		boolean isConnectionNull = connect ==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement(
					"SELECT B.cd_produto_servico AS cd_disciplina, (B.nm_produto_servico) AS nm_disciplina " +
					"FROM acd_professor_disciplina A " +
					"JOIN grl_produto_servico B ON (A.cd_disciplina = B.cd_produto_servico) " +
					"JOIN grl_produto_servico_empresa BE ON (A.cd_disciplina = BE.cd_produto_servico) " +
					"JOIN acd_disciplina BB ON (A.cd_disciplina = BB.cd_disciplina) " +
					"LEFT OUTER JOIN acd_area_conhecimento C ON ( BB.cd_area_conhecimento = C.cd_area_conhecimento ) " +
					"WHERE A.cd_professor = "+cdProfessor+" ORDER BY B.cd_produto_servico");
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

}