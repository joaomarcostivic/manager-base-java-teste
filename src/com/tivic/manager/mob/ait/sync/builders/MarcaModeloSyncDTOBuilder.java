package com.tivic.manager.mob.ait.sync.builders;

import com.tivic.manager.fta.MarcaModelo;
import com.tivic.manager.mob.ait.sync.entities.MarcaModeloSyncDTO;

public class MarcaModeloSyncDTOBuilder {
	
	private MarcaModelo marcaModelo;
	private MarcaModeloSyncDTO marcaModeloDTO;
	
	
	public MarcaModeloSyncDTOBuilder(MarcaModelo marcaModelo) {
		this.marcaModelo = marcaModelo;
		this.marcaModeloDTO = new MarcaModeloSyncDTO();
		setMarcaModeloDTO();
	}
	
	private void setMarcaModeloDTO() {
		this.marcaModeloDTO.setCdMarca(this.marcaModelo.getCdMarca());
		this.marcaModeloDTO.setNmMarca(this.marcaModelo.getNmMarca());
		this.marcaModeloDTO.setNmModelo(this.marcaModelo.getNmModelo());
	}
	
	public MarcaModeloSyncDTO build() {
		return this.marcaModeloDTO;
	}
}
