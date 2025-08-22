package com.tivic.manager.grl.pessoavinculohistorico;

import java.util.ArrayList;
import java.util.List;

import com.tivic.manager.util.pagination.PagedResponse;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.search.Search;
import com.tivic.sol.search.SearchBuilder;
import com.tivic.sol.search.SearchCriterios;

public class PessoaVinculoHistoricoService implements IPessoaVinculoHistoricoService {

	@Override
	public PagedResponse<PessoaVinculoHistorico> findPessoaVinculo(SearchCriterios searchCriterios) throws Exception {
		CustomConnection customConnection = new CustomConnection();
		try {
			customConnection.initConnection(false);
			Search<PessoaVinculoHistorico> historicoSearch = findPessoaVinculo(searchCriterios, customConnection);
			List<PessoaVinculoHistorico> historicoList = new ArrayList<PessoaVinculoHistorico>(historicoSearch.getList(PessoaVinculoHistorico.class));
			customConnection.finishConnection();
			return new PagedResponse<PessoaVinculoHistorico>(historicoList, historicoSearch.getRsm().getTotal());
		} finally {
			customConnection.closeConnection();
		}
	}

	@Override
	public Search<PessoaVinculoHistorico> findPessoaVinculo(SearchCriterios searchCriterios, CustomConnection customConnection) throws Exception {
		Search<PessoaVinculoHistorico> search = new SearchBuilder<PessoaVinculoHistorico>("grl_pessoa_vinculo_historico")
				.searchCriterios(searchCriterios)
				.customConnection(customConnection)
				.count()
			.build();
		return search;
	}

}
