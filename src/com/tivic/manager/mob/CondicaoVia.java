package com.tivic.manager.mob;

public class CondicaoVia {

	private int cdCondicaoVia;
	private String nmCondicaoVia;
	private String idCondicaoVia;

	public CondicaoVia() { }

	public CondicaoVia(int cdCondicaoVia,
			String nmCondicaoVia,
			String idCondicaoVia) {
		setCdCondicaoVia(cdCondicaoVia);
		setNmCondicaoVia(nmCondicaoVia);
		setIdCondicaoVia(idCondicaoVia);
	}
	public void setCdCondicaoVia(int cdCondicaoVia){
		this.cdCondicaoVia=cdCondicaoVia;
	}
	public int getCdCondicaoVia(){
		return this.cdCondicaoVia;
	}
	public void setNmCondicaoVia(String nmCondicaoVia){
		this.nmCondicaoVia=nmCondicaoVia;
	}
	public String getNmCondicaoVia(){
		return this.nmCondicaoVia;
	}
	public void setIdCondicaoVia(String idCondicaoVia){
		this.idCondicaoVia=idCondicaoVia;
	}
	public String getIdCondicaoVia(){
		return this.idCondicaoVia;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdCondicaoVia: " +  getCdCondicaoVia();
		valueToString += ", nmCondicaoVia: " +  getNmCondicaoVia();
		valueToString += ", idCondicaoVia: " +  getIdCondicaoVia();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new CondicaoVia(getCdCondicaoVia(),
			getNmCondicaoVia(),
			getIdCondicaoVia());
	}

}