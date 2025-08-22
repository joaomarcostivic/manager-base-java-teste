package com.tivic.manager.mob.relatorioestatisticas;

import com.tivic.manager.util.pagination.PagedResponse;
import com.tivic.sol.search.SearchCriterios;

public interface IRelatorioEstatisticasService {
	public PagedResponse<RelatorioEstatisticasDTO> findNais(SearchCriterios searchCriterios) throws Exception;
	public PagedResponse<RelatorioEstatisticasNipDTO> findNips(SearchCriterios searchCriterios) throws Exception;
	public PagedResponse<RelatorioEstatisticasNipDTO> findJulgamentoJari(SearchCriterios searchCriterios) throws Exception;
	public PagedResponse<RelatorioEstatisticasNipDTO> findPagamentoNip(SearchCriterios searchCriterios) throws Exception;
}
