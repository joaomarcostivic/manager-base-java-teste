package com.tivic.manager.acd;

import java.util.GregorianCalendar;

public class InstituicaoPeriodo {

	private int cdPeriodoLetivo;
	private int cdInstituicao;
	private String nmPeriodoLetivo;
	private GregorianCalendar dtInicial;
	private GregorianCalendar dtFinal;
	private int nrDiasLetivos;
	private int stPeriodoLetivo;
	private int cdTipoPeriodo;
	private int cdPeriodoLetivoSuperior;

	public InstituicaoPeriodo(){ }

	public InstituicaoPeriodo(int cdPeriodoLetivo,
			int cdInstituicao,
			String nmPeriodoLetivo,
			GregorianCalendar dtInicial,
			GregorianCalendar dtFinal,
			int nrDiasLetivos,
			int stPeriodoLetivo,
			int cdTipoPeriodo,
			int cdPeriodoLetivoSuperior){
		setCdPeriodoLetivo(cdPeriodoLetivo);
		setCdInstituicao(cdInstituicao);
		setNmPeriodoLetivo(nmPeriodoLetivo);
		setDtInicial(dtInicial);
		setDtFinal(dtFinal);
		setNrDiasLetivos(nrDiasLetivos);
		setStPeriodoLetivo(stPeriodoLetivo);
		setCdTipoPeriodo(cdTipoPeriodo);
		setCdPeriodoLetivoSuperior(cdPeriodoLetivoSuperior);
	}
	public void setCdPeriodoLetivo(int cdPeriodoLetivo){
		this.cdPeriodoLetivo=cdPeriodoLetivo;
	}
	public int getCdPeriodoLetivo(){
		return this.cdPeriodoLetivo;
	}
	public void setNmPeriodoLetivo(String nmPeriodoLetivo){
		this.nmPeriodoLetivo=nmPeriodoLetivo;
	}
	public String getNmPeriodoLetivo(){
		return this.nmPeriodoLetivo;
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
	public void setNrDiasLetivos(int nrDiasLetivos){
		this.nrDiasLetivos=nrDiasLetivos;
	}
	public int getNrDiasLetivos(){
		return this.nrDiasLetivos;
	}
	public void setStPeriodoLetivo(int stPeriodoLetivo){
		this.stPeriodoLetivo=stPeriodoLetivo;
	}
	public int getStPeriodoLetivo(){
		return this.stPeriodoLetivo;
	}
	public void setCdTipoPeriodo(int cdTipoPeriodo){
		this.cdTipoPeriodo=cdTipoPeriodo;
	}
	public int getCdTipoPeriodo(){
		return this.cdTipoPeriodo;
	}
	public void setCdInstituicao(int cdInstituicao){
		this.cdInstituicao=cdInstituicao;
	}
	public int getCdInstituicao(){
		return this.cdInstituicao;
	}
	public void setCdPeriodoLetivoSuperior(int cdPeriodoLetivoSuperior){
		this.cdPeriodoLetivoSuperior=cdPeriodoLetivoSuperior;
	}
	public int getCdPeriodoLetivoSuperior(){
		return this.cdPeriodoLetivoSuperior;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "\"cdPeriodoLetivo\": " +  getCdPeriodoLetivo();
		valueToString += ", \"cdInstituicao\": " +  getCdInstituicao();
		valueToString += ", \"nmPeriodoLetivo\": \"" +  getNmPeriodoLetivo()+"\"";
		valueToString += ", \"dtInicial\": \"" +  sol.util.Util.formatDateTime(getDtInicial(), "dd/MM/yyyy HH:mm:ss:SSS", "")+"\"";
		valueToString += ", \"dtFinal\": \"" +  sol.util.Util.formatDateTime(getDtFinal(), "dd/MM/yyyy HH:mm:ss:SSS", "")+"\"";
		valueToString += ", \"nrDiasLetivos\": " +  getNrDiasLetivos();
		valueToString += ", \"stPeriodoLetivo\": " +  getStPeriodoLetivo();
		valueToString += ", \"cdTipoPeriodo\": " +  getCdTipoPeriodo();
		valueToString += ", \"cdPeriodoLetivoSuperior\": " +  getCdPeriodoLetivoSuperior();
		return "{" + valueToString + "}";
	}
	
	public String toORM() {
		String valueToString = "";
		valueToString += "\"cdPeriodoLetivo\": " +  getCdPeriodoLetivo();
		valueToString += ", \"cdInstituicao\": " +  getCdInstituicao();
		valueToString += ", \"nmPeriodoLetivo\": \"" +  getNmPeriodoLetivo()+"\"";
		valueToString += ", \"dtInicial\": \"" +  sol.util.Util.formatDateTime(getDtInicial(), "yyyy-MM-dd", "")+"\"";
		valueToString += ", \"dtFinal\": \"" +  sol.util.Util.formatDateTime(getDtFinal(), "yyyy-MM-dd", "")+"\"";
		valueToString += ", \"nrDiasLetivos\": " +  getNrDiasLetivos();
		valueToString += ", \"stPeriodoLetivo\": " +  getStPeriodoLetivo();
		valueToString += ", \"cdTipoPeriodo\": " +  getCdTipoPeriodo();
		valueToString += ", \"cdPeriodoLetivoSuperior\": " +  getCdPeriodoLetivoSuperior();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new InstituicaoPeriodo(getCdPeriodoLetivo(),
			getCdInstituicao(),
			getNmPeriodoLetivo(),
			getDtInicial()==null ? null : (GregorianCalendar)getDtInicial().clone(),
			getDtFinal()==null ? null : (GregorianCalendar)getDtFinal().clone(),
			getNrDiasLetivos(),
			getStPeriodoLetivo(),
			getCdTipoPeriodo(),
			getCdPeriodoLetivoSuperior());
	}

}