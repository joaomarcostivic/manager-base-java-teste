package com.tivic.manager.adapter.base.antiga.talonario;

import com.tivic.manager.adapter.base.antiga.IAdapterService;
import com.tivic.manager.mob.Talonario;
import com.tivic.manager.wsdl.exceptions.ValidacaoException;

public class AdapterTalonarioService implements IAdapterService<TalonarioOld, Talonario> {

	@Override
	public TalonarioOld toBaseAntiga(Talonario talonario) throws ValidacaoException {
		if (talonario == null)
			throw new ValidacaoException("Um talonário válido deve ser informado para a conversão.");
		return new AdapterTalonario().toBaseAntiga(talonario);
	}

	@Override
	public Talonario toBaseNova(TalonarioOld talonarioOld) throws Exception {
		if (talonarioOld == null)
			throw new ValidacaoException("Um talonário válido deve ser informado para a conversão.");
		return new AdapterTalonario().toBaseNova(talonarioOld);
	}

}
