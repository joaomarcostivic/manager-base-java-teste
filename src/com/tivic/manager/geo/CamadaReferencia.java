package com.tivic.manager.geo;

public class CamadaReferencia {

	private int cdReferencia;
	private int cdCamada;

	public CamadaReferencia(){ }

	public CamadaReferencia(int cdReferencia,
			int cdCamada){
		setCdReferencia(cdReferencia);
		setCdCamada(cdCamada);
	}
	public void setCdReferencia(int cdReferencia){
		this.cdReferencia=cdReferencia;
	}
	public int getCdReferencia(){
		return this.cdReferencia;
	}
	public void setCdCamada(int cdCamada){
		this.cdCamada=cdCamada;
	}
	public int getCdCamada(){
		return this.cdCamada;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdReferencia: " +  getCdReferencia();
		valueToString += ", cdCamada: " +  getCdCamada();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new CamadaReferencia(getCdReferencia(),
			getCdCamada());
	}

}
