package com.tivic.manager.mob.lotes.repository.publicacao;

import java.util.List;

import com.tivic.manager.mob.lotes.dao.publicacao.LotePublicacaoDAO;
import com.tivic.manager.mob.lotes.model.publicacao.LotePublicacao;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.search.Search;
import com.tivic.sol.search.SearchBuilder;
import com.tivic.sol.search.SearchCriterios;

public class LotePublicacaoRepositoryDAO implements LotePublicacaoRepository {
	
	@Override
	public LotePublicacao insert(LotePublicacao lotePublicacao, CustomConnection customConnection) throws Exception {
		int cdLotePublicacao = LotePublicacaoDAO.insert(lotePublicacao, customConnection.getConnection());
		if (cdLotePublicacao <= 0)
			throw new Exception("Erro ao inserir LotePublicacao.");
		lotePublicacao.setCdLotePublicacao(cdLotePublicacao);
		return lotePublicacao;
	}

	@Override
	public LotePublicacao update(LotePublicacao lotePublicacao, CustomConnection customConnection) throws Exception {
		LotePublicacaoDAO.update(lotePublicacao, customConnection.getConnection());
		return lotePublicacao;
	}

	@Override
	public LotePublicacao get(int cdLotePublicacao, CustomConnection customConnection) throws Exception {
		return LotePublicacaoDAO.get(cdLotePublicacao, customConnection.getConnection());
	}

	@Override
	public List<LotePublicacao> find(SearchCriterios searchCriterios, CustomConnection customConnection) throws Exception {
		Search<LotePublicacao> search = new SearchBuilder<LotePublicacao>("grl_lote_publicacao")
				.searchCriterios(searchCriterios)
				.customConnection(customConnection)
				.build();
		return search.getList(LotePublicacao.class);
	}
}
