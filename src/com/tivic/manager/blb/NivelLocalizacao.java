package com.tivic.manager.blb;

public class NivelLocalizacao {

	private int cdNivelLocalizacao;
	private String nmNivelLocalizacao;
	private String idNivelLocalizacao;

	public NivelLocalizacao(){ }

	public NivelLocalizacao(int cdNivelLocalizacao,
			String nmNivelLocalizacao,
			String idNivelLocalizacao){
		setCdNivelLocalizacao(cdNivelLocalizacao);
		setNmNivelLocalizacao(nmNivelLocalizacao);
		setIdNivelLocalizacao(idNivelLocalizacao);
	}
	public void setCdNivelLocalizacao(int cdNivelLocalizacao){
		this.cdNivelLocalizacao=cdNivelLocalizacao;
	}
	public int getCdNivelLocalizacao(){
		return this.cdNivelLocalizacao;
	}
	public void setNmNivelLocalizacao(String nmNivelLocalizacao){
		this.nmNivelLocalizacao=nmNivelLocalizacao;
	}
	public String getNmNivelLocalizacao(){
		return this.nmNivelLocalizacao;
	}
	public void setIdNivelLocalizacao(String idNivelLocalizacao){
		this.idNivelLocalizacao=idNivelLocalizacao;
	}
	public String getIdNivelLocalizacao(){
		return this.idNivelLocalizacao;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdNivelLocalizacao: " +  getCdNivelLocalizacao();
		valueToString += ", nmNivelLocalizacao: " +  getNmNivelLocalizacao();
		valueToString += ", idNivelLocalizacao: " +  getIdNivelLocalizacao();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new NivelLocalizacao(getCdNivelLocalizacao(),
			getNmNivelLocalizacao(),
			getIdNivelLocalizacao());
	}

}