package com.tivic.manager.evt;

public class Local {

	private int cdLocal;
	private String nmLocal;
	private String dsLocal;
	private String idLocal;

	public Local(){ }

	public Local(int cdLocal,
			String nmLocal,
			String dsLocal,
			String idLocal){
		setCdLocal(cdLocal);
		setNmLocal(nmLocal);
		setDsLocal(dsLocal);
		setIdLocal(idLocal);
	}
	public void setCdLocal(int cdLocal){
		this.cdLocal=cdLocal;
	}
	public int getCdLocal(){
		return this.cdLocal;
	}
	public void setNmLocal(String nmLocal){
		this.nmLocal=nmLocal;
	}
	public String getNmLocal(){
		return this.nmLocal;
	}
	public void setDsLocal(String dsLocal){
		this.dsLocal=dsLocal;
	}
	public String getDsLocal(){
		return this.dsLocal;
	}
	public void setIdLocal(String idLocal){
		this.idLocal=idLocal;
	}
	public String getIdLocal(){
		return this.idLocal;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdLocal: " +  getCdLocal();
		valueToString += ", nmLocal: " +  getNmLocal();
		valueToString += ", dsLocal: " +  getDsLocal();
		valueToString += ", idLocal: " +  getIdLocal();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new Local(getCdLocal(),
			getNmLocal(),
			getDsLocal(),
			getIdLocal());
	}

}