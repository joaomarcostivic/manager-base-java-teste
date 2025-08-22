package com.tivic.manager.mob.lotes.repository.dividaativa;

import java.util.List;

import com.tivic.manager.mob.lotes.model.dividaativa.LoteDividaAtivaAit;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.search.SearchCriterios;

public interface LoteDividaAtivaAitRepository {
	void insert(LoteDividaAtivaAit loteDividaAtivaAit, CustomConnection customConnection) throws Exception;
	void update(LoteDividaAtivaAit loteDividaAtivaAit, CustomConnection customConnection) throws Exception;
	LoteDividaAtivaAit get(int cdLoteDividaAtivaAit, int cdAit, CustomConnection customConnection) throws Exception;
	List<LoteDividaAtivaAit> find(SearchCriterios searchCriterios, CustomConnection customConnection) throws Exception;
}
