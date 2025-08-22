package com.tivic.manager.prc;

public class RegraFatCidade {

	private int cdRegraFaturamento;
	private int cdCidade;

	public RegraFatCidade(){ }

	public RegraFatCidade(int cdRegraFaturamento,
			int cdCidade){
		setCdRegraFaturamento(cdRegraFaturamento);
		setCdCidade(cdCidade);
	}
	public void setCdRegraFaturamento(int cdRegraFaturamento){
		this.cdRegraFaturamento=cdRegraFaturamento;
	}
	public int getCdRegraFaturamento(){
		return this.cdRegraFaturamento;
	}
	public void setCdCidade(int cdCidade){
		this.cdCidade=cdCidade;
	}
	public int getCdCidade(){
		return this.cdCidade;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdRegraFaturamento: " +  getCdRegraFaturamento();
		valueToString += ", cdCidade: " +  getCdCidade();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new RegraFatCidade(getCdRegraFaturamento(),
			getCdCidade());
	}

}
