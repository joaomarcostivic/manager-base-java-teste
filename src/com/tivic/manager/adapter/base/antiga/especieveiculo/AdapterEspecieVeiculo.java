package com.tivic.manager.adapter.base.antiga.especieveiculo;

import java.io.IOException;

import javax.swing.text.BadLocationException;

import com.tivic.manager.fta.EspecieVeiculo;

public class AdapterEspecieVeiculo {
	
	public EspecieVeiculoOld toBaseAntiga(EspecieVeiculo especieVeiculo) {
		return new EspecieVeiculoOldBuilder()
				.setCodEspecie(especieVeiculo.getCdEspecie())
				.setDsEspecie(especieVeiculo.getDsEspecie())
				.build();
	}
	
	public EspecieVeiculo toBaseNova(EspecieVeiculoOld especieVeiculoOld) throws IOException, BadLocationException {
		return new EspecieVeiculoBuilder()
				.setCdEspecie(especieVeiculoOld.getCodEspecie())
				.setDsEspecie(especieVeiculoOld.getDsEspecie())
				.build();
	}

}
