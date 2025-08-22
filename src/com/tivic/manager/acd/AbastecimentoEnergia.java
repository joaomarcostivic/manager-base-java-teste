package com.tivic.manager.acd;

public class AbastecimentoEnergia {

	private int cdAbastecimentoEnergia;
	private String nmAbastecimentoEnergia;
	private String idAbastecimentoEnergia;

	public AbastecimentoEnergia(){ }

	public AbastecimentoEnergia(int cdAbastecimentoEnergia,
			String nmAbastecimentoEnergia,
			String idAbastecimentoEnergia){
		setCdAbastecimentoEnergia(cdAbastecimentoEnergia);
		setNmAbastecimentoEnergia(nmAbastecimentoEnergia);
		setIdAbastecimentoEnergia(idAbastecimentoEnergia);
	}
	public void setCdAbastecimentoEnergia(int cdAbastecimentoEnergia){
		this.cdAbastecimentoEnergia=cdAbastecimentoEnergia;
	}
	public int getCdAbastecimentoEnergia(){
		return this.cdAbastecimentoEnergia;
	}
	public void setNmAbastecimentoEnergia(String nmAbastecimentoEnergia){
		this.nmAbastecimentoEnergia=nmAbastecimentoEnergia;
	}
	public String getNmAbastecimentoEnergia(){
		return this.nmAbastecimentoEnergia;
	}
	public void setIdAbastecimentoEnergia(String idAbastecimentoEnergia){
		this.idAbastecimentoEnergia=idAbastecimentoEnergia;
	}
	public String getIdAbastecimentoEnergia(){
		return this.idAbastecimentoEnergia;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdAbastecimentoEnergia: " +  getCdAbastecimentoEnergia();
		valueToString += ", nmAbastecimentoEnergia: " +  getNmAbastecimentoEnergia();
		valueToString += ", idAbastecimentoEnergia: " +  getIdAbastecimentoEnergia();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new AbastecimentoEnergia(getCdAbastecimentoEnergia(),
			getNmAbastecimentoEnergia(),
			getIdAbastecimentoEnergia());
	}

}