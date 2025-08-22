package com.tivic.manager.mob.eventoarquivo;

import java.util.List;

import javax.ws.rs.BadRequestException;

import com.tivic.manager.mob.EventoArquivo;
import com.tivic.manager.mob.EventoArquivoDAO;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.search.Search;
import com.tivic.sol.search.SearchBuilder;
import com.tivic.sol.search.SearchCriterios;

public class EventoArquivoRepositoryDAO implements EventoArquivoRepository {
	
	@Override
	public void insert(EventoArquivo eventoArquivo, CustomConnection customConnection) {
		int codRetorno = EventoArquivoDAO.insert(eventoArquivo, customConnection.getConnection());
		if (codRetorno <= 0) {
			throw new BadRequestException("Ocorreu um erro ao salvar o arquivo do evento.");
		}
	}
	
	@Override
	public void update(EventoArquivo eventoArquivo, CustomConnection customConnection) throws Exception {
		int codRetorno = EventoArquivoDAO.update(eventoArquivo, customConnection.getConnection());
		if(codRetorno <= 0)
			throw new Exception("Erro ao atualizar o evento arquivo");
	}

	@Override
	public List<EventoArquivo> find(SearchCriterios searchCriterios, CustomConnection customConnection) throws Exception {
		Search<EventoArquivo> search = new SearchBuilder<EventoArquivo>("mob_evento_arquivo")
				.searchCriterios(searchCriterios)
				.customConnection(customConnection)
				.build();
		return search.getList(EventoArquivo.class);
	}
	
	@Override
	public EventoArquivo get(int cdEvento, int cdArquivo, CustomConnection customConnection) throws Exception {
		return EventoArquivoDAO.get(cdEvento, cdArquivo, customConnection.getConnection());
	}

}
