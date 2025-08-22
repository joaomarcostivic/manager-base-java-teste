package com.tivic.manager.crt;

public class Orgao {

	private int cdOrgao;
	private String nmOrgao;

	public Orgao(int cdOrgao,
			String nmOrgao){
		setCdOrgao(cdOrgao);
		setNmOrgao(nmOrgao);
	}
	public void setCdOrgao(int cdOrgao){
		this.cdOrgao=cdOrgao;
	}
	public int getCdOrgao(){
		return this.cdOrgao;
	}
	public void setNmOrgao(String nmOrgao){
		this.nmOrgao=nmOrgao;
	}
	public String getNmOrgao(){
		return this.nmOrgao;
	}
}