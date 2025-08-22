package com.tivic.manager.acd;

public class InstituicaoInstrumentosPedagogicos {

	private int cdInstituicao;
	private int cdTipoInstrumentosPedagogicos;
	private int cdPeriodoLetivo;

	public InstituicaoInstrumentosPedagogicos() { }

	public InstituicaoInstrumentosPedagogicos(int cdInstituicao,
			int cdTipoInstrumentosPedagogicos,
			int cdPeriodoLetivo) {
		setCdInstituicao(cdInstituicao);
		setCdTipoInstrumentosPedagogicos(cdTipoInstrumentosPedagogicos);
		setCdPeriodoLetivo(cdPeriodoLetivo);
	}
	public void setCdInstituicao(int cdInstituicao){
		this.cdInstituicao=cdInstituicao;
	}
	public int getCdInstituicao(){
		return this.cdInstituicao;
	}
	public void setCdTipoInstrumentosPedagogicos(int cdTipoInstrumentosPedagogicos){
		this.cdTipoInstrumentosPedagogicos=cdTipoInstrumentosPedagogicos;
	}
	public int getCdTipoInstrumentosPedagogicos(){
		return this.cdTipoInstrumentosPedagogicos;
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
		valueToString += ", cdTipoInstrumentosPedagogicos: " +  getCdTipoInstrumentosPedagogicos();
		valueToString += ", cdPeriodoLetivo: " +  getCdPeriodoLetivo();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new InstituicaoInstrumentosPedagogicos(getCdInstituicao(),
			getCdTipoInstrumentosPedagogicos(),
			getCdPeriodoLetivo());
	}

}