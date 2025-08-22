package com.tivic.manager.triagem.validator;

import com.tivic.manager.mob.EventoEquipamento;
import com.tivic.manager.triagem.dtos.ProcessamentoEventoDTO;
import com.tivic.manager.triagem.enums.SituacaoEventoEquipamentoEnum;
import com.tivic.manager.triagem.exceptions.ValidacaoSituacaoEventoEstacionamentoException;
import com.tivic.sol.connection.CustomConnection;

public class EventoSituacaoValidator implements IProcessamentoEventoEstacionamentoValidator {
	
    @Override
    public void validate(ProcessamentoEventoDTO dto, EventoEquipamento eventoEquipamento, CustomConnection customConnection) throws Exception {
        if (eventoEquipamento.getStEvento() != SituacaoEventoEquipamentoEnum.NAO_PROCESSADO.getKey()) {
            throw new ValidacaoSituacaoEventoEstacionamentoException(
                String.format("Evento %d já está com situação %s e não pode ser processado novamente", 
                    dto.getCdEvento(), SituacaoEventoEquipamentoEnum.valueOf(eventoEquipamento.getStEvento()))
            );
        }
        
        if (dto.getStEvento() != SituacaoEventoEquipamentoEnum.CONFIRMADO.getKey() && 
            dto.getStEvento() != SituacaoEventoEquipamentoEnum.CANCELADO.getKey()) {
            throw new ValidacaoSituacaoEventoEstacionamentoException(
                String.format("Situação %d inválida para processamento", dto.getStEvento())
            );
        }
    }
}