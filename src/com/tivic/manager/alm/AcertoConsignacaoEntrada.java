package com.tivic.manager.alm;

import java.util.GregorianCalendar;

public class AcertoConsignacaoEntrada {

	private int cdAcertoConsignacao;
	private GregorianCalendar dtAcerto;
	private int stAcerto;
	private int tpAcerto;
	private int cdFornecedor;
	private GregorianCalendar dtEmissao;
	private GregorianCalendar dtVencimento;
	private int cdEmpresa;
	private int cdDocumentoSaida;
	private int cdDocumentoEntrada;
	private String idAcertoConsignacao;

	public AcertoConsignacaoEntrada(int cdAcertoConsignacao,
			GregorianCalendar dtAcerto,
			int stAcerto,
			int tpAcerto,
			int cdFornecedor,
			GregorianCalendar dtEmissao,
			GregorianCalendar dtVencimento,
			int cdEmpresa,
			int cdDocumentoSaida,
			int cdDocumentoEntrada,
			String idAcertoConsignacao){
		setCdAcertoConsignacao(cdAcertoConsignacao);
		setDtAcerto(dtAcerto);
		setStAcerto(stAcerto);
		setTpAcerto(tpAcerto);
		setCdFornecedor(cdFornecedor);
		setDtEmissao(dtEmissao);
		setDtVencimento(dtVencimento);
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
	public void setCdFornecedor(int cdFornecedor){
		this.cdFornecedor=cdFornecedor;
	}
	public int getCdFornecedor(){
		return this.cdFornecedor;
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
		valueToString += ", cdFornecedor: " +  getCdFornecedor();
		valueToString += ", dtEmissao: " +  sol.util.Util.formatDateTime(getDtEmissao(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", dtVencimento: " +  sol.util.Util.formatDateTime(getDtVencimento(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", cdEmpresa: " +  getCdEmpresa();
		valueToString += ", cdDocumentoSaida: " +  getCdDocumentoSaida();
		valueToString += ", cdDocumentoEntrada: " +  getCdDocumentoEntrada();
		valueToString += ", idAcertoConsignacao: " +  getIdAcertoConsignacao();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new AcertoConsignacaoEntrada(getCdAcertoConsignacao(),
			getDtAcerto()==null ? null : (GregorianCalendar)getDtAcerto().clone(),
			getStAcerto(),
			getTpAcerto(),
			getCdFornecedor(),
			getDtEmissao()==null ? null : (GregorianCalendar)getDtEmissao().clone(),
			getDtVencimento()==null ? null : (GregorianCalendar)getDtVencimento().clone(),
			getCdEmpresa(),
			getCdDocumentoSaida(),
			getCdDocumentoEntrada(),
			getIdAcertoConsignacao());
	}

}
