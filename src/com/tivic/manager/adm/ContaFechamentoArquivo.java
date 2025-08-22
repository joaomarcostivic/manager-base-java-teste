package com.tivic.manager.adm;

public class ContaFechamentoArquivo {

	private int cdConta;
	private int cdFechamento;
	private int cdArquivo;

	public ContaFechamentoArquivo() { }

	public ContaFechamentoArquivo(int cdConta,
			int cdFechamento,
			int cdArquivo) {
		setCdConta(cdConta);
		setCdFechamento(cdFechamento);
		setCdArquivo(cdArquivo);
	}
	public void setCdConta(int cdConta){
		this.cdConta=cdConta;
	}
	public int getCdConta(){
		return this.cdConta;
	}
	public void setCdFechamento(int cdFechamento){
		this.cdFechamento=cdFechamento;
	}
	public int getCdFechamento(){
		return this.cdFechamento;
	}
	public void setCdArquivo(int cdArquivo){
		this.cdArquivo=cdArquivo;
	}
	public int getCdArquivo(){
		return this.cdArquivo;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdConta: " +  getCdConta();
		valueToString += ", cdFechamento: " +  getCdFechamento();
		valueToString += ", cdArquivo: " +  getCdArquivo();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new ContaFechamentoArquivo(getCdConta(),
			getCdFechamento(),
			getCdArquivo());
	}

}