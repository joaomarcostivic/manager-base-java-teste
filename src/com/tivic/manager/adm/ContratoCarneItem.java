package com.tivic.manager.adm;

public class ContratoCarneItem {

	private int cdContrato;
	private int cdCarne;
	private int cdContaReceber;

	public ContratoCarneItem(int cdContrato,
			int cdCarne,
			int cdContaReceber){
		setCdContrato(cdContrato);
		setCdCarne(cdCarne);
		setCdContaReceber(cdContaReceber);
	}
	public void setCdContrato(int cdContrato){
		this.cdContrato=cdContrato;
	}
	public int getCdContrato(){
		return this.cdContrato;
	}
	public void setCdCarne(int cdCarne){
		this.cdCarne=cdCarne;
	}
	public int getCdCarne(){
		return this.cdCarne;
	}
	public void setCdContaReceber(int cdContaReceber){
		this.cdContaReceber=cdContaReceber;
	}
	public int getCdContaReceber(){
		return this.cdContaReceber;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdContrato: " +  getCdContrato();
		valueToString += ", cdCarne: " +  getCdCarne();
		valueToString += ", cdContaReceber: " +  getCdContaReceber();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new ContratoCarneItem(getCdContrato(),
			getCdCarne(),
			getCdContaReceber());
	}

}
