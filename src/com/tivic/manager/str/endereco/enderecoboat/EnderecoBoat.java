package com.tivic.manager.str.endereco.enderecoboat;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class EnderecoBoat {
	private int cdBoat;
	private int cdEndereco;
	
	public EnderecoBoat() {}
	
	public EnderecoBoat(int cdEndereco, int cdBoat) {
		this.cdEndereco = cdEndereco;
		this.cdBoat = cdBoat;
	}
	
	public int getCdBoat() {
		return cdBoat;
	}

	public void setCdBoat(int cdBoat) {
		this.cdBoat = cdBoat;
	}

	public int getCdEndereco() {
		return cdEndereco;
	}

	public void setCdEndereco(int cdEndereco) {
		this.cdEndereco = cdEndereco;
	}

	@Override
	public String toString() {
		try {
			return new ObjectMapper().writeValueAsString(this);
		} catch (JsonProcessingException e) {
			return "Não foi possivel serializar o objeto";
		}
	}
}
