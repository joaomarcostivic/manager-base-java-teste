package com.tivic.manager.srh;

public class TipoOcorrencia {

	private int cdTipoOcorrencia;
	private String nmTipoOcorrencia;
	private String idTipoOcorrencia;

	public TipoOcorrencia() { }

	public TipoOcorrencia(int cdTipoOcorrencia,
			String nmTipoOcorrencia,
			String idTipoOcorrencia) {
		setCdTipoOcorrencia(cdTipoOcorrencia);
		setNmTipoOcorrencia(nmTipoOcorrencia);
		setIdTipoOcorrencia(idTipoOcorrencia);
	}
	public void setCdTipoOcorrencia(int cdTipoOcorrencia){
		this.cdTipoOcorrencia=cdTipoOcorrencia;
	}
	public int getCdTipoOcorrencia(){
		return this.cdTipoOcorrencia;
	}
	public void setNmTipoOcorrencia(String nmTipoOcorrencia){
		this.nmTipoOcorrencia=nmTipoOcorrencia;
	}
	public String getNmTipoOcorrencia(){
		return this.nmTipoOcorrencia;
	}
	public void setIdTipoOcorrencia(String idTipoOcorrencia){
		this.idTipoOcorrencia=idTipoOcorrencia;
	}
	public String getIdTipoOcorrencia(){
		return this.idTipoOcorrencia;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdTipoOcorrencia: " +  getCdTipoOcorrencia();
		valueToString += ", nmTipoOcorrencia: " +  getNmTipoOcorrencia();
		valueToString += ", idTipoOcorrencia: " +  getIdTipoOcorrencia();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new TipoOcorrencia(getCdTipoOcorrencia(),
			getNmTipoOcorrencia(),
			getIdTipoOcorrencia());
	}

}