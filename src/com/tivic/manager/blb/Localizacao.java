package com.tivic.manager.blb;

public class Localizacao {

	private int cdLocalizacao;
	private String nmLocalizacao;
	private String nrLocalizacao;
	private int nrLocalizacaoFinal;
	private int cdLocalizacaoSuperior;
	private int cdInstituicao;
	private int cdDependencia;

	public Localizacao(){ }

	public Localizacao(int cdLocalizacao,
			String nmLocalizacao,
			String nrLocalizacao,
			int nrLocalizacaoFinal,
			int cdLocalizacaoSuperior,
			int cdInstituicao,
			int cdDependencia){
		setCdLocalizacao(cdLocalizacao);
		setNmLocalizacao(nmLocalizacao);
		setNrLocalizacao(nrLocalizacao);
		setNrLocalizacaoFinal(nrLocalizacaoFinal);
		setCdLocalizacaoSuperior(cdLocalizacaoSuperior);
		setCdInstituicao(cdInstituicao);
		setCdDependencia(cdDependencia);
	}
	public void setCdLocalizacao(int cdLocalizacao){
		this.cdLocalizacao=cdLocalizacao;
	}
	public int getCdLocalizacao(){
		return this.cdLocalizacao;
	}
	public void setNmLocalizacao(String nmLocalizacao){
		this.nmLocalizacao=nmLocalizacao;
	}
	public String getNmLocalizacao(){
		return this.nmLocalizacao;
	}
	public void setNrLocalizacao(String nrLocalizacao){
		this.nrLocalizacao=nrLocalizacao;
	}
	public String getNrLocalizacao(){
		return this.nrLocalizacao;
	}
	public void setNrLocalizacaoFinal(int nrLocalizacaoFinal){
		this.nrLocalizacaoFinal=nrLocalizacaoFinal;
	}
	public int getNrLocalizacaoFinal(){
		return this.nrLocalizacaoFinal;
	}
	public void setCdLocalizacaoSuperior(int cdLocalizacaoSuperior){
		this.cdLocalizacaoSuperior=cdLocalizacaoSuperior;
	}
	public int getCdLocalizacaoSuperior(){
		return this.cdLocalizacaoSuperior;
	}
	public void setCdInstituicao(int cdInstituicao){
		this.cdInstituicao=cdInstituicao;
	}
	public int getCdInstituicao(){
		return this.cdInstituicao;
	}
	public void setCdDependencia(int cdDependencia){
		this.cdDependencia=cdDependencia;
	}
	public int getCdDependencia(){
		return this.cdDependencia;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdLocalizacao: " +  getCdLocalizacao();
		valueToString += ", nmLocalizacao: " +  getNmLocalizacao();
		valueToString += ", nrLocalizacao: " +  getNrLocalizacao();
		valueToString += ", nrLocalizacaoFinal: " +  getNrLocalizacaoFinal();
		valueToString += ", cdLocalizacaoSuperior: " +  getCdLocalizacaoSuperior();
		valueToString += ", cdInstituicao: " +  getCdInstituicao();
		valueToString += ", cdDependencia: " +  getCdDependencia();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new Localizacao(getCdLocalizacao(),
			getNmLocalizacao(),
			getNrLocalizacao(),
			getNrLocalizacaoFinal(),
			getCdLocalizacaoSuperior(),
			getCdInstituicao(),
			getCdDependencia());
	}

}