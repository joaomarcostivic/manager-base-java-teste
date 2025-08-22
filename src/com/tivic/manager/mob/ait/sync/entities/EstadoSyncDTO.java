package com.tivic.manager.mob.ait.sync.entities;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.JsonProcessingException;

public class EstadoSyncDTO {

	private int cdEstado;
	private int cdPais;
	private String nmEstado;
	private String sgEstado;

	public int getCdEstado() {
		return cdEstado;
	}

	public void setCdEstado(int cdEstado) {
		this.cdEstado = cdEstado;
	}

	public int getCdPais() {
		return cdPais;
	}

	public void setCdPais(int cdPais) {
		this.cdPais = cdPais;
	}

	public String getNmEstado() {
		return nmEstado;
	}

	public void setNmEstado(String nmEstado) {
		this.nmEstado = nmEstado;
	}

	public String getSgEstado() {
		return sgEstado;
	}

	public void setSgEstado(String sgEstado) {
		this.sgEstado = sgEstado;
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
