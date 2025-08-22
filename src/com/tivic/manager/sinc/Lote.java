package com.tivic.manager.sinc;

import java.util.GregorianCalendar;

public class Lote {

	private int cdLote;
	private GregorianCalendar dtLote;

	public Lote(){ }

	public Lote(int cdLote,
			GregorianCalendar dtLote){
		setCdLote(cdLote);
		setDtLote(dtLote);
	}
	public void setCdLote(int cdLote){
		this.cdLote=cdLote;
	}
	public int getCdLote(){
		return this.cdLote;
	}
	public void setDtLote(GregorianCalendar dtLote){
		this.dtLote=dtLote;
	}
	public GregorianCalendar getDtLote(){
		return this.dtLote;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdLote: " +  getCdLote();
		valueToString += ", dtLote: " +  sol.util.Util.formatDateTime(getDtLote(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new Lote(getCdLote(),
			getDtLote()==null ? null : (GregorianCalendar)getDtLote().clone());
	}

}