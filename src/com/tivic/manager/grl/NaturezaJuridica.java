package com.tivic.manager.grl;

public class NaturezaJuridica {

	private int cdNaturezaJuridica;
	private int cdNaturezaSuperior;
	private String nmNaturezaJuridica;
	private String idNaturezaJuridica;
	private String nrNaturezaJuridica;
	
	public NaturezaJuridica(){}
	
	public NaturezaJuridica(int cdNaturezaJuridica,
			int cdNaturezaSuperior,
			String nmNaturezaJuridica,
			String idNaturezaJuridica, String nrNaturezaJuridica){
		setCdNaturezaJuridica(cdNaturezaJuridica);
		setCdNaturezaSuperior(cdNaturezaSuperior);
		setNmNaturezaJuridica(nmNaturezaJuridica);
		setIdNaturezaJuridica(idNaturezaJuridica);
		setNrNaturezaJuridica(nrNaturezaJuridica);
	}
	public void setCdNaturezaJuridica(int cdNaturezaJuridica){
		this.cdNaturezaJuridica=cdNaturezaJuridica;
	}
	public int getCdNaturezaJuridica(){
		return this.cdNaturezaJuridica;
	}
	public void setCdNaturezaSuperior(int cdNaturezaSuperior){
		this.cdNaturezaSuperior=cdNaturezaSuperior;
	}
	public int getCdNaturezaSuperior(){
		return this.cdNaturezaSuperior;
	}
	public void setNmNaturezaJuridica(String nmNaturezaJuridica){
		this.nmNaturezaJuridica=nmNaturezaJuridica;
	}
	public String getNmNaturezaJuridica(){
		return this.nmNaturezaJuridica;
	}
	public void setIdNaturezaJuridica(String idNaturezaJuridica){
		this.idNaturezaJuridica=idNaturezaJuridica;
	}
	public String getIdNaturezaJuridica(){
		return this.idNaturezaJuridica;
	}
	
	public String getNrNaturezaJuridica() {
		return nrNaturezaJuridica;
	}
	public void setNrNaturezaJuridica(String nrNaturezaJuridica) {
		this.nrNaturezaJuridica = nrNaturezaJuridica;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdNaturezaJuridica: " +  getCdNaturezaJuridica();
		valueToString += ", cdNaturezaSuperior: " +  getCdNaturezaSuperior();
		valueToString += ", nmNaturezaJuridica: " +  getNmNaturezaJuridica();
		valueToString += ", idNaturezaJuridica: " +  getIdNaturezaJuridica();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new NaturezaJuridica(getCdNaturezaJuridica(),
			getCdNaturezaSuperior(),
			getNmNaturezaJuridica(),
			getIdNaturezaJuridica(), getNrNaturezaJuridica());
	}

}