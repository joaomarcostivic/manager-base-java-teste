package com.tivic.manager.mob.ait.aitpessoa;

import java.util.List;

import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.search.Search;
import com.tivic.sol.search.SearchBuilder;
import com.tivic.sol.search.SearchCriterios;

public class AitPessoaRepositoryDAO implements AitPessoaRepository {

	@Override
	public void insert(AitPessoa aitPessoa, CustomConnection customConnection) throws Exception {
		AitPessoaDAO.insert(aitPessoa, customConnection.getConnection());
	}

	@Override
	public void update(AitPessoa aitPessoa, CustomConnection customConnection) throws Exception {
		AitPessoaDAO.update(aitPessoa, customConnection.getConnection());

	}

	@Override
	public AitPessoa get(int cdAitPessoa, CustomConnection customConnection) throws Exception {
		AitPessoa aitPessoa = AitPessoaDAO.get(cdAitPessoa, customConnection.getConnection());
		return aitPessoa;
	}

	@Override
	public List<AitPessoa> find(SearchCriterios searchCriterios) throws Exception {
		CustomConnection customConnection = new CustomConnection();
		try {
			customConnection.initConnection(false);
			List<AitPessoa> aitPessaoList = find(searchCriterios, customConnection);
			customConnection.finishConnection();
			return aitPessaoList;
		} finally {
			customConnection.closeConnection();
		}
	}

	@Override
	public List<AitPessoa> find(SearchCriterios searchCriterios, CustomConnection customConnection) throws Exception {
		Search<AitPessoa> search = new SearchBuilder<AitPessoa>("mob_ait_pessoa")
				.fields("*")
				.searchCriterios(searchCriterios)
				.customConnection(customConnection)
				.build();
		
		return search.getList(AitPessoa.class);
	}
}
