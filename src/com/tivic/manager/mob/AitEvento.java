package com.tivic.manager.mob;

public class AitEvento {

	private int cdAit;
	private int cdEvento;

	public AitEvento() { }

	public AitEvento(int cdAit,
			int cdEvento) {
		setCdAit(cdAit);
		setCdEvento(cdEvento);
	}
	public void setCdAit(int cdAit){
		this.cdAit=cdAit;
	}
	public int getCdAit(){
		return this.cdAit;
	}
	public void setCdEvento(int cdEvento){
		this.cdEvento=cdEvento;
	}
	public int getCdEvento(){
		return this.cdEvento;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdAit: " +  getCdAit();
		valueToString += ", cdEvento: " +  getCdEvento();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new AitEvento(getCdAit(),
			getCdEvento());
	}

}