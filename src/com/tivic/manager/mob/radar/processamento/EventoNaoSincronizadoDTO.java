package com.tivic.manager.mob.radar.processamento;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class EventoNaoSincronizadoDTO {
	private int cdEvento;
	private String mensagem;
	
	public EventoNaoSincronizadoDTO() {}
	
	public int getCdEvento() {
		return cdEvento;
	}

	public void setCdEvento(int cdEvento) {
		this.cdEvento = cdEvento;
	}

	public String getMensagem() {
		return mensagem;
	}

	public void setMensagem(String mensagem) {
		this.mensagem = mensagem;
	}

	@Override
	public String toString() {
		try {
			return new ObjectMapper().writeValueAsString(this);
		}
		catch (JsonProcessingException e) {
			e.printStackTrace();
			return "Não foi possível serializar o objeto informado";
		}
	}
}