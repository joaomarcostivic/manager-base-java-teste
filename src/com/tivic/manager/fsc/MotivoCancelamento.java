package com.tivic.manager.fsc;

public class MotivoCancelamento {

	private int cdMotivoCancelamento;
	private String dsMotivoCancelamento;
	private String idMotivoCancelamento;

	public MotivoCancelamento(int cdMotivoCancelamento,
			String dsMotivoCancelamento,
			String idMotivoCancelamento){
		setCdMotivoCancelamento(cdMotivoCancelamento);
		setDsMotivoCancelamento(dsMotivoCancelamento);
		setIdMotivoCancelamento(idMotivoCancelamento);
	}
	public void setCdMotivoCancelamento(int cdMotivoCancelamento){
		this.cdMotivoCancelamento=cdMotivoCancelamento;
	}
	public int getCdMotivoCancelamento(){
		return this.cdMotivoCancelamento;
	}
	public void setDsMotivoCancelamento(String dsMotivoCancelamento){
		this.dsMotivoCancelamento=dsMotivoCancelamento;
	}
	public String getDsMotivoCancelamento(){
		return this.dsMotivoCancelamento;
	}
	public void setIdMotivoCancelamento(String idMotivoCancelamento){
		this.idMotivoCancelamento=idMotivoCancelamento;
	}
	public String getIdMotivoCancelamento(){
		return this.idMotivoCancelamento;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdMotivoCancelamento: " +  getCdMotivoCancelamento();
		valueToString += ", dsMotivoCancelamento: " +  getDsMotivoCancelamento();
		valueToString += ", idMotivoCancelamento: " +  getIdMotivoCancelamento();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new MotivoCancelamento(getCdMotivoCancelamento(),
			getDsMotivoCancelamento(),
			getIdMotivoCancelamento());
	}

}