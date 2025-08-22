package com.tivic.manager.mob.lote.impressao;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class QuantidadePertencenteLoteDTO{
	private int qtdCdLoteImpressao;

	public int getQtdCdLoteImpressao() {
		return qtdCdLoteImpressao;
	}

	public void setQtdCdLoteImpressao(int qtdCdLoteImpressao) {
		this.qtdCdLoteImpressao = qtdCdLoteImpressao;
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
