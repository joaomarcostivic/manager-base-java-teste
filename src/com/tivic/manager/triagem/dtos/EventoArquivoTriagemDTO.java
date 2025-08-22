package com.tivic.manager.triagem.dtos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@JsonIgnoreProperties(ignoreUnknown = true)
public class EventoArquivoTriagemDTO {
	private int cdEvento;
	private int cdArquivo;
	private int lgImpressao;
	
	public int getCdEvento() {
		return cdEvento;
	}
	
	public void setCdEvento(int cdEvento) {
		this.cdEvento = cdEvento;
	}
	
	public int getCdArquivo() {
		return cdArquivo;
	}
	
	public void setCdArquivo(int cdArquivo) {
		this.cdArquivo = cdArquivo;
	}
	
	public int getLgImpressao() {
		return lgImpressao;
	}
	
	public void setLgImpressao(int lgImpressao) {
		this.lgImpressao = lgImpressao;
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
