package com.tivic.manager.mob.lotes.repository;

import java.util.List;

import com.tivic.manager.mob.lotes.model.Lote;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.search.SearchCriterios;

public interface LoteRepository {
	public void insert(Lote lote, CustomConnection customConnection) throws Exception;
	public void update(Lote lote, CustomConnection customConnection) throws Exception;
	public void delete(int cdLote, CustomConnection customConnection) throws Exception;
	public Lote get(int cdLote, CustomConnection customConnection) throws Exception;
	public List<Lote> find(SearchCriterios searchCriterios, CustomConnection customConnection) throws Exception;
}
