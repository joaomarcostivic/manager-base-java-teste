package com.tivic.manager.fta;

public class TipoCheckup {

	private int cdTipoCheckup;
	private String nmTipoCheckup;

	public TipoCheckup(int cdTipoCheckup,
			String nmTipoCheckup){
		setCdTipoCheckup(cdTipoCheckup);
		setNmTipoCheckup(nmTipoCheckup);
	}
	public void setCdTipoCheckup(int cdTipoCheckup){
		this.cdTipoCheckup=cdTipoCheckup;
	}
	public int getCdTipoCheckup(){
		return this.cdTipoCheckup;
	}
	public void setNmTipoCheckup(String nmTipoCheckup){
		this.nmTipoCheckup=nmTipoCheckup;
	}
	public String getNmTipoCheckup(){
		return this.nmTipoCheckup;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdTipoCheckup: " +  getCdTipoCheckup();
		valueToString += ", nmTipoCheckup: " +  getNmTipoCheckup();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new TipoCheckup(getCdTipoCheckup(),
			getNmTipoCheckup());
	}

}