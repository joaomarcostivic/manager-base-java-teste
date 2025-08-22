package com.tivic.manager.adm;

public class ContaPagarEvento {

	private int cdContaPagar;
	private int cdEventoFinanceiro;
	private float vlEventoFinanceiro;

	public ContaPagarEvento(int cdContaPagar,
			int cdEventoFinanceiro,
			float vlEventoFinanceiro){
		setCdContaPagar(cdContaPagar);
		setCdEventoFinanceiro(cdEventoFinanceiro);
		setVlEventoFinanceiro(vlEventoFinanceiro);
	}
	public void setCdContaPagar(int cdContaPagar){
		this.cdContaPagar=cdContaPagar;
	}
	public int getCdContaPagar(){
		return this.cdContaPagar;
	}
	public void setCdEventoFinanceiro(int cdEventoFinanceiro){
		this.cdEventoFinanceiro=cdEventoFinanceiro;
	}
	public int getCdEventoFinanceiro(){
		return this.cdEventoFinanceiro;
	}
	public void setVlEventoFinanceiro(float vlEventoFinanceiro){
		this.vlEventoFinanceiro=vlEventoFinanceiro;
	}
	public float getVlEventoFinanceiro(){
		return this.vlEventoFinanceiro;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdContaPagar: " +  getCdContaPagar();
		valueToString += ", cdEventoFinanceiro: " +  getCdEventoFinanceiro();
		valueToString += ", vlEventoFinanceiro: " +  getVlEventoFinanceiro();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new ContaPagarEvento(getCdContaPagar(),
			getCdEventoFinanceiro(),
			getVlEventoFinanceiro());
	}

}