package com.tivic.manager.triagem.services.eventos.validator;

import com.tivic.manager.triagem.dtos.NotificacaoEstacionamentoDigitalDTO;

public class EventoEstacionamentoHasEnderecoValidator implements ICapturaEventoEstacionamentoValidator {
	
	
	public EventoEstacionamentoHasEnderecoValidator() throws Exception { }

	@Override
	public boolean validate(NotificacaoEstacionamentoDigitalDTO notificacao, CapturaEventoEstacionamentoValidationStatistics stats) {
	    String nrImovel = notificacao.getNrImovelReferencia();
	    String nmRua = notificacao.getNmRuaNotificacao();

	    if (nrImovel == null || nrImovel.isEmpty() || 
	        nmRua == null || nmRua.isEmpty()) {
	        stats.incrementFailedByMissingAddress();
	        return false;
	    }
	    return true;
	}

}
