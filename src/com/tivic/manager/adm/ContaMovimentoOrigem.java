package com.tivic.manager.adm;

public class ContaMovimentoOrigem {

	private int cdContaMovimentoOrigem;
	private int cdMovimentoConta;
	private int cdConta;
	private int cdContaReceber;

	public ContaMovimentoOrigem(){ }

	public ContaMovimentoOrigem(int cdContaMovimentoOrigem,
			int cdMovimentoConta,
			int cdConta,
			int cdContaReceber){
		setCdContaMovimentoOrigem(cdContaMovimentoOrigem);
		setCdMovimentoConta(cdMovimentoConta);
		setCdConta(cdConta);
		setCdContaReceber(cdContaReceber);
	}
	public void setCdContaMovimentoOrigem(int cdContaMovimentoOrigem){
		this.cdContaMovimentoOrigem=cdContaMovimentoOrigem;
	}
	public int getCdContaMovimentoOrigem(){
		return this.cdContaMovimentoOrigem;
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
	public void setCdContaReceber(int cdContaReceber){
		this.cdContaReceber=cdContaReceber;
	}
	public int getCdContaReceber(){
		return this.cdContaReceber;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdContaMovimentoOrigem: " +  getCdContaMovimentoOrigem();
		valueToString += ", cdMovimentoConta: " +  getCdMovimentoConta();
		valueToString += ", cdConta: " +  getCdConta();
		valueToString += ", cdContaReceber: " +  getCdContaReceber();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new ContaMovimentoOrigem(getCdContaMovimentoOrigem(),
			getCdMovimentoConta(),
			getCdConta(),
			getCdContaReceber());
	}

}