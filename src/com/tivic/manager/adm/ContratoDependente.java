package com.tivic.manager.adm;

import java.util.GregorianCalendar;

public class ContratoDependente {

	private int cdContrato;
	private int cdDependente;
	private GregorianCalendar dtVinculacao;
	private GregorianCalendar dtDesvinculacao;
	private float prPagamento;
	private GregorianCalendar dtFinalVigencia;
	private int cdDependencia;
	private int cdAgente;
	private String txtObservacao;
	private String nrDependente;
	private int tpParentesco;
	private int stDependente;

	public ContratoDependente(int cdContrato,
			int cdDependente,
			GregorianCalendar dtVinculacao,
			GregorianCalendar dtDesvinculacao,
			float prPagamento,
			GregorianCalendar dtFinalVigencia,
			int cdDependencia,
			int cdAgente,
			String txtObservacao,
			String nrDependente,
			int tpParentesco,
			int stDependente){
		setCdContrato(cdContrato);
		setCdDependente(cdDependente);
		setDtVinculacao(dtVinculacao);
		setDtDesvinculacao(dtDesvinculacao);
		setPrPagamento(prPagamento);
		setDtFinalVigencia(dtFinalVigencia);
		setCdDependencia(cdDependencia);
		setCdAgente(cdAgente);
		setTxtObservacao(txtObservacao);
		setNrDependente(nrDependente);
		setTpParentesco(tpParentesco);
		setStDependente(stDependente);
	}
	public void setCdContrato(int cdContrato){
		this.cdContrato=cdContrato;
	}
	public int getCdContrato(){
		return this.cdContrato;
	}
	public void setCdDependente(int cdDependente){
		this.cdDependente=cdDependente;
	}
	public int getCdDependente(){
		return this.cdDependente;
	}
	public void setDtVinculacao(GregorianCalendar dtVinculacao){
		this.dtVinculacao=dtVinculacao;
	}
	public GregorianCalendar getDtVinculacao(){
		return this.dtVinculacao;
	}
	public void setDtDesvinculacao(GregorianCalendar dtDesvinculacao){
		this.dtDesvinculacao=dtDesvinculacao;
	}
	public GregorianCalendar getDtDesvinculacao(){
		return this.dtDesvinculacao;
	}
	public void setPrPagamento(float prPagamento){
		this.prPagamento=prPagamento;
	}
	public float getPrPagamento(){
		return this.prPagamento;
	}
	public void setDtFinalVigencia(GregorianCalendar dtFinalVigencia){
		this.dtFinalVigencia=dtFinalVigencia;
	}
	public GregorianCalendar getDtFinalVigencia(){
		return this.dtFinalVigencia;
	}
	public void setCdDependencia(int cdDependencia){
		this.cdDependencia=cdDependencia;
	}
	public int getCdDependencia(){
		return this.cdDependencia;
	}
	public void setCdAgente(int cdAgente){
		this.cdAgente=cdAgente;
	}
	public int getCdAgente(){
		return this.cdAgente;
	}
	public void setTxtObservacao(String txtObservacao){
		this.txtObservacao=txtObservacao;
	}
	public String getTxtObservacao(){
		return this.txtObservacao;
	}
	public void setNrDependente(String nrDependente){
		this.nrDependente=nrDependente;
	}
	public String getNrDependente(){
		return this.nrDependente;
	}
	public void setTpParentesco(int tpParentesco){
		this.tpParentesco=tpParentesco;
	}
	public int getTpParentesco(){
		return this.tpParentesco;
	}
	public void setStDependente(int stDependente){
		this.stDependente=stDependente;
	}
	public int getStDependente(){
		return this.stDependente;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdContrato: " +  getCdContrato();
		valueToString += ", cdDependente: " +  getCdDependente();
		valueToString += ", dtVinculacao: " +  sol.util.Util.formatDateTime(getDtVinculacao(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", dtDesvinculacao: " +  sol.util.Util.formatDateTime(getDtDesvinculacao(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", prPagamento: " +  getPrPagamento();
		valueToString += ", dtFinalVigencia: " +  sol.util.Util.formatDateTime(getDtFinalVigencia(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", cdDependencia: " +  getCdDependencia();
		valueToString += ", cdAgente: " +  getCdAgente();
		valueToString += ", txtObservacao: " +  getTxtObservacao();
		valueToString += ", nrDependente: " +  getNrDependente();
		valueToString += ", tpParentesco: " +  getTpParentesco();
		valueToString += ", stDependente: " +  getStDependente();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new ContratoDependente(getCdContrato(),
			getCdDependente(),
			getDtVinculacao()==null ? null : (GregorianCalendar)getDtVinculacao().clone(),
			getDtDesvinculacao()==null ? null : (GregorianCalendar)getDtDesvinculacao().clone(),
			getPrPagamento(),
			getDtFinalVigencia()==null ? null : (GregorianCalendar)getDtFinalVigencia().clone(),
			getCdDependencia(),
			getCdAgente(),
			getTxtObservacao(),
			getNrDependente(),
			getTpParentesco(),
			getStDependente());
	}

}
