package com.tivic.manager.acd;

import java.util.GregorianCalendar;

public class DisciplinaAvaliacaoAluno {

	private int cdOfertaAvaliacao;
	private int cdOferta;
	private float vlConceito;
	private float vlConceitoAproveitamento;
	private GregorianCalendar dtLancamento;
	private GregorianCalendar dtAplicacao;
	private int lgSegundaChamada;
	private int cdConceito;
	private String txtObservacao;
	private int cdMatricula;
	
	public DisciplinaAvaliacaoAluno(){ }

	public DisciplinaAvaliacaoAluno(int cdOfertaAvaliacao,
			int cdOferta,
			float vlConceito,
			float vlConceitoAproveitamento,
			GregorianCalendar dtLancamento,
			GregorianCalendar dtAplicacao,
			int lgSegundaChamada,
			int cdConceito,
			String txtObservacao,
			int cdMatricula){
		setCdOfertaAvaliacao(cdOfertaAvaliacao);
		setCdOferta(cdOferta);
		setVlConceito(vlConceito);
		setVlConceitoAproveitamento(vlConceitoAproveitamento);
		setDtLancamento(dtLancamento);
		setDtAplicacao(dtAplicacao);
		setLgSegundaChamada(lgSegundaChamada);
		setCdConceito(cdConceito);
		setTxtObservacao(txtObservacao);
		setCdMatricula(cdMatricula);
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
	public void setVlConceito(float vlConceito){
		this.vlConceito=vlConceito;
	}
	public float getVlConceito(){
		return this.vlConceito;
	}
	public void setVlConceitoAproveitamento(float vlConceitoAproveitamento){
		this.vlConceitoAproveitamento=vlConceitoAproveitamento;
	}
	public float getVlConceitoAproveitamento(){
		return this.vlConceitoAproveitamento;
	}
	public void setDtLancamento(GregorianCalendar dtLancamento){
		this.dtLancamento=dtLancamento;
	}
	public GregorianCalendar getDtLancamento(){
		return this.dtLancamento;
	}
	public void setDtAplicacao(GregorianCalendar dtAplicacao){
		this.dtAplicacao=dtAplicacao;
	}
	public GregorianCalendar getDtAplicacao(){
		return this.dtAplicacao;
	}
	public void setLgSegundaChamada(int lgSegundaChamada){
		this.lgSegundaChamada=lgSegundaChamada;
	}
	public int getLgSegundaChamada(){
		return this.lgSegundaChamada;
	}
	public void setCdConceito(int cdConceito){
		this.cdConceito=cdConceito;
	}
	public int getCdConceito(){
		return this.cdConceito;
	}
	public void setTxtObservacao(String txtObservacao) {
		this.txtObservacao = txtObservacao;
	}
	public String getTxtObservacao() {
		return txtObservacao;
	}
	public void setCdMatricula(int cdMatricula){
		this.cdMatricula=cdMatricula;
	}
	public int getCdMatricula(){
		return this.cdMatricula;
	}
	
	public String toString() {
		String valueToString = "";
		valueToString += "\"cdOfertaAvaliacao\": " +  getCdOfertaAvaliacao();
		valueToString += ", \"cdOferta\": " +  getCdOferta();
		valueToString += ", \"vlConceito\": " +  getVlConceito();
		valueToString += ", \"vlConceitoAproveitamento\": " +  getVlConceitoAproveitamento();
		valueToString += ", \"dtLancamento\": \"" +  sol.util.Util.formatDateTime(getDtLancamento(), "dd/MM/yyyy HH:mm:ss:SSS", "")+"\"";
		valueToString += ", \"dtAplicacao\": \"" +  sol.util.Util.formatDateTime(getDtAplicacao(), "dd/MM/yyyy HH:mm:ss:SSS", "")+"\"";
		valueToString += ", \"lgSegundaChamada\": " +  getLgSegundaChamada();
		valueToString += ", \"cdConceito\": " +  getCdConceito();
		valueToString += ", \"txtObservacao\": \"" +  getTxtObservacao()+"\"";
		valueToString += ", \"cdMatricula\": " +  getCdMatricula();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new DisciplinaAvaliacaoAluno(getCdOfertaAvaliacao(),
			getCdOferta(),
			getVlConceito(),
			getVlConceitoAproveitamento(),
			getDtLancamento()==null ? null : (GregorianCalendar)getDtLancamento().clone(),
			getDtAplicacao()==null ? null : (GregorianCalendar)getDtAplicacao().clone(),
			getLgSegundaChamada(),
			getCdConceito(),
			getTxtObservacao(),
			getCdMatricula());
	}

}