package com.tivic.manager.adm;

import java.util.GregorianCalendar;

public class Garantia {

	private int cdGarantia;
	private int cdSeguradora;
	private int cdAdministradora;
	private GregorianCalendar dtGarantia;
	private int tpGarantia;
	private int nrTempoGarantia;
	private int cdDocSaidaReferencia;

	public Garantia(int cdGarantia,
			int cdSeguradora,
			int cdAdministradora,
			GregorianCalendar dtGarantia,
			int tpGarantia,
			int nrTempoGarantia,
			int cdDocSaidaReferencia){
		setCdGarantia(cdGarantia);
		setCdSeguradora(cdSeguradora);
		setCdAdministradora(cdAdministradora);
		setDtGarantia(dtGarantia);
		setTpGarantia(tpGarantia);
		setNrTempoGarantia(nrTempoGarantia);
		setCdDocSaidaReferencia(cdDocSaidaReferencia);
	}
	public void setCdGarantia(int cdGarantia){
		this.cdGarantia=cdGarantia;
	}
	public int getCdGarantia(){
		return this.cdGarantia;
	}
	public void setCdSeguradora(int cdSeguradora){
		this.cdSeguradora=cdSeguradora;
	}
	public int getCdSeguradora(){
		return this.cdSeguradora;
	}
	public void setCdAdministradora(int cdAdministradora){
		this.cdAdministradora=cdAdministradora;
	}
	public int getCdAdministradora(){
		return this.cdAdministradora;
	}
	public void setDtGarantia(GregorianCalendar dtGarantia){
		this.dtGarantia=dtGarantia;
	}
	public GregorianCalendar getDtGarantia(){
		return this.dtGarantia;
	}
	public void setTpGarantia(int tpGarantia){
		this.tpGarantia=tpGarantia;
	}
	public int getTpGarantia(){
		return this.tpGarantia;
	}
	public void setNrTempoGarantia(int nrTempoGarantia){
		this.nrTempoGarantia=nrTempoGarantia;
	}
	public int getNrTempoGarantia(){
		return this.nrTempoGarantia;
	}
	public void setCdDocSaidaReferencia(int cdDocSaidaReferencia){
		this.cdDocSaidaReferencia=cdDocSaidaReferencia;
	}
	public int getCdDocSaidaReferencia(){
		return this.cdDocSaidaReferencia;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdGarantia: " +  getCdGarantia();
		valueToString += ", cdSeguradora: " +  getCdSeguradora();
		valueToString += ", cdAdministradora: " +  getCdAdministradora();
		valueToString += ", dtGarantia: " +  sol.util.Util.formatDateTime(getDtGarantia(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", tpGarantia: " +  getTpGarantia();
		valueToString += ", nrTempoGarantia: " +  getNrTempoGarantia();
		valueToString += ", cdDocSaidaReferencia: " +  getCdDocSaidaReferencia();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new Garantia(getCdGarantia(),
			getCdSeguradora(),
			getCdAdministradora(),
			getDtGarantia()==null ? null : (GregorianCalendar)getDtGarantia().clone(),
			getTpGarantia(),
			getNrTempoGarantia(),
			getCdDocSaidaReferencia());
	}

}
