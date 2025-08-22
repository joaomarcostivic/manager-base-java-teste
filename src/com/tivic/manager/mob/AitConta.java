package com.tivic.manager.mob;

import java.util.GregorianCalendar;

public class AitConta {

	private int cdAit;
	private int cdAitConta;
	private int cdContaReceber;
	private String nrAit;
	private Double vlPagamento;
	private GregorianCalendar dtPagamento;
	private int stAitConta;

	public AitConta(){ }

	public AitConta(int cdAit,
			int cdAitConta,
			int cdContaReceber,
			String nrAit,
			Double vlPagamento,
			GregorianCalendar dtPagamento,
			int stAitConta){
		setCdAit(cdAit);
		setCdAitConta(cdAitConta);
		setCdContaReceber(cdContaReceber);
		setNrAit(nrAit);
		setVlPagamento(vlPagamento);
		setDtPagamento(dtPagamento);
		setStAitConta(stAitConta);
	}
	public void setCdAit(int cdAit){
		this.cdAit=cdAit;
	}
	public int getCdAit(){
		return this.cdAit;
	}
	public void setCdAitConta(int cdAitConta){
		this.cdAitConta=cdAitConta;
	}
	public int getCdAitConta(){
		return this.cdAitConta;
	}
	public void setCdContaReceber(int cdContaReceber){
		this.cdContaReceber=cdContaReceber;
	}
	public int getCdContaReceber(){
		return this.cdContaReceber;
	}
	public void setNrAit(String nrAit){
		this.nrAit=nrAit;
	}
	public String getNrAit(){
		return this.nrAit;
	}
	public void setVlPagamento(Double vlPagamento){
		this.vlPagamento=vlPagamento;
	}
	public Double getVlPagamento(){
		return this.vlPagamento;
	}
	public void setDtPagamento(GregorianCalendar dtPagamento){
		this.dtPagamento=dtPagamento;
	}
	public GregorianCalendar getDtPagamento(){
		return this.dtPagamento;
	}
	public void setStAitConta(int stAitConta){
		this.stAitConta=stAitConta;
	}
	public int getStAitConta(){
		return this.stAitConta;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdAit: " +  getCdAit();
		valueToString += ", cdAitConta: " +  getCdAitConta();
		valueToString += ", cdContaReceber: " +  getCdContaReceber();
		valueToString += ", nrAit: " +  getNrAit();
		valueToString += ", vlPagamento: " +  getVlPagamento();
		valueToString += ", dtPagamento: " +  sol.util.Util.formatDateTime(getDtPagamento(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", stAitConta: " +  getStAitConta();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new AitConta(getCdAit(),
			getCdAitConta(),
			getCdContaReceber(),
			getNrAit(),
			getVlPagamento(),
			getDtPagamento()==null ? null : (GregorianCalendar)getDtPagamento().clone(),
			getStAitConta());
	}

}