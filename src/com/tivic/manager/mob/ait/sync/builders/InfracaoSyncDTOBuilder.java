package com.tivic.manager.mob.ait.sync.builders;

import com.tivic.manager.mob.Infracao;
import com.tivic.manager.mob.ait.sync.entities.InfracaoSyncDTO;

public class InfracaoSyncDTOBuilder {
	
	private Infracao infracao;
	private InfracaoSyncDTO infracaoDTO;
	
	public InfracaoSyncDTOBuilder(Infracao infracao) {
		this.infracao = infracao;
		this.infracaoDTO = new InfracaoSyncDTO();
		setInfracaoDTO();
	}
	
	private void setInfracaoDTO() {
		this.infracaoDTO.setCdInfracao(this.infracao.getCdInfracao());
		this.infracaoDTO.setDsInfracao(this.infracao.getDsInfracao());
		this.infracaoDTO.setNrPontuacao(this.infracao.getNrPontuacao());
		this.infracaoDTO.setNrCodDetran(this.infracao.getNrCodDetran());
		this.infracaoDTO.setNmNatureza(this.infracao.getNmNatureza());
		this.infracaoDTO.setNrArtigo(this.infracao.getNrArtigo());
		this.infracaoDTO.setNrParagrafo(this.infracao.getNrParagrafo());
		this.infracaoDTO.setNrInciso(this.infracao.getNrInciso());
		this.infracaoDTO.setNrAlinea(this.infracao.getNrAlinea());
		this.infracaoDTO.setTpCompetencia(this.infracao.getTpCompetencia());
		this.infracaoDTO.setLgPrioritaria(this.infracao.getLgPrioritaria());
		if(this.infracao.getDtFimVigencia() != null) {			
			this.infracaoDTO.setDtFimVigencia( this.infracao.getDtFimVigencia().getTime().getTime());
		}
		this.infracaoDTO.setVlInfracao(this.infracao.getVlInfracao());
		this.infracaoDTO.setLgSuspensaoCnh(this.infracao.getLgSuspensaoCnh());
		this.infracaoDTO.setTpResponsabilidade(this.infracao.getTpResponsabilidade());
	}
	
	public InfracaoSyncDTO build() {
		return this.infracaoDTO;
	}
}
