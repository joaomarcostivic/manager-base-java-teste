package com.tivic.manager.mob.aitevento;

import java.util.List;

import javax.ws.rs.core.NoContentException;

import com.tivic.manager.mob.AitEvento;
import com.tivic.sol.cdi.BeansFactory;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.search.SearchCriterios;

public class AitEventoService implements IAitEventoService {

	private AitEventoRepository aitEventoRepository;
	
	public AitEventoService() throws Exception {
		this.aitEventoRepository = (AitEventoRepository) BeansFactory.get(AitEventoRepository.class);
	}
	
	@Override
	public AitEvento insert(AitEvento aitEvento) throws Exception{
		CustomConnection customConnection = new CustomConnection();
		try {
			customConnection.initConnection(true);
			insert(aitEvento, customConnection);
			customConnection.finishConnection();
			return aitEvento;
		} finally {
			customConnection.closeConnection();
		}
	}
	
	@Override
	public AitEvento insert(AitEvento aitEvento, CustomConnection customConnection) throws Exception{
		aitEventoRepository.insert(aitEvento, customConnection);
		return aitEvento;
	}
	
	@Override
	public AitEvento update(AitEvento aitEvento) throws Exception{
		CustomConnection customConnection = new CustomConnection();
		try {
			customConnection.initConnection(true);
			update(aitEvento, customConnection);
			customConnection.finishConnection();
			return aitEvento;
		} finally {
			customConnection.closeConnection();
		}
	}
	
	@Override
	public AitEvento update(AitEvento aitEvento, CustomConnection customConnection) throws Exception{
		aitEventoRepository.update(aitEvento, customConnection);
		return aitEvento;
	}
	
	@Override
	public AitEvento get(int cdAit, int cdEvento) throws Exception {
		CustomConnection customConnection = new CustomConnection();
		try {
			customConnection.initConnection(false);
			AitEvento aitEvento = get(cdAit, cdEvento, customConnection);
			customConnection.finishConnection();
			return aitEvento;
		} finally {
			customConnection.closeConnection();
		}
	}

	@Override
	public AitEvento get(int cdAit, int cdEvento, CustomConnection customConnection) throws Exception {
		AitEvento aitEvento = this.aitEventoRepository.get(cdAit, cdEvento, customConnection);
		
		if(aitEvento == null)
			throw new NoContentException("Nenhum evento de AIT encontrado");
		
		return aitEvento;
	}
	
	@Override
	public List<AitEvento> find(SearchCriterios searchCriterios) throws Exception {
		CustomConnection customConnection = new CustomConnection();
		try {
			customConnection.initConnection(false);
			List<AitEvento> aitEventos = find(searchCriterios, customConnection);
			customConnection.finishConnection();
			return aitEventos;
		} finally {
			customConnection.closeConnection();
		}
	}

	@Override
	public List<AitEvento> find(SearchCriterios searchCriterios, CustomConnection customConnection) throws Exception {
		return this.aitEventoRepository.find(searchCriterios, customConnection);
	}

}
