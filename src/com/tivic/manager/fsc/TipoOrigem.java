package com.tivic.manager.fsc;

public class TipoOrigem {

	private int cdTipoOrigem;
	private String nmTipoOrigem;
	private String idTipoOrigem;
	private int stTipoOrigem;

	public TipoOrigem(int cdTipoOrigem,
			String nmTipoOrigem,
			String idTipoOrigem,
			int stTipoOrigem){
		setCdTipoOrigem(cdTipoOrigem);
		setNmTipoOrigem(nmTipoOrigem);
		setIdTipoOrigem(idTipoOrigem);
		setStTipoOrigem(stTipoOrigem);
	}
	public void setCdTipoOrigem(int cdTipoOrigem){
		this.cdTipoOrigem=cdTipoOrigem;
	}
	public int getCdTipoOrigem(){
		return this.cdTipoOrigem;
	}
	public void setNmTipoOrigem(String nmTipoOrigem){
		this.nmTipoOrigem=nmTipoOrigem;
	}
	public String getNmTipoOrigem(){
		return this.nmTipoOrigem;
	}
	public void setIdTipoOrigem(String idTipoOrigem){
		this.idTipoOrigem=idTipoOrigem;
	}
	public String getIdTipoOrigem(){
		return this.idTipoOrigem;
	}
	public void setStTipoOrigem(int stTipoOrigem){
		this.stTipoOrigem=stTipoOrigem;
	}
	public int getStTipoOrigem(){
		return this.stTipoOrigem;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdTipoOrigem: " +  getCdTipoOrigem();
		valueToString += ", nmTipoOrigem: " +  getNmTipoOrigem();
		valueToString += ", idTipoOrigem: " +  getIdTipoOrigem();
		valueToString += ", stTipoOrigem: " +  getStTipoOrigem();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new TipoOrigem(getCdTipoOrigem(),
			getNmTipoOrigem(),
			getIdTipoOrigem(),
			getStTipoOrigem());
	}

}