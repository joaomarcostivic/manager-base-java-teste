package com.tivic.manager.grl.pessoavinculohistorico;

import com.tivic.manager.util.pagination.PagedResponse;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.search.Search;
import com.tivic.sol.search.SearchCriterios;

public interface IPessoaVinculoHistoricoService {
	PagedResponse<PessoaVinculoHistorico> findPessoaVinculo(SearchCriterios searchCriterios) throws Exception;
	Search<PessoaVinculoHistorico> findPessoaVinculo(SearchCriterios searchCriterios, CustomConnection customConnection) throws Exception;

}
