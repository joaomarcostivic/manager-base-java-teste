package com.tivic.manager.adapter.base.antiga.ocorrencia;

public class OcorrenciaOldBuilder {
	private OcorrenciaOld ocorrenciaOld;
	
	public OcorrenciaOldBuilder() {
    	this.ocorrenciaOld = new OcorrenciaOld();
    }
	
	public OcorrenciaOldBuilder setCodOcorrencia(int codOcorrencia) {
		ocorrenciaOld.setCodOcorrencia(codOcorrencia);
        return this;
    }
	
	public OcorrenciaOldBuilder setDsOcorrencia(String dsOcorrencia) {
		ocorrenciaOld.setDsOcorrencia(dsOcorrencia);
        return this;
    }
	
	public OcorrenciaOld build() {
		return this.ocorrenciaOld;
	}
	
}
