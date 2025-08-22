package com.tivic.manager.mob.funset;

public class FunsetParametrosEntrada {
	private String nrMes;
	private String nrAno;
	private String nrCodigoBancoArrecadador;
	
	public FunsetParametrosEntrada(String nrAno, String nrMes) {
		this.nrAno = nrAno;
		this.nrMes = nrMes;
	}
	
	public String getNrAno() {
		return nrAno;
	}
	
	public void setNrCodigoBancoArrecadador(String nrCodigoBancoArrecadador) {
		this.nrCodigoBancoArrecadador = nrCodigoBancoArrecadador;
	}
	
	public String getNrCodigoBancoArrecadador() {
		return nrCodigoBancoArrecadador;
	}
	
	public String getNrMes() {
		return nrMes;
	}
}
