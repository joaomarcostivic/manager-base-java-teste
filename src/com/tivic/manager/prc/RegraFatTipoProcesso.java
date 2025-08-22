package com.tivic.manager.prc;

public class RegraFatTipoProcesso {

	private int cdRegraFaturamento;
	private int cdTipoProcesso;

	public RegraFatTipoProcesso(){ }

	public RegraFatTipoProcesso(int cdRegraFaturamento,
			int cdTipoProcesso){
		setCdRegraFaturamento(cdRegraFaturamento);
		setCdTipoProcesso(cdTipoProcesso);
	}
	public void setCdRegraFaturamento(int cdRegraFaturamento){
		this.cdRegraFaturamento=cdRegraFaturamento;
	}
	public int getCdRegraFaturamento(){
		return this.cdRegraFaturamento;
	}
	public void setCdTipoProcesso(int cdTipoProcesso){
		this.cdTipoProcesso=cdTipoProcesso;
	}
	public int getCdTipoProcesso(){
		return this.cdTipoProcesso;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdRegraFaturamento: " +  getCdRegraFaturamento();
		valueToString += ", cdTipoProcesso: " +  getCdTipoProcesso();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new RegraFatTipoProcesso(getCdRegraFaturamento(),
			getCdTipoProcesso());
	}

}
