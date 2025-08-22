package com.tivic.manager.adapter.base.antiga.especieveiculo;

import com.tivic.manager.fta.EspecieVeiculo;

public class EspecieVeiculoBuilder {
	private EspecieVeiculo especieVeiculo;
	
	public EspecieVeiculoBuilder() {
    	this.especieVeiculo = new EspecieVeiculo();
    }
	
	public EspecieVeiculoBuilder setCdEspecie(int cdEspecie) {
		especieVeiculo.setCdEspecie(cdEspecie);
        return this;
    }
	
	public EspecieVeiculoBuilder setDsEspecie(String dsEspecie) {
		especieVeiculo.setDsEspecie(dsEspecie);
        return this;
    }
	
	public EspecieVeiculo build() {
		return this.especieVeiculo;
	}
	
}
