package com.tivic.manager.grl;

public class TipoLogradouro {

	private int cdTipoLogradouro;
	private String nmTipoLogradouro;
	private String sgTipoLogradouro;

	public TipoLogradouro() {}
			
	public TipoLogradouro(int cdTipoLogradouro,
			String nmTipoLogradouro,
			String sgTipoLogradouro){
		setCdTipoLogradouro(cdTipoLogradouro);
		setNmTipoLogradouro(nmTipoLogradouro);
		setSgTipoLogradouro(sgTipoLogradouro);
	}
	public void setCdTipoLogradouro(int cdTipoLogradouro){
		this.cdTipoLogradouro=cdTipoLogradouro;
	}
	public int getCdTipoLogradouro(){
		return this.cdTipoLogradouro;
	}
	public void setNmTipoLogradouro(String nmTipoLogradouro){
		this.nmTipoLogradouro=nmTipoLogradouro;
	}
	public String getNmTipoLogradouro(){
		return this.nmTipoLogradouro;
	}
	public void setSgTipoLogradouro(String sgTipoLogradouro){
		this.sgTipoLogradouro=sgTipoLogradouro;
	}
	public String getSgTipoLogradouro(){
		return this.sgTipoLogradouro;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdTipoLogradouro: " +  getCdTipoLogradouro();
		valueToString += ", nmTipoLogradouro: " +  getNmTipoLogradouro();
		valueToString += ", sgTipoLogradouro: " +  getSgTipoLogradouro();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new TipoLogradouro(cdTipoLogradouro,
			nmTipoLogradouro,
			sgTipoLogradouro);
	}

}