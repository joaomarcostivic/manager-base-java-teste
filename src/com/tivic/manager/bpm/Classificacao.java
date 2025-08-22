package com.tivic.manager.bpm;

public class Classificacao {

	private int cdClassificacao;
	private int cdClassificacaoSuperior;
	private String nmClassificacao;
	private String nrClassificacao;
	private String sgClassificacao;

	public Classificacao(int cdClassificacao,
			int cdClassificacaoSuperior,
			String nmClassificacao,
			String nrClassificacao,
			String sgClassificacao){
		setCdClassificacao(cdClassificacao);
		setCdClassificacaoSuperior(cdClassificacaoSuperior);
		setNmClassificacao(nmClassificacao);
		setNrClassificacao(nrClassificacao);
		setSgClassificacao(sgClassificacao);
	}
	public void setCdClassificacao(int cdClassificacao){ 
		this.cdClassificacao=cdClassificacao;
	}
	public int getCdClassificacao(){
		return this.cdClassificacao;
	}
	public void setCdClassificacaoSuperior(int cdClassificacaoSuperior){
		this.cdClassificacaoSuperior=cdClassificacaoSuperior;
	}
	public int getCdClassificacaoSuperior(){
		return this.cdClassificacaoSuperior;
	}
	public void setNmClassificacao(String nmClassificacao){
		this.nmClassificacao=nmClassificacao;
	}
	public String getNmClassificacao(){
		return this.nmClassificacao;
	}
	public void setNrClassificacao(String nrClassificacao){
		this.nrClassificacao=nrClassificacao;
	}
	public String getNrClassificacao(){
		return this.nrClassificacao;
	}
	public void setSgClassificacao(String sgClassificacao){
		this.sgClassificacao=sgClassificacao;
	}
	public String getSgClassificacao(){
		return this.sgClassificacao;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdClassificacao: " +  getCdClassificacao();
		valueToString += ", cdClassificacaoSuperior: " +  getCdClassificacaoSuperior();
		valueToString += ", nmClassificacao: " +  getNmClassificacao();
		valueToString += ", nrClassificacao: " +  getNrClassificacao();
		valueToString += ", sgClassificacao: " +  getSgClassificacao();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new Classificacao(cdClassificacao,
			cdClassificacaoSuperior,
			nmClassificacao,
			nrClassificacao,
			sgClassificacao);
	}

}