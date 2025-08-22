package com.tivic.manager.adm;

import java.util.GregorianCalendar;

public class Cheque {

	private int cdCheque;
	private int cdConta;
	private String nrCheque;
	private GregorianCalendar dtEmissao;
	private GregorianCalendar dtLiberacao;
	private GregorianCalendar dtImpressao;
	private String idTalao;
	private int stCheque;
	private String dsObservacao;

	public Cheque(){ }

	public Cheque(int cdCheque,
			int cdConta,
			String nrCheque,
			GregorianCalendar dtEmissao,
			GregorianCalendar dtLiberacao,
			GregorianCalendar dtImpressao,
			String idTalao,
			int stCheque,
			String dsObservacao){
		setCdCheque(cdCheque);
		setCdConta(cdConta);
		setNrCheque(nrCheque);
		setDtEmissao(dtEmissao);
		setDtLiberacao(dtLiberacao);
		setDtImpressao(dtImpressao);
		setIdTalao(idTalao);
		setStCheque(stCheque);
		setDsObservacao(dsObservacao);
	}
	public void setCdCheque(int cdCheque){
		this.cdCheque=cdCheque;
	}
	public int getCdCheque(){
		return this.cdCheque;
	}
	public void setCdConta(int cdConta){
		this.cdConta=cdConta;
	}
	public int getCdConta(){
		return this.cdConta;
	}
	public void setNrCheque(String nrCheque){
		this.nrCheque=nrCheque;
	}
	public String getNrCheque(){
		return this.nrCheque;
	}
	public void setDtEmissao(GregorianCalendar dtEmissao){
		this.dtEmissao=dtEmissao;
	}
	public GregorianCalendar getDtEmissao(){
		return this.dtEmissao;
	}
	public void setDtLiberacao(GregorianCalendar dtLiberacao){
		this.dtLiberacao=dtLiberacao;
	}
	public GregorianCalendar getDtLiberacao(){
		return this.dtLiberacao;
	}
	public void setDtImpressao(GregorianCalendar dtImpressao){
		this.dtImpressao=dtImpressao;
	}
	public GregorianCalendar getDtImpressao(){
		return this.dtImpressao;
	}
	public void setIdTalao(String idTalao){
		this.idTalao=idTalao;
	}
	public String getIdTalao(){
		return this.idTalao;
	}
	public void setStCheque(int stCheque){
		this.stCheque=stCheque;
	}
	public int getStCheque(){
		return this.stCheque;
	}
	public void setDsObservacao(String dsObservacao){
		this.dsObservacao=dsObservacao;
	}
	public String getDsObservacao(){
		return this.dsObservacao;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdCheque: " +  getCdCheque();
		valueToString += ", cdConta: " +  getCdConta();
		valueToString += ", nrCheque: " +  getNrCheque();
		valueToString += ", dtEmissao: " +  sol.util.Util.formatDateTime(getDtEmissao(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", dtLiberacao: " +  sol.util.Util.formatDateTime(getDtLiberacao(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", dtImpressao: " +  sol.util.Util.formatDateTime(getDtImpressao(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", idTalao: " +  getIdTalao();
		valueToString += ", stCheque: " +  getStCheque();
		valueToString += ", dsObservacao: " +  getDsObservacao();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new Cheque(getCdCheque(),
			getCdConta(),
			getNrCheque(),
			getDtEmissao()==null ? null : (GregorianCalendar)getDtEmissao().clone(),
			getDtLiberacao()==null ? null : (GregorianCalendar)getDtLiberacao().clone(),
			getDtImpressao()==null ? null : (GregorianCalendar)getDtImpressao().clone(),
			getIdTalao(),
			getStCheque(),
			getDsObservacao());
	}

}