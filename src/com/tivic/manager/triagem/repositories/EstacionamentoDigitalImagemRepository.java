package com.tivic.manager.triagem.repositories;

import java.util.List;
import javax.ws.rs.BadRequestException;
import com.tivic.manager.triagem.entities.EventoEstacionamentoDigitalImagem;
import com.tivic.manager.util.ResultSetMapper;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.search.SearchCriterios;

public class EstacionamentoDigitalImagemRepository implements IEstacionamentoDigitalImagemRepository {

	@Override
	public void insert(EventoEstacionamentoDigitalImagem eventoEstacionamentoDigitalImagem, CustomConnection customConnection) throws Exception {
		int codRetorno = EventoEstacionamentoDigitalImagemDAO.insert(eventoEstacionamentoDigitalImagem, customConnection.getConnection());
		if (codRetorno <= 0) {
			throw new BadRequestException("Ocorreu um erro ao inserir as imagens do evento.");
		}
	}

	@Override
	public void update(EventoEstacionamentoDigitalImagem eventoEstacionamentoDigitalImagem, CustomConnection customConnection) throws Exception {
		int codRetorno = EventoEstacionamentoDigitalImagemDAO.update(eventoEstacionamentoDigitalImagem, customConnection.getConnection());
		if (codRetorno <= 0) {
			throw new BadRequestException("Ocorreu um erro ao atualizar as imagens do evento.");
		}
	}

	@Override
	public EventoEstacionamentoDigitalImagem get(int cdEvento) throws Exception {
		CustomConnection customConnection = new CustomConnection();
		try {
			customConnection.initConnection(false);
			EventoEstacionamentoDigitalImagem eventoEstacionamentoDigitalImagem = get(cdEvento, customConnection);
			customConnection.finishConnection();
			return eventoEstacionamentoDigitalImagem;
		} finally {
			customConnection.closeConnection();
		}
	}

	@Override
	public EventoEstacionamentoDigitalImagem get(int cdEvento, CustomConnection customConnection) throws Exception {
		return EventoEstacionamentoDigitalImagemDAO.get(cdEvento, customConnection.getConnection());
	}

	@Override
	public List<EventoEstacionamentoDigitalImagem> find(SearchCriterios searchCriterios) throws Exception {
		CustomConnection customConnection = new CustomConnection();
		try {
			customConnection.initConnection(false);
			List<EventoEstacionamentoDigitalImagem> eventoEstacionamentoDigitalImagens = find(searchCriterios, customConnection);
			customConnection.finishConnection();
			return eventoEstacionamentoDigitalImagens;
		} finally {
			customConnection.closeConnection();
		}
	}

	@Override
	public List<EventoEstacionamentoDigitalImagem> find(SearchCriterios searchCriterios, CustomConnection customConnection) throws Exception {
		ResultSetMapper<EventoEstacionamentoDigitalImagem> rsm = new ResultSetMapper<EventoEstacionamentoDigitalImagem>(EventoEstacionamentoDigitalImagemDAO.find(searchCriterios.getCriterios(), customConnection.getConnection()), EventoEstacionamentoDigitalImagem.class);
		return rsm.toList();
	}

}
