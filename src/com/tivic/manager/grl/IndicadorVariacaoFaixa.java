package com.tivic.manager.grl;

import java.util.GregorianCalendar;

public class IndicadorVariacaoFaixa {

	private int cdFaixa;
	private int cdIndicador;
	private GregorianCalendar dtInicio;
	private float vlReferencia1;
	private float vlReferencia2;
	private float vlReferencia3;

	public IndicadorVariacaoFaixa(int cdFaixa,
			int cdIndicador,
			GregorianCalendar dtInicio,
			float vlReferencia1,
			float vlReferencia2,
			float vlReferencia3){
		setCdFaixa(cdFaixa);
		setCdIndicador(cdIndicador);
		setDtInicio(dtInicio);
		setVlReferencia1(vlReferencia1);
		setVlReferencia2(vlReferencia2);
		setVlReferencia3(vlReferencia3);
	}
	public void setCdFaixa(int cdFaixa){
		this.cdFaixa=cdFaixa;
	}
	public int getCdFaixa(){
		return this.cdFaixa;
	}
	public void setCdIndicador(int cdIndicador){
		this.cdIndicador=cdIndicador;
	}
	public int getCdIndicador(){
		return this.cdIndicador;
	}
	public void setDtInicio(GregorianCalendar dtInicio){
		this.dtInicio=dtInicio;
	}
	public GregorianCalendar getDtInicio(){
		return this.dtInicio;
	}
	public void setVlReferencia1(float vlReferencia1){
		this.vlReferencia1=vlReferencia1;
	}
	public float getVlReferencia1(){
		return this.vlReferencia1;
	}
	public void setVlReferencia2(float vlReferencia2){
		this.vlReferencia2=vlReferencia2;
	}
	public float getVlReferencia2(){
		return this.vlReferencia2;
	}
	public void setVlReferencia3(float vlReferencia3){
		this.vlReferencia3=vlReferencia3;
	}
	public float getVlReferencia3(){
		return this.vlReferencia3;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdFaixa: " +  getCdFaixa();
		valueToString += ", cdIndicador: " +  getCdIndicador();
		valueToString += ", dtInicio: " +  sol.util.Util.formatDateTime(getDtInicio(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", vlReferencia1: " +  getVlReferencia1();
		valueToString += ", vlReferencia2: " +  getVlReferencia2();
		valueToString += ", vlReferencia3: " +  getVlReferencia3();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new IndicadorVariacaoFaixa(getCdFaixa(),
			getCdIndicador(),
			getDtInicio()==null ? null : (GregorianCalendar)getDtInicio().clone(),
			getVlReferencia1(),
			getVlReferencia2(),
			getVlReferencia3());
	}

}