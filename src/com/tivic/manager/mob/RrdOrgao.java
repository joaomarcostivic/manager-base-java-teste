package com.tivic.manager.mob;

public class RrdOrgao {

	private int cdRrdOrgao;
	private String idRrdOrgao;
	private String nmRrdOrgao;
	private int stRrdOrgao;

	public RrdOrgao() { }

	public RrdOrgao(int cdRrdOrgao,
			String idRrdOrgao,
			String nmRrdOrgao,
			int stRrdOrgao) {
		setCdRrdOrgao(cdRrdOrgao);
		setIdRrdOrgao(idRrdOrgao);
		setNmRrdOrgao(nmRrdOrgao);
		setStRrdOrgao(stRrdOrgao);
	}
	public void setCdRrdOrgao(int cdRrdOrgao){
		this.cdRrdOrgao=cdRrdOrgao;
	}
	public int getCdRrdOrgao(){
		return this.cdRrdOrgao;
	}
	public void setIdRrdOrgao(String idRrdOrgao){
		this.idRrdOrgao=idRrdOrgao;
	}
	public String getIdRrdOrgao(){
		return this.idRrdOrgao;
	}
	public void setNmRrdOrgao(String nmRrdOrgao){
		this.nmRrdOrgao=nmRrdOrgao;
	}
	public String getNmRrdOrgao(){
		return this.nmRrdOrgao;
	}
	public void setStRrdOrgao(int stRrdOrgao){
		this.stRrdOrgao=stRrdOrgao;
	}
	public int getStRrdOrgao(){
		return this.stRrdOrgao;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdRrdOrgao: " +  getCdRrdOrgao();
		valueToString += ", idRrdOrgao: " +  getIdRrdOrgao();
		valueToString += ", nmRrdOrgao: " +  getNmRrdOrgao();
		valueToString += ", stRrdOrgao: " +  getStRrdOrgao();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new RrdOrgao(getCdRrdOrgao(),
			getIdRrdOrgao(),
			getNmRrdOrgao(),
			getStRrdOrgao());
	}

}