package com.tivic.manager.prc;

public class RegraFatGrupoProcesso {

	private int cdRegraFaturamento;
	private int cdGrupoProcesso;

	public RegraFatGrupoProcesso(){ }

	public RegraFatGrupoProcesso(int cdRegraFaturamento,
			int cdGrupoProcesso){
		setCdRegraFaturamento(cdRegraFaturamento);
		setCdGrupoProcesso(cdGrupoProcesso);
	}
	public void setCdRegraFaturamento(int cdRegraFaturamento){
		this.cdRegraFaturamento=cdRegraFaturamento;
	}
	public int getCdRegraFaturamento(){
		return this.cdRegraFaturamento;
	}
	public void setCdGrupoProcesso(int cdGrupoProcesso){
		this.cdGrupoProcesso=cdGrupoProcesso;
	}
	public int getCdGrupoProcesso(){
		return this.cdGrupoProcesso;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdRegraFaturamento: " +  getCdRegraFaturamento();
		valueToString += ", cdGrupoProcesso: " +  getCdGrupoProcesso();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new RegraFatGrupoProcesso(getCdRegraFaturamento(),
			getCdGrupoProcesso());
	}

}
