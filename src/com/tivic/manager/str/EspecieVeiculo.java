package com.tivic.manager.str;

public class EspecieVeiculo {

	private int cdEspecie;
	private String dsEspecie;

	public EspecieVeiculo(){ }

	public EspecieVeiculo(int cdEspecie,
			String dsEspecie){
		setCdEspecie(cdEspecie);
		setDsEspecie(dsEspecie);
	}
	public void setCdEspecie(int cdEspecie){
		this.cdEspecie=cdEspecie;
	}
	public int getCdEspecie(){
		return this.cdEspecie;
	}
	public void setDsEspecie(String dsEspecie){
		this.dsEspecie=dsEspecie;
	}
	public String getDsEspecie(){
		return this.dsEspecie;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdEspecie: " +  getCdEspecie();
		valueToString += ", dsEspecie: " +  getDsEspecie();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new EspecieVeiculo(getCdEspecie(),
			getDsEspecie());
	}

}