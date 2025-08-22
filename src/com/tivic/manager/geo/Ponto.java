package com.tivic.manager.geo;

public class Ponto {

	private int cdPonto;
	private int cdReferencia;
	private float vlLatitude;
	private float vlLongitude;

	public Ponto(){ }

	public Ponto(int cdPonto,
			int cdReferencia,
			float vlLatitude,
			float vlLongitude){
		setCdPonto(cdPonto);
		setCdReferencia(cdReferencia);
		setVlLatitude(vlLatitude);
		setVlLongitude(vlLongitude);
	}
	public void setCdPonto(int cdPonto){
		this.cdPonto=cdPonto;
	}
	public int getCdPonto(){
		return this.cdPonto;
	}
	public void setCdReferencia(int cdReferencia){
		this.cdReferencia=cdReferencia;
	}
	public int getCdReferencia(){
		return this.cdReferencia;
	}
	public void setVlLatitude(float vlLatitude){
		this.vlLatitude=vlLatitude;
	}
	public float getVlLatitude(){
		return this.vlLatitude;
	}
	public void setVlLongitude(float vlLongitude){
		this.vlLongitude=vlLongitude;
	}
	public float getVlLongitude(){
		return this.vlLongitude;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdPonto: " +  getCdPonto();
		valueToString += ", cdReferencia: " +  getCdReferencia();
		valueToString += ", vlLatitude: " +  getVlLatitude();
		valueToString += ", vlLongitude: " +  getVlLongitude();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new Ponto(getCdPonto(),
			getCdReferencia(),
			getVlLatitude(),
			getVlLongitude());
	}

}