package com.tivic.manager.adm;

import java.util.GregorianCalendar;

public class ContratoProdutoTerceiro {

	private int cdContrato;
	private int cdProdutoTerceiro;
	private GregorianCalendar dtContratacao;
	private float qtProdutoServico;
	private float vlProdutoServico;

	public ContratoProdutoTerceiro(int cdContrato,
			int cdProdutoTerceiro,
			GregorianCalendar dtContratacao,
			float qtProdutoServico,
			float vlProdutoServico){
		setCdContrato(cdContrato);
		setCdProdutoTerceiro(cdProdutoTerceiro);
		setDtContratacao(dtContratacao);
		setQtProdutoServico(qtProdutoServico);
		setVlProdutoServico(vlProdutoServico);
	}
	public void setCdContrato(int cdContrato){
		this.cdContrato=cdContrato;
	}
	public int getCdContrato(){
		return this.cdContrato;
	}
	public void setCdProdutoTerceiro(int cdProdutoTerceiro){
		this.cdProdutoTerceiro=cdProdutoTerceiro;
	}
	public int getCdProdutoTerceiro(){
		return this.cdProdutoTerceiro;
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
	public String toString() {
		String valueToString = "";
		valueToString += "cdContrato: " +  getCdContrato();
		valueToString += ", cdProdutoTerceiro: " +  getCdProdutoTerceiro();
		valueToString += ", dtContratacao: " +  sol.util.Util.formatDateTime(getDtContratacao(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", qtProdutoServico: " +  getQtProdutoServico();
		valueToString += ", vlProdutoServico: " +  getVlProdutoServico();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new ContratoProdutoTerceiro(getCdContrato(),
			getCdProdutoTerceiro(),
			getDtContratacao()==null ? null : (GregorianCalendar)getDtContratacao().clone(),
			getQtProdutoServico(),
			getVlProdutoServico());
	}

}
