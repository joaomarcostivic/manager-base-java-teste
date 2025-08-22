package com.tivic.manager.mob;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Lacre {

	private int cdLacre;
	private String idLacre;
	private int stLacre;
	private String idSerie;

	public Lacre(){ }

	public Lacre(int cdLacre,
			String idLacre,
			int stLacre,
			String idSerie){
		setCdLacre(cdLacre);
		setIdLacre(idLacre);
		setStLacre(stLacre);
		setIdSerie(idSerie);
	}
	public void setCdLacre(int cdLacre){
		this.cdLacre=cdLacre;
	}
	public int getCdLacre(){
		return this.cdLacre;
	}
	public void setIdLacre(String idLacre){
		this.idLacre=idLacre;
	}
	public String getIdLacre(){
		return this.idLacre;
	}
	public void setStLacre(int stLacre){
		this.stLacre=stLacre;
	}
	public int getStLacre(){
		return this.stLacre;
	}
	public void setIdSerie(String idSerie){
		this.idSerie=idSerie;
	}
	public String getIdSerie(){
		return this.idSerie;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdLacre: " +  getCdLacre();
		valueToString += ", idLacre: " +  getIdLacre();
		valueToString += ", stLacre: " +  getStLacre();
		valueToString += ", idSerie: " +  getIdSerie();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new Lacre(getCdLacre(),
			getIdLacre(),
			getStLacre(),
			getIdSerie());
	}

}