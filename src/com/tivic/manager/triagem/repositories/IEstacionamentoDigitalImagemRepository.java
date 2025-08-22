package com.tivic.manager.triagem.repositories;

import java.util.List;
import com.tivic.manager.triagem.entities.EventoEstacionamentoDigitalImagem;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.search.SearchCriterios;

public interface IEstacionamentoDigitalImagemRepository {
	void insert(EventoEstacionamentoDigitalImagem eventoEstacionamentoDigitalImagem, CustomConnection customConnection) throws Exception;
	void update(EventoEstacionamentoDigitalImagem eventoEstacionamentoDigitalImagem, CustomConnection customConnection) throws Exception;
	EventoEstacionamentoDigitalImagem get(int cdEvento) throws Exception;
	EventoEstacionamentoDigitalImagem get(int cdEvento, CustomConnection customConnection) throws Exception;
	List<EventoEstacionamentoDigitalImagem> find(SearchCriterios searchCriterios) throws Exception;
	List<EventoEstacionamentoDigitalImagem> find(SearchCriterios searchCriterios, CustomConnection customConnection) throws Exception;
}
