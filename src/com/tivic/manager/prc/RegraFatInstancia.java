package com.tivic.manager.prc;

public class RegraFatInstancia {

	private int cdRegraFaturamento;
	private int tpInstancia;

	public RegraFatInstancia(){ }

	public RegraFatInstancia(int cdRegraFaturamento,
			int tpInstancia){
		setCdRegraFaturamento(cdRegraFaturamento);
		setTpInstancia(tpInstancia);
	}
	public void setCdRegraFaturamento(int cdRegraFaturamento){
		this.cdRegraFaturamento=cdRegraFaturamento;
	}
	public int getCdRegraFaturamento(){
		return this.cdRegraFaturamento;
	}
	public void setTpInstancia(int tpInstancia){
		this.tpInstancia=tpInstancia;
	}
	public int getTpInstancia(){
		return this.tpInstancia;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdRegraFaturamento: " +  getCdRegraFaturamento();
		valueToString += ", tpInstancia: " +  getTpInstancia();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new RegraFatInstancia(getCdRegraFaturamento(),
			getTpInstancia());
	}

}
