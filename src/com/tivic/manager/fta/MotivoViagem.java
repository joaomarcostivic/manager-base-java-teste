package com.tivic.manager.fta;

public class MotivoViagem {

	private int cdMotivo;
	private String nmMotivo;

	public MotivoViagem(int cdMotivo,
			String nmMotivo){
		setCdMotivo(cdMotivo);
		setNmMotivo(nmMotivo);
	}
	public void setCdMotivo(int cdMotivo){
		this.cdMotivo=cdMotivo;
	}
	public int getCdMotivo(){
		return this.cdMotivo;
	}
	public void setNmMotivo(String nmMotivo){
		this.nmMotivo=nmMotivo;
	}
	public String getNmMotivo(){
		return this.nmMotivo;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdMotivo: " +  getCdMotivo();
		valueToString += ", nmMotivo: " +  getNmMotivo();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new MotivoViagem(getCdMotivo(),
			getNmMotivo());
	}

}