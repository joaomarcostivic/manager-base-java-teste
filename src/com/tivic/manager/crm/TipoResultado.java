package com.tivic.manager.crm;

public class TipoResultado {

	private int cdTipoResultado;
	private String nmTipoResultado;
	private String idTipoResultado;
	private int stTipoResultado;

	public TipoResultado(int cdTipoResultado,
			String nmTipoResultado,
			String idTipoResultado,
			int stTipoResultado){
		setCdTipoResultado(cdTipoResultado);
		setNmTipoResultado(nmTipoResultado);
		setIdTipoResultado(idTipoResultado);
		setStTipoResultado(stTipoResultado);
	}
	public void setCdTipoResultado(int cdTipoResultado){
		this.cdTipoResultado=cdTipoResultado;
	}
	public int getCdTipoResultado(){
		return this.cdTipoResultado;
	}
	public void setNmTipoResultado(String nmTipoResultado){
		this.nmTipoResultado=nmTipoResultado;
	}
	public String getNmTipoResultado(){
		return this.nmTipoResultado;
	}
	public void setIdTipoResultado(String idTipoResultado){
		this.idTipoResultado=idTipoResultado;
	}
	public String getIdTipoResultado(){
		return this.idTipoResultado;
	}
	public void setStTipoResultado(int stTipoResultado){
		this.stTipoResultado=stTipoResultado;
	}
	public int getStTipoResultado(){
		return this.stTipoResultado;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdTipoResultado: " +  getCdTipoResultado();
		valueToString += ", nmTipoResultado: " +  getNmTipoResultado();
		valueToString += ", idTipoResultado: " +  getIdTipoResultado();
		valueToString += ", stTipoResultado: " +  getStTipoResultado();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new TipoResultado(getCdTipoResultado(),
			getNmTipoResultado(),
			getIdTipoResultado(),
			getStTipoResultado());
	}

}