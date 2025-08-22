package com.tivic.manager.crt;

import java.util.GregorianCalendar;

public class Documento {

	private int cdDocumento;
	private GregorianCalendar dtDocumento;
	private String txtDocumento;
	private String nmLocalOrigem;
	private String nmLocalDestino;
	private int lgConfidencial;
	private String idDocumento;
	private int cdPessoaAtual;
	private int cdPessoaEmissor;
	private int cdEmpresaEmissora;
	private int cdEmpresaAtual;
	private int stDocumento;

	public Documento(int cdDocumento,
			GregorianCalendar dtDocumento,
			String txtDocumento,
			String nmLocalOrigem,
			String nmLocalDestino,
			int lgConfidencial,
			String idDocumento,
			int cdPessoaAtual,
			int cdPessoaEmissor,
			int cdEmpresaEmissora,
			int cdEmpresaAtual,
			int stDocumento){
		setCdDocumento(cdDocumento);
		setDtDocumento(dtDocumento);
		setTxtDocumento(txtDocumento);
		setNmLocalOrigem(nmLocalOrigem);
		setNmLocalDestino(nmLocalDestino);
		setLgConfidencial(lgConfidencial);
		setIdDocumento(idDocumento);
		setCdPessoaAtual(cdPessoaAtual);
		setCdPessoaEmissor(cdPessoaEmissor);
		setCdEmpresaEmissora(cdEmpresaEmissora);
		setCdEmpresaAtual(cdEmpresaAtual);
		setStDocumento(stDocumento);
	}
	public void setCdDocumento(int cdDocumento){
		this.cdDocumento=cdDocumento;
	}
	public int getCdDocumento(){
		return this.cdDocumento;
	}
	public void setDtDocumento(GregorianCalendar dtDocumento){
		this.dtDocumento=dtDocumento;
	}
	public GregorianCalendar getDtDocumento(){
		return this.dtDocumento;
	}
	public void setTxtDocumento(String txtDocumento){
		this.txtDocumento=txtDocumento;
	}
	public String getTxtDocumento(){
		return this.txtDocumento;
	}
	public void setNmLocalOrigem(String nmLocalOrigem){
		this.nmLocalOrigem=nmLocalOrigem;
	}
	public String getNmLocalOrigem(){
		return this.nmLocalOrigem;
	}
	public void setNmLocalDestino(String nmLocalDestino){
		this.nmLocalDestino=nmLocalDestino;
	}
	public String getNmLocalDestino(){
		return this.nmLocalDestino;
	}
	public void setLgConfidencial(int lgConfidencial){
		this.lgConfidencial=lgConfidencial;
	}
	public int getLgConfidencial(){
		return this.lgConfidencial;
	}
	public void setIdDocumento(String idDocumento){
		this.idDocumento=idDocumento;
	}
	public String getIdDocumento(){
		return this.idDocumento;
	}
	public void setCdPessoaAtual(int cdPessoaAtual){
		this.cdPessoaAtual=cdPessoaAtual;
	}
	public int getCdPessoaAtual(){
		return this.cdPessoaAtual;
	}
	public void setCdPessoaEmissor(int cdPessoaEmissor){
		this.cdPessoaEmissor=cdPessoaEmissor;
	}
	public int getCdPessoaEmissor(){
		return this.cdPessoaEmissor;
	}
	public void setCdEmpresaEmissora(int cdEmpresaEmissora){
		this.cdEmpresaEmissora=cdEmpresaEmissora;
	}
	public int getCdEmpresaEmissora(){
		return this.cdEmpresaEmissora;
	}
	public void setCdEmpresaAtual(int cdEmpresaAtual){
		this.cdEmpresaAtual=cdEmpresaAtual;
	}
	public int getCdEmpresaAtual(){
		return this.cdEmpresaAtual;
	}
	public void setStDocumento(int stDocumento){
		this.stDocumento=stDocumento;
	}
	public int getStDocumento(){
		return this.stDocumento;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdDocumento: " +  getCdDocumento();
		valueToString += ", dtDocumento: " +  sol.util.Util.formatDateTime(getDtDocumento(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", txtDocumento: " +  getTxtDocumento();
		valueToString += ", nmLocalOrigem: " +  getNmLocalOrigem();
		valueToString += ", nmLocalDestino: " +  getNmLocalDestino();
		valueToString += ", lgConfidencial: " +  getLgConfidencial();
		valueToString += ", idDocumento: " +  getIdDocumento();
		valueToString += ", cdPessoaAtual: " +  getCdPessoaAtual();
		valueToString += ", cdPessoaEmissor: " +  getCdPessoaEmissor();
		valueToString += ", cdEmpresaEmissora: " +  getCdEmpresaEmissora();
		valueToString += ", cdEmpresaAtual: " +  getCdEmpresaAtual();
		valueToString += ", stDocumento: " +  getStDocumento();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new Documento(getCdDocumento(),
			getDtDocumento()==null ? null : (GregorianCalendar)getDtDocumento().clone(),
			getTxtDocumento(),
			getNmLocalOrigem(),
			getNmLocalDestino(),
			getLgConfidencial(),
			getIdDocumento(),
			getCdPessoaAtual(),
			getCdPessoaEmissor(),
			getCdEmpresaEmissora(),
			getCdEmpresaAtual(),
			getStDocumento());
	}

}