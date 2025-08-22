package com.tivic.manager.adapter.base.antiga.tipoveiculo;

import com.tivic.manager.adapter.base.antiga.IAdapterService;
import com.tivic.manager.fta.TipoVeiculo;
import com.tivic.manager.wsdl.exceptions.ValidacaoException;

public class AdapterTipoVeiculoService implements IAdapterService<TipoVeiculoOld, TipoVeiculo> {

	@Override
	public TipoVeiculoOld toBaseAntiga(TipoVeiculo tipoVeiculo) throws ValidacaoException {
		if (tipoVeiculo == null)
			throw new ValidacaoException("Uma tipoVeiculo válida deve ser informada para a conversão.");
		return new AdapterTipoVeiculo().toBaseAntiga(tipoVeiculo);
	}

	@Override
	public TipoVeiculo toBaseNova(TipoVeiculoOld tipoVeiculoOld) throws Exception {
		if (tipoVeiculoOld == null)
			throw new ValidacaoException("Uma tipoVeiculo válida deve ser informada para a conversão.");
		return new AdapterTipoVeiculo().toBaseNova(tipoVeiculoOld);
	}
	
}
