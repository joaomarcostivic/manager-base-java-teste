package com.tivic.manager.adm;

public class ContaPagarArquivo {

	private int cdContaPagar;
	private int cdArquivo;

	public ContaPagarArquivo(){ }

	public ContaPagarArquivo(int cdContaPagar,
			int cdArquivo){
		setCdContaPagar(cdContaPagar);
		setCdArquivo(cdArquivo);
	}
	public void setCdContaPagar(int cdContaPagar){
		this.cdContaPagar=cdContaPagar;
	}
	public int getCdContaPagar(){
		return this.cdContaPagar;
	}
	public void setCdArquivo(int cdArquivo){
		this.cdArquivo=cdArquivo;
	}
	public int getCdArquivo(){
		return this.cdArquivo;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdContaPagar: " +  getCdContaPagar();
		valueToString += ", cdArquivo: " +  getCdArquivo();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new ContaPagarArquivo(getCdContaPagar(),
			getCdArquivo());
	}

}