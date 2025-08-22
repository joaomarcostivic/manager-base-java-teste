package com.tivic.manager.prc;

public class TipoOrgao {

	private int cdTipoOrgao;
	private String nmTipoOrgao;

	public TipoOrgao(int cdTipoOrgao,
			String nmTipoOrgao){
		setCdTipoOrgao(cdTipoOrgao);
		setNmTipoOrgao(nmTipoOrgao);
	}
	public void setCdTipoOrgao(int cdTipoOrgao){
		this.cdTipoOrgao=cdTipoOrgao;
	}
	public int getCdTipoOrgao(){
		return this.cdTipoOrgao;
	}
	public void setNmTipoOrgao(String nmTipoOrgao){
		this.nmTipoOrgao=nmTipoOrgao;
	}
	public String getNmTipoOrgao(){
		return this.nmTipoOrgao;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdTipoOrgao: " +  getCdTipoOrgao();
		valueToString += ", nmTipoOrgao: " +  getNmTipoOrgao();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new TipoOrgao(cdTipoOrgao,
			nmTipoOrgao);
	}

}