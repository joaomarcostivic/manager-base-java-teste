package com.tivic.manager.adapter.base.antiga.parametro;

import com.tivic.manager.adapter.base.antiga.IAdapterService;
import com.tivic.manager.grl.Parametro;
import com.tivic.manager.wsdl.exceptions.ValidacaoException;

public class AdapterParametroService implements IAdapterService<ParametroOld<?>, Parametro> {
	
	@Override
	public ParametroOld<?> toBaseAntiga(Parametro parametro) throws ValidacaoException {
		if (parametro.getTpDado() < 0)
			throw new ValidacaoException("Tipo de dado necessário para converter o parâmetro.");
		return new AdapterParametro().toOld(parametro);
	}

	@Override
	public Parametro toBaseNova(ParametroOld<?> parametroOld) throws ValidacaoException {
		if (parametroOld == null)
			throw new ValidacaoException("Um valor valido deve ser informado para conversão.");
		return new AdapterParametro().toBaseNova(parametroOld);
	}
}
