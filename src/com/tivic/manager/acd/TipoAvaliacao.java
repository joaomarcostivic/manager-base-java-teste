package com.tivic.manager.acd;

public class TipoAvaliacao {

	private int cdTipoAvaliacao;
	private String nmTipoAvaliacao;
	private String idTipoAvaliacao;

	public TipoAvaliacao(){ }

	public TipoAvaliacao(int cdTipoAvaliacao,
			String nmTipoAvaliacao,
			String idTipoAvaliacao){
		setCdTipoAvaliacao(cdTipoAvaliacao);
		setNmTipoAvaliacao(nmTipoAvaliacao);
		setIdTipoAvaliacao(idTipoAvaliacao);
	}
	public void setCdTipoAvaliacao(int cdTipoAvaliacao){
		this.cdTipoAvaliacao=cdTipoAvaliacao;
	}
	public int getCdTipoAvaliacao(){
		return this.cdTipoAvaliacao;
	}
	public void setNmTipoAvaliacao(String nmTipoAvaliacao){
		this.nmTipoAvaliacao=nmTipoAvaliacao;
	}
	public String getNmTipoAvaliacao(){
		return this.nmTipoAvaliacao;
	}
	public void setIdTipoAvaliacao(String idTipoAvaliacao){
		this.idTipoAvaliacao=idTipoAvaliacao;
	}
	public String getIdTipoAvaliacao(){
		return this.idTipoAvaliacao;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdTipoAvaliacao: " +  getCdTipoAvaliacao();
		valueToString += ", nmTipoAvaliacao: " +  getNmTipoAvaliacao();
		valueToString += ", idTipoAvaliacao: " +  getIdTipoAvaliacao();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new TipoAvaliacao(getCdTipoAvaliacao(),
			getNmTipoAvaliacao(),
			getIdTipoAvaliacao());
	}

}
