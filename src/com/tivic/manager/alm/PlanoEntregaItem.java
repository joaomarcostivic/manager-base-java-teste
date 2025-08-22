package com.tivic.manager.alm;

public class PlanoEntregaItem {

	private int cdPlano;
	private int cdDocumentoSaida;
	private int cdDocumentoSaidaConsignada;
	private int cdDocumentoEntrada;

	public PlanoEntregaItem() { }

	public PlanoEntregaItem(int cdPlano,
			int cdDocumentoSaida,
			int cdDocumentoSaidaConsignada,
			int cdDocumentoEntrada) {
		setCdPlano(cdPlano);
		setCdDocumentoSaida(cdDocumentoSaida);
		setCdDocumentoSaidaConsignada(cdDocumentoSaidaConsignada);
		setCdDocumentoEntrada(cdDocumentoEntrada);
	}
	public void setCdPlano(int cdPlano){
		this.cdPlano=cdPlano;
	}
	public int getCdPlano(){
		return this.cdPlano;
	}
	public void setCdDocumentoSaida(int cdDocumentoSaida){
		this.cdDocumentoSaida=cdDocumentoSaida;
	}
	public int getCdDocumentoSaida(){
		return this.cdDocumentoSaida;
	}
	public void setCdDocumentoSaidaConsignada(int cdDocumentoSaidaConsignada){
		this.cdDocumentoSaidaConsignada=cdDocumentoSaidaConsignada;
	}
	public int getCdDocumentoSaidaConsignada(){
		return this.cdDocumentoSaidaConsignada;
	}
	public void setCdDocumentoEntrada(int cdDocumentoEntrada){
		this.cdDocumentoEntrada=cdDocumentoEntrada;
	}
	public int getCdDocumentoEntrada(){
		return this.cdDocumentoEntrada;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdPlano: " +  getCdPlano();
		valueToString += ", cdDocumentoSaida: " +  getCdDocumentoSaida();
		valueToString += ", cdDocumentoSaidaConsignada: " +  getCdDocumentoSaidaConsignada();
		valueToString += ", cdDocumentoEntrada: " +  getCdDocumentoEntrada();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new PlanoEntregaItem(getCdPlano(),
			getCdDocumentoSaida(),
			getCdDocumentoSaidaConsignada(),
			getCdDocumentoEntrada());
	}

}