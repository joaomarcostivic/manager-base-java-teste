package com.tivic.manager.mob;

public class EventoMotivoCancelamento {

	private int cdMotivoCancelamento;
	private String nmMotivoCancelamento;

	public EventoMotivoCancelamento() { }

	public EventoMotivoCancelamento(int cdMotivoCancelamento,
			String nmMotivoCancelamento) {
		setCdMotivoCancelamento(cdMotivoCancelamento);
		setNmMotivoCancelamento(nmMotivoCancelamento);
	}
	public void setCdMotivoCancelamento(int cdMotivoCancelamento){
		this.cdMotivoCancelamento=cdMotivoCancelamento;
	}
	public int getCdMotivoCancelamento(){
		return this.cdMotivoCancelamento;
	}
	public void setNmMotivoCancelamento(String nmMotivoCancelamento){
		this.nmMotivoCancelamento=nmMotivoCancelamento;
	}
	public String getNmMotivoCancelamento(){
		return this.nmMotivoCancelamento;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdMotivoCancelamento: " +  getCdMotivoCancelamento();
		valueToString += ", nmMotivoCancelamento: " +  getNmMotivoCancelamento();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new EventoMotivoCancelamento(getCdMotivoCancelamento(),
			getNmMotivoCancelamento());
	}

}