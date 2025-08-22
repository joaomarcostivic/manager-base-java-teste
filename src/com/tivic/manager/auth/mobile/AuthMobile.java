package com.tivic.manager.auth.mobile;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tivic.sol.auth.Auth;

public class AuthMobile extends Auth {

	private String idEquipamento;

	public String getIdEquipamento() {
		return idEquipamento;
	}

	public void setIdEquipamento(String idEquipamento) {
		this.idEquipamento = idEquipamento;
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
