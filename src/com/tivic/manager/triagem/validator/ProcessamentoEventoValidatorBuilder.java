package com.tivic.manager.triagem.validator;

import java.util.ArrayList;
import java.util.List;

import com.tivic.manager.mob.EventoEquipamento;
import com.tivic.manager.mob.eventoequipamento.EventoEquipamentoRepository;
import com.tivic.manager.triagem.dtos.ProcessamentoEventoDTO;
import com.tivic.sol.cdi.BeansFactory;
import com.tivic.sol.connection.CustomConnection;

public class ProcessamentoEventoValidatorBuilder {
    private List<IProcessamentoEventoEstacionamentoValidator> validators;
    private final EventoEquipamentoRepository equipamentoRepository;
    
    public ProcessamentoEventoValidatorBuilder() throws Exception {
        validators = new ArrayList<>();
        equipamentoRepository = (EventoEquipamentoRepository) BeansFactory.get(EventoEquipamentoRepository.class);
        validators.add(new EventoExistenceValidator());
        validators.add(new EventoSituacaoValidator());
        validators.add(new MotivoCancelamentoValidator());
    }
    
    public void validate(ProcessamentoEventoDTO dto, CustomConnection customConection) throws Exception {
    	EventoEquipamento evento = equipamentoRepository.get(dto.getCdEvento(), customConection);
        
        for (IProcessamentoEventoEstacionamentoValidator validator : validators) {
            validator.validate(dto, evento, customConection);
        }
    }
}