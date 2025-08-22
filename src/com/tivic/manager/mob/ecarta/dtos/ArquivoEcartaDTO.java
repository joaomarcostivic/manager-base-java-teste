package com.tivic.manager.mob.ecarta.dtos;

public class ArquivoEcartaDTO {
	private String nmArquivo;
	private byte[] blbArquivo;
	private int tpRetorno;

	public String getNmArquivo() {
		return nmArquivo;
	}

	public void setNmArquivo(String nmArquivo) {
		this.nmArquivo = nmArquivo;
	}

	public byte[] getBlbArquivo() {
		return blbArquivo;
	}

	public void setBlbArquivo(byte[] blbArquivo) {
		this.blbArquivo = blbArquivo;
	}
	
	public int getTpRetorno() {
		return tpRetorno;
	}

	public void setTpRetorno(int tpRetorno) {
		this.tpRetorno = tpRetorno;
	}
}
