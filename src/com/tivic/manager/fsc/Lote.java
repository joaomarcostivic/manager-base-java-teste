package com.tivic.manager.fsc;

import java.util.GregorianCalendar;

public class Lote {

	private int cdLote;
	private GregorianCalendar dtTransmissao;
	private GregorianCalendar dtLote;
	private String nrLote;
	private int stLte;

	public Lote(int cdLote,
			GregorianCalendar dtTransmissao,
			GregorianCalendar dtLote,
			String nrLote,
			int stLte){
		setCdLote(cdLote);
		setDtTransmissao(dtTransmissao);
		setDtLote(dtLote);
		setNrLote(nrLote);
		setStLte(stLte);
	}
	public void setCdLote(int cdLote){
		this.cdLote=cdLote;
	}
	public int getCdLote(){
		return this.cdLote;
	}
	public void setDtTransmissao(GregorianCalendar dtTransmissao){
		this.dtTransmissao=dtTransmissao;
	}
	public GregorianCalendar getDtTransmissao(){
		return this.dtTransmissao;
	}
	public void setDtLote(GregorianCalendar dtLote){
		this.dtLote=dtLote;
	}
	public GregorianCalendar getDtLote(){
		return this.dtLote;
	}
	public void setNrLote(String nrLote){
		this.nrLote=nrLote;
	}
	public String getNrLote(){
		return this.nrLote;
	}
	public void setStLte(int stLte){
		this.stLte=stLte;
	}
	public int getStLte(){
		return this.stLte;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdLote: " +  getCdLote();
		valueToString += ", dtTransmissao: " +  sol.util.Util.formatDateTime(getDtTransmissao(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", dtLote: " +  sol.util.Util.formatDateTime(getDtLote(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", nrLote: " +  getNrLote();
		valueToString += ", stLte: " +  getStLte();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new Lote(getCdLote(),
			getDtTransmissao()==null ? null : (GregorianCalendar)getDtTransmissao().clone(),
			getDtLote()==null ? null : (GregorianCalendar)getDtLote().clone(),
			getNrLote(),
			getStLte());
	}

}