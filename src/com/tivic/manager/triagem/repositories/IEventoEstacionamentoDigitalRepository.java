package com.tivic.manager.triagem.repositories;

import java.util.List;

import com.tivic.manager.triagem.entities.EventoEstacionamentoDigital;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.search.SearchCriterios;

public interface IEventoEstacionamentoDigitalRepository {
	void insert(EventoEstacionamentoDigital eventoEstacionamentoDigital, CustomConnection customConnection) throws Exception;
	void update(EventoEstacionamentoDigital eventoEstacionamentoDigital, CustomConnection customConnection) throws Exception;
	EventoEstacionamentoDigital get(int cdEvento) throws Exception;
	EventoEstacionamentoDigital get(int cdEvento, CustomConnection customConnection) throws Exception;
	List<EventoEstacionamentoDigital> find(SearchCriterios searchCriterios) throws Exception;
	List<EventoEstacionamentoDigital> find(SearchCriterios searchCriterios, CustomConnection customConnection) throws Exception;
}
