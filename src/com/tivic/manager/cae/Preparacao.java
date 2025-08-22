package com.tivic.manager.cae;

public class Preparacao {

	private int cdPreparacao;
	private String nmPreparacao;
	private String txtModoPreparo;
	private int nrMinutos;

	public Preparacao(){ }

	public Preparacao(int cdPreparacao,
			String nmPreparacao,
			String txtModoPreparo,
			int nrMinutos){
		setCdPreparacao(cdPreparacao);
		setNmPreparacao(nmPreparacao);
		setTxtModoPreparo(txtModoPreparo);
		setNrMinutos(nrMinutos);
	}
	public void setCdPreparacao(int cdPreparacao){
		this.cdPreparacao=cdPreparacao;
	}
	public int getCdPreparacao(){
		return this.cdPreparacao;
	}
	public void setNmPreparacao(String nmPreparacao){
		this.nmPreparacao=nmPreparacao;
	}
	public String getNmPreparacao(){
		return this.nmPreparacao;
	}
	public void setTxtModoPreparo(String txtModoPreparo){
		this.txtModoPreparo=txtModoPreparo;
	}
	public String getTxtModoPreparo(){
		return this.txtModoPreparo;
	}
	public void setNrMinutos(int nrMinutos){
		this.nrMinutos=nrMinutos;
	}
	public int getNrMinutos(){
		return this.nrMinutos;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdPreparacao: " +  getCdPreparacao();
		valueToString += ", nmPreparacao: " +  getNmPreparacao();
		valueToString += ", txtModoPreparo: " +  getTxtModoPreparo();
		valueToString += ", nrMinutos: " +  getNrMinutos();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new Preparacao(getCdPreparacao(),
			getNmPreparacao(),
			getTxtModoPreparo(),
			getNrMinutos());
	}

}
