package com.tivic.manager.grl.parametro.dtos;

public class ValorParametroDTO {
	private String nmParametro;
	private int nrValorParametro;
	private String txtValorParametro;
	private byte[] blbArquivo;

	public String getNmParametro() {
		return nmParametro;
	}

	public void setNmParametro(String nmParametro) {
		this.nmParametro = nmParametro;
	}

	public int getNrValorParametro() {
		return nrValorParametro;
	}

	public void setNrValorParametro(int nrValorParametro) {
		this.nrValorParametro = nrValorParametro;
	}

	public String getTxtValorParametro() {
		return txtValorParametro;
	}

	public void setTxtValorParametro(String txtValorParametro) {
		this.txtValorParametro = txtValorParametro;
	}

	public byte[] getBlbArquivo() {
		return blbArquivo;
	}

	public void setBlbArquivo(byte[] blbArquivo) {
		this.blbArquivo = blbArquivo;
	}
}
