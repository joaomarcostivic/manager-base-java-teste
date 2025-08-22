package com.tivic.manager.grl;

import java.util.GregorianCalendar;

public class IndicadorVariacao {

	private int cdIndicador;
	private GregorianCalendar dtInicio;
	private float prVariacao;

	public IndicadorVariacao(){ }

	public IndicadorVariacao(int cdIndicador,
			GregorianCalendar dtInicio,
			float prVariacao){
		setCdIndicador(cdIndicador);
		setDtInicio(dtInicio);
		setPrVariacao(prVariacao);
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
	public void setPrVariacao(float prVariacao){
		this.prVariacao=prVariacao;
	}
	public float getPrVariacao(){
		return this.prVariacao;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdIndicador: " +  getCdIndicador();
		valueToString += ", dtInicio: " +  sol.util.Util.formatDateTime(getDtInicio(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", prVariacao: " +  getPrVariacao();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new IndicadorVariacao(getCdIndicador(),
			getDtInicio()==null ? null : (GregorianCalendar)getDtInicio().clone(),
			getPrVariacao());
	}

}