package com.tivic.manager.prc;

public class OrigemAndamento {

	private int cdOrigemAndamento;
	private String nmOrigemAndamento;
	private String dsOrigemAndamento;
	private int stOrigemAndamento;

	public OrigemAndamento() { }

	public OrigemAndamento(int cdOrigemAndamento,
			String nmOrigemAndamento,
			String dsOrigemAndamento,
			int stOrigemAndamento) {
		setCdOrigemAndamento(cdOrigemAndamento);
		setNmOrigemAndamento(nmOrigemAndamento);
		setDsOrigemAndamento(dsOrigemAndamento);
		setStOrigemAndamento(stOrigemAndamento);
	}
	public void setCdOrigemAndamento(int cdOrigemAndamento){
		this.cdOrigemAndamento=cdOrigemAndamento;
	}
	public int getCdOrigemAndamento(){
		return this.cdOrigemAndamento;
	}
	public void setNmOrigemAndamento(String nmOrigemAndamento){
		this.nmOrigemAndamento=nmOrigemAndamento;
	}
	public String getNmOrigemAndamento(){
		return this.nmOrigemAndamento;
	}
	public void setDsOrigemAndamento(String dsOrigemAndamento){
		this.dsOrigemAndamento=dsOrigemAndamento;
	}
	public String getDsOrigemAndamento(){
		return this.dsOrigemAndamento;
	}
	public void setStOrigemAndamento(int stOrigemAndamento){
		this.stOrigemAndamento=stOrigemAndamento;
	}
	public int getStOrigemAndamento(){
		return this.stOrigemAndamento;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdOrigemAndamento: " +  getCdOrigemAndamento();
		valueToString += ", nmOrigemAndamento: " +  getNmOrigemAndamento();
		valueToString += ", dsOrigemAndamento: " +  getDsOrigemAndamento();
		valueToString += ", stOrigemAndamento: " +  getStOrigemAndamento();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new OrigemAndamento(getCdOrigemAndamento(),
			getNmOrigemAndamento(),
			getDsOrigemAndamento(),
			getStOrigemAndamento());
	}

}