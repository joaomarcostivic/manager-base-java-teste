package com.tivic.manager.mob.ait.sync.entities;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.JsonProcessingException;

public class PaisSyncDTO {

	private int cdPais;
	private String nmPais;
	private String sgPais;

	public int getCdPais() {
		return cdPais;
	}

	public void setCdPais(int cdPais) {
		this.cdPais = cdPais;
	}

	public String getNmPais() {
		return nmPais;
	}

	public void setNmPais(String nmPais) {
		this.nmPais = nmPais;
	}

	public String getSgPais() {
		return sgPais;
	}

	public void setSgPais(String sgPais) {
		this.sgPais = sgPais;
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
