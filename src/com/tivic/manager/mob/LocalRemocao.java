package com.tivic.manager.mob;

public class LocalRemocao {

	private int cdLocalRemocao;
	private String nmLocalRemocao;
	private String idLocalRemocao;

	public LocalRemocao() { }

	public LocalRemocao(int cdLocalRemocao,
			String nmLocalRemocao,
			String idLocalRemocao) {
		setCdLocalRemocao(cdLocalRemocao);
		setNmLocalRemocao(nmLocalRemocao);
		setIdLocalRemocao(idLocalRemocao);
	}
	public void setCdLocalRemocao(int cdLocalRemocao){
		this.cdLocalRemocao=cdLocalRemocao;
	}
	public int getCdLocalRemocao(){
		return this.cdLocalRemocao;
	}
	public void setNmLocalRemocao(String nmLocalRemocao){
		this.nmLocalRemocao=nmLocalRemocao;
	}
	public String getNmLocalRemocao(){
		return this.nmLocalRemocao;
	}
	public void setIdLocalRemocao(String idLocalRemocao){
		this.idLocalRemocao=idLocalRemocao;
	}
	public String getIdLocalRemocao(){
		return this.idLocalRemocao;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdLocalRemocao: " +  getCdLocalRemocao();
		valueToString += ", nmLocalRemocao: " +  getNmLocalRemocao();
		valueToString += ", idLocalRemocao: " +  getIdLocalRemocao();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new LocalRemocao(getCdLocalRemocao(),
			getNmLocalRemocao(),
			getIdLocalRemocao());
	}

}