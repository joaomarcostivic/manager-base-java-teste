package com.tivic.manager.acd;

public class InstituicaoDestinacaoLixo {

	private int cdInstituicao;
	private int cdDestinacaoLixo;
	private int cdPeriodoLetivo;

	public InstituicaoDestinacaoLixo() { }

	public InstituicaoDestinacaoLixo(int cdInstituicao,
			int cdDestinacaoLixo,
			int cdPeriodoLetivo) {
		setCdInstituicao(cdInstituicao);
		setCdDestinacaoLixo(cdDestinacaoLixo);
		setCdPeriodoLetivo(cdPeriodoLetivo);
	}
	public void setCdInstituicao(int cdInstituicao){
		this.cdInstituicao=cdInstituicao;
	}
	public int getCdInstituicao(){
		return this.cdInstituicao;
	}
	public void setCdDestinacaoLixo(int cdDestinacaoLixo){
		this.cdDestinacaoLixo=cdDestinacaoLixo;
	}
	public int getCdDestinacaoLixo(){
		return this.cdDestinacaoLixo;
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
		valueToString += ", cdDestinacaoLixo: " +  getCdDestinacaoLixo();
		valueToString += ", cdPeriodoLetivo: " +  getCdPeriodoLetivo();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new InstituicaoDestinacaoLixo(getCdInstituicao(),
			getCdDestinacaoLixo(),
			getCdPeriodoLetivo());
	}

}