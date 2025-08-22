package com.tivic.manager.acd;

import java.sql.*;
import java.util.ArrayList;

import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.util.Result;

import com.tivic.sol.connection.Conexao;
import com.tivic.manager.seg.AuthData;

public class AulaMatriculaServices {

	public static Result save(AulaMatricula aulaMatricula){
		return save(aulaMatricula, null);
	}

	public static Result save(AulaMatricula aulaMatricula, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			if(aulaMatricula==null)
				return new Result(-1, "Erro ao salvar. AulaMatricula é nulo");

			int retorno;
			if(AulaMatriculaDAO.get(aulaMatricula.getCdAula(), aulaMatricula.getCdMatricula())==null) {
				retorno = AulaMatriculaDAO.insert(aulaMatricula, connect);
			}
			else {
				retorno = AulaMatriculaDAO.update(aulaMatricula, connect);
			}

			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();
			
			return new Result(retorno, (retorno<=0)?"Erro ao salvar...":"Salvo com sucesso...", "AULAMATRICULA", aulaMatricula);
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
	
	public static Result remove(AulaMatricula aulaMatricula) {
		return remove(aulaMatricula.getCdAula(), aulaMatricula.getLgPresenca());
	}
	public static Result remove(int cdAula, int cdMatricula){
		return remove(cdAula, cdMatricula, false, null, null);
	}
	public static Result remove(int cdAula, int cdMatricula, boolean cascade){
		return remove(cdAula, cdMatricula, cascade, null, null);
	}
	public static Result remove(int cdAula, int cdMatricula, boolean cascade, AuthData authData){
		return remove(cdAula, cdMatricula, cascade, authData, null);
	}
	public static Result remove(int cdAula, int cdMatricula, boolean cascade, AuthData authData, Connection connect){
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
			retorno = AulaMatriculaDAO.delete(cdAula, cdMatricula, connect);
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
	
	
	public static ResultSetMap getAllByAula(int cdAula) {
		return getAllByAula(cdAula, null);
	}

	public static ResultSetMap getAllByAula(int cdAula, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
				
			// Sql para buscar a presenca e observacao de cada aluno
			pstmt = connect.prepareStatement("SELECT A.*, B.nr_matricula, C.nm_pessoa FROM acd_aula_matricula A, acd_matricula B, grl_pessoa C " +
											" WHERE A.cd_aula = " + cdAula + 
											"   AND A.cd_matricula = B.cd_matricula " +
											"   AND B.cd_aluno = C.cd_pessoa");
			
			ResultSetMap rsmAulaMatricula = new ResultSetMap(pstmt.executeQuery());
			return rsmAulaMatricula;
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AulaMatriculaServices.getAllByAula: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! AulaMatriculaServices.getAllByAula: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM acd_aula_matricula");
				return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AulaMatriculaServices.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! AulaMatriculaServices.getAll: " + e);
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
		return Search.find("SELECT * FROM acd_aula_matricula A, acd_aula B WHERE A.cd_aula = B.cd_aula ", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}
	
	public static Result updatePresenca(int cdMatriculaDisciplina, int cdAula, int lgPresenca){
		return updatePresenca(cdMatriculaDisciplina, cdAula, lgPresenca, null);
	}
	
	public static Result updatePresenca(int cdMatriculaDisciplina, int cdAula, int lgPresenca, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			AulaMatricula aulaMatricula = AulaMatriculaDAO.get(cdMatriculaDisciplina, cdAula, connect);
			aulaMatricula.setLgPresenca(lgPresenca);
			if(AulaMatriculaDAO.update(aulaMatricula, connect) <= 0){
				if(isConnectionNull)
					Conexao.rollback(connect);
				return new Result(-1, "Erro ao atualizar presença");
			}
			
			//CRIAR OCORRENCIA
			return new Result(1);
			
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! AulaMatriculaServices.updatePresenca: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static float getMediaFrequenciaByTurma(int cdMatricula, int cdTurma) {
		return getMediaFrequenciaByTurma(cdMatricula, cdTurma, null);
	}

	public static float getMediaFrequenciaByTurma(int cdMatricula, int cdTurma, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			
			int qtFrequenciaPositivas = 0;
			
			ResultSetMap rsm = new ResultSetMap(connect.prepareStatement("SELECT AM.lg_presenca FROM acd_aula_matricula AM "
					+ "														JOIN acd_aula A ON (AM.cd_aula = A.cd_aula)"
					+ "														JOIN acd_oferta O ON (A.cd_oferta = O.cd_oferta)"
					+ "														WHERE A.cd_turma = " + cdTurma
					+ "														  AND AM.cd_matricula = " + cdMatricula
					+ "														  AND O.st_oferta = " + OfertaServices.ST_ATIVO
					+ "														  AND A.st_aula = " + AulaServices.ST_FECHADA).executeQuery());
			while(rsm.next()){
				if(rsm.getInt("LG_PRESENCA") == 1)
					qtFrequenciaPositivas++;
			}
			rsm.beforeFirst();
			
			return (rsm.size() == 0 ? 0 : ((float)qtFrequenciaPositivas / (float)rsm.size()));
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! AulaMatriculaServices.getMediaFrequenciaByTurma: " + e);
			return -1;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static float getMediaFrequenciaByDisciplinaCursos(int cdDisciplina, int cdCurso, int cdInstituicao) {
		return getMediaFrequenciaByDisciplinaCursos(cdDisciplina, cdCurso, cdInstituicao, null);
	}

	public static float getMediaFrequenciaByDisciplinaCursos(int cdDisciplina, int cdCurso, int cdInstituicao, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			
			int qtFrequenciaPositivas = 0;
			
			ResultSetMap rsm = new ResultSetMap(connect.prepareStatement("SELECT AM.lg_presenca FROM acd_aula_matricula AM "
					+ "														JOIN acd_aula A ON (AM.cd_aula = A.cd_aula)"
					+ "														JOIN acd_oferta O ON (A.cd_oferta = O.cd_oferta)"
					+ "														JOIN acd_turma T ON (O.cd_turma = T.cd_turma)"
					+ "														WHERE O.cd_disciplina = " + cdDisciplina
					+ "														  AND A.cd_disciplina = " + cdDisciplina
					+ "														  AND O.cd_curso = " + cdCurso
					+ "														  AND T.cd_instituicao = " + cdInstituicao
					+ "														  AND T.st_turma = " + TurmaServices.ST_ATIVO
					+ "														  AND O.st_oferta = " + OfertaServices.ST_ATIVO
					+ "														  AND A.st_aula = " + AulaServices.ST_FECHADA).executeQuery());
			while(rsm.next()){
				if(rsm.getInt("LG_PRESENCA") == 1)
					qtFrequenciaPositivas++;
			}
			rsm.beforeFirst();
			
			return (rsm.size() == 0 ? 0 : ((float)qtFrequenciaPositivas / (float)rsm.size()));
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! AulaMatriculaServices.getMediaFrequenciaByDisciplinaCursos: " + e);
			return -1;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

}