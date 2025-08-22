package com.tivic.manager.grl.parametrovalor;

import java.util.List;

import com.tivic.manager.grl.ParametroValor;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.search.Search;
import com.tivic.sol.search.SearchBuilder;
import com.tivic.sol.search.SearchCriterios;

public class ParametroValorRepositoryDAO implements ParametroValorRepository {

	

	@Override
	public List<ParametroValor> getAll() throws Exception {
		return getAll(new CustomConnection());
	}

	@Override
	public List<ParametroValor> getAll(CustomConnection customConnection) throws Exception {
		SearchCriterios searchCriterios = new SearchCriterios();
		Search<ParametroValor> search = new SearchBuilder<ParametroValor>("grl_parametro_valor")
				.fields("*")
				.searchCriterios(searchCriterios)
				.customConnection(customConnection)
				.build();
		
		return search.getList(ParametroValor.class);
	}

	@Override
	public List<ParametroValor> find(SearchCriterios searchCriterios) throws IllegalArgumentException, Exception {
		return find(searchCriterios, new CustomConnection());
	}

	@Override
	public List<ParametroValor> find(SearchCriterios searchCriterios, CustomConnection customConnection) throws IllegalArgumentException, Exception {
		Search<ParametroValor> search = new SearchBuilder<ParametroValor>("grl_parametro_valor")
				.fields("*")
				.searchCriterios(searchCriterios)
				.customConnection(customConnection)
				.build();
		
		return search.getList(ParametroValor.class);
	}

	@Override
	public ParametroValor get(int cdParametro) throws Exception {
		CustomConnection customConnection = new CustomConnection();
		try {
			customConnection.initConnection(false);
			ParametroValor parametroValor = get(cdParametro, customConnection);
			customConnection.finishConnection();
			return parametroValor; 
		} finally {
			customConnection.closeConnection();
		}
	}

	@Override
	public ParametroValor get(int cdParametro, CustomConnection customConnection) throws Exception {
		SearchCriterios searchCriterios = new SearchCriterios();
		searchCriterios.addCriteriosEqualInteger("cd_parametro", cdParametro);
		Search<ParametroValor> search = new SearchBuilder<ParametroValor>("grl_parametro_valor")
				.fields("*")
				.searchCriterios(searchCriterios)
				.customConnection(customConnection)
				.build();
		
		return search.getList(ParametroValor.class).get(0);
	}
}
