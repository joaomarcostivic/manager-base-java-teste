package com.tivic.manager.fta.cor;

import java.util.List;

import javax.ws.rs.core.NoContentException;

import com.tivic.manager.fta.Cor;
import com.tivic.sol.cdi.BeansFactory;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.search.SearchCriterios;

public class CorService implements ICorService {

	private CorRepository corRepository;
	
	public CorService() throws Exception {
		this.corRepository = (CorRepository) BeansFactory.get(CorRepository.class);
	}
	
	@Override
	public Cor getByNome(String nmCor) throws Exception {
		SearchCriterios searchCriterios = new SearchCriterios();
		searchCriterios.addCriteriosEqualString("nm_cor", nmCor, true);
		List<Cor> cores = this.corRepository.find(searchCriterios);
		if(cores.isEmpty())
			throw new Exception("Nenhuma cor foi encontrada");
		return cores.get(0);
	}
	
	@Override
	public Cor get(int cdCor) throws Exception {
		CustomConnection customConnection = new CustomConnection();
		try {
			customConnection.initConnection(false);
			Cor cor = get(cdCor, customConnection);
			customConnection.finishConnection();
			return cor;
		} finally {
			customConnection.closeConnection();
		}
	}

	@Override
	public Cor get(int cdCor, CustomConnection customConnection) throws Exception {
		Cor cor = this.corRepository.get(cdCor, customConnection);
		
		if(cor == null)
			throw new NoContentException("Nenhuma cor encontrada");
		
		return cor;
	}

}
