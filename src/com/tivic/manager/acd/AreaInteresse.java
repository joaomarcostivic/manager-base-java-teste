package com.tivic.manager.acd;

public class AreaInteresse {

	private int cdAreaInteresse;
	private String nmAreaInteresse;
	private String idAreaInteresse;

	public AreaInteresse(){}
			
	public AreaInteresse(int cdAreaInteresse,
			String nmAreaInteresse,
			String idAreaInteresse){
		setCdAreaInteresse(cdAreaInteresse);
		setNmAreaInteresse(nmAreaInteresse);
		setIdAreaInteresse(idAreaInteresse);
	}
	public void setCdAreaInteresse(int cdAreaInteresse){
		this.cdAreaInteresse=cdAreaInteresse;
	}
	public int getCdAreaInteresse(){
		return this.cdAreaInteresse;
	}
	public void setNmAreaInteresse(String nmAreaInteresse){
		this.nmAreaInteresse=nmAreaInteresse;
	}
	public String getNmAreaInteresse(){
		return this.nmAreaInteresse;
	}
	public void setIdAreaInteresse(String idAreaInteresse){
		this.idAreaInteresse=idAreaInteresse;
	}
	public String getIdAreaInteresse(){
		return this.idAreaInteresse;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdAreaInteresse: " +  getCdAreaInteresse();
		valueToString += ", nmAreaInteresse: " +  getNmAreaInteresse();
		valueToString += ", idAreaInteresse: " +  getIdAreaInteresse();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new AreaInteresse(getCdAreaInteresse(),
			getNmAreaInteresse(),
			getIdAreaInteresse());
	}

}
