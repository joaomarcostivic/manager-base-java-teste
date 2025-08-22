package com.tivic.manager.prc;

public class RegraFatTipoAndamento {

	private int cdRegraFaturamento;
	private int cdTipoAndamento;

	public RegraFatTipoAndamento(){ }

	public RegraFatTipoAndamento(int cdRegraFaturamento,
			int cdTipoAndamento){
		setCdRegraFaturamento(cdRegraFaturamento);
		setCdTipoAndamento(cdTipoAndamento);
	}
	public void setCdRegraFaturamento(int cdRegraFaturamento){
		this.cdRegraFaturamento=cdRegraFaturamento;
	}
	public int getCdRegraFaturamento(){
		return this.cdRegraFaturamento;
	}
	public void setCdTipoAndamento(int cdTipoAndamento){
		this.cdTipoAndamento=cdTipoAndamento;
	}
	public int getCdTipoAndamento(){
		return this.cdTipoAndamento;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdRegraFaturamento: " +  getCdRegraFaturamento();
		valueToString += ", cdTipoAndamento: " +  getCdTipoAndamento();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new RegraFatTipoAndamento(getCdRegraFaturamento(),
			getCdTipoAndamento());
	}

}
