package com.tivic.manager.adm;

public class VendaSaidaItem {

	private int cdPedidoVenda;
	private int cdEmpresa;
	private int cdProdutoServico;
	private int cdDocumentoSaida;
	private float qtSaida;
	private int cdItem;

	public VendaSaidaItem(int cdPedidoVenda,
			int cdEmpresa,
			int cdProdutoServico,
			int cdDocumentoSaida,
			float qtSaida,
			int cdItem){
		setCdPedidoVenda(cdPedidoVenda);
		setCdEmpresa(cdEmpresa);
		setCdProdutoServico(cdProdutoServico);
		setCdDocumentoSaida(cdDocumentoSaida);
		setQtSaida(qtSaida);
		setCdItem(cdItem);
	}
	public void setCdPedidoVenda(int cdPedidoVenda){
		this.cdPedidoVenda=cdPedidoVenda;
	}
	public int getCdPedidoVenda(){
		return this.cdPedidoVenda;
	}
	public void setCdEmpresa(int cdEmpresa){
		this.cdEmpresa=cdEmpresa;
	}
	public int getCdEmpresa(){
		return this.cdEmpresa;
	}
	public void setCdProdutoServico(int cdProdutoServico){
		this.cdProdutoServico=cdProdutoServico;
	}
	public int getCdProdutoServico(){
		return this.cdProdutoServico;
	}
	public void setCdDocumentoSaida(int cdDocumentoSaida){
		this.cdDocumentoSaida=cdDocumentoSaida;
	}
	public int getCdDocumentoSaida(){
		return this.cdDocumentoSaida;
	}
	public void setQtSaida(float qtSaida){
		this.qtSaida=qtSaida;
	}
	public float getQtSaida(){
		return this.qtSaida;
	}
	public void setCdItem(int cdItem){
		this.cdItem=cdItem;
	}
	public int getCdItem(){
		return this.cdItem;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdPedidoVenda: " +  getCdPedidoVenda();
		valueToString += ", cdEmpresa: " +  getCdEmpresa();
		valueToString += ", cdProdutoServico: " +  getCdProdutoServico();
		valueToString += ", cdDocumentoSaida: " +  getCdDocumentoSaida();
		valueToString += ", qtSaida: " +  getQtSaida();
		valueToString += ", cdItem: " +  getCdItem();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new VendaSaidaItem(getCdPedidoVenda(),
			getCdEmpresa(),
			getCdProdutoServico(),
			getCdDocumentoSaida(),
			getQtSaida(),
			getCdItem());
	}

}
