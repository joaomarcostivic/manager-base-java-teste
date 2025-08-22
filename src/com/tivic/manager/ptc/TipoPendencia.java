package com.tivic.manager.ptc;

public class TipoPendencia {

	private int cdTipoPendencia;
	private String nmTipoPendencia;
	private String idTipoPendencia;
	private int stTipoPendencia;
	private int cdEmpresa;
	
	public TipoPendencia() { }
			
	public TipoPendencia(int cdTipoPendencia,
			String nmTipoPendencia,
			String idTipoPendencia,
			int stTipoPendencia,
			int cdEmpresa){
		setCdTipoPendencia(cdTipoPendencia);
		setNmTipoPendencia(nmTipoPendencia);
		setIdTipoPendencia(idTipoPendencia);
		setStTipoPendencia(stTipoPendencia);
		setCdEmpresa(cdEmpresa);
	}
	public void setCdTipoPendencia(int cdTipoPendencia){
		this.cdTipoPendencia=cdTipoPendencia;
	}
	public int getCdTipoPendencia(){
		return this.cdTipoPendencia;
	}
	public void setNmTipoPendencia(String nmTipoPendencia){
		this.nmTipoPendencia=nmTipoPendencia;
	}
	public String getNmTipoPendencia(){
		return this.nmTipoPendencia;
	}
	public void setIdTipoPendencia(String idTipoPendencia){
		this.idTipoPendencia=idTipoPendencia;
	}
	public String getIdTipoPendencia(){
		return this.idTipoPendencia;
	}
	public void setStTipoPendencia(int stTipoPendencia){
		this.stTipoPendencia=stTipoPendencia;
	}
	public int getStTipoPendencia(){
		return this.stTipoPendencia;
	}
	public void setCdEmpresa(int cdEmpresa){
		this.cdEmpresa=cdEmpresa;
	}
	public int getCdEmpresa(){
		return this.cdEmpresa;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdTipoPendencia: " +  getCdTipoPendencia();
		valueToString += ", nmTipoPendencia: " +  getNmTipoPendencia();
		valueToString += ", idTipoPendencia: " +  getIdTipoPendencia();
		valueToString += ", stTipoPendencia: " +  getStTipoPendencia();
		valueToString += ", cdEmpresa: " +  getCdEmpresa();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new TipoPendencia(getCdTipoPendencia(),
			getNmTipoPendencia(),
			getIdTipoPendencia(),
			getStTipoPendencia(),
			getCdEmpresa());
	}

}