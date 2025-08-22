package com.tivic.manager.acd;

import java.util.GregorianCalendar;

public class OcorrenciaOfertaAvaliacao extends com.tivic.manager.grl.Ocorrencia {

	private int cdOfertaAvaliacao;
	private int cdOferta;
	private float vlPesoAnterior;
	private float vlPesoPosterior;
	private GregorianCalendar dtAvaliacaoAnterior;
	private GregorianCalendar dtAvaliacaoPosterior;
	private int stOfertaAvaliacaoAnterior;
	private int stOfertaAvaliacaoPosterior;

	public OcorrenciaOfertaAvaliacao() { }

	public OcorrenciaOfertaAvaliacao(int cdOcorrencia,
			int cdPessoa,
			String txtOcorrencia,
			GregorianCalendar dtOcorrencia,
			int cdTipoOcorrencia,
			int stOcorrencia,
			int cdSistema,
			int cdUsuario,
			int cdOfertaAvaliacao,
			int cdOferta,
			float vlPesoAnterior,
			float vlPesoPosterior,
			GregorianCalendar dtAvaliacaoAnterior,
			GregorianCalendar dtAvaliacaoPosterior,
			int stOfertaAvaliacaoAnterior,
			int stOfertaAvaliacaoPosterior) {
		super(cdOcorrencia,
			cdPessoa,
			txtOcorrencia,
			dtOcorrencia,
			cdTipoOcorrencia,
			stOcorrencia,
			cdSistema,
			cdUsuario);
		setCdOfertaAvaliacao(cdOfertaAvaliacao);
		setCdOferta(cdOferta);
		setVlPesoAnterior(vlPesoAnterior);
		setVlPesoPosterior(vlPesoPosterior);
		setDtAvaliacaoAnterior(dtAvaliacaoAnterior);
		setDtAvaliacaoPosterior(dtAvaliacaoPosterior);
		setStOfertaAvaliacaoAnterior(stOfertaAvaliacaoAnterior);
		setStOfertaAvaliacaoPosterior(stOfertaAvaliacaoPosterior);
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
	public void setVlPesoAnterior(float vlPesoAnterior){
		this.vlPesoAnterior=vlPesoAnterior;
	}
	public float getVlPesoAnterior(){
		return this.vlPesoAnterior;
	}
	public void setVlPesoPosterior(float vlPesoPosterior){
		this.vlPesoPosterior=vlPesoPosterior;
	}
	public float getVlPesoPosterior(){
		return this.vlPesoPosterior;
	}
	public void setDtAvaliacaoAnterior(GregorianCalendar dtAvaliacaoAnterior){
		this.dtAvaliacaoAnterior=dtAvaliacaoAnterior;
	}
	public GregorianCalendar getDtAvaliacaoAnterior(){
		return this.dtAvaliacaoAnterior;
	}
	public void setDtAvaliacaoPosterior(GregorianCalendar dtAvaliacaoPosterior){
		this.dtAvaliacaoPosterior=dtAvaliacaoPosterior;
	}
	public GregorianCalendar getDtAvaliacaoPosterior(){
		return this.dtAvaliacaoPosterior;
	}
	public void setStOfertaAvaliacaoAnterior(int stOfertaAvaliacaoAnterior){
		this.stOfertaAvaliacaoAnterior=stOfertaAvaliacaoAnterior;
	}
	public int getStOfertaAvaliacaoAnterior(){
		return this.stOfertaAvaliacaoAnterior;
	}
	public void setStOfertaAvaliacaoPosterior(int stOfertaAvaliacaoPosterior){
		this.stOfertaAvaliacaoPosterior=stOfertaAvaliacaoPosterior;
	}
	public int getStOfertaAvaliacaoPosterior(){
		return this.stOfertaAvaliacaoPosterior;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdOcorrencia: " +  getCdOcorrencia();
		valueToString += ", cdOfertaAvaliacao: " +  getCdOfertaAvaliacao();
		valueToString += ", cdOferta: " +  getCdOferta();
		valueToString += ", vlPesoAnterior: " +  getVlPesoAnterior();
		valueToString += ", vlPesoPosterior: " +  getVlPesoPosterior();
		valueToString += ", dtAvaliacaoAnterior: " +  sol.util.Util.formatDateTime(getDtAvaliacaoAnterior(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", dtAvaliacaoPosterior: " +  sol.util.Util.formatDateTime(getDtAvaliacaoPosterior(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", stOfertaAvaliacaoAnterior: " +  getStOfertaAvaliacaoAnterior();
		valueToString += ", stOfertaAvaliacaoPosterior: " +  getStOfertaAvaliacaoPosterior();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new OcorrenciaOfertaAvaliacao(getCdOcorrencia(),
			getCdPessoa(),
			getTxtOcorrencia(),
			getDtOcorrencia(),
			getCdTipoOcorrencia(),
			getStOcorrencia(),
			getCdSistema(),
			getCdUsuario(),
			getCdOfertaAvaliacao(),
			getCdOferta(),
			getVlPesoAnterior(),
			getVlPesoPosterior(),
			getDtAvaliacaoAnterior()==null ? null : (GregorianCalendar)getDtAvaliacaoAnterior().clone(),
			getDtAvaliacaoPosterior()==null ? null : (GregorianCalendar)getDtAvaliacaoPosterior().clone(),
			getStOfertaAvaliacaoAnterior(),
			getStOfertaAvaliacaoPosterior());
	}

}