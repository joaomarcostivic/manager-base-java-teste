package com.tivic.manager.mob.radar.sync;

import com.tivic.manager.mob.EventoEquipamento;

public class EventoSyncMensagemDTO {

	private EventoEquipamento evento;
	private String mensagem;
	
	public EventoEquipamento getEvento() {
		return evento;
	}
	
	public void setEvento(EventoEquipamento evento) {
		this.evento = evento;
	}
	
	public String getMensagem() {
		return mensagem;
	}
	
	public void setMensagem(String mensagem) {
		this.mensagem = mensagem;
	}
	
}