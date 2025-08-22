package com.tivic.manager.mob.ait.aitimagempessoa;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tivic.manager.mob.AitImagem;

public class AitImagemPessoa  extends AitImagem {
	
	private int cdAitPessoa;

	public int getCdAitPessoa() {
		return cdAitPessoa;
	}

	public void setCdAitPessoa(int cdAitPessoa) {
		this.cdAitPessoa = cdAitPessoa;
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
