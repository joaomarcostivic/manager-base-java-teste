package com.tivic.manager.mob.ait.aitimagempessoa;

import java.util.List;

import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.search.Search;
import com.tivic.sol.search.SearchBuilder;
import com.tivic.sol.search.SearchCriterios;

public class AitImagemPessoaRepositoryDAO implements AitImagemPessoaRepository {

	@Override
	public void insert(AitImagemPessoa aitImagemPessoa, CustomConnection customConnection) throws Exception {
		AitImagemPessoaDAO.insert(aitImagemPessoa, customConnection.getConnection());
	}

	@Override
	public void update(AitImagemPessoa aitImagemPessoa, CustomConnection customConnection) throws Exception {
		AitImagemPessoaDAO.update(aitImagemPessoa, customConnection.getConnection());		
	}

	@Override
	public AitImagemPessoa get(int cdAitImagem, CustomConnection customConnection) throws Exception {
		AitImagemPessoa aitImagemPessoa = AitImagemPessoaDAO.get(cdAitImagem, customConnection.getConnection());
		return aitImagemPessoa;
	}

	@Override
	public List<AitImagemPessoa> find(SearchCriterios searchCriterios) throws Exception {
		CustomConnection customConnection = new CustomConnection();
		try {
			customConnection.initConnection(false);
			List<AitImagemPessoa> aitImagemPessaoList = find(searchCriterios, customConnection);
			customConnection.finishConnection();
			return aitImagemPessaoList;
		} finally {
			customConnection.closeConnection();
		}
	}

	@Override
	public List<AitImagemPessoa> find(SearchCriterios searchCriterios, CustomConnection customConnection) throws Exception {
		Search<AitImagemPessoa> search = new SearchBuilder<AitImagemPessoa>("mob_ait_imagem_pessoa")
				.fields("*")
				.searchCriterios(searchCriterios)
				.customConnection(customConnection)
				.build();
		
		return search.getList(AitImagemPessoa.class);
	}
}
