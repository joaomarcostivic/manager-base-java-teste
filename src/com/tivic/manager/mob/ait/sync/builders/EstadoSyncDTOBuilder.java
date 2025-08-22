package com.tivic.manager.mob.ait.sync.builders;

import com.tivic.manager.grl.Estado;
import com.tivic.manager.mob.ait.sync.entities.EstadoSyncDTO;

public class EstadoSyncDTOBuilder {
	
	private Estado estado;
	private EstadoSyncDTO estadoDTO;

	public EstadoSyncDTOBuilder(Estado estado) {
		this.estado = estado;
		this.estadoDTO = new EstadoSyncDTO();
		setEstadoDTO();
	}
	
	private void setEstadoDTO() {
		this.estadoDTO.setCdEstado(this.estado.getCdEstado());
		this.estadoDTO.setCdPais(this.estado.getCdPais());
		this.estadoDTO.setNmEstado(this.estado.getNmEstado());
		this.estadoDTO.setSgEstado(this.estado.getSgEstado());
	}
	
	public EstadoSyncDTO build() {
		return this.estadoDTO;
	}
}
