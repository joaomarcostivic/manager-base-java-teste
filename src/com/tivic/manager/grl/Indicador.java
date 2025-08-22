package com.tivic.manager.grl;

import java.util.GregorianCalendar;

public class Indicador {

	private int cdIndicador;
	private String nmIndicador;
	private int tpIndicador;
	private int lgAcumulativo;
	private GregorianCalendar dtVigenciaInicial;
	private GregorianCalendar dtVigenciaFinal;

	public Indicador() { }

	public Indicador(int cdIndicador,
			String nmIndicador,
			int tpIndicador,
			int lgAcumulativo,
			GregorianCalendar dtVigenciaInicial,
			GregorianCalendar dtVigenciaFinal) {
		setCdIndicador(cdIndicador);
		setNmIndicador(nmIndicador);
		setTpIndicador(tpIndicador);
		setLgAcumulativo(lgAcumulativo);
		setDtVigenciaInicial(dtVigenciaInicial);
		setDtVigenciaFinal(dtVigenciaFinal);
	}
	public void setCdIndicador(int cdIndicador){
		this.cdIndicador=cdIndicador;
	}
	public int getCdIndicador(){
		return this.cdIndicador;
	}
	public void setNmIndicador(String nmIndicador){
		this.nmIndicador=nmIndicador;
	}
	public String getNmIndicador(){
		return this.nmIndicador;
	}
	public void setTpIndicador(int tpIndicador){
		this.tpIndicador=tpIndicador;
	}
	public int getTpIndicador(){
		return this.tpIndicador;
	}
	public void setLgAcumulativo(int lgAcumulativo){
		this.lgAcumulativo=lgAcumulativo;
	}
	public int getLgAcumulativo(){
		return this.lgAcumulativo;
	}
	public void setDtVigenciaInicial(GregorianCalendar dtVigenciaInicial){
		this.dtVigenciaInicial=dtVigenciaInicial;
	}
	public GregorianCalendar getDtVigenciaInicial(){
		return this.dtVigenciaInicial;
	}
	public void setDtVigenciaFinal(GregorianCalendar dtVigenciaFinal){
		this.dtVigenciaFinal=dtVigenciaFinal;
	}
	public GregorianCalendar getDtVigenciaFinal(){
		return this.dtVigenciaFinal;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdIndicador: " +  getCdIndicador();
		valueToString += ", nmIndicador: " +  getNmIndicador();
		valueToString += ", tpIndicador: " +  getTpIndicador();
		valueToString += ", lgAcumulativo: " +  getLgAcumulativo();
		valueToString += ", dtVigenciaInicial: " +  sol.util.Util.formatDateTime(getDtVigenciaInicial(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", dtVigenciaFinal: " +  sol.util.Util.formatDateTime(getDtVigenciaFinal(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new Indicador(getCdIndicador(),
			getNmIndicador(),
			getTpIndicador(),
			getLgAcumulativo(),
			getDtVigenciaInicial()==null ? null : (GregorianCalendar)getDtVigenciaInicial().clone(),
			getDtVigenciaFinal()==null ? null : (GregorianCalendar)getDtVigenciaFinal().clone());
	}

}