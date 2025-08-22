package com.tivic.manager.mob.ait.sync.builders;

import com.tivic.manager.mob.Ocorrencia;
import com.tivic.manager.mob.ait.sync.entities.OcorrenciaSyncDTO;

public class OcorrenciaSyncDTOBuilder {
	
	private Ocorrencia ocorrencia;
	private OcorrenciaSyncDTO ocorrenciaDTO;

	public OcorrenciaSyncDTOBuilder(Ocorrencia ocorrencia) {
		this.ocorrencia = ocorrencia;
		this.ocorrenciaDTO = new OcorrenciaSyncDTO();
		setOcorrenciaDTO();
	}
	
	private void setOcorrenciaDTO() {
		this.ocorrenciaDTO.setCdOcorrencia(this.ocorrencia.getCdOcorrencia());
		this.ocorrenciaDTO.setDsOcorrencia(this.ocorrencia.getDsOcorrencia());
	}
	
	public OcorrenciaSyncDTO build() {
		return this.ocorrenciaDTO;
	}
}
