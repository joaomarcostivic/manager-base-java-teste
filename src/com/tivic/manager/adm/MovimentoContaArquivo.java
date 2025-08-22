package com.tivic.manager.adm;

public class MovimentoContaArquivo {

	private int cdMovimentoConta;
	private int cdConta;
	private int cdArquivo;

	public MovimentoContaArquivo(){ }

	public MovimentoContaArquivo(int cdMovimentoConta,
			int cdConta,
			int cdArquivo){
		setCdMovimentoConta(cdMovimentoConta);
		setCdConta(cdConta);
		setCdArquivo(cdArquivo);
	}
	public void setCdMovimentoConta(int cdMovimentoConta){
		this.cdMovimentoConta=cdMovimentoConta;
	}
	public int getCdMovimentoConta(){
		return this.cdMovimentoConta;
	}
	public void setCdConta(int cdConta){
		this.cdConta=cdConta;
	}
	public int getCdConta(){
		return this.cdConta;
	}
	public void setCdArquivo(int cdArquivo){
		this.cdArquivo=cdArquivo;
	}
	public int getCdArquivo(){
		return this.cdArquivo;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdMovimentoConta: " +  getCdMovimentoConta();
		valueToString += ", cdConta: " +  getCdConta();
		valueToString += ", cdArquivo: " +  getCdArquivo();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new MovimentoContaArquivo(getCdMovimentoConta(),
			getCdConta(),
			getCdArquivo());
	}

}