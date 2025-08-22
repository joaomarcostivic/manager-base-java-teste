package com.tivic.manager.fsc;

public class TipoContribuicaoSocial {

	private int cdTipoContribuicaoSocial;
	private String nmTipoContribuicaoSocial;
	private String nrTipoContribuicaoSocial;
	
	public TipoContribuicaoSocial(int cdTipoContribuicaoSocial,
			String nmTipoContribuicaoSocial,
			String nrTipoContribuicaoSocial){
		setCdTipoContribuicaoSocial(cdTipoContribuicaoSocial);
		setNmTipoContribuicaoSocial(nmTipoContribuicaoSocial);
		setNrTipoContribuicaoSocial(nrTipoContribuicaoSocial);
	}
	public void setCdTipoContribuicaoSocial(int cdTipoContribuicaoSocial){
		this.cdTipoContribuicaoSocial=cdTipoContribuicaoSocial;
	}
	public int getCdTipoContribuicaoSocial(){
		return this.cdTipoContribuicaoSocial;
	}
	public void setNmTipoContribuicaoSocial(String nmTipoContribuicaoSocial){
		this.nmTipoContribuicaoSocial=nmTipoContribuicaoSocial;
	}
	public String getNmTipoContribuicaoSocial(){
		return this.nmTipoContribuicaoSocial;
	}
	public void setNrTipoContribuicaoSocial(String nrTipoContribuicaoSocial){
		this.nrTipoContribuicaoSocial=nrTipoContribuicaoSocial;
	}
	public String getNrTipoContribuicaoSocial(){
		return this.nrTipoContribuicaoSocial;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdTipoContribuicaoSocial: " +  getCdTipoContribuicaoSocial();
		valueToString += ", nmTipoContribuicaoSocial: " +  getNmTipoContribuicaoSocial();
		valueToString += ", nrTipoContribuicaoSocial: " +  getNrTipoContribuicaoSocial();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new TipoContribuicaoSocial(getCdTipoContribuicaoSocial(),
			getNmTipoContribuicaoSocial(),
			getNrTipoContribuicaoSocial());
	}

}