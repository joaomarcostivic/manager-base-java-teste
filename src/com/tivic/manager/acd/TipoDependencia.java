package com.tivic.manager.acd;

public class TipoDependencia {

	private int cdTipoDependencia;
	private String nmTipoDependencia;
	private String idTipoDependencia;

	public TipoDependencia() { }
			
	public TipoDependencia(int cdTipoDependencia,
			String nmTipoDependencia,
			String idTipoDependencia){
		setCdTipoDependencia(cdTipoDependencia);
		setNmTipoDependencia(nmTipoDependencia);
		setIdTipoDependencia(idTipoDependencia);
	}
	public void setCdTipoDependencia(int cdTipoDependencia){
		this.cdTipoDependencia=cdTipoDependencia;
	}
	public int getCdTipoDependencia(){
		return this.cdTipoDependencia;
	}
	public void setNmTipoDependencia(String nmTipoDependencia){
		this.nmTipoDependencia=nmTipoDependencia;
	}
	public String getNmTipoDependencia(){
		return this.nmTipoDependencia;
	}
	public void setIdTipoDependencia(String idTipoDependencia){
		this.idTipoDependencia=idTipoDependencia;
	}
	public String getIdTipoDependencia(){
		return this.idTipoDependencia;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdTipoDependencia: " +  getCdTipoDependencia();
		valueToString += ", nmTipoDependencia: " +  getNmTipoDependencia();
		valueToString += ", idTipoDependencia: " +  getIdTipoDependencia();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new TipoDependencia(getCdTipoDependencia(),
			getNmTipoDependencia(),
			getIdTipoDependencia());
	}

}