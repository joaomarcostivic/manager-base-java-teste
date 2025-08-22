package com.tivic.manager.str;

public class TipoVeiculo {

	private int cdTipo;
	private String nmTipo;

	public TipoVeiculo(){ }

	public TipoVeiculo(int cdTipo,
			String nmTipo){
		setCdTipo(cdTipo);
		setNmTipo(nmTipo);
	}
	public void setCdTipo(int cdTipo){
		this.cdTipo=cdTipo;
	}
	public int getCdTipo(){
		return this.cdTipo;
	}
	public void setNmTipo(String nmTipo){
		this.nmTipo=nmTipo;
	}
	public String getNmTipo(){
		return this.nmTipo;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdTipo: " +  getCdTipo();
		valueToString += ", nmTipo: " +  getNmTipo();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new TipoVeiculo(getCdTipo(),
			getNmTipo());
	}

}