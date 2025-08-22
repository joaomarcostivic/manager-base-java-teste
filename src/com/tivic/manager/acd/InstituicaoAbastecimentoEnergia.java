package com.tivic.manager.acd;

public class InstituicaoAbastecimentoEnergia {

	private int cdInstituicao;
	private int cdAbastecimentoEnergia;
	private int cdPeriodoLetivo;

	public InstituicaoAbastecimentoEnergia() { }

	public InstituicaoAbastecimentoEnergia(int cdInstituicao,
			int cdAbastecimentoEnergia,
			int cdPeriodoLetivo) {
		setCdInstituicao(cdInstituicao);
		setCdAbastecimentoEnergia(cdAbastecimentoEnergia);
		setCdPeriodoLetivo(cdPeriodoLetivo);
	}
	public void setCdInstituicao(int cdInstituicao){
		this.cdInstituicao=cdInstituicao;
	}
	public int getCdInstituicao(){
		return this.cdInstituicao;
	}
	public void setCdAbastecimentoEnergia(int cdAbastecimentoEnergia){
		this.cdAbastecimentoEnergia=cdAbastecimentoEnergia;
	}
	public int getCdAbastecimentoEnergia(){
		return this.cdAbastecimentoEnergia;
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
		valueToString += ", cdAbastecimentoEnergia: " +  getCdAbastecimentoEnergia();
		valueToString += ", cdPeriodoLetivo: " +  getCdPeriodoLetivo();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new InstituicaoAbastecimentoEnergia(getCdInstituicao(),
			getCdAbastecimentoEnergia(),
			getCdPeriodoLetivo());
	}

}