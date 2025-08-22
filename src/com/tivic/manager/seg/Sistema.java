package com.tivic.manager.seg;

public class Sistema {

	private int cdSistema;
	private String nmSistema;
	private String idSistema;
	private int lgAtivo;

	public Sistema() { }
			
	public Sistema(int cdSistema,
			String nmSistema,
			String idSistema,
			int lgAtivo){
		setCdSistema(cdSistema);
		setNmSistema(nmSistema);
		setIdSistema(idSistema);
		setLgAtivo(lgAtivo);
	}
	public void setCdSistema(int cdSistema){
		this.cdSistema=cdSistema;
	}
	public int getCdSistema(){
		return this.cdSistema;
	}
	public void setNmSistema(String nmSistema){
		this.nmSistema=nmSistema;
	}
	public String getNmSistema(){
		return this.nmSistema;
	}
	public void setIdSistema(String idSistema){
		this.idSistema=idSistema;
	}
	public String getIdSistema(){
		return this.idSistema;
	}
	public void setLgAtivo(int lgAtivo){
		this.lgAtivo=lgAtivo;
	}
	public int getLgAtivo(){
		return this.lgAtivo;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdSistema: " +  getCdSistema();
		valueToString += ", nmSistema: " +  getNmSistema();
		valueToString += ", idSistema: " +  getIdSistema();
		valueToString += ", lgAtivo: " +  getLgAtivo();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new Sistema(getCdSistema(),
			getNmSistema(),
			getIdSistema(),
			getLgAtivo());
	}

}
