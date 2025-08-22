package com.tivic.manager.acd;

public class TipoAcessoInternet {

	private int cdTipoAcessoInternet;
	private String nmTipoAcessoInternet;
	private String idTipoAcessoInternet;
	private int stTipoAcessoInternet;

	public TipoAcessoInternet() { }

	public TipoAcessoInternet(int cdTipoAcessoInternet,
			String nmTipoAcessoInternet,
			String idTipoAcessoInternet,
			int stTipoAcessoInternet) {
		setCdTipoAcessoInternet(cdTipoAcessoInternet);
		setNmTipoAcessoInternet(nmTipoAcessoInternet);
		setIdTipoAcessoInternet(idTipoAcessoInternet);
		setStTipoAcessoInternet(stTipoAcessoInternet);
	}
	public void setCdTipoAcessoInternet(int cdTipoAcessoInternet){
		this.cdTipoAcessoInternet=cdTipoAcessoInternet;
	}
	public int getCdTipoAcessoInternet(){
		return this.cdTipoAcessoInternet;
	}
	public void setNmTipoAcessoInternet(String nmTipoAcessoInternet){
		this.nmTipoAcessoInternet=nmTipoAcessoInternet;
	}
	public String getNmTipoAcessoInternet(){
		return this.nmTipoAcessoInternet;
	}
	public void setIdTipoAcessoInternet(String idTipoAcessoInternet){
		this.idTipoAcessoInternet=idTipoAcessoInternet;
	}
	public String getIdTipoAcessoInternet(){
		return this.idTipoAcessoInternet;
	}
	public void setStTipoAcessoInternet(int stTipoAcessoInternet){
		this.stTipoAcessoInternet=stTipoAcessoInternet;
	}
	public int getStTipoAcessoInternet(){
		return this.stTipoAcessoInternet;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdTipoAcessoInternet: " +  getCdTipoAcessoInternet();
		valueToString += ", nmTipoAcessoInternet: " +  getNmTipoAcessoInternet();
		valueToString += ", idTipoAcessoInternet: " +  getIdTipoAcessoInternet();
		valueToString += ", stTipoAcessoInternet: " +  getStTipoAcessoInternet();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new TipoAcessoInternet(getCdTipoAcessoInternet(),
			getNmTipoAcessoInternet(),
			getIdTipoAcessoInternet(),
			getStTipoAcessoInternet());
	}

}