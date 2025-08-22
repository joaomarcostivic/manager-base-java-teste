package com.tivic.manager.adapter.base.antiga.aitmovimento;

import com.tivic.manager.adapter.base.antiga.IAdapterService;
import com.tivic.manager.mob.AitMovimento;
import com.tivic.manager.wsdl.exceptions.ValidacaoException;

public class AdapterAitMovimentoService implements IAdapterService<AitMovimentoOld, AitMovimento> {

	@Override
	public AitMovimentoOld toBaseAntiga(AitMovimento aitMovimento) throws ValidacaoException {
		if (aitMovimento == null) 
			throw new ValidacaoException("Um movimento valido deve ser informado para a conversão.");
		return new AdapterAitMovimento().adaptarToOld(aitMovimento);
	}

	@Override
	public AitMovimento toBaseNova(AitMovimentoOld aitMovimentoOld) throws Exception {
		if (aitMovimentoOld == null) 
			throw new ValidacaoException("Um movimento valido deve ser informado para a conversão.");
		return new AdapterAitMovimento().adapterToBaseNova(aitMovimentoOld);
	}

}
