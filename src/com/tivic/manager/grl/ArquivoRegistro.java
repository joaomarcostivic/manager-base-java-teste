package com.tivic.manager.grl;

public class ArquivoRegistro {

	private int cdRegistro;
	private int cdArquivo;
	private int cdContaReceber;
	private int cdTipoErro;
	private int cdTipoRegistro;
	private int cdContaPagar;

	public ArquivoRegistro(int cdRegistro,
			int cdArquivo,
			int cdContaReceber,
			int cdTipoErro,
			int cdTipoRegistro,
			int cdContaPagar){
		setCdRegistro(cdRegistro);
		setCdArquivo(cdArquivo);
		setCdContaReceber(cdContaReceber);
		setCdTipoErro(cdTipoErro);
		setCdTipoRegistro(cdTipoRegistro);
		setCdContaPagar(cdContaPagar);
	}
	public void setCdRegistro(int cdRegistro){
		this.cdRegistro=cdRegistro;
	}
	public int getCdRegistro(){
		return this.cdRegistro;
	}
	public void setCdArquivo(int cdArquivo){
		this.cdArquivo=cdArquivo;
	}
	public int getCdArquivo(){
		return this.cdArquivo;
	}
	public void setCdContaReceber(int cdContaReceber){
		this.cdContaReceber=cdContaReceber;
	}
	public int getCdContaReceber(){
		return this.cdContaReceber;
	}
	public void setCdTipoErro(int cdTipoErro){
		this.cdTipoErro=cdTipoErro;
	}
	public int getCdTipoErro(){
		return this.cdTipoErro;
	}
	public void setCdTipoRegistro(int cdTipoRegistro){
		this.cdTipoRegistro=cdTipoRegistro;
	}
	public int getCdTipoRegistro(){
		return this.cdTipoRegistro;
	}
	public void setCdContaPagar(int cdContaPagar){
		this.cdContaPagar=cdContaPagar;
	}
	public int getCdContaPagar(){
		return this.cdContaPagar;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdRegistro: " +  getCdRegistro();
		valueToString += ", cdArquivo: " +  getCdArquivo();
		valueToString += ", cdContaReceber: " +  getCdContaReceber();
		valueToString += ", cdTipoErro: " +  getCdTipoErro();
		valueToString += ", cdTipoRegistro: " +  getCdTipoRegistro();
		valueToString += ", cdContaPagar: " +  getCdContaPagar();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new ArquivoRegistro(getCdRegistro(),
			getCdArquivo(),
			getCdContaReceber(),
			getCdTipoErro(),
			getCdTipoRegistro(),
			getCdContaPagar());
	}

}