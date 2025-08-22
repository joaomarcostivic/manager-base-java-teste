package com.tivic.manager.bpm;

import java.util.GregorianCalendar;

public class ReferenciaMovimentacao {

	private int cdReferencia;
	private int cdMovimentacao;
	private int cdSetor;
	private GregorianCalendar dtMovimentacao;

	public ReferenciaMovimentacao(int cdReferencia,
			int cdMovimentacao,
			int cdSetor,
			GregorianCalendar dtMovimentacao){
		setCdReferencia(cdReferencia);
		setCdMovimentacao(cdMovimentacao);
		setCdSetor(cdSetor);
		setDtMovimentacao(dtMovimentacao);
	}
	public void setCdReferencia(int cdReferencia){
		this.cdReferencia=cdReferencia;
	}
	public int getCdReferencia(){
		return this.cdReferencia;
	}
	public void setCdMovimentacao(int cdMovimentacao){
		this.cdMovimentacao=cdMovimentacao;
	}
	public int getCdMovimentacao(){
		return this.cdMovimentacao;
	}
	public void setCdSetor(int cdSetor){
		this.cdSetor=cdSetor;
	}
	public int getCdSetor(){
		return this.cdSetor;
	}
	public void setDtMovimentacao(GregorianCalendar dtMovimentacao){
		this.dtMovimentacao=dtMovimentacao;
	}
	public GregorianCalendar getDtMovimentacao(){
		return this.dtMovimentacao;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdReferencia: " +  getCdReferencia();
		valueToString += ", cdMovimentacao: " +  getCdMovimentacao();
		valueToString += ", cdSetor: " +  getCdSetor();
		valueToString += ", dtMovimentacao: " +  sol.util.Util.formatDateTime(getDtMovimentacao(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new ReferenciaMovimentacao(getCdReferencia(),
			getCdMovimentacao(),
			getCdSetor(),
			getDtMovimentacao()==null ? null : (GregorianCalendar)getDtMovimentacao().clone());
	}

}
