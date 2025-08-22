package com.tivic.manager.triagem.services.arquivo;

import java.util.ArrayList;
import java.util.List;

import com.tivic.manager.grl.Arquivo;
import com.tivic.manager.grl.arquivo.IArquivoRepository;
import com.tivic.manager.mob.EventoArquivo;
import com.tivic.manager.mob.eventoarquivo.EventoArquivoRepository;
import com.tivic.sol.cdi.BeansFactory;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.search.SearchCriterios;

public class EventoArquivoEstacionamentoService implements IEventoArquivoEstacionamentoService {
	
	private EventoArquivoRepository eventoArquivoRepository;
	private IArquivoRepository arquivoRepository;
	
	public EventoArquivoEstacionamentoService() throws Exception {
		eventoArquivoRepository = (EventoArquivoRepository) BeansFactory.get(EventoArquivoRepository.class);
		arquivoRepository = (IArquivoRepository) BeansFactory.get(IArquivoRepository.class);
	}

	@Override
	public List<Arquivo> getArquivosByEvento(int cdEvento) throws Exception {
		CustomConnection customConnection = new CustomConnection();
		try {
			customConnection.initConnection(false);
			List<Arquivo> arquivos = getArquivosByEvento(cdEvento, customConnection);
			customConnection.finishConnection();
			return arquivos;
		} finally {
			customConnection.closeConnection();
		}
	}
	
	@Override
	public List<Arquivo> getArquivosByEvento(int cdEvento, CustomConnection customConnection) throws Exception {
		List<Arquivo> arquivos = new ArrayList<Arquivo>();
		List<EventoArquivo> eventoArquivos = getEventoArquivosByEvento(cdEvento, customConnection);
		
		for(EventoArquivo eventoArquivo : eventoArquivos) {
			arquivos.add(arquivoRepository.get(eventoArquivo.getCdArquivo(), customConnection));
		}
		
		return arquivos;

	}
	
	@Override
	public List<EventoArquivo> getEventoArquivosByEvento(int cdEvento) throws Exception {
		CustomConnection customConnection = new CustomConnection();
		try {
			customConnection.initConnection(false);
			List<EventoArquivo> arquivos = getEventoArquivosByEvento(cdEvento, customConnection);
			customConnection.finishConnection();
			return arquivos;
		} finally {
			customConnection.closeConnection();
		}
	}
	
	@Override
	public List<EventoArquivo> getEventoArquivosByEvento(int cdEvento, CustomConnection customConnection) throws Exception {
		return eventoArquivoRepository.find(getArquivoCriterios(cdEvento), customConnection);
	}
	
	private SearchCriterios getArquivoCriterios(int cdEvento) {
		SearchCriterios searchCriterios = new SearchCriterios();
		searchCriterios.addCriteriosEqualInteger("cd_evento", cdEvento);
		
		return searchCriterios;
	}
	
	@Override
	public void update(EventoArquivo eventoArquivo) throws Exception {
		CustomConnection customConnection = new CustomConnection();
		try {
			customConnection.initConnection(true);
			update(eventoArquivo, customConnection);
			customConnection.finishConnection();
		} finally {
			customConnection.closeConnection();
		}
	}
	
	@Override
	public void update(EventoArquivo eventoArquivo, CustomConnection customConnection) throws Exception {
		eventoArquivoRepository.update(eventoArquivo, customConnection);
	}
}
