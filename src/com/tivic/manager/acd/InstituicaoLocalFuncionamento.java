package com.tivic.manager.acd;

public class InstituicaoLocalFuncionamento {

	private int cdInstituicao;
	private int cdLocalFuncionamento;
	private int cdPeriodoLetivo;

	public InstituicaoLocalFuncionamento() { }

	public InstituicaoLocalFuncionamento(int cdInstituicao,
			int cdLocalFuncionamento,
			int cdPeriodoLetivo) {
		setCdInstituicao(cdInstituicao);
		setCdLocalFuncionamento(cdLocalFuncionamento);
		setCdPeriodoLetivo(cdPeriodoLetivo);
	}
	public void setCdInstituicao(int cdInstituicao){
		this.cdInstituicao=cdInstituicao;
	}
	public int getCdInstituicao(){
		return this.cdInstituicao;
	}
	public void setCdLocalFuncionamento(int cdLocalFuncionamento){
		this.cdLocalFuncionamento=cdLocalFuncionamento;
	}
	public int getCdLocalFuncionamento(){
		return this.cdLocalFuncionamento;
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
		valueToString += ", cdLocalFuncionamento: " +  getCdLocalFuncionamento();
		valueToString += ", cdPeriodoLetivo: " +  getCdPeriodoLetivo();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new InstituicaoLocalFuncionamento(getCdInstituicao(),
			getCdLocalFuncionamento(),
			getCdPeriodoLetivo());
	}

}