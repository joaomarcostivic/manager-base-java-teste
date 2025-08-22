package com.tivic.manager.adm;

public class ContaFechamentoTitCred {

	private int cdConta;
	private int cdFechamento;
	private int cdTipoDocumento;
	private int cdTituloCredito;

	public ContaFechamentoTitCred(int cdConta,
			int cdFechamento,
			int cdTipoDocumento,
			int cdTituloCredito){
		setCdConta(cdConta);
		setCdFechamento(cdFechamento);
		setCdTipoDocumento(cdTipoDocumento);
		setCdTituloCredito(cdTituloCredito);
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
	public void setCdTipoDocumento(int cdTipoDocumento){
		this.cdTipoDocumento=cdTipoDocumento;
	}
	public int getCdTipoDocumento(){
		return this.cdTipoDocumento;
	}
	public void setCdTituloCredito(int cdTituloCredito){
		this.cdTituloCredito=cdTituloCredito;
	}
	public int getCdTituloCredito(){
		return this.cdTituloCredito;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdConta: " +  getCdConta();
		valueToString += ", cdFechamento: " +  getCdFechamento();
		valueToString += ", cdTipoDocumento: " +  getCdTipoDocumento();
		valueToString += ", cdTituloCredito: " +  getCdTituloCredito();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new ContaFechamentoTitCred(getCdConta(),
			getCdFechamento(),
			getCdTipoDocumento(),
			getCdTituloCredito());
	}

}
