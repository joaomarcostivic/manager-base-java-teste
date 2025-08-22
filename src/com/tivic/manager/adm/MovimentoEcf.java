package com.tivic.manager.adm;

import java.util.GregorianCalendar;

public class MovimentoEcf {

	private int cdReferencia;
	private GregorianCalendar dtMovimento;
	private String nrContadorInicial;
	private String nrContadorFinal;
	private String nrReducao;
	private String nrContadorReinicio;
	private float vlVendaBruta;
	private float vlGeralAcumulado;

	public MovimentoEcf(int cdReferencia,
			GregorianCalendar dtMovimento,
			String nrContadorInicial,
			String nrContadorFinal,
			String nrReducao,
			String nrContadorReinicio,
			float vlVendaBruta,
			float vlGeralAcumulado){
		setCdReferencia(cdReferencia);
		setDtMovimento(dtMovimento);
		setNrContadorInicial(nrContadorInicial);
		setNrContadorFinal(nrContadorFinal);
		setNrReducao(nrReducao);
		setNrContadorReinicio(nrContadorReinicio);
		setVlVendaBruta(vlVendaBruta);
		setVlGeralAcumulado(vlGeralAcumulado);
	}
	public void setCdReferencia(int cdReferencia){
		this.cdReferencia=cdReferencia;
	}
	public int getCdReferencia(){
		return this.cdReferencia;
	}
	public void setDtMovimento(GregorianCalendar dtMovimento){
		this.dtMovimento=dtMovimento;
	}
	public GregorianCalendar getDtMovimento(){
		return this.dtMovimento;
	}
	public void setNrContadorInicial(String nrContadorInicial){
		this.nrContadorInicial=nrContadorInicial;
	}
	public String getNrContadorInicial(){
		return this.nrContadorInicial;
	}
	public void setNrContadorFinal(String nrContadorFinal){
		this.nrContadorFinal=nrContadorFinal;
	}
	public String getNrContadorFinal(){
		return this.nrContadorFinal;
	}
	public void setNrReducao(String nrReducao){
		this.nrReducao=nrReducao;
	}
	public String getNrReducao(){
		return this.nrReducao;
	}
	public void setNrContadorReinicio(String nrContadorReinicio){
		this.nrContadorReinicio=nrContadorReinicio;
	}
	public String getNrContadorReinicio(){
		return this.nrContadorReinicio;
	}
	public void setVlVendaBruta(float vlVendaBruta){
		this.vlVendaBruta=vlVendaBruta;
	}
	public float getVlVendaBruta(){
		return this.vlVendaBruta;
	}
	public void setVlGeralAcumulado(float vlGeralAcumulado){
		this.vlGeralAcumulado=vlGeralAcumulado;
	}
	public float getVlGeralAcumulado(){
		return this.vlGeralAcumulado;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdReferencia: " +  getCdReferencia();
		valueToString += ", dtMovimento: " +  sol.util.Util.formatDateTime(getDtMovimento(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", nrContadorInicial: " +  getNrContadorInicial();
		valueToString += ", nrContadorFinal: " +  getNrContadorFinal();
		valueToString += ", nrReducao: " +  getNrReducao();
		valueToString += ", nrContadorReinicio: " +  getNrContadorReinicio();
		valueToString += ", vlVendaBruta: " +  getVlVendaBruta();
		valueToString += ", vlGeralAcumulado: " +  getVlGeralAcumulado();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new MovimentoEcf(getCdReferencia(),
			getDtMovimento()==null ? null : (GregorianCalendar)getDtMovimento().clone(),
			getNrContadorInicial(),
			getNrContadorFinal(),
			getNrReducao(),
			getNrContadorReinicio(),
			getVlVendaBruta(),
			getVlGeralAcumulado());
	}

}