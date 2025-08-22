package com.tivic.manager.acd;

import java.util.GregorianCalendar;

public class InstituicaoHorario {

	private int cdHorario;
	private int tpTurno;
	private GregorianCalendar hrInicio;
	private GregorianCalendar hrTermino;
	private int nrDiaSemana;
	private int cdInstituicao;
	private int cdPeriodoLetivo;

	public InstituicaoHorario() { }

	public InstituicaoHorario(int cdHorario,
			int tpTurno,
			GregorianCalendar hrInicio,
			GregorianCalendar hrTermino,
			int nrDiaSemana,
			int cdInstituicao,
			int cdPeriodoLetivo) {
		setCdHorario(cdHorario);
		setTpTurno(tpTurno);
		setHrInicio(hrInicio);
		setHrTermino(hrTermino);
		setNrDiaSemana(nrDiaSemana);
		setCdInstituicao(cdInstituicao);
		setCdPeriodoLetivo(cdPeriodoLetivo);
	}
	public void setCdHorario(int cdHorario){
		this.cdHorario=cdHorario;
	}
	public int getCdHorario(){
		return this.cdHorario;
	}
	public void setTpTurno(int tpTurno){
		this.tpTurno=tpTurno;
	}
	public int getTpTurno(){
		return this.tpTurno;
	}
	public void setHrInicio(GregorianCalendar hrInicio){
		this.hrInicio=hrInicio;
	}
	public GregorianCalendar getHrInicio(){
		return this.hrInicio;
	}
	public void setHrTermino(GregorianCalendar hrTermino){
		this.hrTermino=hrTermino;
	}
	public GregorianCalendar getHrTermino(){
		return this.hrTermino;
	}
	public void setNrDiaSemana(int nrDiaSemana){
		this.nrDiaSemana=nrDiaSemana;
	}
	public int getNrDiaSemana(){
		return this.nrDiaSemana;
	}
	public void setCdInstituicao(int cdInstituicao){
		this.cdInstituicao=cdInstituicao;
	}
	public int getCdInstituicao(){
		return this.cdInstituicao;
	}
	public void setCdPeriodoLetivo(int cdPeriodoLetivo){
		this.cdPeriodoLetivo=cdPeriodoLetivo;
	}
	public int getCdPeriodoLetivo(){
		return this.cdPeriodoLetivo;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdHorario: " +  getCdHorario();
		valueToString += ", tpTurno: " +  getTpTurno();
		valueToString += ", hrInicio: " +  sol.util.Util.formatDateTime(getHrInicio(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", hrTermino: " +  sol.util.Util.formatDateTime(getHrTermino(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", nrDiaSemana: " +  getNrDiaSemana();
		valueToString += ", cdInstituicao: " +  getCdInstituicao();
		valueToString += ", cdPeriodoLetivo: " +  getCdPeriodoLetivo();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new InstituicaoHorario(getCdHorario(),
			getTpTurno(),
			getHrInicio()==null ? null : (GregorianCalendar)getHrInicio().clone(),
			getHrTermino()==null ? null : (GregorianCalendar)getHrTermino().clone(),
			getNrDiaSemana(),
			getCdInstituicao(),
			getCdPeriodoLetivo());
	}

}