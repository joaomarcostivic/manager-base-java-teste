package com.tivic.manager.adapter.base.antiga.ocorrencia;

import java.io.IOException;

import javax.swing.text.BadLocationException;

import com.tivic.manager.mob.Ocorrencia;

public class AdapterOcorrencia {
	
	public OcorrenciaOld toBaseAntiga(Ocorrencia ocorrencia) {
		return new OcorrenciaOldBuilder()
				.setCodOcorrencia(ocorrencia.getCdOcorrencia())
				.setDsOcorrencia(ocorrencia.getDsOcorrencia())
				.build();
	}
	
	public Ocorrencia toBaseNova(OcorrenciaOld ocorrenciaOld) throws IOException, BadLocationException {
		return new OcorrenciaBuilder()
				.setCdOcorrencia(ocorrenciaOld.getCodOcorrencia())
				.setDsOcorrencia(ocorrenciaOld.getDsOcorrencia())
				.build();
	}

}
