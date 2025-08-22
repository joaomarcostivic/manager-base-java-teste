package com.tivic.manager.mob.ait.sync.entities;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.JsonProcessingException;

public class BairroSyncDTO {

	private int cdBairro;
	private String nmBairro;
	
	public int getCdBairro() {
		return cdBairro;
	}
	
	public void setCdBairro(int cdBairro) {
		this.cdBairro = cdBairro;
	}
	
	public String getNmBairro() {
		return nmBairro;
	}
	
	public void setNmBairro(String nmBairro) {
		this.nmBairro = nmBairro;
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
