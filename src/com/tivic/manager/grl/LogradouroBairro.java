package com.tivic.manager.grl;

public class LogradouroBairro {

	private int cdBairro;
	private int cdLogradouro;

	public LogradouroBairro() { }
			
	public LogradouroBairro(int cdBairro,
			int cdLogradouro){
		setCdBairro(cdBairro);
		setCdLogradouro(cdLogradouro);
	}
	public void setCdBairro(int cdBairro){
		this.cdBairro=cdBairro;
	}
	public int getCdBairro(){
		return this.cdBairro;
	}
	public void setCdLogradouro(int cdLogradouro){
		this.cdLogradouro=cdLogradouro;
	}
	public int getCdLogradouro(){
		return this.cdLogradouro;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdBairro: " +  getCdBairro();
		valueToString += ", cdLogradouro: " +  getCdLogradouro();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new LogradouroBairro(getCdBairro(),
			getCdLogradouro());
	}

}
