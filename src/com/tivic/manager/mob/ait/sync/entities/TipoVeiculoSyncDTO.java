package com.tivic.manager.mob.ait.sync.entities;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.JsonProcessingException;

public class TipoVeiculoSyncDTO {

	private int cdTipoVeiculo;
	private String nmTipoVeiculo;
	
	public int getCdTipoVeiculo() {
		return cdTipoVeiculo;
	}
	
	public void setCdTipoVeiculo(int cdTipoVeiculo) {
		this.cdTipoVeiculo = cdTipoVeiculo;
	}
	
	public String getNmTipoVeiculo() {
		return nmTipoVeiculo;
	}
	
	public void setNmTipoVeiculo(String nmTipoVeiculo) {
		this.nmTipoVeiculo = nmTipoVeiculo;
	}
	
	@Override
	public String toString() {
		try {
			return new ObjectMapper().writeValueAsString(this);
		} catch (JsonProcessingException e) {
			return "Não foi possível serializar o objeto informado";
		}
	}
}
