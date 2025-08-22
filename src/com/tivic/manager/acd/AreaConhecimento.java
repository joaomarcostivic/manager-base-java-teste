package com.tivic.manager.acd;

public class AreaConhecimento {

	private int cdAreaConhecimento;
	private String nmAreaConhecimento;
	private int cdAreaConhecimentoSuperior;
	private String idArea;

	public AreaConhecimento(){ }

	public AreaConhecimento(int cdAreaConhecimento,
			String nmAreaConhecimento,
			int cdAreaConhecimentoSuperior,
			String idArea){
		setCdAreaConhecimento(cdAreaConhecimento);
		setNmAreaConhecimento(nmAreaConhecimento);
		setCdAreaConhecimentoSuperior(cdAreaConhecimentoSuperior);
		setIdArea(idArea);
	}
	public void setCdAreaConhecimento(int cdAreaConhecimento){
		this.cdAreaConhecimento=cdAreaConhecimento;
	}
	public int getCdAreaConhecimento(){
		return this.cdAreaConhecimento;
	}
	public void setNmAreaConhecimento(String nmAreaConhecimento){
		this.nmAreaConhecimento=nmAreaConhecimento;
	}
	public String getNmAreaConhecimento(){
		return this.nmAreaConhecimento;
	}
	public void setCdAreaConhecimentoSuperior(int cdAreaConhecimentoSuperior){
		this.cdAreaConhecimentoSuperior=cdAreaConhecimentoSuperior;
	}
	public int getCdAreaConhecimentoSuperior(){
		return this.cdAreaConhecimentoSuperior;
	}
	public void setIdArea(String idArea){
		this.idArea=idArea;
	}
	public String getIdArea(){
		return this.idArea;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdAreaConhecimento: " +  getCdAreaConhecimento();
		valueToString += ", nmAreaConhecimento: " +  getNmAreaConhecimento();
		valueToString += ", cdAreaConhecimentoSuperior: " +  getCdAreaConhecimentoSuperior();
		valueToString += ", idArea: " +  getIdArea();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new AreaConhecimento(getCdAreaConhecimento(),
			getNmAreaConhecimento(),
			getCdAreaConhecimentoSuperior(),
			getIdArea());
	}

}