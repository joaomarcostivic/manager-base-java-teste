package com.tivic.manager.triagem.services.eventos.validator;

import java.util.ArrayList;
import java.util.List;

import com.tivic.manager.triagem.dtos.NotificacaoEstacionamentoDigitalDTO;

public class CapturaEventoEstacionamentoValidatorBuilder {
    private final List<ICapturaEventoEstacionamentoValidator> validators;

    public CapturaEventoEstacionamentoValidatorBuilder() throws Exception {
        validators = new ArrayList<>();
        validators.add(new EventoEstacionamentoExistsValidator());
        validators.add(new EventoEstacionamentoHasImageValidator());
        validators.add(new EventoEstacionamentoHasEnderecoValidator());
    }

    public boolean validate(NotificacaoEstacionamentoDigitalDTO notificacao, CapturaEventoEstacionamentoValidationStatistics stats) throws Exception {
        for (ICapturaEventoEstacionamentoValidator validator : validators) {
            if (!validator.validate(notificacao, stats)) {
                return false;
            }
        }
        return true;
    }
}
