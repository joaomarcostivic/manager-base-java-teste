package com.tivic.manager.acd;

public class InstituicaoRecursoAcessibilidade {

	private int cdInstituicao;
	private int cdTipoRecursoAcessibilidade;
	private int cdPeriodoLetivo;

	public InstituicaoRecursoAcessibilidade() { }

	public InstituicaoRecursoAcessibilidade(int cdInstituicao,
			int cdTipoRecursoAcessibilidade,
			int cdPeriodoLetivo) {
		setCdInstituicao(cdInstituicao);
		setCdTipoRecursoAcessibilidade(cdTipoRecursoAcessibilidade);
		setCdPeriodoLetivo(cdPeriodoLetivo);
	}
	public void setCdInstituicao(int cdInstituicao){
		this.cdInstituicao=cdInstituicao;
	}
	public int getCdInstituicao(){
		return this.cdInstituicao;
	}
	public void setCdTipoRecursoAcessibilidade(int cdTipoRecursoAcessibilidade){
		this.cdTipoRecursoAcessibilidade=cdTipoRecursoAcessibilidade;
	}
	public int getCdTipoRecursoAcessibilidade(){
		return this.cdTipoRecursoAcessibilidade;
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
		valueToString += ", cdTipoRecursoAcessibilidade: " +  getCdTipoRecursoAcessibilidade();
		valueToString += ", cdPeriodoLetivo: " +  getCdPeriodoLetivo();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new InstituicaoRecursoAcessibilidade(getCdInstituicao(),
			getCdTipoRecursoAcessibilidade(),
			getCdPeriodoLetivo());
	}

}