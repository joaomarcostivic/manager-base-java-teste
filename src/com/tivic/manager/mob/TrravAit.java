package com.tivic.manager.mob;

public class TrravAit {

	private int cdTrrav;
	private int cdAit;
	private int nrTrrav;
	private String nrAit;

	public TrravAit() { }

	public TrravAit(int cdTrrav,
			int cdAit,
			int nrTrrav,
			String nrAit) {
		setCdTrrav(cdTrrav);
		setCdAit(cdAit);
		setNrTrrav(nrTrrav);
		setNrAit(nrAit);
	}
	public void setCdTrrav(int cdTrrav){
		this.cdTrrav=cdTrrav;
	}
	public int getCdTrrav(){
		return this.cdTrrav;
	}
	public void setCdAit(int cdAit){
		this.cdAit=cdAit;
	}
	public int getCdAit(){
		return this.cdAit;
	}
	public void setNrTrrav(int nrTrrav){
		this.nrTrrav=nrTrrav;
	}
	public int getNrTrrav(){
		return this.nrTrrav;
	}
	public void setNrAit(String nrAit){
		this.nrAit=nrAit;
	}
	public String getNrAit(){
		return this.nrAit;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdTrrav: " +  getCdTrrav();
		valueToString += ", cdAit: " +  getCdAit();
		valueToString += ", nrTrrav: " +  getNrTrrav();
		valueToString += ", nrAit: " +  getNrAit();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new TrravAit(getCdTrrav(),
			getCdAit(),
			getNrTrrav(),
			getNrAit());
	}

}