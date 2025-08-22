package com.tivic.manager.crt;

public class Motivo {

	private int cdMotivo;
	private String nmMotivo;

	public Motivo(int cdMotivo,
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
}