package com.tivic.manager.mob.aitpagamento;

import java.util.List;

import com.tivic.sol.connection.CustomConnection;
import com.tivic.manager.mob.AitPagamento;
import com.tivic.sol.search.SearchCriterios;

public interface AitPagamentoRepository {
	public void insert(AitPagamento aitPagamento, CustomConnection customConnection) throws Exception;
	public void update(AitPagamento aitPagamento, CustomConnection customConnection) throws Exception;
	public AitPagamento get(int cdPagamento, int cdAit) throws Exception;
	public AitPagamento get(int cdPagamento,  int cdAit, CustomConnection customConnection) throws Exception;
	public List<AitPagamento> find(SearchCriterios searchCriterios) throws Exception;
	public List<AitPagamento> find(SearchCriterios searchCriterios, CustomConnection customConnection) throws Exception;
}
