package com.tivic.manager.mob.ait.sync.builders;

import com.tivic.manager.grl.Bairro;
import com.tivic.manager.mob.ait.sync.entities.BairroSyncDTO;

public class BairroSyncDTOBuilder {
	
	private Bairro bairro;
	private BairroSyncDTO bairroSyncDTO;
	
	
	public BairroSyncDTOBuilder(Bairro bairro) {
		this.bairro = bairro;
		this.bairroSyncDTO = new BairroSyncDTO();
		setBairroDTO();
	}
	
	private void setBairroDTO() {
		this.bairroSyncDTO.setCdBairro(this.bairro.getCdBairro());
		this.bairroSyncDTO.setNmBairro(this.bairro.getNmBairro());
	}
	
	public BairroSyncDTO build() {
		return this.bairroSyncDTO;
	}
}
