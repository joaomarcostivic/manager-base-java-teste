package com.tivic.manager.mob.ait.sync.entities;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.JsonProcessingException;

public class CidadeSyncDTO {

	private int cdCidade;
	private String nmCidade;
	private int cdEstado;
	private String idCidade;

	public int getCdCidade() {
		return cdCidade;
	}

	public void setCdCidade(int cdCidade) {
		this.cdCidade = cdCidade;
	}

	public String getNmCidade() {
		return nmCidade;
	}

	public void setNmCidade(String nmCidade) {
		this.nmCidade = nmCidade;
	}

	public int getCdEstado() {
		return cdEstado;
	}

	public void setCdEstado(int cdEstado) {
		this.cdEstado = cdEstado;
	}

	public String getIdCidade() {
		return idCidade;
	}

	public void setIdCidade(String idCidade) {
		this.idCidade = idCidade;
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
