package com.tivic.manager.mob;

public class BoatRelatorioAvaria {

	private int cdVistoria;
	private int cdVeiculo;
	private int cdBoat;

	public BoatRelatorioAvaria() { }

	public BoatRelatorioAvaria(int cdVistoria,
			int cdVeiculo,
			int cdBoat) {
		setCdVistoria(cdVistoria);
		setCdVeiculo(cdVeiculo);
		setCdBoat(cdBoat);
	}
	public void setCdVistoria(int cdVistoria){
		this.cdVistoria=cdVistoria;
	}
	public int getCdVistoria(){
		return this.cdVistoria;
	}
	public void setCdVeiculo(int cdVeiculo){
		this.cdVeiculo=cdVeiculo;
	}
	public int getCdVeiculo(){
		return this.cdVeiculo;
	}
	public void setCdBoat(int cdBoat){
		this.cdBoat=cdBoat;
	}
	public int getCdBoat(){
		return this.cdBoat;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdVistoria: " +  getCdVistoria();
		valueToString += ", cdVeiculo: " +  getCdVeiculo();
		valueToString += ", cdBoat: " +  getCdBoat();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new BoatRelatorioAvaria(getCdVistoria(),
			getCdVeiculo(),
			getCdBoat());
	}

}