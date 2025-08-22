package com.tivic.manager.grl;

public class Ncm {

	private int cdNcm;
	private String nmNcm;
	private int cdUnidadeMedida;
	private String nrNcm;

	public Ncm(int cdNcm,
			String nmNcm,
			int cdUnidadeMedida,
			String nrNcm){
		setCdNcm(cdNcm);
		setNmNcm(nmNcm);
		setCdUnidadeMedida(cdUnidadeMedida);
		setNrNcm(nrNcm);
	}
	public void setCdNcm(int cdNcm){
		this.cdNcm=cdNcm;
	}
	public int getCdNcm(){
		return this.cdNcm;
	}
	public void setNmNcm(String nmNcm){
		this.nmNcm=nmNcm;
	}
	public String getNmNcm(){
		return this.nmNcm;
	}
	public void setCdUnidadeMedida(int cdUnidadeMedida){
		this.cdUnidadeMedida=cdUnidadeMedida;
	}
	public int getCdUnidadeMedida(){
		return this.cdUnidadeMedida;
	}
	public void setNrNcm(String nrNcm){
		this.nrNcm=nrNcm;
	}
	public String getNrNcm(){
		return this.nrNcm;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdNcm: " +  getCdNcm();
		valueToString += ", nmNcm: " +  getNmNcm();
		valueToString += ", cdUnidadeMedida: " +  getCdUnidadeMedida();
		valueToString += ", nrNcm: " +  getNrNcm();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new Ncm(getCdNcm(),
			getNmNcm(),
			getCdUnidadeMedida(),
			getNrNcm());
	}

}