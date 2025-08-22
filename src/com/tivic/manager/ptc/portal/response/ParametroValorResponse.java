package com.tivic.manager.ptc.portal.response;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ParametroValorResponse {
	
	private String vlInicial;

	public String getvlInicial() {
		return vlInicial;
	}

	public void setvlIncial(String vlInicial) {
		this.vlInicial = vlInicial;
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
