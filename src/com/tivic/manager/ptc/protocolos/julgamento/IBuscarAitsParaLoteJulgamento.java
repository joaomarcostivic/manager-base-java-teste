package com.tivic.manager.ptc.protocolos.julgamento;

import com.tivic.sol.search.Search;
import com.tivic.sol.search.SearchCriterios;

public interface IBuscarAitsParaLoteJulgamento {

		Search<AitDTO> montarSearchLoteJulgamento(SearchCriterios searchCriterios) throws Exception;
		Search<AitDTO> buscarAitsParaLoteJulgamento(int quantidadeAit) throws Exception;
}
