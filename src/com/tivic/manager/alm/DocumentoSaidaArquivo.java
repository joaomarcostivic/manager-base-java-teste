package com.tivic.manager.alm;

public class DocumentoSaidaArquivo {

	private int cdDocumentoSaida;
	private int cdArquivo;
	private int stDocumentoSaidaArquivo;

	public DocumentoSaidaArquivo(int cdDocumentoSaida,
			int cdArquivo,
			int stDocumentoSaidaArquivo){
		setCdDocumentoSaida(cdDocumentoSaida);
		setCdArquivo(cdArquivo);
		setStDocumentoSaidaArquivo(stDocumentoSaidaArquivo);
	}
	public void setCdDocumentoSaida(int cdDocumentoSaida){
		this.cdDocumentoSaida=cdDocumentoSaida;
	}
	public int getCdDocumentoSaida(){
		return this.cdDocumentoSaida;
	}
	public void setCdArquivo(int cdArquivo){
		this.cdArquivo=cdArquivo;
	}
	public int getCdArquivo(){
		return this.cdArquivo;
	}
	public void setStDocumentoSaidaArquivo(int stDocumentoSaidaArquivo) {
		this.stDocumentoSaidaArquivo = stDocumentoSaidaArquivo;
	}
	public int getStDocumentoSaidaArquivo() {
		return stDocumentoSaidaArquivo;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdDocumentoSaida: " +  getCdDocumentoSaida();
		valueToString += ", cdArquivo: " +  getCdArquivo();
		valueToString += ", stDocumentoSaidaArquivo: " +  getStDocumentoSaidaArquivo();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new DocumentoSaidaArquivo(getCdDocumentoSaida(),
			getCdArquivo(),
			getStDocumentoSaidaArquivo());
	}

}