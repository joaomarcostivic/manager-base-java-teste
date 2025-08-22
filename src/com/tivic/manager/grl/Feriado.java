package com.tivic.manager.grl;

import java.util.GregorianCalendar;

public class Feriado {

	private int cdFeriado;
	private String nmFeriado;
	private GregorianCalendar dtFeriado;
	private int tpFeriado;
	private String idFeriado;
	private int cdEstado;

	public Feriado() { }
			
	public Feriado(int cdFeriado,
			String nmFeriado,
			GregorianCalendar dtFeriado,
			int tpFeriado,
			String idFeriado,
			int cdEstado){
		setCdFeriado(cdFeriado);
		setNmFeriado(nmFeriado);
		setDtFeriado(dtFeriado);
		setTpFeriado(tpFeriado);
		setIdFeriado(idFeriado);
		setCdEstado(cdEstado);
	}
	public void setCdFeriado(int cdFeriado){
		this.cdFeriado=cdFeriado;
	}
	public int getCdFeriado(){
		return this.cdFeriado;
	}
	public void setNmFeriado(String nmFeriado){
		this.nmFeriado=nmFeriado;
	}
	public String getNmFeriado(){
		return this.nmFeriado;
	}
	public void setDtFeriado(GregorianCalendar dtFeriado){
		this.dtFeriado=dtFeriado;
	}
	public GregorianCalendar getDtFeriado(){
		return this.dtFeriado;
	}
	public void setTpFeriado(int tpFeriado){
		this.tpFeriado=tpFeriado;
	}
	public int getTpFeriado(){
		return this.tpFeriado;
	}
	public void setIdFeriado(String idFeriado){
		this.idFeriado=idFeriado;
	}
	public String getIdFeriado(){
		return this.idFeriado;
	}
	public void setCdEstado(int cdEstado){
		this.cdEstado=cdEstado;
	}
	public int getCdEstado(){
		return this.cdEstado;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdFeriado: " +  getCdFeriado();
		valueToString += ", nmFeriado: " +  getNmFeriado();
		valueToString += ", dtFeriado: " +  sol.util.Util.formatDateTime(getDtFeriado(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", tpFeriado: " +  getTpFeriado();
		valueToString += ", idFeriado: " +  getIdFeriado();
		valueToString += ", cdEstado: " +  getCdEstado();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new Feriado(getCdFeriado(),
			getNmFeriado(),
			getDtFeriado()==null ? null : (GregorianCalendar)getDtFeriado().clone(),
			getTpFeriado(),
			getIdFeriado(),
			getCdEstado());
	}

}
