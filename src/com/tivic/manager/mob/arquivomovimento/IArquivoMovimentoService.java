package com.tivic.manager.mob.arquivomovimento;

import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.search.SearchCriterios;

public interface IArquivoMovimentoService {
	ArquivoMovimentoDTO getMovimentoPendente(SearchCriterios searchCriterios) throws Exception;
	ArquivoMovimentoDTO getMovimentoPendente(SearchCriterios searchCriterios, CustomConnection customConnection) throws Exception;
}
