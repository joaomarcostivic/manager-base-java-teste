package com.tivic.manager.mob.lote.impressao;

import com.tivic.sol.search.Search;
import com.tivic.sol.search.SearchCriterios;

public interface IBuscarAitsParaLoteImpressao {
	Search<AitDTO> montarSearchLoteImpressao(SearchCriterios searchCriterios) throws Exception;
	Search<AitDTO> buscarAitsParaLoteImpressao(int quantidadeAit) throws Exception;
}
