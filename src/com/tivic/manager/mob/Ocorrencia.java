package com.tivic.manager.mob;

public class Ocorrencia {

	private int cdOcorrencia;
	private String dsOcorrencia;
	private int idMovimentacao;
	
	public Ocorrencia() {
		// TODO Auto-generated constructor stub
	}

	public Ocorrencia(int cdOcorrencia, String dsOcorrencia, int idMovimentacao) {
		this.cdOcorrencia = cdOcorrencia;
		this.dsOcorrencia = dsOcorrencia;
		this.idMovimentacao = idMovimentacao;
	}

	public int getCdOcorrencia() {
		return cdOcorrencia;
	}

	public void setCdOcorrencia(int cdOcorrencia) {
		this.cdOcorrencia = cdOcorrencia;
	}

	public String getDsOcorrencia() {
		return dsOcorrencia;
	}

	public void setDsOcorrencia(String dsOcorrencia) {
		this.dsOcorrencia = dsOcorrencia;
	}

	public int getIdMovimentacao() {
		return idMovimentacao;
	}

	public void setIdMovimentacao(int idMovimentacao) {
		this.idMovimentacao = idMovimentacao;
	}
	
	public String toString() {
		String valueToString = "";
		valueToString += "cdOcorrencia: " +  getCdOcorrencia();
		valueToString += ", dsOcorrencia: " +  getDsOcorrencia();
		valueToString += ", idMovimentacao: " +  getIdMovimentacao();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new Ocorrencia(getCdOcorrencia(),
			getDsOcorrencia(),
			getIdMovimentacao());
	}
	
}
