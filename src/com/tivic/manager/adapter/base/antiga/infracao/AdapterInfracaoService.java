package com.tivic.manager.adapter.base.antiga.infracao;

import com.tivic.manager.adapter.base.antiga.IAdapterService;
import com.tivic.manager.mob.Infracao;
import com.tivic.manager.wsdl.exceptions.ValidacaoException;

public class AdapterInfracaoService implements IAdapterService<InfracaoOld, Infracao> {

	@Override
	public InfracaoOld toBaseAntiga(Infracao infracao) throws ValidacaoException {
		if (infracao == null)
			throw new ValidacaoException("Uma infração válida deve ser informada para a conversão.");
		return new AdapterInfracao().toBaseAntiga(infracao);
	}

	@Override
	public Infracao toBaseNova(InfracaoOld infracaoOld) throws Exception {
		if (infracaoOld == null)
			throw new ValidacaoException("Uma infração válida deve ser informada para a conversão.");
		return new AdapterInfracao().toBaseNova(infracaoOld);
	}

}
