package com.tivic.manager.ord;

public class Defeito {

	private int cdDefeito;
	private String nmDefeito;
	private String txtDefeito;
	private int nrHorasPrevisaoReparo;
	private int lgAtivo;
	private String idDefeito;

	public Defeito(){ }

	public Defeito(int cdDefeito,
			String nmDefeito,
			String txtDefeito,
			int nrHorasPrevisaoReparo,
			int lgAtivo,
			String idDefeito){
		setCdDefeito(cdDefeito);
		setNmDefeito(nmDefeito);
		setTxtDefeito(txtDefeito);
		setNrHorasPrevisaoReparo(nrHorasPrevisaoReparo);
		setLgAtivo(lgAtivo);
		setIdDefeito(idDefeito);
	}
	public void setCdDefeito(int cdDefeito){
		this.cdDefeito=cdDefeito;
	}
	public int getCdDefeito(){
		return this.cdDefeito;
	}
	public void setNmDefeito(String nmDefeito){
		this.nmDefeito=nmDefeito;
	}
	public String getNmDefeito(){
		return this.nmDefeito;
	}
	public void setTxtDefeito(String txtDefeito){
		this.txtDefeito=txtDefeito;
	}
	public String getTxtDefeito(){
		return this.txtDefeito;
	}
	public void setNrHorasPrevisaoReparo(int nrHorasPrevisaoReparo){
		this.nrHorasPrevisaoReparo=nrHorasPrevisaoReparo;
	}
	public int getNrHorasPrevisaoReparo(){
		return this.nrHorasPrevisaoReparo;
	}
	public void setLgAtivo(int lgAtivo){
		this.lgAtivo=lgAtivo;
	}
	public int getLgAtivo(){
		return this.lgAtivo;
	}
	public void setIdDefeito(String idDefeito){
		this.idDefeito=idDefeito;
	}
	public String getIdDefeito(){
		return this.idDefeito;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdDefeito: " +  getCdDefeito();
		valueToString += ", nmDefeito: " +  getNmDefeito();
		valueToString += ", txtDefeito: " +  getTxtDefeito();
		valueToString += ", nrHorasPrevisaoReparo: " +  getNrHorasPrevisaoReparo();
		valueToString += ", lgAtivo: " +  getLgAtivo();
		valueToString += ", idDefeito: " +  getIdDefeito();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new Defeito(getCdDefeito(),
			getNmDefeito(),
			getTxtDefeito(),
			getNrHorasPrevisaoReparo(),
			getLgAtivo(),
			getIdDefeito());
	}

}
