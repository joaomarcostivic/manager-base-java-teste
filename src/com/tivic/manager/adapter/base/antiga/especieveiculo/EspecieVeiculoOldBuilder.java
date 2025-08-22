package com.tivic.manager.adapter.base.antiga.especieveiculo;

public class EspecieVeiculoOldBuilder {
	private EspecieVeiculoOld especieVeiculoOld;
	
	public EspecieVeiculoOldBuilder() {
    	this.especieVeiculoOld = new EspecieVeiculoOld();
    }
	
	public EspecieVeiculoOldBuilder setCodEspecie(int codEspecie) {
		especieVeiculoOld.setCodEspecie(codEspecie);
        return this;
    }
	
	public EspecieVeiculoOldBuilder setDsEspecie(String dsEspecie) {
		especieVeiculoOld.setDsEspecie(dsEspecie);
        return this;
    }
	
	public EspecieVeiculoOld build() {
		return this.especieVeiculoOld;
	}
	
}
