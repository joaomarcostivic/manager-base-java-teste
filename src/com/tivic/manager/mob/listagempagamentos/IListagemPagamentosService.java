package com.tivic.manager.mob.listagempagamentos;

import com.tivic.manager.util.pagination.PagedResponse;
import com.tivic.sol.search.SearchCriterios;

public interface IListagemPagamentosService {
	public PagedResponse<RelatorioPagamentoDTO> find(SearchCriterios searchCriterios) throws Exception;
}
