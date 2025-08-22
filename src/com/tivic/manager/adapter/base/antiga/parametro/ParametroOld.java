package com.tivic.manager.adapter.base.antiga.parametro;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ParametroOld<T> {
	
	private String nmParametro;
	private String tpDadoParametro;
	private T valorParametro;
	
	public String getNmParametro() {
		return nmParametro;
	}
	
	public void setNmParametro(String nmParametro) {
		this.nmParametro = nmParametro;
	}

	public String getTpDadoParametro() {
		return tpDadoParametro;
	}
	
	public void setTpDadoParametro(String tpDadoParametro) {
		this.tpDadoParametro = tpDadoParametro;
	}
	
	public T getValorParametro() {
		return valorParametro;
	}
	
	public void setValorParametro(T valorParametro) {
		this.valorParametro = valorParametro;
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
