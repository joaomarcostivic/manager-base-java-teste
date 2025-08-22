package com.tivic.manager.mob.ecarta.dtos;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ArquivoServicoDTO {
	
	private String nmArquivo;
	private byte[] arquivoServico;
	
	public String getNmArquivo() {
		return nmArquivo;
	}
	public void setNmArquivo(String nmArquivo) {
		this.nmArquivo = nmArquivo;
	}
	public byte[] getArquivoServico() {
		return arquivoServico;
	}
	public void setArquivoServico(byte[] arquivoServico) {
		this.arquivoServico = arquivoServico;
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
