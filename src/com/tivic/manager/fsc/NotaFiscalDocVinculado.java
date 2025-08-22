package com.tivic.manager.fsc;

public class NotaFiscalDocVinculado {

	private int cdNotaFiscal;
	private int cdDocVinculado;
	private int cdNotaFiscalVinculada;
	private int cdDocumentoSaida;
	private int cdDocumentoEntrada;
	private int tpDocumentoVinculado;

	public NotaFiscalDocVinculado(int cdNotaFiscal,
			int cdDocVinculado,
			int cdNotaFiscalVinculada,
			int cdDocumentoSaida,
			int cdDocumentoEntrada,
			int tpDocumentoVinculado){
		setCdNotaFiscal(cdNotaFiscal);
		setCdDocVinculado(cdDocVinculado);
		setCdNotaFiscalVinculada(cdNotaFiscalVinculada);
		setCdDocumentoSaida(cdDocumentoSaida);
		setCdDocumentoEntrada(cdDocumentoEntrada);
		setTpDocumentoVinculado(tpDocumentoVinculado);
	}
	public void setCdNotaFiscal(int cdNotaFiscal){
		this.cdNotaFiscal=cdNotaFiscal;
	}
	public int getCdNotaFiscal(){
		return this.cdNotaFiscal;
	}
	public void setCdDocVinculado(int cdDocVinculado){
		this.cdDocVinculado=cdDocVinculado;
	}
	public int getCdDocVinculado(){
		return this.cdDocVinculado;
	}
	public void setCdNotaFiscalVinculada(int cdNotaFiscalVinculada){
		this.cdNotaFiscalVinculada=cdNotaFiscalVinculada;
	}
	public int getCdNotaFiscalVinculada(){
		return this.cdNotaFiscalVinculada;
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
	public void setTpDocumentoVinculado(int tpDocumentoVinculado){
		this.tpDocumentoVinculado=tpDocumentoVinculado;
	}
	public int getTpDocumentoVinculado(){
		return this.tpDocumentoVinculado;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdNotaFiscal: " +  getCdNotaFiscal();
		valueToString += ", cdDocVinculado: " +  getCdDocVinculado();
		valueToString += ", cdNotaFiscalVinculada: " +  getCdNotaFiscalVinculada();
		valueToString += ", cdDocumentoSaida: " +  getCdDocumentoSaida();
		valueToString += ", cdDocumentoEntrada: " +  getCdDocumentoEntrada();
		valueToString += ", tpDocumentoVinculado: " +  getTpDocumentoVinculado();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new NotaFiscalDocVinculado(getCdNotaFiscal(),
			getCdDocVinculado(),
			getCdNotaFiscalVinculada(),
			getCdDocumentoSaida(),
			getCdDocumentoEntrada(),
			getTpDocumentoVinculado());
	}

}