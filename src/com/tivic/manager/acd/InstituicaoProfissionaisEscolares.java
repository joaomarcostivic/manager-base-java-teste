package com.tivic.manager.acd;

public class InstituicaoProfissionaisEscolares {

	private int cdInstituicao;
	private int cdTipoProfissionaisEscolares;
	private int cdPeriodoLetivo;
	private int qtProfissionaisEscolares;

	public InstituicaoProfissionaisEscolares() { }

	public InstituicaoProfissionaisEscolares(int cdInstituicao,
			int cdTipoProfissionaisEscolares,
			int cdPeriodoLetivo,
			int qtProfissionaisEscolares) {
		setCdInstituicao(cdInstituicao);
		setCdTipoProfissionaisEscolares(cdTipoProfissionaisEscolares);
		setCdPeriodoLetivo(cdPeriodoLetivo);
		setQtProfissionaisEscolares(qtProfissionaisEscolares);
	}
	public void setCdInstituicao(int cdInstituicao){
		this.cdInstituicao=cdInstituicao;
	}
	public int getCdInstituicao(){
		return this.cdInstituicao;
	}
	public void setCdTipoProfissionaisEscolares(int cdTipoProfissionaisEscolares){
		this.cdTipoProfissionaisEscolares=cdTipoProfissionaisEscolares;
	}
	public int getCdTipoProfissionaisEscolares(){
		return this.cdTipoProfissionaisEscolares;
	}
	public void setCdPeriodoLetivo(int cdPeriodoLetivo){
		this.cdPeriodoLetivo=cdPeriodoLetivo;
	}
	public int getCdPeriodoLetivo(){
		return this.cdPeriodoLetivo;
	}
	public void setQtProfissionaisEscolares(int qtProfissionaisEscolares){
		this.qtProfissionaisEscolares=qtProfissionaisEscolares;
	}
	public int getQtProfissionaisEscolares(){
		return this.qtProfissionaisEscolares;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdInstituicao: " +  getCdInstituicao();
		valueToString += ", cdTipoProfissionaisEscolares: " +  getCdTipoProfissionaisEscolares();
		valueToString += ", cdPeriodoLetivo: " +  getCdPeriodoLetivo();
		valueToString += ", qtProfissionaisEscolares: " +  getQtProfissionaisEscolares();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new InstituicaoProfissionaisEscolares(getCdInstituicao(),
			getCdTipoProfissionaisEscolares(),
			getCdPeriodoLetivo(),
			getQtProfissionaisEscolares());
	}

}