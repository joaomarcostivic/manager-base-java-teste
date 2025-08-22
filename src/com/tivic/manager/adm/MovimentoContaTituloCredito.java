package com.tivic.manager.adm;

public class MovimentoContaTituloCredito {

	private int cdTituloCredito;
	private int cdMovimentoConta;
	private int cdConta;
	
	public MovimentoContaTituloCredito(){}
	
	public MovimentoContaTituloCredito(int cdTituloCredito,
			int cdMovimentoConta,
			int cdConta){
		setCdTituloCredito(cdTituloCredito);
		setCdMovimentoConta(cdMovimentoConta);
		setCdConta(cdConta);
	}
	public void setCdTituloCredito(int cdTituloCredito){
		this.cdTituloCredito=cdTituloCredito;
	}
	public int getCdTituloCredito(){
		return this.cdTituloCredito;
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
	public String toString() {
		String valueToString = "";
		valueToString += "cdTituloCredito: " +  getCdTituloCredito();
		valueToString += ", cdMovimentoConta: " +  getCdMovimentoConta();
		valueToString += ", cdConta: " +  getCdConta();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new MovimentoContaTituloCredito(getCdTituloCredito(),
			getCdMovimentoConta(),
			getCdConta());
	}

}