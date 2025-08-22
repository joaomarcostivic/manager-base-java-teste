package com.tivic.manager.triagem.builders;

import com.tivic.manager.triagem.dtos.NotificacaoEstacionamentoDigitalDTO;
import com.tivic.manager.triagem.entities.EventoEstacionamentoDigital;
import com.tivic.manager.triagem.enums.StEventoEstacionamentoDigitalEnum;

public class EventoEstacionamentoNotificacaoBuilder {
	
	private EventoEstacionamentoDigital eventoEstacionamentoDigital;
	
	public EventoEstacionamentoNotificacaoBuilder() {
		eventoEstacionamentoDigital = new EventoEstacionamentoDigital();
	}
	
	public EventoEstacionamentoNotificacaoBuilder notificacaoEstacionamentoDigital(NotificacaoEstacionamentoDigitalDTO notificacaoEstacionamentoDigitalDTO, int cdEvento) {
		eventoEstacionamentoDigital = new EventoEstacionamentoDigitalBuilder()
				.cdEvento(cdEvento)
				.nrNotificacao(notificacaoEstacionamentoDigitalDTO.getNrNotificacao())
				.dsNotificacao(notificacaoEstacionamentoDigitalDTO.getDsNotificacao())
				.stNotificacao(StEventoEstacionamentoDigitalEnum.PENDETE.getKey())
				.build();
		return this;
	}
	
	public EventoEstacionamentoDigital build() {
		return eventoEstacionamentoDigital;
	}

}
