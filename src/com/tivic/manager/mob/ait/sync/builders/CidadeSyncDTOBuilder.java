package com.tivic.manager.mob.ait.sync.builders;

import com.tivic.manager.grl.Cidade;
import com.tivic.manager.mob.ait.sync.entities.CidadeSyncDTO;

public class CidadeSyncDTOBuilder {
	
	private Cidade cidade;
	private CidadeSyncDTO cidadeDTO;
	
	public CidadeSyncDTOBuilder(Cidade cidade) {
		this.cidade = cidade;
		this.cidadeDTO = new CidadeSyncDTO();
		setCidadeDTO();
	}
	
	private void setCidadeDTO() {
		this.cidadeDTO.setCdCidade(this.cidade.getCdCidade());
		this.cidadeDTO.setNmCidade(this.cidade.getNmCidade());
		this.cidadeDTO.setCdEstado(this.cidade.getCdEstado());
		this.cidadeDTO.setIdCidade(this.cidade.getIdCidade());
	}

	public CidadeSyncDTO build() {
		return this.cidadeDTO;
	}
}
