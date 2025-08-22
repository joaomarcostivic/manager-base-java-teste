package com.tivic.manager.prc;

public class RegraFatTipoPrazo {

	private int cdRegraFaturamento;
	private int cdTipoPrazo;

	public RegraFatTipoPrazo(){ }

	public RegraFatTipoPrazo(int cdRegraFaturamento,
			int cdTipoPrazo){
		setCdRegraFaturamento(cdRegraFaturamento);
		setCdTipoPrazo(cdTipoPrazo);
	}
	public void setCdRegraFaturamento(int cdRegraFaturamento){
		this.cdRegraFaturamento=cdRegraFaturamento;
	}
	public int getCdRegraFaturamento(){
		return this.cdRegraFaturamento;
	}
	public void setCdTipoPrazo(int cdTipoPrazo){
		this.cdTipoPrazo=cdTipoPrazo;
	}
	public int getCdTipoPrazo(){
		return this.cdTipoPrazo;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdRegraFaturamento: " +  getCdRegraFaturamento();
		valueToString += ", cdTipoPrazo: " +  getCdTipoPrazo();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new RegraFatTipoPrazo(getCdRegraFaturamento(),
			getCdTipoPrazo());
	}

}
