package com.tivic.manager.triagem.repositories;

import java.util.List;
import javax.ws.rs.BadRequestException;

import com.tivic.manager.triagem.entities.EventoEstacionamentoDigital;
import com.tivic.manager.util.ResultSetMapper;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.search.SearchCriterios;

public class EventoEstacionamentoDigitalRepositoryDAO implements IEventoEstacionamentoDigitalRepository {

	@Override
	public void insert(EventoEstacionamentoDigital eventoEstacionamentoDigital, CustomConnection customConnection) throws Exception {
		int codRetorno = EventoEstacionamentoDigitalDAO.insert(eventoEstacionamentoDigital, customConnection.getConnection());
		if (codRetorno <= 0)
			throw new BadRequestException("Ocorreu um erro ao salva o evento.");
	}

	@Override
	public void update(EventoEstacionamentoDigital eventoEstacionamentoDigital, CustomConnection customConnection) throws Exception {
		int codRetorno = EventoEstacionamentoDigitalDAO.update(eventoEstacionamentoDigital, customConnection.getConnection());
		if (codRetorno <= 0)
			throw new BadRequestException("Ocorreu um erro ao atualizar o evento.");
	}
	
	@Override
	public EventoEstacionamentoDigital get(int cdEvento) throws Exception {
		CustomConnection customConnection = new CustomConnection();
		try {
			customConnection.initConnection(false);
			EventoEstacionamentoDigital eventoEstacionamentoDigital = get(cdEvento, customConnection);
			customConnection.finishConnection();
			return eventoEstacionamentoDigital;
		} finally {
			customConnection.closeConnection();
		}
	}

	@Override
	public EventoEstacionamentoDigital get(int cdEvento, CustomConnection customConnection) throws Exception {
		return EventoEstacionamentoDigitalDAO.get(cdEvento, customConnection.getConnection());
	}
	
	@Override
	public List<EventoEstacionamentoDigital> find(SearchCriterios searchCriterios) throws Exception {
		CustomConnection customConnection = new CustomConnection();
		try {
			customConnection.initConnection(false);
			List<EventoEstacionamentoDigital> eventoEstacionamentoDigitalList = find(searchCriterios, customConnection);
			customConnection.finishConnection();
			return eventoEstacionamentoDigitalList;
		} finally {
			customConnection.closeConnection();
		}
	}

	@Override
	public List<EventoEstacionamentoDigital> find(SearchCriterios searchCriterios, CustomConnection customConnection) throws Exception {
		ResultSetMapper<EventoEstacionamentoDigital> rsm = new ResultSetMapper<EventoEstacionamentoDigital>(EventoEstacionamentoDigitalDAO.find(searchCriterios.getCriterios(), customConnection.getConnection()), EventoEstacionamentoDigital.class);
		return rsm.toList();
	}

}
