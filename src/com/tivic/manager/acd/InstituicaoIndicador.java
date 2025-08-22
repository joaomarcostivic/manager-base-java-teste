package com.tivic.manager.acd;

import java.util.GregorianCalendar;

public class InstituicaoIndicador {

	private int cdInstituicao;
	private int cdIndicador;
	private int cdPeriodoLetivo;
	private Double vlIndicadorInicial;
	private Double vlIndicadorFinal;
	private GregorianCalendar dtIndicador;

	public InstituicaoIndicador() { }

	public InstituicaoIndicador(int cdInstituicao,
			int cdIndicador,
			int cdPeriodoLetivo,
			Double vlIndicadorInicial,
			Double vlIndicadorFinal,
			GregorianCalendar dtIndicador) {
		setCdInstituicao(cdInstituicao);
		setCdIndicador(cdIndicador);
		setCdPeriodoLetivo(cdPeriodoLetivo);
		setVlIndicadorInicial(vlIndicadorInicial);
		setVlIndicadorFinal(vlIndicadorFinal);
		setDtIndicador(dtIndicador);
	}
	public void setCdInstituicao(int cdInstituicao){
		this.cdInstituicao=cdInstituicao;
	}
	public int getCdInstituicao(){
		return this.cdInstituicao;
	}
	public void setCdIndicador(int cdIndicador){
		this.cdIndicador=cdIndicador;
	}
	public int getCdIndicador(){
		return this.cdIndicador;
	}
	public void setCdPeriodoLetivo(int cdPeriodoLetivo){
		this.cdPeriodoLetivo=cdPeriodoLetivo;
	}
	public int getCdPeriodoLetivo(){
		return this.cdPeriodoLetivo;
	}
	public void setVlIndicadorInicial(Double vlIndicadorInicial){
		this.vlIndicadorInicial=vlIndicadorInicial;
	}
	public Double getVlIndicadorInicial(){
		return this.vlIndicadorInicial;
	}
	public void setVlIndicadorFinal(Double vlIndicadorFinal){
		this.vlIndicadorFinal=vlIndicadorFinal;
	}
	public Double getVlIndicadorFinal(){
		return this.vlIndicadorFinal;
	}
	public void setDtIndicador(GregorianCalendar dtIndicador){
		this.dtIndicador=dtIndicador;
	}
	public GregorianCalendar getDtIndicador(){
		return this.dtIndicador;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdInstituicao: " +  getCdInstituicao();
		valueToString += ", cdIndicador: " +  getCdIndicador();
		valueToString += ", cdPeriodoLetivo: " +  getCdPeriodoLetivo();
		valueToString += ", vlIndicadorInicial: " +  getVlIndicadorInicial();
		valueToString += ", vlIndicadorFinal: " +  getVlIndicadorFinal();
		valueToString += ", dtIndicador: " +  sol.util.Util.formatDateTime(getDtIndicador(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new InstituicaoIndicador(getCdInstituicao(),
			getCdIndicador(),
			getCdPeriodoLetivo(),
			getVlIndicadorInicial(),
			getVlIndicadorFinal(),
			getDtIndicador()==null ? null : (GregorianCalendar)getDtIndicador().clone());
	}

}