package com.tivic.manager.mob;

public class VistoriaPlanoItemArquivo {

	private int cdVistoriaPlanoItem;
	private int cdArquivo;

	public VistoriaPlanoItemArquivo(){ }

	public VistoriaPlanoItemArquivo(int cdVistoriaPlanoItem,
			int cdArquivo){
		setCdVistoriaPlanoItem(cdVistoriaPlanoItem);
		setCdArquivo(cdArquivo);
	}
	public void setCdVistoriaPlanoItem(int cdVistoriaPlanoItem){
		this.cdVistoriaPlanoItem=cdVistoriaPlanoItem;
	}
	public int getCdVistoriaPlanoItem(){
		return this.cdVistoriaPlanoItem;
	}
	public void setCdArquivo(int cdArquivo){
		this.cdArquivo=cdArquivo;
	}
	public int getCdArquivo(){
		return this.cdArquivo;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdVistoriaPlanoItem: " +  getCdVistoriaPlanoItem();
		valueToString += ", cdArquivo: " +  getCdArquivo();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new VistoriaPlanoItemArquivo(getCdVistoriaPlanoItem(),
			getCdArquivo());
	}

}