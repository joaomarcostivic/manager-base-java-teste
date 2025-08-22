package com.tivic.manager.acd;

public class InstituicaoCurso {

	private int cdInstituicao;
	private int cdCurso;
	private int cdPeriodoLetivo;

	public InstituicaoCurso() { }

	public InstituicaoCurso(int cdInstituicao,
			int cdCurso,
			int cdPeriodoLetivo) {
		setCdInstituicao(cdInstituicao);
		setCdCurso(cdCurso);
		setCdPeriodoLetivo(cdPeriodoLetivo);
	}
	public void setCdInstituicao(int cdInstituicao){
		this.cdInstituicao=cdInstituicao;
	}
	public int getCdInstituicao(){
		return this.cdInstituicao;
	}
	public void setCdCurso(int cdCurso){
		this.cdCurso=cdCurso;
	}
	public int getCdCurso(){
		return this.cdCurso;
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
		valueToString += ", cdCurso: " +  getCdCurso();
		valueToString += ", cdPeriodoLetivo: " +  getCdPeriodoLetivo();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new InstituicaoCurso(getCdInstituicao(),
			getCdCurso(),
			getCdPeriodoLetivo());
	}

}