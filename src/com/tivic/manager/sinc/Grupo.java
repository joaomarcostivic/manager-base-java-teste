package com.tivic.manager.sinc;

public class Grupo {

	private int cdGrupo;
	private String nmGrupo;
	private int stGrupo;

	public Grupo(){ }

	public Grupo(int cdGrupo,
			String nmGrupo,
			int stGrupo){
		setCdGrupo(cdGrupo);
		setNmGrupo(nmGrupo);
		setStGrupo(stGrupo);
	}
	public void setCdGrupo(int cdGrupo){
		this.cdGrupo=cdGrupo;
	}
	public int getCdGrupo(){
		return this.cdGrupo;
	}
	public void setNmGrupo(String nmGrupo){
		this.nmGrupo=nmGrupo;
	}
	public String getNmGrupo(){
		return this.nmGrupo;
	}
	public void setStGrupo(int stGrupo){
		this.stGrupo=stGrupo;
	}
	public int getStGrupo(){
		return this.stGrupo;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdGrupo: " +  getCdGrupo();
		valueToString += ", nmGrupo: " +  getNmGrupo();
		valueToString += ", stGrupo: " +  getStGrupo();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new Grupo(getCdGrupo(),
			getNmGrupo(),
			getStGrupo());
	}

}