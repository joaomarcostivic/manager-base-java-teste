package com.tivic.manager.acd;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Types;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;
import sol.dao.Search;
import sol.util.Result;
import sol.util.Util;

import com.tivic.sol.connection.Conexao;
import com.tivic.manager.grl.OcorrenciaServices;
import com.tivic.manager.grl.TipoOcorrenciaServices;


public class InstituicaoHorarioServices {

	/* tipos de turnos */
	public static final int TP_MATUTINO = 0;
	public static final int TP_VESPERTINO = 1;
	public static final int TP_NOTURNO = 2;
	public static final int TP_DIURNO = 3;
	public static final int TP_INTEGRAL = 4;
	public static final int TP_FIM_DE_SEMANA = 5;
	
	public static final String[] tiposTurno = {"Matutino",
		"Vespertino",
		"Noturno",
		"Diurno",
		"Integral",
		"Fim de Semana"};
	
	public static final int NR_DIA_SEMANA_DOMINGO = 0;
	public static final int NR_DIA_SEMANA_SEGUNDA = 1;
	public static final int NR_DIA_SEMANA_TERCA   = 2;
	public static final int NR_DIA_SEMANA_QUARTA  = 3;
	public static final int NR_DIA_SEMANA_QUINTA  = 4;
	public static final int NR_DIA_SEMANA_SEXTA   = 5;
	public static final int NR_DIA_SEMANA_SABADO  = 6;
	
	public static final String[] diasSemana = {"Domingo",
											   "Segunda-Feira",
											   "Terça-Feira", 
											   "Quarta-Feira",
											   "Quinta-Feira",
											   "Sexta-Feira",
											   "Sábado"};
	
	public static Result save(InstituicaoHorario horario, int cdUsuario){
		return save(horario, cdUsuario, null);
	}
	
	public static Result save(InstituicaoHorario horario, int cdUsuario, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			if(horario==null)
				return new Result(-1, "Erro ao salvar. Horário é nulo");
			
			if(horario.getTpTurno() == TP_MATUTINO
					&& ((horario.getHrInicio().get(Calendar.HOUR_OF_DAY) < 6 || horario.getHrTermino().get(Calendar.HOUR_OF_DAY) < 6)
					|| (horario.getHrInicio().get(Calendar.HOUR_OF_DAY) > 16 || horario.getHrTermino().get(Calendar.HOUR_OF_DAY) > 16))){
				if (isConnectionNull)
					Conexao.rollback(connect);
				return new Result(-1, "Horário matutino não pode ser antes das 06h e após as 16h");
			}
			
			if(horario.getTpTurno() == TP_VESPERTINO
					&& ((horario.getHrInicio().get(Calendar.HOUR_OF_DAY) < 9 || horario.getHrTermino().get(Calendar.HOUR_OF_DAY) < 9)
					|| (horario.getHrInicio().get(Calendar.HOUR_OF_DAY) > 21 || horario.getHrTermino().get(Calendar.HOUR_OF_DAY) > 21))){
				if (isConnectionNull)
					Conexao.rollback(connect);
				return new Result(-1, "Horário vespertino não pode ser antes das 09h e após as 21h");
			}
			
			if(horario.getTpTurno() == TP_NOTURNO
					&& ((horario.getHrInicio().get(Calendar.HOUR_OF_DAY) < 16 || horario.getHrTermino().get(Calendar.HOUR_OF_DAY) < 16)
					|| (horario.getHrInicio().get(Calendar.HOUR_OF_DAY) > 23 || horario.getHrTermino().get(Calendar.HOUR_OF_DAY) > 23))){
				if (isConnectionNull)
					Conexao.rollback(connect);
				return new Result(-1, "Horário noturno não pode ser antes das 16h e após as 23h");
			}
			// Verifica se existe instituição ao inserir ou alterar horários
//			if( !instituicaoHorarioDisponivel( horario, connect)  )
//				return new Result( -1, "A Instituição já possui este horário ocupado. Exclua o mesmo para lançar outro na faixa desejada.", null, null );
			
			int cdTipoOcorrencia = 0;
			
			int retorno;
			String txtOcorrencia = "";
			
			if(horario.getCdHorario()==0){
				cdTipoOcorrencia = TipoOcorrenciaServices.getById(InstituicaoEducacensoServices.TP_OCORRENCIA_CADASTRO_INSTITUICAO_HORARIO, connect).getCdTipoOcorrencia();
				txtOcorrencia = "Adicionado horário: " + com.tivic.manager.util.Util.convCalendarStringHourMinute(horario.getHrInicio()) + " - " + com.tivic.manager.util.Util.convCalendarStringHourMinute(horario.getHrTermino()) +" da escola " + InstituicaoDAO.get(horario.getCdInstituicao(), connect).getNmPessoa();
				retorno = InstituicaoHorarioDAO.insert(horario, connect);
				horario.setCdHorario(retorno);
			}
			else {
				cdTipoOcorrencia = TipoOcorrenciaServices.getById(InstituicaoEducacensoServices.TP_OCORRENCIA_ALTERACAO_INSTITUICAO_HORARIO, connect).getCdTipoOcorrencia();
				InstituicaoHorario instituicaoHorarioAntigo = InstituicaoHorarioDAO.get(horario.getCdHorario());
				txtOcorrencia = "Alterado horario: "+com.tivic.manager.util.Util.convCalendarStringHourMinute(instituicaoHorarioAntigo.getHrInicio())+" - "+com.tivic.manager.util.Util.convCalendarStringHourMinute(instituicaoHorarioAntigo.getHrTermino())+" ("+InstituicaoHorarioServices.diasSemana[instituicaoHorarioAntigo.getNrDiaSemana()]+") para horário: " + com.tivic.manager.util.Util.convCalendarStringHourMinute(horario.getHrInicio()) + " - " + com.tivic.manager.util.Util.convCalendarStringHourMinute(horario.getHrTermino()) +" ("+InstituicaoHorarioServices.diasSemana[horario.getNrDiaSemana()]+") da escola " + InstituicaoDAO.get(horario.getCdInstituicao(), connect).getNmPessoa();
				retorno = InstituicaoHorarioDAO.update(horario, connect);
			}
			
			
			ResultSetMap rsmPeriodoAtual = InstituicaoPeriodoServices.getPeriodoAtualOfInstituicao(horario.getCdInstituicao());
			InstituicaoPeriodo instituicaoPeriodo = null; 
			if(rsmPeriodoAtual.next()){
				instituicaoPeriodo = InstituicaoPeriodoDAO.get(rsmPeriodoAtual.getInt("cd_periodo_letivo"), connect);
			}
			
			
			OcorrenciaInstituicao ocorrencia = new OcorrenciaInstituicao(0, 0, txtOcorrencia, com.tivic.manager.util.Util.getDataAtual(), cdTipoOcorrencia, OcorrenciaServices.ST_CONCLUIDO, 0, cdUsuario, horario.getCdInstituicao(), com.tivic.manager.util.Util.getDataAtual(), cdUsuario, instituicaoPeriodo.getCdPeriodoLetivo());
			OcorrenciaInstituicaoServices.save(ocorrencia, connect);
			
			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();
			
			return new Result(retorno, (retorno<=0)?"Erro ao salvar...":"Salvo com sucesso...", "INSTITUICAOHORARIO", horario);
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
	 * Salva um array de horarios da instituicao
	 * @param horarios
	 * @param connect
	 * @return
	 */
//	public static Result save(ArrayList<InstituicaoHorario> horarios, Connection connect){
//		
//		boolean isConnectionNull = connect==null;
//		try {
//			if (isConnectionNull) {
//				connect = Conexao.conectar();
//				connect.setAutoCommit(false);
//			}
//			
//			if(horarios==null)
//				return new Result(-1, "Lista de horários é nula.");
//			
//			if(horarios.size()==0)
//				return new Result(-1, "Lista de horários é vazia.");
//			
//			int retorno = 0;
//			
//			//TODO: iterar e usar o save individual
//		
//			if(retorno<=0)
//				Conexao.rollback(connect);
//			else if (isConnectionNull)
//				connect.commit();
//			
//			return new Result(retorno, (retorno<=0)?"Erro ao salvar...":"Salvo com sucesso...");
//		}
//		catch(Exception e){
//			e.printStackTrace();
//			if (isConnectionNull)
//				Conexao.rollback(connect);
//			return new Result(-1, e.getMessage());
//		}
//		finally{
//			if (isConnectionNull)
//				Conexao.desconectar(connect);
//		}
//	}
	
	private static boolean instituicaoHorarioDisponivel( InstituicaoHorario instituicaoHorario, Connection connect ) throws Exception{
		PreparedStatement pstmt;
		SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		
		//evitar choque quando hrInicil coincidir com hrTermino
		GregorianCalendar hrI = instituicaoHorario.getHrInicio();
		hrI.add(Calendar.MINUTE, 1);
		GregorianCalendar hrF = instituicaoHorario.getHrTermino();
		hrF.add(Calendar.MINUTE, -1);
		
		String hrInicio = format.format( hrI.getTime() );
		String hrTermino = format.format( hrF.getTime() );
		
		hrI = instituicaoHorario.getHrInicio();
		hrI.add(Calendar.MINUTE, -1);
		hrF = instituicaoHorario.getHrTermino();
		hrF.add(Calendar.MINUTE, 1);
		
		boolean isDisponivel = false;
		try{
//			Verifica se existe o período
			pstmt = connect.prepareStatement("SELECT COUNT(*) FROM acd_instituicao_horario "+
											 "WHERE nr_dia_semana = ?  "+
											 "AND tp_turno = ?  "+
											 "AND cd_instituicao = ?  "+
											 "AND ( ? BETWEEN hr_inicio AND hr_termino "+
											 "      OR ? BETWEEN hr_inicio AND hr_termino) ");
			
			pstmt.setInt(1, instituicaoHorario.getNrDiaSemana() );
			pstmt.setInt(2, instituicaoHorario.getTpTurno() );
			pstmt.setInt(3, instituicaoHorario.getCdInstituicao() );
			pstmt.setTimestamp(4, Util.convStringTimestamp( hrInicio ));
			pstmt.setTimestamp(5, Util.convStringTimestamp( hrTermino ));
			//
			ResultSet result = pstmt.executeQuery();
			result.next();
			
			isDisponivel = result.getInt("count") > 0?false:true; 
		}catch(Exception e){
			e.printStackTrace();
			throw new Exception("Falha ao verificar disponibilidade de horário da instituicao!!");
		}
		return isDisponivel;
	}
	
	public static Result remove(int cdHorario, int cdUsuario){
		return remove(cdHorario, cdUsuario, false, null);
	}
	
	public static Result remove(int cdHorario, int cdUsuario, Connection connect){
		return remove(cdHorario, cdUsuario, false, connect);
	}
	
	public static Result remove(int cdHorario, int cdUsuario, boolean cascade){
		return remove(cdHorario, cdUsuario, cascade, null);
	}
	
	public static Result remove(int cdHorario, int cdUsuario, boolean cascade, Connection connect){
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
			
			InstituicaoHorario instituicaoHorario = InstituicaoHorarioDAO.get(cdHorario, connect);
			Instituicao instituicao = InstituicaoDAO.get(instituicaoHorario.getCdInstituicao(), connect);
			if(!cascade || retorno>0)
				retorno = InstituicaoHorarioDAO.delete(cdHorario, connect);
			
			int cdTipoOcorrencia = TipoOcorrenciaServices.getById(InstituicaoEducacensoServices.TP_OCORRENCIA_REMOCAO_INSTITUICAO_HORARIO, connect).getCdTipoOcorrencia();
			ResultSetMap rsmPeriodoAtual = InstituicaoPeriodoServices.getPeriodoAtualOfInstituicao(instituicaoHorario.getCdInstituicao());
			InstituicaoPeriodo instituicaoPeriodo = null; 
			if(rsmPeriodoAtual.next()){
				instituicaoPeriodo = InstituicaoPeriodoDAO.get(rsmPeriodoAtual.getInt("cd_periodo_letivo"), connect);
			}
			
			
			OcorrenciaInstituicao ocorrencia = new OcorrenciaInstituicao(0, 0, "Removido horário: " + com.tivic.manager.util.Util.convCalendarStringHourMinute(instituicaoHorario.getHrInicio()) + " - " + com.tivic.manager.util.Util.convCalendarStringHourMinute(instituicaoHorario.getHrTermino()) +" ("+diasSemana[instituicaoHorario.getNrDiaSemana()]+") da escola " + instituicao.getNmPessoa(), com.tivic.manager.util.Util.getDataAtual(), cdTipoOcorrencia, OcorrenciaServices.ST_CONCLUIDO, 0, cdUsuario, instituicao.getCdInstituicao(), com.tivic.manager.util.Util.getDataAtual(), cdUsuario, instituicaoPeriodo.getCdPeriodoLetivo());
			OcorrenciaInstituicaoServices.save(ocorrencia, connect);
			
			if(retorno<=0){
				Conexao.rollback(connect);
				return new Result(-2, "Este horário está vinculado a outros registros e não pode ser excluído!");
			}
			else if (isConnectionNull)
				connect.commit();
			
			return new Result(1, "Horário excluído com sucesso!");
		}
		catch(Exception e){
			e.printStackTrace();
			if (isConnectionNull)
				Conexao.rollback(connect);
			return new Result(-1, "Erro ao excluir horário!");
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static Result removeAll(int cdInstituicao, int cdPeriodoLetivo, int cdUsuario){
		return removeAll(cdInstituicao, cdPeriodoLetivo, cdUsuario, false, null);
	}
	
	public static Result removeAll(int cdInstituicao, int cdPeriodoLetivo, int cdUsuario, Connection connect){
		return removeAll(cdInstituicao, cdPeriodoLetivo, cdUsuario, false, connect);
	}
	
	public static Result removeAll(int cdInstituicao, int cdPeriodoLetivo, int cdUsuario, boolean cascade){
		return removeAll(cdInstituicao, cdPeriodoLetivo, cdUsuario, cascade, null);
	}
	
	public static Result removeAll(int cdInstituicao, int cdPeriodoLetivo, int cdUsuario, boolean cascade, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			int retorno = 0;
			
			ResultSetMap rsmHorarios = new ResultSetMap(connect.prepareStatement("SELECT * FROM acd_instituicao_horario WHERE cd_instituicao = " + cdInstituicao + " AND cd_periodo_letivo = " + cdPeriodoLetivo).executeQuery());
			
			while(rsmHorarios.next()){
				if(cascade){
					/** SE HOUVER, REMOVER TABELAS ASSOCIADAS **/
					retorno = 1;
				}
					
				if(!cascade || retorno>0)
					retorno = InstituicaoHorarioDAO.delete(rsmHorarios.getInt("cd_horario"), connect);
				
				if(retorno<=0){
					Conexao.rollback(connect);
					return new Result(-2, "Horário está vinculado a outros registros e não pode ser excluído!");
				}
			}

			Instituicao instituicao = InstituicaoDAO.get(cdInstituicao, connect);
			
			int cdTipoOcorrencia = TipoOcorrenciaServices.getById(InstituicaoEducacensoServices.TP_OCORRENCIA_REMOCAO_INSTITUICAO_HORARIO, connect).getCdTipoOcorrencia();
			ResultSetMap rsmPeriodoAtual = InstituicaoPeriodoServices.getPeriodoAtualOfInstituicao(instituicao.getCdInstituicao());
			InstituicaoPeriodo instituicaoPeriodo = null; 
			if(rsmPeriodoAtual.next()){
				instituicaoPeriodo = InstituicaoPeriodoDAO.get(rsmPeriodoAtual.getInt("cd_periodo_letivo"), connect);
			}
			
			
			OcorrenciaInstituicao ocorrencia = new OcorrenciaInstituicao(0, 0, "Removido todos os horários da escola " + instituicao.getNmPessoa(), com.tivic.manager.util.Util.getDataAtual(), cdTipoOcorrencia, OcorrenciaServices.ST_CONCLUIDO, 0, cdUsuario, instituicao.getCdInstituicao(), com.tivic.manager.util.Util.getDataAtual(), cdUsuario, instituicaoPeriodo.getCdPeriodoLetivo());
			OcorrenciaInstituicaoServices.save(ocorrencia, connect);
			
			if (isConnectionNull)
				connect.commit();
			
			return new Result(1, "Horário excluído com sucesso!");
		}
		catch(Exception e){
			e.printStackTrace();
			if (isConnectionNull)
				Conexao.rollback(connect);
			return new Result(-1, "Erro ao excluir horário!");
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static Result removeByInstituicaoPeriodo(int cdInstituicao, int cdPeriodoLetivo, Connection connect) {
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
			criterios.add(new ItemComparator("cd_instituicao", "" + cdInstituicao, ItemComparator.EQUAL, Types.INTEGER));
			criterios.add(new ItemComparator("cd_periodo_letivo", "" + cdPeriodoLetivo, ItemComparator.EQUAL, Types.INTEGER));
			ResultSetMap rsm = InstituicaoHorarioDAO.find(criterios, connect);
			while (rsm.next()) {
				int ret = InstituicaoHorarioDAO.delete(rsm.getInt("cd_horario"), connect);
				if(ret<0){
					Conexao.rollback(connect);
					return new Result(ret, "Erro ao excluir instituição horário");
				}
			}
			
			if (isConnectionNull)
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
			pstmt = connect.prepareStatement("SELECT * FROM acd_instituicao_horario ORDER BY hr_inicio");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! InstituicaoHorarioDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static ResultSetMap getAllByInstituicao(int cdInstituicao) {
		return getAllByInstituicao(cdInstituicao, 0, null);
	}
	
	public static ResultSetMap getAllByInstituicaoByPeriodo(int cdInstituicao, int cdPeriodoLetivo) {
		return getAllByInstituicao(cdInstituicao, 0, -1, cdPeriodoLetivo, null);
	}
	
	public static ResultSetMap getAllByInstituicaoByTurno(int cdInstituicao, int tpTurno, int cdPeriodoLetivo) {
		return getAllByInstituicao(cdInstituicao, 0, tpTurno, cdPeriodoLetivo, null);
	}

	public static ResultSetMap getAllByInstituicao(int cdInstituicao, Connection connect) {
		return getAllByInstituicao(cdInstituicao, 0, connect);
	}
	
	public static ResultSetMap getAllByInstituicao(int cdInstituicao, int cdProfessor) {
		return getAllByInstituicao(cdInstituicao, cdProfessor, null);
	}

	public static ResultSetMap getAllByInstituicao(int cdInstituicao, int cdProfessor, Connection connect) {
		return getAllByInstituicao(cdInstituicao, cdProfessor, -1, 0, connect);
	}
	
	public static ResultSetMap getAllByInstituicao(int cdInstituicao, int cdProfessor, int cdPeriodoLetivo, Connection connect) {
		return getAllByInstituicao(cdInstituicao, cdProfessor, -1, cdPeriodoLetivo, connect);
	}
	
	public static ResultSetMap getAllByInstituicao(int cdInstituicao, int cdProfessor, int tpTurno, int cdPeriodoLetivo, Connection connect) {
		return getAllByInstituicao(cdInstituicao, cdProfessor, tpTurno, cdPeriodoLetivo, -1, connect);
	}
	
	public static ResultSetMap getAllByInstituicao(int cdInstituicao, int cdProfessor, int tpTurno, int cdPeriodoLetivo, int nrDiaSemana, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			
			if(cdPeriodoLetivo == 0){
				ResultSetMap rsmPeriodoRecente = InstituicaoPeriodoServices.getPeriodoRecenteOfInstituicao(cdInstituicao);
				if(rsmPeriodoRecente.next())
					cdPeriodoLetivo = rsmPeriodoRecente.getInt("cd_periodo_letivo");				
			}
			
			pstmt = connect.prepareStatement("SELECT * FROM acd_instituicao_horario A "
					+ "						   WHERE cd_instituicao = "+cdInstituicao
					+ (tpTurno >= 0 ?             " AND tp_turno = " + tpTurno : "")
					+ (cdPeriodoLetivo > 0 ?      " AND A.cd_periodo_letivo = " + cdPeriodoLetivo : "")
					+ (nrDiaSemana >= 0?          " AND A.nr_dia_semana = " + nrDiaSemana : "")
					+ "				  		  ORDER BY nr_dia_semana, hr_inicio");
			ResultSetMap rsmHorarioInstituicao = new ResultSetMap(pstmt.executeQuery());
			
			pstmt = connect.prepareStatement("SELECT * FROM acd_instituicao_horario A "
					+ (cdProfessor > 0 ? "	   JOIN acd_professor_horario B ON (A.cd_horario = B.cd_horario AND B.cd_professor = "+cdProfessor+")" : "")
					+ "						   WHERE cd_instituicao = "+cdInstituicao
					+ (tpTurno >= 0 ?             " AND tp_turno = " + tpTurno : "")
					+ (cdPeriodoLetivo > 0 ?      " AND A.cd_periodo_letivo = " + cdPeriodoLetivo : "")
					+ (nrDiaSemana >= 0?          " AND A.nr_dia_semana = " + nrDiaSemana : "")
					+ "				  		  ORDER BY nr_dia_semana, hr_inicio");
			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
			while(rsm.next()){
				
				//GaMBIARRA PARA SOLUCIONAR TEMPORARIAMENTE UM PROBLEMA EM QUE NO SERVIDOR DA SMED ESTA COLOCANDO MAIS 25MINUTOS NA PASSAGEM PARA O JAVA
				
				GregorianCalendar hrInicio = rsm.getGregorianCalendar("hr_inicio");
				
				if(hrInicio.get(Calendar.HOUR_OF_DAY) >= 21 && hrInicio.get(Calendar.SECOND) > 0){
					hrInicio.add(Calendar.MINUTE, 25);
				}
				
				rsm.setValueToField("hr_inicio", hrInicio);
				
				GregorianCalendar hrTermino = rsm.getGregorianCalendar("hr_termino");
				
				if(hrTermino.get(Calendar.HOUR_OF_DAY) >= 21 && hrTermino.get(Calendar.SECOND) > 0){
					hrTermino.add(Calendar.MINUTE, 25);
				}
				
				rsm.setValueToField("hr_termino", hrTermino);
				
				rsm.setValueToField("CL_HORARIO", diasSemana[rsm.getInt("nr_dia_semana")] + " - " + Util.formatDateTime(hrInicio, "HH:mm") + " - " + Util.formatDateTime(hrTermino, "HH:mm"));
			}
			rsm.beforeFirst();
			
			Instituicao instituicao = InstituicaoDAO.get(cdInstituicao, connect);
			
			if(rsmHorarioInstituicao.size() == 0 && instituicao != null){
				pstmt = connect.prepareStatement("SELECT * FROM acd_instituicao_horario A "
						+ "						   WHERE cd_instituicao = "+instituicao.getCdPessoaSuperior()
						+ (tpTurno >= 0 ?             " AND tp_turno = " + tpTurno : "")
						+ (cdPeriodoLetivo > 0 ?      " AND A.cd_periodo_letivo = " + cdPeriodoLetivo : "")
						+ (nrDiaSemana >= 0?          " AND A.nr_dia_semana = " + nrDiaSemana : "")
						+ "				  		  ORDER BY nr_dia_semana, hr_inicio");
				rsm = new ResultSetMap(pstmt.executeQuery());
				
				while(rsm.next()){
					
					GregorianCalendar hrInicio = rsm.getGregorianCalendar("hr_inicio");
					GregorianCalendar hrTermino = rsm.getGregorianCalendar("hr_termino");
					
					rsm.setValueToField("CL_HORARIO", diasSemana[rsm.getInt("nr_dia_semana")] + " - " + Util.formatDateTime(hrInicio, "HH:mm") + " - " + Util.formatDateTime(hrTermino, "HH:mm"));
				}
				rsm.beforeFirst();
			}
			
			return rsm;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! InstituicaoHorarioDAO.getAllByInstituicao: " + e);
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
		return Search.find("SELECT * FROM acd_instituicao_horario ", " ORDER BY hr_inicio ", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}
	
	
	public static int deleteTurno(int cdInstituicao, int tpTurno) {
		return deleteTurno(cdInstituicao, tpTurno, null);
	}

	public static int deleteTurno(int cdInstituicao, int tpTurno, Connection connection){
		boolean isConnectionNull = connection==null;
		try {
			if (isConnectionNull)
				connection = Conexao.conectar();
			
			PreparedStatement pstmt = connection.prepareStatement("DELETE " +
					"FROM acd_instituicao_horario " +
					"WHERE cd_instituicao = ? " +
					"  AND tp_turno = ?");
			pstmt.setInt(1, cdInstituicao);
			pstmt.setInt(2, tpTurno);
			pstmt.execute();
			
			return 1;
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! InstituicaoHorarioServices.deleteTurno: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}
	
	public static Result saveTurno(int cdInstituicao, int tpTurno, ArrayList<InstituicaoHorario> horarios, int cdUsuario) {
		return saveTurno(cdInstituicao, tpTurno, horarios, cdUsuario, null);
	}

	public static Result saveTurno(int cdInstituicao, int tpTurno, ArrayList<InstituicaoHorario> horarios, int cdUsuario, Connection connection){
		boolean isConnectionNull = connection==null;
		
		try {
			if (isConnectionNull) {
				connection = Conexao.conectar();
				connection.setAutoCommit(false);
			}
			Instituicao instituicao = InstituicaoDAO.get(cdInstituicao, connection);
			if (instituicao == null) {
				return new Result(-2, "Instituição indicada não existe.");
			}
			
			for (int i=0; horarios!=null && i<horarios.size(); i++) {
				
				InstituicaoHorario horario = horarios.get(i);
				
				//GaMBIARRA PARA SOLUCIONAR TEMPORARIAMENTE UM PROBLEMA EM QUE NO SERVIDOR DA SMED ESTA COLOCANDO MAIS 25MINUTOS NA PASSAGEM PARA O JAVA
				if(horario.getHrInicio() != null && horario.getHrInicio().get(Calendar.HOUR_OF_DAY) >= 21 && horario.getHrInicio().get(Calendar.SECOND) > 0){
					horario.getHrInicio().add(Calendar.MINUTE, -25); 
				}
				//GaMBIARRA PARA SOLUCIONAR TEMPORARIAMENTE UM PROBLEMA EM QUE NO SERVIDOR DA SMED ESTA COLOCANDO MAIS 25MINUTOS NA PASSAGEM PARA O JAVA
				if(horario.getHrTermino() != null && horario.getHrTermino().get(Calendar.HOUR_OF_DAY) >= 21 && horario.getHrTermino().get(Calendar.SECOND) > 0){
					horario.getHrTermino().add(Calendar.MINUTE, -25); 
				}
				
				if(horario.getTpTurno() == TP_MATUTINO
						&& ((horario.getHrInicio().get(Calendar.HOUR_OF_DAY) < 6 || horario.getHrTermino().get(Calendar.HOUR_OF_DAY) < 6)
						|| (horario.getHrInicio().get(Calendar.HOUR_OF_DAY) > 16 || horario.getHrTermino().get(Calendar.HOUR_OF_DAY) > 16))){
					if (isConnectionNull)
						Conexao.rollback(connection);
					return new Result(-1, "Horário matutino não pode ser antes das 06h e após as 16h");
				}
				
				if(horario.getTpTurno() == TP_VESPERTINO
						&& ((horario.getHrInicio().get(Calendar.HOUR_OF_DAY) < 9 || horario.getHrTermino().get(Calendar.HOUR_OF_DAY) < 9)
						|| (horario.getHrInicio().get(Calendar.HOUR_OF_DAY) > 21 || horario.getHrTermino().get(Calendar.HOUR_OF_DAY) > 21))){
					if (isConnectionNull)
						Conexao.rollback(connection);
					return new Result(-1, "Horário vespertino não pode ser antes das 09h e após as 21h");
				}
				
				if(horario.getTpTurno() == TP_NOTURNO
						&& ((horario.getHrInicio().get(Calendar.HOUR_OF_DAY) < 16 || horario.getHrTermino().get(Calendar.HOUR_OF_DAY) < 16)
						|| (horario.getHrInicio().get(Calendar.HOUR_OF_DAY) > 23 || horario.getHrTermino().get(Calendar.HOUR_OF_DAY) > 23))){
					if (isConnectionNull)
						Conexao.rollback(connection);
					return new Result(-1, "Horário noturno não pode ser antes das 16h e após as 23h");
				}
				
//				int retorno;
//				if(horario.getCdHorario()==0){
//
//					if( !instituicaoHorarioDisponivel( horario, connection)  )
//						return new Result( -1, "A Instituição já possui este horário ocupado.", null, null );
//
//					retorno = InstituicaoHorarioDAO.insert(horario, connection);
//					horario.setCdHorario(retorno);
//				}
//				else {
//					retorno = InstituicaoHorarioDAO.update(horario, connection);
//				}
//				
//				if(retorno<=0)
//					Conexao.rollback(connection);
//				else if (isConnectionNull)
//					connection.commit();
//				
//				return new Result(retorno, (retorno<=0)?"Erro ao salvar...":"Salvo com sucesso...", "INSTITUICAOHORARIO", horario);
				
				if(horarios.get(i).getCdHorario() <= 0){
//					if( !instituicaoHorarioDisponivel( horarios.get(i), connection)  )
//						return new Result( -1, "A Instituição já possui este horário ocupado. Exclua o mesmo para lançar outro na faixa desejada.", null, null );
					
					if (InstituicaoHorarioDAO.insert(horarios.get(i), connection) <= 0) {
						if (isConnectionNull)
							Conexao.rollback(connection);
						return new Result(-1, "Erro ao salvar horários");
					}
				}
				else{
					if (InstituicaoHorarioDAO.update(horarios.get(i), connection) <= 0) {
						if (isConnectionNull)
							Conexao.rollback(connection);
						return new Result(-1, "Erro ao atualizar horários");
					}
				}
				
				int cdTipoOcorrencia = TipoOcorrenciaServices.getById(InstituicaoEducacensoServices.TP_OCORRENCIA_CADASTRO_INSTITUICAO_HORARIO, connection).getCdTipoOcorrencia();
				InstituicaoHorario instituicaoHorario = InstituicaoHorarioDAO.get(horarios.get(i).getCdHorario(), connection);
				ResultSetMap rsmPeriodoAtual = InstituicaoPeriodoServices.getPeriodoAtualOfInstituicao(cdInstituicao);
				InstituicaoPeriodo instituicaoPeriodo = null; 
				if(rsmPeriodoAtual.next()){
					instituicaoPeriodo = InstituicaoPeriodoDAO.get(rsmPeriodoAtual.getInt("cd_periodo_letivo"), connection);
				}
				
				
				OcorrenciaInstituicao ocorrencia = new OcorrenciaInstituicao(0, 0, "Adicionado horário: " + com.tivic.manager.util.Util.convCalendarStringHourMinute(instituicaoHorario.getHrInicio()) + " - " + com.tivic.manager.util.Util.convCalendarStringHourMinute(instituicaoHorario.getHrTermino()) +" ("+diasSemana[instituicaoHorario.getNrDiaSemana()]+") da escola " + instituicao.getNmPessoa(), com.tivic.manager.util.Util.getDataAtual(), cdTipoOcorrencia, OcorrenciaServices.ST_CONCLUIDO, 0, cdUsuario, instituicao.getCdInstituicao(), com.tivic.manager.util.Util.getDataAtual(), cdUsuario, instituicaoPeriodo.getCdPeriodoLetivo());
				OcorrenciaInstituicaoServices.save(ocorrencia, connection);
			}
			
			if (isConnectionNull)
				connection.commit();
			
			return new Result(1, "Horários do turno salvos com sucesso");
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! InstituicaoHorarioServices.saveTurno: " +  e);
			if (isConnectionNull)
				Conexao.rollback(connection);
			return new Result(-1, "Erro ao salvar horários");
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}
}