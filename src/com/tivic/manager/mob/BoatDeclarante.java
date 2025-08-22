package com.tivic.manager.mob;

public class BoatDeclarante {

	private int cdBoat;
	private int cdDeclarante;
	private String dsDeclaracao;
	private int tpRelacao;

	public BoatDeclarante() { }

	public BoatDeclarante(int cdBoat,
			int cdDeclarante,
			String dsDeclaracao,
			int tpRelacao) {
		setCdBoat(cdBoat);
		setCdDeclarante(cdDeclarante);
		setDsDeclaracao(dsDeclaracao);
		setTpRelacao(tpRelacao);
	}
	public void setCdBoat(int cdBoat){
		this.cdBoat=cdBoat;
	}
	public int getCdBoat(){
		return this.cdBoat;
	}
	public void setCdDeclarante(int cdDeclarante){
		this.cdDeclarante=cdDeclarante;
	}
	public int getCdDeclarante(){
		return this.cdDeclarante;
	}
	public void setDsDeclaracao(String dsDeclaracao){
		this.dsDeclaracao=dsDeclaracao;
	}
	public String getDsDeclaracao(){
		return this.dsDeclaracao;
	}
	public void setTpRelacao(int tpRelacao){
		this.tpRelacao=tpRelacao;
	}
	public int getTpRelacao(){
		return this.tpRelacao;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdBoat: " +  getCdBoat();
		valueToString += ", cdDeclarante: " +  getCdDeclarante();
		valueToString += ", dsDeclaracao: " +  getDsDeclaracao();
		valueToString += ", tpRelacao: " +  getTpRelacao();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new BoatDeclarante(getCdBoat(),
			getCdDeclarante(),
			getDsDeclaracao(),
			getTpRelacao());
	}

}