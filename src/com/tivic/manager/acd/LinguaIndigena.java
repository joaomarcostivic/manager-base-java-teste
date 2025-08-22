package com.tivic.manager.acd;

public class LinguaIndigena {

	private int cdLinguaIndigena;
	private String nmLinguaIndigena;
	private String idLinguaIndigena;

	public LinguaIndigena(){ }

	public LinguaIndigena(int cdLinguaIndigena,
			String nmLinguaIndigena,
			String idLinguaIndigena){
		setCdLinguaIndigena(cdLinguaIndigena);
		setNmLinguaIndigena(nmLinguaIndigena);
		setIdLinguaIndigena(idLinguaIndigena);
	}
	public void setCdLinguaIndigena(int cdLinguaIndigena){
		this.cdLinguaIndigena=cdLinguaIndigena;
	}
	public int getCdLinguaIndigena(){
		return this.cdLinguaIndigena;
	}
	public void setNmLinguaIndigena(String nmLinguaIndigena){
		this.nmLinguaIndigena=nmLinguaIndigena;
	}
	public String getNmLinguaIndigena(){
		return this.nmLinguaIndigena;
	}
	public void setIdLinguaIndigena(String idLinguaIndigena){
		this.idLinguaIndigena=idLinguaIndigena;
	}
	public String getIdLinguaIndigena(){
		return this.idLinguaIndigena;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdLinguaIndigena: " +  getCdLinguaIndigena();
		valueToString += ", nmLinguaIndigena: " +  getNmLinguaIndigena();
		valueToString += ", idLinguaIndigena: " +  getIdLinguaIndigena();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new LinguaIndigena(getCdLinguaIndigena(),
			getNmLinguaIndigena(),
			getIdLinguaIndigena());
	}

}