package com.tivic.manager.mob.aitpagamento;

import java.util.List;

import com.tivic.sol.connection.CustomConnection;
import com.tivic.manager.mob.AitPagamento;
import com.tivic.manager.mob.AitPagamentoDAO;
import com.tivic.sol.search.Search;
import com.tivic.sol.search.SearchBuilder;
import com.tivic.sol.search.SearchCriterios;

public class AitPagamentoRepositoryDAO implements AitPagamentoRepository {

	@Override
	public void insert(AitPagamento aitPagamento, CustomConnection customConnection) throws Exception {
		int cdPagamento = AitPagamentoDAO.insert(aitPagamento, customConnection.getConnection());
		if (cdPagamento < 0)
			throw new Exception("Erro ao inserir AitPagamento.");
		aitPagamento.setCdPagamento(cdPagamento);	
	}

	@Override
	public void update(AitPagamento aitPagamento, CustomConnection customConnection) throws Exception {
		AitPagamentoDAO.update(aitPagamento, customConnection.getConnection());
	}

	@Override
	public AitPagamento get(int cdAit, int cdPagamento) throws Exception {
		return get(cdAit, cdPagamento, new CustomConnection());
	}

	@Override
	public AitPagamento get(int cdAit, int cdPagamento, CustomConnection customConnection) throws Exception {
		return AitPagamentoDAO.get(cdAit, cdPagamento, customConnection.getConnection());
	}

	@Override
	public List<AitPagamento> find(SearchCriterios searchCriterios) throws Exception {
		return find(searchCriterios, new CustomConnection());
	}

	@Override
	public List<AitPagamento> find(SearchCriterios searchCriterios, CustomConnection customConnection) throws Exception {
		Search<AitPagamento> search = new SearchBuilder<AitPagamento>("mob_ait_pagamento")
				.searchCriterios(searchCriterios)
				.customConnection(customConnection)
				.build();
		return search.getList(AitPagamento.class);
	}
}