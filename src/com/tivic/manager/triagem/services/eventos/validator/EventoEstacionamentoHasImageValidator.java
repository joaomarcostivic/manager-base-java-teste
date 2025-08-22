package com.tivic.manager.triagem.services.eventos.validator;

import com.tivic.manager.triagem.dtos.NotificacaoEstacionamentoDigitalDTO;

public class EventoEstacionamentoHasImageValidator implements ICapturaEventoEstacionamentoValidator {
	
	
	public EventoEstacionamentoHasImageValidator() throws Exception { }

	@Override
	public boolean validate(NotificacaoEstacionamentoDigitalDTO notificacao, CapturaEventoEstacionamentoValidationStatistics stats) {
	    if (notificacao.getImagemNotificacaoDTOList() == null || notificacao.getImagemNotificacaoDTOList().isEmpty()) {
	        stats.incrementFailedByMissingImages();
	        return false;
	    }
	    return true;
	}

}
