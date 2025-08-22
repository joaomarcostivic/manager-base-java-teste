package com.tivic.manager.srh;

public class CategoriaFgts {

	private int cdCategoriaFgts;
	private String nmCategoriaFgts;
	private String idCategoriaFgts;

	public CategoriaFgts(){ }

	public CategoriaFgts(int cdCategoriaFgts,
			String nmCategoriaFgts,
			String idCategoriaFgts){
		setCdCategoriaFgts(cdCategoriaFgts);
		setNmCategoriaFgts(nmCategoriaFgts);
		setIdCategoriaFgts(idCategoriaFgts);
	}
	public void setCdCategoriaFgts(int cdCategoriaFgts){
		this.cdCategoriaFgts=cdCategoriaFgts;
	}
	public int getCdCategoriaFgts(){
		return this.cdCategoriaFgts;
	}
	public void setNmCategoriaFgts(String nmCategoriaFgts){
		this.nmCategoriaFgts=nmCategoriaFgts;
	}
	public String getNmCategoriaFgts(){
		return this.nmCategoriaFgts;
	}
	public void setIdCategoriaFgts(String idCategoriaFgts){
		this.idCategoriaFgts=idCategoriaFgts;
	}
	public String getIdCategoriaFgts(){
		return this.idCategoriaFgts;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdCategoriaFgts: " +  getCdCategoriaFgts();
		valueToString += ", nmCategoriaFgts: " +  getNmCategoriaFgts();
		valueToString += ", idCategoriaFgts: " +  getIdCategoriaFgts();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new CategoriaFgts(getCdCategoriaFgts(),
			getNmCategoriaFgts(),
			getIdCategoriaFgts());
	}

}
