package com.tivic.manager.ctb;

import java.util.GregorianCalendar;

public class Exercicio {

	private int cdExercicio;
	private String nmExercicio;
	private GregorianCalendar dtInicial;
	private GregorianCalendar dtFinal;

	public Exercicio(){ }

	public Exercicio(int cdExercicio,
			String nmExercicio,
			GregorianCalendar dtInicial,
			GregorianCalendar dtFinal){
		setCdExercicio(cdExercicio);
		setNmExercicio(nmExercicio);
		setDtInicial(dtInicial);
		setDtFinal(dtFinal);
	}
	public void setCdExercicio(int cdExercicio){
		this.cdExercicio=cdExercicio;
	}
	public int getCdExercicio(){
		return this.cdExercicio;
	}
	public void setNmExercicio(String nmExercicio){
		this.nmExercicio=nmExercicio;
	}
	public String getNmExercicio(){
		return this.nmExercicio;
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
		valueToString += "cdExercicio: " +  getCdExercicio();
		valueToString += ", nmExercicio: " +  getNmExercicio();
		valueToString += ", dtInicial: " +  sol.util.Util.formatDateTime(getDtInicial(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", dtFinal: " +  sol.util.Util.formatDateTime(getDtFinal(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new Exercicio(getCdExercicio(),
			getNmExercicio(),
			getDtInicial()==null ? null : (GregorianCalendar)getDtInicial().clone(),
			getDtFinal()==null ? null : (GregorianCalendar)getDtFinal().clone());
	}

}