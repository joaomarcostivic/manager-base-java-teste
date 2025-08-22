package com.tivic.manager.mob;

public class BoatRelacao {

	private int cdBoat;
	private int cdBoatRelacao;

	public BoatRelacao() { }

	public BoatRelacao(int cdBoat,
			int cdBoatRelacao) {
		setCdBoat(cdBoat);
		setCdBoatRelacao(cdBoatRelacao);
	}
	public void setCdBoat(int cdBoat){
		this.cdBoat=cdBoat;
	}
	public int getCdBoat(){
		return this.cdBoat;
	}
	public void setCdBoatRelacao(int cdBoatRelacao){
		this.cdBoatRelacao=cdBoatRelacao;
	}
	public int getCdBoatRelacao(){
		return this.cdBoatRelacao;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdBoat: " +  getCdBoat();
		valueToString += ", cdBoatRelacao: " +  getCdBoatRelacao();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new BoatRelacao(getCdBoat(),
			getCdBoatRelacao());
	}

}