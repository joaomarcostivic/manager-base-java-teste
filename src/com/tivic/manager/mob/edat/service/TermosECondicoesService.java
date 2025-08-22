package com.tivic.manager.mob.edat.service;

import java.util.List;

import com.tivic.manager.mob.edat.TermosECondicoes;
import com.tivic.manager.mob.edat.repositories.ITermosECondicoesRepository;
import com.tivic.manager.mob.edat.repositories.ITermosECondicoesService;
import com.tivic.sol.cdi.BeansFactory;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.search.SearchCriterios;

public class TermosECondicoesService implements ITermosECondicoesService {
	
    private ITermosECondicoesRepository termosECondicoesRepository;

    public TermosECondicoesService() throws Exception {
        this.termosECondicoesRepository = (ITermosECondicoesRepository) BeansFactory.get(ITermosECondicoesRepository.class);
    }

    @Override
    public TermosECondicoes get(int cdTermo) throws Exception {
    	CustomConnection customConnection = new CustomConnection();
		try {
			customConnection.initConnection(false);
			TermosECondicoes termo = get(cdTermo, customConnection);
			customConnection.finishConnection();
			return termo;
		} finally {
			customConnection.closeConnection();
		}
    }

    @Override
    public TermosECondicoes get(int cdTermo, CustomConnection customConnection) throws Exception {
        return this.termosECondicoesRepository.get(cdTermo, customConnection);
    }

	@Override
	public List<TermosECondicoes> find(SearchCriterios searchCriterios) throws Exception {
		CustomConnection customConnection = new CustomConnection();
		try {
			customConnection.initConnection(false);
			List<TermosECondicoes> listTermos = find(searchCriterios, customConnection);
			customConnection.finishConnection();
			return listTermos;
		} finally {
			customConnection.closeConnection();
		}
	}

	@Override
	public List<TermosECondicoes> find(SearchCriterios searchCriterios, CustomConnection customConnection)
			throws Exception {
		List<TermosECondicoes> termos = this.termosECondicoesRepository.find(searchCriterios, customConnection);
		return termos;
	}

	@Override
	public void insert(TermosECondicoes termos) throws Exception {
		CustomConnection customConnection = new CustomConnection();
		try {
			customConnection.initConnection(true);
			insert(termos, customConnection);
			customConnection.finishConnection();
		} finally {
			customConnection.closeConnection();
		}
	}

	@Override
	public void insert(TermosECondicoes termos, CustomConnection customConnection) throws Exception {
		termosECondicoesRepository.insert(termos, customConnection);
	}

	@Override
	public void update(TermosECondicoes termos) throws Exception {
		CustomConnection customConnection = new CustomConnection();
		try {
			customConnection.initConnection(true);
			update(termos, customConnection);
			customConnection.finishConnection();
		} finally {
			customConnection.closeConnection();
		}
	}

	@Override
	public void update(TermosECondicoes termos, CustomConnection customConnection) throws Exception {
		termosECondicoesRepository.update(termos, customConnection);
	}
	
	@Override
	public List<TermosECondicoes> updateAll(List<TermosECondicoes> termosList) throws Exception {
		CustomConnection customConnection = new CustomConnection();
		try {
			customConnection.initConnection(true);
			updateAll(termosList, customConnection);
			customConnection.finishConnection();
		} finally {
			customConnection.closeConnection();
		}
		return termosList;
	}

	@Override
	public void updateAll(List<TermosECondicoes> termosList, CustomConnection customConnection) throws Exception {
		termosECondicoesRepository.updateAll(termosList, customConnection);
	}
    
	@Override
	public void delete(int cdTermo) throws Exception {
		CustomConnection customConnection = new CustomConnection();
		try {
			customConnection.initConnection(true);
			delete(cdTermo, customConnection);
			customConnection.finishConnection();
		} finally {
			customConnection.closeConnection();
		}
	}

    @Override
    public void delete(int cdTermo, CustomConnection customConnection) throws Exception {
        termosECondicoesRepository.delete(cdTermo, customConnection);
    }
}
