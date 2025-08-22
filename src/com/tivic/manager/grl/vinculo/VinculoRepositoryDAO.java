package com.tivic.manager.grl.vinculo;

import java.util.List;

import com.tivic.manager.grl.Vinculo;
import com.tivic.manager.grl.VinculoDAO;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.search.Search;
import com.tivic.sol.search.SearchCriterios;
import com.tivic.sol.search.SearchBuilder;

public class VinculoRepositoryDAO implements VinculoRepository {

	@Override
	public Vinculo get(int cdVinculo) throws Exception {
		return get(cdVinculo, new CustomConnection());
	}

	@Override
	public Vinculo get(int cdVinculo, CustomConnection customConnection) throws Exception {
		return VinculoDAO.get(cdVinculo, customConnection.getConnection());
	}

	@Override
	public List<Vinculo> find() throws Exception {
		return find(new CustomConnection());
	}

	@Override
	public List<Vinculo> find(CustomConnection customConnection) throws Exception {
		SearchCriterios searchCriterios = new SearchCriterios();
		Search<Vinculo> vinculos = new SearchBuilder<Vinculo>("grl_vinculo")
				.searchCriterios(searchCriterios)
				.customConnection(customConnection)
			.build();
		return vinculos.getList(Vinculo.class);
	}

}
