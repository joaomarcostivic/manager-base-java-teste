package com.tivic.manager.acd;

import java.util.GregorianCalendar;

public class OcorrenciaDisciplinaAvaliacaoAluno extends com.tivic.manager.grl.Ocorrencia {

	private int cdMatriculaDisciplina;
	private int cdOfertaAvaliacao;
	private int cdOferta;
	private float vlConceitoAnterior;
	private float vlConceitoPosterior;

	public OcorrenciaDisciplinaAvaliacaoAluno() { }

	public OcorrenciaDisciplinaAvaliacaoAluno(int cdOcorrencia,
			int cdPessoa,
			String txtOcorrencia,
			GregorianCalendar dtOcorrencia,
			int cdTipoOcorrencia,
			int stOcorrencia,
			int cdSistema,
			int cdUsuario,
			int cdMatriculaDisciplina,
			int cdOfertaAvaliacao,
			int cdOferta,
			float vlConceitoAnterior,
			float vlConceitoPosterior) {
		super(cdOcorrencia,
			cdPessoa,
			txtOcorrencia,
			dtOcorrencia,
			cdTipoOcorrencia,
			stOcorrencia,
			cdSistema,
			cdUsuario);
		setCdMatriculaDisciplina(cdMatriculaDisciplina);
		setCdOfertaAvaliacao(cdOfertaAvaliacao);
		setCdOferta(cdOferta);
		setVlConceitoAnterior(vlConceitoAnterior);
		setVlConceitoPosterior(vlConceitoPosterior);
	}
	public void setCdMatriculaDisciplina(int cdMatriculaDisciplina){
		this.cdMatriculaDisciplina=cdMatriculaDisciplina;
	}
	public int getCdMatriculaDisciplina(){
		return this.cdMatriculaDisciplina;
	}
	public void setCdOfertaAvaliacao(int cdOfertaAvaliacao){
		this.cdOfertaAvaliacao=cdOfertaAvaliacao;
	}
	public int getCdOfertaAvaliacao(){
		return this.cdOfertaAvaliacao;
	}
	public void setCdOferta(int cdOferta){
		this.cdOferta=cdOferta;
	}
	public int getCdOferta(){
		return this.cdOferta;
	}
	public void setVlConceitoAnterior(float vlConceitoAnterior){
		this.vlConceitoAnterior=vlConceitoAnterior;
	}
	public float getVlConceitoAnterior(){
		return this.vlConceitoAnterior;
	}
	public void setVlConceitoPosterior(float vlConceitoPosterior){
		this.vlConceitoPosterior=vlConceitoPosterior;
	}
	public float getVlConceitoPosterior(){
		return this.vlConceitoPosterior;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdOcorrencia: " +  getCdOcorrencia();
		valueToString += ", cdMatriculaDisciplina: " +  getCdMatriculaDisciplina();
		valueToString += ", cdOfertaAvaliacao: " +  getCdOfertaAvaliacao();
		valueToString += ", cdOferta: " +  getCdOferta();
		valueToString += ", vlConceitoAnterior: " +  getVlConceitoAnterior();
		valueToString += ", vlConceitoPosterior: " +  getVlConceitoPosterior();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new OcorrenciaDisciplinaAvaliacaoAluno(getCdOcorrencia(),
			getCdPessoa(),
			getTxtOcorrencia(),
			getDtOcorrencia(),
			getCdTipoOcorrencia(),
			getStOcorrencia(),
			getCdSistema(),
			getCdUsuario(),
			getCdMatriculaDisciplina(),
			getCdOfertaAvaliacao(),
			getCdOferta(),
			getVlConceitoAnterior(),
			getVlConceitoPosterior());
	}

}