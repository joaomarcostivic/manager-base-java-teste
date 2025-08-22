package com.tivic.manager.str;

public class Cor {

	private int cdCor;
	private String nmCor;

	public Cor(){ }

	public Cor(int cdCor,
			String nmCor){
		setCdCor(cdCor);
		setNmCor(nmCor);
	}
	public void setCdCor(int cdCor){
		this.cdCor=cdCor;
	}
	public int getCdCor(){
		return this.cdCor;
	}
	public void setNmCor(String nmCor){
		this.nmCor=nmCor;
	}
	public String getNmCor(){
		return this.nmCor;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdCor: " +  getCdCor();
		valueToString += ", nmCor: " +  getNmCor();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new Cor(getCdCor(),
			getNmCor());
	}

}