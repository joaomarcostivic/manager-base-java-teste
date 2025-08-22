package com.tivic.manager.agd;

import java.util.GregorianCalendar;

public class LocalHorario {

	private int cdHorario;
	private GregorianCalendar hrInicio;
	private GregorianCalendar hrTermino;
	private int nrDiaSemana;
	private int cdLocal;
	private int stHorario;

	public LocalHorario(){ }

	public LocalHorario(int cdHorario,
			GregorianCalendar hrInicio,
			GregorianCalendar hrTermino,
			int nrDiaSemana,
			int cdLocal,
			int stHorario){
		setCdHorario(cdHorario);
		setHrInicio(hrInicio);
		setHrTermino(hrTermino);
		setNrDiaSemana(nrDiaSemana);
		setCdLocal(cdLocal);
		setStHorario(stHorario);
	}
	public void setCdHorario(int cdHorario){
		this.cdHorario=cdHorario;
	}
	public int getCdHorario(){
		return this.cdHorario;
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
	public void setCdLocal(int cdLocal){
		this.cdLocal=cdLocal;
	}
	public int getCdLocal(){
		return this.cdLocal;
	}
	public void setStHorario(int stHorario){
		this.stHorario=stHorario;
	}
	public int getStHorario(){
		return this.stHorario;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdHorario: " +  getCdHorario();
		valueToString += ", hrInicio: " +  sol.util.Util.formatDateTime(getHrInicio(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", hrTermino: " +  sol.util.Util.formatDateTime(getHrTermino(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", nrDiaSemana: " +  getNrDiaSemana();
		valueToString += ", cdLocal: " +  getCdLocal();
		valueToString += ", stHorario: " +  getStHorario();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new LocalHorario(getCdHorario(),
			getHrInicio()==null ? null : (GregorianCalendar)getHrInicio().clone(),
			getHrTermino()==null ? null : (GregorianCalendar)getHrTermino().clone(),
			getNrDiaSemana(),
			getCdLocal(),
			getStHorario());
	}

}