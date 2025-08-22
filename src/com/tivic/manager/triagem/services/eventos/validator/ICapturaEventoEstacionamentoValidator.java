package com.tivic.manager.triagem.services.eventos.validator;

import com.tivic.manager.triagem.dtos.NotificacaoEstacionamentoDigitalDTO;

public interface ICapturaEventoEstacionamentoValidator {
	public boolean validate(NotificacaoEstacionamentoDigitalDTO notificacao, CapturaEventoEstacionamentoValidationStatistics stats) throws Exception;
}
