package com.tivic.manager.fta;

public class TrechoRota {

	private int cdTrecho;
	private int cdRota;
	private String nmTrecho;
	private float qtDistanciaOrigem;
	private float qtDistanciaTrecho;
	private int tpPavimento;
	private int cdCidadeParada;

	public TrechoRota(int cdTrecho,
			int cdRota,
			String nmTrecho,
			float qtDistanciaOrigem,
			float qtDistanciaTrecho,
			int tpPavimento,
			int cdCidadeParada){
		setCdTrecho(cdTrecho);
		setCdRota(cdRota);
		setNmTrecho(nmTrecho);
		setQtDistanciaOrigem(qtDistanciaOrigem);
		setQtDistanciaTrecho(qtDistanciaTrecho);
		setTpPavimento(tpPavimento);
		setCdCidadeParada(cdCidadeParada);
	}
	public void setCdTrecho(int cdTrecho){
		this.cdTrecho=cdTrecho;
	}
	public int getCdTrecho(){
		return this.cdTrecho;
	}
	public void setCdRota(int cdRota){
		this.cdRota=cdRota;
	}
	public int getCdRota(){
		return this.cdRota;
	}
	public void setNmTrecho(String nmTrecho){
		this.nmTrecho=nmTrecho;
	}
	public String getNmTrecho(){
		return this.nmTrecho;
	}
	public void setQtDistanciaOrigem(float qtDistanciaOrigem){
		this.qtDistanciaOrigem=qtDistanciaOrigem;
	}
	public float getQtDistanciaOrigem(){
		return this.qtDistanciaOrigem;
	}
	public void setQtDistanciaTrecho(float qtDistanciaTrecho){
		this.qtDistanciaTrecho=qtDistanciaTrecho;
	}
	public float getQtDistanciaTrecho(){
		return this.qtDistanciaTrecho;
	}
	public void setTpPavimento(int tpPavimento){
		this.tpPavimento=tpPavimento;
	}
	public int getTpPavimento(){
		return this.tpPavimento;
	}
	public void setCdCidadeParada(int cdCidadeParada){
		this.cdCidadeParada=cdCidadeParada;
	}
	public int getCdCidadeParada(){
		return this.cdCidadeParada;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdTrecho: " +  getCdTrecho();
		valueToString += ", cdRota: " +  getCdRota();
		valueToString += ", nmTrecho: " +  getNmTrecho();
		valueToString += ", qtDistanciaOrigem: " +  getQtDistanciaOrigem();
		valueToString += ", qtDistanciaTrecho: " +  getQtDistanciaTrecho();
		valueToString += ", tpPavimento: " +  getTpPavimento();
		valueToString += ", cdCidadeParada: " +  getCdCidadeParada();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new TrechoRota(getCdTrecho(),
			getCdRota(),
			getNmTrecho(),
			getQtDistanciaOrigem(),
			getQtDistanciaTrecho(),
			getTpPavimento(),
			getCdCidadeParada());
	}

}