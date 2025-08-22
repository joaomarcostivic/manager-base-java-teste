package com.tivic.manager.mob;

public class TipoVeiculoAtributo {

	private int cdTipoVeiculoAtributo;
	private int cdTipoVeiculoComponente;
	private String nmAtributo;

	public TipoVeiculoAtributo(){ }

	public TipoVeiculoAtributo(int cdTipoVeiculoAtributo,
			int cdTipoVeiculoComponente,
			String nmAtributo){
		setCdTipoVeiculoAtributo(cdTipoVeiculoAtributo);
		setCdTipoVeiculoComponente(cdTipoVeiculoComponente);
		setNmAtributo(nmAtributo);
	}
	public void setCdTipoVeiculoAtributo(int cdTipoVeiculoAtributo){
		this.cdTipoVeiculoAtributo=cdTipoVeiculoAtributo;
	}
	public int getCdTipoVeiculoAtributo(){
		return this.cdTipoVeiculoAtributo;
	}
	public void setCdTipoVeiculoComponente(int cdTipoVeiculoComponente){
		this.cdTipoVeiculoComponente=cdTipoVeiculoComponente;
	}
	public int getCdTipoVeiculoComponente(){
		return this.cdTipoVeiculoComponente;
	}
	public void setNmAtributo(String nmAtributo){
		this.nmAtributo=nmAtributo;
	}
	public String getNmAtributo(){
		return this.nmAtributo;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdTipoVeiculoAtributo: " +  getCdTipoVeiculoAtributo();
		valueToString += ", cdTipoVeiculoComponente: " +  getCdTipoVeiculoComponente();
		valueToString += ", nmAtributo: " +  getNmAtributo();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new TipoVeiculoAtributo(getCdTipoVeiculoAtributo(),
			getCdTipoVeiculoComponente(),
			getNmAtributo());
	}

}