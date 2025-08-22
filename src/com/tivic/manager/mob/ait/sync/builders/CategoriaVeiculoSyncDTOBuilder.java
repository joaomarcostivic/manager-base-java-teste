package com.tivic.manager.mob.ait.sync.builders;

import com.tivic.manager.fta.CategoriaVeiculo;
import com.tivic.manager.mob.ait.sync.entities.CategoriaVeiculoSyncDTO;

public class CategoriaVeiculoSyncDTOBuilder {
	
	private CategoriaVeiculo categoriaVeiculo;
	private CategoriaVeiculoSyncDTO categoriaVeiculoSyncDTO;
	
	
	public CategoriaVeiculoSyncDTOBuilder(CategoriaVeiculo categoriaVeiculo) {
		this.categoriaVeiculo = categoriaVeiculo;
		this.categoriaVeiculoSyncDTO = new CategoriaVeiculoSyncDTO();
		setCategoriaVeiculoDTO();
	}
	
	private void setCategoriaVeiculoDTO() {
		this.categoriaVeiculoSyncDTO.setCdCategoria(this.categoriaVeiculo.getCdCategoria());
		this.categoriaVeiculoSyncDTO.setNmCategoria(this.categoriaVeiculo.getNmCategoria());
	}
	
	public CategoriaVeiculoSyncDTO build() {
		return this.categoriaVeiculoSyncDTO;
	}
}
