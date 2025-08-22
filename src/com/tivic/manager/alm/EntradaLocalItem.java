package com.tivic.manager.alm;

public class EntradaLocalItem {

	private int cdProdutoServico;
	private int cdDocumentoEntrada;
	private int cdEmpresa;
	private int cdLocalArmazenamento;
	private float qtEntrada;
	private float qtEntradaConsignada;
	private int cdEntradaLocalItem;
	private int cdReferencia;
	private int cdItem;

	public EntradaLocalItem(){}
	
	public EntradaLocalItem(int cdProdutoServico, int cdDocumentoEntrada,
			int cdEmpresa, int cdLocalArmazenamento, float qtEntrada,
			float qtEntradaConsignada, int cdEntradaLocalItem,
			int cdReferencia, int cdItem) {
		setCdProdutoServico(cdProdutoServico);
		setCdDocumentoEntrada(cdDocumentoEntrada);
		setCdEmpresa(cdEmpresa);
		setCdLocalArmazenamento(cdLocalArmazenamento);
		setQtEntrada(qtEntrada);
		setQtEntradaConsignada(qtEntradaConsignada);
		setCdEntradaLocalItem(cdEntradaLocalItem);
		setCdReferencia(cdReferencia);
		setCdItem(cdItem);
	}
	public EntradaLocalItem(int cdProdutoServico,
			int cdDocumentoEntrada,
			int cdEmpresa,
			int cdLocalArmazenamento,
			float qtEntrada,
			float qtEntradaConsignada,
			int cdEntradaLocalItem,
			int cdItem){
		setCdProdutoServico(cdProdutoServico);
		setCdDocumentoEntrada(cdDocumentoEntrada);
		setCdEmpresa(cdEmpresa);
		setCdLocalArmazenamento(cdLocalArmazenamento);
		setQtEntrada(qtEntrada);
		setQtEntradaConsignada(qtEntradaConsignada);
		setCdEntradaLocalItem(cdEntradaLocalItem);
		setCdItem(cdItem);
	}
	public void setCdProdutoServico(int cdProdutoServico){
		this.cdProdutoServico=cdProdutoServico;
	}
	public int getCdProdutoServico(){
		return this.cdProdutoServico;
	}
	public void setCdDocumentoEntrada(int cdDocumentoEntrada){
		this.cdDocumentoEntrada=cdDocumentoEntrada;
	}
	public int getCdDocumentoEntrada(){
		return this.cdDocumentoEntrada;
	}
	public void setCdEmpresa(int cdEmpresa){
		this.cdEmpresa=cdEmpresa;
	}
	public int getCdEmpresa(){
		return this.cdEmpresa;
	}
	public void setCdLocalArmazenamento(int cdLocalArmazenamento){
		this.cdLocalArmazenamento=cdLocalArmazenamento;
	}
	public int getCdLocalArmazenamento(){
		return this.cdLocalArmazenamento;
	}
	public void setQtEntrada(float qtEntrada){
		this.qtEntrada=qtEntrada;
	}
	public float getQtEntrada(){
		return this.qtEntrada;
	}
	public void setQtEntradaConsignada(float qtEntradaConsignada){
		this.qtEntradaConsignada=qtEntradaConsignada;
	}
	public float getQtEntradaConsignada(){
		return this.qtEntradaConsignada;
	}
	public void setCdEntradaLocalItem(int cdEntradaLocalItem){
		this.cdEntradaLocalItem=cdEntradaLocalItem;
	}
	public int getCdEntradaLocalItem(){
		return this.cdEntradaLocalItem;
	}
	public void setCdItem(int cdItem){
		this.cdItem=cdItem;
	}
	public int getCdReferencia() {
		return cdReferencia;
	}
	public void setCdReferencia(int cdReferencia) {
		this.cdReferencia = cdReferencia;
	}
	public int getCdItem(){
		return this.cdItem;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdProdutoServico: " +  getCdProdutoServico();
		valueToString += ", cdDocumentoEntrada: " +  getCdDocumentoEntrada();
		valueToString += ", cdEmpresa: " +  getCdEmpresa();
		valueToString += ", cdLocalArmazenamento: " +  getCdLocalArmazenamento();
		valueToString += ", qtEntrada: " +  getQtEntrada();
		valueToString += ", qtEntradaConsignada: " +  getQtEntradaConsignada();
		valueToString += ", cdEntradaLocalItem: " +  getCdEntradaLocalItem();
		valueToString += ", cdReferencia: " +  getCdReferencia();
		valueToString += ", cdItem: " +  getCdItem();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new EntradaLocalItem(getCdProdutoServico(),
			getCdDocumentoEntrada(),
			getCdEmpresa(),
			getCdLocalArmazenamento(),
			getQtEntrada(),
			getQtEntradaConsignada(),
			getCdEntradaLocalItem(),
			getCdReferencia(),
			getCdItem());
	}

}
