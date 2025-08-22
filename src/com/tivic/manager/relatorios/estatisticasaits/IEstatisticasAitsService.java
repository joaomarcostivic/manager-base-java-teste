package com.tivic.manager.relatorios.estatisticasaits;

import com.tivic.manager.util.pagination.PagedResponse;
import com.tivic.sol.search.SearchCriterios;

public interface IEstatisticasAitsService {
	public PagedResponse<RelatorioEstatisticasAitsDTO> findInfracoes(SearchCriterios searchCriterios) throws Exception;

}
