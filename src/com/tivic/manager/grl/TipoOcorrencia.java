package com.tivic.manager.grl;

public class TipoOcorrencia {

	private int cdTipoOcorrencia;
	private String nmTipoOcorrencia;
	private String idTipoOcorrencia;
	private int lgEmail;

	public TipoOcorrencia(){ }

	public TipoOcorrencia(int cdTipoOcorrencia,
			String nmTipoOcorrencia,
			String idTipoOcorrencia,
			int lgEmail){
		setCdTipoOcorrencia(cdTipoOcorrencia);
		setNmTipoOcorrencia(nmTipoOcorrencia);
		setIdTipoOcorrencia(idTipoOcorrencia);
		setLgEmail(lgEmail);
	}
	
	public TipoOcorrencia(int cdTipoOcorrencia,
			String nmTipoOcorrencia){
		setCdTipoOcorrencia(cdTipoOcorrencia);
		setNmTipoOcorrencia(nmTipoOcorrencia);
		setIdTipoOcorrencia(null);
		setLgEmail(0);
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
	public int getLgEmail() {
		return lgEmail;
	}
	public void setLgEmail(int lgEmail) {
		this.lgEmail = lgEmail;
	}
	
	public String toString() {
		String valueToString = "";
		valueToString += "cdTipoOcorrencia: " +  getCdTipoOcorrencia();
		valueToString += ", nmTipoOcorrencia: " +  getNmTipoOcorrencia();
		valueToString += ", idTipoOcorrencia: " +  getIdTipoOcorrencia();
		valueToString += ", lgEmail: " +  getLgEmail();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new TipoOcorrencia(getCdTipoOcorrencia(),
			getNmTipoOcorrencia(),
			getIdTipoOcorrencia(),
			getLgEmail());
	}

}