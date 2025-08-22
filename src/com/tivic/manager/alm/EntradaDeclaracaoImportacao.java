package com.tivic.manager.alm;

import java.util.GregorianCalendar;

public class EntradaDeclaracaoImportacao {

	private int cdEntradaDeclaracaoImportacao;
	private int cdDocumentoEntrada;
	private String nrDeclaracaoImportacao;
	private GregorianCalendar dtRegistro;
	private String nmLocal;
	private String sgUfDesembaraco;
	private GregorianCalendar dtDesembaraco;
	private int qtAdicao;
	private float vlTaxaDolar;
	private int tpViaTransporte;
	private int tpIntermedio;
	private String nrCnpjIntermediario;
	private int cdEstadoIntermediario;

	public EntradaDeclaracaoImportacao(){ }

	public EntradaDeclaracaoImportacao(int cdEntradaDeclaracaoImportacao,
			int cdDocumentoEntrada,
			String nrDeclaracaoImportacao,
			GregorianCalendar dtRegistro,
			String nmLocal,
			String sgUfDesembaraco,
			GregorianCalendar dtDesembaraco,
			int qtAdicao,
			float vlTaxaDolar,
			int tpViaTransporte,
			int tpIntermedio,
			String nrCnpjIntermediario,
			int cdEstadoIntermediario){
		setCdEntradaDeclaracaoImportacao(cdEntradaDeclaracaoImportacao);
		setCdDocumentoEntrada(cdDocumentoEntrada);
		setNrDeclaracaoImportacao(nrDeclaracaoImportacao);
		setDtRegistro(dtRegistro);
		setNmLocal(nmLocal);
		setSgUfDesembaraco(sgUfDesembaraco);
		setDtDesembaraco(dtDesembaraco);
		setQtAdicao(qtAdicao);
		setVlTaxaDolar(vlTaxaDolar);
		setTpViaTransporte(tpViaTransporte);
		setTpIntermedio(tpIntermedio);
		setNrCnpjIntermediario(nrCnpjIntermediario);
		setCdEstadoIntermediario(cdEstadoIntermediario);
	}
	public void setCdEntradaDeclaracaoImportacao(int cdEntradaDeclaracaoImportacao){
		this.cdEntradaDeclaracaoImportacao=cdEntradaDeclaracaoImportacao;
	}
	public int getCdEntradaDeclaracaoImportacao(){
		return this.cdEntradaDeclaracaoImportacao;
	}
	public void setCdDocumentoEntrada(int cdDocumentoEntrada){
		this.cdDocumentoEntrada=cdDocumentoEntrada;
	}
	public int getCdDocumentoEntrada(){
		return this.cdDocumentoEntrada;
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
	public void setNmLocal(String nmLocal){
		this.nmLocal=nmLocal;
	}
	public String getNmLocal(){
		return this.nmLocal;
	}
	public void setSgUfDesembaraco(String sgUfDesembaraco){
		this.sgUfDesembaraco=sgUfDesembaraco;
	}
	public String getSgUfDesembaraco(){
		return this.sgUfDesembaraco;
	}
	public void setDtDesembaraco(GregorianCalendar dtDesembaraco){
		this.dtDesembaraco=dtDesembaraco;
	}
	public GregorianCalendar getDtDesembaraco(){
		return this.dtDesembaraco;
	}
	public void setQtAdicao(int qtAdicao){
		this.qtAdicao=qtAdicao;
	}
	public int getQtAdicao(){
		return this.qtAdicao;
	}
	public void setVlTaxaDolar(float vlTaxaDolar){
		this.vlTaxaDolar=vlTaxaDolar;
	}
	public float getVlTaxaDolar(){
		return this.vlTaxaDolar;
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
		valueToString += "cdEntradaDeclaracaoImportacao: " +  getCdEntradaDeclaracaoImportacao();
		valueToString += ", cdDocumentoEntrada: " +  getCdDocumentoEntrada();
		valueToString += ", nrDeclaracaoImportacao: " +  getNrDeclaracaoImportacao();
		valueToString += ", dtRegistro: " +  sol.util.Util.formatDateTime(getDtRegistro(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", nmLocal: " +  getNmLocal();
		valueToString += ", sgUfDesembaraco: " +  getSgUfDesembaraco();
		valueToString += ", dtDesembaraco: " +  sol.util.Util.formatDateTime(getDtDesembaraco(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", qtAdicao: " +  getQtAdicao();
		valueToString += ", vlTaxaDolar: " +  getVlTaxaDolar();
		valueToString += ", tpViaTransporte: " +  getTpViaTransporte();
		valueToString += ", tpIntermedio: " +  getTpIntermedio();
		valueToString += ", nrCnpjIntermediario: " +  getNrCnpjIntermediario();
		valueToString += ", cdEstadoIntermediario: " +  getCdEstadoIntermediario();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new EntradaDeclaracaoImportacao(getCdEntradaDeclaracaoImportacao(),
			getCdDocumentoEntrada(),
			getNrDeclaracaoImportacao(),
			getDtRegistro()==null ? null : (GregorianCalendar)getDtRegistro().clone(),
			getNmLocal(),
			getSgUfDesembaraco(),
			getDtDesembaraco()==null ? null : (GregorianCalendar)getDtDesembaraco().clone(),
			getQtAdicao(),
			getVlTaxaDolar(),
			getTpViaTransporte(),
			getTpIntermedio(),
			getNrCnpjIntermediario(),
			getCdEstadoIntermediario());
	}

}