package com.tivic.manager.blb;

public class Autor {

	private int cdAutor;
	private String nmAutor;
	private String idAutor;

	public Autor(){ }

	public Autor(int cdAutor,
			String nmAutor,
			String idAutor){
		setCdAutor(cdAutor);
		setNmAutor(nmAutor);
		setIdAutor(idAutor);
	}
	public void setCdAutor(int cdAutor){
		this.cdAutor=cdAutor;
	}
	public int getCdAutor(){
		return this.cdAutor;
	}
	public void setNmAutor(String nmAutor){
		this.nmAutor=nmAutor;
	}
	public String getNmAutor(){
		return this.nmAutor;
	}
	public void setIdAutor(String idAutor){
		this.idAutor=idAutor;
	}
	public String getIdAutor(){
		return this.idAutor;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdAutor: " +  getCdAutor();
		valueToString += ", nmAutor: " +  getNmAutor();
		valueToString += ", idAutor: " +  getIdAutor();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new Autor(getCdAutor(),
			getNmAutor(),
			getIdAutor());
	}

}