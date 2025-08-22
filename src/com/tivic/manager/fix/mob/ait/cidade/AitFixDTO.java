package com.tivic.manager.fix.mob.ait.cidade;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class AitFixDTO {
	private int cdAit;
	private String ufPlacaProdemge;
	private String idCidadeProdemge;
	
	public int getCdAit() {
		return cdAit;
	}

	public void setCdAit(int cdAit) {
		this.cdAit = cdAit;
	}

	public String getUfPlacaProdemge() {
		return ufPlacaProdemge;
	}
	
	public void setUfPlacaProdemge(String ufPlacaProdemge) {
		this.ufPlacaProdemge = ufPlacaProdemge;
	}
	
	public String getIdCidadeProdemge() {
		return idCidadeProdemge;
	}
	
	public void setIdCidadeProdemge(String idCidadeProdemge) {
		this.idCidadeProdemge = idCidadeProdemge;
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
