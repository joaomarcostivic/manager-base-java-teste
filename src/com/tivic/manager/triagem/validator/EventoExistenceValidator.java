package com.tivic.manager.triagem.validator;

import com.tivic.manager.mob.EventoEquipamento;
import com.tivic.manager.triagem.dtos.ProcessamentoEventoDTO;
import com.tivic.manager.triagem.exceptions.EventoEstacionamentoNotFoundException;
import com.tivic.sol.connection.CustomConnection;

public class EventoExistenceValidator implements IProcessamentoEventoEstacionamentoValidator {
    @Override
    public void validate(ProcessamentoEventoDTO dto, EventoEquipamento evento, CustomConnection customConnection) throws Exception {
        if (evento == null) {
            throw new EventoEstacionamentoNotFoundException(dto.getCdEvento());
        }
    }
}