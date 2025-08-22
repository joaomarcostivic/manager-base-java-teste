package com.tivic.manager.grl.pessoavinculohistorico.repository;

import java.util.List;

import com.tivic.manager.grl.pessoavinculohistorico.PessoaVinculoHistorico;
import com.tivic.manager.grl.pessoavinculohistorico.PessoaVinculoHistoricoDAO;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.search.Search;
import com.tivic.sol.search.SearchBuilder;
import com.tivic.sol.search.SearchCriterios;

public class PessoaVinculoHistoricoRepositoryDAO implements PessoaVinculoHistoricoRepository {

	@Override
	public void insert(PessoaVinculoHistorico pessoaVinculoHistorico, CustomConnection customConnection) throws Exception {
		int cdPessoa = PessoaVinculoHistoricoDAO.insert(pessoaVinculoHistorico, customConnection.getConnection());
		if(cdPessoa < 0)
			throw new Exception("Erro ao inserir PessoaVinculoHistorico.");
	}

	@Override
	public void update(PessoaVinculoHistorico pessoaVinculoHistorico, CustomConnection customConnection) throws Exception {
		int result = PessoaVinculoHistoricoDAO.update(pessoaVinculoHistorico, customConnection.getConnection());
		if(result < 0)
			throw new Exception("Erro ao atualizar PessoaVinculoHistorico.");		
	}

	@Override
	public PessoaVinculoHistorico get(int cdVinculoHistorico) throws Exception {
		return get(cdVinculoHistorico, new CustomConnection());
	}

	@Override
	public PessoaVinculoHistorico get(int cdVinculoHistorico, CustomConnection customConnection) throws Exception {
		return PessoaVinculoHistoricoDAO.get(cdVinculoHistorico, customConnection.getConnection());
	}

	@Override
	public List<PessoaVinculoHistorico> find(SearchCriterios searchCriterios) throws Exception {
		return find(searchCriterios, new CustomConnection());
	}

	@Override
	public List<PessoaVinculoHistorico> find(SearchCriterios searchCriterios, CustomConnection customConnection) throws Exception {
		Search<PessoaVinculoHistorico> search = new SearchBuilder<PessoaVinculoHistorico>("grl_pessoa_vinculo_historico")
				.searchCriterios(searchCriterios)
				.customConnection(customConnection)
			.build();
		return search.getList(PessoaVinculoHistorico.class);
	}
}
