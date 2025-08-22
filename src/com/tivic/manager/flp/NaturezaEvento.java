package com.tivic.manager.flp;

public class NaturezaEvento {

	private int cdNaturezaEvento;
	private int cdNaturezaSuperior;
	private String nmNaturezaEvento;
	private String idNaturezaEvento;

	public NaturezaEvento(){}
	public NaturezaEvento(int cdNaturezaEvento,
			int cdNaturezaSuperior,
			String nmNaturezaEvento,
			String idNaturezaEvento){
		setCdNaturezaEvento(cdNaturezaEvento);
		setCdNaturezaSuperior(cdNaturezaSuperior);
		setNmNaturezaEvento(nmNaturezaEvento);
		setIdNaturezaEvento(idNaturezaEvento);
	}
	public void setCdNaturezaEvento(int cdNaturezaEvento){
		this.cdNaturezaEvento=cdNaturezaEvento;
	}
	public int getCdNaturezaEvento(){
		return this.cdNaturezaEvento;
	}
	public void setCdNaturezaSuperior(int cdNaturezaSuperior){
		this.cdNaturezaSuperior=cdNaturezaSuperior;
	}
	public int getCdNaturezaSuperior(){
		return this.cdNaturezaSuperior;
	}
	public void setNmNaturezaEvento(String nmNaturezaEvento){
		this.nmNaturezaEvento=nmNaturezaEvento;
	}
	public String getNmNaturezaEvento(){
		return this.nmNaturezaEvento;
	}
	public void setIdNaturezaEvento(String idNaturezaEvento){
		this.idNaturezaEvento=idNaturezaEvento;
	}
	public String getIdNaturezaEvento(){
		return this.idNaturezaEvento;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdNaturezaEvento: " +  getCdNaturezaEvento();
		valueToString += ", cdNaturezaSuperior: " +  getCdNaturezaSuperior();
		valueToString += ", nmNaturezaEvento: " +  getNmNaturezaEvento();
		valueToString += ", idNaturezaEvento: " +  getIdNaturezaEvento();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new NaturezaEvento(getCdNaturezaEvento(),
			getCdNaturezaSuperior(),
			getNmNaturezaEvento(),
			getIdNaturezaEvento());
	}

}