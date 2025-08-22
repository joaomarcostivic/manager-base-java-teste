package com.tivic.manager.mob;

public class VistoriaItemGrupo {

	private int cdVistoriaItemGrupo;
	private String nmGrupo;

	public VistoriaItemGrupo(){ }

	public VistoriaItemGrupo(int cdVistoriaItemGrupo,
			String nmGrupo){
		setCdVistoriaItemGrupo(cdVistoriaItemGrupo);
		setNmGrupo(nmGrupo);
	}
	public void setCdVistoriaItemGrupo(int cdVistoriaItemGrupo){
		this.cdVistoriaItemGrupo=cdVistoriaItemGrupo;
	}
	public int getCdVistoriaItemGrupo(){
		return this.cdVistoriaItemGrupo;
	}
	public void setNmGrupo(String nmGrupo){
		this.nmGrupo=nmGrupo;
	}
	public String getNmGrupo(){
		return this.nmGrupo;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdVistoriaItemGrupo: " +  getCdVistoriaItemGrupo();
		valueToString += ", nmGrupo: " +  getNmGrupo();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new VistoriaItemGrupo(getCdVistoriaItemGrupo(),
			getNmGrupo());
	}

}