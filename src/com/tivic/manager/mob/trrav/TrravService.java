package com.tivic.manager.mob.trrav;

import java.util.List;

import javax.ws.rs.BadRequestException;
import javax.ws.rs.core.NoContentException;

import com.tivic.manager.mob.Trrav;
import com.tivic.sol.cdi.BeansFactory;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.search.SearchCriterios;

public class TrravService implements ITrravService {
	TrravRepository trravRepository;  

	public TrravService() throws Exception {
		trravRepository = (TrravRepository) BeansFactory.get(TrravRepository.class);
	}
	
	@Override
	public Trrav insert(Trrav trrav) throws Exception {
		CustomConnection customConnection = new CustomConnection();
		try {
			customConnection.initConnection(true);
			insert(trrav, customConnection);
			customConnection.finishConnection();
			return trrav;
		} finally {
			customConnection.closeConnection();
		}
	}

	@Override
	public Trrav insert(Trrav trrav, CustomConnection customConnection) throws BadRequestException, Exception {
		this.trravRepository.insert(trrav, customConnection);
		return trrav;
	}

	@Override
	public Trrav update(Trrav trrav) throws Exception {
		CustomConnection customConnection = new CustomConnection();
		try {
			customConnection.initConnection(true);
			update(trrav, customConnection);
			customConnection.finishConnection();
			return trrav;
		} finally {
			customConnection.closeConnection();
		}
	}

	@Override
	public Trrav update(Trrav trrav, CustomConnection customConnection) throws Exception {
		this.trravRepository.update(trrav, customConnection);
		return trrav;
	}

	@Override
	public Trrav get(int cdTrrav) throws Exception {
		CustomConnection customConnection = new CustomConnection();
		try {
			customConnection.initConnection(false);
			Trrav trrav = get(cdTrrav, customConnection);
			customConnection.finishConnection();
			return trrav;
		} finally {
			customConnection.closeConnection();
		}
	}

	@Override
	public Trrav get(int cdTrrav, CustomConnection customConnection) throws Exception {
		Trrav trrav = this.trravRepository.get(cdTrrav, customConnection);
		
		if(trrav == null)
			throw new NoContentException("Nenhum TRRAV encontrado");
		
		return trrav;
	}
	
	@Override
	public List<Trrav> find(SearchCriterios searchCriterios) throws Exception {
		CustomConnection customConnection = new CustomConnection();
		try {
			customConnection.initConnection(false);
			List<Trrav> trravs = find(searchCriterios, customConnection);
			customConnection.finishConnection();
			return trravs;
		} finally {
			customConnection.closeConnection();
		}
	}

	@Override
	public List<Trrav> find(SearchCriterios searchCriterios, CustomConnection customConnection) throws Exception {
		return this.trravRepository.find(searchCriterios, customConnection);
	}

}
