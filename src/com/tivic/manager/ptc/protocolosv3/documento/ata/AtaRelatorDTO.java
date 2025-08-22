package com.tivic.manager.ptc.protocolosv3.documento.ata;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class AtaRelatorDTO {	
	private String nmVinculo;
	private String nmPessoa;
	
	public String getNmVinculo() {
		return nmVinculo;
	}
	
	public void setNmVinculo(String nmVinculo) {
		this.nmVinculo = nmVinculo;
	}
	
	public String getNmPessoa() {
		return nmPessoa;
	}
	
	public void setNmPessoa(String nmPessoa) {
		this.nmPessoa = nmPessoa;
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
