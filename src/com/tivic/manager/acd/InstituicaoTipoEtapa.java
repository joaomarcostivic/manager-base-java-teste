package com.tivic.manager.acd;

public class InstituicaoTipoEtapa {

	private int cdEtapa;
	private int cdInstituicao;
	private int cdPeriodoLetivo;

	public InstituicaoTipoEtapa() { }

	public InstituicaoTipoEtapa(int cdEtapa,
			int cdInstituicao,
			int cdPeriodoLetivo) {
		setCdEtapa(cdEtapa);
		setCdInstituicao(cdInstituicao);
		setCdPeriodoLetivo(cdPeriodoLetivo);
	}
	public void setCdEtapa(int cdEtapa){
		this.cdEtapa=cdEtapa;
	}
	public int getCdEtapa(){
		return this.cdEtapa;
	}
	public void setCdInstituicao(int cdInstituicao){
		this.cdInstituicao=cdInstituicao;
	}
	public int getCdInstituicao(){
		return this.cdInstituicao;
	}
	public void setCdPeriodoLetivo(int cdPeriodoLetivo){
		this.cdPeriodoLetivo=cdPeriodoLetivo;
	}
	public int getCdPeriodoLetivo(){
		return this.cdPeriodoLetivo;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdEtapa: " +  getCdEtapa();
		valueToString += ", cdInstituicao: " +  getCdInstituicao();
		valueToString += ", cdPeriodoLetivo: " +  getCdPeriodoLetivo();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new InstituicaoTipoEtapa(getCdEtapa(),
			getCdInstituicao(),
			getCdPeriodoLetivo());
	}

}