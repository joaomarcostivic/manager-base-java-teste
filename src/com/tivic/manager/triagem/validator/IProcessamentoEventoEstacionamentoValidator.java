package com.tivic.manager.triagem.validator;

import com.tivic.manager.mob.EventoEquipamento;
import com.tivic.manager.triagem.dtos.ProcessamentoEventoDTO;
import com.tivic.sol.connection.CustomConnection;

public interface IProcessamentoEventoEstacionamentoValidator {
    void validate(ProcessamentoEventoDTO dto, EventoEquipamento evento, CustomConnection customConnection) throws Exception;
}