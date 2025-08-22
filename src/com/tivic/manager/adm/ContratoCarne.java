package com.tivic.manager.adm;

import java.util.GregorianCalendar;

public class ContratoCarne {

	private int cdContrato;
	private int cdCarne;
	private int nrVia;
	private GregorianCalendar dtLancamento;

	public ContratoCarne(int cdContrato,
			int cdCarne,
			int nrVia,
			GregorianCalendar dtLancamento){
		setCdContrato(cdContrato);
		setCdCarne(cdCarne);
		setNrVia(nrVia);
		setDtLancamento(dtLancamento);
	}
	public void setCdContrato(int cdContrato){
		this.cdContrato=cdContrato;
	}
	public int getCdContrato(){
		return this.cdContrato;
	}
	public void setCdCarne(int cdCarne){
		this.cdCarne=cdCarne;
	}
	public int getCdCarne(){
		return this.cdCarne;
	}
	public void setNrVia(int nrVia){
		this.nrVia=nrVia;
	}
	public int getNrVia(){
		return this.nrVia;
	}
	public void setDtLancamento(GregorianCalendar dtLancamento){
		this.dtLancamento=dtLancamento;
	}
	public GregorianCalendar getDtLancamento(){
		return this.dtLancamento;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdContrato: " +  getCdContrato();
		valueToString += ", cdCarne: " +  getCdCarne();
		valueToString += ", nrVia: " +  getNrVia();
		valueToString += ", dtLancamento: " +  sol.util.Util.formatDateTime(getDtLancamento(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new ContratoCarne(getCdContrato(),
			getCdCarne(),
			getNrVia(),
			getDtLancamento()==null ? null : (GregorianCalendar)getDtLancamento().clone());
	}

}
