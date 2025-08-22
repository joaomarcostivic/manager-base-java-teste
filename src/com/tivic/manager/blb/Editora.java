package com.tivic.manager.blb;

public class Editora {

	private int cdEditora;
	private String nmEditora;
	private String idEditora;

	public Editora(){ }

	public Editora(int cdEditora,
			String nmEditora,
			String idEditora){
		setCdEditora(cdEditora);
		setNmEditora(nmEditora);
		setIdEditora(idEditora);
	}
	public void setCdEditora(int cdEditora){
		this.cdEditora=cdEditora;
	}
	public int getCdEditora(){
		return this.cdEditora;
	}
	public void setNmEditora(String nmEditora){
		this.nmEditora=nmEditora;
	}
	public String getNmEditora(){
		return this.nmEditora;
	}
	public void setIdEditora(String idEditora){
		this.idEditora=idEditora;
	}
	public String getIdEditora(){
		return this.idEditora;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdEditora: " +  getCdEditora();
		valueToString += ", nmEditora: " +  getNmEditora();
		valueToString += ", idEditora: " +  getIdEditora();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new Editora(getCdEditora(),
			getNmEditora(),
			getIdEditora());
	}

}