package com.tivic.manager.mob;

public class TrravRrd {

	private int cdRrd;
	private int cdTrrav;

	public TrravRrd() { }

	public TrravRrd(int cdRrd,
			int cdTrrav) {
		setCdRrd(cdRrd);
		setCdTrrav(cdTrrav);
	}
	public void setCdRrd(int cdRrd){
		this.cdRrd=cdRrd;
	}
	public int getCdRrd(){
		return this.cdRrd;
	}
	public void setCdTrrav(int cdTrrav){
		this.cdTrrav=cdTrrav;
	}
	public int getCdTrrav(){
		return this.cdTrrav;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdRrd: " +  getCdRrd();
		valueToString += ", cdTrrav: " +  getCdTrrav();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new TrravRrd(getCdRrd(),
			getCdTrrav());
	}

}