package com.tivic.manager.mob.ait.sync.entities;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.JsonProcessingException;

public class MarcaModeloSyncDTO {

	private int cdMarca;
	private String nmMarca;
	private String nmModelo;

	public int getCdMarca() {
		return cdMarca;
	}

	public void setCdMarca(int cdMarca) {
		this.cdMarca = cdMarca;
	}

	public String getNmMarca() {
		return nmMarca;
	}

	public void setNmMarca(String nmMarca) {
		this.nmMarca = nmMarca;
	}

	public String getNmModelo() {
		return nmModelo;
	}

	public void setNmModelo(String nmModelo) {
		this.nmModelo = nmModelo;
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
