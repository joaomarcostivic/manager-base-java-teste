package com.tivic.manager.bpm;

import java.util.GregorianCalendar;

public class ComponenteReferencia {

	private int cdComponente;
	private int cdReferencia;
	private String nmComponente;
	private GregorianCalendar dtGarantia;
	private GregorianCalendar dtValidade;
	private GregorianCalendar dtAquisicao;
	private GregorianCalendar dtBaixa;
	private String nrSerie;
	private int stComponente;

	public ComponenteReferencia(int cdComponente,
			int cdReferencia,
			String nmComponente,
			GregorianCalendar dtGarantia,
			GregorianCalendar dtValidade,
			GregorianCalendar dtAquisicao,
			GregorianCalendar dtBaixa,
			String nrSerie,
			int stComponente){
		setCdComponente(cdComponente);
		setCdReferencia(cdReferencia);
		setNmComponente(nmComponente);
		setDtGarantia(dtGarantia);
		setDtValidade(dtValidade);
		setDtAquisicao(dtAquisicao);
		setDtBaixa(dtBaixa);
		setNrSerie(nrSerie);
		setStComponente(stComponente);
	}
	public void setCdComponente(int cdComponente){
		this.cdComponente=cdComponente;
	}
	public int getCdComponente(){
		return this.cdComponente;
	}
	public void setCdReferencia(int cdReferencia){
		this.cdReferencia=cdReferencia;
	}
	public int getCdReferencia(){
		return this.cdReferencia;
	}
	public void setNmComponente(String nmComponente){
		this.nmComponente=nmComponente;
	}
	public String getNmComponente(){
		return this.nmComponente;
	}
	public void setDtGarantia(GregorianCalendar dtGarantia){
		this.dtGarantia=dtGarantia;
	}
	public GregorianCalendar getDtGarantia(){
		return this.dtGarantia;
	}
	public void setDtValidade(GregorianCalendar dtValidade){
		this.dtValidade=dtValidade;
	}
	public GregorianCalendar getDtValidade(){
		return this.dtValidade;
	}
	public void setDtAquisicao(GregorianCalendar dtAquisicao){
		this.dtAquisicao=dtAquisicao;
	}
	public GregorianCalendar getDtAquisicao(){
		return this.dtAquisicao;
	}
	public void setDtBaixa(GregorianCalendar dtBaixa){
		this.dtBaixa=dtBaixa;
	}
	public GregorianCalendar getDtBaixa(){
		return this.dtBaixa;
	}
	public void setNrSerie(String nrSerie){
		this.nrSerie=nrSerie;
	}
	public String getNrSerie(){
		return this.nrSerie;
	}
	public void setStComponente(int stComponente){
		this.stComponente=stComponente;
	}
	public int getStComponente(){
		return this.stComponente;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdComponente: " +  getCdComponente();
		valueToString += ", cdReferencia: " +  getCdReferencia();
		valueToString += ", nmComponente: " +  getNmComponente();
		valueToString += ", dtGarantia: " +  sol.util.Util.formatDateTime(getDtGarantia(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", dtValidade: " +  sol.util.Util.formatDateTime(getDtValidade(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", dtAquisicao: " +  sol.util.Util.formatDateTime(getDtAquisicao(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", dtBaixa: " +  sol.util.Util.formatDateTime(getDtBaixa(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", nrSerie: " +  getNrSerie();
		valueToString += ", stComponente: " +  getStComponente();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new ComponenteReferencia(getCdComponente(),
			getCdReferencia(),
			getNmComponente(),
			getDtGarantia()==null ? null : (GregorianCalendar)getDtGarantia().clone(),
			getDtValidade()==null ? null : (GregorianCalendar)getDtValidade().clone(),
			getDtAquisicao()==null ? null : (GregorianCalendar)getDtAquisicao().clone(),
			getDtBaixa()==null ? null : (GregorianCalendar)getDtBaixa().clone(),
			getNrSerie(),
			getStComponente());
	}

}
