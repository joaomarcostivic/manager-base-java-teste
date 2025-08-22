package com.tivic.manager.grl;

public class Regiao {

	private int cdRegiao;
	private String nmRegiao;
	private int tpRegiao;

	public Regiao() { }
			
	public Regiao(int cdRegiao,
			String nmRegiao,
			int tpRegiao){
		setCdRegiao(cdRegiao);
		setNmRegiao(nmRegiao);
		setTpRegiao(tpRegiao);
	}
	public void setCdRegiao(int cdRegiao){
		this.cdRegiao=cdRegiao;
	}
	public int getCdRegiao(){
		return this.cdRegiao;
	}
	public void setNmRegiao(String nmRegiao){
		this.nmRegiao=nmRegiao;
	}
	public String getNmRegiao(){
		return this.nmRegiao;
	}
	public void setTpRegiao(int tpRegiao){
		this.tpRegiao=tpRegiao;
	}
	public int getTpRegiao(){
		return this.tpRegiao;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdRegiao: " +  getCdRegiao();
		valueToString += ", nmRegiao: " +  getNmRegiao();
		valueToString += ", tpRegiao: " +  getTpRegiao();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new Regiao(getCdRegiao(),
			getNmRegiao(),
			getTpRegiao());
	}

}