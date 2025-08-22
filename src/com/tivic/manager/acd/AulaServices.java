package com.tivic.manager.acd;

import java.sql.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;

import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.util.Result;

import com.tivic.manager.util.Util;
import com.tivic.sol.connection.Conexao;

public class AulaServices {
	
	public static final int ST_EM_ABERTO   = 0;
	public static final int ST_FECHADA = 1;
	public static final int ST_CANCELADA = 2;

	public static final String[] situacoesAula = {"Em Aberto", "Fechada", "Cancelada"};
	
	public static Result save(Aula aula){
		return save(aula, null, null, null);
	}
	
	public static Result save(Aula aula, ArrayList<AulaMatricula> aulasMatricula){
		return save(aula, null, aulasMatricula, null);
	}

	public static Result save(Aula aula, Connection connect){
		return save(aula, null, null, connect);
	}
	
	public static Result save(Aula aula, Plano plano){
		return save(aula, plano, null, null);
	}

	public static Result save(Aula aula, Plano plano, ArrayList<AulaMatricula> aulasMatricula, Connection connect){
		return save(aula, plano, aulasMatricula, null, null);
	}
	
	public static Result save(Aula aula, ArrayList<AulaMatricula> aulasMatricula, ArrayList<AulaTopico> aulasTopico){
		return save(aula, null, aulasMatricula, aulasTopico, null);
	}
	 /**
	  * Insere ou atualiza uma aula
	  * @param aula
	  * @param plano
	  * @param aulasMatricula
	  * @param aulasTopico
	  * @param connect
	  * @return
	  */
	public static Result save(Aula aula, Plano plano, ArrayList<AulaMatricula> aulasMatricula, ArrayList<AulaTopico> aulasTopico, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			if(aula==null)
				return new Result(-1, "Erro ao salvar. Aula é nulo");

			
			// Validacao no horario do professor ?
			Professor professor = ProfessorDAO.get(aula.getCdProfessor(), connect);
			Turma turma = TurmaDAO.get(aula.getCdTurma(), connect);
			Curso curso = CursoDAO.get(turma.getCdCurso(), connect);
			
			ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
			criterios.add(new ItemComparator("cd_turma", "" + aula.getCdTurma(), ItemComparator.EQUAL, Types.INTEGER));
			
			//Nem todas as aulas requerem disciplinas, como as do INfantil e do Fundamental 1 (segundo exigencias recentes dos professores)
			if(aula.getCdDisciplina() > 0)
				criterios.add(new ItemComparator("cd_disciplina", "" + aula.getCdDisciplina(), ItemComparator.EQUAL, Types.INTEGER));
			criterios.add(new ItemComparator("cd_professor", "" + aula.getCdProfessor(), ItemComparator.EQUAL, Types.INTEGER));
			criterios.add(new ItemComparator("dt_aula", Util.convCalendarStringSql(aula.getDtAula()), ItemComparator.EQUAL, Types.VARCHAR));
			//O Horario foi temporairamente retirado para flexibilizar o cadastro de frequencia
//			if(aula.getCdHorario() > 0)
//				criterios.add(new ItemComparator("cd_horario", "" + aula.getCdHorario(), ItemComparator.EQUAL, Types.INTEGER));
			criterios.add(new ItemComparator("cd_oferta", "" + aula.getCdOferta(), ItemComparator.EQUAL, Types.INTEGER));
			criterios.add(new ItemComparator("cd_aula", "" + aula.getCdAula(), ItemComparator.DIFFERENT, Types.INTEGER));
			ResultSetMap rsm = AulaDAO.find(criterios, connect);
			if(rsm.next()){
				if(isConnectionNull)
					Conexao.rollback(connect);
				return new Result(-1, "Esta aula já foi cadastrada.");
			}
			
			//Caso a turma seja integral, o sistema permite que o professor tenha aulas em horários conflitantes
//			if(turma.getTpTurno() != TurmaServices.TP_INTEGRAL){
//				criterios = new ArrayList<ItemComparator>();
//				criterios.add(new ItemComparator("cd_professor", "" + aula.getCdProfessor(), ItemComparator.EQUAL, Types.INTEGER));
//				criterios.add(new ItemComparator("dt_aula", Util.convCalendarStringSql(aula.getDtAula()), ItemComparator.EQUAL, Types.VARCHAR));
//				criterios.add(new ItemComparator("cd_horario", "" + aula.getCdHorario(), ItemComparator.EQUAL, Types.INTEGER));
//				criterios.add(new ItemComparator("cd_oferta", "" + aula.getCdOferta(), ItemComparator.EQUAL, Types.INTEGER));
//				criterios.add(new ItemComparator("cd_aula", "" + aula.getCdAula(), ItemComparator.DIFFERENT, Types.INTEGER));
//				if(AulaDAO.find(criterios, connect).next()){
//					if(isConnectionNull)
//						Conexao.rollback(connect);
//					return new Result(-1, "O professor(a) " + professor.getNmPessoa() + " já está dando aula nesse horário.");
//				}
//			}
			
			// Validacao de aula ?
			
			criterios = new ArrayList<ItemComparator>();
			criterios.add(new ItemComparator("cd_turma", "" + aula.getCdTurma(), ItemComparator.EQUAL, Types.INTEGER));
			criterios.add(new ItemComparator("dt_aula", Util.convCalendarStringSql(aula.getDtAula()), ItemComparator.EQUAL, Types.VARCHAR));
//			criterios.add(new ItemComparator("cd_horario", "" + aula.getCdHorario(), ItemComparator.EQUAL, Types.INTEGER));
			criterios.add(new ItemComparator("cd_oferta", "" + aula.getCdOferta(), ItemComparator.EQUAL, Types.INTEGER));
			criterios.add(new ItemComparator("cd_aula", "" + aula.getCdAula(), ItemComparator.DIFFERENT, Types.INTEGER));
			if(AulaDAO.find(criterios, connect).next()){
				if(isConnectionNull)
					Conexao.rollback(connect);
				return new Result(-1, "A turma " + curso.getNmProdutoServico() + " - " + turma.getNmTurma() + " já está tendo aula nesse horário.");
			}
			
			// Validacao do plano de curso, quando nao tem
//			if(plano!=null && plano.getCdPlano()==0){
//				int ret = PlanoDAO.insert(plano, connect);
//				if(ret <= 0){
//					if(isConnectionNull)
//						Conexao.rollback(connect);
//					return new Result(ret, "Erro ao salvar plano");
//				}
//				plano.setCdPlano(ret);
//				aula.setCdPlano(ret);
//			}
			
			//Atualiza os topicos/conteudos das aulas
			if(aulasTopico != null){
				
				int ret = connect.prepareStatement("DELETE FROM acd_aula_topico WHERE cd_aula = " + aula.getCdAula()).executeUpdate();
				if(ret < 0){
					if(isConnectionNull)
						Conexao.rollback(connect);
					return new Result(-1, "Erro ao remover topicos de aula");
				}
				
				for(AulaTopico aulaTopico : aulasTopico){
					
					if(AulaTopicoDAO.insert(new AulaTopico(aulaTopico.getCdPlano(), aulaTopico.getCdSecao(), aulaTopico.getCdTopico(), aula.getCdAula(), 1, null), connect) < 0){
						if(isConnectionNull)
							Conexao.rollback(connect);
						return new Result(-1, "Erro ao inserir topicos de aula");
					}
				}
			}
			
			
			
			// SALVAR AULA
			int retorno;
			if(aula.getCdAula()==0){
				retorno = AulaDAO.insert(aula, connect);
				aula.setCdAula(retorno);
			}
			else {
				retorno = AulaDAO.update(aula, connect);
			}

			// CRIAR AS RELACOES 
//			Turma turmaAula = TurmaServices.get(aula.getCdTurma(), connect);
//			
//			ResultSetMap rsmAlunos = TurmaServices.getAlunos(aula.getCdTurma(), true);
//			while(rsmAlunos.next()){
//				criterios = new ArrayList<ItemComparator>();
//				criterios.add(new ItemComparator("cd_matricula", rsmAlunos.getString("cd_matricula"), ItemComparator.EQUAL, Types.INTEGER));
//				criterios.add(new ItemComparator("cd_disciplina", "" + aula.getCdDisciplina(), ItemComparator.EQUAL, Types.INTEGER));
//				ResultSetMap rsmMatriculaDisciplina = MatriculaDisciplinaServices.find(criterios, connect);
//				MatriculaDisciplina matriculaDisciplina = new MatriculaDisciplina();
//				if(!rsmMatriculaDisciplina.next()){
//					matriculaDisciplina.setCdCurso(turmaAula.getCdCurso());
//					matriculaDisciplina.setCdDisciplina(aula.getCdDisciplina());
//					matriculaDisciplina.setCdMatricula(rsmAlunos.getInt("cd_matricula"));
//					matriculaDisciplina.setCdPeriodoLetivo(turmaAula.getCdPeriodoLetivo());
//					matriculaDisciplina.setCdProfessor(aula.getCdProfessor());
//					int ret = MatriculaDisciplinaDAO.insert(matriculaDisciplina, connect);
//					if(ret <= 0){
//						if(isConnectionNull)
//							Conexao.rollback(connect);
//						return new Result(-1, "Erro ao salvar matricula disciplina");
//					}
//					matriculaDisciplina.setCdMatriculaDisciplina(ret);
//					
//				}
//				else{
//					matriculaDisciplina = MatriculaDisciplinaDAO.get(rsmMatriculaDisciplina.getInt("cd_matricula_disciplina"), connect);
//				}
//				
//				criterios = new ArrayList<ItemComparator>();
//				criterios.add(new ItemComparator("cd_aula", "" + aula.getCdAula(), ItemComparator.EQUAL, Types.INTEGER));
//				criterios.add(new ItemComparator("cd_matricula_disciplina", "" + matriculaDisciplina.getCdMatriculaDisciplina(), ItemComparator.EQUAL, Types.INTEGER));
//				ResultSetMap rsmAulaMatricula = AulaMatriculaDAO.find(criterios, connect);
//				if(!rsmAulaMatricula.next()){
//					AulaMatricula aulaMatricula = new AulaMatricula(matriculaDisciplina.getCdMatriculaDisciplina(), aula.getCdAula(), 1, null);
//					if(AulaMatriculaDAO.insert(aulaMatricula, connect) <= 0){
//						if(isConnectionNull)
//							Conexao.rollback(connect);
//						return new Result(-1, "Erro ao salvar aula matricula");
//					}
//				}
//			}
			if(aulasMatricula != null && aulasMatricula.size() > 0){
				for(AulaMatricula aulaMatricula : aulasMatricula){
					
					if(aulaMatricula.getCdAula() == 0)
						aulaMatricula.setCdAula(aula.getCdAula());
					
					Result result = AulaMatriculaServices.save(aulaMatricula, connect);
					if(result.getCode() < 0) {
						if(isConnectionNull)
							Conexao.rollback(connect);
						return new Result(-1, "Erro ao salvar aula matricula");
					}
				}
			}
			else{
				ResultSetMap rsmMatriculas = InstituicaoServices.getAllMatriculasAtivas(turma.getCdInstituicao(), turma.getCdPeriodoLetivo(), turma.getCdTurma(), true, true, true, connect);
				while(rsmMatriculas.next()){
					AulaMatricula aulaMatricula = new AulaMatricula(aula.getCdAula(), 0, "", rsmMatriculas.getInt("cd_matricula"));
					Result result = AulaMatriculaServices.save(aulaMatricula, connect);
					if(result.getCode() < 0) {
						if(isConnectionNull)
							Conexao.rollback(connect);
						return new Result(-1, "Erro ao salvar aula matricula");
					}
				}
			}
			
			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();

			Result result = new Result((retorno>=0 ? aula.getCdAula() : retorno), (retorno<=0)?"Erro ao salvar...":"Salvo com sucesso...", "AULA", aula);
			return result;
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
	public static Result remove(int cdAula){
		return remove(cdAula, false, null);
	}
	public static Result remove(int cdAula, boolean cascade){
		return remove(cdAula, cascade, null);
	}
	public static Result remove(int cdAula, boolean cascade, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			int retorno = 0;
			if(cascade){
				connect.prepareStatement("DELETE FROM acd_aula_matricula WHERE cd_aula = " + cdAula).executeUpdate();
				
				connect.prepareStatement("DELETE FROM acd_aula_topico WHERE cd_aula = " + cdAula).executeUpdate();
				
				retorno = 1;
			}
			
			if(!cascade || retorno>0)
				retorno = AulaDAO.delete(cdAula, connect);
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
			pstmt = connect.prepareStatement("SELECT * FROM acd_aula");
				return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AulaServices.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! AulaServices.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	/**
	 * Metodo para buscar as aulas por oferta
	 * @return
	 */
	public static ResultSetMap getAllByOferta(int cdOferta) {
		return getAllByOferta(cdOferta, null);
	}
	
	public static ResultSetMap getAllByOferta(int cdOferta, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM acd_aula A "
					+ "						    LEFT OUTER JOIN acd_oferta_horario B ON (A.cd_horario = B.cd_horario AND A.cd_oferta = B.cd_oferta) "
					+ "						  WHERE A.cd_oferta = " + cdOferta
					+ " 					  ORDER BY dt_aula DESC");
			ResultSetMap rsm =  new ResultSetMap(pstmt.executeQuery());
			
			while(rsm.next()){
				
				//Acrescenta algo na string caso seja aula de reposição
				if(rsm.getInt("lg_reposicao") == 0){
					rsm.setValueToField("NM_DT_AULA", Util.convCalendarString3(rsm.getGregorianCalendar("dt_aula")));
					if(rsm.getGregorianCalendar("hr_inicio") != null && rsm.getGregorianCalendar("hr_termino") != null)
						rsm.setValueToField("CL_HORARIO", Util.convCalendarStringHourMinute(rsm.getGregorianCalendar("hr_inicio")) + " - " + Util.convCalendarStringHourMinute(rsm.getGregorianCalendar("hr_termino")));
					else
						rsm.setValueToField("CL_HORARIO", "");
					if(rsm.getGregorianCalendar("dt_aula") != null)
						rsm.setValueToField("CL_DIA_SEMANA", InstituicaoHorarioServices.diasSemana[rsm.getGregorianCalendar("dt_aula").get(Calendar.DAY_OF_WEEK)-1]);
					else
						rsm.setValueToField("CL_DIA_SEMANA", "");
				}
				else{
					rsm.setValueToField("NM_DT_AULA", Util.convCalendarString3(rsm.getGregorianCalendar("dt_aula")) + " (reposição)");
					rsm.setValueToField("CL_HORARIO", Util.convCalendarStringHourMinute(rsm.getGregorianCalendar("hr_inicio_reposicao")) + " - " + Util.convCalendarStringHourMinute(rsm.getGregorianCalendar("hr_termino_reposicao")));
					rsm.setValueToField("CL_DIA_SEMANA", InstituicaoHorarioServices.diasSemana[rsm.getGregorianCalendar("dt_aula").get(Calendar.DAY_OF_WEEK)-1]);
				}
				
				rsm.setValueToField("CL_ST_AULA", (rsm.getInt("st_aula") >= 0 && rsm.getInt("st_aula") <= 3 ? situacoesAula[rsm.getInt("st_aula")] : "Nenhuma"));
				
				ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
				criterios.add(new ItemComparator("A.cd_aula", rsm.getString("cd_aula"), ItemComparator.EQUAL, Types.INTEGER));
				criterios.add(new ItemComparator("lg_presenca", "0", ItemComparator.EQUAL, Types.INTEGER));
				ResultSetMap rsmFaltantes = AulaMatriculaServices.find(criterios, connect);
				
				rsm.setValueToField("NR_FALTANTES", (rsm.getInt("st_aula") == ST_CANCELADA ? 0 : rsmFaltantes.size()));
				rsm.setValueToField("CL_FALTANTES", (rsm.getInt("st_aula") == ST_CANCELADA ? " - " : (rsmFaltantes.size() == 0 ? "Nenhum" : rsmFaltantes.size())));
				
				
			}
			rsm.beforeFirst();
			
			return rsm;
			
			
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AulaServices.getAllByOferta: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! AulaServices.getAllByOferta: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	/**
	 * Metodo para buscar as aulas por turma
	 * @return
	 */
	public static ResultSetMap getAllByTurma(int cdTurma) {
		return getAllByOferta(cdTurma, null);
	}
	
	public static ResultSetMap getAllByTurma(int cdTurma, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM acd_aula A "
					+ "						    LEFT OUTER JOIN acd_oferta_horario B ON (A.cd_horario = B.cd_horario AND A.cd_oferta = B.cd_oferta) "
					+ "						  WHERE A.cd_turma = " + cdTurma
					+ " 					  ORDER BY dt_aula DESC");
			ResultSetMap rsm =  new ResultSetMap(pstmt.executeQuery());
			
			while(rsm.next()){
				
				//Acrescenta algo na string caso seja aula de reposi��o
				if(rsm.getInt("lg_reposicao") == 0){
					rsm.setValueToField("DT_NM_AULA", Util.convCalendarString3(rsm.getGregorianCalendar("dt_aula")));
					if(rsm.getGregorianCalendar("hr_inicio") != null && rsm.getGregorianCalendar("hr_termino") != null)
						rsm.setValueToField("CL_HORARIO", Util.convCalendarStringHourMinute(rsm.getGregorianCalendar("hr_inicio")) + " - " + Util.convCalendarStringHourMinute(rsm.getGregorianCalendar("hr_termino")));
					else
						rsm.setValueToField("CL_HORARIO", "");
					if(rsm.getGregorianCalendar("dt_aula") != null)
						rsm.setValueToField("CL_DIA_SEMANA", InstituicaoHorarioServices.diasSemana[rsm.getGregorianCalendar("dt_aula").get(Calendar.DAY_OF_WEEK)-1]);
					else
						rsm.setValueToField("CL_DIA_SEMANA", "");
				}
				else{
					rsm.setValueToField("DT_NM_AULA", Util.convCalendarString3(rsm.getGregorianCalendar("dt_aula")) + " (reposi��o)");
					rsm.setValueToField("CL_HORARIO", Util.convCalendarStringHourMinute(rsm.getGregorianCalendar("hr_inicio_reposicao")) + " - " + Util.convCalendarStringHourMinute(rsm.getGregorianCalendar("hr_termino_reposicao")));
					rsm.setValueToField("CL_DIA_SEMANA", InstituicaoHorarioServices.diasSemana[rsm.getGregorianCalendar("dt_aula").get(Calendar.DAY_OF_WEEK)-1]);
				}
				
				rsm.setValueToField("CL_ST_AULA", (rsm.getInt("st_aula") >= 0 && rsm.getInt("st_aula") <= 3 ? situacoesAula[rsm.getInt("st_aula")] : "Nenhuma"));
				
				ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
				criterios.add(new ItemComparator("A.cd_aula", rsm.getString("cd_aula"), ItemComparator.EQUAL, Types.INTEGER));
				criterios.add(new ItemComparator("lg_presenca", "0", ItemComparator.EQUAL, Types.INTEGER));
				ResultSetMap rsmFaltantes = AulaMatriculaServices.find(criterios, connect);
				
				rsm.setValueToField("NR_FALTANTES", (rsm.getInt("st_aula") == ST_CANCELADA ? 0 : rsmFaltantes.size()));
				rsm.setValueToField("CL_FALTANTES", (rsm.getInt("st_aula") == ST_CANCELADA ? " - " : (rsmFaltantes.size() == 0 ? "Nenhum" : rsmFaltantes.size())));
				
				
			}
			rsm.beforeFirst();
			
			return rsm;
			
			
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AulaServices.getAllByOferta: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! AulaServices.getAllByOferta: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	/**
	 * Metodo que retorna quantidade de aulas realizadas por oferta
	 * @return quantidade de aula dadas
	 */
	public static int getQtdAulasRealizadasByOferta(int cdOferta) {
		int quantidade = 0;
		
		ResultSetMap rsm = getAllByOferta(cdOferta);
		while (rsm.next()) {
			if(rsm.getInt("ST_AULA")==ST_FECHADA)
				quantidade++;
		}

		return quantidade;
	}
	
	public static ResultSetMap getAulasByAlunoPeriodo(int cdAluno, int cdInstituicao, int tpPeriodo, GregorianCalendar dtAtual) {
		return getAulasByAlunoPeriodo(cdAluno, cdInstituicao, tpPeriodo, dtAtual, null);
	}
	
	/**
	 * Busca todas as aulas de determina aluno
	 * @param cdAluno
	 * @param cdInstituicao
	 * @param tpPeriodo
	 * @param dtAtual
	 * @param connect
	 * @return
	 */
	public static ResultSetMap getAulasByAlunoPeriodo(int cdAluno, int cdInstituicao, int tpPeriodo, GregorianCalendar dtAtual, Connection connect) {
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			ResultSetMap rsmAgrupado = new ResultSetMap();
			
			ResultSetMap rsmMatriculas = MatriculaServices.getAllByAluno(cdAluno, connect);
			while(rsmMatriculas.next()){
				ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
				criterios.add(new ItemComparator("T.cd_turma", "" + rsmMatriculas.getInt("cd_turma"), ItemComparator.EQUAL, Types.INTEGER));
				criterios.add(new ItemComparator("O.st_oferta", "" + OfertaServices.ST_ATIVO, ItemComparator.EQUAL, Types.INTEGER));
				criterios.add(new ItemComparator("A.st_aula", "" + ST_EM_ABERTO + ", " + ST_FECHADA, ItemComparator.IN, Types.INTEGER));
				
				//Busca as aulas a partir do tipo de periodo passado
				switch(tpPeriodo){
					case 0://Mensal
						GregorianCalendar primeiroDiaMes = (GregorianCalendar)dtAtual.clone();
						primeiroDiaMes.set(Calendar.DAY_OF_MONTH, primeiroDiaMes.getActualMinimum(Calendar.DAY_OF_MONTH));
						primeiroDiaMes.set(Calendar.HOUR_OF_DAY, 0);
						primeiroDiaMes.set(Calendar.MINUTE, 0);
						primeiroDiaMes.set(Calendar.SECOND, 0);
						criterios.add(new ItemComparator("A.dt_aula", Util.formatDateTime(primeiroDiaMes, "dd/MM/yyyy HH:mm:ss"), ItemComparator.GREATER_EQUAL, Types.TIMESTAMP));
						
						GregorianCalendar ultimoDiaMes = (GregorianCalendar)dtAtual.clone();
						ultimoDiaMes.set(Calendar.DAY_OF_MONTH, ultimoDiaMes.getActualMaximum(Calendar.DAY_OF_MONTH));
						ultimoDiaMes.set(Calendar.HOUR_OF_DAY, 23);
						ultimoDiaMes.set(Calendar.MINUTE, 59);
						ultimoDiaMes.set(Calendar.SECOND, 59);
						criterios.add(new ItemComparator("A.dt_aula", Util.formatDateTime(ultimoDiaMes, "dd/MM/yyyy HH:mm:ss"), ItemComparator.MINOR_EQUAL, Types.TIMESTAMP));
						
					break;
					
					case 1://Semanal
						GregorianCalendar primeiroDiaSemana = (GregorianCalendar)dtAtual.clone();
						primeiroDiaSemana.set(Calendar.DAY_OF_MONTH, primeiroDiaSemana.getActualMinimum(Calendar.DAY_OF_WEEK));
						primeiroDiaSemana.set(Calendar.HOUR_OF_DAY, 0);
						primeiroDiaSemana.set(Calendar.MINUTE, 0);
						primeiroDiaSemana.set(Calendar.SECOND, 0);
						criterios.add(new ItemComparator("A.dt_aula", Util.formatDateTime(primeiroDiaSemana, "dd/MM/yyyy HH:mm:ss"), ItemComparator.GREATER_EQUAL, Types.TIMESTAMP));
						
						GregorianCalendar ultimoDiaSemana = (GregorianCalendar)dtAtual.clone();
						ultimoDiaSemana.set(Calendar.DAY_OF_MONTH, ultimoDiaSemana.getActualMaximum(Calendar.DAY_OF_WEEK));
						ultimoDiaSemana.set(Calendar.HOUR_OF_DAY, 23);
						ultimoDiaSemana.set(Calendar.MINUTE, 59);
						ultimoDiaSemana.set(Calendar.SECOND, 59);
						criterios.add(new ItemComparator("A.dt_aula", Util.formatDateTime(ultimoDiaSemana, "dd/MM/yyyy HH:mm:ss"), ItemComparator.MINOR_EQUAL, Types.TIMESTAMP));
					break;
					
					case 2://Diário
						GregorianCalendar primeiroDia = (GregorianCalendar)dtAtual.clone();
						primeiroDia.set(Calendar.HOUR_OF_DAY, 0);
						primeiroDia.set(Calendar.MINUTE, 0);
						primeiroDia.set(Calendar.SECOND, 0);
						criterios.add(new ItemComparator("A.dt_aula", Util.formatDateTime(primeiroDia, "dd/MM/yyyy HH:mm:ss"), ItemComparator.GREATER_EQUAL, Types.TIMESTAMP));
						
						GregorianCalendar ultimoDia = (GregorianCalendar)dtAtual.clone();
						ultimoDia.set(Calendar.HOUR_OF_DAY, 23);
						ultimoDia.set(Calendar.MINUTE, 59);
						ultimoDia.set(Calendar.SECOND, 59);
						criterios.add(new ItemComparator("A.dt_aula", Util.formatDateTime(ultimoDia, "dd/MM/yyyy HH:mm:ss"), ItemComparator.MINOR_EQUAL, Types.TIMESTAMP));
					break;
				}
			
				
				ResultSetMap rsm = find(criterios, null);
				
				ArrayList<String> orderBy = new ArrayList<String>();
				orderBy.add("NM_INSTITUICAO");
				rsm.orderBy(orderBy);
		
				String nmInstituicao = "";
				ArrayList<HashMap<String, Object>> aulas = new ArrayList<HashMap<String, Object>>();
				
				while(rsm.next()){
					
					//Usa uma variavel diferente de hora caso seja reposição
					if(rsm.getInt("lg_reposicao") == 0)
						rsm.setValueToField("NR_HORARIO_INICIO", rsm.getDateFormat("HR_INICIO", "HH:mm"));
					else
						rsm.setValueToField("NR_HORARIO_INICIO", rsm.getDateFormat("HR_INICIO_REPOSICAO", "HH:mm"));
					
					if(cdInstituicao != 0 && cdInstituicao != rsm.getInt("CD_INSTITUICAO")){
						HashMap<String, Object> register = new HashMap<String, Object>();
		
						register.put("NM_INSTITUICAO", nmInstituicao);
						register.put("CD_INSTITUICAO", cdInstituicao);	
						register.put("AULAS", aulas);
						
						rsmAgrupado.addRegister(register);
						
						aulas = new ArrayList<HashMap<String,Object>>();
						
										
					} else {
						aulas.add(rsm.getRegister());
					}
		
					cdInstituicao = rsm.getInt("CD_INSTITUICAO");
					nmInstituicao = rsm.getString("NM_FANTASIA");
				}
				rsm.beforeFirst();
				
				HashMap<String, Object> register = new HashMap<String, Object>();
				register.put("NM_INSTITUICAO", nmInstituicao);
				register.put("CD_INSTITUICAO", cdInstituicao);
				register.put("AULAS", aulas);
				
				rsmAgrupado.addRegister(register);
				
				rsmAgrupado.beforeFirst();	
			}
			
			
			return rsmAgrupado;
			
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AulaServices.getAulasByAlunoPeriodo: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! AulaServices.getAulasByAlunoPeriodo: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	
	public static ResultSetMap getAulasByProfessor(int cdProfessor, int cdInstituicao) {
		return getAulasByProfessor(cdProfessor, cdInstituicao, null);
	}
	
	public static ResultSetMap getAulasByProfessor(int cdProfessor, int cdInstituicao, Connection connect) {
		return getAulasByProfessorPeriodo(cdProfessor, cdInstituicao, -1, null, connect);
	}
	
	
	public static ResultSetMap getAulasByProfessorPeriodo(int cdProfessor, int cdInstituicao, int tpPeriodo, GregorianCalendar dtAtual) {
		return getAulasByProfessorPeriodo(cdProfessor, cdInstituicao, tpPeriodo, dtAtual, null);
	}
	
	/**
	 * Busca todas as aulas de um professor no periodo
	 * @param cdProfessor
	 * @param cdInstituicao
	 * @param tpPeriodo
	 * @param dtAtual
	 * @param connect
	 * @return
	 */
	public static ResultSetMap getAulasByProfessorPeriodo(int cdProfessor, int cdInstituicao, int tpPeriodo, GregorianCalendar dtAtual, Connection connect) {
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
			criterios.add(new ItemComparator("A.cd_professor", "" + cdProfessor, ItemComparator.EQUAL, Types.INTEGER));
			criterios.add(new ItemComparator("T.cd_instituicao", "" + cdInstituicao, ItemComparator.EQUAL, Types.INTEGER));
			
			//Busca a faixa de aulas a partir do tipo de periodo passado
			switch(tpPeriodo){
				case 0://Mensal
					GregorianCalendar primeiroDiaMes = (GregorianCalendar)dtAtual.clone();
					primeiroDiaMes.set(Calendar.DAY_OF_MONTH, primeiroDiaMes.getActualMinimum(Calendar.DAY_OF_MONTH));
					primeiroDiaMes.set(Calendar.HOUR_OF_DAY, 0);
					primeiroDiaMes.set(Calendar.MINUTE, 0);
					primeiroDiaMes.set(Calendar.SECOND, 0);
					criterios.add(new ItemComparator("A.dt_aula", Util.formatDateTime(primeiroDiaMes, "dd/MM/yyyy HH:mm:ss"), ItemComparator.GREATER_EQUAL, Types.TIMESTAMP));
					
					GregorianCalendar ultimoDiaMes = (GregorianCalendar)dtAtual.clone();
					ultimoDiaMes.set(Calendar.DAY_OF_MONTH, ultimoDiaMes.getActualMaximum(Calendar.DAY_OF_MONTH));
					ultimoDiaMes.set(Calendar.HOUR_OF_DAY, 23);
					ultimoDiaMes.set(Calendar.MINUTE, 59);
					ultimoDiaMes.set(Calendar.SECOND, 59);
					criterios.add(new ItemComparator("A.dt_aula", Util.formatDateTime(ultimoDiaMes, "dd/MM/yyyy HH:mm:ss"), ItemComparator.MINOR_EQUAL, Types.TIMESTAMP));
					
				break;
				
				case 1://Semanal
					GregorianCalendar primeiroDiaSemana = (GregorianCalendar)dtAtual.clone();
					primeiroDiaSemana.set(Calendar.DAY_OF_MONTH, primeiroDiaSemana.getActualMinimum(Calendar.DAY_OF_WEEK));
					primeiroDiaSemana.set(Calendar.HOUR_OF_DAY, 0);
					primeiroDiaSemana.set(Calendar.MINUTE, 0);
					primeiroDiaSemana.set(Calendar.SECOND, 0);
					criterios.add(new ItemComparator("A.dt_aula", Util.formatDateTime(primeiroDiaSemana, "dd/MM/yyyy HH:mm:ss"), ItemComparator.GREATER_EQUAL, Types.TIMESTAMP));
					
					GregorianCalendar ultimoDiaSemana = (GregorianCalendar)dtAtual.clone();
					ultimoDiaSemana.set(Calendar.DAY_OF_MONTH, ultimoDiaSemana.getActualMaximum(Calendar.DAY_OF_WEEK));
					ultimoDiaSemana.set(Calendar.HOUR_OF_DAY, 23);
					ultimoDiaSemana.set(Calendar.MINUTE, 59);
					ultimoDiaSemana.set(Calendar.SECOND, 59);
					criterios.add(new ItemComparator("A.dt_aula", Util.formatDateTime(ultimoDiaSemana, "dd/MM/yyyy HH:mm:ss"), ItemComparator.MINOR_EQUAL, Types.TIMESTAMP));
				break;
				
				case 2://Diário
					GregorianCalendar primeiroDia = (GregorianCalendar)dtAtual.clone();
					primeiroDia.set(Calendar.HOUR_OF_DAY, 0);
					primeiroDia.set(Calendar.MINUTE, 0);
					primeiroDia.set(Calendar.SECOND, 0);
					criterios.add(new ItemComparator("A.dt_aula", Util.formatDateTime(primeiroDia, "dd/MM/yyyy HH:mm:ss"), ItemComparator.GREATER_EQUAL, Types.TIMESTAMP));
					
					GregorianCalendar ultimoDia = (GregorianCalendar)dtAtual.clone();
					ultimoDia.set(Calendar.HOUR_OF_DAY, 23);
					ultimoDia.set(Calendar.MINUTE, 59);
					ultimoDia.set(Calendar.SECOND, 59);
					criterios.add(new ItemComparator("A.dt_aula", Util.formatDateTime(ultimoDia, "dd/MM/yyyy HH:mm:ss"), ItemComparator.MINOR_EQUAL, Types.TIMESTAMP));
				break;
			}
			
			ResultSetMap rsm = find(criterios, null);
			ResultSetMap rsmAgrupado = new ResultSetMap();
			
			ArrayList<String> orderBy = new ArrayList<String>();
			orderBy.add("NM_INSTITUICAO");
			rsm.orderBy(orderBy);
	
			String nmInstituicao = "";
			ArrayList<HashMap<String, Object>> aulas = new ArrayList<HashMap<String, Object>>();
			
			while(rsm.next()){
				if(rsm.getInt("lg_reposicao") == 0)
					rsm.setValueToField("NR_HORARIO_INICIO", rsm.getDateFormat("HR_INICIO", "HH:mm"));
				else
					rsm.setValueToField("NR_HORARIO_INICIO", rsm.getDateFormat("HR_INICIO_REPOSICAO", "HH:mm"));
				
				if(cdInstituicao != 0 && cdInstituicao != rsm.getInt("CD_INSTITUICAO")){
					HashMap<String, Object> register = new HashMap<String, Object>();
	
					register.put("NM_INSTITUICAO", nmInstituicao);
					register.put("CD_INSTITUICAO", cdInstituicao);	
					register.put("AULAS", aulas);
					
					rsmAgrupado.addRegister(register);
					
					aulas = new ArrayList<HashMap<String,Object>>();
					
									
				} else {
					aulas.add(rsm.getRegister());
				}
	
				cdInstituicao = rsm.getInt("CD_INSTITUICAO");
				nmInstituicao = rsm.getString("NM_FANTASIA");
			}
			rsm.beforeFirst();
			
			HashMap<String, Object> register = new HashMap<String, Object>();
			register.put("NM_INSTITUICAO", nmInstituicao);
			register.put("CD_INSTITUICAO", cdInstituicao);
			register.put("AULAS", aulas);
			
			rsmAgrupado.addRegister(register);
			
			rsmAgrupado.beforeFirst();		
			
			
			return rsmAgrupado;
		
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AulaServices.getAulasByProfessor: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! AulaServices.getAulasByProfessor: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static Result getAula(int cdProfessor, int cdDisciplina, int cdTurma, int cdHorario, GregorianCalendar dtAula) {
		return getAula(cdProfessor, cdDisciplina, cdTurma, cdHorario, dtAula, null);
	}
	
	/**
	 * Busca a aula a partir dos parametros passados
	 * @param cdProfessor
	 * @param cdDisciplina
	 * @param cdTurma
	 * @param cdHorario
	 * @param dtAula
	 * @param connect
	 * @return
	 */
	public static Result getAula(int cdProfessor, int cdDisciplina, int cdTurma, int cdHorario, GregorianCalendar dtAula, Connection connect) {
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			GregorianCalendar dtAulaI = (GregorianCalendar)dtAula.clone();
			dtAulaI.set(Calendar.HOUR_OF_DAY, 0);
			dtAulaI.set(Calendar.MINUTE, 0);
			dtAulaI.set(Calendar.SECOND, 0);
			dtAulaI.set(Calendar.MILLISECOND, 0);
			
			GregorianCalendar dtAulaF = (GregorianCalendar)dtAula.clone();
			dtAulaF.set(Calendar.HOUR_OF_DAY, 23);
			dtAulaF.set(Calendar.MINUTE, 59);
			dtAulaF.set(Calendar.SECOND, 59);
			
			
			ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
			criterios.add(new ItemComparator("A.cd_professor", "" + cdProfessor, ItemComparator.EQUAL, Types.INTEGER));
			if(cdDisciplina > 0)
				criterios.add(new ItemComparator("A.cd_disciplina", "" + cdDisciplina, ItemComparator.EQUAL, Types.INTEGER));
			criterios.add(new ItemComparator("A.cd_turma", "" + cdTurma, ItemComparator.EQUAL, Types.INTEGER));
			if(cdHorario > 0)
				criterios.add(new ItemComparator("A.cd_horario", "" + cdHorario, ItemComparator.EQUAL, Types.INTEGER));
			criterios.add(new ItemComparator("A.dt_aula", "" + Util.convCalendarStringSqlCompleto(dtAulaI), ItemComparator.GREATER_EQUAL, Types.VARCHAR));
			criterios.add(new ItemComparator("A.dt_aula", "" + Util.convCalendarStringSqlCompleto(dtAulaF), ItemComparator.MINOR_EQUAL, Types.VARCHAR));
			ResultSetMap rsm = find(criterios, connect);
			Result result = new Result(1);
			if(rsm.next()){
				PreparedStatement pstmt = connect.prepareStatement("SELECT A.*, B.*, D.nm_pessoa as NM_ALUNO, E.nm_turma, E.cd_turma, F.nm_produto_servico AS NM_CURSO FROM acd_matricula A"
						+ "												JOIN acd_aula_matricula B ON(A.cd_matricula = B.cd_matricula AND B.cd_aula = " + rsm.getInt("cd_aula") + ") "
						+ "												JOIN grl_pessoa D ON (A.cd_aluno = D.cd_pessoa) "
						+ "												JOIN acd_turma E ON (A.cd_turma = E.cd_turma) "
						+ "												JOIN grl_produto_servico F ON (A.cd_curso = F.cd_produto_servico) "
						+ "											WHERE A.cd_turma = " + cdTurma
						+ (cdDisciplina > 0 ? "						  AND A.cd_disciplina = " + cdDisciplina : "") 
						+ "											  AND A.st_matricula = " + MatriculaServices.ST_ATIVA
						+ "											ORDER BY D.nm_pessoa");
				ResultSetMap rsmMatriculas = new ResultSetMap(pstmt.executeQuery());
				
				result.addObject("RSM_ALUNOS", rsmMatriculas);
				Aula aula = AulaDAO.get(rsm.getInt("cd_aula"), connect);
				result.addObject("AULA", aula );
			}
			else{
				Turma turma = TurmaDAO.get(cdTurma, connect);
				
				ResultSetMap rsmMatriculas = InstituicaoServices.getAllMatriculasAtivas(turma.getCdInstituicao(), turma.getCdPeriodoLetivo(), cdTurma, true, false, true, connect);
				while(rsmMatriculas.next()){
					rsmMatriculas.setValueToField("CD_AULA", 0);
					rsmMatriculas.setValueToField("LG_PRESENCA", 0);
					rsmMatriculas.setValueToField("TXT_OBSERVACAO", "");
				}
				rsmMatriculas.beforeFirst();
				
				result.addObject("RSM_ALUNOS", rsmMatriculas);
				result.addObject("AULA", null);
			}
			
			return result;
		
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AulaServices.getAllByOferta: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! AulaServices.getAllByOferta: " + e);
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
		ResultSetMap rsm = Search.find("SELECT A.*, C.cd_curso, T.nm_turma, T.tp_turno, T.cd_instituicao, T.cd_instituicao AS cd_empresa, IP.nm_pessoa AS nm_instituicao, IP.nm_pessoa AS nm_fantasia, "
				+ "						  PROFP.nm_pessoa AS nm_professor, DPS.nm_produto_servico, IH.nr_dia_semana, "
				+ "						  IH.hr_inicio, IH.hr_termino, (CTE.nm_etapa || ' - ' || CPS.nm_produto_servico) AS nm_curso_completo, CPS.nm_produto_servico,"
				+ "						  TIP.cd_periodo_letivo, TIP.nm_periodo_letivo FROM acd_aula A "
				+ "			  JOIN acd_turma T ON (A.cd_turma = T.cd_turma) "
				+ "			  JOIN acd_instituicao I ON (T.cd_instituicao = I.cd_instituicao) "
				+ "			  JOIN grl_pessoa IP ON (I.cd_instituicao = IP.cd_pessoa) "
				+ "			  JOIN acd_professor PROF ON (A.cd_professor = PROF.cd_professor) "
				+ "			  JOIN grl_pessoa PROFP ON (PROF.cd_professor = PROFP.cd_pessoa) "
				+ "			  LEFT OUTER JOIN acd_disciplina D ON (A.cd_disciplina = D.cd_disciplina) "
				+ "			  LEFT OUTER JOIN grl_produto_servico DPS ON (D.cd_disciplina = DPS.cd_produto_servico) "
				+ "			  JOIN acd_curso C ON (T.cd_curso = C.cd_curso) "
				+ "			  JOIN grl_produto_servico CPS ON (C.cd_curso = CPS.cd_produto_servico) "
				+ "			  JOIN acd_curso_etapa CE ON (C.cd_curso = CE.cd_curso) "
				+ "			  JOIN acd_tipo_etapa CTE ON (CE.cd_etapa = CTE.cd_etapa) "
				+ "			  JOIN acd_oferta O ON (A.cd_oferta = O.cd_oferta) "
				+ "			  LEFT OUTER JOIN acd_oferta_horario IH ON (A.cd_horario = IH.cd_horario AND A.cd_oferta = IH.cd_oferta) "
				+ "			  JOIN acd_instituicao_periodo TIP ON (T.cd_periodo_letivo = TIP.cd_periodo_letivo) ", " ORDER BY A.dt_aula DESC, IH.hr_inicio ", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
		
		while(rsm.next()){
			
			rsm.setValueToField("NM_ST_AULA", situacoesAula[rsm.getInt("st_aula")]);
			
			GregorianCalendar dtAula = rsm.getGregorianCalendar("dt_aula");
			//if(dtAula != null)
			//	rsm.setValueToField("DT_NM_AULA", (Integer.parseInt(String.valueOf(dtAula.get(Calendar.DAY_OF_MONTH))) > 9 ? dtAula.get(Calendar.DAY_OF_MONTH) : "0" + dtAula.get(Calendar.DAY_OF_MONTH)) + "/" + (Integer.parseInt(String.valueOf(dtAula.get(Calendar.MONTH))) + 1 > 9 ? (Integer.parseInt(String.valueOf(dtAula.get(Calendar.MONTH))) + 1) : "0" + (Integer.parseInt(String.valueOf(dtAula.get(Calendar.MONTH))) + 1)) + "/" + dtAula.get(Calendar.YEAR));
			
			
			if(rsm.getInt("lg_reposicao") == 0){
				//rsm.setValueToField("CL_HORARIO", InstituicaoHorarioServices.diasSemana[rsm.getInt("nr_dia_semana")] + " - " + Util.formatDateTime(rsm.getGregorianCalendar("hr_inicio"), "HH:mm") + " - " + Util.formatDateTime(rsm.getGregorianCalendar("hr_termino"), "HH:mm"));
				//rsm.setValueToField("CL_HORARIO_SIMPLES", Util.formatDateTime(rsm.getGregorianCalendar("hr_inicio"), "HH:mm") + " - " + Util.formatDateTime(rsm.getGregorianCalendar("hr_termino"), "HH:mm"));
			}
			else{
				//rsm.setValueToField("CL_HORARIO", InstituicaoHorarioServices.diasSemana[rsm.getGregorianCalendar("dt_aula").get(Calendar.DAY_OF_WEEK)-1] + " - " + Util.formatDateTime(rsm.getGregorianCalendar("hr_inicio_reposicao"), "HH:mm") + " - " + Util.formatDateTime(rsm.getGregorianCalendar("hr_termino_reposicao"), "HH:mm"));
				//rsm.setValueToField("CL_HORARIO_SIMPLES", Util.formatDateTime(rsm.getGregorianCalendar("hr_inicio_reposicao"), "HH:mm") + " - " + Util.formatDateTime(rsm.getGregorianCalendar("hr_termino_reposicao"), "HH:mm"));
			}
			
			rsm.setValueToField("CL_TURNO", TurmaServices.tiposTurno[rsm.getInt("tp_turno")]);
			
			
		}
		rsm.beforeFirst();
		return rsm;
	}
	
	public static Result fechar(int cdAula) {
		return fechar(cdAula, null);
	}

	/**
	 * Faz o fechamento da aula, permitindo que tanto a frequencia, quanto outros dados da aula possam ser analisados na estatística geral
	 * @param cdAula
	 * @param connect
	 * @return
	 */
	public static Result fechar(int cdAula, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		
		try {
			Aula aula = AulaDAO.get(cdAula, connect);
			
			GregorianCalendar dtHoje = new GregorianCalendar();
			dtHoje.set(Calendar.HOUR_OF_DAY, 23);
			dtHoje.set(Calendar.MINUTE, 59);
			dtHoje.set(Calendar.SECOND, 59);
			dtHoje.set(Calendar.MILLISECOND, 59);
			
			if(aula.getDtAula().after(dtHoje)){
				if(isConnectionNull)
					Conexao.rollback(connect);
				return new Result(-1, "A data da aula ainda não foi atingida");
			}
			
			
			aula.setStAula(ST_FECHADA);
			if(AulaDAO.update(aula, connect) < 0){
				if(isConnectionNull)
					Conexao.rollback(connect);
				return new Result(-1, "Erro ao fechar aula");
			}
			
			return new Result(1, "Aula fechada com sucesso");
			
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! AulaServices.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static Result abrir(int cdAula) {
		return abrir(cdAula, null);
	}

	/**
	 * Abre uma aula fechada para revisão
	 * @param cdAula
	 * @param connect
	 * @return
	 */
	public static Result abrir(int cdAula, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		
		try {
			Aula aula = AulaDAO.get(cdAula, connect);
			aula.setStAula(ST_EM_ABERTO);
			if(AulaDAO.update(aula, connect) < 0){
				if(isConnectionNull)
					Conexao.rollback(connect);
				return new Result(-1, "Erro ao fechar aula");
			}
			
			return new Result(1, "Aula fechada com sucesso");
			
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! AulaServices.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static ResultSetMap getOfertaAvaliacaoByData(int cdTurma, int cdCurso, int cdProfessor, int cdDisciplina, GregorianCalendar dtAvaliacao) {
		return getOfertaAvaliacaoByData(cdTurma, cdCurso, cdProfessor, cdDisciplina, dtAvaliacao, null);
	}
	
	/**
	 * Busca todas as avaliações feitas em uma data
	 * @param cdTurma
	 * @param cdCurso
	 * @param cdProfessor
	 * @param cdDisciplina
	 * @param dtAvaliacao
	 * @param connect
	 * @return
	 */
	public static ResultSetMap getOfertaAvaliacaoByData(int cdTurma, int cdCurso, int cdProfessor, int cdDisciplina, GregorianCalendar dtAvaliacao, Connection connect) {
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
			criterios.add(new ItemComparator("B.cd_turma", "" + cdTurma, ItemComparator.EQUAL, Types.INTEGER));
			criterios.add(new ItemComparator("B.cd_curso", "" + cdCurso, ItemComparator.EQUAL, Types.INTEGER));
			criterios.add(new ItemComparator("B.cd_professor", "" + cdProfessor, ItemComparator.EQUAL, Types.INTEGER));
			if(cdDisciplina > 0)
				criterios.add(new ItemComparator("B.cd_disciplina", "" + cdDisciplina, ItemComparator.EQUAL, Types.INTEGER));
			criterios.add(new ItemComparator("A.dt_avaliacao", "" + Util.convCalendarStringSql(dtAvaliacao), ItemComparator.EQUAL, Types.VARCHAR));
			return OfertaAvaliacaoServices.find(criterios);
		
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AulaServices.getAllByOferta: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! AulaServices.getAllByOferta: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static ResultSetMap getAulasAbertas(int cdTurma, int cdProfessor, int cdDisciplina) {
		return getAulasAbertas(cdTurma, cdProfessor, cdDisciplina, null);
	}
	
	/**
	 * Busca todas as aulas abertas
	 * @param cdTurma
	 * @param cdProfessor
	 * @param cdDisciplina
	 * @param connect
	 * @return
	 */
	public static ResultSetMap getAulasAbertas(int cdTurma, int cdProfessor, int cdDisciplina, Connection connect) {
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			ResultSetMap rsmFinal = new ResultSetMap();
			
			ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
			criterios.add(new ItemComparator("cd_turma", "" + cdTurma, ItemComparator.EQUAL, Types.INTEGER));
			criterios.add(new ItemComparator("cd_professor", "" + cdProfessor, ItemComparator.EQUAL, Types.INTEGER));
			if(cdDisciplina > 0)
				criterios.add(new ItemComparator("cd_disciplina", "" + cdDisciplina, ItemComparator.EQUAL, Types.INTEGER));
			criterios.add(new ItemComparator("st_oferta", "" + OfertaServices.ST_ATIVO, ItemComparator.EQUAL, Types.INTEGER));
			ResultSetMap rsm = OfertaDAO.find(criterios);
			if(rsm.size() == 0){
				rsm = new ResultSetMap(connect.prepareStatement("SELECT * FROM acd_oferta O "
						+ "											WHERE O.cd_turma = " + cdTurma 
						+ " 										  AND O.st_oferta = " + OfertaServices.ST_ATIVO 
						+ " 										  AND EXISTS (SELECT * FROM acd_pessoa_oferta PO WHERE PO.cd_oferta = O.cd_oferta AND PO.cd_pessoa = "+cdProfessor+")").executeQuery());
			}
			
			
			if(rsm.next()){
				rsmFinal = new ResultSetMap(connect.prepareStatement("SELECT * FROM acd_aula A, acd_oferta_horario B WHERE A.cd_horario = B.cd_horario AND A.cd_oferta = B.cd_oferta AND A.st_aula = " + ST_EM_ABERTO + " AND A.cd_oferta = " + rsm.getInt("cd_oferta") + (cdDisciplina > 0 ? " AND A.cd_disciplina = " + cdDisciplina : "") + " AND A.cd_turma = " + cdTurma + " ORDER BY DT_AULA DESC").executeQuery());
				while(rsmFinal.next()){
					rsmFinal.setValueToField("CL_HORARIO", Util.convCalendarStringHourMinute(rsmFinal.getGregorianCalendar("hr_inicio")) + " - " + Util.convCalendarStringHourMinute(rsmFinal.getGregorianCalendar("hr_termino")));
					rsmFinal.setValueToField("CL_DIA_SEMANA", InstituicaoHorarioServices.diasSemana[rsmFinal.getInt("nr_dia_semana")]);
					rsmFinal.setValueToField("NM_DT_AULA", Util.convCalendarString3(rsmFinal.getGregorianCalendar("dt_aula")));
					rsmFinal.setValueToField("CL_AULA", rsmFinal.getString("NM_DT_AULA") + " - " + rsmFinal.getString("CL_DIA_SEMANA") + "  (" + rsmFinal.getString("CL_HORARIO") + ")");
				}
				rsmFinal.beforeFirst();
			}
			
			
			return rsmFinal;
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AulaServices.getAulasAbertas: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! AulaServices.getAulasAbertas: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static ResultSetMap getAulasCanceladas(int cdTurma, int cdProfessor, int cdDisciplina, boolean retirarRepostas) {
		return getAulasCanceladas(cdTurma, cdProfessor, cdDisciplina, retirarRepostas, null);
	}
	
	/**
	 * Busca todas as aulas canceladas
	 * @param cdTurma
	 * @param cdProfessor
	 * @param cdDisciplina
	 * @param retirarRepostas
	 * @param connect
	 * @return
	 */
	public static ResultSetMap getAulasCanceladas(int cdTurma, int cdProfessor, int cdDisciplina, boolean retirarRepostas, Connection connect) {
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			ResultSetMap rsmFinal = new ResultSetMap();
			
			ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
			criterios.add(new ItemComparator("cd_turma", "" + cdTurma, ItemComparator.EQUAL, Types.INTEGER));
			criterios.add(new ItemComparator("cd_professor", "" + cdProfessor, ItemComparator.EQUAL, Types.INTEGER));
			if(cdDisciplina > 0)
				criterios.add(new ItemComparator("cd_disciplina", "" + cdDisciplina, ItemComparator.EQUAL, Types.INTEGER));
			criterios.add(new ItemComparator("st_oferta", "" + OfertaServices.ST_ATIVO, ItemComparator.EQUAL, Types.INTEGER));
			ResultSetMap rsm = OfertaDAO.find(criterios);
			if(rsm.size() == 0){
				rsm = new ResultSetMap(connect.prepareStatement("SELECT * FROM acd_oferta O "
						+ "											WHERE O.cd_turma = " + cdTurma 
						+ " 										  AND O.st_oferta = " + OfertaServices.ST_ATIVO 
						+ " 										  AND EXISTS (SELECT * FROM acd_pessoa_oferta PO WHERE PO.cd_oferta = O.cd_oferta AND PO.cd_pessoa = "+cdProfessor+")").executeQuery());
			}
			if(rsm.next()){
				rsmFinal = new ResultSetMap(connect.prepareStatement("SELECT * FROM acd_aula A, acd_oferta_horario B "
						+ "												WHERE A.cd_horario = B.cd_horario "
						+ "												  AND A.cd_oferta = B.cd_oferta "
						+ "												  AND A.st_aula = " + ST_CANCELADA 
						+ " 											  AND A.cd_oferta = " + rsm.getInt("cd_oferta") 
						+ (cdDisciplina > 0 ? " 						  AND A.cd_disciplina = " + cdDisciplina : "") 
						+ " 											  AND A.cd_turma = " + cdTurma
						+ (retirarRepostas ? "							  AND NOT EXISTS (SELECT * FROM acd_aula AA WHERE AA.cd_aula_reposta = A.cd_aula)" : "")
						+ " 											ORDER BY DT_AULA DESC").executeQuery());
				while(rsmFinal.next()){
					rsmFinal.setValueToField("CL_HORARIO", Util.convCalendarStringHourMinute(rsmFinal.getGregorianCalendar("hr_inicio")) + " - " + Util.convCalendarStringHourMinute(rsmFinal.getGregorianCalendar("hr_termino")));
					rsmFinal.setValueToField("CL_DIA_SEMANA", InstituicaoHorarioServices.diasSemana[rsmFinal.getInt("nr_dia_semana")]);
					rsmFinal.setValueToField("NM_DT_AULA", Util.convCalendarString3(rsmFinal.getGregorianCalendar("dt_aula")));
					rsmFinal.setValueToField("CL_AULA", rsmFinal.getString("NM_DT_AULA") + " - " + rsmFinal.getString("CL_DIA_SEMANA") + "  (" + rsmFinal.getString("CL_HORARIO") + ")");
				}
				rsmFinal.beforeFirst();
			}
			
			return rsmFinal;
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AulaServices.getAulasCanceladas: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! AulaServices.getAulasAbertas: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static Result cancelar(int cdAula, String txtMotivoCancelamento) {
		return cancelar(cdAula, txtMotivoCancelamento, null);
	}

	/**
	 * Cancela uma aula, indicando o motivo do cancelamento
	 * @param cdAula
	 * @param txtMotivoCancelamento
	 * @param connect
	 * @return
	 */
	public static Result cancelar(int cdAula, String txtMotivoCancelamento, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		
		try {
			Aula aula = AulaDAO.get(cdAula, connect);
			aula.setStAula(ST_CANCELADA);
			aula.setTxtMotivoCancelamento(txtMotivoCancelamento);
			if(AulaDAO.update(aula, connect) < 0){
				if(isConnectionNull)
					Conexao.rollback(connect);
				return new Result(-1, "Erro ao fechar aula");
			}
			
			return new Result(1, "Aula cancelada com sucesso");
			
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! AulaServices.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static Result copiarPlanejamento(int cdAula) {
		return copiarPlanejamento(cdAula, null);
	}

	/**
	 * Caso o usuário deseje fazer uma aula de reposição, esse metodo ajuda a fazer a copia do planejamento da aula a ser substituida
	 * @param cdAula
	 * @param connect
	 * @return
	 */
	public static Result copiarPlanejamento(int cdAula, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		
		try {
			Aula aula = AulaDAO.get(cdAula, connect);
			Aula aulaReposta = AulaDAO.get(aula.getCdAulaReposta(), connect);
			
			aula.setTxtConteudo(aulaReposta.getTxtConteudo());
			
			if(AulaDAO.update(aula, connect) < 0){
				if(isConnectionNull)
					Conexao.rollback(connect);
				return new Result(-1, "Erro ao adicionar plano de aula em aula");
			}
			
			ResultSetMap rsmTopicos = AulaTopicoServices.getAulasTopicoByAula(aulaReposta.getCdAula(), connect);
			while(rsmTopicos.next()){
				AulaTopico aulaTopico = AulaTopicoDAO.get(rsmTopicos.getInt("cd_plano"), rsmTopicos.getInt("cd_secao"), rsmTopicos.getInt("cd_topico"), rsmTopicos.getInt("cd_aula"), connect);
				aulaTopico.setCdAula(aula.getCdAula());
				if(AulaTopicoDAO.insert(aulaTopico, connect) < 0){
					if(isConnectionNull)
						Conexao.rollback(connect);
					return new Result(-1, "Erro ao adicionar tópico em aula");
				}
			}
			
			Result result = new Result(1, "Planejamento importado com sucesso", "txtConteudo", aulaReposta.getTxtConteudo());
			result.addObject("txtTitulo", aulaReposta.getTxtTitulo());
			result.addObject("txtObjetivosAprendizagem", aulaReposta.getTxtObjetivosAprendizagem());
			result.addObject("txtObjetosConhecimento", aulaReposta.getTxtObjetosConhecimento());
			result.addObject("txtCamposExperiencia", aulaReposta.getTxtCamposExperiencia());
			result.addObject("txtProcedimentosMetodologicos", aulaReposta.getTxtProcedimentosMetodologicos());
			result.addObject("txtRecursosDidaticos", aulaReposta.getTxtRecursosDidaticos());
			result.addObject("txtAvaliacao", aulaReposta.getTxtAvaliacao());
			
			return result;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! AulaServices.copiarPlanejamento: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static ResultSetMap getObservacoes(int cdAluno, int cdProfessor) {
		return getObservacoes(cdAluno, cdProfessor, null);
	}

	/**
	 * Busca as observações do aluno passado nas aulas
	 * @param cdAluno
	 * @param cdProfessor
	 * @param connect
	 * @return
	 */
	public static ResultSetMap getObservacoes(int cdAluno, int cdProfessor, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		
		try {
			
			ResultSetMap rsm = new ResultSetMap(connect.prepareStatement("SELECT A.dt_aula, AM.txt_observacao, P_DIS.nm_produto_servico AS nm_disciplina FROM acd_aula_matricula AM"
					+ "													  JOIN acd_aula A ON (AM.cd_aula = A.cd_aula)"
					+ "												 	  JOIN acd_oferta O ON (A.cd_oferta = O.cd_oferta) "
					+ "													  JOIN acd_matricula M ON (AM.cd_matricula = M.cd_matricula)"
					+ "													  LEFT OUTER JOIN grl_produto_servico P_DIS ON (O.cd_disciplina = P_DIS.cd_produto_servico) "
					+ "														WHERE M.cd_aluno = " + cdAluno
					+ "														  AND O.st_oferta = " + OfertaServices.ST_ATIVO
					+ "														  AND (O.cd_professor = "+cdProfessor+" OR EXISTS (SELECT * FROM acd_pessoa_oferta PO "
					+ "																										WHERE PO.cd_oferta = O.cd_oferta "
					+ "																								  		  AND PO.cd_pessoa = "+cdProfessor
					+ " 																									  AND PO.st_pessoa_oferta = "+PessoaOfertaServices.ST_ATIVO+"))"
					+ "														  AND AM.txt_observacao IS NOT NULL"
					+ "														  AND AM.txt_observacao <> ''").executeQuery());
			
			while(rsm.next()){
				rsm.setValueToField("NM_DT_AULA", Util.convCalendarString3(rsm.getGregorianCalendar("dt_aula")));
			}
			rsm.beforeFirst();
			
			
			return rsm;
			
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! AulaServices.getObservacoes: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	

}
