package com.tivic.manager.geo;

public class Mapa {

	private int cdMapa;
	private String nmMapa;
	private String txtMapa;
	private float vlLatitudeInicial;
	private float vlLongitudeInicial;
	private int vlZoom;
	private int tpMapa;

	public Mapa(){ }

	public Mapa(int cdMapa,
			String nmMapa,
			String txtMapa,
			float vlLatitudeInicial,
			float vlLongitudeInicial,
			int vlZoom,
			int tpMapa){
		setCdMapa(cdMapa);
		setNmMapa(nmMapa);
		setTxtMapa(txtMapa);
		setVlLatitudeInicial(vlLatitudeInicial);
		setVlLongitudeInicial(vlLongitudeInicial);
		setVlZoom(vlZoom);
		setTpMapa(tpMapa);
	}
	public void setCdMapa(int cdMapa){
		this.cdMapa=cdMapa;
	}
	public int getCdMapa(){
		return this.cdMapa;
	}
	public void setNmMapa(String nmMapa){
		this.nmMapa=nmMapa;
	}
	public String getNmMapa(){
		return this.nmMapa;
	}
	public void setTxtMapa(String txtMapa){
		this.txtMapa=txtMapa;
	}
	public String getTxtMapa(){
		return this.txtMapa;
	}
	public void setVlLatitudeInicial(float vlLatitudeInicial){
		this.vlLatitudeInicial=vlLatitudeInicial;
	}
	public float getVlLatitudeInicial(){
		return this.vlLatitudeInicial;
	}
	public void setVlLongitudeInicial(float vlLongitudeInicial){
		this.vlLongitudeInicial=vlLongitudeInicial;
	}
	public float getVlLongitudeInicial(){
		return this.vlLongitudeInicial;
	}
	public void setVlZoom(int vlZoom){
		this.vlZoom=vlZoom;
	}
	public int getVlZoom(){
		return this.vlZoom;
	}
	public void setTpMapa(int tpMapa){
		this.tpMapa=tpMapa;
	}
	public int getTpMapa(){
		return this.tpMapa;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdMapa: " +  getCdMapa();
		valueToString += ", nmMapa: " +  getNmMapa();
		valueToString += ", txtMapa: " +  getTxtMapa();
		valueToString += ", vlLatitudeInicial: " +  getVlLatitudeInicial();
		valueToString += ", vlLongitudeInicial: " +  getVlLongitudeInicial();
		valueToString += ", vlZoom: " +  getVlZoom();
		valueToString += ", tpMapa: " +  getTpMapa();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new Mapa(getCdMapa(),
			getNmMapa(),
			getTxtMapa(),
			getVlLatitudeInicial(),
			getVlLongitudeInicial(),
			getVlZoom(),
			getTpMapa());
	}

}
