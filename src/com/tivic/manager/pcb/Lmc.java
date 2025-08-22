package com.tivic.manager.pcb;

import java.util.GregorianCalendar;

public class Lmc {

	private int cdEmpresa;
	private int cdProdutoServico;
	private GregorianCalendar dtLmc;
	private int nrPagina;

	public Lmc(int cdEmpresa,
			int cdProdutoServico,
			GregorianCalendar dtLmc,
			int nrPagina){
		setCdEmpresa(cdEmpresa);
		setCdProdutoServico(cdProdutoServico);
		setDtLmc(dtLmc);
		setNrPagina(nrPagina);
	}
	public void setCdEmpresa(int cdEmpresa){
		this.cdEmpresa=cdEmpresa;
	}
	public int getCdEmpresa(){
		return this.cdEmpresa;
	}
	public void setCdProdutoServico(int cdProdutoServico){
		this.cdProdutoServico=cdProdutoServico;
	}
	public int getCdProdutoServico(){
		return this.cdProdutoServico;
	}
	public void setDtLmc(GregorianCalendar dtLmc){
		this.dtLmc=dtLmc;
	}
	public GregorianCalendar getDtLmc(){
		return this.dtLmc;
	}
	public void setNrPagina(int nrPagina){
		this.nrPagina=nrPagina;
	}
	public int getNrPagina(){
		return this.nrPagina;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdEmpresa: " +  getCdEmpresa();
		valueToString += ", cdProdutoServico: " +  getCdProdutoServico();
		valueToString += ", dtLmc: " +  sol.util.Util.formatDateTime(getDtLmc(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", nrPagina: " +  getNrPagina();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new Lmc(getCdEmpresa(),
			getCdProdutoServico(),
			getDtLmc()==null ? null : (GregorianCalendar)getDtLmc().clone(),
			getNrPagina());
	}

}
