package com.tivic.manager.triagem.validator;

import com.tivic.manager.mob.EventoEquipamento;
import com.tivic.manager.triagem.dtos.ProcessamentoEventoDTO;
import com.tivic.manager.triagem.enums.SituacaoEventoEquipamentoEnum;
import com.tivic.manager.triagem.exceptions.EventoMotivoCancelamentoNotFoundException;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.search.Search;
import com.tivic.sol.search.SearchBuilder;
import com.tivic.sol.search.SearchCriterios;

public class MotivoCancelamentoValidator implements IProcessamentoEventoEstacionamentoValidator {
    @Override
    public void validate(ProcessamentoEventoDTO dto, EventoEquipamento evento, CustomConnection customConnection) throws Exception {
        if (dto.getStEvento() == SituacaoEventoEquipamentoEnum.CANCELADO.getKey()) {
            SearchCriterios searchCriterios = new SearchCriterios();
            searchCriterios.addCriteriosEqualInteger("cd_motivo_cancelamento", dto.getCdMotivoCancelamento());
            Search<Object> search = new SearchBuilder<>("mob_evento_motivo_cancelamento")
                .fields("cd_motivo_cancelamento")
                .searchCriterios(searchCriterios)
                .customConnection(customConnection)
                .build();
            
            if (search.getList(Object.class).isEmpty()) {
                throw new EventoMotivoCancelamentoNotFoundException(dto.getCdMotivoCancelamento());
            }
        }
    }
}