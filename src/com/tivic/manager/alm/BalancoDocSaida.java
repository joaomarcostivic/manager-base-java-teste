package com.tivic.manager.alm;

public class BalancoDocSaida {

	private int cdBalanco;
	private int cdEmpresa;
	private int cdDocumentoSaida;

	public BalancoDocSaida(int cdBalanco,
			int cdEmpresa,
			int cdDocumentoSaida){
		setCdBalanco(cdBalanco);
		setCdEmpresa(cdEmpresa);
		setCdDocumentoSaida(cdDocumentoSaida);
	}
	public void setCdBalanco(int cdBalanco){
		this.cdBalanco=cdBalanco;
	}
	public int getCdBalanco(){
		return this.cdBalanco;
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
	public String toString() {
		String valueToString = "";
		valueToString += "cdBalanco: " +  getCdBalanco();
		valueToString += ", cdEmpresa: " +  getCdEmpresa();
		valueToString += ", cdDocumentoSaida: " +  getCdDocumentoSaida();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new BalancoDocSaida(getCdBalanco(),
			getCdEmpresa(),
			getCdDocumentoSaida());
	}

}
