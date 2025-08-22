package com.tivic.manager.adm;

public class PlanoAplicacao {

	private int cdPlanoAplicacao;
	private int cdPlanoAplicacaoSuperior;
	private String nrConta;
	private String nmConta;
	private String idConta;

	public PlanoAplicacao(int cdPlanoAplicacao,
			int cdPlanoAplicacaoSuperior,
			String nrConta,
			String nmConta,
			String idConta){
		setCdPlanoAplicacao(cdPlanoAplicacao);
		setCdPlanoAplicacaoSuperior(cdPlanoAplicacaoSuperior);
		setNrConta(nrConta);
		setNmConta(nmConta);
		setIdConta(idConta);
	}
	public void setCdPlanoAplicacao(int cdPlanoAplicacao){
		this.cdPlanoAplicacao=cdPlanoAplicacao;
	}
	public int getCdPlanoAplicacao(){
		return this.cdPlanoAplicacao;
	}
	public void setCdPlanoAplicacaoSuperior(int cdPlanoAplicacaoSuperior){
		this.cdPlanoAplicacaoSuperior=cdPlanoAplicacaoSuperior;
	}
	public int getCdPlanoAplicacaoSuperior(){
		return this.cdPlanoAplicacaoSuperior;
	}
	public void setNrConta(String nrConta){
		this.nrConta=nrConta;
	}
	public String getNrConta(){
		return this.nrConta;
	}
	public void setNmConta(String nmConta){
		this.nmConta=nmConta;
	}
	public String getNmConta(){
		return this.nmConta;
	}
	public void setIdConta(String idConta){
		this.idConta=idConta;
	}
	public String getIdConta(){
		return this.idConta;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdPlanoAplicacao: " +  getCdPlanoAplicacao();
		valueToString += ", cdPlanoAplicacaoSuperior: " +  getCdPlanoAplicacaoSuperior();
		valueToString += ", nrConta: " +  getNrConta();
		valueToString += ", nmConta: " +  getNmConta();
		valueToString += ", idConta: " +  getIdConta();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new PlanoAplicacao(cdPlanoAplicacao,
			cdPlanoAplicacaoSuperior,
			nrConta,
			nmConta,
			idConta);
	}

}