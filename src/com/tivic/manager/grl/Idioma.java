package com.tivic.manager.grl;

public class Idioma {

	private int cdIdioma;
	private String nmIdioma;
	private String idIdioma;

	public Idioma() { }

	public Idioma(int cdIdioma,
			String nmIdioma,
			String idIdioma) {
		setCdIdioma(cdIdioma);
		setNmIdioma(nmIdioma);
		setIdIdioma(idIdioma);
	}
	public void setCdIdioma(int cdIdioma){
		this.cdIdioma=cdIdioma;
	}
	public int getCdIdioma(){
		return this.cdIdioma;
	}
	public void setNmIdioma(String nmIdioma){
		this.nmIdioma=nmIdioma;
	}
	public String getNmIdioma(){
		return this.nmIdioma;
	}
	public void setIdIdioma(String idIdioma){
		this.idIdioma=idIdioma;
	}
	public String getIdIdioma(){
		return this.idIdioma;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdIdioma: " +  getCdIdioma();
		valueToString += ", nmIdioma: " +  getNmIdioma();
		valueToString += ", idIdioma: " +  getIdIdioma();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new Idioma(getCdIdioma(),
			getNmIdioma(),
			getIdIdioma());
	}

}