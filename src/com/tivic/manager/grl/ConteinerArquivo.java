package com.tivic.manager.grl;

public class ConteinerArquivo {

	private int cdConteiner;
	private int cdArquivo;

	public ConteinerArquivo(int cdConteiner,
			int cdArquivo){
		setCdConteiner(cdConteiner);
		setCdArquivo(cdArquivo);
	}
	public void setCdConteiner(int cdConteiner){
		this.cdConteiner=cdConteiner;
	}
	public int getCdConteiner(){
		return this.cdConteiner;
	}
	public void setCdArquivo(int cdArquivo){
		this.cdArquivo=cdArquivo;
	}
	public int getCdArquivo(){
		return this.cdArquivo;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdConteiner: " +  getCdConteiner();
		valueToString += ", cdArquivo: " +  getCdArquivo();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new ConteinerArquivo(getCdConteiner(),
			getCdArquivo());
	}

}
