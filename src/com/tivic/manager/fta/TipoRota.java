package com.tivic.manager.fta;

public class TipoRota {

	private int cdTipoRota;
	private String nmTipo;

	public TipoRota(int cdTipoRota,
			String nmTipo){
		setCdTipoRota(cdTipoRota);
		setNmTipo(nmTipo);
	}
	public void setCdTipoRota(int cdTipoRota){
		this.cdTipoRota=cdTipoRota;
	}
	public int getCdTipoRota(){
		return this.cdTipoRota;
	}
	public void setNmTipo(String nmTipo){
		this.nmTipo=nmTipo;
	}
	public String getNmTipo(){
		return this.nmTipo;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdTipoRota: " +  getCdTipoRota();
		valueToString += ", nmTipo: " +  getNmTipo();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new TipoRota(getCdTipoRota(),
			getNmTipo());
	}

}