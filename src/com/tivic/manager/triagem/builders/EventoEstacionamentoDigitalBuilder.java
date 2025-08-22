package com.tivic.manager.triagem.builders;

import com.tivic.manager.triagem.entities.EventoEstacionamentoDigital;

public class EventoEstacionamentoDigitalBuilder {
	
	private EventoEstacionamentoDigital eventoEstacionamentoDigital;
	
	public EventoEstacionamentoDigitalBuilder() {
		eventoEstacionamentoDigital = new EventoEstacionamentoDigital();
	}
	
	public EventoEstacionamentoDigitalBuilder cdEvento(int cdEvento) {
		eventoEstacionamentoDigital.setCdEvento(cdEvento);
		return this;
	}
	
	public EventoEstacionamentoDigitalBuilder nrNotificacao(String nrNotificacao) {
		eventoEstacionamentoDigital.setNrNotificacao(nrNotificacao);
		return this;
	}
	
	public EventoEstacionamentoDigitalBuilder dsNotificacao(String dsNotificacao) {
		eventoEstacionamentoDigital.setDsNotificacao(dsNotificacao);
		return this;
	}
	
	public EventoEstacionamentoDigitalBuilder stNotificacao(int stNotificacao) {
		eventoEstacionamentoDigital.setStNotificacao(stNotificacao);
		return this;
	}
	
	public EventoEstacionamentoDigitalBuilder dsMotivoCancelamento(String dsMotivoCancelamento) {
		eventoEstacionamentoDigital.setDsMotivoCancelamento(dsMotivoCancelamento);
		return this;
	}
	
	public EventoEstacionamentoDigital build() {
		return eventoEstacionamentoDigital;
	}

}
