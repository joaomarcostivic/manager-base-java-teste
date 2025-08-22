package com.tivic.manager.crm;

public class TipoNecessidade {

	private int cdTipoNecessidade;
	private String nmTipoNecessidade;
	private String idTipoNecessidade;
	private int stTipoNecessidade;

	public TipoNecessidade(int cdTipoNecessidade,
			String nmTipoNecessidade,
			String idTipoNecessidade,
			int stTipoNecessidade){
		setCdTipoNecessidade(cdTipoNecessidade);
		setNmTipoNecessidade(nmTipoNecessidade);
		setIdTipoNecessidade(idTipoNecessidade);
		setStTipoNecessidade(stTipoNecessidade);
	}
	public void setCdTipoNecessidade(int cdTipoNecessidade){
		this.cdTipoNecessidade=cdTipoNecessidade;
	}
	public int getCdTipoNecessidade(){
		return this.cdTipoNecessidade;
	}
	public void setNmTipoNecessidade(String nmTipoNecessidade){
		this.nmTipoNecessidade=nmTipoNecessidade;
	}
	public String getNmTipoNecessidade(){
		return this.nmTipoNecessidade;
	}
	public void setIdTipoNecessidade(String idTipoNecessidade){
		this.idTipoNecessidade=idTipoNecessidade;
	}
	public String getIdTipoNecessidade(){
		return this.idTipoNecessidade;
	}
	public void setStTipoNecessidade(int stTipoNecessidade){
		this.stTipoNecessidade=stTipoNecessidade;
	}
	public int getStTipoNecessidade(){
		return this.stTipoNecessidade;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdTipoNecessidade: " +  getCdTipoNecessidade();
		valueToString += ", nmTipoNecessidade: " +  getNmTipoNecessidade();
		valueToString += ", idTipoNecessidade: " +  getIdTipoNecessidade();
		valueToString += ", stTipoNecessidade: " +  getStTipoNecessidade();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new TipoNecessidade(getCdTipoNecessidade(),
			getNmTipoNecessidade(),
			getIdTipoNecessidade(),
			getStTipoNecessidade());
	}

}