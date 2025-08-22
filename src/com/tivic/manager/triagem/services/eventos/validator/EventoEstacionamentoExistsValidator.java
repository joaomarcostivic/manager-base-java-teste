package com.tivic.manager.triagem.services.eventos.validator;

import java.util.List;

import com.tivic.manager.triagem.dtos.NotificacaoEstacionamentoDigitalDTO;
import com.tivic.manager.triagem.entities.EventoEstacionamentoDigital;
import com.tivic.manager.triagem.repositories.IEventoEstacionamentoDigitalRepository;
import com.tivic.sol.cdi.BeansFactory;
import com.tivic.sol.search.SearchCriterios;

public class EventoEstacionamentoExistsValidator implements ICapturaEventoEstacionamentoValidator {
	
	private IEventoEstacionamentoDigitalRepository eventoEstacionamentoDigitalRepository;

	
	public EventoEstacionamentoExistsValidator() throws Exception {
		eventoEstacionamentoDigitalRepository = (IEventoEstacionamentoDigitalRepository) BeansFactory.get(IEventoEstacionamentoDigitalRepository.class);
	}

	@Override
	public boolean validate(NotificacaoEstacionamentoDigitalDTO notificacao, CapturaEventoEstacionamentoValidationStatistics stats) throws Exception {
	    if (isEventoExist(notificacao.getNrNotificacao())) {
	        stats.incrementFailedByExistingEvent();
	        return false;
	    }
	    return true;
	}
    
	private boolean isEventoExist(String nrNotificacao) throws Exception {
		SearchCriterios searchCriterios = new SearchCriterios();
		searchCriterios.addCriteriosEqualString("nr_notificacao", nrNotificacao, true);
		List<EventoEstacionamentoDigital> eventoEstacionamentoDigitalList = eventoEstacionamentoDigitalRepository.find(searchCriterios);
		return !eventoEstacionamentoDigitalList.isEmpty();
	}
}
