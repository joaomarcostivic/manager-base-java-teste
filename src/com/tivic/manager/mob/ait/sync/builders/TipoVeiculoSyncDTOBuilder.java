package com.tivic.manager.mob.ait.sync.builders;

import com.tivic.manager.fta.TipoVeiculo;
import com.tivic.manager.mob.ait.sync.entities.TipoVeiculoSyncDTO;

public class TipoVeiculoSyncDTOBuilder {
	
	private TipoVeiculo tipoVeiculo;
	private TipoVeiculoSyncDTO tipoVeiculoDTO;
	
	
	public TipoVeiculoSyncDTOBuilder(TipoVeiculo tipoVeiculo) {
		this.tipoVeiculo = tipoVeiculo;
		this.tipoVeiculoDTO = new TipoVeiculoSyncDTO();
		setTipoVeiculoDTO();
	}
	
	private void setTipoVeiculoDTO() {
		this.tipoVeiculoDTO.setCdTipoVeiculo(this.tipoVeiculo.getCdTipoVeiculo());
		this.tipoVeiculoDTO.setNmTipoVeiculo(this.tipoVeiculo.getNmTipoVeiculo());
	}
	
	public TipoVeiculoSyncDTO build() {
		return this.tipoVeiculoDTO;
	}
}
