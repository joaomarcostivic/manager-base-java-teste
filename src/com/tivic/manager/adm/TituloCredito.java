package com.tivic.manager.adm;

import java.util.GregorianCalendar;

public class TituloCredito {

	private int cdTituloCredito;
	private int cdInstituicaoFinanceira;
	private int cdAlinea;
	private String nrDocumento;
	private String nrDocumentoEmissor;
	private int tpDocumentoEmissor;
	private String nmEmissor;
	private Double vlTitulo;
	private int tpEmissao;
	private String nrAgencia;
	private GregorianCalendar dtVencimento;
	private GregorianCalendar dtCredito;
	private int stTitulo;
	private String dsObservacao;
	private int cdTipoDocumento;
	private int tpCirculacao;
	private int cdConta;
	private int cdContaReceber;
	private int cdEmissor;
	private int cdPortador;
	private String nrConta;

	public TituloCredito(){ }

	public TituloCredito(int cdTituloCredito,
			int cdInstituicaoFinanceira,
			int cdAlinea,
			String nrDocumento,
			String nrDocumentoEmissor,
			int tpDocumentoEmissor,
			String nmEmissor,
			Double vlTitulo,
			int tpEmissao,
			String nrAgencia,
			GregorianCalendar dtVencimento,
			GregorianCalendar dtCredito,
			int stTitulo,
			String dsObservacao,
			int cdTipoDocumento,
			int tpCirculacao,
			int cdConta,
			int cdContaReceber,
			int cdEmissor,
			int cdPortador,
			String nrConta){
		setCdTituloCredito(cdTituloCredito);
		setCdInstituicaoFinanceira(cdInstituicaoFinanceira);
		setCdAlinea(cdAlinea);
		setNrDocumento(nrDocumento);
		setNrDocumentoEmissor(nrDocumentoEmissor);
		setTpDocumentoEmissor(tpDocumentoEmissor);
		setNmEmissor(nmEmissor);
		setVlTitulo(vlTitulo);
		setTpEmissao(tpEmissao);
		setNrAgencia(nrAgencia);
		setDtVencimento(dtVencimento);
		setDtCredito(dtCredito);
		setStTitulo(stTitulo);
		setDsObservacao(dsObservacao);
		setCdTipoDocumento(cdTipoDocumento);
		setTpCirculacao(tpCirculacao);
		setCdConta(cdConta);
		setCdContaReceber(cdContaReceber);
		setCdEmissor(cdEmissor);
		setCdPortador(cdPortador);
		setNrConta(nrConta);
	}
	public void setCdTituloCredito(int cdTituloCredito){
		this.cdTituloCredito=cdTituloCredito;
	}
	public int getCdTituloCredito(){
		return this.cdTituloCredito;
	}
	public void setCdInstituicaoFinanceira(int cdInstituicaoFinanceira){
		this.cdInstituicaoFinanceira=cdInstituicaoFinanceira;
	}
	public int getCdInstituicaoFinanceira(){
		return this.cdInstituicaoFinanceira;
	}
	public void setCdAlinea(int cdAlinea){
		this.cdAlinea=cdAlinea;
	}
	public int getCdAlinea(){
		return this.cdAlinea;
	}
	public void setNrDocumento(String nrDocumento){
		this.nrDocumento=nrDocumento;
	}
	public String getNrDocumento(){
		return this.nrDocumento;
	}
	public void setNrDocumentoEmissor(String nrDocumentoEmissor){
		this.nrDocumentoEmissor=nrDocumentoEmissor;
	}
	public String getNrDocumentoEmissor(){
		return this.nrDocumentoEmissor;
	}
	public void setTpDocumentoEmissor(int tpDocumentoEmissor){
		this.tpDocumentoEmissor=tpDocumentoEmissor;
	}
	public int getTpDocumentoEmissor(){
		return this.tpDocumentoEmissor;
	}
	public void setNmEmissor(String nmEmissor){
		this.nmEmissor=nmEmissor;
	}
	public String getNmEmissor(){
		return this.nmEmissor;
	}
	public void setVlTitulo(Double vlTitulo){
		this.vlTitulo=vlTitulo;
	}
	public Double getVlTitulo(){
		return this.vlTitulo;
	}
	public void setTpEmissao(int tpEmissao){
		this.tpEmissao=tpEmissao;
	}
	public int getTpEmissao(){
		return this.tpEmissao;
	}
	public void setNrAgencia(String nrAgencia){
		this.nrAgencia=nrAgencia;
	}
	public String getNrAgencia(){
		return this.nrAgencia;
	}
	public void setDtVencimento(GregorianCalendar dtVencimento){
		this.dtVencimento=dtVencimento;
	}
	public GregorianCalendar getDtVencimento(){
		return this.dtVencimento;
	}
	public void setDtCredito(GregorianCalendar dtCredito){
		this.dtCredito=dtCredito;
	}
	public GregorianCalendar getDtCredito(){
		return this.dtCredito;
	}
	public void setStTitulo(int stTitulo){
		this.stTitulo=stTitulo;
	}
	public int getStTitulo(){
		return this.stTitulo;
	}
	public void setDsObservacao(String dsObservacao){
		this.dsObservacao=dsObservacao;
	}
	public String getDsObservacao(){
		return this.dsObservacao;
	}
	public void setCdTipoDocumento(int cdTipoDocumento){
		this.cdTipoDocumento=cdTipoDocumento;
	}
	public int getCdTipoDocumento(){
		return this.cdTipoDocumento;
	}
	public void setTpCirculacao(int tpCirculacao){
		this.tpCirculacao=tpCirculacao;
	}
	public int getTpCirculacao(){
		return this.tpCirculacao;
	}
	public void setCdConta(int cdConta){
		this.cdConta=cdConta;
	}
	public int getCdConta(){
		return this.cdConta;
	}
	public void setCdContaReceber(int cdContaReceber){
		this.cdContaReceber=cdContaReceber;
	}
	public int getCdContaReceber(){
		return this.cdContaReceber;
	}
	public void setCdEmissor(int cdEmissor){
		this.cdEmissor=cdEmissor;
	}
	public int getCdEmissor(){
		return this.cdEmissor;
	}
	public void setCdPortador(int cdPortador){
		this.cdPortador=cdPortador;
	}
	public int getCdPortador(){
		return this.cdPortador;
	}
	public void setNrConta(String nrConta){
		this.nrConta=nrConta;
	}
	public String getNrConta(){
		return this.nrConta;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdTituloCredito: " +  getCdTituloCredito();
		valueToString += ", cdInstituicaoFinanceira: " +  getCdInstituicaoFinanceira();
		valueToString += ", cdAlinea: " +  getCdAlinea();
		valueToString += ", nrDocumento: " +  getNrDocumento();
		valueToString += ", nrDocumentoEmissor: " +  getNrDocumentoEmissor();
		valueToString += ", tpDocumentoEmissor: " +  getTpDocumentoEmissor();
		valueToString += ", nmEmissor: " +  getNmEmissor();
		valueToString += ", vlTitulo: " +  getVlTitulo();
		valueToString += ", tpEmissao: " +  getTpEmissao();
		valueToString += ", nrAgencia: " +  getNrAgencia();
		valueToString += ", dtVencimento: " +  sol.util.Util.formatDateTime(getDtVencimento(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", dtCredito: " +  sol.util.Util.formatDateTime(getDtCredito(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", stTitulo: " +  getStTitulo();
		valueToString += ", dsObservacao: " +  getDsObservacao();
		valueToString += ", cdTipoDocumento: " +  getCdTipoDocumento();
		valueToString += ", tpCirculacao: " +  getTpCirculacao();
		valueToString += ", cdConta: " +  getCdConta();
		valueToString += ", cdContaReceber: " +  getCdContaReceber();
		valueToString += ", cdEmissor: " +  getCdEmissor();
		valueToString += ", cdPortador: " +  getCdPortador();
		valueToString += ", nrConta: " +  getNrConta();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new TituloCredito(getCdTituloCredito(),
			getCdInstituicaoFinanceira(),
			getCdAlinea(),
			getNrDocumento(),
			getNrDocumentoEmissor(),
			getTpDocumentoEmissor(),
			getNmEmissor(),
			getVlTitulo(),
			getTpEmissao(),
			getNrAgencia(),
			getDtVencimento()==null ? null : (GregorianCalendar)getDtVencimento().clone(),
			getDtCredito()==null ? null : (GregorianCalendar)getDtCredito().clone(),
			getStTitulo(),
			getDsObservacao(),
			getCdTipoDocumento(),
			getTpCirculacao(),
			getCdConta(),
			getCdContaReceber(),
			getCdEmissor(),
			getCdPortador(),
			getNrConta());
	}

}