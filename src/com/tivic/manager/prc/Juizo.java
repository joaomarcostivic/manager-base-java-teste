package com.tivic.manager.prc;

public class Juizo {

	private int cdJuizo;
	private String nmJuizo;
	private int tpJuizo;
	private String idJuizo;
	private String nrJuizo;
	private String sgJuizo;
	private int tpInstancia;

	public Juizo(){ }

	public Juizo(int cdJuizo,
			String nmJuizo,
			int tpJuizo,
			String idJuizo,
			String nrJuizo,
			String sgJuizo,
			int tpInstancia){
		setCdJuizo(cdJuizo);
		setNmJuizo(nmJuizo);
		setTpJuizo(tpJuizo);
		setIdJuizo(idJuizo);
		setNrJuizo(nrJuizo);
		setSgJuizo(sgJuizo);
		setTpInstancia(tpInstancia);
	}
	public void setCdJuizo(int cdJuizo){
		this.cdJuizo=cdJuizo;
	}
	public int getCdJuizo(){
		return this.cdJuizo;
	}
	public void setNmJuizo(String nmJuizo){
		this.nmJuizo=nmJuizo;
	}
	public String getNmJuizo(){
		return this.nmJuizo;
	}
	public void setTpJuizo(int tpJuizo){
		this.tpJuizo=tpJuizo;
	}
	public int getTpJuizo(){
		return this.tpJuizo;
	}
	public void setIdJuizo(String idJuizo){
		this.idJuizo=idJuizo;
	}
	public String getIdJuizo(){
		return this.idJuizo;
	}
	public void setNrJuizo(String nrJuizo){
		this.nrJuizo=nrJuizo;
	}
	public String getNrJuizo(){
		return this.nrJuizo;
	}
	public void setSgJuizo(String sgJuizo){
		this.sgJuizo=sgJuizo;
	}
	public String getSgJuizo(){
		return this.sgJuizo;
	}
	public void setTpInstancia(int tpInstancia){
		this.tpInstancia=tpInstancia;
	}
	public int getTpInstancia(){
		return this.tpInstancia;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdJuizo: " +  getCdJuizo();
		valueToString += ", nmJuizo: " +  getNmJuizo();
		valueToString += ", tpJuizo: " +  getTpJuizo();
		valueToString += ", idJuizo: " +  getIdJuizo();
		valueToString += ", nrJuizo: " +  getNrJuizo();
		valueToString += ", sgJuizo: " +  getSgJuizo();
		valueToString += ", tpInstancia: " +  getTpInstancia();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new Juizo(getCdJuizo(),
			getNmJuizo(),
			getTpJuizo(),
			getIdJuizo(),
			getNrJuizo(),
			getSgJuizo(),
			getTpInstancia());
	}

}