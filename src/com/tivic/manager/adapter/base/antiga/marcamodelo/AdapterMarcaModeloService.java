package com.tivic.manager.adapter.base.antiga.marcamodelo;

import com.tivic.manager.adapter.base.antiga.IAdapterService;
import com.tivic.manager.fta.EspecieVeiculo;
import com.tivic.manager.fta.MarcaModelo;
import com.tivic.manager.wsdl.exceptions.ValidacaoException;

public class AdapterMarcaModeloService implements IAdapterService<MarcaModeloOld, MarcaModelo> {

	@Override
	public MarcaModeloOld toBaseAntiga(MarcaModelo marcaModelo) throws ValidacaoException {
		if (marcaModelo == null)
			throw new ValidacaoException("Uma marcaModelo válida deve ser informada para a conversão.");
		return new AdapterMarcaModelo().toBaseAntiga(marcaModelo);
	}

	@Override
	public MarcaModelo toBaseNova(MarcaModeloOld marcaModeloOld) throws Exception {
		if (marcaModeloOld == null)
			throw new ValidacaoException("Uma marcaModelo válida deve ser informada para a conversão.");
		return new AdapterMarcaModelo().toBaseNova(marcaModeloOld);
	}
	
}
