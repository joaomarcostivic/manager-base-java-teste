package com.tivic.manager.mob.tipoevento;

import java.util.List;

import javax.ws.rs.BadRequestException;
import javax.ws.rs.core.NoContentException;

import com.tivic.manager.mob.TipoEvento;
import com.tivic.sol.cdi.BeansFactory;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.search.SearchCriterios;

public class TipoEventoService implements ITipoEventoService {
	TipoEventoRepository tipoEventoRepository;  

	public TipoEventoService() throws Exception {
		tipoEventoRepository = (TipoEventoRepository) BeansFactory.get(TipoEventoRepository.class);
	}
	
	@Override
	public TipoEvento insert(TipoEvento tipoEvento) throws Exception {
		CustomConnection customConnection = new CustomConnection();
		try {
			customConnection.initConnection(true);
			insert(tipoEvento, customConnection);
			customConnection.finishConnection();
			return tipoEvento;
		} finally {
			customConnection.closeConnection();
		}
	}

	@Override
	public TipoEvento insert(TipoEvento tipoEvento, CustomConnection customConnection) throws BadRequestException, Exception {
		this.tipoEventoRepository.insert(tipoEvento, customConnection);
		return tipoEvento;
	}

	@Override
	public TipoEvento update(TipoEvento tipoEvento) throws Exception {
		CustomConnection customConnection = new CustomConnection();
		try {
			customConnection.initConnection(true);
			update(tipoEvento, customConnection);
			customConnection.finishConnection();
			return tipoEvento;
		} finally {
			customConnection.closeConnection();
		}
	}

	@Override
	public TipoEvento update(TipoEvento tipoEvento, CustomConnection customConnection) throws Exception {
		this.tipoEventoRepository.update(tipoEvento, customConnection);
		return tipoEvento;
	}

	@Override
	public TipoEvento get(int cdTipoEvento) throws Exception {
		CustomConnection customConnection = new CustomConnection();
		try {
			customConnection.initConnection(false);
			TipoEvento tipoEvento = get(cdTipoEvento, customConnection);
			customConnection.finishConnection();
			return tipoEvento;
		} finally {
			customConnection.closeConnection();
		}
	}

	@Override
	public TipoEvento get(int cdTipoEvento, CustomConnection customConnection) throws Exception {
		TipoEvento tipoEvento = this.tipoEventoRepository.get(cdTipoEvento, customConnection);

		if(tipoEvento == null)
			throw new NoContentException("Nenhum tipo de evento encontrado");
		
		return tipoEvento;
	}
	
	@Override
	public List<TipoEvento> find(SearchCriterios searchCriterios) throws Exception {
		CustomConnection customConnection = new CustomConnection();
		try {
			customConnection.initConnection(false);
			List<TipoEvento> tiposEvento = find(searchCriterios, customConnection);
			customConnection.finishConnection();
			return tiposEvento;
		} finally {
			customConnection.closeConnection();
		}
	}

	@Override
	public List<TipoEvento> find(SearchCriterios searchCriterios, CustomConnection customConnection) throws Exception {
		return this.tipoEventoRepository.find(searchCriterios, customConnection);
	}
	
	@Override
	public TipoEvento getByIdTipoEvento(String idTipoEvento) throws Exception {
		CustomConnection customConnection = new CustomConnection();
		try {
			customConnection.initConnection(false);
			TipoEvento tipoEvento = getByIdTipoEvento(idTipoEvento, customConnection);
			customConnection.finishConnection();
			return tipoEvento;
		} finally {
			customConnection.closeConnection();
		}
	}
	
	@Override
	public TipoEvento getByIdTipoEvento(String idTipoEvento, CustomConnection customConnection) throws Exception {
		SearchCriterios searchCriterios = new SearchCriterios();
		searchCriterios.addCriteriosEqualString("id_tipo_evento", idTipoEvento);
		
		List<TipoEvento> tiposEvento = find(searchCriterios, customConnection);
		
		if(tiposEvento.isEmpty())
			throw new NoContentException("Nenhum tipo de evento encontrado");
		
		return tiposEvento.get(0);
	}

}
