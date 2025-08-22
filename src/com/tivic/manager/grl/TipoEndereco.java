package com.tivic.manager.grl;

public class TipoEndereco {

	private int cdTipoEndereco;
	private String nmTipoEndereco;

	public TipoEndereco() { }
	
	public TipoEndereco(int cdTipoEndereco,
			String nmTipoEndereco){
		setCdTipoEndereco(cdTipoEndereco);
		setNmTipoEndereco(nmTipoEndereco);
	}
	public void setCdTipoEndereco(int cdTipoEndereco){
		this.cdTipoEndereco=cdTipoEndereco;
	}
	public int getCdTipoEndereco(){
		return this.cdTipoEndereco;
	}
	public void setNmTipoEndereco(String nmTipoEndereco){
		this.nmTipoEndereco=nmTipoEndereco;
	}
	public String getNmTipoEndereco(){
		return this.nmTipoEndereco;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdTipoEndereco: " +  getCdTipoEndereco();
		valueToString += ", nmTipoEndereco: " +  getNmTipoEndereco();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new TipoEndereco(cdTipoEndereco,
			nmTipoEndereco);
	}

}