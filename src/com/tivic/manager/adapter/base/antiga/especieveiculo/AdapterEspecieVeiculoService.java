package com.tivic.manager.adapter.base.antiga.especieveiculo;

import com.tivic.manager.adapter.base.antiga.IAdapterService;
import com.tivic.manager.fta.EspecieVeiculo;
import com.tivic.manager.wsdl.exceptions.ValidacaoException;

public class AdapterEspecieVeiculoService implements IAdapterService<EspecieVeiculoOld, EspecieVeiculo> {

	@Override
	public EspecieVeiculoOld toBaseAntiga(EspecieVeiculo especieVeiculo) throws ValidacaoException {
		if (especieVeiculo == null)
			throw new ValidacaoException("Uma especieVeiculo válida deve ser informada para a conversão.");
		return new AdapterEspecieVeiculo().toBaseAntiga(especieVeiculo);
	}

	@Override
	public EspecieVeiculo toBaseNova(EspecieVeiculoOld especieVeiculoOld) throws Exception {
		if (especieVeiculoOld == null)
			throw new ValidacaoException("Uma especieVeiculo válida deve ser informada para a conversão.");
		return new AdapterEspecieVeiculo().toBaseNova(especieVeiculoOld);
	}
	
}
