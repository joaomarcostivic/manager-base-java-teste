package com.tivic.manager.mob.lote.impressao.remessacorreios.arquivospostagem;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ArquivoRetornoCorreiosDTO {

	private String arquivoRetorno;
	private String nmArquivo;
	
	public String getArquivoRetorno() {
		return arquivoRetorno;
	}

	public void setArquivoRetorno(String arquivoRetorno) {
		this.arquivoRetorno = arquivoRetorno;
	}

	public String getNmArquivo() {
		return nmArquivo;
	}

	public void setNmArquivo(String nmArquivo) {
		this.nmArquivo = nmArquivo;
	}

	public String toString() {
		try {
			return new ObjectMapper().writeValueAsString(this);
		} catch (JsonProcessingException e) {
			return "Não foi possível serializar o objeto informado";
		}
	}
	
}
