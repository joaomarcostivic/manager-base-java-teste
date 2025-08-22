package com.tivic.manager.acd;

import java.sql.*;
import java.util.ArrayList;

import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.util.Result;

import com.tivic.sol.connection.Conexao;

public class MatriculaDisciplinaServices {

	/* tipos de matriculas */
	public static final int TP_NORMAL = 0;
	public static final int TP_COMPLEMENTO = 1;
	public static final int TP_APROVEITAMENTO = 2;
	
	public static final String[] tipoMatricula = {"Normal", 
		"Complemento",
		"Aproveitamento"};
	
	/* situacao da matricuala */
	
	public static final int ST_EM_CURSO = 0;
	public static final int ST_APROVADO = 1;
	public static final int ST_REPROVADO_NOTA = 2;
	public static final int ST_REPROVADO_FALTAS = 3;
	public static final int ST_TRANCADA = 4;
	
	public static final String[] situacaoMatricula = {"Em curso", 
		"Aprovado", 
		"Reprovado (Nota)",
		"Reprovado (Faltas)",
		"Trancada"};
	
	public static Result save(MatriculaDisciplina matriculaDisciplina){
		return save(matriculaDisciplina, null);
	}

	public static Result save(MatriculaDisciplina matriculaDisciplina, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			if(matriculaDisciplina==null)
				return new Result(-1, "Erro ao salvar. MatriculaDisciplina é nulo");

			int retorno;
			if(matriculaDisciplina.getCdMatriculaDisciplina()==0){
				retorno = MatriculaDisciplinaDAO.insert(matriculaDisciplina, connect);
				matriculaDisciplina.setCdMatriculaDisciplina(retorno);
			}
			else {
				retorno = MatriculaDisciplinaDAO.update(matriculaDisciplina, connect);
			}

			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();

			return new Result(retorno, (retorno<=0)?"Erro ao salvar...":"Salvo com sucesso...", "MATRICULADISCIPLINA", matriculaDisciplina);
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
	
	/**
	 * Método para salvar a aprovacao do aluno na disciplina
	 * @param cdMatriculaDisciplina
	 * @param lgAprovado
	 * @return
	 */
	public static int saveAprovacao(int cdMatriculaDisciplina, int lgAprovado){
		return saveAprovacao(cdMatriculaDisciplina, lgAprovado, null);
	}

	public static int saveAprovacao(int cdMatriculaDisciplina, int lgAprovado, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			
			PreparedStatement pstmt = connect.prepareStatement("UPDATE acd_matricula_disciplina SET lg_aprovado=? WHERE cd_matricula_disciplina=?");
			
			pstmt.setInt(1, lgAprovado);
			pstmt.setInt(2, cdMatriculaDisciplina);
			
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! MatriculaDisciplinaServices.saveAprovacao: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! MatriculaDisciplinaServices.saveAprovacao: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static Result remove(int cdMatriculaDisciplina){
		return remove(cdMatriculaDisciplina, false, null);
	}
	
	public static Result remove(int cdMatriculaDisciplina, boolean cascade){
		return remove(cdMatriculaDisciplina, cascade, null);
	}
	
	public static Result remove(int cdMatriculaDisciplina, boolean cascade, Connection connect){
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
			retorno = MatriculaDisciplinaDAO.delete(cdMatriculaDisciplina, connect);
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
			ResultSetMap rsm = getAllByMatriculaSimples(cdMatricula, connect);
			while (rsm.next()) {
				if (rsm.getInt("cd_matricula") == cdMatricula) {
					retorno = remove(rsm.getInt("cd_matricula_disciplina"), true, connect).getCode();
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
	
	public static ResultSetMap getAll() {
		return getAll(null);
	}

	public static ResultSetMap getAll(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM acd_matricula_disciplina");
				return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! MatriculaDisciplinaServices.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! MatriculaDisciplinaServices.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static ResultSetMap getAllByMatriculaSimples(int cdMatricula) {
		return getAllByMatriculaSimples(cdMatricula, null);
	}

	public static ResultSetMap getAllByMatriculaSimples(int cdMatricula, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM acd_matricula_disciplina WHERE cd_matricula = " + cdMatricula);
				return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! MatriculaDisciplinaServices.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! MatriculaDisciplinaServices.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	/**
	 * Retorna o conceito do aluno na disciplina
	 * 
	 * @param cdDisciplina (disciplina, cdPeriodoLetivo)
	 * @return ResultSetMap
	 */
	@Deprecated
	public static ResultSetMap getAllByDisciplina(int cdDisciplina) {
		return getAllByDisciplina(cdDisciplina, 0, null);
	}
	@Deprecated
	public static ResultSetMap getAllByDisciplina(int cdDisciplina, int cdPeriodoLetivo) {
		return getAllByDisciplina(cdDisciplina, cdPeriodoLetivo, null);
	}
	@Deprecated
	public static ResultSetMap getAllByDisciplina(int cdDisciplina, int cdPeriodoLetivo, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		ResultSetMap rsmMatriculasDisciplinas;
		try {
			rsmMatriculasDisciplinas = new ResultSetMap( connect.prepareStatement("SELECT A.*, B.*, D.nm_pessoa AS nm_aluno FROM acd_matricula_disciplina A "+
																				  "  LEFT OUTER JOIN acd_grade_conceito B ON (A.cd_conceito = B.cd_conceito)"+
																				  "  LEFT OUTER JOIN acd_matricula C ON (A.cd_matricula = C.cd_matricula)"+
																				  "  LEFT OUTER JOIN grl_pessoa D ON (C.cd_aluno = D.cd_pessoa)"+
																				  " WHERE A.cd_disciplina = " + cdDisciplina +
																				  (cdPeriodoLetivo!=0?" AND A.cd_periodo_letivo = " + cdPeriodoLetivo : "") +
																				  " ORDER BY nm_aluno").executeQuery() );
			while( rsmMatriculasDisciplinas.next() ){
//				ResultSetMap rsmAulasMatricula = AulaMatriculaServices.getAllByMatriculaDisciplina( rsmMatriculasDisciplinas.getInt("cd_matricula_disciplina") , connect);
//				rsmMatriculasDisciplinas.setValueToField("AULAS", rsmAulasMatricula);
			}
			return rsmMatriculasDisciplinas;
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! MatriculaDisciplinaServices.getAllByMatricula: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! MatriculaDisciplinaServices.getAllByMatricula: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	/**
	 * Retorna o conceito do aluno na disciplina
	 * 
	 * @param cdMatricula (matricula do aluno)
	 * @return ResultSetMap
	 */
	@Deprecated
	public static ResultSetMap getAllByMatricula(int cdMatricula) {
		return getAllByMatricula( cdMatricula, false, null);
	}

	@Deprecated
	public static ResultSetMap getAllByMatricula(int cdMatricula, Connection connect) {
		return getAllByMatricula( cdMatricula, false, connect);
	}
	
	@Deprecated
	public static ResultSetMap getAllByMatricula(int cdMatricula, int cdProfessor, Connection connect) {
		return getAllByMatricula( cdMatricula, cdProfessor, false, connect);
	}
	
	@Deprecated
	public static ResultSetMap getAllByMatricula(int cdMatricula, boolean semConceito) {
		return getAllByMatricula( cdMatricula, semConceito, null);
	}

	@Deprecated
	public static ResultSetMap getAllByMatricula(int cdMatricula, boolean semConceito, Connection connect) {
		return getAllByMatricula( cdMatricula, 0, semConceito, connect);
	}
	
	@Deprecated
	public static ResultSetMap getAllByMatricula(int cdMatricula, int cdProfessor, boolean semConceito, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		ResultSetMap rsmMatriculasDisciplinas;
		try {
			
			rsmMatriculasDisciplinas = new ResultSetMap( connect.prepareStatement("SELECT A.*, B.*, C.nm_produto_servico AS nm_disciplina FROM acd_matricula_disciplina A "+
																				  " LEFT OUTER JOIN acd_oferta O ON (A.cd_oferta = O.cd_oferta) " +
																				  " LEFT OUTER JOIN acd_grade_conceito B ON (A.cd_conceito = B.cd_conceito) "+
																				  " LEFT OUTER JOIN grl_produto_servico C ON (A.cd_disciplina = C.cd_produto_servico) "+
																				  "WHERE A.cd_matricula = "+cdMatricula+
																			  	  "  AND O.st_oferta = " + OfertaServices.ST_ATIVO + 
																			  	  (cdProfessor > 0 ? " AND (O.cd_professor = "+cdProfessor+" OR EXISTS (SELECT * FROM acd_pessoa_oferta PO "
																			  	  																		+ "	WHERE PO.cd_oferta = O.cd_oferta "
																			  	  																		+ "	  AND PO.cd_pessoa = "+cdProfessor
																			  	  																		+ "   AND PO.st_pessoa_oferta = "+PessoaOfertaServices.ST_ATIVO+"))" : "") + 
																		  	       " ORDER BY C.nm_produto_servico").executeQuery() );
			
			if(!semConceito){
				while( rsmMatriculasDisciplinas.next() ){
//					ResultSetMap rsmAulasMatricula = AulaMatriculaServices.getAllByMatriculaDisciplina( rsmMatriculasDisciplinas.getInt("cd_matricula_disciplina") , connect);
//					rsmMatriculasDisciplinas.setValueToField("AULAS", rsmAulasMatricula);
				}
				rsmMatriculasDisciplinas.beforeFirst();
			}
			
			return rsmMatriculasDisciplinas;
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! MatriculaDisciplinaServices.getAllByMatricula: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! MatriculaDisciplinaServices.getAllByMatricula: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	@Deprecated
	public static ResultSetMap getByAlunoDisciplina(int cdMatricula, int cdDisciplina) {
		return getByAlunoDisciplina( cdMatricula, cdDisciplina, null);
	}

	@Deprecated
	public static ResultSetMap getByAlunoDisciplina(int cdMatricula, int cdDisciplina, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		ResultSetMap rsmMatriculasDisciplinas;
		try {
			
			rsmMatriculasDisciplinas = new ResultSetMap( connect.prepareStatement("SELECT A.*, B.* FROM acd_matricula_disciplina A "+
																				  "LEFT OUTER JOIN acd_grade_conceito B ON ( A.cd_conceito = B.cd_conceito ) "+
																				  "WHERE A.cd_matricula = "+cdMatricula+
																				  (cdDisciplina > 0 ? "  AND A.cd_disciplina = "+cdDisciplina : "")).executeQuery() );
			while( rsmMatriculasDisciplinas.next() ){
//				ResultSetMap rsmAulasMatricula = AulaMatriculaServices.getAllByMatriculaDisciplina( rsmMatriculasDisciplinas.getInt("cd_matricula_disciplina") , connect);
//				rsmMatriculasDisciplinas.setValueToField("AULAS", rsmAulasMatricula);
			}
			rsmMatriculasDisciplinas.beforeFirst();
			
			return rsmMatriculasDisciplinas;
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! MatriculaDisciplinaServices.getByAlunoDisciplina: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! MatriculaDisciplinaServices.getByAlunoDisciplina: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	@Deprecated
	public static ResultSetMap getByMatriculaDisciplinaOferta(int cdMatricula, int cdDisciplina, int cdOferta) {
		return getByMatriculaDisciplinaOferta( cdMatricula, cdDisciplina, cdOferta, null);
	}

	@Deprecated
	public static ResultSetMap getByMatriculaDisciplinaOferta(int cdMatricula, int cdDisciplina, int cdOferta, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		ResultSetMap rsmMatriculasDisciplinas;
		try {
			rsmMatriculasDisciplinas = new ResultSetMap( connect.prepareStatement("SELECT A.*, B.* FROM acd_matricula_disciplina A "+
																				  "LEFT OUTER JOIN acd_grade_conceito B ON ( A.cd_conceito = B.cd_conceito ) "+
																				  "WHERE A.cd_matricula = "+cdMatricula+
																				  "  AND A.cd_disciplina = "+cdDisciplina+
																				  "  AND A.cd_oferta = " + cdOferta).executeQuery() );
//			while( rsmMatriculasDisciplinas.next() ){
//				ResultSetMap rsmAulasMatricula = AulaMatriculaServices.getAllByMatriculaDisciplina( rsmMatriculasDisciplinas.getInt("cd_matricula_disciplina") , connect);
//				rsmMatriculasDisciplinas.setValueToField("AULAS", rsmAulasMatricula);
//			}
//			rsmMatriculasDisciplinas.beforeFirst();
			
			return rsmMatriculasDisciplinas;
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! MatriculaDisciplinaServices.getByAlunoDisciplina: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! MatriculaDisciplinaServices.getByAlunoDisciplina: " + e);
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
		return Search.find("SELECT * FROM acd_matricula_disciplina A "+ 
						   "  LEFT OUTER JOIN acd_disciplina B ON (A.cd_disciplina = B.cd_disciplina) " +
						   "  JOIN acd_instituicao_periodo C ON (A.cd_periodo_letivo = C.cd_periodo_letivo) ", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}
	
	@Deprecated
	public static int insert(MatriculaDisciplina matDisciplina) {
		return insert(matDisciplina, null);
	}

	@Deprecated
	public static int insert(MatriculaDisciplina matDisciplina, Connection connection){
		boolean isConnectionNull = connection==null;
		try {
			connection = isConnectionNull ? Conexao.conectar() : connection;
			connection.setAutoCommit(isConnectionNull ? false : connection.getAutoCommit());
			
			int code = MatriculaDisciplinaDAO.insert(matDisciplina, connection);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connection);
				return -1;
			}
			
			if (isConnectionNull)
				connection.commit();

			return code;
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! MatriculaDisciplinaServices.insert: " +  e);
			if (isConnectionNull)
				Conexao.rollback(connection);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}
	
}