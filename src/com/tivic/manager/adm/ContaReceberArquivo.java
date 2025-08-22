package com.tivic.manager.adm;

public class ContaReceberArquivo {

	private int cdContaReceber;
	private int cdArquivo;
	private int cdNivelCobranca;
	private int cdCobranca;

	public ContaReceberArquivo(){ }

	public ContaReceberArquivo(int cdContaReceber,
			int cdArquivo,
			int cdNivelCobranca,
			int cdCobranca){
		setCdContaReceber(cdContaReceber);
		setCdArquivo(cdArquivo);
		setCdNivelCobranca(cdNivelCobranca);
		setCdCobranca(cdCobranca);
	}
	public void setCdContaReceber(int cdContaReceber){
		this.cdContaReceber=cdContaReceber;
	}
	public int getCdContaReceber(){
		return this.cdContaReceber;
	}
	public void setCdArquivo(int cdArquivo){
		this.cdArquivo=cdArquivo;
	}
	public int getCdArquivo(){
		return this.cdArquivo;
	}
	public void setCdNivelCobranca(int cdNivelCobranca){
		this.cdNivelCobranca=cdNivelCobranca;
	}
	public int getCdNivelCobranca(){
		return this.cdNivelCobranca;
	}
	public void setCdCobranca(int cdCobranca){
		this.cdCobranca=cdCobranca;
	}
	public int getCdCobranca(){
		return this.cdCobranca;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdContaReceber: " +  getCdContaReceber();
		valueToString += ", cdArquivo: " +  getCdArquivo();
		valueToString += ", cdNivelCobranca: " +  getCdNivelCobranca();
		valueToString += ", cdCobranca: " +  getCdCobranca();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new ContaReceberArquivo(getCdContaReceber(),
			getCdArquivo(),
			getCdNivelCobranca(),
			getCdCobranca());
	}

}