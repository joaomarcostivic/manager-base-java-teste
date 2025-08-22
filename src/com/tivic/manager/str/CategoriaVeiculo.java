package com.tivic.manager.str;

public class CategoriaVeiculo {

	private int cdCategoria;
	private String nmCategoria;

	public CategoriaVeiculo(){ }

	public CategoriaVeiculo(int cdCategoria,
			String nmCategoria){
		setCdCategoria(cdCategoria);
		setNmCategoria(nmCategoria);
	}
	public void setCdCategoria(int cdCategoria){
		this.cdCategoria=cdCategoria;
	}
	public int getCdCategoria(){
		return this.cdCategoria;
	}
	public void setNmCategoria(String nmCategoria){
		this.nmCategoria=nmCategoria;
	}
	public String getNmCategoria(){
		return this.nmCategoria;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdCategoria: " +  getCdCategoria();
		valueToString += ", nmCategoria: " +  getNmCategoria();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new CategoriaVeiculo(getCdCategoria(),
			getNmCategoria());
	}

}