package com.tivic.manager.ptc.protocolosv3.recursos;

import java.util.List;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.search.SearchCriterios;

public interface IRecursoRepository {
	int insert(Recurso recurso, CustomConnection customConnection) throws Exception;
	int update(Recurso recurso, CustomConnection customConnection) throws Exception;
	Recurso get(int cdDocumento, int cdAta, CustomConnection customConnection) throws Exception;
	List<Recurso> find(SearchCriterios searchCriterios, CustomConnection customConnection) throws Exception;
	void delete(int cdDocumento, int cdAta, CustomConnection customConnection) throws Exception;
}
