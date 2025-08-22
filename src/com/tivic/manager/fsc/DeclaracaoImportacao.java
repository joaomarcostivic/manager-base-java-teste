package com.tivic.manager.fsc;

import java.util.GregorianCalendar;

public class DeclaracaoImportacao {

	private int cdDeclaracaoImportacao;
	private int cdExportador;
	private int cdEstado;
	private int cdItem;
	private int cdNotaFiscal;
	private String nrDeclaracaoImportacao;
	private GregorianCalendar dtRegistro;
	private String dsLocalDesembaraco;
	private GregorianCalendar dtDesembaraco;
	private int tpViaTransporte;
	private int tpIntermedio;
	private String nrCnpjIntermediario;
	private int cdEstadoIntermediario;

	public DeclaracaoImportacao(){ }

	public DeclaracaoImportacao(int cdDeclaracaoImportacao,
			int cdExportador,
			int cdEstado,
			int cdItem,
			int cdNotaFiscal,
			String nrDeclaracaoImportacao,
			GregorianCalendar dtRegistro,
			String dsLocalDesembaraco,
			GregorianCalendar dtDesembaraco,
			int tpViaTransporte,
			int tpIntermedio,
			String nrCnpjIntermediario,
			int cdEstadoIntermediario){
		setCdDeclaracaoImportacao(cdDeclaracaoImportacao);
		setCdExportador(cdExportador);
		setCdEstado(cdEstado);
		setCdItem(cdItem);
		setCdNotaFiscal(cdNotaFiscal);
		setNrDeclaracaoImportacao(nrDeclaracaoImportacao);
		setDtRegistro(dtRegistro);
		setDsLocalDesembaraco(dsLocalDesembaraco);
		setDtDesembaraco(dtDesembaraco);
		setTpViaTransporte(tpViaTransporte);
		setTpIntermedio(tpIntermedio);
		setNrCnpjIntermediario(nrCnpjIntermediario);
		setCdEstadoIntermediario(cdEstadoIntermediario);
	}
	public void setCdDeclaracaoImportacao(int cdDeclaracaoImportacao){
		this.cdDeclaracaoImportacao=cdDeclaracaoImportacao;
	}
	public int getCdDeclaracaoImportacao(){
		return this.cdDeclaracaoImportacao;
	}
	public void setCdExportador(int cdExportador){
		this.cdExportador=cdExportador;
	}
	public int getCdExportador(){
		return this.cdExportador;
	}
	public void setCdEstado(int cdEstado){
		this.cdEstado=cdEstado;
	}
	public int getCdEstado(){
		return this.cdEstado;
	}
	public void setCdItem(int cdItem){
		this.cdItem=cdItem;
	}
	public int getCdItem(){
		return this.cdItem;
	}
	public void setCdNotaFiscal(int cdNotaFiscal){
		this.cdNotaFiscal=cdNotaFiscal;
	}
	public int getCdNotaFiscal(){
		return this.cdNotaFiscal;
	}
	public void setNrDeclaracaoImportacao(String nrDeclaracaoImportacao){
		this.nrDeclaracaoImportacao=nrDeclaracaoImportacao;
	}
	public String getNrDeclaracaoImportacao(){
		return this.nrDeclaracaoImportacao;
	}
	public void setDtRegistro(GregorianCalendar dtRegistro){
		this.dtRegistro=dtRegistro;
	}
	public GregorianCalendar getDtRegistro(){
		return this.dtRegistro;
	}
	public void setDsLocalDesembaraco(String dsLocalDesembaraco){
		this.dsLocalDesembaraco=dsLocalDesembaraco;
	}
	public String getDsLocalDesembaraco(){
		return this.dsLocalDesembaraco;
	}
	public void setDtDesembaraco(GregorianCalendar dtDesembaraco){
		this.dtDesembaraco=dtDesembaraco;
	}
	public GregorianCalendar getDtDesembaraco(){
		return this.dtDesembaraco;
	}
	public void setTpViaTransporte(int tpViaTransporte){
		this.tpViaTransporte=tpViaTransporte;
	}
	public int getTpViaTransporte(){
		return this.tpViaTransporte;
	}
	public void setTpIntermedio(int tpIntermedio){
		this.tpIntermedio=tpIntermedio;
	}
	public int getTpIntermedio(){
		return this.tpIntermedio;
	}
	public void setNrCnpjIntermediario(String nrCnpjIntermediario){
		this.nrCnpjIntermediario=nrCnpjIntermediario;
	}
	public String getNrCnpjIntermediario(){
		return this.nrCnpjIntermediario;
	}
	public void setCdEstadoIntermediario(int cdEstadoIntermediario){
		this.cdEstadoIntermediario=cdEstadoIntermediario;
	}
	public int getCdEstadoIntermediario(){
		return this.cdEstadoIntermediario;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdDeclaracaoImportacao: " +  getCdDeclaracaoImportacao();
		valueToString += ", cdExportador: " +  getCdExportador();
		valueToString += ", cdEstado: " +  getCdEstado();
		valueToString += ", cdItem: " +  getCdItem();
		valueToString += ", cdNotaFiscal: " +  getCdNotaFiscal();
		valueToString += ", nrDeclaracaoImportacao: " +  getNrDeclaracaoImportacao();
		valueToString += ", dtRegistro: " +  sol.util.Util.formatDateTime(getDtRegistro(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", dsLocalDesembaraco: " +  getDsLocalDesembaraco();
		valueToString += ", dtDesembaraco: " +  sol.util.Util.formatDateTime(getDtDesembaraco(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", tpViaTransporte: " +  getTpViaTransporte();
		valueToString += ", tpIntermedio: " +  getTpIntermedio();
		valueToString += ", nrCnpjIntermediario: " +  getNrCnpjIntermediario();
		valueToString += ", cdEstadoIntermediario: " +  getCdEstadoIntermediario();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new DeclaracaoImportacao(getCdDeclaracaoImportacao(),
			getCdExportador(),
			getCdEstado(),
			getCdItem(),
			getCdNotaFiscal(),
			getNrDeclaracaoImportacao(),
			getDtRegistro()==null ? null : (GregorianCalendar)getDtRegistro().clone(),
			getDsLocalDesembaraco(),
			getDtDesembaraco()==null ? null : (GregorianCalendar)getDtDesembaraco().clone(),
			getTpViaTransporte(),
			getTpIntermedio(),
			getNrCnpjIntermediario(),
			getCdEstadoIntermediario());
	}

}