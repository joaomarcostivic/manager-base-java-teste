package com.tivic.manager.flp;

import java.util.GregorianCalendar;

public class MatriculaEventoFinanceiro {

	private int cdEventoFinanceiro;
	private int cdMatricula;
	private GregorianCalendar dtInicio;
	private int qtRepeticoes;
	private float qtEventoFinanceiro;
	private float vlEventoFinanceiro;
	private float qtHoras;

	public MatriculaEventoFinanceiro(int cdEventoFinanceiro,
			int cdMatricula,
			GregorianCalendar dtInicio,
			int qtRepeticoes,
			float qtEventoFinanceiro,
			float vlEventoFinanceiro,
			float qtHoras){
		setCdEventoFinanceiro(cdEventoFinanceiro);
		setCdMatricula(cdMatricula);
		setDtInicio(dtInicio);
		setQtRepeticoes(qtRepeticoes);
		setQtEventoFinanceiro(qtEventoFinanceiro);
		setVlEventoFinanceiro(vlEventoFinanceiro);
		setQtHoras(qtHoras);
	}
	public void setCdEventoFinanceiro(int cdEventoFinanceiro){
		this.cdEventoFinanceiro=cdEventoFinanceiro;
	}
	public int getCdEventoFinanceiro(){
		return this.cdEventoFinanceiro;
	}
	public void setCdMatricula(int cdMatricula){
		this.cdMatricula=cdMatricula;
	}
	public int getCdMatricula(){
		return this.cdMatricula;
	}
	public void setDtInicio(GregorianCalendar dtInicio){
		this.dtInicio=dtInicio;
	}
	public GregorianCalendar getDtInicio(){
		return this.dtInicio;
	}
	public void setQtRepeticoes(int qtRepeticoes){
		this.qtRepeticoes=qtRepeticoes;
	}
	public int getQtRepeticoes(){
		return this.qtRepeticoes;
	}
	public void setQtEventoFinanceiro(float qtEventoFinanceiro){
		this.qtEventoFinanceiro=qtEventoFinanceiro;
	}
	public float getQtEventoFinanceiro(){
		return this.qtEventoFinanceiro;
	}
	public void setVlEventoFinanceiro(float vlEventoFinanceiro){
		this.vlEventoFinanceiro=vlEventoFinanceiro;
	}
	public float getVlEventoFinanceiro(){
		return this.vlEventoFinanceiro;
	}
	public void setQtHoras(float qtHoras){
		this.qtHoras=qtHoras;
	}
	public float getQtHoras(){
		return this.qtHoras;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdEventoFinanceiro: " +  getCdEventoFinanceiro();
		valueToString += ", cdMatricula: " +  getCdMatricula();
		valueToString += ", dtInicio: " +  sol.util.Util.formatDateTime(getDtInicio(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", qtRepeticoes: " +  getQtRepeticoes();
		valueToString += ", qtEventoFinanceiro: " +  getQtEventoFinanceiro();
		valueToString += ", vlEventoFinanceiro: " +  getVlEventoFinanceiro();
		valueToString += ", qtHoras: " +  getQtHoras();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new MatriculaEventoFinanceiro(getCdEventoFinanceiro(),
			getCdMatricula(),
			getDtInicio()==null ? null : (GregorianCalendar)getDtInicio().clone(),
			getQtRepeticoes(),
			getQtEventoFinanceiro(),
			getVlEventoFinanceiro(),
			getQtHoras());
	}

}