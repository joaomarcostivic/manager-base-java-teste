package com.tivic.manager.triagem.repositories;

import java.util.List;
import javax.ws.rs.BadRequestException;
import com.tivic.manager.triagem.entities.EventoEstacionamentoDigitalDetalhes;
import com.tivic.manager.util.ResultSetMapper;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.search.SearchCriterios;

public class EventoEstacionamentoDigitalDetalhesRepository implements IEventoEstacionamentoDigitalDetalhesRepository {

	@Override
	public void insert(EventoEstacionamentoDigitalDetalhes eventoEstacionamentoDigitalDetalhes,CustomConnection customConnection) throws Exception {
		int codRetorno = EventoEstacionamentoDigitalDetalhesDAO.insert(eventoEstacionamentoDigitalDetalhes, customConnection.getConnection());
		if (codRetorno <= 0) {
			throw new BadRequestException("Ocorreu um erro ao salvar os detalhes do evento.");
		}
	}

	@Override
	public void update(EventoEstacionamentoDigitalDetalhes eventoEstacionamentoDigitalDetalhes, CustomConnection customConnection) throws Exception {
		int codRetorno = EventoEstacionamentoDigitalDetalhesDAO.update(eventoEstacionamentoDigitalDetalhes, customConnection.getConnection());
		if (codRetorno <= 0) {
			throw new BadRequestException("Ocorreu um erro ao atualizar os detalhes do evento.");
		}
	}

	@Override
	public EventoEstacionamentoDigitalDetalhes getByCdEvento(int cdEvento) throws Exception {
		CustomConnection customConnection = new CustomConnection();
		try {
			customConnection.initConnection(false);
			EventoEstacionamentoDigitalDetalhes eventoEstacionamentoDigitalDetalhes = getByCdEvento(cdEvento, customConnection);
			customConnection.finishConnection();
			return eventoEstacionamentoDigitalDetalhes;
		} finally {
			customConnection.closeConnection();
		}
	}

	@Override
	public EventoEstacionamentoDigitalDetalhes getByCdEvento(int cdEvento, CustomConnection customConnection) throws Exception {
		return EventoEstacionamentoDigitalDetalhesDAO.get(cdEvento, customConnection.getConnection());
	}

	@Override
	public List<EventoEstacionamentoDigitalDetalhes> find(SearchCriterios searchCriterios) throws Exception {
		CustomConnection customConnection = new CustomConnection();
		try {
			customConnection.initConnection(false);
			List<EventoEstacionamentoDigitalDetalhes> eventoEstacionamentoDigitalDetalhesList = find(searchCriterios, customConnection);
			customConnection.finishConnection();
			return eventoEstacionamentoDigitalDetalhesList;
		} finally {
			customConnection.closeConnection();
		}
	}

	@Override
	public List<EventoEstacionamentoDigitalDetalhes> find(SearchCriterios searchCriterios,CustomConnection customConnection) throws Exception {
		ResultSetMapper<EventoEstacionamentoDigitalDetalhes> rsm = new ResultSetMapper<EventoEstacionamentoDigitalDetalhes>(EventoEstacionamentoDigitalDetalhesDAO.find(searchCriterios.getCriterios(), customConnection.getConnection()), EventoEstacionamentoDigitalDetalhes.class);
		return rsm.toList();
	}

}
