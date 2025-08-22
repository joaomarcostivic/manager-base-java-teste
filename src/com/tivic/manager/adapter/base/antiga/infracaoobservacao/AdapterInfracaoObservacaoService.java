package com.tivic.manager.adapter.base.antiga.infracaoobservacao;

import com.tivic.manager.adapter.base.antiga.IAdapterService;
import com.tivic.manager.mob.InfracaoObservacao;
import com.tivic.manager.wsdl.exceptions.ValidacaoException;

public class AdapterInfracaoObservacaoService implements IAdapterService<InfracaoObservacaoOld, InfracaoObservacao> {

	@Override
	public InfracaoObservacaoOld toBaseAntiga(InfracaoObservacao infracaoObservacao) throws ValidacaoException {
		if (infracaoObservacao == null)
			throw new ValidacaoException("Uma infracaoObservacao válida deve ser informada para a conversão.");
		return new AdapterInfracaoObservacao().toBaseAntiga(infracaoObservacao);
	}

	@Override
	public InfracaoObservacao toBaseNova(InfracaoObservacaoOld infracaoObservacaoOld) throws Exception {
		if (infracaoObservacaoOld == null)
			throw new ValidacaoException("Uma infracaoObservacao válida deve ser informada para a conversão.");
		return new AdapterInfracaoObservacao().toBaseNova(infracaoObservacaoOld);
	}
	
}
