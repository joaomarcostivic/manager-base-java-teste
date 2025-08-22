package com.tivic.manager.adm;

import java.util.GregorianCalendar;

public class ContratoNegociacao {

	private int cdContrato;
	private int cdNegociacao;
	private GregorianCalendar dtNegociacao;
	private int cdUsuario;
	private int cdResponsavel;
	private int cdDocumentoSaida;
	private float prMultaMora;
	private float prJurosMora;
	private float vlMultaMora;
	private float vlJurosMora;
	private float prMultaPenal;
	private float vlDesconto;
	private float vlAcrescimo;
	private int qtParcelas;
	private GregorianCalendar dtPrimeiroVencimento;
	private int nrDiaVencimento;
	private float vlParcela;
	private float vlFinal;
	private float prCarenciaAnterior;
	private float vlCarencia;
	private float vlPago;
	private String txtObservacao;
	private float vlMultaPenal;
	private float vlCorridoContrato;
	private int stNegociacao;
	private int tpNegociacao;
	private int lgCarencia;
	private float prCorridoContrato;

	public ContratoNegociacao(int cdContrato,
			int cdNegociacao,
			GregorianCalendar dtNegociacao,
			int cdUsuario,
			int cdResponsavel,
			int cdDocumentoSaida,
			float prMultaMora,
			float prJurosMora,
			float vlMultaMora,
			float vlJurosMora,
			float prMultaPenal,
			float vlDesconto,
			float vlAcrescimo,
			int qtParcelas,
			GregorianCalendar dtPrimeiroVencimento,
			int nrDiaVencimento,
			float vlParcela,
			float vlFinal,
			float prCarenciaAnterior,
			float vlCarencia,
			float vlPago,
			String txtObservacao,
			float vlMultaPenal,
			float vlCorridoContrato,
			int stNegociacao,
			int tpNegociacao,
			int lgCarencia,
			float prCorridoContrato){
		setCdContrato(cdContrato);
		setCdNegociacao(cdNegociacao);
		setDtNegociacao(dtNegociacao);
		setCdUsuario(cdUsuario);
		setCdResponsavel(cdResponsavel);
		setCdDocumentoSaida(cdDocumentoSaida);
		setPrMultaMora(prMultaMora);
		setPrJurosMora(prJurosMora);
		setVlMultaMora(vlMultaMora);
		setVlJurosMora(vlJurosMora);
		setPrMultaPenal(prMultaPenal);
		setVlDesconto(vlDesconto);
		setVlAcrescimo(vlAcrescimo);
		setQtParcelas(qtParcelas);
		setDtPrimeiroVencimento(dtPrimeiroVencimento);
		setNrDiaVencimento(nrDiaVencimento);
		setVlParcela(vlParcela);
		setVlFinal(vlFinal);
		setPrCarenciaAnterior(prCarenciaAnterior);
		setVlCarencia(vlCarencia);
		setVlPago(vlPago);
		setTxtObservacao(txtObservacao);
		setVlMultaPenal(vlMultaPenal);
		setVlCorridoContrato(vlCorridoContrato);
		setStNegociacao(stNegociacao);
		setTpNegociacao(tpNegociacao);
		setLgCarencia(lgCarencia);
		setPrCorridoContrato(prCorridoContrato);
	}
	public void setCdContrato(int cdContrato){
		this.cdContrato=cdContrato;
	}
	public int getCdContrato(){
		return this.cdContrato;
	}
	public void setCdNegociacao(int cdNegociacao){
		this.cdNegociacao=cdNegociacao;
	}
	public int getCdNegociacao(){
		return this.cdNegociacao;
	}
	public void setDtNegociacao(GregorianCalendar dtNegociacao){
		this.dtNegociacao=dtNegociacao;
	}
	public GregorianCalendar getDtNegociacao(){
		return this.dtNegociacao;
	}
	public void setCdUsuario(int cdUsuario){
		this.cdUsuario=cdUsuario;
	}
	public int getCdUsuario(){
		return this.cdUsuario;
	}
	public void setCdResponsavel(int cdResponsavel){
		this.cdResponsavel=cdResponsavel;
	}
	public int getCdResponsavel(){
		return this.cdResponsavel;
	}
	public void setCdDocumentoSaida(int cdDocumentoSaida){
		this.cdDocumentoSaida=cdDocumentoSaida;
	}
	public int getCdDocumentoSaida(){
		return this.cdDocumentoSaida;
	}
	public void setPrMultaMora(float prMultaMora){
		this.prMultaMora=prMultaMora;
	}
	public float getPrMultaMora(){
		return this.prMultaMora;
	}
	public void setPrJurosMora(float prJurosMora){
		this.prJurosMora=prJurosMora;
	}
	public float getPrJurosMora(){
		return this.prJurosMora;
	}
	public void setVlMultaMora(float vlMultaMora){
		this.vlMultaMora=vlMultaMora;
	}
	public float getVlMultaMora(){
		return this.vlMultaMora;
	}
	public void setVlJurosMora(float vlJurosMora){
		this.vlJurosMora=vlJurosMora;
	}
	public float getVlJurosMora(){
		return this.vlJurosMora;
	}
	public void setPrMultaPenal(float prMultaPenal){
		this.prMultaPenal=prMultaPenal;
	}
	public float getPrMultaPenal(){
		return this.prMultaPenal;
	}
	public void setVlDesconto(float vlDesconto){
		this.vlDesconto=vlDesconto;
	}
	public float getVlDesconto(){
		return this.vlDesconto;
	}
	public void setVlAcrescimo(float vlAcrescimo){
		this.vlAcrescimo=vlAcrescimo;
	}
	public float getVlAcrescimo(){
		return this.vlAcrescimo;
	}
	public void setQtParcelas(int qtParcelas){
		this.qtParcelas=qtParcelas;
	}
	public int getQtParcelas(){
		return this.qtParcelas;
	}
	public void setDtPrimeiroVencimento(GregorianCalendar dtPrimeiroVencimento){
		this.dtPrimeiroVencimento=dtPrimeiroVencimento;
	}
	public GregorianCalendar getDtPrimeiroVencimento(){
		return this.dtPrimeiroVencimento;
	}
	public void setNrDiaVencimento(int nrDiaVencimento){
		this.nrDiaVencimento=nrDiaVencimento;
	}
	public int getNrDiaVencimento(){
		return this.nrDiaVencimento;
	}
	public void setVlParcela(float vlParcela){
		this.vlParcela=vlParcela;
	}
	public float getVlParcela(){
		return this.vlParcela;
	}
	public void setVlFinal(float vlFinal){
		this.vlFinal=vlFinal;
	}
	public float getVlFinal(){
		return this.vlFinal;
	}
	public void setPrCarenciaAnterior(float prCarenciaAnterior){
		this.prCarenciaAnterior=prCarenciaAnterior;
	}
	public float getPrCarenciaAnterior(){
		return this.prCarenciaAnterior;
	}
	public void setVlCarencia(float vlCarencia){
		this.vlCarencia=vlCarencia;
	}
	public float getVlCarencia(){
		return this.vlCarencia;
	}
	public void setVlPago(float vlPago){
		this.vlPago=vlPago;
	}
	public float getVlPago(){
		return this.vlPago;
	}
	public void setTxtObservacao(String txtObservacao){
		this.txtObservacao=txtObservacao;
	}
	public String getTxtObservacao(){
		return this.txtObservacao;
	}
	public void setVlMultaPenal(float vlMultaPenal){
		this.vlMultaPenal=vlMultaPenal;
	}
	public float getVlMultaPenal(){
		return this.vlMultaPenal;
	}
	public void setVlCorridoContrato(float vlCorridoContrato){
		this.vlCorridoContrato=vlCorridoContrato;
	}
	public float getVlCorridoContrato(){
		return this.vlCorridoContrato;
	}
	public void setStNegociacao(int stNegociacao){
		this.stNegociacao=stNegociacao;
	}
	public int getStNegociacao(){
		return this.stNegociacao;
	}
	public void setTpNegociacao(int tpNegociacao){
		this.tpNegociacao=tpNegociacao;
	}
	public int getTpNegociacao(){
		return this.tpNegociacao;
	}
	public void setLgCarencia(int lgCarencia){
		this.lgCarencia=lgCarencia;
	}
	public int getLgCarencia(){
		return this.lgCarencia;
	}
	public void setPrCorridoContrato(float prCorridoContrato){
		this.prCorridoContrato=prCorridoContrato;
	}
	public float getPrCorridoContrato(){
		return this.prCorridoContrato;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdContrato: " +  getCdContrato();
		valueToString += ", cdNegociacao: " +  getCdNegociacao();
		valueToString += ", dtNegociacao: " +  sol.util.Util.formatDateTime(getDtNegociacao(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", cdUsuario: " +  getCdUsuario();
		valueToString += ", cdResponsavel: " +  getCdResponsavel();
		valueToString += ", cdDocumentoSaida: " +  getCdDocumentoSaida();
		valueToString += ", prMultaMora: " +  getPrMultaMora();
		valueToString += ", prJurosMora: " +  getPrJurosMora();
		valueToString += ", vlMultaMora: " +  getVlMultaMora();
		valueToString += ", vlJurosMora: " +  getVlJurosMora();
		valueToString += ", prMultaPenal: " +  getPrMultaPenal();
		valueToString += ", vlDesconto: " +  getVlDesconto();
		valueToString += ", vlAcrescimo: " +  getVlAcrescimo();
		valueToString += ", qtParcelas: " +  getQtParcelas();
		valueToString += ", dtPrimeiroVencimento: " +  sol.util.Util.formatDateTime(getDtPrimeiroVencimento(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", nrDiaVencimento: " +  getNrDiaVencimento();
		valueToString += ", vlParcela: " +  getVlParcela();
		valueToString += ", vlFinal: " +  getVlFinal();
		valueToString += ", prCarenciaAnterior: " +  getPrCarenciaAnterior();
		valueToString += ", vlCarencia: " +  getVlCarencia();
		valueToString += ", vlPago: " +  getVlPago();
		valueToString += ", txtObservacao: " +  getTxtObservacao();
		valueToString += ", vlMultaPenal: " +  getVlMultaPenal();
		valueToString += ", vlCorridoContrato: " +  getVlCorridoContrato();
		valueToString += ", stNegociacao: " +  getStNegociacao();
		valueToString += ", tpNegociacao: " +  getTpNegociacao();
		valueToString += ", lgCarencia: " +  getLgCarencia();
		valueToString += ", prCorridoContrato: " +  getPrCorridoContrato();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new ContratoNegociacao(getCdContrato(),
			getCdNegociacao(),
			getDtNegociacao()==null ? null : (GregorianCalendar)getDtNegociacao().clone(),
			getCdUsuario(),
			getCdResponsavel(),
			getCdDocumentoSaida(),
			getPrMultaMora(),
			getPrJurosMora(),
			getVlMultaMora(),
			getVlJurosMora(),
			getPrMultaPenal(),
			getVlDesconto(),
			getVlAcrescimo(),
			getQtParcelas(),
			getDtPrimeiroVencimento()==null ? null : (GregorianCalendar)getDtPrimeiroVencimento().clone(),
			getNrDiaVencimento(),
			getVlParcela(),
			getVlFinal(),
			getPrCarenciaAnterior(),
			getVlCarencia(),
			getVlPago(),
			getTxtObservacao(),
			getVlMultaPenal(),
			getVlCorridoContrato(),
			getStNegociacao(),
			getTpNegociacao(),
			getLgCarencia(),
			getPrCorridoContrato());
	}

}
