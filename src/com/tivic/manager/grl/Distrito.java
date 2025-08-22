package com.tivic.manager.grl;

public class Distrito {

	private int cdDistrito;
	private int cdCidade;
	private String nmDistrito;
	private String nrCep;
	private String idDistrito;

	public Distrito(int cdDistrito,
			int cdCidade,
			String nmDistrito,
			String nrCep){
		setCdDistrito(cdDistrito);
		setCdCidade(cdCidade);
		setNmDistrito(nmDistrito);
		setNrCep(nrCep);
	}
	public Distrito(int cdDistrito,
			int cdCidade,
			String nmDistrito,
			String nrCep,
			String idDistrito){
		setCdDistrito(cdDistrito);
		setCdCidade(cdCidade);
		setNmDistrito(nmDistrito);
		setNrCep(nrCep);
		setIdDistrito(idDistrito);
	}
	public void setCdDistrito(int cdDistrito){
		this.cdDistrito=cdDistrito;
	}
	public int getCdDistrito(){
		return this.cdDistrito;
	}
	public void setCdCidade(int cdCidade){
		this.cdCidade=cdCidade;
	}
	public int getCdCidade(){
		return this.cdCidade;
	}
	public void setNmDistrito(String nmDistrito){
		this.nmDistrito=nmDistrito;
	}
	public String getNmDistrito(){
		return this.nmDistrito;
	}
	public void setNrCep(String nrCep){
		this.nrCep=nrCep;
	}
	public String getNrCep(){
		return this.nrCep;
	}
	public void setIdDistrito(String idDistrito) {
		this.idDistrito = idDistrito;
	}
	public String getIdDistrito() {
		return idDistrito;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdDistrito: " +  getCdDistrito();
		valueToString += ", cdCidade: " +  getCdCidade();
		valueToString += ", nmDistrito: " +  getNmDistrito();
		valueToString += ", nrCep: " +  getNrCep();
		valueToString += ", idDistrito: " +  getIdDistrito();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new Distrito(getCdDistrito(),
			getCdCidade(),
			getNmDistrito(),
			getNrCep(),
			getIdDistrito());
	}

}
