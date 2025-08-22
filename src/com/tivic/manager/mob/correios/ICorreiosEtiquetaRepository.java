package com.tivic.manager.mob.correios;

import java.util.List;

import com.tivic.manager.mob.CorreiosEtiqueta;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.search.SearchCriterios;

public interface ICorreiosEtiquetaRepository {
	public void update(CorreiosEtiqueta correiosEtiqueta, CustomConnection customConnection) throws Exception;
	public CorreiosEtiqueta get(int cdEtiqueta, CustomConnection customConnection) throws Exception;
	public void insert(CorreiosEtiqueta correiosEtiqueta, CustomConnection customConnection) throws Exception;
	public void delete(int cdEtiqueta, CustomConnection customConnection) throws Exception;
	public List<CorreiosEtiqueta> find(SearchCriterios searchCriterios, CustomConnection customConnection) throws IllegalArgumentException, Exception;
}
