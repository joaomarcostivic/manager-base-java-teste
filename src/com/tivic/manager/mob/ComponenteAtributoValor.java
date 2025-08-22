package com.tivic.manager.mob;

public class ComponenteAtributoValor {

	private int cdComponenteAtributoValor;
	private int cdVeiculo;
	private int cdComponenteVeiculo;
	private String nmAtributo;
	private String vlAtributo;

	public ComponenteAtributoValor(){ }

	public ComponenteAtributoValor(int cdComponenteAtributoValor,
			int cdVeiculo,
			int cdComponenteVeiculo,
			String nmAtributo,
			String vlAtributo){
		setCdComponenteAtributoValor(cdComponenteAtributoValor);
		setCdVeiculo(cdVeiculo);
		setCdComponenteVeiculo(cdComponenteVeiculo);
		setNmAtributo(nmAtributo);
		setVlAtributo(vlAtributo);
	}
	public void setCdComponenteAtributoValor(int cdComponenteAtributoValor){
		this.cdComponenteAtributoValor=cdComponenteAtributoValor;
	}
	public int getCdComponenteAtributoValor(){
		return this.cdComponenteAtributoValor;
	}
	public void setCdVeiculo(int cdVeiculo){
		this.cdVeiculo=cdVeiculo;
	}
	public int getCdVeiculo(){
		return this.cdVeiculo;
	}
	public void setCdComponenteVeiculo(int cdComponenteVeiculo){
		this.cdComponenteVeiculo=cdComponenteVeiculo;
	}
	public int getCdComponenteVeiculo(){
		return this.cdComponenteVeiculo;
	}
	public void setNmAtributo(String nmAtributo){
		this.nmAtributo=nmAtributo;
	}
	public String getNmAtributo(){
		return this.nmAtributo;
	}
	public void setVlAtributo(String vlAtributo){
		this.vlAtributo=vlAtributo;
	}
	public String getVlAtributo(){
		return this.vlAtributo;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdComponenteAtributoValor: " +  getCdComponenteAtributoValor();
		valueToString += ", cdVeiculo: " +  getCdVeiculo();
		valueToString += ", cdComponenteVeiculo: " +  getCdComponenteVeiculo();
		valueToString += ", nmAtributo: " +  getNmAtributo();
		valueToString += ", vlAtributo: " +  getVlAtributo();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new ComponenteAtributoValor(getCdComponenteAtributoValor(),
			getCdVeiculo(),
			getCdComponenteVeiculo(),
			getNmAtributo(),
			getVlAtributo());
	}

}