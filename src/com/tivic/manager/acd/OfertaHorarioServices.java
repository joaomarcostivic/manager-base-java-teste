package com.tivic.manager.acd;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

import org.apache.commons.httpclient.util.DateUtil;

import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.util.Result;
import sol.util.Util;

import com.dataprom.radar.ws.dominio.DiaSemana;
import com.tivic.sol.connection.Conexao;

public class OfertaHorarioServices {

	public static int NR_DIA_DOMINGO = 0;
	public static int NR_DIA_SEGUNDA = 1;
	public static int NR_DIA_TERCA   = 2;
	public static int NR_DIA_QUARTA  = 3;
	public static int NR_DIA_QUINTA  = 4;
	public static int NR_DIA_SEXTA   = 5;
	public static int NR_DIA_SABADO  = 6;
	
	public static String[] nrDiasSemana = {"Domingo", "Segunda-feira", "Terça-feira", "Quarta-feira", "Quinta-feira", "Sexta-feira", "Sábado"};
	
	public static int ST_INATIVO   	= 0;
	public static int ST_ATIVO  	= 1;
	
	public static Result save( OfertaHorario ofertaHorario, int cdInstituicao ){
		return save(ofertaHorario, cdInstituicao, null);
	}
	
	public static Result save( OfertaHorario ofertaHorario, int cdInstituicao, Connection connect ){
		return save(ofertaHorario, cdInstituicao, 0, -1, connect);
	}
	
	public static Result save( OfertaHorario ofertaHorario, int cdInstituicao, int cdPeriodoLetivo ){
		return save(ofertaHorario, cdInstituicao, cdPeriodoLetivo, -1, null);
	}
	
	public static Result save( OfertaHorario ofertaHorario, int cdInstituicao, int cdPeriodoLetivo, Connection connect ){
		return save(ofertaHorario, cdInstituicao, cdPeriodoLetivo, -1, connect);
	}
	
	public static Result save( OfertaHorario ofertaHorario, int cdInstituicao, int cdPeriodoLetivo, int tpTurno ){
		return save(ofertaHorario, cdInstituicao, cdPeriodoLetivo, tpTurno, null);
	}

	public static Result save( OfertaHorario ofertaHorario, int cdInstituicao, int cdPeriodoLetivo, int tpTurno,  Connection connect){
		boolean isConnectionNull = connect==null;
		int retorno = 0;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			if(ofertaHorario==null)
				return new Result(-1, "Erro ao salvar. OfertaHorario é nulo");
			
			Oferta oferta = OfertaDAO.get( ofertaHorario.getCdOferta(), connect );
			
			ResultSetMap rsmOfertaHorario = new ResultSetMap(connect.prepareStatement("SELECT * FROM acd_oferta_horario OH WHERE OH.cd_oferta = " + oferta.getCdOferta()).executeQuery());
			while(rsmOfertaHorario.next()){
				
				OfertaHorario ofertaHorarioCadastrado = OfertaHorarioDAO.get(rsmOfertaHorario.getInt("cd_horario"), rsmOfertaHorario.getInt("cd_oferta"), connect);
				
				if(ofertaHorario.getNrDiaSemana() == ofertaHorarioCadastrado.getNrDiaSemana()){
					if(isConnectionNull)
						Conexao.rollback(connect);
					return new Result(-1, OfertaHorarioServices.nrDiasSemana[ofertaHorario.getNrDiaSemana()] + " já tem um horário cadastrado para essa oferta");
				}
				
			}
			rsmOfertaHorario.beforeFirst();
			
			//VALIDAÇÕES DE HORARIO
			ResultSetMap rsmInstituicaoHorario = instituicaoHorarioDisponivel(ofertaHorario, cdInstituicao, tpTurno, connect);
			if( rsmInstituicaoHorario.size() == 0 )
				return new Result( -2, "A Instituição não possui este horário disponível.", null, null );
			else{
				if(rsmInstituicaoHorario.next()){
					ofertaHorario.setCdHorarioInstituicao(rsmInstituicaoHorario.getInt("cd_horario"));
				}
			}
//			if(  oferta.getCdProfessor() > 0 && !professorInstituicaoHorarioDisponivel(ofertaHorario, cdInstituicao, tpTurno, connect)  )
//				return new Result( -3, "O Professor não possui este horário disponível para esta instituicao.", null, null );
//			
//			if( oferta.getCdProfessor() > 0 && !professorHorarioDisponivel(ofertaHorario, cdInstituicao, cdPeriodoLetivo, tpTurno, connect)  )
//				return new Result( -4, "O Professor possui este horário ocupado.", null, null );
			
			if( !turmaHorarioDisponivel(ofertaHorario, cdInstituicao, connect)  )
				return new Result( -5, "A turma possui este horário ocupado.", null, null );
			
			if( !salaAulaDisponivel(ofertaHorario, cdInstituicao, connect)  )
				return new Result( -5, "A sala de aula possui este horário ocupado.", null, null );
			
			if(ofertaHorario.getCdHorario()==0){
				retorno = OfertaHorarioDAO.insert(ofertaHorario, connect);
				ofertaHorario.setCdHorario(retorno);
			}
			else {
				retorno = OfertaHorarioDAO.update(ofertaHorario, connect);
			}
		
			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();

			return new Result(retorno, (retorno<=0)?"Erro ao salvar...":"Salvo com sucesso...", "OFERTAHORARIO", ofertaHorario);
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
	
	private static ResultSetMap instituicaoHorarioDisponivel( OfertaHorario ofertaHorario, int cdInstituicao, int tpTurno, Connection connect ) throws Exception{
		PreparedStatement pstmt;
		SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		String hrInicio = format.format( ofertaHorario.getHrInicio().getTime() );
		String hrTermino = format.format( ofertaHorario.getHrTermino().getTime() );
		
		ResultSetMap rsmPeriodoAtual = InstituicaoServices.getPeriodoLetivoVigente(cdInstituicao, connect);
		int cdPeriodoLetivoAtual = 0;
		if(rsmPeriodoAtual.next()){
			cdPeriodoLetivoAtual = rsmPeriodoAtual.getInt("cd_periodo_letivo");
		}
		
		Oferta oferta = OfertaDAO.get(ofertaHorario.getCdOferta(), connect);
		ResultSetMap rsm;
		try{
			pstmt = connect.prepareStatement("SELECT * FROM acd_instituicao_horario A "+
											"WHERE A.nr_dia_semana = ?  "+
											"AND A.tp_turno = ?  "+
											"AND A.cd_instituicao = ?  "+
											"AND A.cd_periodo_letivo = ?  "
											//+
											//"AND ( ? BETWEEN A.hr_inicio AND A.hr_termino "+
											//"AND ? BETWEEN A.hr_inicio AND A.hr_termino)"
											);
			pstmt.setInt(1, ofertaHorario.getNrDiaSemana() );
			pstmt.setInt(2, (tpTurno < 0 ? oferta.getTpTurno() : tpTurno));
			pstmt.setInt(3, cdInstituicao );
			pstmt.setInt(4, cdPeriodoLetivoAtual );
			//pstmt.setTimestamp(5, Util.convStringTimestamp( hrInicio ));
			//pstmt.setTimestamp(6, Util.convStringTimestamp( hrTermino )  );
			rsm = new ResultSetMap(pstmt.executeQuery());
			
		}catch(Exception e){
			e.printStackTrace();
			throw new Exception("Falha ao verificar disponibilidade de horário da instituicao!!");
		}
		return rsm;
	}
	
	private static boolean professorInstituicaoHorarioDisponivel( OfertaHorario ofertaHorario, int cdInstituicao, int tpTurno, Connection connect ) throws Exception{
		PreparedStatement pstmt;
		SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		String hrInicio = format.format( ofertaHorario.getHrInicio().getTime() );
		String hrTermino = format.format( ofertaHorario.getHrTermino().getTime() );
		Oferta oferta = OfertaDAO.get( ofertaHorario.getCdOferta(), connect );
		
		boolean isDisponivel = false;
		try{
			pstmt = connect.prepareStatement("SELECT COUNT(*) FROM acd_instituicao_horario A "+
											"LEFT OUTER JOIN acd_professor_horario B ON (  A.cd_horario = B.cd_horario ) "+
											"WHERE A.nr_dia_semana = ?  "+
											"AND A.tp_turno = ?  "+
											"AND A.cd_instituicao = ?  "+
											"AND A.cd_periodo_letivo = ?  "+
											"AND B.cd_professor = ?  "+
											"AND ( ? BETWEEN A.hr_inicio AND A.hr_termino "+
											"OR ? BETWEEN A.hr_inicio AND A.hr_termino)");
			
			pstmt.setInt(1, ofertaHorario.getNrDiaSemana() );
			pstmt.setInt(2, (tpTurno < 0 ? oferta.getTpTurno() : tpTurno) );
			pstmt.setInt(3, cdInstituicao );
			pstmt.setInt(4, oferta.getCdPeriodoLetivo() );
			pstmt.setInt(5, oferta.getCdProfessor() );
			pstmt.setTimestamp(6, Util.convStringTimestamp( hrInicio ));
			pstmt.setTimestamp(7, Util.convStringTimestamp( hrTermino )  );
			ResultSet result = pstmt.executeQuery();
			result.next();
			isDisponivel = result.getInt("count") > 0?true:false; 
		}catch(Exception e){
			e.printStackTrace();
			throw new Exception("Falha ao verificar disponibilidade de horário do professor para a instituicao!!");
		}
		
		return isDisponivel;
	}
	
	private static boolean professorHorarioDisponivel(OfertaHorario ofertaHorario, int cdInstituicao, int cdPeriodoLetivo, int tpTurno, Connection connect ) throws Exception{
		
		PreparedStatement pstmt;
		SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		OfertaHorario ofertaHorarioNovo = (OfertaHorario)ofertaHorario.clone();
		ofertaHorarioNovo.getHrInicio().add(Calendar.MINUTE, +1);
		ofertaHorarioNovo.getHrTermino().add(Calendar.MINUTE, -1);
		String hrInicio = format.format( ofertaHorarioNovo.getHrInicio().getTime() );
		String hrTermino = format.format( ofertaHorarioNovo.getHrTermino().getTime() );
		Oferta oferta = OfertaDAO.get( ofertaHorarioNovo.getCdOferta(), connect );
		boolean isDisponivel = false;
		try{
//			pstmt = connect.prepareStatement("SELECT COUNT(*) FROM acd_instituicao_horario A "+
//											"LEFT OUTER JOIN acd_professor_horario B ON (  A.cd_horario = B.cd_horario ) "+
//											"LEFT OUTER JOIN acd_oferta C ON (  C.cd_professor = B.cd_professor ) "+
//											"LEFT OUTER JOIN acd_oferta_horario D ON (  C.cd_oferta = D.cd_oferta ) "+
//											"WHERE A.nr_dia_semana = ?  "+
//											"AND A.tp_turno = ?  "+
//											"AND A.cd_instituicao = ?  "+
//											"AND B.cd_professor = ?  "+
//											"AND D.cd_horario <> ?  "+
//											"AND ( ? BETWEEN D.hr_inicio AND D.hr_termino "+
//											"OR ? BETWEEN D.hr_inicio AND D.hr_termino)");
			pstmt = connect.prepareStatement("SELECT COUNT(*) FROM acd_oferta A "+
											 "LEFT OUTER JOIN acd_oferta_horario B ON ( A.cd_oferta = B.cd_oferta ) "+
											 "LEFT OUTER JOIN acd_professor_horario C ON ( C.cd_horario = B.cd_horario_instituicao ) "+
											 "LEFT OUTER JOIN acd_instituicao_horario D ON ( D.cd_horario = B.cd_horario_instituicao ) "+
											 "WHERE D.nr_dia_semana = ?  "+
											 "AND D.tp_turno = ?  "+
											 "AND D.cd_instituicao = ?  "+
											 "AND D.cd_periodo_letivo = A.cd_periodo_letivo  "+
											 "AND C.cd_professor = ?  "+
											 "AND ( ? BETWEEN D.hr_inicio AND D.hr_termino "+
											 " AND ? BETWEEN D.hr_inicio AND D.hr_termino) "+
											 (cdPeriodoLetivo > 0 ? " AND A.cd_periodo_letivo = ?  " : ""));
			
			pstmt.setInt(1, ofertaHorarioNovo.getNrDiaSemana() );
			pstmt.setInt(2, (tpTurno < 0 ? oferta.getTpTurno() : tpTurno) );
			pstmt.setInt(3, cdInstituicao );
			pstmt.setInt(4, oferta.getCdProfessor() );
			pstmt.setTimestamp(5, Util.convStringTimestamp( hrInicio ));
			pstmt.setTimestamp(6, Util.convStringTimestamp( hrTermino )  );
			if(cdPeriodoLetivo > 0)
				pstmt.setInt(7, cdPeriodoLetivo);
			ResultSetMap result = new ResultSetMap(pstmt.executeQuery());
			result.next();
			isDisponivel = result.getInt("count") == 0?true:false; 
		}catch(Exception e){
			e.printStackTrace();
			throw new Exception("Falha ao verificar disponibilidade de horário do professor para a turma!!");
		}
		
		return isDisponivel;
	}
	
	private static boolean turmaHorarioDisponivel(OfertaHorario ofertaHorario, int cdInstituicao, Connection connect ) throws Exception{
		
		PreparedStatement pstmt;
		SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		String hrInicio = format.format( ofertaHorario.getHrInicio().getTime() );
		String hrTermino = format.format( ofertaHorario.getHrTermino().getTime() );
		Oferta oferta = OfertaDAO.get( ofertaHorario.getCdOferta(), connect );
		boolean isDisponivel = false;
		try{
			pstmt = connect.prepareStatement("SELECT COUNT(*) FROM acd_oferta A "+
											"LEFT OUTER JOIN acd_oferta_horario B ON (  A.cd_oferta = B.cd_oferta ) "+
											"WHERE B.nr_dia_semana = ?  "+
											"AND A.tp_turno = ?  "+
											"AND A.cd_turma = ?  "+
											"AND B.cd_horario <> ?  "+
											"AND ( ? BETWEEN B.hr_inicio AND B.hr_termino "+
											"OR ? BETWEEN B.hr_inicio AND B.hr_termino)");
			
			pstmt.setInt(1, ofertaHorario.getNrDiaSemana() );
			pstmt.setInt(2, oferta.getTpTurno() );
			pstmt.setInt(3, oferta.getCdTurma() );
			pstmt.setInt(4, ofertaHorario.getCdHorario() );
			pstmt.setTimestamp(5, Util.convStringTimestamp( hrInicio ) );
			pstmt.setTimestamp(6, Util.convStringTimestamp( hrTermino )  );
			ResultSet result = pstmt.executeQuery();
			result.next();
			isDisponivel = result.getInt("count") == 0?true:false; 
		}catch(Exception e){
			e.printStackTrace();
			throw new Exception("Falha ao verificar disponibilidade de horário da turma!!");
		}
		
		return isDisponivel;
	}
	
	private static boolean salaAulaDisponivel(OfertaHorario ofertaHorario, int cdInstituicao, Connection connect ) throws Exception{
		
		PreparedStatement pstmt;
		SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		String hrInicio = format.format( ofertaHorario.getHrInicio().getTime() );
		String hrTermino = format.format( ofertaHorario.getHrTermino().getTime() );
		Oferta oferta = OfertaDAO.get( ofertaHorario.getCdOferta(), connect );
		boolean isDisponivel = false;
		try{
			pstmt = connect.prepareStatement("SELECT COUNT(*) FROM acd_oferta A "+
											"LEFT OUTER JOIN acd_oferta_horario B ON (  A.cd_oferta = B.cd_oferta ) "+
											"WHERE B.nr_dia_semana = ?  "+
											"AND A.tp_turno = ?  "+
											"AND A.cd_dependencia = ?  "+
											"AND B.cd_horario <> ?  "+
											"AND ( ? BETWEEN B.hr_inicio AND B.hr_termino "+
											"OR ? BETWEEN B.hr_inicio AND B.hr_termino)");
			
			pstmt.setInt(1, ofertaHorario.getNrDiaSemana() );
			pstmt.setInt(2, oferta.getTpTurno() );
			pstmt.setInt(3, oferta.getCdDependencia() );
			pstmt.setInt(4, ofertaHorario.getCdHorario() );
			pstmt.setTimestamp(5, Util.convStringTimestamp( hrInicio ) );
			pstmt.setTimestamp(6, Util.convStringTimestamp( hrTermino )  );
			ResultSet result = pstmt.executeQuery();
			result.next();
			isDisponivel = result.getInt("count") == 0?true:false; 
		}catch(Exception e){
			e.printStackTrace();
			throw new Exception("Falha ao verificar disponibilidade de horário da sala de aula!!");
		}
		
		return isDisponivel;
	}
	
	
	public static Result remove(ArrayList<Integer> codigosHorario, int cdOferta){
		return remove(codigosHorario, cdOferta, null);
	}
	public static Result remove(ArrayList<Integer> codigosHorario, int cdOferta, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			for(Integer codigoHorario : codigosHorario){
				Result result = remove(codigoHorario, cdOferta, connect);
				if(result.getCode() < 0){
					if(isConnectionNull)
						Conexao.rollback(connect);
					return result;
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
	
	public static Result remove(int cdHorario, int cdOferta){
		return remove(cdHorario, cdOferta, false, null);
	}
	
	public static Result remove(int cdHorario, int cdOferta, Connection connect){
		return remove(cdHorario, cdOferta, false, connect);
	}
	public static Result remove(int cdHorario, int cdOferta, boolean cascade){
		return remove(cdHorario, cdOferta, cascade, null);
	}
	public static Result remove(int cdHorario, int cdOferta, boolean cascade, Connection connect){
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
				retorno = OfertaHorarioDAO.delete(cdHorario, cdOferta, connect);
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
	
	public static Result removeAll(int cdOferta){
		return removeAll(cdOferta, false, null);
	}
	public static Result removeAll(int cdOferta, boolean cascade){
		return removeAll(cdOferta, cascade, null);
	}
	public static Result removeAll(int cdOferta, boolean cascade, Connection connect){
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
				retorno = connect.prepareStatement("DELETE FROM acd_oferta_horario WHERE cd_oferta = "+cdOferta).executeUpdate();
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
			pstmt = connect.prepareStatement("SELECT * FROM acd_oferta_horario");
				return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! OfertaHorarioServices.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! OfertaHorarioServices.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	/**
	 * Método para retornar os horarios por instituicao
	 * @return horarios por instituicao
	 */
	public static ResultSetMap getAllByInstituicao(int cdInstituicao) {
		return getAllByInstituicao(cdInstituicao, null);
	}

	public static ResultSetMap getAllByInstituicao(int cdInstituicao, Connection connect) {
		boolean isConnectionNull = connect == null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM acd_oferta_horario" + 
											" WHERE cd_instituicao = " + cdInstituicao);
				return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! OfertaHorarioServices.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! OfertaHorarioServices.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static ResultSetMap getAllByTurmaDisciplina(int cdTurma, int cdDisciplina, int nrDiaSemana) {
		return getAllByTurmaDisciplina(cdTurma, cdDisciplina, nrDiaSemana, null);
	}
	public static ResultSetMap getAllByTurmaDisciplina(int cdTurma, int cdDisciplina, int nrDiaSemana, Connection connect) {
		boolean isConnectionNull = connect == null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM acd_oferta WHERE cd_turma = " + cdTurma + (cdDisciplina > 0 ? " AND cd_disciplina = " + cdDisciplina : "") + " AND st_oferta = " + OfertaServices.ST_ATIVO);
			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
			if(rsm.next()){
				ResultSetMap rsmOferta = getAllByOferta(rsm.getInt("cd_oferta"), nrDiaSemana, connect);
				
				return rsmOferta;
			}
			
			return rsm;
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! OfertaHorarioServices.getAllByOferta: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! OfertaHorarioServices.getAllByOferta: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	/**
	 * Método para retornar os horarios por oferta
	 * @return horarios por instituicao
	 */
	public static ResultSetMap getAllByOferta(int cdOferta) {
		return getAllByOferta(cdOferta, -1, null);
	}

	public static ResultSetMap getAllByOferta(int cdOferta, Connection connect) {
		return getAllByOferta(cdOferta, -1, connect);
	}
	
	public static ResultSetMap getAllByOferta(int cdOferta, int nrDiaSemana) {
		return getAllByOferta(cdOferta, nrDiaSemana, null);
	}

	public static ResultSetMap getAllByOferta(int cdOferta, int nrDiaSemana, Connection connect) {
		boolean isConnectionNull = connect == null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM acd_oferta_horario" + 
											" WHERE cd_oferta = " + cdOferta + 
											(nrDiaSemana >= 0 ? " AND nr_dia_semana = " + nrDiaSemana : ""));
			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
			while(rsm.next()){
				rsm.setValueToField("CL_DIA_SEMANA", nrDiasSemana[rsm.getInt("nr_dia_semana")]);
				
				GregorianCalendar hrInicio = rsm.getGregorianCalendar("hr_inicio");
				GregorianCalendar hrTermino = rsm.getGregorianCalendar("hr_termino");
				
				rsm.setValueToField("CL_HORARIO_INICIO", (hrInicio.get(Calendar.HOUR_OF_DAY) > 9 ? hrInicio.get(Calendar.HOUR_OF_DAY) : "0" + hrInicio.get(Calendar.HOUR_OF_DAY)) + ":" + (hrInicio.get(Calendar.MINUTE) > 9 ? hrInicio.get(Calendar.MINUTE) : "0" + hrInicio.get(Calendar.MINUTE)));
				rsm.setValueToField("CL_HORARIO_TERMINO", (hrTermino.get(Calendar.HOUR_OF_DAY) > 9 ? hrTermino.get(Calendar.HOUR_OF_DAY) : "0" + hrTermino.get(Calendar.HOUR_OF_DAY)) + ":" + (hrTermino.get(Calendar.MINUTE) > 9 ? hrTermino.get(Calendar.MINUTE) : "0" + hrTermino.get(Calendar.MINUTE)));
				
				rsm.setValueToField("CL_HORARIO", rsm.getString("CL_HORARIO_INICIO") + " - " + rsm.getString("CL_HORARIO_TERMINO"));
				
			}
			rsm.beforeFirst();
			
			return rsm;
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! OfertaHorarioServices.getAllByOferta: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! OfertaHorarioServices.getAllByOferta: " + e);
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
		return Search.find("SELECT * FROM acd_oferta_horario", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
