package com.tivic.manager.str;

import java.util.GregorianCalendar;

public class Talonario {

	private int cdTalao;
	private int cdAgente;
	private int nrInicial;
	private int nrFinal;
	private GregorianCalendar dtEntrega;
	private GregorianCalendar dtDevolucao;
	private int stTalao;
	private int nrTalao;
	private int tpTalao;
	private String sgTalao;
	private int nrUltimoAit;

	public Talonario() { }

	public Talonario(int cdTalao,
			int cdAgente,
			int nrInicial,
			int nrFinal,
			GregorianCalendar dtEntrega,
			GregorianCalendar dtDevolucao,
			int stTalao,
			int nrTalao,
			int tpTalao,
			String sgTalao,
			int nrUltimoAit) {
		setCdTalao(cdTalao);
		setCdAgente(cdAgente);
		setNrInicial(nrInicial);
		setNrFinal(nrFinal);
		setDtEntrega(dtEntrega);
		setDtDevolucao(dtDevolucao);
		setStTalao(stTalao);
		setNrTalao(nrTalao);
		setTpTalao(tpTalao);
		setSgTalao(sgTalao);
		setNrUltimoAit(nrUltimoAit);
	}
	public void setCdTalao(int cdTalao){
		this.cdTalao=cdTalao;
	}
	public int getCdTalao(){
		return this.cdTalao;
	}
	public void setCdAgente(int cdAgente){
		this.cdAgente=cdAgente;
	}
	public int getCdAgente(){
		return this.cdAgente;
	}
	public void setNrInicial(int nrInicial){
		this.nrInicial=nrInicial;
	}
	public int getNrInicial(){
		return this.nrInicial;
	}
	public void setNrFinal(int nrFinal){
		this.nrFinal=nrFinal;
	}
	public int getNrFinal(){
		return this.nrFinal;
	}
	public void setDtEntrega(GregorianCalendar dtEntrega){
		this.dtEntrega=dtEntrega;
	}
	public GregorianCalendar getDtEntrega(){
		return this.dtEntrega;
	}
	public void setDtDevolucao(GregorianCalendar dtDevolucao){
		this.dtDevolucao=dtDevolucao;
	}
	public GregorianCalendar getDtDevolucao(){
		return this.dtDevolucao;
	}
	public void setStTalao(int stTalao){
		this.stTalao=stTalao;
	}
	public int getStTalao(){
		return this.stTalao;
	}
	public void setNrTalao(int nrTalao){
		this.nrTalao=nrTalao;
	}
	public int getNrTalao(){
		return this.nrTalao;
	}
	public void setTpTalao(int tpTalao){
		this.tpTalao=tpTalao;
	}
	public int getTpTalao(){
		return this.tpTalao;
	}
	public void setSgTalao(String sgTalao){
		this.sgTalao=sgTalao;
	}
	public String getSgTalao(){
		return this.sgTalao;
	}
	public int getNrUltimoAit() {
		return nrUltimoAit;
	}
	public void setNrUltimoAit(int nrUltimoAit) {
		this.nrUltimoAit = nrUltimoAit;
	}

	public String toString() {
		String valueToString = "";
		valueToString += "cdTalao: " +  getCdTalao();
		valueToString += ", cdAgente: " +  getCdAgente();
		valueToString += ", nrInicial: " +  getNrInicial();
		valueToString += ", nrFinal: " +  getNrFinal();
		valueToString += ", dtEntrega: " +  sol.util.Util.formatDateTime(getDtEntrega(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", dtDevolucao: " +  sol.util.Util.formatDateTime(getDtDevolucao(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", stTalao: " +  getStTalao();
		valueToString += ", nrTalao: " +  getNrTalao();
		valueToString += ", tpTalao: " +  getTpTalao();
		valueToString += ", sgTalao: " +  getSgTalao();
		valueToString += ", nrUltimoAit: " +  getNrUltimoAit();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new Talonario(getCdTalao(),
			getCdAgente(),
			getNrInicial(),
			getNrFinal(),
			getDtEntrega()==null ? null : (GregorianCalendar)getDtEntrega().clone(),
			getDtDevolucao()==null ? null : (GregorianCalendar)getDtDevolucao().clone(),
			getStTalao(),
			getNrTalao(),
			getTpTalao(),
			getSgTalao(),
			getNrUltimoAit());
	}

}
