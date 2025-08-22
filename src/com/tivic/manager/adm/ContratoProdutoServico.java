package com.tivic.manager.adm;

import java.util.GregorianCalendar;

public class ContratoProdutoServico {

	private int cdContrato;
	private int cdProdutoServico;
	private float qtProdutoServico;
	private float vlProdutoServico;
	private GregorianCalendar dtContratacao;
	private int cdTipoProdutoServico;
	private int cdGarantia;
	private int cdReferencia;
	private int cdEmpresa;

	public ContratoProdutoServico(int cdContrato,
			int cdProdutoServico,
			float qtProdutoServico,
			float vlProdutoServico,
			GregorianCalendar dtContratacao,
			int cdTipoProdutoServico,
			int cdGarantia,
			int cdReferencia,
			int cdEmpresa){
		setCdContrato(cdContrato);
		setCdProdutoServico(cdProdutoServico);
		setQtProdutoServico(qtProdutoServico);
		setVlProdutoServico(vlProdutoServico);
		setDtContratacao(dtContratacao);
		setCdTipoProdutoServico(cdTipoProdutoServico);
		setCdGarantia(cdGarantia);
		setCdReferencia(cdReferencia);
		setCdEmpresa(cdEmpresa);
	}
	public void setCdContrato(int cdContrato){
		this.cdContrato=cdContrato;
	}
	public int getCdContrato(){
		return this.cdContrato;
	}
	public void setCdProdutoServico(int cdProdutoServico){
		this.cdProdutoServico=cdProdutoServico;
	}
	public int getCdProdutoServico(){
		return this.cdProdutoServico;
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
	public void setDtContratacao(GregorianCalendar dtContratacao){
		this.dtContratacao=dtContratacao;
	}
	public GregorianCalendar getDtContratacao(){
		return this.dtContratacao;
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
	public void setCdReferencia(int cdReferencia){
		this.cdReferencia=cdReferencia;
	}
	public int getCdReferencia(){
		return this.cdReferencia;
	}
	public void setCdEmpresa(int cdEmpresa){
		this.cdEmpresa=cdEmpresa;
	}
	public int getCdEmpresa(){
		return this.cdEmpresa;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdContrato: " +  getCdContrato();
		valueToString += ", cdProdutoServico: " +  getCdProdutoServico();
		valueToString += ", qtProdutoServico: " +  getQtProdutoServico();
		valueToString += ", vlProdutoServico: " +  getVlProdutoServico();
		valueToString += ", dtContratacao: " +  sol.util.Util.formatDateTime(getDtContratacao(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", cdTipoProdutoServico: " +  getCdTipoProdutoServico();
		valueToString += ", cdGarantia: " +  getCdGarantia();
		valueToString += ", cdReferencia: " +  getCdReferencia();
		valueToString += ", cdEmpresa: " +  getCdEmpresa();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new ContratoProdutoServico(getCdContrato(),
			getCdProdutoServico(),
			getQtProdutoServico(),
			getVlProdutoServico(),
			getDtContratacao()==null ? null : (GregorianCalendar)getDtContratacao().clone(),
			getCdTipoProdutoServico(),
			getCdGarantia(),
			getCdReferencia(),
			getCdEmpresa());
	}

}
