package com.tivic.manager.mob;

public class VeiculoAtributoValor {

	private int cdVeiculoAtributoValor;
	private int cdVeiculo;
	private int cdTipoVeiculoAtributo;
	private String vlAtributo;

	public VeiculoAtributoValor(){ }

	public VeiculoAtributoValor(int cdVeiculoAtributoValor,
			int cdVeiculo,
			int cdTipoVeiculoAtributo,
			String vlAtributo){
		setCdVeiculoAtributoValor(cdVeiculoAtributoValor);
		setCdVeiculo(cdVeiculo);
		setCdTipoVeiculoAtributo(cdTipoVeiculoAtributo);
		setVlAtributo(vlAtributo);
	}
	public void setCdVeiculoAtributoValor(int cdVeiculoAtributoValor){
		this.cdVeiculoAtributoValor=cdVeiculoAtributoValor;
	}
	public int getCdVeiculoAtributoValor(){
		return this.cdVeiculoAtributoValor;
	}
	public void setCdVeiculo(int cdVeiculo){
		this.cdVeiculo=cdVeiculo;
	}
	public int getCdVeiculo(){
		return this.cdVeiculo;
	}
	public void setCdTipoVeiculoAtributo(int cdTipoVeiculoAtributo){
		this.cdTipoVeiculoAtributo=cdTipoVeiculoAtributo;
	}
	public int getCdTipoVeiculoAtributo(){
		return this.cdTipoVeiculoAtributo;
	}
	public void setVlAtributo(String vlAtributo){
		this.vlAtributo=vlAtributo;
	}
	public String getVlAtributo(){
		return this.vlAtributo;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdVeiculoAtributoValor: " +  getCdVeiculoAtributoValor();
		valueToString += ", cdVeiculo: " +  getCdVeiculo();
		valueToString += ", cdTipoVeiculoAtributo: " +  getCdTipoVeiculoAtributo();
		valueToString += ", vlAtributo: " +  getVlAtributo();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new VeiculoAtributoValor(getCdVeiculoAtributoValor(),
			getCdVeiculo(),
			getCdTipoVeiculoAtributo(),
			getVlAtributo());
	}

}