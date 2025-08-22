package com.tivic.manager.crm;

public class TipoParticipantePermissao {

	private int cdTipoParticipante;
	private int cdTipoOcorrencia;
	private int lgAtivo;

	public TipoParticipantePermissao(int cdTipoParticipante,
			int cdTipoOcorrencia,
			int lgAtivo){
		setCdTipoParticipante(cdTipoParticipante);
		setCdTipoOcorrencia(cdTipoOcorrencia);
		setLgAtivo(lgAtivo);
	}
	public void setCdTipoParticipante(int cdTipoParticipante){
		this.cdTipoParticipante=cdTipoParticipante;
	}
	public int getCdTipoParticipante(){
		return this.cdTipoParticipante;
	}
	public void setCdTipoOcorrencia(int cdTipoOcorrencia){
		this.cdTipoOcorrencia=cdTipoOcorrencia;
	}
	public int getCdTipoOcorrencia(){
		return this.cdTipoOcorrencia;
	}
	public void setLgAtivo(int lgAtivo){
		this.lgAtivo=lgAtivo;
	}
	public int getLgAtivo(){
		return this.lgAtivo;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdTipoParticipante: " +  getCdTipoParticipante();
		valueToString += ", cdTipoOcorrencia: " +  getCdTipoOcorrencia();
		valueToString += ", lgAtivo: " +  getLgAtivo();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new TipoParticipantePermissao(getCdTipoParticipante(),
			getCdTipoOcorrencia(),
			getLgAtivo());
	}

}