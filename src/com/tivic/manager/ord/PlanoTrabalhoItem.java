package com.tivic.manager.ord;

public class PlanoTrabalhoItem {

	private int cdPlanoTrabalho;
	private int cdItem;
	private int cdDocumentoSaida;
	private int cdDocumentoEntrada;
	private int tpMovimento;

	public PlanoTrabalhoItem() { }

	public PlanoTrabalhoItem(int cdPlanoTrabalho,
			int cdItem,
			int cdDocumentoSaida,
			int cdDocumentoEntrada,
			int tpMovimento) {
		setCdPlanoTrabalho(cdPlanoTrabalho);
		setCdItem(cdItem);
		setCdDocumentoSaida(cdDocumentoSaida);
		setCdDocumentoEntrada(cdDocumentoEntrada);
		setTpMovimento(tpMovimento);
	}
	public void setCdPlanoTrabalho(int cdPlanoTrabalho){
		this.cdPlanoTrabalho=cdPlanoTrabalho;
	}
	public int getCdPlanoTrabalho(){
		return this.cdPlanoTrabalho;
	}
	public void setCdItem(int cdItem){
		this.cdItem=cdItem;
	}
	public int getCdItem(){
		return this.cdItem;
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
	public void setTpMovimento(int tpMovimento){
		this.tpMovimento=tpMovimento;
	}
	public int getTpMovimento(){
		return this.tpMovimento;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdPlanoTrabalho: " +  getCdPlanoTrabalho();
		valueToString += ", cdItem: " +  getCdItem();
		valueToString += ", cdDocumentoSaida: " +  getCdDocumentoSaida();
		valueToString += ", cdDocumentoEntrada: " +  getCdDocumentoEntrada();
		valueToString += ", tpMovimento: " +  getTpMovimento();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new PlanoTrabalhoItem(getCdPlanoTrabalho(),
			getCdItem(),
			getCdDocumentoSaida(),
			getCdDocumentoEntrada(),
			getTpMovimento());
	}

}