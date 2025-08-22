package com.tivic.manager.ord;

public class Modalidade {

	private int cdModalidade;
	private String nmModalidade;
	private String idModalidade;
	private int lgContrato;

	public Modalidade(){ }

	public Modalidade(int cdModalidade,
			String nmModalidade,
			String idModalidade,
			int lgContrato){
		setCdModalidade(cdModalidade);
		setNmModalidade(nmModalidade);
		setIdModalidade(idModalidade);
		setLgContrato(lgContrato);
	}
	public void setCdModalidade(int cdModalidade){
		this.cdModalidade=cdModalidade;
	}
	public int getCdModalidade(){
		return this.cdModalidade;
	}
	public void setNmModalidade(String nmModalidade){
		this.nmModalidade=nmModalidade;
	}
	public String getNmModalidade(){
		return this.nmModalidade;
	}
	public void setIdModalidade(String idModalidade){
		this.idModalidade=idModalidade;
	}
	public String getIdModalidade(){
		return this.idModalidade;
	}
	public void setLgContrato(int lgContrato){
		this.lgContrato=lgContrato;
	}
	public int getLgContrato(){
		return this.lgContrato;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdModalidade: " +  getCdModalidade();
		valueToString += ", nmModalidade: " +  getNmModalidade();
		valueToString += ", idModalidade: " +  getIdModalidade();
		valueToString += ", lgContrato: " +  getLgContrato();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new Modalidade(getCdModalidade(),
			getNmModalidade(),
			getIdModalidade(),
			getLgContrato());
	}

}
