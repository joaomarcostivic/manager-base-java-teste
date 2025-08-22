package com.tivic.manager.agd;

public class TipoSituacao {

	private int cdTipoSituacao;
	private String nmTipoSituacao;
	private String idTipoSituacao;
	private int nrOrdem;

	public TipoSituacao() { }

	public TipoSituacao(int cdTipoSituacao,
			String nmTipoSituacao,
			String idTipoSituacao,
			int nrOrdem) {
		setCdTipoSituacao(cdTipoSituacao);
		setNmTipoSituacao(nmTipoSituacao);
		setIdTipoSituacao(idTipoSituacao);
		setNrOrdem(nrOrdem);
	}
	public void setCdTipoSituacao(int cdTipoSituacao){
		this.cdTipoSituacao=cdTipoSituacao;
	}
	public int getCdTipoSituacao(){
		return this.cdTipoSituacao;
	}
	public void setNmTipoSituacao(String nmTipoSituacao){
		this.nmTipoSituacao=nmTipoSituacao;
	}
	public String getNmTipoSituacao(){
		return this.nmTipoSituacao;
	}
	public void setIdTipoSituacao(String idTipoSituacao){
		this.idTipoSituacao=idTipoSituacao;
	}
	public String getIdTipoSituacao(){
		return this.idTipoSituacao;
	}
	public void setNrOrdem(int nrOrdem){
		this.nrOrdem=nrOrdem;
	}
	public int getNrOrdem(){
		return this.nrOrdem;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdTipoSituacao: " +  getCdTipoSituacao();
		valueToString += ", nmTipoSituacao: " +  getNmTipoSituacao();
		valueToString += ", idTipoSituacao: " +  getIdTipoSituacao();
		valueToString += ", nrOrdem: " +  getNrOrdem();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new TipoSituacao(getCdTipoSituacao(),
			getNmTipoSituacao(),
			getIdTipoSituacao(),
			getNrOrdem());
	}

}