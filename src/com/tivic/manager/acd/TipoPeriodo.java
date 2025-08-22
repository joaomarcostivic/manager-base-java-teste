package com.tivic.manager.acd;

public class TipoPeriodo {

	private int cdTipoPeriodo;
	private String nmTipoPeriodo;
	private int stTipoPeriodo;
	private String idTipoPeriodo;

	public TipoPeriodo(){ }

	public TipoPeriodo(int cdTipoPeriodo,
			String nmTipoPeriodo,
			int stTipoPeriodo,
			String idTipoPeriodo){
		setCdTipoPeriodo(cdTipoPeriodo);
		setNmTipoPeriodo(nmTipoPeriodo);
		setStTipoPeriodo(stTipoPeriodo);
		setIdTipoPeriodo(idTipoPeriodo);
	}
	public void setCdTipoPeriodo(int cdTipoPeriodo){
		this.cdTipoPeriodo=cdTipoPeriodo;
	}
	public int getCdTipoPeriodo(){
		return this.cdTipoPeriodo;
	}
	public void setNmTipoPeriodo(String nmTipoPeriodo){
		this.nmTipoPeriodo=nmTipoPeriodo;
	}
	public String getNmTipoPeriodo(){
		return this.nmTipoPeriodo;
	}
	public void setStTipoPeriodo(int stTipoPeriodo){
		this.stTipoPeriodo=stTipoPeriodo;
	}
	public int getStTipoPeriodo(){
		return this.stTipoPeriodo;
	}
	public void setIdTipoPeriodo(String idTipoPeriodo){
		this.idTipoPeriodo=idTipoPeriodo;
	}
	public String getIdTipoPeriodo(){
		return this.idTipoPeriodo;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdTipoPeriodo: " +  getCdTipoPeriodo();
		valueToString += ", nmTipoPeriodo: " +  getNmTipoPeriodo();
		valueToString += ", stTipoPeriodo: " +  getStTipoPeriodo();
		valueToString += ", idTipoPeriodo: " +  getIdTipoPeriodo();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new TipoPeriodo(getCdTipoPeriodo(),
			getNmTipoPeriodo(),
			getStTipoPeriodo(),
			getIdTipoPeriodo());
	}

}