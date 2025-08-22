package com.tivic.manager.agd;

public class Local {

	private int cdLocal;
	private String nmLocal;
	private int cdTipoLocal;
	
	public Local(){ }

	public Local(int cdLocal,
			String nmLocal, 
			int cdTipoLocal){
		setCdLocal(cdLocal);
		setNmLocal(nmLocal);
		setCdTipoLocal(cdTipoLocal);
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
	public void setCdTipoLocal(int cdTipoLocal){
		this.cdTipoLocal=cdTipoLocal;
	}
	public int getCdTipoLocal(){
		return this.cdTipoLocal;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdLocal: " +  getCdLocal();
		valueToString += ", nmLocal: " +  getNmLocal();
		valueToString += "cdTipoLocal: " +  getCdTipoLocal();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new Local(getCdLocal(),
			getNmLocal(),
			getCdTipoLocal());
	}

}