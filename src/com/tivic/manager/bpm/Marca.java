package com.tivic.manager.bpm;

public class Marca {

	private int cdMarca;
	private String nmMarca;
	private String idMarca;

	public Marca() { }

	public Marca(int cdMarca,
			String nmMarca,
			String idMarca) {
		setCdMarca(cdMarca);
		setNmMarca(nmMarca);
		setIdMarca(idMarca);
	}
	public void setCdMarca(int cdMarca){
		this.cdMarca=cdMarca;
	}
	public int getCdMarca(){
		return this.cdMarca;
	}
	public void setNmMarca(String nmMarca){
		this.nmMarca=nmMarca;
	}
	public String getNmMarca(){
		return this.nmMarca;
	}
	public void setIdMarca(String idMarca){
		this.idMarca=idMarca;
	}
	public String getIdMarca(){
		return this.idMarca;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdMarca: " +  getCdMarca();
		valueToString += ", nmMarca: " +  getNmMarca();
		valueToString += ", idMarca: " +  getIdMarca();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new Marca(getCdMarca(),
			getNmMarca(),
			getIdMarca());
	}

}