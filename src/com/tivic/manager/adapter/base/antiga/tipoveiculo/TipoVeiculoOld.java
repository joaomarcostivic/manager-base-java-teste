package com.tivic.manager.adapter.base.antiga.tipoveiculo;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class TipoVeiculoOld {
	private int codTipo;
	private String nmTipo;
	
	public TipoVeiculoOld() {}
	
	public TipoVeiculoOld(
			int codTipo,
			String nmTipo
	){
		setCodTipo(codTipo);
		setNmTipo(nmTipo);
	}
	
	public int getCodTipo() {
		return codTipo;
	}
	
	public void setCodTipo(int codTipo) {
		this.codTipo = codTipo;
	}
	
	public String getNmTipo() {
		return nmTipo;
	}
	
	public void setNmTipo(String nmTipo) {
		this.nmTipo = nmTipo;
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
