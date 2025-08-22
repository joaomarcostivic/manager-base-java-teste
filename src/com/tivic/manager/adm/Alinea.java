package com.tivic.manager.adm;

public class Alinea {

	private int cdAlinea;
	private String nmAlinea;
	private String idAlinea;
	
	public Alinea(){}
	
	public Alinea(int cdAlinea,
			String nmAlinea,
			String idAlinea){
		setCdAlinea(cdAlinea);
		setNmAlinea(nmAlinea);
		setIdAlinea(idAlinea);
	}
	public void setCdAlinea(int cdAlinea){
		this.cdAlinea=cdAlinea;
	}
	public int getCdAlinea(){
		return this.cdAlinea;
	}
	public void setNmAlinea(String nmAlinea){
		this.nmAlinea=nmAlinea;
	}
	public String getNmAlinea(){
		return this.nmAlinea;
	}
	public void setIdAlinea(String idAlinea){
		this.idAlinea=idAlinea;
	}
	public String getIdAlinea(){
		return this.idAlinea;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdAlinea: " +  getCdAlinea();
		valueToString += ", nmAlinea: " +  getNmAlinea();
		valueToString += ", idAlinea: " +  getIdAlinea();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new Alinea(cdAlinea,
			nmAlinea,
			idAlinea);
	}

}