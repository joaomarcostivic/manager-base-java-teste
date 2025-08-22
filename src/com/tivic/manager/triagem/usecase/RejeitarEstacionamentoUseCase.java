package com.tivic.manager.triagem.usecase;

import java.util.List;

import com.tivic.manager.mob.EventoMotivoCancelamento;
import com.tivic.manager.triagem.dtos.ProcessamentoEventoDTO;
import com.tivic.manager.triagem.dtos.RetornoTriagemEventoEstacionamentoDTO;
import com.tivic.manager.triagem.entities.EventoEstacionamentoDigital;
import com.tivic.manager.triagem.exceptions.EventoEstacionamentoNotFoundException;
import com.tivic.manager.triagem.exceptions.EventoMotivoCancelamentoNotFoundException;
import com.tivic.manager.triagem.repositories.IEventoEstacionamentoDigitalRepository;
import com.tivic.manager.triagem.services.eventos.IEventoEstacionamentoService;
import com.tivic.manager.triagem.validator.ProcessamentoEventoValidatorBuilder;
import com.tivic.sol.cdi.BeansFactory;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.search.Search;
import com.tivic.sol.search.SearchBuilder;
import com.tivic.sol.search.SearchCriterios;

public class RejeitarEstacionamentoUseCase {
    
	private final IEventoEstacionamentoService eventoEstacionamentoService;
	private final IEventoEstacionamentoDigitalRepository eventoEstacionamentoDigitalRepository;

    public RejeitarEstacionamentoUseCase() throws Exception {
		eventoEstacionamentoService = (IEventoEstacionamentoService) BeansFactory.get(IEventoEstacionamentoService.class);
        eventoEstacionamentoDigitalRepository = (IEventoEstacionamentoDigitalRepository) BeansFactory.get(IEventoEstacionamentoDigitalRepository.class);
    }

    public RetornoTriagemEventoEstacionamentoDTO execute(ProcessamentoEventoDTO processamentoEventoDTO) throws Exception {
		CustomConnection customConnection = new CustomConnection();
		try {
			customConnection.initConnection(true);
	    	new ProcessamentoEventoValidatorBuilder().validate(processamentoEventoDTO, customConnection);
	    	eventoEstacionamentoService.rejeitar(processamentoEventoDTO, customConnection);
	    	EventoEstacionamentoDigital eventoEstacionamento = buscarEventoDigital(processamentoEventoDTO.getCdEvento(), customConnection);
	    	EventoMotivoCancelamento motivoCancelamento = buscarMotivoCancelamento(processamentoEventoDTO.getCdMotivoCancelamento(), customConnection);
			customConnection.finishConnection();
			return mapearParaRetornoTriagem(eventoEstacionamento,motivoCancelamento);
		} finally {
			customConnection.closeConnection();
		}
    }
    
    private EventoEstacionamentoDigital buscarEventoDigital(int cdEvento, CustomConnection customConnection) throws Exception {
        EventoEstacionamentoDigital evento = eventoEstacionamentoDigitalRepository.get(cdEvento, customConnection);
        
        if (evento == null)
            throw new EventoEstacionamentoNotFoundException(cdEvento);
        
        return evento;
    }
    
    private EventoMotivoCancelamento buscarMotivoCancelamento(int cdMotivoCancelamento, CustomConnection customConnection) throws Exception {
		SearchCriterios searchCriterios = new SearchCriterios();
		searchCriterios.addCriteriosEqualInteger("cd_motivo_cancelamento", cdMotivoCancelamento);
		Search<EventoMotivoCancelamento> search = new SearchBuilder<EventoMotivoCancelamento>("mob_evento_motivo_cancelamento")
				.fields("*")
				.searchCriterios(searchCriterios)
				.customConnection(customConnection)
				.build();
		
		List<EventoMotivoCancelamento> list = search.getList(EventoMotivoCancelamento.class);
		
		if (list.isEmpty())
			throw new EventoMotivoCancelamentoNotFoundException(cdMotivoCancelamento);
		
		return list.get(0);
    }
    
    private RetornoTriagemEventoEstacionamentoDTO mapearParaRetornoTriagem(EventoEstacionamentoDigital eventoDigital, EventoMotivoCancelamento motivoCancelamento) {
    	    return RetornoTriagemEventoEstacionamentoDTO.builder()
    	        .nrNotificacao(eventoDigital.getNrNotificacao())
    	        .dsMotivoCancelamento(eventoDigital.getDsMotivoCancelamento())
    	        .nmMotivoCancelamento(motivoCancelamento.getNmMotivoCancelamento())
    	        .build();
	}
}