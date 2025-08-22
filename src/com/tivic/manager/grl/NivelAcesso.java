package com.tivic.manager.grl;

public class NivelAcesso {

	private int cdNivelAcesso;
	private String nmNivelAcesso;

	public NivelAcesso(int cdNivelAcesso,
			String nmNivelAcesso){
		setCdNivelAcesso(cdNivelAcesso);
		setNmNivelAcesso(nmNivelAcesso);
	}
	public void setCdNivelAcesso(int cdNivelAcesso){
		this.cdNivelAcesso=cdNivelAcesso;
	}
	public int getCdNivelAcesso(){
		return this.cdNivelAcesso;
	}
	public void setNmNivelAcesso(String nmNivelAcesso){
		this.nmNivelAcesso=nmNivelAcesso;
	}
	public String getNmNivelAcesso(){
		return this.nmNivelAcesso;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdNivelAcesso: " +  getCdNivelAcesso();
		valueToString += ", nmNivelAcesso: " +  getNmNivelAcesso();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new NivelAcesso(getCdNivelAcesso(),
			getNmNivelAcesso());
	}

}
