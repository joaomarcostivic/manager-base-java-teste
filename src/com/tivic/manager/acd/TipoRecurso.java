package com.tivic.manager.acd;

public class TipoRecurso {

	private int cdTipoRecurso;
	private String nmTipoRecurso;
	private int stTipoRecurso;

	public TipoRecurso() { }

	public TipoRecurso(int cdTipoRecurso,
			String nmTipoRecurso,
			int stTipoRecurso) {
		setCdTipoRecurso(cdTipoRecurso);
		setNmTipoRecurso(nmTipoRecurso);
		setStTipoRecurso(stTipoRecurso);
	}
	public void setCdTipoRecurso(int cdTipoRecurso){
		this.cdTipoRecurso=cdTipoRecurso;
	}
	public int getCdTipoRecurso(){
		return this.cdTipoRecurso;
	}
	public void setNmTipoRecurso(String nmTipoRecurso){
		this.nmTipoRecurso=nmTipoRecurso;
	}
	public String getNmTipoRecurso(){
		return this.nmTipoRecurso;
	}
	public void setStTipoRecurso(int stTipoRecurso){
		this.stTipoRecurso=stTipoRecurso;
	}
	public int getStTipoRecurso(){
		return this.stTipoRecurso;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdTipoRecurso: " +  getCdTipoRecurso();
		valueToString += ", nmTipoRecurso: " +  getNmTipoRecurso();
		valueToString += ", stTipoRecurso: " +  getStTipoRecurso();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new TipoRecurso(getCdTipoRecurso(),
			getNmTipoRecurso(),
			getStTipoRecurso());
	}

}