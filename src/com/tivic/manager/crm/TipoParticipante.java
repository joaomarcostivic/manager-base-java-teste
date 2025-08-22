package com.tivic.manager.crm;

public class TipoParticipante {

	private int cdTipoParticipante;
	private String nmTipoParticipante;

	public TipoParticipante(int cdTipoParticipante,
			String nmTipoParticipante){
		setCdTipoParticipante(cdTipoParticipante);
		setNmTipoParticipante(nmTipoParticipante);
	}
	public void setCdTipoParticipante(int cdTipoParticipante){
		this.cdTipoParticipante=cdTipoParticipante;
	}
	public int getCdTipoParticipante(){
		return this.cdTipoParticipante;
	}
	public void setNmTipoParticipante(String nmTipoParticipante){
		this.nmTipoParticipante=nmTipoParticipante;
	}
	public String getNmTipoParticipante(){
		return this.nmTipoParticipante;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdTipoParticipante: " +  getCdTipoParticipante();
		valueToString += ", nmTipoParticipante: " +  getNmTipoParticipante();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new TipoParticipante(getCdTipoParticipante(),
			getNmTipoParticipante());
	}

}