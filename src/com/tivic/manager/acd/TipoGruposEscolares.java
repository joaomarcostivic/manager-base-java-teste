package com.tivic.manager.acd;

public class TipoGruposEscolares {

	private int cdTipoGruposEscolares;
	private String nmTipoGruposEscolares;
	private String idTipoGruposEscolares;
	private int stTipoGruposEscolares;

	public TipoGruposEscolares() { }

	public TipoGruposEscolares(int cdTipoGruposEscolares,
			String nmTipoGruposEscolares,
			String idTipoGruposEscolares,
			int stTipoGruposEscolares) {
		setCdTipoGruposEscolares(cdTipoGruposEscolares);
		setNmTipoGruposEscolares(nmTipoGruposEscolares);
		setIdTipoGruposEscolares(idTipoGruposEscolares);
		setStTipoGruposEscolares(stTipoGruposEscolares);
	}
	public void setCdTipoGruposEscolares(int cdTipoGruposEscolares){
		this.cdTipoGruposEscolares=cdTipoGruposEscolares;
	}
	public int getCdTipoGruposEscolares(){
		return this.cdTipoGruposEscolares;
	}
	public void setNmTipoGruposEscolares(String nmTipoGruposEscolares){
		this.nmTipoGruposEscolares=nmTipoGruposEscolares;
	}
	public String getNmTipoGruposEscolares(){
		return this.nmTipoGruposEscolares;
	}
	public void setIdTipoGruposEscolares(String idTipoGruposEscolares){
		this.idTipoGruposEscolares=idTipoGruposEscolares;
	}
	public String getIdTipoGruposEscolares(){
		return this.idTipoGruposEscolares;
	}
	public void setStTipoGruposEscolares(int stTipoGruposEscolares){
		this.stTipoGruposEscolares=stTipoGruposEscolares;
	}
	public int getStTipoGruposEscolares(){
		return this.stTipoGruposEscolares;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdTipoGruposEscolares: " +  getCdTipoGruposEscolares();
		valueToString += ", nmTipoGruposEscolares: " +  getNmTipoGruposEscolares();
		valueToString += ", idTipoGruposEscolares: " +  getIdTipoGruposEscolares();
		valueToString += ", stTipoGruposEscolares: " +  getStTipoGruposEscolares();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new TipoGruposEscolares(getCdTipoGruposEscolares(),
			getNmTipoGruposEscolares(),
			getIdTipoGruposEscolares(),
			getStTipoGruposEscolares());
	}

}