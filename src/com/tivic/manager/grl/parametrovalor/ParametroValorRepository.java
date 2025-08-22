package com.tivic.manager.grl.parametrovalor;

import java.util.List;

import com.tivic.manager.grl.ParametroValor;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.search.SearchCriterios;

public interface ParametroValorRepository {
	ParametroValor get(int cdParametro) throws Exception;
	ParametroValor get(int cdParametro, CustomConnection customConnection) throws Exception;
	List<ParametroValor> getAll() throws Exception;
	List<ParametroValor> getAll(CustomConnection customConnection) throws Exception;
	List<ParametroValor> find(SearchCriterios searchCriterios) throws IllegalArgumentException, Exception;
	List<ParametroValor> find(SearchCriterios searchCriterios, CustomConnection customConnection) throws IllegalArgumentException, Exception;
}
