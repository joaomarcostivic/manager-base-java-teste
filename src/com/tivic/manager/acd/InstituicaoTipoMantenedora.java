package com.tivic.manager.acd;

public class InstituicaoTipoMantenedora {

	private int cdInstituicao;
	private int cdTipoMantenedora;
	private int cdPeriodoLetivo;

	public InstituicaoTipoMantenedora() { }

	public InstituicaoTipoMantenedora(int cdInstituicao,
			int cdTipoMantenedora,
			int cdPeriodoLetivo) {
		setCdInstituicao(cdInstituicao);
		setCdTipoMantenedora(cdTipoMantenedora);
		setCdPeriodoLetivo(cdPeriodoLetivo);
	}
	public void setCdInstituicao(int cdInstituicao){
		this.cdInstituicao=cdInstituicao;
	}
	public int getCdInstituicao(){
		return this.cdInstituicao;
	}
	public void setCdTipoMantenedora(int cdTipoMantenedora){
		this.cdTipoMantenedora=cdTipoMantenedora;
	}
	public int getCdTipoMantenedora(){
		return this.cdTipoMantenedora;
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
		valueToString += ", cdTipoMantenedora: " +  getCdTipoMantenedora();
		valueToString += ", cdPeriodoLetivo: " +  getCdPeriodoLetivo();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new InstituicaoTipoMantenedora(getCdInstituicao(),
			getCdTipoMantenedora(),
			getCdPeriodoLetivo());
	}

}