package com.tivic.manager.adapter.base.antiga.ocorrencia;

import com.tivic.manager.adapter.base.antiga.IAdapterService;
import com.tivic.manager.mob.Ocorrencia;
import com.tivic.manager.wsdl.exceptions.ValidacaoException;

public class AdapterOcorrenciaService implements IAdapterService<OcorrenciaOld, Ocorrencia> {

	@Override
	public OcorrenciaOld toBaseAntiga(Ocorrencia ocorrencia) throws ValidacaoException {
		if (ocorrencia == null)
			throw new ValidacaoException("Uma ocorrência válida deve ser informada para a conversão.");
		return new AdapterOcorrencia().toBaseAntiga(ocorrencia);
	}

	@Override
	public Ocorrencia toBaseNova(OcorrenciaOld ocorrenciaOld) throws Exception {
		if (ocorrenciaOld == null)
			throw new ValidacaoException("Uma ocorrência válida deve ser informada para a conversão.");
		return new AdapterOcorrencia().toBaseNova(ocorrenciaOld);
	}
	
}
