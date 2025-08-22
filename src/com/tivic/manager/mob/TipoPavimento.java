package com.tivic.manager.mob;

public class TipoPavimento {

	private int cdTipoPavimento;
	private String nmTipoPavimento;
	private String idTipoPavimento;

	public TipoPavimento() { }

	public TipoPavimento(int cdTipoPavimento,
			String nmTipoPavimento,
			String idTipoPavimento) {
		setCdTipoPavimento(cdTipoPavimento);
		setNmTipoPavimento(nmTipoPavimento);
		setIdTipoPavimento(idTipoPavimento);
	}
	public void setCdTipoPavimento(int cdTipoPavimento){
		this.cdTipoPavimento=cdTipoPavimento;
	}
	public int getCdTipoPavimento(){
		return this.cdTipoPavimento;
	}
	public void setNmTipoPavimento(String nmTipoPavimento){
		this.nmTipoPavimento=nmTipoPavimento;
	}
	public String getNmTipoPavimento(){
		return this.nmTipoPavimento;
	}
	public void setIdTipoPavimento(String idTipoPavimento){
		this.idTipoPavimento=idTipoPavimento;
	}
	public String getIdTipoPavimento(){
		return this.idTipoPavimento;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdTipoPavimento: " +  getCdTipoPavimento();
		valueToString += ", nmTipoPavimento: " +  getNmTipoPavimento();
		valueToString += ", idTipoPavimento: " +  getIdTipoPavimento();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new TipoPavimento(getCdTipoPavimento(),
			getNmTipoPavimento(),
			getIdTipoPavimento());
	}

}