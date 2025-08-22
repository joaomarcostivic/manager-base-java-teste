package com.tivic.manager.alm;

public class DocEntradaReferencia {

	private int cdDocEntradaReferencia;
	private int cdProdutoServico;
	private int cdEmpresa;
	private int cdDocumentoEntrada;
	private int cdLocalArmazenamento;
	private int cdEntradaLocalItem;
	private int cdReferencia;

	public DocEntradaReferencia(int cdDocEntradaReferencia,
			int cdProdutoServico,
			int cdEmpresa,
			int cdDocumentoEntrada,
			int cdLocalArmazenamento,
			int cdEntradaLocalItem,
			int cdReferencia){
		setCdDocEntradaReferencia(cdDocEntradaReferencia);
		setCdProdutoServico(cdProdutoServico);
		setCdEmpresa(cdEmpresa);
		setCdDocumentoEntrada(cdDocumentoEntrada);
		setCdLocalArmazenamento(cdLocalArmazenamento);
		setCdEntradaLocalItem(cdEntradaLocalItem);
		setCdReferencia(cdReferencia);
	}
	public void setCdDocEntradaReferencia(int cdDocEntradaReferencia){
		this.cdDocEntradaReferencia=cdDocEntradaReferencia;
	}
	public int getCdDocEntradaReferencia(){
		return this.cdDocEntradaReferencia;
	}
	public void setCdProdutoServico(int cdProdutoServico){
		this.cdProdutoServico=cdProdutoServico;
	}
	public int getCdProdutoServico(){
		return this.cdProdutoServico;
	}
	public void setCdEmpresa(int cdEmpresa){
		this.cdEmpresa=cdEmpresa;
	}
	public int getCdEmpresa(){
		return this.cdEmpresa;
	}
	public void setCdDocumentoEntrada(int cdDocumentoEntrada){
		this.cdDocumentoEntrada=cdDocumentoEntrada;
	}
	public int getCdDocumentoEntrada(){
		return this.cdDocumentoEntrada;
	}
	public void setCdLocalArmazenamento(int cdLocalArmazenamento){
		this.cdLocalArmazenamento=cdLocalArmazenamento;
	}
	public int getCdLocalArmazenamento(){
		return this.cdLocalArmazenamento;
	}
	public void setCdEntradaLocalItem(int cdEntradaLocalItem){
		this.cdEntradaLocalItem=cdEntradaLocalItem;
	}
	public int getCdEntradaLocalItem(){
		return this.cdEntradaLocalItem;
	}
	public void setCdReferencia(int cdReferencia){
		this.cdReferencia=cdReferencia;
	}
	public int getCdReferencia(){
		return this.cdReferencia;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdDocEntradaReferencia: " +  getCdDocEntradaReferencia();
		valueToString += ", cdProdutoServico: " +  getCdProdutoServico();
		valueToString += ", cdEmpresa: " +  getCdEmpresa();
		valueToString += ", cdDocumentoEntrada: " +  getCdDocumentoEntrada();
		valueToString += ", cdLocalArmazenamento: " +  getCdLocalArmazenamento();
		valueToString += ", cdEntradaLocalItem: " +  getCdEntradaLocalItem();
		valueToString += ", cdReferencia: " +  getCdReferencia();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new DocEntradaReferencia(getCdDocEntradaReferencia(),
			getCdProdutoServico(),
			getCdEmpresa(),
			getCdDocumentoEntrada(),
			getCdLocalArmazenamento(),
			getCdEntradaLocalItem(),
			getCdReferencia());
	}

}
