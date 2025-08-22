package com.tivic.manager.acd;

import java.util.GregorianCalendar;

public class OfertaHorario {

	private int cdHorario;
	private int cdOferta;
	private int nrDiaSemana;
	private GregorianCalendar hrInicio;
	private GregorianCalendar hrTermino;
	private int lgSemanal;
	private int stHorario;
	private int cdHorarioInstituicao;

	public OfertaHorario(){ }

	public OfertaHorario(int cdHorario,
			int cdOferta,
			int nrDiaSemana,
			GregorianCalendar hrInicio,
			GregorianCalendar hrTermino,
			int lgSemanal,
			int stHorario,
			int cdHorarioInstituicao){
		setCdHorario(cdHorario);
		setCdOferta(cdOferta);
		setNrDiaSemana(nrDiaSemana);
		setHrInicio(hrInicio);
		setHrTermino(hrTermino);
		setLgSemanal(lgSemanal);
		setStHorario(stHorario);
		setCdHorarioInstituicao(cdHorarioInstituicao);
	}
	public void setCdHorario(int cdHorario){
		this.cdHorario=cdHorario;
	}
	public int getCdHorario(){
		return this.cdHorario;
	}
	public void setCdOferta(int cdOferta){
		this.cdOferta=cdOferta;
	}
	public int getCdOferta(){
		return this.cdOferta;
	}
	public void setNrDiaSemana(int nrDiaSemana){
		this.nrDiaSemana=nrDiaSemana;
	}
	public int getNrDiaSemana(){
		return this.nrDiaSemana;
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
	public void setLgSemanal(int lgSemanal){
		this.lgSemanal=lgSemanal;
	}
	public int getLgSemanal(){
		return this.lgSemanal;
	}
	public void setStHorario(int stHorario){
		this.stHorario=stHorario;
	}
	public int getStHorario(){
		return this.stHorario;
	}
	public void setCdHorarioInstituicao(int cdHorarioInstituicao){
		this.cdHorarioInstituicao=cdHorarioInstituicao;
	}
	public int getCdHorarioInstituicao(){
		return this.cdHorarioInstituicao;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdHorario: " +  getCdHorario();
		valueToString += ", cdOferta: " +  getCdOferta();
		valueToString += ", nrDiaSemana: " +  getNrDiaSemana();
		valueToString += ", hrInicio: " +  sol.util.Util.formatDateTime(getHrInicio(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", hrTermino: " +  sol.util.Util.formatDateTime(getHrTermino(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", lgSemanal: " +  getLgSemanal();
		valueToString += ", stHorario: " +  getStHorario();
		valueToString += ", cdHorarioInstituicao: " +  getCdHorarioInstituicao();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new OfertaHorario(getCdHorario(),
			getCdOferta(),
			getNrDiaSemana(),
			getHrInicio()==null ? null : (GregorianCalendar)getHrInicio().clone(),
			getHrTermino()==null ? null : (GregorianCalendar)getHrTermino().clone(),
			getLgSemanal(),
			getStHorario(),
			getCdHorarioInstituicao());
	}

}