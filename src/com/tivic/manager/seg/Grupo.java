package com.tivic.manager.seg;

public class Grupo {

	private int cdGrupo;
	private String nmGrupo;

	public Grupo() { }
	
	public Grupo(int cdGrupo,
			String nmGrupo){
		setCdGrupo(cdGrupo);
		setNmGrupo(nmGrupo);
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
	public String toString() {
		String valueToString = "";
		valueToString += "cdGrupo: " +  getCdGrupo();
		valueToString += ", nmGrupo: " +  getNmGrupo();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new Grupo(cdGrupo,
			nmGrupo);
	}

}