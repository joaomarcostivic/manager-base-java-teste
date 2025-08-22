package com.tivic.manager.crm;

public class Regra {

	private int cdRegra;
	private String nmRegra;
	private int tpRegra;

	public Regra(int cdRegra,
			String nmRegra,
			int tpRegra){
		setCdRegra(cdRegra);
		setNmRegra(nmRegra);
		setTpRegra(tpRegra);
	}
	public void setCdRegra(int cdRegra){
		this.cdRegra=cdRegra;
	}
	public int getCdRegra(){
		return this.cdRegra;
	}
	public void setNmRegra(String nmRegra){
		this.nmRegra=nmRegra;
	}
	public String getNmRegra(){
		return this.nmRegra;
	}
	public void setTpRegra(int tpRegra){
		this.tpRegra=tpRegra;
	}
	public int getTpRegra(){
		return this.tpRegra;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdRegra: " +  getCdRegra();
		valueToString += ", tpRegra: " +  getTpRegra();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new Regra(getCdRegra(),
			getNmRegra(),
			getTpRegra());
	}

}
