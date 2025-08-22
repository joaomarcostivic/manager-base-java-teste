package com.tivic.manager.acd;

import java.sql.Timestamp;
import java.util.GregorianCalendar;
import java.util.Map;

import sol.dao.Util;

public class Oferta {

	private int cdOferta;
	private int cdTurma;
	private int cdPeriodoLetivo;
	private GregorianCalendar dtInicio;
	private GregorianCalendar dtTermino;
	private int nrVagas;
	private int nrDias;
	private int tpTurno;
	private float vlDisciplina;
	private int stClasseDisciplina;
	private int cdInstituicaoPratica;
	private int cdSupervisorPratica;
	private int cdProfessor;
	private int tpControleFrequencia;
	private int cdMatriz;
	private int cdCurso;
	private int cdCursoModulo;
	private int cdDisciplina;
	private int cdDependencia;
	private int stOferta;
	
	public Oferta(){ }

	public Oferta(int cdOferta,
			int cdTurma,
			int cdPeriodoLetivo,
			GregorianCalendar dtInicio,
			GregorianCalendar dtTermino,
			int nrVagas,
			int nrDias,
			int tpTurno,
			float vlDisciplina,
			int stClasseDisciplina,
			int cdInstituicaoPratica,
			int cdSupervisorPratica,
			int cdProfessor,
			int tpControleFrequencia,
			int cdMatriz,
			int cdCurso,
			int cdCursoModulo,
			int cdDisciplina,
			int cdDependencia,
			int stOferta){
		setCdOferta(cdOferta);
		setCdTurma(cdTurma);
		setCdPeriodoLetivo(cdPeriodoLetivo);
		setDtInicio(dtInicio);
		setDtTermino(dtTermino);
		setNrVagas(nrVagas);
		setNrDias(nrDias);
		setTpTurno(tpTurno);
		setVlDisciplina(vlDisciplina);
		setStClasseDisciplina(stClasseDisciplina);
		setCdInstituicaoPratica(cdInstituicaoPratica);
		setCdSupervisorPratica(cdSupervisorPratica);
		setCdProfessor(cdProfessor);
		setTpControleFrequencia(tpControleFrequencia);
		setCdMatriz(cdMatriz);
		setCdCurso(cdCurso);
		setCdCursoModulo(cdCursoModulo);
		setCdDisciplina(cdDisciplina);
		setCdDependencia(cdDependencia);
		setStOferta(stOferta);
	}
	public void setCdOferta(int cdOferta){
		this.cdOferta=cdOferta;
	}
	public int getCdOferta(){
		return this.cdOferta;
	}
	public void setCdTurma(int cdTurma){
		this.cdTurma=cdTurma;
	}
	public int getCdTurma(){
		return this.cdTurma;
	}
	public void setCdPeriodoLetivo(int cdPeriodoLetivo){
		this.cdPeriodoLetivo=cdPeriodoLetivo;
	}
	public int getCdPeriodoLetivo(){
		return this.cdPeriodoLetivo;
	}
	public void setDtInicio(GregorianCalendar dtInicio){
		this.dtInicio=dtInicio;
	}
	public GregorianCalendar getDtInicio(){
		return this.dtInicio;
	}
	public void setDtTermino(GregorianCalendar dtTermino){
		this.dtTermino=dtTermino;
	}
	public GregorianCalendar getDtTermino(){
		return this.dtTermino;
	}
	public void setNrVagas(int nrVagas){
		this.nrVagas=nrVagas;
	}
	public int getNrVagas(){
		return this.nrVagas;
	}
	public void setNrDias(int nrDias){
		this.nrDias=nrDias;
	}
	public int getNrDias(){
		return this.nrDias;
	}
	public void setTpTurno(int tpTurno){
		this.tpTurno=tpTurno;
	}
	public int getTpTurno(){
		return this.tpTurno;
	}
	public void setVlDisciplina(float vlDisciplina){
		this.vlDisciplina=vlDisciplina;
	}
	public float getVlDisciplina(){
		return this.vlDisciplina;
	}
	public void setStClasseDisciplina(int stClasseDisciplina){
		this.stClasseDisciplina=stClasseDisciplina;
	}
	public int getStClasseDisciplina(){
		return this.stClasseDisciplina;
	}
	public void setCdInstituicaoPratica(int cdInstituicaoPratica){
		this.cdInstituicaoPratica=cdInstituicaoPratica;
	}
	public int getCdInstituicaoPratica(){
		return this.cdInstituicaoPratica;
	}
	public void setCdSupervisorPratica(int cdSupervisorPratica){
		this.cdSupervisorPratica=cdSupervisorPratica;
	}
	public int getCdSupervisorPratica(){
		return this.cdSupervisorPratica;
	}
	public void setCdProfessor(int cdProfessor){
		this.cdProfessor=cdProfessor;
	}
	public int getCdProfessor(){
		return this.cdProfessor;
	}
	public void setTpControleFrequencia(int tpControleFrequencia){
		this.tpControleFrequencia=tpControleFrequencia;
	}
	public int getTpControleFrequencia(){
		return this.tpControleFrequencia;
	}
	public void setCdMatriz(int cdMatriz){
		this.cdMatriz=cdMatriz;
	}
	public int getCdMatriz(){
		return this.cdMatriz;
	}
	public void setCdCurso(int cdCurso){
		this.cdCurso=cdCurso;
	}
	public int getCdCurso(){
		return this.cdCurso;
	}
	public void setCdCursoModulo(int cdCursoModulo){
		this.cdCursoModulo=cdCursoModulo;
	}
	public int getCdCursoModulo(){
		return this.cdCursoModulo;
	}
	public void setCdDisciplina(int cdDisciplina){
		this.cdDisciplina=cdDisciplina;
	}
	public int getCdDisciplina(){
		return this.cdDisciplina;
	}
	public void setCdDependencia(int cdDependencia){
		this.cdDependencia=cdDependencia;
	}
	public int getCdDependencia(){
		return this.cdDependencia;
	}
	public void setStOferta(int stOferta) {
		this.stOferta = stOferta;
	}
	public int getStOferta() {
		return stOferta;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "\"cdOferta\": " +  getCdOferta();
		valueToString += ", \"cdTurma\": " +  getCdTurma();
		valueToString += ", \"cdPeriodoLetivo\": " +  getCdPeriodoLetivo();
		valueToString += ", \"dtInicio\": \"" +  sol.util.Util.formatDateTime(getDtInicio(), "dd/MM/yyyy HH:mm:ss:SSS", "") + "\"";
		valueToString += ", \"dtTermino\": \"" +  sol.util.Util.formatDateTime(getDtTermino(), "dd/MM/yyyy HH:mm:ss:SSS", "") + "\"";
		valueToString += ", \"nrVagas\": " +  getNrVagas();
		valueToString += ", \"nrDias\": " +  getNrDias();
		valueToString += ", \"tpTurno\": " +  getTpTurno();
		valueToString += ", \"vlDisciplina\": " +  getVlDisciplina();
		valueToString += ", \"stClasseDisciplina\": " +  getStClasseDisciplina();
		valueToString += ", \"cdInstituicaoPratica\": " +  getCdInstituicaoPratica();
		valueToString += ", \"cdSupervisorPratica\": " +  getCdSupervisorPratica();
		valueToString += ", \"cdProfessor\": " +  getCdProfessor();
		valueToString += ", \"tpControleFrequencia\": " +  getTpControleFrequencia();
		valueToString += ", \"cdMatriz\": " +  getCdMatriz();
		valueToString += ", \"cdCurso\": " +  getCdCurso();
		valueToString += ", \"cdCursoModulo\": " +  getCdCursoModulo();
		valueToString += ", \"cdDisciplina\": " +  getCdDisciplina();
		valueToString += ", \"cdDependencia\": " +  getCdDependencia();
		valueToString += ", \"stOferta\": " +  getStOferta();
		return "{" + valueToString + "}";
	}
	
	public static String fromRegister(Map<String, Object> register) {
		String valueToString = "";
		valueToString += "\"cdOferta\": " + register.get("CD_OFERTA");
		valueToString += ", \"cdTurma\": " + register.get("CD_TURMA");
		valueToString += ", \"cdPeriodoLetivo\": " + register.get("CD_PERIODO_LETIVO");
		if(register.get("DT_INICIO") != null)
			valueToString += ", \"dtInicio\": \"" + sol.util.Util.formatDateTime(Util.longToCalendar(((Timestamp)register.get("DT_INICIO")).getTime()), "yyyy-MM-dd", "") +"\"";
		if(register.get("DT_TERMINO") != null)
			valueToString += ", \"dtTermino\": \"" + sol.util.Util.formatDateTime(Util.longToCalendar(((Timestamp)register.get("DT_TERMINO")).getTime()), "yyyy-MM-dd", "") +"\"";
		valueToString += ", \"nrVagas\": " + register.get("NR_VAGAS");
		valueToString += ", \"nrDias\": " + register.get("NR_DIAS");
		valueToString += ", \"tpTurno\": " + register.get("TP_TURNO");
		valueToString += ", \"vlDisciplina\": " + register.get("VL_DISCIPLINA");
		valueToString += ", \"stClasseDisciplina\": " + register.get("ST_CLASSE_DISCIPLINA");
		valueToString += ", \"cdInstituicaoPratica\": " + register.get("CD_INSTITUICAO_PRATICA");
		valueToString += ", \"cdSupervisorPratica\": " + register.get("CD_SUPERVISOR_PRATICA");
		valueToString += ", \"cdProfessor\": " + register.get("CD_PROFESSOR");
		valueToString += ", \"tpControleFrequencia\": " + register.get("TP_CONTROLE_FREQUENCIA");
		valueToString += ", \"cdMatriz\": " + register.get("CD_MATRIZ");
		valueToString += ", \"cdCurso\": " + register.get("CD_CURSO");
		valueToString += ", \"cdCursoModulo\": " + register.get("CD_CURSO_MODULO");
		valueToString += ", \"cdDisciplina\": " + register.get("CD_DISCIPLINA");
		valueToString += ", \"cdDependencia\": " + register.get("CD_DEPENDENCIA");
		valueToString += ", \"stOferta\": " + register.get("ST_OFERTA");
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new Oferta(getCdOferta(),
			getCdTurma(),
			getCdPeriodoLetivo(),
			getDtInicio()==null ? null : (GregorianCalendar)getDtInicio().clone(),
			getDtTermino()==null ? null : (GregorianCalendar)getDtTermino().clone(),
			getNrVagas(),
			getNrDias(),
			getTpTurno(),
			getVlDisciplina(),
			getStClasseDisciplina(),
			getCdInstituicaoPratica(),
			getCdSupervisorPratica(),
			getCdProfessor(),
			getTpControleFrequencia(),
			getCdMatriz(),
			getCdCurso(),
			getCdCursoModulo(),
			getCdDisciplina(),
			getCdDependencia(),
			getStOferta());
	}

}