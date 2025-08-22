package com.tivic.manager.ptc.fase;

import java.util.List;

import javax.ws.rs.BadRequestException;
import javax.ws.rs.core.NoContentException;

import com.tivic.manager.ptc.Fase;
import com.tivic.manager.ptc.portal.credencialestacionamento.DatabaseConnectionManager;
import com.tivic.sol.cdi.BeansFactory;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.search.Search;
import com.tivic.sol.search.SearchBuilder;
import com.tivic.sol.search.SearchCriterios;

public class FaseService implements IFaseService{
	private IFaseRepository faseRepository;
	
	public FaseService() throws Exception{
		this.faseRepository = (IFaseRepository) BeansFactory.get(IFaseRepository.class);
	}
	
	@Override
	public Fase get(int id) throws BadRequestException, Exception {
		return get(id, new CustomConnection());
	}
	
	public Fase get(int id, CustomConnection customConnection) throws NoContentException, Exception{
		try {
			customConnection.initConnection(false);
			Fase fase = faseRepository.get(id, customConnection);
			customConnection.finishConnection();
			if(fase == null) {
				throw new NoContentException("Nenhum tipo de documento encontrado");
			}
			return fase;
		} finally {
			customConnection.closeConnection();
		}
	}

	@Override
	public List<Fase> getAll() throws Exception {
		CustomConnection customConnection = new CustomConnection();
		try {
			new DatabaseConnectionManager().configConnection(customConnection, false);	
			List<Fase> fases = getAll(customConnection);
			customConnection.finishConnection();
			return fases;
		} finally {
			customConnection.closeConnection();
		}
	}

	@Override
	public List<Fase> getAll(CustomConnection customConnection) throws Exception {
		SearchCriterios searchCriterios = new SearchCriterios();
		Search<Fase> search = new SearchBuilder<Fase>("ptc_fase")
				.fields("*")
				.searchCriterios(searchCriterios)
				.customConnection(customConnection)
			.build();
		
		if(search.getList(Fase.class).isEmpty()) {
			throw new NoContentException("Nenhuma fase encontrada.");
		}
		
		return search.getList(Fase.class);
	}
}
