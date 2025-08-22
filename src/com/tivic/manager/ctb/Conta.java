package com.tivic.manager.ctb;

public class Conta {

	private int cdConta;
	private int cdHistoricoPadrao;
	private String nmConta;
	private String nrContaReduzida;
	private String nrContaImpressao;

	public Conta(int cdConta,
			int cdHistoricoPadrao,
			String nmConta,
			String nrContaReduzida,
			String nrContaImpressao){
		setCdConta(cdConta);
		setCdHistoricoPadrao(cdHistoricoPadrao);
		setNmConta(nmConta);
		setNrContaReduzida(nrContaReduzida);
		setNrContaImpressao(nrContaImpressao);
	}
	public void setCdConta(int cdConta){
		this.cdConta=cdConta;
	}
	public int getCdConta(){
		return this.cdConta;
	}
	public void setCdHistoricoPadrao(int cdHistoricoPadrao){
		this.cdHistoricoPadrao=cdHistoricoPadrao;
	}
	public int getCdHistoricoPadrao(){
		return this.cdHistoricoPadrao;
	}
	public void setNmConta(String nmConta){
		this.nmConta=nmConta;
	}
	public String getNmConta(){
		return this.nmConta;
	}
	public void setNrContaReduzida(String nrContaReduzida){
		this.nrContaReduzida=nrContaReduzida;
	}
	public String getNrContaReduzida(){
		return this.nrContaReduzida;
	}
	public void setNrContaImpressao(String nrContaImpressao){
		this.nrContaImpressao=nrContaImpressao;
	}
	public String getNrContaImpressao(){
		return this.nrContaImpressao;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdConta: " +  getCdConta();
		valueToString += ", cdHistoricoPadrao: " +  getCdHistoricoPadrao();
		valueToString += ", nmConta: " +  getNmConta();
		valueToString += ", nrContaReduzida: " +  getNrContaReduzida();
		valueToString += ", nrContaImpressao: " +  getNrContaImpressao();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new Conta(getCdConta(),
			getCdHistoricoPadrao(),
			getNmConta(),
			getNrContaReduzida(),
			getNrContaImpressao());
	}

}
