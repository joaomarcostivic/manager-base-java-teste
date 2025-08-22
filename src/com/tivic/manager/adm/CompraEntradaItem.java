package com.tivic.manager.adm;

public class CompraEntradaItem {

	private int cdEmpresa;
	private int cdOrdemCompra;
	private int cdDocumentoEntrada;
	private int cdProdutoServico;
	private float qtRecebida;
	private int cdItem;

	public CompraEntradaItem(){ }

	public CompraEntradaItem(int cdEmpresa,
			int cdOrdemCompra,
			int cdDocumentoEntrada,
			int cdProdutoServico,
			float qtRecebida,
			int cdItem){
		setCdEmpresa(cdEmpresa);
		setCdOrdemCompra(cdOrdemCompra);
		setCdDocumentoEntrada(cdDocumentoEntrada);
		setCdProdutoServico(cdProdutoServico);
		setQtRecebida(qtRecebida);
		setCdItem(cdItem);
	}
	public void setCdEmpresa(int cdEmpresa){
		this.cdEmpresa=cdEmpresa;
	}
	public int getCdEmpresa(){
		return this.cdEmpresa;
	}
	public void setCdOrdemCompra(int cdOrdemCompra){
		this.cdOrdemCompra=cdOrdemCompra;
	}
	public int getCdOrdemCompra(){
		return this.cdOrdemCompra;
	}
	public void setCdDocumentoEntrada(int cdDocumentoEntrada){
		this.cdDocumentoEntrada=cdDocumentoEntrada;
	}
	public int getCdDocumentoEntrada(){
		return this.cdDocumentoEntrada;
	}
	public void setCdProdutoServico(int cdProdutoServico){
		this.cdProdutoServico=cdProdutoServico;
	}
	public int getCdProdutoServico(){
		return this.cdProdutoServico;
	}
	public void setQtRecebida(float qtRecebida){
		this.qtRecebida=qtRecebida;
	}
	public float getQtRecebida(){
		return this.qtRecebida;
	}
	public void setCdItem(int cdItem){
		this.cdItem=cdItem;
	}
	public int getCdItem(){
		return this.cdItem;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdEmpresa: " +  getCdEmpresa();
		valueToString += ", cdOrdemCompra: " +  getCdOrdemCompra();
		valueToString += ", cdDocumentoEntrada: " +  getCdDocumentoEntrada();
		valueToString += ", cdProdutoServico: " +  getCdProdutoServico();
		valueToString += ", qtRecebida: " +  getQtRecebida();
		valueToString += ", cdItem: " +  getCdItem();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new CompraEntradaItem(getCdEmpresa(),
			getCdOrdemCompra(),
			getCdDocumentoEntrada(),
			getCdProdutoServico(),
			getQtRecebida(),
			getCdItem());
	}

}