package com.tivic.manager.acd;

import java.util.GregorianCalendar;

public class CursoUnidade {

	private int cdUnidade;
	private int cdCurso;
	private String nmUnidade;
	private GregorianCalendar dtInicial;
	private GregorianCalendar dtFinal;
	private String txtObservacao;
	private int nrOrdem;

	public CursoUnidade(){ }

	public CursoUnidade(int cdUnidade,
			int cdCurso,
			String nmUnidade,
			GregorianCalendar dtInicial,
			GregorianCalendar dtFinal,
			String txtObservacao,
			int nrOrdem){
		setCdUnidade(cdUnidade);
		setCdCurso(cdCurso);
		setNmUnidade(nmUnidade);
		setDtInicial(dtInicial);
		setDtFinal(dtFinal);
		setTxtObservacao(txtObservacao);
		setNrOrdem(nrOrdem);
	}
	public void setCdUnidade(int cdUnidade){
		this.cdUnidade=cdUnidade;
	}
	public int getCdUnidade(){
		return this.cdUnidade;
	}
	public void setCdCurso(int cdCurso){
		this.cdCurso=cdCurso;
	}
	public int getCdCurso(){
		return this.cdCurso;
	}
	public void setNmUnidade(String nmUnidade){
		this.nmUnidade=nmUnidade;
	}
	public String getNmUnidade(){
		return this.nmUnidade;
	}
	public void setDtInicial(GregorianCalendar dtInicial){
		this.dtInicial=dtInicial;
	}
	public GregorianCalendar getDtInicial(){
		return this.dtInicial;
	}
	public void setDtFinal(GregorianCalendar dtFinal){
		this.dtFinal=dtFinal;
	}
	public GregorianCalendar getDtFinal(){
		return this.dtFinal;
	}
	public void setTxtObservacao(String txtObservacao){
		this.txtObservacao=txtObservacao;
	}
	public String getTxtObservacao(){
		return this.txtObservacao;
	}
	public void setNrOrdem(int nrOrdem){
		this.nrOrdem=nrOrdem;
	}
	public int getNrOrdem(){
		return this.nrOrdem;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "\"cdUnidade\": " +  getCdUnidade();
		valueToString += ", \"cdCurso\": " +  getCdCurso();
		valueToString += ", \"nmUnidade\": \"" +  getNmUnidade()+"\"";
		valueToString += ", \"dtInicial\": \"" +  sol.util.Util.formatDateTime(getDtInicial(), "dd/MM/yyyy HH:mm:ss:SSS", "")+"\"";
		valueToString += ", \"dtFinal\": \"" +  sol.util.Util.formatDateTime(getDtFinal(), "dd/MM/yyyy HH:mm:ss:SSS", "")+"\"";
		valueToString += ", \"txtObservacao\": \"" +  getTxtObservacao()+"\"";
		valueToString += ", \"nrOrdem\": " +  getNrOrdem();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new CursoUnidade(getCdUnidade(),
			getCdCurso(),
			getNmUnidade(),
			getDtInicial()==null ? null : (GregorianCalendar)getDtInicial().clone(),
			getDtFinal()==null ? null : (GregorianCalendar)getDtFinal().clone(),
			getTxtObservacao(),
			getNrOrdem());
	}

}
