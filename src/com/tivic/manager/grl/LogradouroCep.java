package com.tivic.manager.grl;

public class LogradouroCep {

	private int cdLogradouro;
	private String nrCep;
	private String nrEnderecoInicial;
	private String nrEnderecoFinal;

	public LogradouroCep(int cdLogradouro,
			String nrCep,
			String nrEnderecoInicial,
			String nrEnderecoFinal){
		setCdLogradouro(cdLogradouro);
		setNrCep(nrCep);
		setNrEnderecoInicial(nrEnderecoInicial);
		setNrEnderecoFinal(nrEnderecoFinal);
	}
	public void setCdLogradouro(int cdLogradouro){
		this.cdLogradouro=cdLogradouro;
	}
	public int getCdLogradouro(){
		return this.cdLogradouro;
	}
	public void setNrCep(String nrCep){
		this.nrCep=nrCep;
	}
	public String getNrCep(){
		return this.nrCep;
	}
	public void setNrEnderecoInicial(String nrEnderecoInicial){
		this.nrEnderecoInicial=nrEnderecoInicial;
	}
	public String getNrEnderecoInicial(){
		return this.nrEnderecoInicial;
	}
	public void setNrEnderecoFinal(String nrEnderecoFinal){
		this.nrEnderecoFinal=nrEnderecoFinal;
	}
	public String getNrEnderecoFinal(){
		return this.nrEnderecoFinal;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdLogradouro: " +  getCdLogradouro();
		valueToString += ", nrCep: " +  getNrCep();
		valueToString += ", nrEnderecoInicial: " +  getNrEnderecoInicial();
		valueToString += ", nrEnderecoFinal: " +  getNrEnderecoFinal();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new LogradouroCep(getCdLogradouro(),
			getNrCep(),
			getNrEnderecoInicial(),
			getNrEnderecoFinal());
	}

}
