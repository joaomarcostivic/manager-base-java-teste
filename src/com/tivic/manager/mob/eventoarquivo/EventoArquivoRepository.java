package com.tivic.manager.mob.eventoarquivo;

import java.util.List;

import com.tivic.manager.mob.EventoArquivo;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.search.SearchCriterios;

public interface EventoArquivoRepository {
	public void insert(EventoArquivo eventoArquivo, CustomConnection customConnection);
	public void update(EventoArquivo eventoArquivo, CustomConnection customConnection) throws Exception;
	public List<EventoArquivo> find(SearchCriterios searchCriterios, CustomConnection customConnection) throws Exception;
	public EventoArquivo get(int cdEvento, int cdArquivo, CustomConnection customConnection) throws Exception;
}
