package com.tivic.manager.prc;

public class OutroInteressado {

	private int cdOutroInteressado;
	private int cdProcesso;
	private String nmOutroInteressado;
	private String nmQualificacao;

	public OutroInteressado(){ }

	public OutroInteressado(int cdOutroInteressado,
			int cdProcesso,
			String nmOutroInteressado,
			String nmQualificacao){
		setCdOutroInteressado(cdOutroInteressado);
		setCdProcesso(cdProcesso);
		setNmOutroInteressado(nmOutroInteressado);
		setNmQualificacao(nmQualificacao);
	}
	public void setCdOutroInteressado(int cdOutroInteressado){
		this.cdOutroInteressado=cdOutroInteressado;
	}
	public int getCdOutroInteressado(){
		return this.cdOutroInteressado;
	}
	public void setCdProcesso(int cdProcesso){
		this.cdProcesso=cdProcesso;
	}
	public int getCdProcesso(){
		return this.cdProcesso;
	}
	public void setNmOutroInteressado(String nmOutroInteressado){
		this.nmOutroInteressado=nmOutroInteressado;
	}
	public String getNmOutroInteressado(){
		return this.nmOutroInteressado;
	}
	public void setNmQualificacao(String nmQualificacao){
		this.nmQualificacao=nmQualificacao;
	}
	public String getNmQualificacao(){
		return this.nmQualificacao;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdOutroInteressado: " +  getCdOutroInteressado();
		valueToString += ", cdProcesso: " +  getCdProcesso();
		valueToString += ", nmOutroInteressado: " +  getNmOutroInteressado();
		valueToString += ", nmQualificacao: " +  getNmQualificacao();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new OutroInteressado(getCdOutroInteressado(),
			getCdProcesso(),
			getNmOutroInteressado(),
			getNmQualificacao());
	}

}
