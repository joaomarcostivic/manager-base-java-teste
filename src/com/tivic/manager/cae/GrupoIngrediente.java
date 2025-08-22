package com.tivic.manager.cae;

public class GrupoIngrediente {

	private int cdGrupo;
	private String nmGrupo;
	private String sgGrupo;

	public GrupoIngrediente(){ }

	public GrupoIngrediente(int cdGrupo,
			String nmGrupo,
			String sgGrupo){
		setCdGrupo(cdGrupo);
		setNmGrupo(nmGrupo);
		setSgGrupo(sgGrupo);
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
	public void setSgGrupo(String sgGrupo){
		this.sgGrupo=sgGrupo;
	}
	public String getSgGrupo(){
		return this.sgGrupo;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdGrupo: " +  getCdGrupo();
		valueToString += ", nmGrupo: " +  getNmGrupo();
		valueToString += ", sgGrupo: " +  getSgGrupo();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new GrupoIngrediente(getCdGrupo(),
			getNmGrupo(),
			getSgGrupo());
	}

}
