package com.tivic.manager.triagem.repositories;

import java.util.List;
import com.tivic.manager.triagem.entities.EventoEstacionamentoDigitalDetalhes;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.search.SearchCriterios;

public interface IEventoEstacionamentoDigitalDetalhesRepository {
	void insert(EventoEstacionamentoDigitalDetalhes eventoEstacionamentoDigitalDetalhes, CustomConnection customConnection) throws Exception;
	void update(EventoEstacionamentoDigitalDetalhes eventoEstacionamentoDigitalDetalhes, CustomConnection customConnection) throws Exception;
	EventoEstacionamentoDigitalDetalhes getByCdEvento(int cdEvento) throws Exception;
	EventoEstacionamentoDigitalDetalhes getByCdEvento(int cdEvento, CustomConnection customConnection) throws Exception;
	List<EventoEstacionamentoDigitalDetalhes> find(SearchCriterios searchCriterios) throws Exception;
	List<EventoEstacionamentoDigitalDetalhes> find(SearchCriterios searchCriterios, CustomConnection customConnection) throws Exception;
}
