package com.tivic.manager.mob.ait.sync.builders;

import com.tivic.manager.grl.Pais;
import com.tivic.manager.mob.ait.sync.entities.PaisSyncDTO;

public class PaisSyncDTOBuilder {
	
	private Pais pais;
	private PaisSyncDTO paisDTO;
	
	public PaisSyncDTOBuilder(Pais pais) {
		this.pais = pais;
		this.paisDTO = new PaisSyncDTO();
		setPaisDTO();
	}
	
	private void setPaisDTO() {
		this.paisDTO.setCdPais(this.pais.getCdPais());
		this.paisDTO.setNmPais(this.pais.getNmPais());
		this.paisDTO.setSgPais(this.pais.getSgPais());
	}
	
	public PaisSyncDTO build() {
		return this.paisDTO;
	}
}
