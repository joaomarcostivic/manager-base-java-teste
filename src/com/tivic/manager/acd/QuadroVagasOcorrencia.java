package com.tivic.manager.acd;

public class QuadroVagasOcorrencia {

	private int cdQuadroVagas;
	private int cdInstituicao;
	private int cdOcorrencia;

	public QuadroVagasOcorrencia(){ }

	public QuadroVagasOcorrencia(int cdQuadroVagas,
			int cdInstituicao,
			int cdOcorrencia){
		setCdQuadroVagas(cdQuadroVagas);
		setCdInstituicao(cdInstituicao);
		setCdOcorrencia(cdOcorrencia);
	}
	public void setCdQuadroVagas(int cdQuadroVagas){
		this.cdQuadroVagas=cdQuadroVagas;
	}
	public int getCdQuadroVagas(){
		return this.cdQuadroVagas;
	}
	public void setCdInstituicao(int cdInstituicao){
		this.cdInstituicao=cdInstituicao;
	}
	public int getCdInstituicao(){
		return this.cdInstituicao;
	}
	public void setCdOcorrencia(int cdOcorrencia){
		this.cdOcorrencia=cdOcorrencia;
	}
	public int getCdOcorrencia(){
		return this.cdOcorrencia;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdQuadroVagas: " +  getCdQuadroVagas();
		valueToString += ", cdInstituicao: " +  getCdInstituicao();
		valueToString += ", cdOcorrencia: " +  getCdOcorrencia();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new QuadroVagasOcorrencia(getCdQuadroVagas(),
			getCdInstituicao(),
			getCdOcorrencia());
	}

}