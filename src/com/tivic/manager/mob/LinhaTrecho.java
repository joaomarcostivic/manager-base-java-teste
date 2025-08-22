package com.tivic.manager.mob;

public class LinhaTrecho {

	private int cdLinha;
	private int cdRota;
	private int cdTrecho;
	private int cdTrechoAnterior;
	private int cdParada;
	private int cdGeorreferencia;
	private Double qtKm;

	public LinhaTrecho() { }

	public LinhaTrecho(int cdLinha,
			int cdRota,
			int cdTrecho,
			int cdTrechoAnterior,
			int cdParada,
			int cdGeorreferencia,
			Double qtKm) {
		setCdLinha(cdLinha);
		setCdRota(cdRota);
		setCdTrecho(cdTrecho);
		setCdTrechoAnterior(cdTrechoAnterior);
		setCdParada(cdParada);
		setCdGeorreferencia(cdGeorreferencia);
		setQtKm(qtKm);
	}
	public void setCdLinha(int cdLinha){
		this.cdLinha=cdLinha;
	}
	public int getCdLinha(){
		return this.cdLinha;
	}
	public void setCdRota(int cdRota){
		this.cdRota=cdRota;
	}
	public int getCdRota(){
		return this.cdRota;
	}
	public void setCdTrecho(int cdTrecho){
		this.cdTrecho=cdTrecho;
	}
	public int getCdTrecho(){
		return this.cdTrecho;
	}
	public void setCdTrechoAnterior(int cdTrechoAnterior){
		this.cdTrechoAnterior=cdTrechoAnterior;
	}
	public int getCdTrechoAnterior(){
		return this.cdTrechoAnterior;
	}
	public void setCdParada(int cdParada){
		this.cdParada=cdParada;
	}
	public int getCdParada(){
		return this.cdParada;
	}
	public void setCdGeorreferencia(int cdGeorreferencia){
		this.cdGeorreferencia=cdGeorreferencia;
	}
	public int getCdGeorreferencia(){
		return this.cdGeorreferencia;
	}
	public void setQtKm(Double qtKm){
		this.qtKm=qtKm;
	}
	public Double getQtKm(){
		return this.qtKm;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdLinha: " +  getCdLinha();
		valueToString += ", cdRota: " +  getCdRota();
		valueToString += ", cdTrecho: " +  getCdTrecho();
		valueToString += ", cdTrechoAnterior: " +  getCdTrechoAnterior();
		valueToString += ", cdParada: " +  getCdParada();
		valueToString += ", cdGeorreferencia: " +  getCdGeorreferencia();
		valueToString += ", qtKm: " +  getQtKm();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new LinhaTrecho(getCdLinha(),
			getCdRota(),
			getCdTrecho(),
			getCdTrechoAnterior(),
			getCdParada(),
			getCdGeorreferencia(),
			getQtKm());
	}
	
}