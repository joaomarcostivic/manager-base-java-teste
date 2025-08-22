package com.tivic.manager.importacao;

import java.sql.Connection;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;

import com.tivic.manager.acd.Aula;
import com.tivic.manager.acd.AulaDAO;
import com.tivic.manager.acd.AulaMatricula;
import com.tivic.manager.acd.AulaMatriculaDAO;
import com.tivic.manager.acd.AulaMatriculaServices;
import com.tivic.manager.acd.DisciplinaAvaliacaoAluno;
import com.tivic.manager.acd.DisciplinaAvaliacaoAlunoDAO;
import com.tivic.manager.acd.OfertaAvaliacao;
import com.tivic.manager.acd.OfertaAvaliacaoDAO;
import com.tivic.sol.connection.Conexao;
import com.tivic.manager.util.Util;

import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;
import sol.util.RestData;
import sol.util.Result;

public class MobileImport {

	/**
	 * 
	 * Analisa ACD_AULA, verifica se dt_criacao é nulo ou se já existe um conjunto de cd_disciplina, cd_horario, cd_oferta, cd_turma e dt_aula e atualiza, 
	 * se não cria o registro. Guarda o novo código de cd_aula
	 * 
	 * 
	 * Analisa ACD_AULA_MATRICULA, verifica se dt_criacao é nulo e atualiza, 
	 * se não cria o registro. Busca o código novo da aula caso seja um registro criado
	 * 
	 * 
	 * Analisa ACD_OFERTA_AVALIACAO, verifica se dt_criacao é nulo e atualiza, 
	 * se não cria o registro. Guarda a tupla de cd_oferta_avaliacao e cd_oferta
	 * 
	 * 
	 * Analisa ACD_AULA, verifica se dt_criacao é nulo e atualiza, 
	 * se não cria o registro. Busca a tupla de códigos novos de oferta_avaliacao caso seja um registro criado
	 * 
	 * 
	 * 
	 * 
	 * 
	 * @param restData
	 * @return
	 */
	
	public static Result sincronizacaoMobileProfessor(RestData restData){
		return sincronizacaoMobileProfessor(restData, null);
	}
	
	public static Result sincronizacaoMobileProfessor(RestData restData, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull){
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
		
			Result result = new Result(1, "Sincronização realizada com sucesso");
	
			
			ResultSetMap rsmAula = (ResultSetMap) restData.getArg("rsmAula");
			ResultSetMap rsmAulaMatricula = (ResultSetMap) restData.getArg("rsmAulaMatricula");
			
			ResultSetMap rsmOfertaAvaliacao = (ResultSetMap) restData.getArg("rsmOfertaAvaliacao");
			ResultSetMap rsmDisciplinaAvaliacaoAluno = (ResultSetMap) restData.getArg("rsmDisciplinaAvaliacaoAluno");
			
			HashMap<Integer, Integer> codigosAula = new HashMap<Integer, Integer>();
			HashMap<ArrayList<Integer>, ArrayList<Integer>> codigosOfertaAvaliacao = new HashMap<ArrayList<Integer>, ArrayList<Integer>>();
			
			//ACD_AULA
			while(rsmAula.next()){
				
				Aula aula = (rsmAula.getGregorianCalendar("dt_criacao") == null ? AulaDAO.get(rsmAula.getInt("cd_aula"), connect) : new Aula());
				
				aula.setCdPlano(rsmAula.getInt("cd_plano"));
				aula.setCdProfessor(rsmAula.getInt("cd_professor"));
				aula.setCdProfessorSubstituto(rsmAula.getInt("cd_professor_substituto"));
				aula.setCdTipoAula(rsmAula.getInt("cd_tipo_aula"));
				aula.setDtAula(rsmAula.getGregorianCalendar("dt_aula"));
				aula.setHrInicioReposicao(rsmAula.getGregorianCalendar("hr_inicio_reposicao"));
				aula.setHrTerminoReposicao(rsmAula.getGregorianCalendar("hr_terminos_reposicao"));
				aula.setStAula(rsmAula.getInt("st_aula"));
				aula.setTxtAvaliacao(rsmAula.getString("txt_avaliacao"));
				aula.setTxtCamposExperiencia(rsmAula.getString("txt_campos_experiencia"));
				aula.setTxtConteudo(rsmAula.getString("txt_conteudo"));
				aula.setTxtMotivoCancelamento(rsmAula.getString("txt_motivos_cancelamento"));
				aula.setTxtObjetivosAprendizagem(rsmAula.getString("txt_objetivos_aprendizage"));
				aula.setTxtObjetosConhecimento(rsmAula.getString("txt_objetos_conhecimento"));
				aula.setTxtObservacao(rsmAula.getString("txt_observacao"));
				aula.setTxtProcedimentosMetodologicos(rsmAula.getString("txt_procedimentos_metodologicos"));
				aula.setTxtRecursosDidaticos(rsmAula.getString("txt_recursos_didaticos"));
				aula.setTxtTitulo(rsmAula.getString("txt_titulo"));
				
				
				GregorianCalendar dtAulaInicial = (GregorianCalendar)rsmAula.getGregorianCalendar("dt_aula").clone();
				dtAulaInicial.set(Calendar.HOUR_OF_DAY, 0);
				dtAulaInicial.set(Calendar.MINUTE, 0);
				dtAulaInicial.set(Calendar.SECOND, 0);
				dtAulaInicial.set(Calendar.MILLISECOND, 0);
				
				GregorianCalendar dtAulaTermino = (GregorianCalendar)rsmAula.getGregorianCalendar("dt_aula").clone();
				dtAulaTermino.set(Calendar.HOUR_OF_DAY, 23);
				dtAulaTermino.set(Calendar.MINUTE, 59);
				dtAulaTermino.set(Calendar.SECOND, 59);
				dtAulaTermino.set(Calendar.MILLISECOND, 59);
				
				ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
				criterios.add(new ItemComparator("cd_disciplina", rsmAula.getString("cd_disciplina"), ItemComparator.EQUAL, Types.INTEGER));
				criterios.add(new ItemComparator("cd_horario", rsmAula.getString("cd_horario"), ItemComparator.EQUAL, Types.INTEGER));
				criterios.add(new ItemComparator("cd_oferta", rsmAula.getString("cd_oferta"), ItemComparator.EQUAL, Types.INTEGER));
				criterios.add(new ItemComparator("cd_turma", rsmAula.getString("cd_turma"), ItemComparator.EQUAL, Types.INTEGER));
				criterios.add(new ItemComparator("dt_aula", "" + Util.convCalendarStringSql(dtAulaInicial), ItemComparator.GREATER_EQUAL, Types.TIMESTAMP));
				criterios.add(new ItemComparator("dt_aula", "" + Util.convCalendarStringSql(dtAulaTermino), ItemComparator.MINOR_EQUAL, Types.TIMESTAMP));
				ResultSetMap rsmAulaExistente = AulaDAO.find(criterios, connect);
				
				//Registro de Aula: Atualizacao
				if(rsmAula.getGregorianCalendar("dt_criacao") == null && rsmAulaExistente.size()==0){
					
					if(AulaDAO.update(aula, connect) < 0){
						if(isConnectionNull)
							Conexao.rollback(connect);
						return new Result(-1, "Erro na sincronização, ao atualizar tabela de aula");
					}
					
				}
				
				//Registro de Aula: Inserção
				else{
					
					if(rsmAulaExistente.next()){
						aula.setCdAulaReposta(rsmAulaExistente.getInt("cd_aula_reposta"));
						aula.setCdDisciplina(rsmAulaExistente.getInt("cd_disciplina"));
						aula.setCdHorario(rsmAulaExistente.getInt("cd_horario"));
						aula.setCdOferta(rsmAulaExistente.getInt("cd_oferta"));
						aula.setCdTurma(rsmAulaExistente.getInt("cd_turma"));
					}
					else{
						aula.setCdAulaReposta(rsmAula.getInt("cd_aula_reposta"));
						aula.setCdDisciplina(rsmAula.getInt("cd_disciplina"));
						aula.setCdHorario(rsmAula.getInt("cd_horario"));
						aula.setCdOferta(rsmAula.getInt("cd_oferta"));
						aula.setCdTurma(rsmAula.getInt("cd_turma"));
					}
					
					int cdAulaNovo = AulaDAO.insert(aula, connect);
					
					if(cdAulaNovo < 0){
						if(isConnectionNull)
							Conexao.rollback(connect);
						return new Result(-1, "Erro na sincronização, ao inserir tabela de aula");
					}
					
					codigosAula.put(rsmAula.getInt("cd_aula"), cdAulaNovo);
				}
				
				
				
			}
			
			
			//ACD_AULA_MATRICULA
			while(rsmAulaMatricula.next()){
				
				AulaMatricula aulaMatricula = (rsmAulaMatricula.getGregorianCalendar("dt_criacao") == null ? AulaMatriculaDAO.get(rsmAulaMatricula.getInt("cd_matricula_disciplina"), rsmAulaMatricula.getInt("cd_aula"), connect) : new AulaMatricula());
				
				aulaMatricula.setLgPresenca(rsmAulaMatricula.getInt("lg_presenca"));
				aulaMatricula.setTxtObservacao(rsmAulaMatricula.getString("txt_observacao"));
				
				//Registro de Aula Matricula: Atualizacao
				if(rsmAulaMatricula.getGregorianCalendar("dt_criacao") == null){
					
					if(AulaMatriculaDAO.update(aulaMatricula, connect) < 0){
						if(isConnectionNull)
							Conexao.rollback(connect);
						return new Result(-1, "Erro na sincronização, ao atualizar tabela de aula matricula");
					}
										
				}
				
				//Registro de Aula Matricula: Inserção
				else{
					aulaMatricula.setCdAula(codigosAula.get(rsmAulaMatricula.getInt("cd_aula")));
					//aulaMatricula.setCdMatriculaDisciplina(rsmAulaMatricula.getInt("cd_matricula_disciplina"));
					
					if(AulaMatriculaDAO.insert(aulaMatricula, connect) < 0){
						if(isConnectionNull)
							Conexao.rollback(connect);
						return new Result(-1, "Erro na sincronização, ao inserir tabela de aula matricula");
					}
				}
				
				
				
			}
			
			
			//ACD_OFERTA_AVALIACAO
			while(rsmOfertaAvaliacao.next()){
				
				OfertaAvaliacao ofertaAvaliacao = (rsmOfertaAvaliacao.getGregorianCalendar("dt_criacao") == null ? OfertaAvaliacaoDAO.get(rsmOfertaAvaliacao.getInt("cd_oferta_avaliacao"), rsmOfertaAvaliacao.getInt("cd_oferta"), connect) : new OfertaAvaliacao());
				
				
				ofertaAvaliacao.setCdFormulario(rsmOfertaAvaliacao.getInt("cd_formulario"));
				ofertaAvaliacao.setCdTipoAvaliacao(rsmOfertaAvaliacao.getInt("cd_tipo_avaliacao"));
				ofertaAvaliacao.setCdUnidade(rsmOfertaAvaliacao.getInt("cd_unidade"));
				ofertaAvaliacao.setDtAvaliacao(rsmOfertaAvaliacao.getGregorianCalendar("dt_avaliacao"));
				ofertaAvaliacao.setIdOfertaAvaliacao(rsmOfertaAvaliacao.getString("id_oferta_avaliacao"));
				ofertaAvaliacao.setNmAvaliacao(rsmOfertaAvaliacao.getString("nm_avaliacao"));
				ofertaAvaliacao.setStOfertaAvaliacao(rsmOfertaAvaliacao.getInt("st_oferta_avaliacao"));
				ofertaAvaliacao.setTxtObservacao(rsmOfertaAvaliacao.getString("txt_observacao"));
				ofertaAvaliacao.setVlPeso(rsmOfertaAvaliacao.getFloat("vl_peso"));
				
				//Registro de Oferta avaliação: Atualizacao
				if(rsmOfertaAvaliacao.getGregorianCalendar("dt_criacao") == null){
					
					if(OfertaAvaliacaoDAO.update(ofertaAvaliacao, connect) < 0){
						if(isConnectionNull)
							Conexao.rollback(connect);
						return new Result(-1, "Erro na sincronização, ao atualizar tabela de oferta avaliacao");
					}
					
				}
				
				//Registro de Oferta avaliação: Inserção
				else{
					ofertaAvaliacao.setCdAula(rsmOfertaAvaliacao.getInt("cd_aula"));
					ofertaAvaliacao.setCdCurso(rsmOfertaAvaliacao.getInt("cd_curso"));
					ofertaAvaliacao.setCdCursoPeriodo(rsmOfertaAvaliacao.getInt("cd_curso_periodo"));
					ofertaAvaliacao.setCdDisciplina(rsmOfertaAvaliacao.getInt("cd_disciplina"));
					ofertaAvaliacao.setCdDisciplinaAvaliacao(rsmOfertaAvaliacao.getInt("cd_disciplina_avaliacao"));
					ofertaAvaliacao.setCdMatriz(rsmOfertaAvaliacao.getInt("cd_matriz"));
					ofertaAvaliacao.setCdOferta(rsmOfertaAvaliacao.getInt("cd_oferta"));
					ofertaAvaliacao.setCdOfertaAvaliacao(rsmOfertaAvaliacao.getInt("cd_oferta_avaliacao"));
					
					int cdOfertaAvaliacaoNovo = OfertaAvaliacaoDAO.insert(ofertaAvaliacao, connect);
					
					if(cdOfertaAvaliacaoNovo < 0){
						if(isConnectionNull)
							Conexao.rollback(connect);
						return new Result(-1, "Erro na sincronização, ao inserir tabela de oferta avaliacao");
					}
					
					ArrayList<Integer> registroAntigo = new ArrayList<Integer>();
					registroAntigo.add(rsmOfertaAvaliacao.getInt("cd_oferta_avaliacao"));
					registroAntigo.add(rsmOfertaAvaliacao.getInt("cd_oferta"));
					
					ArrayList<Integer> registroNovo = new ArrayList<Integer>();
					registroNovo.add(cdOfertaAvaliacaoNovo);
					registroNovo.add(rsmOfertaAvaliacao.getInt("cd_oferta"));
					
					codigosOfertaAvaliacao.put(registroAntigo, registroNovo);
				}
				
				
				
			}
			
			
			//ACD_DISCIPLINA_AVALIACAO_ALUNO
			while(rsmDisciplinaAvaliacaoAluno.next()){
				
				DisciplinaAvaliacaoAluno disciplinaAvaliacaoAluno = (rsmDisciplinaAvaliacaoAluno.getGregorianCalendar("dt_criacao") == null ? DisciplinaAvaliacaoAlunoDAO.get(rsmDisciplinaAvaliacaoAluno.getInt("cd_matricula_disciplina"), rsmDisciplinaAvaliacaoAluno.getInt("cd_oferta_avaliacao"), rsmDisciplinaAvaliacaoAluno.getInt("cd_oferta"), connect) : new DisciplinaAvaliacaoAluno());
				
				
				disciplinaAvaliacaoAluno.setCdConceito(rsmDisciplinaAvaliacaoAluno.getInt("cd_conceito"));
				disciplinaAvaliacaoAluno.setDtAplicacao(rsmDisciplinaAvaliacaoAluno.getGregorianCalendar("dt_aplicacao"));
				disciplinaAvaliacaoAluno.setDtLancamento(rsmDisciplinaAvaliacaoAluno.getGregorianCalendar("dt_lancamento"));
				disciplinaAvaliacaoAluno.setLgSegundaChamada(rsmDisciplinaAvaliacaoAluno.getInt("lg_segunda_chamada"));
				disciplinaAvaliacaoAluno.setTxtObservacao(rsmDisciplinaAvaliacaoAluno.getString("txt_observacao"));
				disciplinaAvaliacaoAluno.setVlConceito(rsmDisciplinaAvaliacaoAluno.getFloat("vl_conceito"));
				disciplinaAvaliacaoAluno.setVlConceitoAproveitamento(rsmDisciplinaAvaliacaoAluno.getFloat("vl_conceito_aproveitamento"));
				
				//Registro de Disciplina avaliação aluno: Atualizacao
				if(rsmDisciplinaAvaliacaoAluno.getGregorianCalendar("dt_criacao") == null){
					
					if(DisciplinaAvaliacaoAlunoDAO.update(disciplinaAvaliacaoAluno, connect) < 0){
						if(isConnectionNull)
							Conexao.rollback(connect);
						return new Result(-1, "Erro na sincronização, ao atualizar tabela de disciplina avaliacao aluno");
					}
					
				}
				
				//Registro de Disciplina avaliação aluno: Inserção
				else{
					
					disciplinaAvaliacaoAluno.setCdMatricula(rsmDisciplinaAvaliacaoAluno.getInt("cd_matricula"));
					
					for(ArrayList<Integer> tupla : codigosOfertaAvaliacao.keySet()){
						if(tupla.get(0) == rsmDisciplinaAvaliacaoAluno.getInt("cd_oferta_avaliacao") 
							&& tupla.get(1) == rsmDisciplinaAvaliacaoAluno.getInt("cd_oferta")){
							
							ArrayList<Integer> tuplaReferente = codigosOfertaAvaliacao.get(tupla);
							
							disciplinaAvaliacaoAluno.setCdOfertaAvaliacao(tuplaReferente.get(0));
							disciplinaAvaliacaoAluno.setCdOferta(tuplaReferente.get(1));
							
							break;
						}
					}
					
					
					
					if(DisciplinaAvaliacaoAlunoDAO.insert(disciplinaAvaliacaoAluno, connect) < 0){
						if(isConnectionNull)
							Conexao.rollback(connect);
						return new Result(-1, "Erro na sincronização, ao inserir tabela de disciplina avaliacao aluno");
					}
					
				}
				
			}
			
			return result;
		
		
		}
		catch(Exception e) {
			e.printStackTrace(java.lang.System.out);
			Util.registerLog(e);
			return new Result(-3, "Erro ao autenticar...");
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
}
