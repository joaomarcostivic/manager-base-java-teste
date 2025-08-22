package com.tivic.manager.adm;

public class ContaReceberNegociacao {

	private int cdContrato;
	private int cdNegociacao;
	private int cdContaReceber;

	public ContaReceberNegociacao(int cdContrato,
			int cdNegociacao,
			int cdContaReceber){
		setCdContrato(cdContrato);
		setCdNegociacao(cdNegociacao);
		setCdContaReceber(cdContaReceber);
	}
	public void setCdContrato(int cdContrato){
		this.cdContrato=cdContrato;
	}
	public int getCdContrato(){
		return this.cdContrato;
	}
	public void setCdNegociacao(int cdNegociacao){
		this.cdNegociacao=cdNegociacao;
	}
	public int getCdNegociacao(){
		return this.cdNegociacao;
	}
	public void setCdContaReceber(int cdContaReceber){
		this.cdContaReceber=cdContaReceber;
	}
	public int getCdContaReceber(){
		return this.cdContaReceber;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdContrato: " +  getCdContrato();
		valueToString += ", cdNegociacao: " +  getCdNegociacao();
		valueToString += ", cdContaReceber: " +  getCdContaReceber();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new ContaReceberNegociacao(getCdContrato(),
			getCdNegociacao(),
			getCdContaReceber());
	}

}
