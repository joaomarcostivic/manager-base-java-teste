package com.tivic.manager.acd;

public class TipoAula {

	private int cdTipoAula;
	private String nmTipoAula;
	private float vlHoraAula;

	public TipoAula(){ }

	public TipoAula(int cdTipoAula,
			String nmTipoAula,
			float vlHoraAula){
		setCdTipoAula(cdTipoAula);
		setNmTipoAula(nmTipoAula);
		setVlHoraAula(vlHoraAula);
	}
	public void setCdTipoAula(int cdTipoAula){
		this.cdTipoAula=cdTipoAula;
	}
	public int getCdTipoAula(){
		return this.cdTipoAula;
	}
	public void setNmTipoAula(String nmTipoAula){
		this.nmTipoAula=nmTipoAula;
	}
	public String getNmTipoAula(){
		return this.nmTipoAula;
	}
	public void setVlHoraAula(float vlHoraAula){
		this.vlHoraAula=vlHoraAula;
	}
	public float getVlHoraAula(){
		return this.vlHoraAula;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdTipoAula: " +  getCdTipoAula();
		valueToString += ", nmTipoAula: " +  getNmTipoAula();
		valueToString += ", vlHoraAula: " +  getVlHoraAula();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new TipoAula(getCdTipoAula(),
			getNmTipoAula(),
			getVlHoraAula());
	}

}