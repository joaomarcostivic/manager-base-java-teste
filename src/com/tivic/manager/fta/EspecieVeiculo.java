package com.tivic.manager.fta;

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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + cdEspecie;
		result = prime * result + ((dsEspecie == null) ? 0 : dsEspecie.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		EspecieVeiculo other = (EspecieVeiculo) obj;
		if (cdEspecie != other.cdEspecie)
			return false;
		if (dsEspecie == null) {
			if (other.dsEspecie != null)
				return false;
		} else if (!dsEspecie.equals(other.dsEspecie))
			return false;
		return true;
	}
	
	

}