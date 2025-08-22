package com.tivic.manager.mob.ait.sync.builders;

import com.tivic.manager.mob.InfracaoObservacao;
import com.tivic.manager.mob.ait.sync.entities.InfracaoObservacaoSyncDTO;

public class InfracaoObservacaoSyncDTOBuilder {

	private InfracaoObservacao infracaoObservacao;
	private InfracaoObservacaoSyncDTO infracaoObservacaoDTO;
	
	public InfracaoObservacaoSyncDTOBuilder(InfracaoObservacao infracaoObservacao) {
		this.infracaoObservacao = infracaoObservacao;
		this.infracaoObservacaoDTO = new InfracaoObservacaoSyncDTO();
		setInfracaoObservacaoDTO();
	}
	
	private void setInfracaoObservacaoDTO() {
		this.infracaoObservacaoDTO.setCdInfracao(this.infracaoObservacao.getCdInfracao());
		this.infracaoObservacaoDTO.setCdObservacao(this.infracaoObservacao.getCdObservacao());
		this.infracaoObservacaoDTO.setNrObservacao(this.infracaoObservacao.getNrObservacao());
		this.infracaoObservacaoDTO.setNmObservacao(this.infracaoObservacao.getNmObservacao());
		this.infracaoObservacaoDTO.setTxtObservacao(this.infracaoObservacao.getTxtObservacao());
	}
	
	public InfracaoObservacaoSyncDTO build() {
		return this.infracaoObservacaoDTO;
	}
}
