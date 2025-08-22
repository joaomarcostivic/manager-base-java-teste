package com.tivic.manager.crm;

public class ContratoFidelidadeMov {

	private int cdContrato;
	private int cdMovimento;

	public ContratoFidelidadeMov(int cdContrato,
			int cdMovimento){
		setCdContrato(cdContrato);
		setCdMovimento(cdMovimento);
	}
	public void setCdContrato(int cdContrato){
		this.cdContrato=cdContrato;
	}
	public int getCdContrato(){
		return this.cdContrato;
	}
	public void setCdMovimento(int cdMovimento){
		this.cdMovimento=cdMovimento;
	}
	public int getCdMovimento(){
		return this.cdMovimento;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdContrato: " +  getCdContrato();
		valueToString += ", cdMovimento: " +  getCdMovimento();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new ContratoFidelidadeMov(getCdContrato(),
			getCdMovimento());
	}

}
