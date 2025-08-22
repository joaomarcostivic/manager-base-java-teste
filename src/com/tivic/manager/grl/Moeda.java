package com.tivic.manager.grl;

public class Moeda {

	private int cdMoeda;
	private int cdIndicador;
	private String nmMoeda;
	private String sgMoeda;
	private String idMoeda;
	private int lgAtivo;

	public Moeda(){ }

	public Moeda(int cdMoeda,
			int cdIndicador,
			String nmMoeda,
			String sgMoeda,
			String idMoeda,
			int lgAtivo){
		setCdMoeda(cdMoeda);
		setCdIndicador(cdIndicador);
		setNmMoeda(nmMoeda);
		setSgMoeda(sgMoeda);
		setIdMoeda(idMoeda);
		setLgAtivo(lgAtivo);
	}
	public void setCdMoeda(int cdMoeda){
		this.cdMoeda=cdMoeda;
	}
	public int getCdMoeda(){
		return this.cdMoeda;
	}
	public void setCdIndicador(int cdIndicador){
		this.cdIndicador=cdIndicador;
	}
	public int getCdIndicador(){
		return this.cdIndicador;
	}
	public void setNmMoeda(String nmMoeda){
		this.nmMoeda=nmMoeda;
	}
	public String getNmMoeda(){
		return this.nmMoeda;
	}
	public void setSgMoeda(String sgMoeda){
		this.sgMoeda=sgMoeda;
	}
	public String getSgMoeda(){
		return this.sgMoeda;
	}
	public void setIdMoeda(String idMoeda){
		this.idMoeda=idMoeda;
	}
	public String getIdMoeda(){
		return this.idMoeda;
	}
	public void setLgAtivo(int lgAtivo){
		this.lgAtivo=lgAtivo;
	}
	public int getLgAtivo(){
		return this.lgAtivo;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdMoeda: " +  getCdMoeda();
		valueToString += ", cdIndicador: " +  getCdIndicador();
		valueToString += ", nmMoeda: " +  getNmMoeda();
		valueToString += ", sgMoeda: " +  getSgMoeda();
		valueToString += ", idMoeda: " +  getIdMoeda();
		valueToString += ", lgAtivo: " +  getLgAtivo();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new Moeda(getCdMoeda(),
			getCdIndicador(),
			getNmMoeda(),
			getSgMoeda(),
			getIdMoeda(),
			getLgAtivo());
	}

}
