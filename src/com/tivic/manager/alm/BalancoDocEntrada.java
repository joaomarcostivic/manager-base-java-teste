package com.tivic.manager.alm;

public class BalancoDocEntrada {

	private int cdBalanco;
	private int cdEmpresa;
	private int cdDocumentoEntrada;

	public BalancoDocEntrada(int cdBalanco,
			int cdEmpresa,
			int cdDocumentoEntrada){
		setCdBalanco(cdBalanco);
		setCdEmpresa(cdEmpresa);
		setCdDocumentoEntrada(cdDocumentoEntrada);
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
	public void setCdDocumentoEntrada(int cdDocumentoEntrada){
		this.cdDocumentoEntrada=cdDocumentoEntrada;
	}
	public int getCdDocumentoEntrada(){
		return this.cdDocumentoEntrada;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdBalanco: " +  getCdBalanco();
		valueToString += ", cdEmpresa: " +  getCdEmpresa();
		valueToString += ", cdDocumentoEntrada: " +  getCdDocumentoEntrada();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new BalancoDocEntrada(getCdBalanco(),
			getCdEmpresa(),
			getCdDocumentoEntrada());
	}

}
