package com.tivic.manager.grl;

public class TipoArquivo {

	private int cdTipoArquivo;
	private String nmTipoArquivo;
	private String idTipoArquivo;
	
	public TipoArquivo(){}
	
	public TipoArquivo(int cdTipoArquivo,
			String nmTipoArquivo,
			String idTipoArquivo){
		setCdTipoArquivo(cdTipoArquivo);
		setNmTipoArquivo(nmTipoArquivo);
		setIdTipoArquivo(idTipoArquivo);
	}
	public void setCdTipoArquivo(int cdTipoArquivo){
		this.cdTipoArquivo=cdTipoArquivo;
	}
	public int getCdTipoArquivo(){
		return this.cdTipoArquivo;
	}
	public void setNmTipoArquivo(String nmTipoArquivo){
		this.nmTipoArquivo=nmTipoArquivo;
	}
	public String getNmTipoArquivo(){
		return this.nmTipoArquivo;
	}
	public void setIdTipoArquivo(String idTipoArquivo){
		this.idTipoArquivo=idTipoArquivo;
	}
	public String getIdTipoArquivo(){
		return this.idTipoArquivo;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdTipoArquivo: " +  getCdTipoArquivo();
		valueToString += ", nmTipoArquivo: " +  getNmTipoArquivo();
		valueToString += ", idTipoArquivo: " +  getIdTipoArquivo();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new TipoArquivo(getCdTipoArquivo(),
			getNmTipoArquivo(),
			getIdTipoArquivo());
	}

}