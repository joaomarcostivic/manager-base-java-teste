package com.tivic.manager.acd;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;

import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.util.Result;

import com.tivic.sol.connection.Conexao;
import com.tivic.manager.grl.OcorrenciaServices;
import com.tivic.manager.util.Util;

public class DisciplinaAvaliacaoAlunoServices {
		
	public static Result save(DisciplinaAvaliacaoAluno disciplinaAvaliacaoAluno, int cdUsuario){
		return save(disciplinaAvaliacaoAluno, null, cdUsuario, null);
	}
	
	public static Result save(DisciplinaAvaliacaoAluno disciplinaAvaliacaoAluno, DisciplinaAvaliacaoAluno disciplinaAvaliacaoAlunoAnterior, int cdUsuario){
		return save(disciplinaAvaliacaoAluno, disciplinaAvaliacaoAlunoAnterior, cdUsuario, null);
	}
	
	public static Result save(DisciplinaAvaliacaoAluno disciplinaAvaliacaoAluno, int cdUsuario, Connection connect){
		return save(disciplinaAvaliacaoAluno, null, cdUsuario, connect);
	}

	public static Result save(DisciplinaAvaliacaoAluno disciplinaAvaliacaoAluno, DisciplinaAvaliacaoAluno disciplinaAvaliacaoAlunoAnterior, int cdUsuario, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			if(disciplinaAvaliacaoAluno==null)
				return new Result(-1, "Erro ao salvar. DisciplinaAvaliacaoAluno é nulo");

			int retorno;
			
			OfertaAvaliacao ofertaAvaliacao = OfertaAvaliacaoDAO.get(disciplinaAvaliacaoAluno.getCdOfertaAvaliacao(), disciplinaAvaliacaoAluno.getCdOferta(), connect);
			//Retirado pois agora os professores lançam as notas diretamente como AVALIADA
//			if(ofertaAvaliacao.getStOfertaAvaliacao() == OfertaAvaliacaoServices.ST_OFERTA_AVALIACAO_AVALIADA){
//				if(isConnectionNull)
//					Conexao.rollback(connect);
//				return new Result(-1, "A avaliação está fechada!");
//			}
			if(disciplinaAvaliacaoAluno.getVlConceito() > ofertaAvaliacao.getVlPeso()){
				if(isConnectionNull)
					Conexao.rollback(connect);
				return new Result(-1, "A nota do aluno não pode ser maior do que a nota da avaliação. Caso queira atribuir ponto extra, por favor aumente a nota da avaliação!");
			}
			
			DisciplinaAvaliacaoAluno d = DisciplinaAvaliacaoAlunoDAO.get(disciplinaAvaliacaoAluno.getCdMatricula(), disciplinaAvaliacaoAluno.getCdOfertaAvaliacao(), disciplinaAvaliacaoAluno.getCdOferta());
			if(d==null){
				retorno = DisciplinaAvaliacaoAlunoDAO.insert(disciplinaAvaliacaoAluno, connect);
			}
			else {
				retorno = DisciplinaAvaliacaoAlunoDAO.update(disciplinaAvaliacaoAluno, connect);
			}
			
			if(ofertaAvaliacao.getStOfertaAvaliacao() == OfertaAvaliacaoServices.ST_OFERTA_AVALIACAO_ABERTA){
				ofertaAvaliacao.setStOfertaAvaliacao(OfertaAvaliacaoServices.ST_OFERTA_AVALIACAO_APLICADA);
				if(OfertaAvaliacaoDAO.update(ofertaAvaliacao, connect) < 0){
					if(isConnectionNull)
						Conexao.rollback(connect);
					return new Result(-1, "Erro ao atualizar oferta avaliação");
				}
			}

//			int cdTipoOcorrencia = com.tivic.manager.grl.TipoOcorrenciaServices.getById(InstituicaoEducacensoServices.TP_OCORRENCIA_DISCIPLINA_AVALIACAO_ALUNO, connect).getCdTipoOcorrencia();
//			if(cdTipoOcorrencia > 0){
				
//				Matricula matricula = MatriculaDAO.get(disciplinaAvaliacaoAluno.getCdMatricula(), connect);
//				Aluno aluno = AlunoDAO.get(matricula.getCdAluno(), connect);
				
//				String txtOcorrencia = "";
//				if(disciplinaAvaliacaoAlunoAnterior.getVlConceito() > 0)
//					txtOcorrencia = "Alterada a nota do aluno "+aluno.getNmPessoa()+" ("+matricula.getNrMatricula()+"). Nota antiga: " + Util.formatNumber(disciplinaAvaliacaoAlunoAnterior.getVlConceito(), 1) + " Nota nova: " + Util.formatNumber(disciplinaAvaliacaoAluno.getVlConceito(), 1) + ".";
//				else
//					txtOcorrencia = "Adicionada a nota do aluno "+aluno.getNmPessoa()+" ("+matricula.getNrMatricula()+"). Nota nova: " + Util.formatNumber(disciplinaAvaliacaoAluno.getVlConceito(), 1) + ".";
//				
//				OcorrenciaDisciplinaAvaliacaoAluno ocorrencia = new OcorrenciaDisciplinaAvaliacaoAluno(0, 0, txtOcorrencia, Util.getDataAtual(), cdTipoOcorrencia, OcorrenciaServices.ST_CONCLUIDO, 0, cdUsuario, disciplinaAvaliacaoAluno.getCdMatriculaDisciplina(), disciplinaAvaliacaoAluno.getCdOfertaAvaliacao(), disciplinaAvaliacaoAluno.getCdOferta(), disciplinaAvaliacaoAlunoAnterior.getVlConceito(), disciplinaAvaliacaoAluno.getVlConceito());
//				if(OcorrenciaDisciplinaAvaliacaoAlunoServices.save(ocorrencia, null, connect).getCode() <= 0){
//					if(isConnectionNull)
//						Conexao.rollback(connect);
//					return new Result(-1, "Erro ao registrar ocorrencia de alteração na disciplina avaliação aluno");
//				}
//			}
			
			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();

			return new Result(retorno, (retorno<=0)?"Erro ao salvar...":"Salvo com sucesso...", "DISCIPLINAAVALIACAOALUNO", disciplinaAvaliacaoAluno);
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
	public static Result remove(int cdMatricula, int cdOfertaAvaliacao, int cdOferta){
		return remove(cdMatricula, cdOfertaAvaliacao, cdOferta, false, null);
	}
	public static Result remove(int cdMatricula, int cdOfertaAvaliacao, int cdOferta, boolean cascade){
		return remove(cdMatricula, cdOfertaAvaliacao, cdOferta, cascade, null);
	}
	public static Result remove(int cdMatricula, int cdOfertaAvaliacao, int cdOferta, boolean cascade, Connection connect){
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
			retorno = DisciplinaAvaliacaoAlunoDAO.delete(cdMatricula, cdOfertaAvaliacao, cdOferta, connect);
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

	public static Result removeByremoveByOfertao(int cdOfertaAvaliacao, int cdOferta) {
		return removeByOferta(cdOfertaAvaliacao, cdOferta, null);
	}
	
	public static Result removeByOferta(int cdOfertaAvaliacao, int cdOferta, Connection connect) {
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			int retorno = 0;
			ResultSetMap rsm = getAll();
			while (rsm.next()) {
				if (rsm.getInt("cd_oferta_avaliacao") == cdOfertaAvaliacao && rsm.getInt("cd_oferta") == cdOferta) {
					retorno = remove(rsm.getInt("cd_matricula_disciplina"), cdOfertaAvaliacao, cdOferta, true, connect).getCode();
					if(retorno<=0){
						Conexao.rollback(connect);
						return new Result(-2, "Este registro está vinculada a outros registros e não pode ser excluído!");
					}
				}
			}
			
			if (isConnectionNull)
				connect.commit();
			
			return new Result(1, "Registro excluída com sucesso!");
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
			pstmt = connect.prepareStatement("SELECT A.*, B.* FROM acd_disciplina_avaliacao_aluno A, "+
											 "acd_grade_conceito B "+
											 "WHERE A.cd_conceito = B.cd_conceito ");
				return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! DisciplinaAvaliacaoAlunoServices.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! DisciplinaAvaliacaoAlunoServices.getAll: " + e);
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
			pstmt = connect.prepareStatement("SELECT A.*, B.* FROM acd_disciplina_avaliacao_aluno A, "+
											 "acd_grade_conceito B "+
											 "WHERE A.cd_conceito = B.cd_conceito "+
											 "AND  A.cd_matricula_disciplina = "+cdMatricula);
				return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! DisciplinaAvaliacaoAlunoServices.getAllByMatricula: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! DisciplinaAvaliacaoAlunoServices.getAllByMatricula: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}	
	
	public static ResultSetMap getAllByMatriculaUnidadeDisciplina(int cdMatricula, int cdUnidade, int cdDisciplina) {
		return getAllByMatriculaUnidadeDisciplina(cdMatricula, cdUnidade, cdDisciplina, null);
	}

	public static ResultSetMap getAllByMatriculaUnidadeDisciplina(int cdMatricula, int cdUnidade, int cdDisciplina, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement("SELECT A.*, C.* FROM acd_disciplina_avaliacao_aluno A, acd_oferta_avaliacao C "+
											 "WHERE A.cd_oferta_avaliacao = C.cd_oferta_avaliacao" +
											 "  AND  A.cd_oferta = C.cd_oferta" +
											 "  AND  A.cd_matricula_disciplina = " + cdMatricula + 
											 "  AND C.cd_unidade = " + cdUnidade +
											 "  AND C.cd_disciplina = " + cdDisciplina + 
											 "	AND C.st_oferta_avaliacao = " + OfertaAvaliacaoServices.ST_OFERTA_AVALIACAO_AVALIADA);
				return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! DisciplinaAvaliacaoAlunoServices.getAllByMatricula: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! DisciplinaAvaliacaoAlunoServices.getAllByMatricula: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}	
	
	

	public static ResultSetMap getAllByMatriculaUnidadeDisciplinaAluno(int cdOferta, int cdUnidade, int cdDisciplina) {
		return getAllByMatriculaUnidadeDisciplinaAluno(cdOferta, cdUnidade, cdDisciplina, null);
	}

	public static ResultSetMap getAllByMatriculaUnidadeDisciplinaAluno(int cdOferta, int cdUnidade, int cdDisciplina, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement("SELECT A.*, C.* FROM acd_disciplina_avaliacao_aluno A, acd_oferta_avaliacao C "+
											 "WHERE A.cd_oferta_avaliacao = C.cd_oferta_avaliacao" +
											 "  AND C.cd_oferta " + cdOferta +
											 "  AND C.cd_unidade = " + cdUnidade +
											 "  AND C.cd_disciplina = " + cdDisciplina + 
											 "	AND C.st_oferta_avaliacao = " + OfertaAvaliacaoServices.ST_OFERTA_AVALIACAO_AVALIADA);
				return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! DisciplinaAvaliacaoAlunoServices.getAllByMatricula: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! DisciplinaAvaliacaoAlunoServices.getAllByMatricula: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}	
	
	public static ResultSetMap getAllByOfertaAvaliacao(int cdOfertaAvaliacao, int cdOferta) {
		return getAllByOfertaAvaliacao(cdOfertaAvaliacao, cdOferta, null);
	}

	public static ResultSetMap getAllByOfertaAvaliacao(int cdOfertaAvaliacao, int cdOferta, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ResultSetMap rsmFinal = new ResultSetMap();
			
			Oferta oferta = OfertaDAO.get(cdOferta, connect);
			Turma turma = TurmaDAO.get(oferta.getCdTurma(), connect);
			ResultSetMap rsm = InstituicaoServices.getAllMatriculasAtivas(turma.getCdInstituicao(), turma.getCdPeriodoLetivo(), turma.getCdTurma(), false, true);
			while(rsm.next()){
				
				HashMap<String, Object> register = new HashMap<String, Object>();
				
				register.put("CD_MATRICULA", rsm.getInt("cd_matricula"));
				register.put("NR_MATRICULA", rsm.getString("nr_matricula"));
				register.put("CD_ALUNO", rsm.getInt("cd_aluno"));
				register.put("NM_ALUNO", rsm.getString("nm_aluno"));
				DisciplinaAvaliacaoAluno disciplinaAvaliacaoAluno = DisciplinaAvaliacaoAlunoDAO.get(rsm.getInt("cd_matricula"), cdOfertaAvaliacao, cdOferta, connect);
				if(disciplinaAvaliacaoAluno != null){
					register.put("VL_CONCEITO", disciplinaAvaliacaoAluno.getVlConceito());
					register.put("CL_CONCEITO", Util.formatNumber(disciplinaAvaliacaoAluno.getVlConceito(), 1));
				}
				else{
					register.put("VL_CONCEITO", 0);
				}
				
				rsmFinal.addRegister(register);
				
			}
			rsm.beforeFirst();
			
			return rsmFinal;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! DisciplinaAvaliacaoAlunoServices.getAllByOfertaAvaliacao: " + e);
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
		ResultSetMap rsm = Search.find("SELECT * FROM acd_disciplina_avaliacao_aluno A, acd_oferta_avaliacao B WHERE A.cd_oferta_avaliacao = B.cd_oferta_avaliacao AND A.cd_oferta = B.cd_oferta", "", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
		while(rsm.next()){
			CursoUnidade cursoUnidade = CursoUnidadeDAO.get(rsm.getInt("cd_unidade"), rsm.getInt("cd_curso"), connect);
			rsm.setValueToField("NM_UNIDADE", cursoUnidade.getNmUnidade());
			rsm.setValueToField("DT_NM_AVALIACAO", Util.convCalendarString3(rsm.getGregorianCalendar("dt_avaliacao")));
			rsm.setValueToField("NM_ST_OFERTA_AVALIACAO", OfertaAvaliacaoServices.situacaoOfertaAvaliacao[rsm.getInt("ST_OFERTA_AVALIACAO")]);
			TipoAvaliacao tipoAvaliacao = TipoAvaliacaoDAO.get(rsm.getInt("cd_tipo_avaliacao"), connect);
			rsm.setValueToField("NM_TP_AVALIACAO", (tipoAvaliacao != null ? tipoAvaliacao.getNmTipoAvaliacao() : ""));
			rsm.setValueToField("CL_PESO", Util.formatNumber(rsm.getFloat("vl_peso"), 1));
			rsm.setValueToField("CL_CONCEITO", Util.formatNumber(rsm.getFloat("vl_conceito"), 1));
		}
		rsm.beforeFirst();
		
		ArrayList<String> fields = new ArrayList<String>();
		fields.add("CD_UNIDADE");
		fields.add("DT_AVALIACAO");
		rsm.orderBy(fields);
		
		rsm.beforeFirst();
		return rsm;
	}
	
	public static float getMediaNotaByTurma(int cdMatricula, int cdTurma) {
		return getMediaNotaByTurma(cdMatricula, cdTurma, null);
	}

	public static float getMediaNotaByTurma(int cdMatricula, int cdTurma, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			
			float qtMediaNota = 0;
			
			ResultSetMap rsm = new ResultSetMap(connect.prepareStatement("SELECT DAA.VL_CONCEITO, OA.VL_PESO FROM acd_disciplina_avaliacao_aluno DAA "
					+ "														JOIN acd_oferta_avaliacao OA ON (DAA.cd_oferta_avaliacao = OA.cd_oferta_avaliacao"
					+ "																						AND DAA.cd_oferta = OA.cd_oferta)"
					+ "														JOIN acd_matricula_disciplina MD ON (DAA.cd_matricula_disciplina = MD.cd_matricula_disciplina)"
					+ "														JOIN acd_oferta O ON (OA.cd_oferta = O.cd_oferta)"
					+ "														WHERE O.cd_turma = " + cdTurma
					+ "														  AND MD.cd_matricula = " + cdMatricula
					+ "														  AND O.st_oferta = " + OfertaServices.ST_ATIVO
					+ "														  AND MD.st_matricula_disciplina = " + MatriculaDisciplinaServices.ST_EM_CURSO
					+ "														  AND OA.st_oferta_avaliacao = " + OfertaAvaliacaoServices.ST_OFERTA_AVALIACAO_AVALIADA).executeQuery());
			while(rsm.next()){
				
				qtMediaNota += (rsm.getFloat("VL_CONCEITO") / rsm.getFloat("VL_PESO"));
				
			}
			rsm.beforeFirst();
			
			return (rsm.size() == 0 ? 0 : (qtMediaNota / rsm.size()));
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! DisciplinaAvaliacaoAlunoServices.getMediaNotaByTurma: " + e);
			return -1;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static float getMediaNotaByDisciplinaCursos(int cdDisciplina, int cdCurso, int cdInstituicao) {
		return getMediaNotaByDisciplinaCursos(cdDisciplina, cdCurso, cdInstituicao, null);
	}

	public static float getMediaNotaByDisciplinaCursos(int cdDisciplina, int cdCurso, int cdInstituicao, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			
			float qtMediaNota = 0;
			
			ResultSetMap rsm = new ResultSetMap(connect.prepareStatement("SELECT DAA.VL_CONCEITO, OA.VL_PESO FROM acd_disciplina_avaliacao_aluno DAA "
					+ "														JOIN acd_oferta_avaliacao OA ON (DAA.cd_oferta_avaliacao = OA.cd_oferta_avaliacao"
					+ "																						AND DAA.cd_oferta = OA.cd_oferta)"
					+ "														JOIN acd_matricula_disciplina MD ON (DAA.cd_matricula_disciplina = MD.cd_matricula_disciplina)"
					+ "														JOIN acd_oferta O ON (OA.cd_oferta = O.cd_oferta)"
					+ "														JOIN acd_turma T ON (O.cd_turma = T.cd_turma)"
					+ "														WHERE O.cd_disciplina = " + cdDisciplina
					+ "														  AND O.cd_curso = " + cdCurso
					+ "														  AND T.cd_instituicao = " + cdInstituicao
					+ "														  AND T.st_turma = " + TurmaServices.ST_ATIVO
					+ "														  AND O.st_oferta = " + OfertaServices.ST_ATIVO
					+ "														  AND MD.st_matricula_disciplina = " + MatriculaDisciplinaServices.ST_EM_CURSO
					+ "														  AND OA.st_oferta_avaliacao = " + OfertaAvaliacaoServices.ST_OFERTA_AVALIACAO_AVALIADA).executeQuery());
			while(rsm.next()){
				
				qtMediaNota += (rsm.getFloat("VL_CONCEITO") / rsm.getFloat("VL_PESO"));
				
			}
			rsm.beforeFirst();
			
			return (rsm.size() == 0 ? 0 : (qtMediaNota / rsm.size()));
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! DisciplinaAvaliacaoAlunoServices.getMediaNotaByTurma: " + e);
			return -1;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	

}