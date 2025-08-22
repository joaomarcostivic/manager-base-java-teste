package com.tivic.manager.mob;

import java.util.GregorianCalendar;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.tivic.sol.serializer.CalendarDeserialize;
import com.tivic.sol.serializer.CalendarSerializer;


@JsonIgnoreProperties(ignoreUnknown = true)
public class CorreiosLote {

	private int cdLote;
	@JsonSerialize(converter = CalendarSerializer.class)
	@JsonDeserialize(converter = CalendarDeserialize.class)
	private GregorianCalendar dtLote;
	private int nrInicial;
	private int nrFinal;
	private int stLote;
	private int tpLote;
	private String sgLote;
	@JsonSerialize(converter = CalendarSerializer.class)
	@JsonDeserialize(converter = CalendarDeserialize.class)
	private GregorianCalendar dtVencimento;
	
	
	public CorreiosLote() { }

	public CorreiosLote(int cdLote,
			GregorianCalendar dtLote,
			int nrInicial,
			int nrFinal,
			int stLote,
			int tpLote,
			String sgLote,
			GregorianCalendar dtVencimento) {
		setCdLote(cdLote);
		setDtLote(dtLote);
		setNrInicial(nrInicial);
		setNrFinal(nrFinal);
		setStLote(stLote);
		setTpLote(tpLote);
		setSgLote(sgLote);
		setDtVencimento(dtVencimento);
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
	public void setNrInicial(int nrInicial){
		this.nrInicial=nrInicial;
	}
	public int getNrInicial(){
		return this.nrInicial;
	}
	public void setNrFinal(int nrFinal){
		this.nrFinal=nrFinal;
	}
	public int getNrFinal(){
		return this.nrFinal;
	}
	public void setStLote(int stLote){
		this.stLote=stLote;
	}
	public int getStLote(){
		return this.stLote;
	}
	public void setTpLote(int tpLote){
		this.tpLote=tpLote;
	}
	public int getTpLote(){
		return this.tpLote;
	}
	public void setSgLote(String sgLote){
		this.sgLote=sgLote;
	}
	public String getSgLote(){
		return this.sgLote;
	}
	public void setDtVencimento(GregorianCalendar dtVencimento){
		this.dtVencimento=dtVencimento;
	}
	public GregorianCalendar getDtVencimento(){
		return this.dtVencimento;
	}
	
	public String toString() {
		String valueToString = "";
		valueToString += "cdLote: " +  getCdLote();
		valueToString += ", dtLote: " +  sol.util.Util.formatDateTime(getDtLote(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", nrInicial: " +  getNrInicial();
		valueToString += ", nrFinal: " +  getNrFinal();
		valueToString += ", stLote: " +  getStLote();
		valueToString += ", tpLote: " +  getTpLote();
		valueToString += ", sgLote: " +  getSgLote();
		valueToString += ", dtVencimento: " +  sol.util.Util.formatDateTime(getDtVencimento(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new CorreiosLote(getCdLote(),
			getDtLote()==null ? null : (GregorianCalendar)getDtLote().clone(),
			getNrInicial(),
			getNrFinal(),
			getStLote(),
			getTpLote(),
			getSgLote(),
			getDtVencimento()==null ? null : (GregorianCalendar)getDtVencimento().clone());
	}

}