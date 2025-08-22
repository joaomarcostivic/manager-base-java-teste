package com.tivic.manager.agd;

public class TipoLocal {

	private int cdTipoLocal;
	private String nmTipoLocal;
	private String idTipoLocal;

	public TipoLocal(){ }

	public TipoLocal(int cdTipoLocal,
			String nmTipoLocal,
			String idTipoLocal){
		setCdTipoLocal(cdTipoLocal);
		setNmTipoLocal(nmTipoLocal);
		setIdTipoLocal(idTipoLocal);
	}
	public void setCdTipoLocal(int cdTipoLocal){
		this.cdTipoLocal=cdTipoLocal;
	}
	public int getCdTipoLocal(){
		return this.cdTipoLocal;
	}
	public void setNmTipoLocal(String nmTipoLocal){
		this.nmTipoLocal=nmTipoLocal;
	}
	public String getNmTipoLocal(){
		return this.nmTipoLocal;
	}
	public void setIdTipoLocal(String idTipoLocal){
		this.idTipoLocal=idTipoLocal;
	}
	public String getIdTipoLocal(){
		return this.idTipoLocal;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdTipoLocal: " +  getCdTipoLocal();
		valueToString += ", nmTipoLocal: " +  getNmTipoLocal();
		valueToString += ", idTipoLocal: " +  getIdTipoLocal();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new TipoLocal(getCdTipoLocal(),
			getNmTipoLocal(),
			getIdTipoLocal());
	}

}