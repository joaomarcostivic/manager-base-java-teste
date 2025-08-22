package com.tivic.manager.fta;

public class TipoTransporte {

	private int cdTipoTransporte;
	private String nmTipoTransporte;
	private String idTipoTransporte;

	public TipoTransporte(){ }

	public TipoTransporte(int cdTipoTransporte,
			String nmTipoTransporte,
			String idTipoTransporte){
		setCdTipoTransporte(cdTipoTransporte);
		setNmTipoTransporte(nmTipoTransporte);
		setIdTipoTransporte(idTipoTransporte);
	}
	public void setCdTipoTransporte(int cdTipoTransporte){
		this.cdTipoTransporte=cdTipoTransporte;
	}
	public int getCdTipoTransporte(){
		return this.cdTipoTransporte;
	}
	public void setNmTipoTransporte(String nmTipoTransporte){
		this.nmTipoTransporte=nmTipoTransporte;
	}
	public String getNmTipoTransporte(){
		return this.nmTipoTransporte;
	}
	public void setIdTipoTransporte(String idTipoTransporte){
		this.idTipoTransporte=idTipoTransporte;
	}
	public String getIdTipoTransporte(){
		return this.idTipoTransporte;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdTipoTransporte: " +  getCdTipoTransporte();
		valueToString += ", nmTipoTransporte: " +  getNmTipoTransporte();
		valueToString += ", idTipoTransporte: " +  getIdTipoTransporte();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new TipoTransporte(getCdTipoTransporte(),
			getNmTipoTransporte(),
			getIdTipoTransporte());
	}

}