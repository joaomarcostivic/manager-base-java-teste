package com.tivic.manager.mob.eventoequipamento;

import java.util.List;

import javax.ws.rs.core.NoContentException;

import com.tivic.manager.mob.EventoEquipamento;
import com.tivic.sol.cdi.BeansFactory;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.search.SearchCriterios;

public class EventoEquipamentoService  implements IEventoEquipamentoService{
	
	private EventoEquipamentoRepository eventoEquipamentoRepository;
	
	public EventoEquipamentoService() throws Exception {
		this.eventoEquipamentoRepository = (EventoEquipamentoRepository) BeansFactory.get(EventoEquipamentoRepository.class);		
	}
	
	@Override
	public EventoEquipamento insert(EventoEquipamento eventoEquipamento) throws Exception{
		CustomConnection customConnection = new CustomConnection();
		try {
			customConnection.initConnection(true);
			insert(eventoEquipamento, customConnection);
			customConnection.finishConnection();
			return eventoEquipamento;
		} finally {
			customConnection.closeConnection();
		}
	}
	
	@Override
	public EventoEquipamento insert(EventoEquipamento eventoEquipamento, CustomConnection customConnection) throws Exception{
		eventoEquipamentoRepository.insert(eventoEquipamento, customConnection);
		return eventoEquipamento;
	}
	
	@Override
	public EventoEquipamento update(EventoEquipamento eventoEquipamento) throws Exception{
		CustomConnection customConnection = new CustomConnection();
		try {
			customConnection.initConnection(true);
			update(eventoEquipamento, customConnection);
			customConnection.finishConnection();
			return eventoEquipamento;
		} finally {
			customConnection.closeConnection();
		}
	}
	
	@Override
	public EventoEquipamento update(EventoEquipamento eventoEquipamento, CustomConnection customConnection) throws Exception{
		eventoEquipamentoRepository.update(eventoEquipamento, customConnection);
		return eventoEquipamento;
	}
	
	@Override
	public EventoEquipamento get(int cdEvento) throws Exception {
		CustomConnection customConnection = new CustomConnection();
		try {
			customConnection.initConnection(false);
			EventoEquipamento eventoEquipamento = get(cdEvento, customConnection);
			customConnection.finishConnection();
			return eventoEquipamento;
		} finally {
			customConnection.closeConnection();
		}
	}

	@Override
	public EventoEquipamento get(int cdEvento, CustomConnection customConnection) throws Exception {
		EventoEquipamento eventoEquipamento = this.eventoEquipamentoRepository.get(cdEvento, customConnection);
		
		if(eventoEquipamento == null)
			throw new NoContentException("Nenhum evento equipamento encontrado");
		
		return eventoEquipamento;
	}
	
	@Override
	public List<EventoEquipamento> find(SearchCriterios searchCriterios) throws Exception {
		CustomConnection customConnection = new CustomConnection();
		try {
			customConnection.initConnection(false);
			List<EventoEquipamento> eventoEquipamentos = find(searchCriterios, customConnection);
			customConnection.finishConnection();
			return eventoEquipamentos;
		} finally {
			customConnection.closeConnection();
		}
	}

	@Override
	public List<EventoEquipamento> find(SearchCriterios searchCriterios, CustomConnection customConnection) throws Exception {
		return this.eventoEquipamentoRepository.find(searchCriterios, customConnection);
	}
	
}
