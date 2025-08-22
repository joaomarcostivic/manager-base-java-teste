package com.tivic.manager.adapter.base.antiga.ocorrencia;

import com.tivic.manager.mob.Ocorrencia;

public class OcorrenciaBuilder {
	private Ocorrencia ocorrencia;
	
	public OcorrenciaBuilder() {
    	this.ocorrencia = new Ocorrencia();
    }
	
	public OcorrenciaBuilder setCdOcorrencia(int cdOcorrencia) {
		ocorrencia.setCdOcorrencia(cdOcorrencia);
        return this;
    }
	
	public OcorrenciaBuilder setDsOcorrencia(String dsOcorrencia) {
		ocorrencia.setDsOcorrencia(dsOcorrencia);
        return this;
    }
	
	public Ocorrencia build() {
		return this.ocorrencia;
	}
	
}
