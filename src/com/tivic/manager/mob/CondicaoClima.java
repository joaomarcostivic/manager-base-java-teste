package com.tivic.manager.mob;

public class CondicaoClima {

	private int cdCondicaoClima;
	private String nmCondicaoClima;
	private String idCondicaoClima;

	public CondicaoClima() { }

	public CondicaoClima(int cdCondicaoClima,
			String nmCondicaoClima,
			String idCondicaoClima) {
		setCdCondicaoClima(cdCondicaoClima);
		setNmCondicaoClima(nmCondicaoClima);
		setIdCondicaoClima(idCondicaoClima);
	}
	public void setCdCondicaoClima(int cdCondicaoClima){
		this.cdCondicaoClima=cdCondicaoClima;
	}
	public int getCdCondicaoClima(){
		return this.cdCondicaoClima;
	}
	public void setNmCondicaoClima(String nmCondicaoClima){
		this.nmCondicaoClima=nmCondicaoClima;
	}
	public String getNmCondicaoClima(){
		return this.nmCondicaoClima;
	}
	public void setIdCondicaoClima(String idCondicaoClima){
		this.idCondicaoClima=idCondicaoClima;
	}
	public String getIdCondicaoClima(){
		return this.idCondicaoClima;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdCondicaoClima: " +  getCdCondicaoClima();
		valueToString += ", nmCondicaoClima: " +  getNmCondicaoClima();
		valueToString += ", idCondicaoClima: " +  getIdCondicaoClima();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new CondicaoClima(getCdCondicaoClima(),
			getNmCondicaoClima(),
			getIdCondicaoClima());
	}

}