package com.tivic.manager.mob.funset.parts;

public class FunsetCabecalho {

	private String tipoRegistro;
	private String codigoOrgaoTransitoArrecadador;
	private String mesCompetencia;
	
	public FunsetCabecalho() {
		// TODO Auto-generated constructor stub
	}

	public String getTipoRegistro() {
		return tipoRegistro;
	}

	public void setTipoRegistro(String tipoRegistro) {
		this.tipoRegistro = tipoRegistro;
	}

	public String getCodigoOrgaoTransitoArrecadador() {
		return codigoOrgaoTransitoArrecadador;
	}

	public void setCodigoOrgaoTransitoArrecadador(String codigoOrgaoTransitoArrecadador) {
		this.codigoOrgaoTransitoArrecadador = codigoOrgaoTransitoArrecadador;
	}

	public String getMesCompetencia() {
		return mesCompetencia;
	}

	public void setMesCompetencia(String mesCompetencia) {
		this.mesCompetencia = mesCompetencia;
	}
	
	public String build() {
		return getTipoRegistro() + getCodigoOrgaoTransitoArrecadador() + getMesCompetencia();
	}
}
