package com.tivic.manager.mob.convenio;

import java.util.List;

import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.search.Search;
import com.tivic.sol.search.SearchBuilder;
import com.tivic.sol.search.SearchCriterios;

public class ConvenioRepositoryDAO implements ConvenioRepository {

	@Override
	public void insert(Convenio convenio, CustomConnection customConnection) throws Exception {
		int insert = ConvenioDAO.insert(convenio, customConnection.getConnection());
		if(insert <= 0) {
			throw new Exception("Nào foi possível inserir o convênio");
		}
	}

	@Override
	public void update(Convenio convenio, CustomConnection customConnection) throws Exception {
		int update = ConvenioDAO.update(convenio, customConnection.getConnection());
		if(update <= 0) {
			throw new Exception("Não foi possível atualizar o convênio");
		}	
	}

	@Override
	public Convenio get(int cdConvenio, CustomConnection customConnection) throws Exception {
		Convenio convenio = ConvenioDAO.get(cdConvenio, customConnection.getConnection());
		return convenio;
	}

	@Override
	public List<Convenio> find(SearchCriterios searchCriterios) throws Exception {
		CustomConnection customConnection = new CustomConnection();
		try {
			customConnection.initConnection(false);
			List<Convenio> convenioList = find(searchCriterios, customConnection);
			customConnection.finishConnection();
			return convenioList;
		} finally {
			customConnection.closeConnection();
		}
	}

	@Override
	public List<Convenio> find(SearchCriterios searchCriterios, CustomConnection customConnection) throws Exception {
		Search<Convenio> search = new SearchBuilder<Convenio>("mob_convenio")
				.fields("*")
				.searchCriterios(searchCriterios)
				.customConnection(customConnection)
				.build();
		
		return search.getList(Convenio.class);
	}
}
