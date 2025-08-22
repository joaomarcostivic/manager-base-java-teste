package com.tivic.manager.grl;

public class Escolaridade {

	private int cdEscolaridade;
	private String nmEscolaridade;
	private String idEscolaridade;

	public Escolaridade() { }
			
	public Escolaridade(int cdEscolaridade,
			String nmEscolaridade,
			String idEscolaridade){
		setCdEscolaridade(cdEscolaridade);
		setNmEscolaridade(nmEscolaridade);
		setIdEscolaridade(idEscolaridade);
	}
	public void setCdEscolaridade(int cdEscolaridade){
		this.cdEscolaridade=cdEscolaridade;
	}
	public int getCdEscolaridade(){
		return this.cdEscolaridade;
	}
	public void setNmEscolaridade(String nmEscolaridade){
		this.nmEscolaridade=nmEscolaridade;
	}
	public String getNmEscolaridade(){
		return this.nmEscolaridade;
	}
	public void setIdEscolaridade(String idEscolaridade){
		this.idEscolaridade=idEscolaridade;
	}
	public String getIdEscolaridade(){
		return this.idEscolaridade;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdEscolaridade: " +  getCdEscolaridade();
		valueToString += ", nmEscolaridade: " +  getNmEscolaridade();
		valueToString += ", idEscolaridade: " +  getIdEscolaridade();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new Escolaridade(cdEscolaridade,
			nmEscolaridade,
			idEscolaridade);
	}

}