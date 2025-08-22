package com.tivic.manager.adm;

public class TipoDesconto {

	private int cdTipoDesconto;
	private String nmTipoDesconto;

	public TipoDesconto(int cdTipoDesconto,
			String nmTipoDesconto){
		setCdTipoDesconto(cdTipoDesconto);
		setNmTipoDesconto(nmTipoDesconto);
	}
	public void setCdTipoDesconto(int cdTipoDesconto){
		this.cdTipoDesconto=cdTipoDesconto;
	}
	public int getCdTipoDesconto(){
		return this.cdTipoDesconto;
	}
	public void setNmTipoDesconto(String nmTipoDesconto){
		this.nmTipoDesconto=nmTipoDesconto;
	}
	public String getNmTipoDesconto(){
		return this.nmTipoDesconto;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdTipoDesconto: " +  getCdTipoDesconto();
		valueToString += ", nmTipoDesconto: " +  getNmTipoDesconto();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new TipoDesconto(getCdTipoDesconto(),
			getNmTipoDesconto());
	}

}
