package com.tivic.manager.mob.eventoarquivo;

import java.util.List;

import javax.ws.rs.core.NoContentException;

import com.tivic.manager.mob.EventoArquivo;
import com.tivic.sol.cdi.BeansFactory;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.search.SearchCriterios;

public class EventoArquivoService implements IEventoArquivoService {

	private EventoArquivoRepository eventoArquivoRepository;
	
	public EventoArquivoService() throws Exception {
		this.eventoArquivoRepository = (EventoArquivoRepository) BeansFactory.get(EventoArquivoRepository.class);
	}
	
	@Override
	public EventoArquivo insert(EventoArquivo eventoArquivo) throws Exception{
		CustomConnection customConnection = new CustomConnection();
		try {
			customConnection.initConnection(true);
			insert(eventoArquivo, customConnection);
			customConnection.finishConnection();
			return eventoArquivo;
		} finally {
			customConnection.closeConnection();
		}
	}
	
	@Override
	public EventoArquivo insert(EventoArquivo eventoArquivo, CustomConnection customConnection) throws Exception{
		eventoArquivoRepository.insert(eventoArquivo, customConnection);
		return eventoArquivo;
	}
	
	@Override
	public EventoArquivo update(EventoArquivo eventoArquivo) throws Exception{
		CustomConnection customConnection = new CustomConnection();
		try {
			customConnection.initConnection(true);
			update(eventoArquivo, customConnection);
			customConnection.finishConnection();
			return eventoArquivo;
		} finally {
			customConnection.closeConnection();
		}
	}
	
	@Override
	public EventoArquivo update(EventoArquivo eventoArquivo, CustomConnection customConnection) throws Exception{
		eventoArquivoRepository.update(eventoArquivo, customConnection);
		return eventoArquivo;
	}
	
	@Override
	public EventoArquivo get(int cdEvento, int cdArquivo) throws Exception {
		CustomConnection customConnection = new CustomConnection();
		try {
			customConnection.initConnection(false);
			EventoArquivo eventoArquivo = get(cdEvento, cdArquivo, customConnection);
			customConnection.finishConnection();
			return eventoArquivo;
		} finally {
			customConnection.closeConnection();
		}
	}

	@Override
	public EventoArquivo get(int cdEvento, int cdArquivo, CustomConnection customConnection) throws Exception {
		EventoArquivo eventoArquivo = this.eventoArquivoRepository.get(cdEvento, cdArquivo, customConnection);
		
		if(eventoArquivo == null)
			throw new NoContentException("Nenhum evento de AIT encontrado");
		
		return eventoArquivo;
	}
	
	@Override
	public List<EventoArquivo> find(SearchCriterios searchCriterios) throws Exception{
		CustomConnection customConnection = new CustomConnection();
		try {
			customConnection.initConnection(false);
			List<EventoArquivo> eventoArquivos = find(searchCriterios, customConnection);
			customConnection.finishConnection();
			return eventoArquivos;
		} finally {
			customConnection.closeConnection();
		}
	}
	
	@Override
	public List<EventoArquivo> find(SearchCriterios searchCriterios, CustomConnection customConnection) throws Exception{
		return eventoArquivoRepository.find(searchCriterios, customConnection);
	}

}
