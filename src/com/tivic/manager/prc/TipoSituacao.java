package com.tivic.manager.prc;

public class TipoSituacao {

	private int cdTipoSituacao;
	private String nmTipoSituacao;
	private String idTipoSituacao;
	private int nrOrdem;
	private int lgRetrocede;

	public TipoSituacao() { }

	public TipoSituacao(int cdTipoSituacao,
			String nmTipoSituacao,
			String idTipoSituacao,
			int nrOrdem,
			int lgRetrocede) {
		setCdTipoSituacao(cdTipoSituacao);
		setNmTipoSituacao(nmTipoSituacao);
		setIdTipoSituacao(idTipoSituacao);
		setNrOrdem(nrOrdem);
		setLgRetrocede(lgRetrocede);
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
	public void setLgRetrocede(int lgRetrocede){
		this.lgRetrocede=lgRetrocede;
	}
	public int getLgRetrocede(){
		return this.lgRetrocede;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdTipoSituacao: " +  getCdTipoSituacao();
		valueToString += ", nmTipoSituacao: " +  getNmTipoSituacao();
		valueToString += ", idTipoSituacao: " +  getIdTipoSituacao();
		valueToString += ", nrOrdem: " +  getNrOrdem();
		valueToString += ", lgRetrocede: " +  getLgRetrocede();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new TipoSituacao(getCdTipoSituacao(),
			getNmTipoSituacao(),
			getIdTipoSituacao(),
			getNrOrdem(),
			getLgRetrocede());
	}

}