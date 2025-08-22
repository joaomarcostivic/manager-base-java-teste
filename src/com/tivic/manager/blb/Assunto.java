package com.tivic.manager.blb;

public class Assunto {

	private int cdAssunto;
	private String nmAssunto;
	private String idAssunto;
	private int tpTabelaAssunto;
	private int cdAssuntoSuperior;

	public Assunto(){ }

	public Assunto(int cdAssunto,
			String nmAssunto,
			String idAssunto,
			int tpTabelaAssunto,
			int cdAssuntoSuperior){
		setCdAssunto(cdAssunto);
		setNmAssunto(nmAssunto);
		setIdAssunto(idAssunto);
		setTpTabelaAssunto(tpTabelaAssunto);
		setCdAssuntoSuperior(cdAssuntoSuperior);
	}
	public void setCdAssunto(int cdAssunto){
		this.cdAssunto=cdAssunto;
	}
	public int getCdAssunto(){
		return this.cdAssunto;
	}
	public void setNmAssunto(String nmAssunto){
		this.nmAssunto=nmAssunto;
	}
	public String getNmAssunto(){
		return this.nmAssunto;
	}
	public void setIdAssunto(String idAssunto){
		this.idAssunto=idAssunto;
	}
	public String getIdAssunto(){
		return this.idAssunto;
	}
	public void setTpTabelaAssunto(int tpTabelaAssunto){
		this.tpTabelaAssunto=tpTabelaAssunto;
	}
	public int getTpTabelaAssunto(){
		return this.tpTabelaAssunto;
	}
	public void setCdAssuntoSuperior(int cdAssuntoSuperior){
		this.cdAssuntoSuperior=cdAssuntoSuperior;
	}
	public int getCdAssuntoSuperior(){
		return this.cdAssuntoSuperior;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdAssunto: " +  getCdAssunto();
		valueToString += ", nmAssunto: " +  getNmAssunto();
		valueToString += ", idAssunto: " +  getIdAssunto();
		valueToString += ", tpTabelaAssunto: " +  getTpTabelaAssunto();
		valueToString += ", cdAssuntoSuperior: " +  getCdAssuntoSuperior();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new Assunto(getCdAssunto(),
			getNmAssunto(),
			getIdAssunto(),
			getTpTabelaAssunto(),
			getCdAssuntoSuperior());
	}

}