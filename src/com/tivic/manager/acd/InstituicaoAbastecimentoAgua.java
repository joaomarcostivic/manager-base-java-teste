package com.tivic.manager.acd;

public class InstituicaoAbastecimentoAgua {

	private int cdInstituicao;
	private int cdAbastecimentoAgua;
	private int cdPeriodoLetivo;

	public InstituicaoAbastecimentoAgua() { }

	public InstituicaoAbastecimentoAgua(int cdInstituicao,
			int cdAbastecimentoAgua,
			int cdPeriodoLetivo) {
		setCdInstituicao(cdInstituicao);
		setCdAbastecimentoAgua(cdAbastecimentoAgua);
		setCdPeriodoLetivo(cdPeriodoLetivo);
	}
	public void setCdInstituicao(int cdInstituicao){
		this.cdInstituicao=cdInstituicao;
	}
	public int getCdInstituicao(){
		return this.cdInstituicao;
	}
	public void setCdAbastecimentoAgua(int cdAbastecimentoAgua){
		this.cdAbastecimentoAgua=cdAbastecimentoAgua;
	}
	public int getCdAbastecimentoAgua(){
		return this.cdAbastecimentoAgua;
	}
	public void setCdPeriodoLetivo(int cdPeriodoLetivo){
		this.cdPeriodoLetivo=cdPeriodoLetivo;
	}
	public int getCdPeriodoLetivo(){
		return this.cdPeriodoLetivo;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdInstituicao: " +  getCdInstituicao();
		valueToString += ", cdAbastecimentoAgua: " +  getCdAbastecimentoAgua();
		valueToString += ", cdPeriodoLetivo: " +  getCdPeriodoLetivo();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new InstituicaoAbastecimentoAgua(getCdInstituicao(),
			getCdAbastecimentoAgua(),
			getCdPeriodoLetivo());
	}

}