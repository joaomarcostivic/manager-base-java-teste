package com.tivic.manager.ptc;

import java.util.GregorianCalendar;

public class DocumentoConta {
	
	public static final int ST_INATIVO = 0;
	public static final int ST_ATIVO  = 1;

	private int cdDocumento;
	private int cdDocumentoConta;
	private int cdContaReceber;
	private int cdContaPagar;
	private String nrDocumento;
	private String codMovimento;
	private double vlPagamento;
	private GregorianCalendar dtPagamento;
	private int stDocumentoConta;

	public DocumentoConta(){ }

	public DocumentoConta(int cdDocumento,
			int cdDocumentoConta,
			int cdContaReceber,
			int cdContaPagar,
			String nrDocumento,
			String codMovimento,
			double vlPagamento,
			GregorianCalendar dtPagamento,
			int stDocumentoConta){
		setCdDocumento(cdDocumento);
		setCdDocumentoConta(cdDocumentoConta);
		setCdContaReceber(cdContaReceber);
		setCdContaPagar(cdContaPagar);
		setNrDocumento(nrDocumento);
		setCodMovimento(codMovimento);
		setVlPagamento(vlPagamento);
		setDtPagamento(dtPagamento);
		setStDocumentoConta(stDocumentoConta);
	}
	public void setCdDocumento(int cdDocumento){
		this.cdDocumento=cdDocumento;
	}
	public int getCdDocumento(){
		return this.cdDocumento;
	}
	public void setCdDocumentoConta(int cdDocumentoConta){
		this.cdDocumentoConta=cdDocumentoConta;
	}
	public int getCdDocumentoConta(){
		return this.cdDocumentoConta;
	}
	public void setCdContaReceber(int cdContaReceber){
		this.cdContaReceber=cdContaReceber;
	}
	public int getCdContaReceber(){
		return this.cdContaReceber;
	}
	public void setCdContaPagar(int cdContaPagar){
		this.cdContaPagar=cdContaPagar;
	}
	public int getCdContaPagar(){
		return this.cdContaPagar;
	}
	public void setNrDocumento(String nrDocumento){
		this.nrDocumento=nrDocumento;
	}
	public String getNrDocumento(){
		return this.nrDocumento;
	}
	public void setCodMovimento(String codMovimento){
		this.codMovimento=codMovimento;
	}
	public String getCodMovimento(){
		return this.codMovimento;
	}
	public void setVlPagamento(double vlPagamento){
		this.vlPagamento=vlPagamento;
	}
	public double getVlPagamento() {
		return vlPagamento;
	}
	public void setDtPagamento(GregorianCalendar dtPagamento){
		this.dtPagamento=dtPagamento;
	}
	public GregorianCalendar getDtPagamento(){
		return this.dtPagamento;
	}
	public void setStDocumentoConta(int stDocumentoConta){
		this.stDocumentoConta=stDocumentoConta;
	}
	public int getStDocumentoConta(){
		return this.stDocumentoConta;
	}
	
	public String toString() {
		String valueToString = "";
		valueToString += "cdDocumento: " +  getCdDocumento();
		valueToString += ", cdDocumentoConta: " +  getCdDocumentoConta();
		valueToString += ", cdContaReceber: " +  getCdContaReceber();
		valueToString += ", cdContaPagar: " +  getCdContaPagar();
		valueToString += ", nrDocumento: " +  getNrDocumento();
		valueToString += ", codMovimento: " +  getCodMovimento();
		valueToString += ", vlPagamento: " +  getVlPagamento();
		valueToString += ", dtPagamento: " +  getDtPagamento();
		valueToString += ", stDocumentoConta: " +  getStDocumentoConta();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new DocumentoConta(getCdDocumento(),
			getCdDocumentoConta(),
			getCdContaReceber(),
			getCdContaPagar(),
			getNrDocumento(),
			getCodMovimento(),
			getVlPagamento(),
			getDtPagamento(),
			getStDocumentoConta());
	}
}