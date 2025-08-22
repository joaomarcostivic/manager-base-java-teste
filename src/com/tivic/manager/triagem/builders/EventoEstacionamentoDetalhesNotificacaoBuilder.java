package com.tivic.manager.triagem.builders;

import com.tivic.manager.triagem.dtos.NotificacaoEstacionamentoDigitalDTO;
import com.tivic.manager.triagem.entities.EventoEstacionamentoDigitalDetalhes;

public class EventoEstacionamentoDetalhesNotificacaoBuilder {
	
	private EventoEstacionamentoDigitalDetalhes eventoEstacionamentoDigitalDetalhes;
	
	public EventoEstacionamentoDetalhesNotificacaoBuilder() {
		eventoEstacionamentoDigitalDetalhes = new EventoEstacionamentoDigitalDetalhes();
	}
	
	public EventoEstacionamentoDetalhesNotificacaoBuilder notificacaoEstacionamentoDigital(NotificacaoEstacionamentoDigitalDTO notificacaoEstacionamentoDigitalDTO, int cdEvento) {
		eventoEstacionamentoDigitalDetalhes = new EventoEstacionamentoDigitalDetalhesBuilder()
				.cdEvento(cdEvento)
				.idDispositivo(notificacaoEstacionamentoDigitalDTO.getIdDispositivo())
				.nrVaga(notificacaoEstacionamentoDigitalDTO.getNrVaga())
				.nmRuaNotificacao(notificacaoEstacionamentoDigitalDTO.getNmRuaNotificacao())
				.nrImovelReferencia(notificacaoEstacionamentoDigitalDTO.getNrImovelReferencia())
				.idColaborador(notificacaoEstacionamentoDigitalDTO.getIdColaborador())
				.build();
		return this;
	}
	
	public EventoEstacionamentoDigitalDetalhes build() {
		return eventoEstacionamentoDigitalDetalhes;
	}

}
