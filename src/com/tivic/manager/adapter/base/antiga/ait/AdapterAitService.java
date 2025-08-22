package com.tivic.manager.adapter.base.antiga.ait;

import com.tivic.manager.adapter.base.antiga.IAdapterService;
import com.tivic.manager.mob.Ait;
import com.tivic.manager.wsdl.exceptions.ValidacaoException;

public class AdapterAitService implements IAdapterService<AitOld, Ait> {
	
	@Override
	public AitOld toBaseAntiga(Ait ait) throws Exception {
		if (ait == null)
			throw new ValidacaoException("Um AIT valido deve ser informado para conversão.");
		return new AdapterAit().toOld(ait);
	}

	@Override
	public Ait toBaseNova(AitOld aitOld) throws Exception {
		if (aitOld == null)
			throw new ValidacaoException("Um AIT valido deve ser informado para conversão.");
		return new AdapterAit().toBaseNova(aitOld);
	}
	
}
