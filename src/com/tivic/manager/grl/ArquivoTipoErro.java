package com.tivic.manager.grl;

public class ArquivoTipoErro {

	private int cdTipoErro;
	private String nmTipoErro;
	private String idTipoErro;

	public ArquivoTipoErro(int cdTipoErro,
			String nmTipoErro,
			String idTipoErro){
		setCdTipoErro(cdTipoErro);
		setNmTipoErro(nmTipoErro);
		setIdTipoErro(idTipoErro);
	}
	public void setCdTipoErro(int cdTipoErro){
		this.cdTipoErro=cdTipoErro;
	}
	public int getCdTipoErro(){
		return this.cdTipoErro;
	}
	public void setNmTipoErro(String nmTipoErro){
		this.nmTipoErro=nmTipoErro;
	}
	public String getNmTipoErro(){
		return this.nmTipoErro;
	}
	public void setIdTipoErro(String idTipoErro){
		this.idTipoErro=idTipoErro;
	}
	public String getIdTipoErro(){
		return this.idTipoErro;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdTipoErro: " +  getCdTipoErro();
		valueToString += ", nmTipoErro: " +  getNmTipoErro();
		valueToString += ", idTipoErro: " +  getIdTipoErro();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new ArquivoTipoErro(getCdTipoErro(),
			getNmTipoErro(),
			getIdTipoErro());
	}

}