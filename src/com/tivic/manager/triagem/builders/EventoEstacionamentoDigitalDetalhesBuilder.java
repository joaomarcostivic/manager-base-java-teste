package com.tivic.manager.triagem.builders;

import com.tivic.manager.triagem.entities.EventoEstacionamentoDigitalDetalhes;

public class EventoEstacionamentoDigitalDetalhesBuilder {

	private EventoEstacionamentoDigitalDetalhes eventoEstacionamentoDigitalDetalhes;
	
	public EventoEstacionamentoDigitalDetalhesBuilder() {
		eventoEstacionamentoDigitalDetalhes = new EventoEstacionamentoDigitalDetalhes();
	}
	
	public EventoEstacionamentoDigitalDetalhesBuilder cdEvento(int cdEvento) {
		eventoEstacionamentoDigitalDetalhes.setCdEvento(cdEvento);
		return this;
	}
	
	public EventoEstacionamentoDigitalDetalhesBuilder idDispositivo(String idDispositivo) {
		eventoEstacionamentoDigitalDetalhes.setIdDispositivo(idDispositivo);
		return this;
	}
	
	public EventoEstacionamentoDigitalDetalhesBuilder nrVaga(String nrVaga) {
		eventoEstacionamentoDigitalDetalhes.setNrVaga(nrVaga);
		return this;
	}
	
	public EventoEstacionamentoDigitalDetalhesBuilder nmRuaNotificacao(String nmRuaNotificacao) {
		eventoEstacionamentoDigitalDetalhes.setNmRuaNotificacao(nmRuaNotificacao);
		return this;
	}
	
	public EventoEstacionamentoDigitalDetalhesBuilder nrImovelReferencia(String nrImovelReferencia) {
		eventoEstacionamentoDigitalDetalhes.setNrImovelReferencia(nrImovelReferencia);
		return this;
	}
	
	public EventoEstacionamentoDigitalDetalhesBuilder idColaborador(String idColaborador) {
		eventoEstacionamentoDigitalDetalhes.setIdColaborador(idColaborador);
		return this;
	}
	
	public EventoEstacionamentoDigitalDetalhes build() {
		return eventoEstacionamentoDigitalDetalhes;
	}
	
}
