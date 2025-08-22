package com.tivic.manager.mob.ait.sync.entities;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.JsonProcessingException;

public class CategoriaVeiculoSyncDTO {

	private int cdCategoria;
	private String nmCategoria;
	
	public int getCdCategoria() {
		return cdCategoria;
	}
	
	public void setCdCategoria(int cdCategoria) {
		this.cdCategoria = cdCategoria;
	}
	
	public String getNmCategoria() {
		return nmCategoria;
	}
	
	public void setNmCategoria(String nmCategoria) {
		this.nmCategoria = nmCategoria;
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
