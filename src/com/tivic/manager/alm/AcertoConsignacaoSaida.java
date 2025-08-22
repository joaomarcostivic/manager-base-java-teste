package com.tivic.manager.alm;

import java.util.GregorianCalendar;

public class AcertoConsignacaoSaida {

	private int cdAcertoConsignacao;
	private GregorianCalendar dtAcerto;
	private int stAcerto;
	private int tpAcerto;
	private GregorianCalendar dtEmissao;
	private GregorianCalendar dtVencimento;
	private int cdCliente;
	private int cdEmpresa;
	private int cdDocumentoSaida;
	private int cdDocumentoEntrada;
	private String idAcertoConsignacao;

	public AcertoConsignacaoSaida(int cdAcertoConsignacao,
			GregorianCalendar dtAcerto,
			int stAcerto,
			int tpAcerto,
			GregorianCalendar dtEmissao,
			GregorianCalendar dtVencimento,
			int cdCliente,
			int cdEmpresa,
			int cdDocumentoSaida,
			int cdDocumentoEntrada,
			String idAcertoConsignacao){
		setCdAcertoConsignacao(cdAcertoConsignacao);
		setDtAcerto(dtAcerto);
		setStAcerto(stAcerto);
		setTpAcerto(tpAcerto);
		setDtEmissao(dtEmissao);
		setDtVencimento(dtVencimento);
		setCdCliente(cdCliente);
		setCdEmpresa(cdEmpresa);
		setCdDocumentoSaida(cdDocumentoSaida);
		setCdDocumentoEntrada(cdDocumentoEntrada);
		setIdAcertoConsignacao(idAcertoConsignacao);
	}
	public void setCdAcertoConsignacao(int cdAcertoConsignacao){
		this.cdAcertoConsignacao=cdAcertoConsignacao;
	}
	public int getCdAcertoConsignacao(){
		return this.cdAcertoConsignacao;
	}
	public void setDtAcerto(GregorianCalendar dtAcerto){
		this.dtAcerto=dtAcerto;
	}
	public GregorianCalendar getDtAcerto(){
		return this.dtAcerto;
	}
	public void setStAcerto(int stAcerto){
		this.stAcerto=stAcerto;
	}
	public int getStAcerto(){
		return this.stAcerto;
	}
	public void setTpAcerto(int tpAcerto){
		this.tpAcerto=tpAcerto;
	}
	public int getTpAcerto(){
		return this.tpAcerto;
	}
	public void setDtEmissao(GregorianCalendar dtEmissao){
		this.dtEmissao=dtEmissao;
	}
	public GregorianCalendar getDtEmissao(){
		return this.dtEmissao;
	}
	public void setDtVencimento(GregorianCalendar dtVencimento){
		this.dtVencimento=dtVencimento;
	}
	public GregorianCalendar getDtVencimento(){
		return this.dtVencimento;
	}
	public void setCdCliente(int cdCliente){
		this.cdCliente=cdCliente;
	}
	public int getCdCliente(){
		return this.cdCliente;
	}
	public void setCdEmpresa(int cdEmpresa){
		this.cdEmpresa=cdEmpresa;
	}
	public int getCdEmpresa(){
		return this.cdEmpresa;
	}
	public void setCdDocumentoSaida(int cdDocumentoSaida){
		this.cdDocumentoSaida=cdDocumentoSaida;
	}
	public int getCdDocumentoSaida(){
		return this.cdDocumentoSaida;
	}
	public void setCdDocumentoEntrada(int cdDocumentoEntrada){
		this.cdDocumentoEntrada=cdDocumentoEntrada;
	}
	public int getCdDocumentoEntrada(){
		return this.cdDocumentoEntrada;
	}
	public void setIdAcertoConsignacao(String idAcertoConsignacao){
		this.idAcertoConsignacao=idAcertoConsignacao;
	}
	public String getIdAcertoConsignacao(){
		return this.idAcertoConsignacao;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdAcertoConsignacao: " +  getCdAcertoConsignacao();
		valueToString += ", dtAcerto: " +  sol.util.Util.formatDateTime(getDtAcerto(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", stAcerto: " +  getStAcerto();
		valueToString += ", tpAcerto: " +  getTpAcerto();
		valueToString += ", dtEmissao: " +  sol.util.Util.formatDateTime(getDtEmissao(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", dtVencimento: " +  sol.util.Util.formatDateTime(getDtVencimento(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", cdCliente: " +  getCdCliente();
		valueToString += ", cdEmpresa: " +  getCdEmpresa();
		valueToString += ", cdDocumentoSaida: " +  getCdDocumentoSaida();
		valueToString += ", cdDocumentoEntrada: " +  getCdDocumentoEntrada();
		valueToString += ", idAcertoConsignacao: " +  getIdAcertoConsignacao();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new AcertoConsignacaoSaida(getCdAcertoConsignacao(),
			getDtAcerto()==null ? null : (GregorianCalendar)getDtAcerto().clone(),
			getStAcerto(),
			getTpAcerto(),
			getDtEmissao()==null ? null : (GregorianCalendar)getDtEmissao().clone(),
			getDtVencimento()==null ? null : (GregorianCalendar)getDtVencimento().clone(),
			getCdCliente(),
			getCdEmpresa(),
			getCdDocumentoSaida(),
			getCdDocumentoEntrada(),
			getIdAcertoConsignacao());
	}

}
