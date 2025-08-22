package com.tivic.manager.ctb;

import java.util.GregorianCalendar;

public class Competencia {

	private int cdCompetencia;
	private int cdExercicio;
	private String nmCompetencia;
	private GregorianCalendar dtInicial;
	private GregorianCalendar dtFinal;

	public Competencia(){ }

	public Competencia(int cdCompetencia,
			int cdExercicio,
			String nmCompetencia,
			GregorianCalendar dtInicial,
			GregorianCalendar dtFinal){
		setCdCompetencia(cdCompetencia);
		setCdExercicio(cdExercicio);
		setNmCompetencia(nmCompetencia);
		setDtInicial(dtInicial);
		setDtFinal(dtFinal);
	}
	public void setCdCompetencia(int cdCompetencia){
		this.cdCompetencia=cdCompetencia;
	}
	public int getCdCompetencia(){
		return this.cdCompetencia;
	}
	public void setCdExercicio(int cdExercicio){
		this.cdExercicio=cdExercicio;
	}
	public int getCdExercicio(){
		return this.cdExercicio;
	}
	public void setNmCompetencia(String nmCompetencia){
		this.nmCompetencia=nmCompetencia;
	}
	public String getNmCompetencia(){
		return this.nmCompetencia;
	}
	public void setDtInicial(GregorianCalendar dtInicial){
		this.dtInicial=dtInicial;
	}
	public GregorianCalendar getDtInicial(){
		return this.dtInicial;
	}
	public void setDtFinal(GregorianCalendar dtFinal){
		this.dtFinal=dtFinal;
	}
	public GregorianCalendar getDtFinal(){
		return this.dtFinal;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdCompetencia: " +  getCdCompetencia();
		valueToString += ", cdExercicio: " +  getCdExercicio();
		valueToString += ", nmCompetencia: " +  getNmCompetencia();
		valueToString += ", dtInicial: " +  sol.util.Util.formatDateTime(getDtInicial(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", dtFinal: " +  sol.util.Util.formatDateTime(getDtFinal(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new Competencia(getCdCompetencia(),
			getCdExercicio(),
			getNmCompetencia(),
			getDtInicial()==null ? null : (GregorianCalendar)getDtInicial().clone(),
			getDtFinal()==null ? null : (GregorianCalendar)getDtFinal().clone());
	}

}