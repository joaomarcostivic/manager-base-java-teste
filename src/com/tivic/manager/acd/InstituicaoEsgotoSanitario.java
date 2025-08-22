package com.tivic.manager.acd;

public class InstituicaoEsgotoSanitario {

	private int cdInstituicao;
	private int cdEsgotoSanitario;
	private int cdPeriodoLetivo;

	public InstituicaoEsgotoSanitario() { }

	public InstituicaoEsgotoSanitario(int cdInstituicao,
			int cdEsgotoSanitario,
			int cdPeriodoLetivo) {
		setCdInstituicao(cdInstituicao);
		setCdEsgotoSanitario(cdEsgotoSanitario);
		setCdPeriodoLetivo(cdPeriodoLetivo);
	}
	public void setCdInstituicao(int cdInstituicao){
		this.cdInstituicao=cdInstituicao;
	}
	public int getCdInstituicao(){
		return this.cdInstituicao;
	}
	public void setCdEsgotoSanitario(int cdEsgotoSanitario){
		this.cdEsgotoSanitario=cdEsgotoSanitario;
	}
	public int getCdEsgotoSanitario(){
		return this.cdEsgotoSanitario;
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
		valueToString += ", cdEsgotoSanitario: " +  getCdEsgotoSanitario();
		valueToString += ", cdPeriodoLetivo: " +  getCdPeriodoLetivo();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new InstituicaoEsgotoSanitario(getCdInstituicao(),
			getCdEsgotoSanitario(),
			getCdPeriodoLetivo());
	}

}