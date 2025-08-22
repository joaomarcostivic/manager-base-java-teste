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
import com.tivic.manager.grl.ParametroServices;
import com.tivic.manager.grl.TipoOcorrenciaServices;
import com.tivic.manager.util.Util;

public class TurmaHorarioServices {

	public static Result save(TurmaHorario turmaHorario, int cdUsuario){
		return save(turmaHorario, cdUsuario, null);
	}

	public static Result save(TurmaHorario turmaHorario, int cdUsuario, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			if(turmaHorario==null)
				return new Result(-1, "Erro ao salvar. TurmaHorario é nulo");

			int retorno;
			if(TurmaHorarioDAO.get(turmaHorario.getCdHorario(), turmaHorario.getCdTurma())==null){
				retorno = TurmaHorarioDAO.insert(turmaHorario, connect);
			}
			else {
				retorno = TurmaHorarioDAO.update(turmaHorario, connect);
			}

			int cdTipoOcorrencia = TipoOcorrenciaServices.getById(InstituicaoEducacensoServices.TP_OCORRENCIA_ADICIONAR_HORARIO_TURMA, connect).getCdTipoOcorrencia();
			
			Turma turma = TurmaDAO.get(turmaHorario.getCdTurma(), connect);
			Curso curso = CursoDAO.get(turma.getCdCurso(), connect);
			InstituicaoHorario instituicaoHorario = InstituicaoHorarioDAO.get(turmaHorario.getCdHorario(), connect);
			Instituicao instituicao = InstituicaoDAO.get(turma.getCdInstituicao(), connect);
			
			OcorrenciaTurma ocorrencia = new OcorrenciaTurma(0, 0, "Adicionado horário: " + Util.convCalendarStringHourMinute(instituicaoHorario.getHrInicio()) + " - " + Util.convCalendarStringHourMinute(instituicaoHorario.getHrTermino()) + " ("+InstituicaoHorarioServices.diasSemana[instituicaoHorario.getNrDiaSemana()]+") da Turma " + curso.getNmProdutoServico() + " - " + turma.getNmTurma() +" da escola " + instituicao.getNmPessoa(), Util.getDataAtual(), cdTipoOcorrencia, OcorrenciaServices.ST_CONCLUIDO, 0, cdUsuario, turma.getCdTurma(), Util.getDataAtual(), cdUsuario, turma.getStTurma(), turma.getStTurma());
			OcorrenciaTurmaServices.save(ocorrencia, connect);
			
			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();

			return new Result(retorno, (retorno<=0)?"Erro ao salvar...":"Salvo com sucesso...", "TURMAHORARIO", turmaHorario);
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
	public static Result remove(int cdHorario, int cdTurma, int cdUsuario){
		return remove(cdHorario, cdTurma, cdUsuario, false, null);
	}
	public static Result remove(int cdHorario, int cdTurma, int cdUsuario, boolean cascade){
		return remove(cdHorario, cdTurma, cdUsuario, cascade, null);
	}
	public static Result remove(int cdHorario, int cdTurma, int cdUsuario, boolean cascade, Connection connect){
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
			TurmaHorario turmaHorario = TurmaHorarioDAO.get(cdHorario, cdTurma, connect);
			
			if(!cascade || retorno>0)
				retorno = TurmaHorarioDAO.delete(cdHorario, cdTurma, connect);
			
			int cdTipoOcorrencia = TipoOcorrenciaServices.getById(InstituicaoEducacensoServices.TP_OCORRENCIA_REMOVER_HORARIO_TURMA, connect).getCdTipoOcorrencia();
			
			Turma turma = TurmaDAO.get(turmaHorario.getCdTurma(), connect);
			Curso curso = CursoDAO.get(turma.getCdCurso(), connect);
			InstituicaoHorario instituicaoHorario = InstituicaoHorarioDAO.get(turmaHorario.getCdHorario(), connect);
			Instituicao instituicao = InstituicaoDAO.get(turma.getCdInstituicao(), connect);
			
			OcorrenciaTurma ocorrencia = new OcorrenciaTurma(0, 0, "Removido horário: " + Util.convCalendarStringHourMinute(instituicaoHorario.getHrInicio()) + " - " + Util.convCalendarStringHourMinute(instituicaoHorario.getHrTermino()) + " ("+InstituicaoHorarioServices.diasSemana[instituicaoHorario.getNrDiaSemana()]+") da Turma " + curso.getNmProdutoServico() + " - " + turma.getNmTurma() +" da escola " + instituicao.getNmPessoa(), Util.getDataAtual(), cdTipoOcorrencia, OcorrenciaServices.ST_CONCLUIDO, 0, cdUsuario, turma.getCdTurma(), Util.getDataAtual(), cdUsuario, turma.getStTurma(), turma.getStTurma());
			OcorrenciaTurmaServices.save(ocorrencia, connect);
			
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
			pstmt = connect.prepareStatement("SELECT * FROM acd_turma_horario");
				return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TurmaHorarioServices.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! TurmaHorarioServices.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ResultSetMap getAllOcupadosByInstituicao(int cdTurma, int cdInstituicao, int cdPeriodoLetivo) {
		return getAllOcupadosByInstituicao(cdTurma, cdInstituicao, cdPeriodoLetivo, null);
	}
	
	public static ResultSetMap getAllOcupadosByInstituicao(int cdTurma, int cdInstituicao, int cdPeriodoLetivo, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement("SELECT A.*, B.*, C.*, D.* "+
											 "FROM acd_turma_horario A "+
											 "JOIN acd_instituicao_horario B ON( A.cd_horario = B.cd_horario ) "+
											 "JOIN acd_oferta C ON( A.cd_turma = C.cd_turma ) "+
											 "JOIN acd_oferta_horario D ON(  C.cd_oferta = D.cd_oferta AND B.cd_horario = D.cd_horario_instituicao ) "+
											 "WHERE A.cd_turma = "+cdTurma+
											 " AND B.cd_instituicao = "+cdInstituicao+
											 " AND C.cd_periodo_letivo = " + cdPeriodoLetivo+
											 " AND B.cd_periodo_letivo = " + cdPeriodoLetivo);
			
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TurmaHorarioServices.getAllOcupadosByOferta: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! TurmaHorarioServices.getAllOcupadosByOferta: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static ResultSetMap getAllByTurma(int cdTurma) {
		return getAllByTurma(cdTurma, null);
	}

	public static ResultSetMap getAllByTurma(int cdTurma, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement("SELECT A.*, B.* FROM acd_turma_horario A "+
											 "JOIN acd_instituicao_horario B ON ( A.cd_horario = B.cd_horario ) "+
											 "JOIN acd_instituicao_periodo C ON( B.cd_instituicao = C.cd_instituicao AND C.st_periodo_letivo = "+InstituicaoPeriodoServices.ST_ATUAL+" )"+
											 "WHERE B.cd_periodo_letivo = B.cd_periodo_letivo "+ 
											 "  AND cd_turma = "+cdTurma);
			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
			return rsm;
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TurmaHorarioServices.getAllByTurmaInstituicao: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! TurmaHorarioServices.getAllByTurmaInstituicao: " + e);
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
		return Search.find("SELECT * FROM acd_turma_horario", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

	
	public static ResultSetMap getAllTurmasHorariosByInstituicaoPeriodo(int cdInstituicao, int cdPeriodoLetivo) {
		return getAllTurmasHorariosByInstituicaoPeriodo(cdInstituicao, cdPeriodoLetivo, null);
	}
	
	public static ResultSetMap getAllTurmasHorariosByInstituicaoPeriodo(int cdInstituicao, int cdPeriodoLetivo, Connection connection) {
		boolean isConnectionNull = connection==null;
		try {
			if (isConnectionNull)
				connection = Conexao.conectar();
			
			ResultSetMap rsmFinal = new ResultSetMap();
			
			PreparedStatement pstmt = connection.prepareStatement("SELECT B.*, E.* " +
					"FROM acd_turma B " +
					"LEFT OUTER JOIN acd_turma_horario A ON (A.cd_turma = B.cd_turma) " +
					"LEFT OUTER JOIN acd_instituicao_horario E ON (A.cd_horario = E.cd_horario"
					+ "												AND B.cd_instituicao = E.cd_instituicao"
					+ "												AND B.cd_periodo_letivo = E.cd_periodo_letivo) " +
					"WHERE B.st_turma <> "+ TurmaServices.ST_INATIVO +
					" AND B.nm_turma NOT LIKE 'TRANS%' " +
					" AND B.cd_instituicao = " + cdInstituicao +
					" AND B.cd_periodo_letivo = " + cdPeriodoLetivo +
					" ORDER BY B.cd_turma, E.nr_dia_semana, B.tp_turno, E.hr_inicio, E.hr_termino");
			
			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
			int cdTurma = 0;
			ArrayList<Integer> qtDiasSemana = new ArrayList<Integer>();
			boolean possuiDiasRepetidos = false;
			boolean turmaMaisEducacao = false;
			HashMap<String, String> conjuntoHorarioDia = new HashMap<String, String>();
			while(rsm.next()){
				if(cdTurma > 0 && cdTurma != rsm.getInt("cd_turma")){
					Turma turma = TurmaDAO.get(cdTurma, connection);
					Curso curso = CursoDAO.get(turma.getCdCurso(), connection);
					
					HashMap<String, Object> register = new HashMap<String, Object>();
					
					register.put("NM_CURSO", curso.getNmProdutoServico());
					register.put("NM_TURMA", turma.getNmTurma());
					register.put("NM_TP_TURNO", TurmaServices.tiposTurno[turma.getTpTurno()]);
					
					if(conjuntoHorarioDia.size() == 0){
						register.put("ST_HORARIOS", 0);
						register.put("NM_ST_HORARIOS", "Não lançado");
					}
					else{
						boolean lancadoComErros = false;
						String txtErros = "";
						if((qtDiasSemana.size() < 5 && curso.getCdCurso() != ParametroServices.getValorOfParametroAsInteger("CD_CURSO_ATENDIMENTO_ESPECIALIZADO", 0) && curso.getCdCurso() != ParametroServices.getValorOfParametroAsInteger("CD_CURSO_MAIS_EDUCACAO", 0))){
							lancadoComErros = true;
							txtErros += "Turma do regular com menos de cinco dias da semana, ";
						}
						else if(possuiDiasRepetidos && !turmaMaisEducacao){
							lancadoComErros = true;
							txtErros += "Possui dias repetidos, ";
						}
						else if(qtDiasSemana.size() > 7 && !turmaMaisEducacao){
							lancadoComErros = true;
							txtErros += "Mais de sete dias da semana, ";
						}
						
						
						if(lancadoComErros){
							register.put("ST_HORARIOS", 1);
							register.put("NM_ST_HORARIOS", "Lançado com erros: " + txtErros.substring(0, txtErros.length()-2));
						}
						else{
							register.put("ST_HORARIOS", 2);
							register.put("NM_ST_HORARIOS", "Lançado");
						}
					}
					
					String nmHorarioDia = "";
					
					for(String horario : conjuntoHorarioDia.keySet()){
						nmHorarioDia += horario + " ("+conjuntoHorarioDia.get(horario).substring(0, conjuntoHorarioDia.get(horario).length()-2)+") | ";
					}
					if(nmHorarioDia.length() > 0)
						nmHorarioDia = nmHorarioDia.substring(0, nmHorarioDia.length()-3);
					
					register.put("NM_HORARIO_DIA", nmHorarioDia);
					register.put("CD_TURMA", turma.getCdTurma());
					rsmFinal.addRegister(register);
					
					
					cdTurma = rsm.getInt("cd_turma");
					qtDiasSemana = new ArrayList<Integer>();
					possuiDiasRepetidos = false;
					turmaMaisEducacao = false;
					conjuntoHorarioDia = new HashMap<String, String>();
					
					if(TurmaHorarioDAO.get(rsm.getInt("cd_horario"), rsm.getInt("cd_turma")) != null){
						if(rsm.getInt("nr_dia_semana") == InstituicaoHorarioServices.NR_DIA_SEMANA_SEGUNDA
							|| rsm.getInt("nr_dia_semana") == InstituicaoHorarioServices.NR_DIA_SEMANA_TERCA
							|| rsm.getInt("nr_dia_semana") == InstituicaoHorarioServices.NR_DIA_SEMANA_QUARTA
							|| rsm.getInt("nr_dia_semana") == InstituicaoHorarioServices.NR_DIA_SEMANA_QUINTA
							|| rsm.getInt("nr_dia_semana") == InstituicaoHorarioServices.NR_DIA_SEMANA_SEXTA){
							
							if(turma.getCdCurso() == ParametroServices.getValorOfParametroAsInteger("CD_CURSO_MAIS_EDUCACAO", 0)){
								turmaMaisEducacao = true;
							}
							
							if(qtDiasSemana.contains(rsm.getInt("nr_dia_semana")))
								possuiDiasRepetidos = true;
							qtDiasSemana.add(rsm.getInt("nr_dia_semana"));
						}
						if(!conjuntoHorarioDia.containsKey(Util.convCalendarStringHourMinute(rsm.getGregorianCalendar("hr_inicio")) + " - " + Util.convCalendarStringHourMinute(rsm.getGregorianCalendar("hr_termino")))){
							conjuntoHorarioDia.put(Util.convCalendarStringHourMinute(rsm.getGregorianCalendar("hr_inicio")) + " - " + Util.convCalendarStringHourMinute(rsm.getGregorianCalendar("hr_termino")), "");
						}
						
						conjuntoHorarioDia.put(Util.convCalendarStringHourMinute(rsm.getGregorianCalendar("hr_inicio")) + " - " + Util.convCalendarStringHourMinute(rsm.getGregorianCalendar("hr_termino")), 
							conjuntoHorarioDia.get(Util.convCalendarStringHourMinute(rsm.getGregorianCalendar("hr_inicio")) + " - " + Util.convCalendarStringHourMinute(rsm.getGregorianCalendar("hr_termino"))) + InstituicaoHorarioServices.diasSemana[rsm.getInt("nr_dia_semana")] + ", ");
					}
					
				}
				else{
					cdTurma = rsm.getInt("cd_turma");
					Turma turma = TurmaDAO.get(cdTurma, connection);
					if(TurmaHorarioDAO.get(rsm.getInt("cd_horario"), rsm.getInt("cd_turma")) != null){
						if(rsm.getInt("nr_dia_semana") == InstituicaoHorarioServices.NR_DIA_SEMANA_SEGUNDA
							|| rsm.getInt("nr_dia_semana") == InstituicaoHorarioServices.NR_DIA_SEMANA_TERCA
							|| rsm.getInt("nr_dia_semana") == InstituicaoHorarioServices.NR_DIA_SEMANA_QUARTA
							|| rsm.getInt("nr_dia_semana") == InstituicaoHorarioServices.NR_DIA_SEMANA_QUINTA
							|| rsm.getInt("nr_dia_semana") == InstituicaoHorarioServices.NR_DIA_SEMANA_SEXTA){
							
							if(turma.getLgMaisEduca() == 1){
								turmaMaisEducacao = true;
							}
							
							if(qtDiasSemana.contains(rsm.getInt("nr_dia_semana")))
								possuiDiasRepetidos = true;
							qtDiasSemana.add(rsm.getInt("nr_dia_semana"));
						}
						
						if(!conjuntoHorarioDia.containsKey(Util.convCalendarStringHourMinute(rsm.getGregorianCalendar("hr_inicio")) + " - " + Util.convCalendarStringHourMinute(rsm.getGregorianCalendar("hr_termino")))){
							conjuntoHorarioDia.put(Util.convCalendarStringHourMinute(rsm.getGregorianCalendar("hr_inicio")) + " - " + Util.convCalendarStringHourMinute(rsm.getGregorianCalendar("hr_termino")), "");
						}
						
						conjuntoHorarioDia.put(Util.convCalendarStringHourMinute(rsm.getGregorianCalendar("hr_inicio")) + " - " + Util.convCalendarStringHourMinute(rsm.getGregorianCalendar("hr_termino")), 
							conjuntoHorarioDia.get(Util.convCalendarStringHourMinute(rsm.getGregorianCalendar("hr_inicio")) + " - " + Util.convCalendarStringHourMinute(rsm.getGregorianCalendar("hr_termino"))) + InstituicaoHorarioServices.diasSemana[rsm.getInt("nr_dia_semana")] + ", ");
					}
				}
			}
			rsm.beforeFirst();
			
			
			//ULTIMA LINHA
			Turma turma = TurmaDAO.get(cdTurma, connection);
			Curso curso = CursoDAO.get(turma.getCdCurso(), connection);
			
			HashMap<String, Object> register = new HashMap<String, Object>();
			
			register.put("NM_CURSO", curso.getNmProdutoServico());
			register.put("NM_TURMA", turma.getNmTurma());
			register.put("NM_TP_TURNO", TurmaServices.tiposTurno[turma.getTpTurno()]);
			
			if(conjuntoHorarioDia.size() == 0){
				register.put("ST_HORARIOS", 0);
				register.put("NM_ST_HORARIOS", "Não lançado");
			}
			else{
				boolean lancadoComErros = false;
				String txtErros = "";
				if((qtDiasSemana.size() < 5 && curso.getCdCurso() != ParametroServices.getValorOfParametroAsInteger("CD_CURSO_ATENDIMENTO_ESPECIALIZADO", 0) && curso.getCdCurso() != ParametroServices.getValorOfParametroAsInteger("CD_CURSO_MAIS_EDUCACAO", 0))){
					lancadoComErros = true;
					txtErros += "Turma do regular com menos de cinco dias da semana, ";
				}
				else if(possuiDiasRepetidos && !turmaMaisEducacao){
					lancadoComErros = true;
					txtErros += "Possui dias repetidos, ";
				}
				else if(qtDiasSemana.size() > 7 && !turmaMaisEducacao){
					lancadoComErros = true;
					txtErros += "Mais de sete dias da semana, ";
				}
				
				
				if(lancadoComErros){
					register.put("ST_HORARIOS", 1);
					register.put("NM_ST_HORARIOS", "Lançado com erros: " + txtErros.substring(0, txtErros.length()-2));
				}
				else{
					register.put("ST_HORARIOS", 2);
					register.put("NM_ST_HORARIOS", "Lançado");
				}
			}
			
			String nmHorarioDia = "";
			
			for(String horario : conjuntoHorarioDia.keySet()){
				nmHorarioDia += horario + " ("+conjuntoHorarioDia.get(horario).substring(0, conjuntoHorarioDia.get(horario).length()-2)+") | ";
			}
			if(nmHorarioDia.length() > 0)
				nmHorarioDia = nmHorarioDia.substring(0, nmHorarioDia.length()-3);
			
			register.put("NM_HORARIO_DIA", nmHorarioDia);
			register.put("CD_TURMA", turma.getCdTurma());
			rsmFinal.addRegister(register);
			
			
			ArrayList<String> fields = new ArrayList<String>();
			fields.add("NM_CURSO");
			fields.add("NM_TURMA");
			fields.add("NM_TP_TURNO");
			rsm.orderBy(fields);
			rsm.beforeFirst();
			
			return rsmFinal;
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
