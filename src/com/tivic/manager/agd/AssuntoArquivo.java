package com.tivic.manager.agd;

public class AssuntoArquivo {

	private int cdAssunto;
	private int cdArquivo;
	private int cdUsuario;

	public AssuntoArquivo(int cdAssunto,
			int cdArquivo,
			int cdUsuario){
		setCdAssunto(cdAssunto);
		setCdArquivo(cdArquivo);
		setCdUsuario(cdUsuario);
	}
	public void setCdAssunto(int cdAssunto){
		this.cdAssunto=cdAssunto;
	}
	public int getCdAssunto(){
		return this.cdAssunto;
	}
	public void setCdArquivo(int cdArquivo){
		this.cdArquivo=cdArquivo;
	}
	public int getCdArquivo(){
		return this.cdArquivo;
	}
	public void setCdUsuario(int cdUsuario){
		this.cdUsuario=cdUsuario;
	}
	public int getCdUsuario(){
		return this.cdUsuario;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdAssunto: " +  getCdAssunto();
		valueToString += ", cdArquivo: " +  getCdArquivo();
		valueToString += ", cdUsuario: " +  getCdUsuario();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new AssuntoArquivo(getCdAssunto(),
			getCdArquivo(),
			getCdUsuario());
	}

}
