package com.tivic.manager.adm;

import java.util.GregorianCalendar;

public class ContratoReferencia {

	private int cdContrato;
	private int cdReferencia;
	private GregorianCalendar dtContratacao;
	private float qtProdutoServico;
	private float vlProdutoServico;
	private int cdTipoProdutoServico;
	private int cdGarantia;

	public ContratoReferencia(int cdContrato,
			int cdReferencia,
			GregorianCalendar dtContratacao,
			float qtProdutoServico,
			float vlProdutoServico,
			int cdTipoProdutoServico,
			int cdGarantia){
		setCdContrato(cdContrato);
		setCdReferencia(cdReferencia);
		setDtContratacao(dtContratacao);
		setQtProdutoServico(qtProdutoServico);
		setVlProdutoServico(vlProdutoServico);
		setCdTipoProdutoServico(cdTipoProdutoServico);
		setCdGarantia(cdGarantia);
	}
	public void setCdContrato(int cdContrato){
		this.cdContrato=cdContrato;
	}
	public int getCdContrato(){
		return this.cdContrato;
	}
	public void setCdReferencia(int cdReferencia){
		this.cdReferencia=cdReferencia;
	}
	public int getCdReferencia(){
		return this.cdReferencia;
	}
	public void setDtContratacao(GregorianCalendar dtContratacao){
		this.dtContratacao=dtContratacao;
	}
	public GregorianCalendar getDtContratacao(){
		return this.dtContratacao;
	}
	public void setQtProdutoServico(float qtProdutoServico){
		this.qtProdutoServico=qtProdutoServico;
	}
	public float getQtProdutoServico(){
		return this.qtProdutoServico;
	}
	public void setVlProdutoServico(float vlProdutoServico){
		this.vlProdutoServico=vlProdutoServico;
	}
	public float getVlProdutoServico(){
		return this.vlProdutoServico;
	}
	public void setCdTipoProdutoServico(int cdTipoProdutoServico){
		this.cdTipoProdutoServico=cdTipoProdutoServico;
	}
	public int getCdTipoProdutoServico(){
		return this.cdTipoProdutoServico;
	}
	public void setCdGarantia(int cdGarantia){
		this.cdGarantia=cdGarantia;
	}
	public int getCdGarantia(){
		return this.cdGarantia;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdContrato: " +  getCdContrato();
		valueToString += ", cdReferencia: " +  getCdReferencia();
		valueToString += ", dtContratacao: " +  sol.util.Util.formatDateTime(getDtContratacao(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", qtProdutoServico: " +  getQtProdutoServico();
		valueToString += ", vlProdutoServico: " +  getVlProdutoServico();
		valueToString += ", cdTipoProdutoServico: " +  getCdTipoProdutoServico();
		valueToString += ", cdGarantia: " +  getCdGarantia();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new ContratoReferencia(getCdContrato(),
			getCdReferencia(),
			getDtContratacao()==null ? null : (GregorianCalendar)getDtContratacao().clone(),
			getQtProdutoServico(),
			getVlProdutoServico(),
			getCdTipoProdutoServico(),
			getCdGarantia());
	}

}
