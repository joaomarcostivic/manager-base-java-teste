package com.tivic.manager.mob.correios;

import java.util.List;
import com.tivic.manager.mob.CorreiosLote;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.search.SearchCriterios;

public interface CorreiosLoteRepository {
	CorreiosLote update(CorreiosLote correiosLote, CustomConnection customConnection) throws Exception;
	CorreiosLote insert(CorreiosLote correiosLote, CustomConnection customConnection) throws Exception;
	CorreiosLote delete(CorreiosLote correiosLote, CustomConnection customConnection) throws Exception;
	CorreiosLote get(int cdLote, CustomConnection customConnection) throws Exception;
	List<CorreiosLote> find(SearchCriterios searchCriterios, CustomConnection customConnection) throws IllegalArgumentException, Exception;
}
