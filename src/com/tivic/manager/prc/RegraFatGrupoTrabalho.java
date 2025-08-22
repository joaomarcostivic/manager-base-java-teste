package com.tivic.manager.prc;

public class RegraFatGrupoTrabalho {

	private int cdRegraFaturamento;
	private int cdGrupoTrabalho;

	public RegraFatGrupoTrabalho(){ }

	public RegraFatGrupoTrabalho(int cdRegraFaturamento,
			int cdGrupoTrabalho){
		setCdRegraFaturamento(cdRegraFaturamento);
		setCdGrupoTrabalho(cdGrupoTrabalho);
	}
	public void setCdRegraFaturamento(int cdRegraFaturamento){
		this.cdRegraFaturamento=cdRegraFaturamento;
	}
	public int getCdRegraFaturamento(){
		return this.cdRegraFaturamento;
	}
	public void setCdGrupoTrabalho(int cdGrupoTrabalho){
		this.cdGrupoTrabalho=cdGrupoTrabalho;
	}
	public int getCdGrupoTrabalho(){
		return this.cdGrupoTrabalho;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdRegraFaturamento: " +  getCdRegraFaturamento();
		valueToString += ", cdGrupoTrabalho: " +  getCdGrupoTrabalho();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new RegraFatGrupoTrabalho(getCdRegraFaturamento(),
			getCdGrupoTrabalho());
	}

}
