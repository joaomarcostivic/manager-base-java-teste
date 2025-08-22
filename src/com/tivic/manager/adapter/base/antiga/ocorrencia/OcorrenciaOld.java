package com.tivic.manager.adapter.base.antiga.ocorrencia;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class OcorrenciaOld {
	private int codOcorrencia;
	private String dsOcorrencia;
	
	public OcorrenciaOld() {}
	
	public OcorrenciaOld(
			int codOcorrencia,
			String dsOcorrencia) {
		setCodOcorrencia(codOcorrencia);
		setDsOcorrencia(dsOcorrencia);
	}

	public int getCodOcorrencia() {
		return codOcorrencia;
	}

	public void setCodOcorrencia(int codOcorrencia) {
		this.codOcorrencia = codOcorrencia;
	}

	public String getDsOcorrencia() {
		return dsOcorrencia;
	}

	public void setDsOcorrencia(String dsOcorrencia) {
		this.dsOcorrencia = dsOcorrencia;
	}

	@Override
	public String toString() {
		try {
			return new ObjectMapper().writeValueAsString(this);
		}
		catch (JsonProcessingException e) {
			return "Não foi possível serializar o objeto informado";
		}
	}
	
}
