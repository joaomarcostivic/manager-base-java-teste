package com.tivic.manager.acd;

public class TipoRecursoAcessibilidade {

	private int cdTipoRecursoAcessibilidade;
	private String nmTipoRecursoAcessibilidade;
	private String idTipoRecursoAcessibilidade;
	private int stTipoRecursoAcessibilidade;

	public TipoRecursoAcessibilidade() { }

	public TipoRecursoAcessibilidade(int cdTipoRecursoAcessibilidade,
			String nmTipoRecursoAcessibilidade,
			String idTipoRecursoAcessibilidade,
			int stTipoRecursoAcessibilidade) {
		setCdTipoRecursoAcessibilidade(cdTipoRecursoAcessibilidade);
		setNmTipoRecursoAcessibilidade(nmTipoRecursoAcessibilidade);
		setIdTipoRecursoAcessibilidade(idTipoRecursoAcessibilidade);
		setStTipoRecursoAcessibilidade(stTipoRecursoAcessibilidade);
	}
	public void setCdTipoRecursoAcessibilidade(int cdTipoRecursoAcessibilidade){
		this.cdTipoRecursoAcessibilidade=cdTipoRecursoAcessibilidade;
	}
	public int getCdTipoRecursoAcessibilidade(){
		return this.cdTipoRecursoAcessibilidade;
	}
	public void setNmTipoRecursoAcessibilidade(String nmTipoRecursoAcessibilidade){
		this.nmTipoRecursoAcessibilidade=nmTipoRecursoAcessibilidade;
	}
	public String getNmTipoRecursoAcessibilidade(){
		return this.nmTipoRecursoAcessibilidade;
	}
	public void setIdTipoRecursoAcessibilidade(String idTipoRecursoAcessibilidade){
		this.idTipoRecursoAcessibilidade=idTipoRecursoAcessibilidade;
	}
	public String getIdTipoRecursoAcessibilidade(){
		return this.idTipoRecursoAcessibilidade;
	}
	public void setStTipoRecursoAcessibilidade(int stTipoRecursoAcessibilidade){
		this.stTipoRecursoAcessibilidade=stTipoRecursoAcessibilidade;
	}
	public int getStTipoRecursoAcessibilidade(){
		return this.stTipoRecursoAcessibilidade;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdTipoRecursoAcessibilidade: " +  getCdTipoRecursoAcessibilidade();
		valueToString += ", nmTipoRecursoAcessibilidade: " +  getNmTipoRecursoAcessibilidade();
		valueToString += ", idTipoRecursoAcessibilidade: " +  getIdTipoRecursoAcessibilidade();
		valueToString += ", stTipoRecursoAcessibilidade: " +  getStTipoRecursoAcessibilidade();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new TipoRecursoAcessibilidade(getCdTipoRecursoAcessibilidade(),
			getNmTipoRecursoAcessibilidade(),
			getIdTipoRecursoAcessibilidade(),
			getStTipoRecursoAcessibilidade());
	}

}