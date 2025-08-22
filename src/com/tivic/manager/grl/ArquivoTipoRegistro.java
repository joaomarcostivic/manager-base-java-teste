package com.tivic.manager.grl;

public class ArquivoTipoRegistro {

	private int cdTipoRegistro;
	private String nmTipoRegistro;
	private String idTipoArquivo;

	public ArquivoTipoRegistro(int cdTipoRegistro,
			String nmTipoRegistro,
			String idTipoArquivo){
		setCdTipoRegistro(cdTipoRegistro);
		setNmTipoRegistro(nmTipoRegistro);
		setIdTipoArquivo(idTipoArquivo);
	}
	public void setCdTipoRegistro(int cdTipoRegistro){
		this.cdTipoRegistro=cdTipoRegistro;
	}
	public int getCdTipoRegistro(){
		return this.cdTipoRegistro;
	}
	public void setNmTipoRegistro(String nmTipoRegistro){
		this.nmTipoRegistro=nmTipoRegistro;
	}
	public String getNmTipoRegistro(){
		return this.nmTipoRegistro;
	}
	public void setIdTipoArquivo(String idTipoArquivo){
		this.idTipoArquivo=idTipoArquivo;
	}
	public String getIdTipoArquivo(){
		return this.idTipoArquivo;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdTipoRegistro: " +  getCdTipoRegistro();
		valueToString += ", nmTipoRegistro: " +  getNmTipoRegistro();
		valueToString += ", idTipoArquivo: " +  getIdTipoArquivo();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new ArquivoTipoRegistro(getCdTipoRegistro(),
			getNmTipoRegistro(),
			getIdTipoArquivo());
	}

}