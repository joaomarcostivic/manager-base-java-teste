package com.tivic.manager.mob.radar.processamento;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tivic.manager.mob.EventoEquipamento;

public class ProcessamentoEventosDTO {
	private int qtdEventosProcessados = 0;
	private List<EventoNaoSincronizadoDTO> eventosNaoProcessados;
	
	public ProcessamentoEventosDTO() {
		this.eventosNaoProcessados = new ArrayList<EventoNaoSincronizadoDTO>();
	}
	
	public int getNrEventosProcessados() {
		return qtdEventosProcessados;
	}
	
	public void setNrEventosProcessados(int qtdEventosProcessados) {
		this.qtdEventosProcessados = qtdEventosProcessados;
	}
	
	public List<EventoNaoSincronizadoDTO> getEventosNaoProcessados() {
		return eventosNaoProcessados;
	}
	
	public void setEventosNaoProcessados(List<EventoNaoSincronizadoDTO> eventosNaoProcessados) {
		this.eventosNaoProcessados = eventosNaoProcessados;
	}
	
	public void addEventoProcessado() {
		qtdEventosProcessados++;
	}
	
	public void addEventoNaoProcessado(EventoEquipamento evento, String mensagem) {
		EventoNaoSincronizadoDTO aitMensagemDTO = new EventoNaoSincronizadoDTO();
		aitMensagemDTO.setCdEvento(evento.getCdEvento());
		aitMensagemDTO.setMensagem(mensagem);
		this.eventosNaoProcessados.add(aitMensagemDTO);
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
