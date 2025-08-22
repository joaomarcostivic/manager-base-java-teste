package com.tivic.manager.mob;

public class TipoVeiculoComponente {

	private int cdTipoVeiculoComponente;
	private int cdTipoVeiculo;
	private String nmComponente;

	public TipoVeiculoComponente(){ }

	public TipoVeiculoComponente(int cdTipoVeiculoComponente,
			int cdTipoVeiculo,
			String nmComponente){
		setCdTipoVeiculoComponente(cdTipoVeiculoComponente);
		setCdTipoVeiculo(cdTipoVeiculo);
		setNmComponente(nmComponente);
	}
	public void setCdTipoVeiculoComponente(int cdTipoVeiculoComponente){
		this.cdTipoVeiculoComponente=cdTipoVeiculoComponente;
	}
	public int getCdTipoVeiculoComponente(){
		return this.cdTipoVeiculoComponente;
	}
	public void setCdTipoVeiculo(int cdTipoVeiculo){
		this.cdTipoVeiculo=cdTipoVeiculo;
	}
	public int getCdTipoVeiculo(){
		return this.cdTipoVeiculo;
	}
	public void setNmComponente(String nmComponente){
		this.nmComponente=nmComponente;
	}
	public String getNmComponente(){
		return this.nmComponente;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdTipoVeiculoComponente: " +  getCdTipoVeiculoComponente();
		valueToString += ", cdTipoVeiculo: " +  getCdTipoVeiculo();
		valueToString += ", nmComponente: " +  getNmComponente();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new TipoVeiculoComponente(getCdTipoVeiculoComponente(),
			getCdTipoVeiculo(),
			getNmComponente());
	}

}