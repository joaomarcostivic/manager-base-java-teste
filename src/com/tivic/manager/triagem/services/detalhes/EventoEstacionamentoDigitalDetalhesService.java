package com.tivic.manager.triagem.services.detalhes;

import com.tivic.manager.triagem.entities.EventoEstacionamentoDigitalDetalhes;
import com.tivic.manager.triagem.repositories.IEventoEstacionamentoDigitalDetalhesRepository;
import com.tivic.sol.cdi.BeansFactory;
import com.tivic.sol.connection.CustomConnection;

public class EventoEstacionamentoDigitalDetalhesService implements IEventoEstacionamentoDigitalDetalhesService {
	
	private IEventoEstacionamentoDigitalDetalhesRepository eventoEstacionamentoDigitalDetalhesRepository;
	
	public EventoEstacionamentoDigitalDetalhesService() throws Exception {
		eventoEstacionamentoDigitalDetalhesRepository = (IEventoEstacionamentoDigitalDetalhesRepository) BeansFactory.get(IEventoEstacionamentoDigitalDetalhesRepository.class);
	}

	@Override
	public EventoEstacionamentoDigitalDetalhes getDetalhesByEvento(int cdEvento) throws Exception {
		CustomConnection customConnection = new CustomConnection();
		try {
			customConnection.initConnection(false);
			EventoEstacionamentoDigitalDetalhes detalhes = getDetalhesByEvento(cdEvento, customConnection);
			customConnection.finishConnection();
			return detalhes;
		} finally {
			customConnection.closeConnection();
		}
	}

	@Override
	public EventoEstacionamentoDigitalDetalhes getDetalhesByEvento(int cdEvento, CustomConnection customConnection) throws Exception {
		return eventoEstacionamentoDigitalDetalhesRepository.getByCdEvento(cdEvento, customConnection);
	}

	@Override
	public void updateDetalhes(EventoEstacionamentoDigitalDetalhes eventoEstacionamentoDigitalDetalhes) throws Exception {
		CustomConnection customConnection = new CustomConnection();
		try {
			customConnection.initConnection(true);
			updateDetalhes(eventoEstacionamentoDigitalDetalhes, customConnection);
			customConnection.finishConnection();
		} finally {
			customConnection.closeConnection();
		}
	}

	@Override
	public void updateDetalhes(EventoEstacionamentoDigitalDetalhes eventoEstacionamentoDigitalDetalhes, CustomConnection customConnection) throws Exception {
		eventoEstacionamentoDigitalDetalhesRepository.update(eventoEstacionamentoDigitalDetalhes, customConnection);
	}
}
