package com.tivic.manager.acd;

public class FormacaoAreaConhecimento {

	private int cdFormacaoAreaConhecimento;
	private String nmArea;
	private String idArea;

	public FormacaoAreaConhecimento(){ }

	public FormacaoAreaConhecimento(int cdFormacaoAreaConhecimento,
			String nmArea,
			String idArea){
		setCdFormacaoAreaConhecimento(cdFormacaoAreaConhecimento);
		setNmArea(nmArea);
		setIdArea(idArea);
	}
	public void setCdFormacaoAreaConhecimento(int cdFormacaoAreaConhecimento){
		this.cdFormacaoAreaConhecimento=cdFormacaoAreaConhecimento;
	}
	public int getCdFormacaoAreaConhecimento(){
		return this.cdFormacaoAreaConhecimento;
	}
	public void setNmArea(String nmArea){
		this.nmArea=nmArea;
	}
	public String getNmArea(){
		return this.nmArea;
	}
	public void setIdArea(String idArea){
		this.idArea=idArea;
	}
	public String getIdArea(){
		return this.idArea;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdFormacaoAreaConhecimento: " +  getCdFormacaoAreaConhecimento();
		valueToString += ", nmArea: " +  getNmArea();
		valueToString += ", idArea: " +  getIdArea();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new FormacaoAreaConhecimento(getCdFormacaoAreaConhecimento(),
			getNmArea(),
			getIdArea());
	}

}