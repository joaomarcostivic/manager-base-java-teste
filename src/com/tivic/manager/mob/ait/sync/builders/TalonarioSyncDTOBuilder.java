package com.tivic.manager.mob.ait.sync.builders;

import com.tivic.manager.mob.Talonario;
import com.tivic.manager.mob.ait.sync.entities.TalonarioSyncDTO;

public class TalonarioSyncDTOBuilder {
	
	private Talonario talonario;
	private TalonarioSyncDTO talonarioDTO;
	
	public TalonarioSyncDTOBuilder(Talonario talonario) {
		this.talonario = talonario;
		this.talonarioDTO = new TalonarioSyncDTO();
		setTalonarioDTO();
	}
	
	private void setTalonarioDTO() {
		this.talonarioDTO.setCdTalao(this.talonario.getCdTalao());
		this.talonarioDTO.setNrTalao(this.talonario.getNrTalao());
		this.talonarioDTO.setNrInicial(this.talonario.getNrInicial());
		this.talonarioDTO.setNrFinal(this.talonario.getNrFinal());
		this.talonarioDTO.setCdAgente(this.talonario.getCdAgente());
		if(this.talonario.getDtEntrega() != null) {
			this.talonarioDTO.setDtEntrega(this.talonario.getDtEntrega().getTime().getTime());
		}
		if(this.talonario.getDtDevolucao() != null) {			
			this.talonarioDTO.setDtDevolucao(this.talonario.getDtDevolucao().getTime().getTime());
		}
		this.talonarioDTO.setStTalao(this.talonario.getStTalao());
		this.talonarioDTO.setTpTalao(this.talonario.getTpTalao());
		this.talonarioDTO.setSgTalao(this.talonario.getSgTalao());
		this.talonarioDTO.setNrUltimoAit(this.talonario.getNrUltimoAit());
	}
	
	public TalonarioSyncDTO build() {
		return this.talonarioDTO;
	}
}
