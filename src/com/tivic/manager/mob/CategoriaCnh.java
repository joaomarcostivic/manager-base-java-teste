package com.tivic.manager.mob;

public class CategoriaCnh {

	private int cdCategoriaCnh;
	private String nmCategoria;

	public CategoriaCnh() { }

	public CategoriaCnh(int cdCategoriaCnh,
			String nmCategoria) {
		setCdCategoriaCnh(cdCategoriaCnh);
		setNmCategoria(nmCategoria);
	}
	public void setCdCategoriaCnh(int cdCategoriaCnh){
		this.cdCategoriaCnh=cdCategoriaCnh;
	}
	public int getCdCategoriaCnh(){
		return this.cdCategoriaCnh;
	}
	public void setNmCategoria(String nmCategoria){
		this.nmCategoria=nmCategoria;
	}
	public String getNmCategoria(){
		return this.nmCategoria;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdCategoriaCnh: " +  getCdCategoriaCnh();
		valueToString += ", nmCategoria: " +  getNmCategoria();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new CategoriaCnh(getCdCategoriaCnh(),
			getNmCategoria());
	}

}