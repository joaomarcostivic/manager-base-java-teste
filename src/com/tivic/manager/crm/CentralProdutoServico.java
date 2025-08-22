package com.tivic.manager.crm;

public class CentralProdutoServico {

	private int cdCentral;
	private int cdProdutoServico;
	private int stProdutoServico;

	public CentralProdutoServico(int cdCentral,
			int cdProdutoServico,
			int stProdutoServico){
		setCdCentral(cdCentral);
		setCdProdutoServico(cdProdutoServico);
		setStProdutoServico(stProdutoServico);
	}
	public void setCdCentral(int cdCentral){
		this.cdCentral=cdCentral;
	}
	public int getCdCentral(){
		return this.cdCentral;
	}
	public void setCdProdutoServico(int cdProdutoServico){
		this.cdProdutoServico=cdProdutoServico;
	}
	public int getCdProdutoServico(){
		return this.cdProdutoServico;
	}
	public void setStProdutoServico(int stProdutoServico){
		this.stProdutoServico=stProdutoServico;
	}
	public int getStProdutoServico(){
		return this.stProdutoServico;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdCentral: " +  getCdCentral();
		valueToString += ", cdProdutoServico: " +  getCdProdutoServico();
		valueToString += ", stProdutoServico: " +  getStProdutoServico();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new CentralProdutoServico(getCdCentral(),
			getCdProdutoServico(),
			getStProdutoServico());
	}

}
