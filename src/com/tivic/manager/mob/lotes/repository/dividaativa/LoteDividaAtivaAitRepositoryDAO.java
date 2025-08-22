package com.tivic.manager.mob.lotes.repository.dividaativa;

import java.util.List;

import com.tivic.manager.mob.lotes.dao.dividaativa.LoteDividaAtivaAitDAO;
import com.tivic.manager.mob.lotes.model.dividaativa.LoteDividaAtivaAit;
import com.tivic.manager.mob.lotes.service.dividaativa.exceptions.UpdateTabelaLoteDividaAtivaException;
import com.tivic.manager.mob.lotes.service.dividaativa.exceptions.InsertTabelaLoteDividaAtivaException;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.search.Search;
import com.tivic.sol.search.SearchBuilder;
import com.tivic.sol.search.SearchCriterios;


public class LoteDividaAtivaAitRepositoryDAO implements LoteDividaAtivaAitRepository {

	@Override
	public void insert(LoteDividaAtivaAit loteDividaAtivaAit, CustomConnection customConnection) throws Exception {
		int insert = LoteDividaAtivaAitDAO.insert(loteDividaAtivaAit, customConnection.getConnection());
		if(insert <= 0) {
			throw new InsertTabelaLoteDividaAtivaException();
		}
	}

	@Override
	public void update(LoteDividaAtivaAit loteDividaAtivaAit, CustomConnection customConnection) throws Exception {
		int update = LoteDividaAtivaAitDAO.update(loteDividaAtivaAit, customConnection.getConnection());
		if(update <= 0) {
			throw new UpdateTabelaLoteDividaAtivaException();
		}

	}

	@Override
	public LoteDividaAtivaAit get(int cdLoteDividaAtivaAit, int cdAit, CustomConnection customConnection) throws Exception {
		return LoteDividaAtivaAitDAO.get(cdLoteDividaAtivaAit, cdAit, customConnection.getConnection());
	}

	@Override
	public List<LoteDividaAtivaAit> find(SearchCriterios searchCriterios, CustomConnection customConnection) throws Exception {
		Search<LoteDividaAtivaAit> search = new SearchBuilder<LoteDividaAtivaAit>("mob_lote_divida_ativa_ait")
				.fields("*")
				.searchCriterios(searchCriterios)
				.customConnection(customConnection)
				.build();
		
		return search.getList(LoteDividaAtivaAit.class);
	}
}
