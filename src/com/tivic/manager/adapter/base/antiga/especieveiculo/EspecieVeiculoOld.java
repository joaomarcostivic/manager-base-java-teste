package com.tivic.manager.adapter.base.antiga.especieveiculo;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class EspecieVeiculoOld {
	private int codEspecie;
	private String dsEspecie;
	
	public EspecieVeiculoOld() {}
	
	public EspecieVeiculoOld(
			int codEspecie,
			String dsEspecie) {
		setCodEspecie(codEspecie);
		setDsEspecie(dsEspecie);
	}

	public int getCodEspecie() {
		return codEspecie;
	}

	public void setCodEspecie(int codEspecie) {
		this.codEspecie = codEspecie;
	}

	public String getDsEspecie() {
		return dsEspecie;
	}

	public void setDsEspecie(String dsEspecie) {
		this.dsEspecie = dsEspecie;
	}

	@Override
	public String toString() {
		try {
			return new ObjectMapper().writeValueAsString(this);
		}
		catch (JsonProcessingException e) {
			return "Não foi possível serializar o objeto informado";
		}
	}
	
}
