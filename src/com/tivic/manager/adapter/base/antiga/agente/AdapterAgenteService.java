package com.tivic.manager.adapter.base.antiga.agente;

import com.tivic.manager.adapter.base.antiga.IAdapterService;
import com.tivic.manager.mob.Agente;
import com.tivic.manager.wsdl.exceptions.ValidacaoException;

public class AdapterAgenteService implements IAdapterService<AgenteOld, Agente> {

	@Override
	public AgenteOld toBaseAntiga(Agente agente) throws ValidacaoException {
		if (agente == null)
			throw new ValidacaoException("Um agente válido deve ser informado para a conversão.");
		return new AdapterAgente().toBaseAntiga(agente);
	}

	@Override
	public Agente toBaseNova(AgenteOld agenteOld) throws Exception {
		if (agenteOld == null)
			throw new ValidacaoException("Um agente valido deve ser informado para a conversão.");
		return new AdapterAgente().toBaseNova(agenteOld);
	}

}
