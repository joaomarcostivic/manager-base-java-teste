package com.tivic.manager.prc;

import java.util.GregorianCalendar;

public class Calculo {

	private int cdProcesso;
	private int nrParcela;
	private GregorianCalendar dtParcela;
	private String dsCoeficiente;
	private float vlParcela;
	private float vlCorrigido;
	private float prJuros;

	public Calculo(int cdProcesso,
			int nrParcela,
			GregorianCalendar dtParcela,
			String dsCoeficiente,
			float vlParcela,
			float vlCorrigido,
			float prJuros){
		setCdProcesso(cdProcesso);
		setNrParcela(nrParcela);
		setDtParcela(dtParcela);
		setDsCoeficiente(dsCoeficiente);
		setVlParcela(vlParcela);
		setVlCorrigido(vlCorrigido);
		setPrJuros(prJuros);
	}
	public void setCdProcesso(int cdProcesso){
		this.cdProcesso=cdProcesso;
	}
	public int getCdProcesso(){
		return this.cdProcesso;
	}
	public void setNrParcela(int nrParcela){
		this.nrParcela=nrParcela;
	}
	public int getNrParcela(){
		return this.nrParcela;
	}
	public void setDtParcela(GregorianCalendar dtParcela){
		this.dtParcela=dtParcela;
	}
	public GregorianCalendar getDtParcela(){
		return this.dtParcela;
	}
	public void setDsCoeficiente(String dsCoeficiente){
		this.dsCoeficiente=dsCoeficiente;
	}
	public String getDsCoeficiente(){
		return this.dsCoeficiente;
	}
	public void setVlParcela(float vlParcela){
		this.vlParcela=vlParcela;
	}
	public float getVlParcela(){
		return this.vlParcela;
	}
	public void setVlCorrigido(float vlCorrigido){
		this.vlCorrigido=vlCorrigido;
	}
	public float getVlCorrigido(){
		return this.vlCorrigido;
	}
	public void setPrJuros(float prJuros){
		this.prJuros=prJuros;
	}
	public float getPrJuros(){
		return this.prJuros;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdProcesso: " +  getCdProcesso();
		valueToString += ", nrParcela: " +  getNrParcela();
		valueToString += ", dtParcela: " +  sol.util.Util.formatDateTime(getDtParcela(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", dsCoeficiente: " +  getDsCoeficiente();
		valueToString += ", vlParcela: " +  getVlParcela();
		valueToString += ", vlCorrigido: " +  getVlCorrigido();
		valueToString += ", prJuros: " +  getPrJuros();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new Calculo(cdProcesso,
			nrParcela,
			dtParcela==null ? null : (GregorianCalendar)dtParcela.clone(),
			dsCoeficiente,
			vlParcela,
			vlCorrigido,
			prJuros);
	}

}