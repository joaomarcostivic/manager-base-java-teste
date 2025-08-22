package com.tivic.manager.grl;

public class Doenca {

	private int cdDoenca;
	private String nmDoenca;
	private String idDoenca;
	private int tpDoenca;

	public Doenca(){ }

	public Doenca(int cdDoenca,
			String nmDoenca,
			String idDoenca,
			int tpDoenca){
		setCdDoenca(cdDoenca);
		setNmDoenca(nmDoenca);
		setIdDoenca(idDoenca);
		setTpDoenca(tpDoenca);
	}
	public void setCdDoenca(int cdDoenca){
		this.cdDoenca=cdDoenca;
	}
	public int getCdDoenca(){
		return this.cdDoenca;
	}
	public void setNmDoenca(String nmDoenca){
		this.nmDoenca=nmDoenca;
	}
	public String getNmDoenca(){
		return this.nmDoenca;
	}
	public void setIdDoenca(String idDoenca){
		this.idDoenca=idDoenca;
	}
	public String getIdDoenca(){
		return this.idDoenca;
	}
	public void setTpDoenca(int tpDoenca){
		this.tpDoenca=tpDoenca;
	}
	public int getTpDoenca(){
		return this.tpDoenca;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdDoenca: " +  getCdDoenca();
		valueToString += ", nmDoenca: " +  getNmDoenca();
		valueToString += ", idDoenca: " +  getIdDoenca();
		valueToString += ", tpDoenca: " +  getTpDoenca();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new Doenca(getCdDoenca(),
			getNmDoenca(),
			getIdDoenca(),
			getTpDoenca());
	}

}