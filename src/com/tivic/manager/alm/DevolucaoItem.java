package com.tivic.manager.alm;

public class DevolucaoItem {

	private int cdDocumentoSaida;
	private int cdProdutoServico;
	private int cdEmpresa;
	private int cdItem;
	private int cdDocumentoEntrada;
	private int cdItemEntrada;
	private float qtDevolvida;
	private int cdUnidadeMedida;

	public DevolucaoItem(int cdDocumentoSaida,
			int cdProdutoServico,
			int cdEmpresa,
			int cdItem,
			int cdDocumentoEntrada,
			int cdItemEntrada,
			float qtDevolvida,
			int cdUnidadeMedida){
		setCdDocumentoSaida(cdDocumentoSaida);
		setCdProdutoServico(cdProdutoServico);
		setCdEmpresa(cdEmpresa);
		setCdItem(cdItem);
		setCdDocumentoEntrada(cdDocumentoEntrada);
		setCdItemEntrada(cdItemEntrada);
		setQtDevolvida(qtDevolvida);
		setCdUnidadeMedida(cdUnidadeMedida);
	}
	public void setCdDocumentoSaida(int cdDocumentoSaida){
		this.cdDocumentoSaida=cdDocumentoSaida;
	}
	public int getCdDocumentoSaida(){
		return this.cdDocumentoSaida;
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
	public void setCdItem(int cdItem){
		this.cdItem=cdItem;
	}
	public int getCdItem(){
		return this.cdItem;
	}
	public void setCdDocumentoEntrada(int cdDocumentoEntrada){
		this.cdDocumentoEntrada=cdDocumentoEntrada;
	}
	public int getCdDocumentoEntrada(){
		return this.cdDocumentoEntrada;
	}
	public void setCdItemEntrada(int cdItemEntrada){
		this.cdItemEntrada=cdItemEntrada;
	}
	public int getCdItemEntrada(){
		return this.cdItemEntrada;
	}
	public void setQtDevolvida(float qtDevolvida){
		this.qtDevolvida=qtDevolvida;
	}
	public float getQtDevolvida(){
		return this.qtDevolvida;
	}
	public void setCdUnidadeMedida(int cdUnidadeMedida){
		this.cdUnidadeMedida=cdUnidadeMedida;
	}
	public int getCdUnidadeMedida(){
		return this.cdUnidadeMedida;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdDocumentoSaida: " +  getCdDocumentoSaida();
		valueToString += ", cdProdutoServico: " +  getCdProdutoServico();
		valueToString += ", cdEmpresa: " +  getCdEmpresa();
		valueToString += ", cdItem: " +  getCdItem();
		valueToString += ", cdDocumentoEntrada: " +  getCdDocumentoEntrada();
		valueToString += ", cdItemEntrada: " +  getCdItemEntrada();
		valueToString += ", qtDevolvida: " +  getQtDevolvida();
		valueToString += ", cdUnidadeMedida: " +  getCdUnidadeMedida();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new DevolucaoItem(getCdDocumentoSaida(),
			getCdProdutoServico(),
			getCdEmpresa(),
			getCdItem(),
			getCdDocumentoEntrada(),
			getCdItemEntrada(),
			getQtDevolvida(),
			getCdUnidadeMedida());
	}

}
