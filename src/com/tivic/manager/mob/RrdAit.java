package com.tivic.manager.mob;

public class RrdAit {

	private int cdAit;
	private int cdRrd;
	private int nrRrd;
	private String nrAit;

	public RrdAit() { }

	public RrdAit(int cdAit,
			int cdRrd,
			int nrRrd,
			String nrAit) {
		setCdAit(cdAit);
		setCdRrd(cdRrd);
		setNrRrd(nrRrd);
		setNrAit(nrAit);
	}
	public void setCdAit(int cdAit){
		this.cdAit=cdAit;
	}
	public int getCdAit(){
		return this.cdAit;
	}
	public void setCdRrd(int cdRrd){
		this.cdRrd=cdRrd;
	}
	public int getCdRrd(){
		return this.cdRrd;
	}
	public void setNrRrd(int nrRrd){
		this.nrRrd=nrRrd;
	}
	public int getNrRrd(){
		return this.nrRrd;
	}
	public void setNrAit(String nrAit){
		this.nrAit=nrAit;
	}
	public String getNrAit(){
		return this.nrAit;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdAit: " +  getCdAit();
		valueToString += ", cdRrd: " +  getCdRrd();
		valueToString += ", nrRrd: " +  getNrRrd();
		valueToString += ", nrAit: " +  getNrAit();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new RrdAit(getCdAit(),
			getCdRrd(),
			getNrRrd(),
			getNrAit());
	}

}