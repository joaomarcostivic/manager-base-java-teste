package com.tivic.manager.adm;

import java.util.GregorianCalendar;

public class ContratoEvento {

	private int cdEventoFinanceiro;
	private int cdContrato;
	private int tpRepeticao;
	private float vlEventoFinanceiro;
	private int qtVezes;
	private int tpCalculo;
	private GregorianCalendar dtInicioLancamento;

	public ContratoEvento(int cdEventoFinanceiro,
			int cdContrato,
			int tpRepeticao,
			float vlEventoFinanceiro,
			int qtVezes,
			int tpCalculo,
			GregorianCalendar dtInicioLancamento){
		setCdEventoFinanceiro(cdEventoFinanceiro);
		setCdContrato(cdContrato);
		setTpRepeticao(tpRepeticao);
		setVlEventoFinanceiro(vlEventoFinanceiro);
		setQtVezes(qtVezes);
		setTpCalculo(tpCalculo);
		setDtInicioLancamento(dtInicioLancamento);
	}
	public void setCdEventoFinanceiro(int cdEventoFinanceiro){
		this.cdEventoFinanceiro=cdEventoFinanceiro;
	}
	public int getCdEventoFinanceiro(){
		return this.cdEventoFinanceiro;
	}
	public void setCdContrato(int cdContrato){
		this.cdContrato=cdContrato;
	}
	public int getCdContrato(){
		return this.cdContrato;
	}
	public void setTpRepeticao(int tpRepeticao){
		this.tpRepeticao=tpRepeticao;
	}
	public int getTpRepeticao(){
		return this.tpRepeticao;
	}
	public void setVlEventoFinanceiro(float vlEventoFinanceiro){
		this.vlEventoFinanceiro=vlEventoFinanceiro;
	}
	public float getVlEventoFinanceiro(){
		return this.vlEventoFinanceiro;
	}
	public void setQtVezes(int qtVezes){
		this.qtVezes=qtVezes;
	}
	public int getQtVezes(){
		return this.qtVezes;
	}
	public void setTpCalculo(int tpCalculo){
		this.tpCalculo=tpCalculo;
	}
	public int getTpCalculo(){
		return this.tpCalculo;
	}
	public void setDtInicioLancamento(GregorianCalendar dtInicioLancamento){
		this.dtInicioLancamento=dtInicioLancamento;
	}
	public GregorianCalendar getDtInicioLancamento(){
		return this.dtInicioLancamento;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdEventoFinanceiro: " +  getCdEventoFinanceiro();
		valueToString += ", cdContrato: " +  getCdContrato();
		valueToString += ", tpRepeticao: " +  getTpRepeticao();
		valueToString += ", vlEventoFinanceiro: " +  getVlEventoFinanceiro();
		valueToString += ", qtVezes: " +  getQtVezes();
		valueToString += ", tpCalculo: " +  getTpCalculo();
		valueToString += ", dtInicioLancamento: " +  sol.util.Util.formatDateTime(getDtInicioLancamento(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new ContratoEvento(getCdEventoFinanceiro(),
			getCdContrato(),
			getTpRepeticao(),
			getVlEventoFinanceiro(),
			getQtVezes(),
			getTpCalculo(),
			getDtInicioLancamento()==null ? null : (GregorianCalendar)getDtInicioLancamento().clone());
	}

}
