package com.tivic.manager.grl;

public class Alergia {

	private int cdAlergia;
	private String nmAlergia;
	private String idAlergia;
	private int tpAlergia;

	public Alergia(){ }

	public Alergia(int cdAlergia,
			String nmAlergia,
			String idAlergia,
			int tpAlergia){
		setCdAlergia(cdAlergia);
		setNmAlergia(nmAlergia);
		setIdAlergia(idAlergia);
		setTpAlergia(tpAlergia);
	}
	public void setCdAlergia(int cdAlergia){
		this.cdAlergia=cdAlergia;
	}
	public int getCdAlergia(){
		return this.cdAlergia;
	}
	public void setNmAlergia(String nmAlergia){
		this.nmAlergia=nmAlergia;
	}
	public String getNmAlergia(){
		return this.nmAlergia;
	}
	public void setIdAlergia(String idAlergia){
		this.idAlergia=idAlergia;
	}
	public String getIdAlergia(){
		return this.idAlergia;
	}
	public void setTpAlergia(int tpAlergia){
		this.tpAlergia=tpAlergia;
	}
	public int getTpAlergia(){
		return this.tpAlergia;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdAlergia: " +  getCdAlergia();
		valueToString += ", nmAlergia: " +  getNmAlergia();
		valueToString += ", idAlergia: " +  getIdAlergia();
		valueToString += ", tpAlergia: " +  getTpAlergia();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new Alergia(getCdAlergia(),
			getNmAlergia(),
			getIdAlergia(),
			getTpAlergia());
	}

}