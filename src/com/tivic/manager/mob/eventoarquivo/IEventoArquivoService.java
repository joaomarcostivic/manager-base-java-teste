package com.tivic.manager.mob.eventoarquivo;

import java.util.List;

import com.tivic.manager.mob.EventoArquivo;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.search.SearchCriterios;

public interface IEventoArquivoService {
	public EventoArquivo insert(EventoArquivo eventoArquivo) throws Exception;
	public EventoArquivo insert(EventoArquivo eventoArquivo, CustomConnection customConnection) throws Exception;
	public EventoArquivo update(EventoArquivo eventoArquivo) throws Exception;
	public EventoArquivo update(EventoArquivo eventoArquivo, CustomConnection customConnection) throws Exception;
	public EventoArquivo get(int cdEvento, int cdArquivo) throws Exception;
	public EventoArquivo get(int cdEvento, int cdArquivo, CustomConnection customConnection) throws Exception;
	public List<EventoArquivo> find(SearchCriterios searchCriterios) throws Exception;
	public List<EventoArquivo> find(SearchCriterios searchCriterios, CustomConnection customConnection) throws Exception;

}
