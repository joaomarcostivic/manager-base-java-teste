package com.tivic.manager.mob.radar.sync;

import java.util.ArrayList;
import java.util.List;

import com.tivic.manager.mob.Ait;
import com.tivic.manager.mob.EventoEquipamento;

public class AitSyncDTO {

	private List<Ait> aitsEmitidos;
	private List<EventoSyncMensagemDTO> eventosNaoEmitidos;
	
	public AitSyncDTO() {
		aitsEmitidos = new ArrayList<Ait>();
		eventosNaoEmitidos = new ArrayList<EventoSyncMensagemDTO>();
	}
	
	public List<EventoSyncMensagemDTO> getEventosNaoEmitidos() {
		return eventosNaoEmitidos;
	}
	
	public void setEventosNaoEmitidos(List<EventoSyncMensagemDTO> eventoNaoEmitidos) {
		this.eventosNaoEmitidos = eventoNaoEmitidos;
	}
	
	public void addEventosNaoEmitidos(EventoEquipamento evento, String mensagem) {
		EventoSyncMensagemDTO aitMensagemDTO = new EventoSyncMensagemDTO();
		aitMensagemDTO.setEvento(evento);
		aitMensagemDTO.setMensagem(mensagem);
		this.eventosNaoEmitidos.add(aitMensagemDTO);
	}
	
	public List<Ait> getAitsEmitidos() {
		return aitsEmitidos;
	}
	
	public void setAitsEmitidos(List<Ait> aitsEmitidos) {
		this.aitsEmitidos = aitsEmitidos;
	}

	public void addAitEmitido(Ait ait) {
		this.aitsEmitidos.add(ait);
	}
	
}