package com.tivic.manager.mob.arquivomovimento;

import java.util.List;

import com.tivic.manager.mob.ArquivoMovimento;
import com.tivic.manager.mob.ArquivoMovimentoDAO;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.search.Search;
import com.tivic.sol.search.SearchBuilder;
import com.tivic.sol.search.SearchCriterios;

public class ArquivoMovimentoRepositoryDAO implements ArquivoMovimentoRepository {
	@Override
	public void insert(ArquivoMovimento arquivoMovimento, CustomConnection customConnection) throws Exception {
		int cdArquivoMovimento = ArquivoMovimentoDAO.insert(arquivoMovimento, customConnection.getConnection());
		if (cdArquivoMovimento < 0)
			throw new Exception("Erro ao inserir ArquivoMovimento.");
		arquivoMovimento.setCdArquivoMovimento(cdArquivoMovimento);	}

	@Override
	public void update(ArquivoMovimento arquivoMovimento, CustomConnection customConnection) throws Exception {
		ArquivoMovimentoDAO.update(arquivoMovimento, customConnection.getConnection());
	}

	@Override
	public ArquivoMovimento get(int cdArquivoMovimento, int cdMovimento, int cdAit) throws Exception {
		return get(cdArquivoMovimento, cdMovimento, cdAit, new CustomConnection());
	}

	@Override
	public ArquivoMovimento get(int cdArquivoMovimento, int cdMovimento, int cdAit, CustomConnection customConnection) throws Exception {
		return ArquivoMovimentoDAO.get(cdArquivoMovimento, cdMovimento, cdAit, customConnection.getConnection());
	}

	@Override
	public List<ArquivoMovimento> find(SearchCriterios searchCriterios) throws Exception {
		return find(searchCriterios, new CustomConnection());
	}

	@Override
	public List<ArquivoMovimento> find(SearchCriterios searchCriterios, CustomConnection customConnection) throws Exception {
		Search<ArquivoMovimento> search = new SearchBuilder<ArquivoMovimento>("mob_arquivo_movimento")
				.searchCriterios(searchCriterios)
				.customConnection(customConnection).build();
		return search.getList(ArquivoMovimento.class);
	}
}
