package com.tivic.manager.grl.pais;

import java.util.List;

import com.tivic.manager.grl.Pais;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.search.Search;
import com.tivic.sol.search.SearchBuilder;
import com.tivic.sol.search.SearchCriterios;

public class PaisRepositoryDAO implements PaisRepository {

	@Override
	public List<Pais> getAll() throws Exception {
		return getAll(new CustomConnection());
	}

	@Override
	public List<Pais> getAll(CustomConnection customConnection) throws Exception {
		SearchCriterios searchCriterios = new SearchCriterios();
		Search<Pais> search = new SearchBuilder<Pais>("grl_pais")
				.fields("*")
				.searchCriterios(searchCriterios)
				.customConnection(customConnection)
				.build();
		
		return search.getList(Pais.class);
	}

}
