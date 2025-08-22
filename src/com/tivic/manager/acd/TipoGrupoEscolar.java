package com.tivic.manager.acd;

public class TipoGrupoEscolar {

	private int cdTipoGrupo;
	private String nmGrupo;
	private String idGrupo;

	public TipoGrupoEscolar(){ }

	public TipoGrupoEscolar(int cdTipoGrupo,
			String nmGrupo,
			String idGrupo){
		setCdTipoGrupo(cdTipoGrupo);
		setNmGrupo(nmGrupo);
		setIdGrupo(idGrupo);
	}
	public void setCdTipoGrupo(int cdTipoGrupo){
		this.cdTipoGrupo=cdTipoGrupo;
	}
	public int getCdTipoGrupo(){
		return this.cdTipoGrupo;
	}
	public void setNmGrupo(String nmGrupo){
		this.nmGrupo=nmGrupo;
	}
	public String getNmGrupo(){
		return this.nmGrupo;
	}
	public void setIdGrupo(String idGrupo){
		this.idGrupo=idGrupo;
	}
	public String getIdGrupo(){
		return this.idGrupo;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdTipoGrupo: " +  getCdTipoGrupo();
		valueToString += ", nmGrupo: " +  getNmGrupo();
		valueToString += ", idGrupo: " +  getIdGrupo();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new TipoGrupoEscolar(getCdTipoGrupo(),
			getNmGrupo(),
			getIdGrupo());
	}

}
