package com.tivic.manager.mob;

public class TipoAcidente {

	private int cdTipoAcidente;
	private String nmTipoAcidente;
	private String idTipoAcidente;
	private String dsTipoAcidente;

	public TipoAcidente() { }

	public TipoAcidente(int cdTipoAcidente,
			String nmTipoAcidente,
			String idTipoAcidente,
			String dsTipoAcidente) {
		setCdTipoAcidente(cdTipoAcidente);
		setNmTipoAcidente(nmTipoAcidente);
		setIdTipoAcidente(idTipoAcidente);
		setDsTipoAcidente(dsTipoAcidente);
	}
	public void setCdTipoAcidente(int cdTipoAcidente){
		this.cdTipoAcidente=cdTipoAcidente;
	}
	public int getCdTipoAcidente(){
		return this.cdTipoAcidente;
	}
	public void setNmTipoAcidente(String nmTipoAcidente){
		this.nmTipoAcidente=nmTipoAcidente;
	}
	public String getNmTipoAcidente(){
		return this.nmTipoAcidente;
	}
	public void setIdTipoAcidente(String idTipoAcidente){
		this.idTipoAcidente=idTipoAcidente;
	}
	public String getIdTipoAcidente(){
		return this.idTipoAcidente;
	}
	public void setDsTipoAcidente(String dsTipoAcidente){
		this.dsTipoAcidente=dsTipoAcidente;
	}
	public String getDsTipoAcidente(){
		return this.dsTipoAcidente;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdTipoAcidente: " +  getCdTipoAcidente();
		valueToString += ", nmTipoAcidente: " +  getNmTipoAcidente();
		valueToString += ", idTipoAcidente: " +  getIdTipoAcidente();
		valueToString += ", dsTipoAcidente: " +  getDsTipoAcidente();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new TipoAcidente(getCdTipoAcidente(),
			getNmTipoAcidente(),
			getIdTipoAcidente(),
			getDsTipoAcidente());
	}

}