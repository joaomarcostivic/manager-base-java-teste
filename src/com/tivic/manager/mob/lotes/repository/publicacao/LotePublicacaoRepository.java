package com.tivic.manager.mob.lotes.repository.publicacao;

import java.util.List;

import com.tivic.manager.mob.lotes.model.publicacao.LotePublicacao;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.search.SearchCriterios;

public interface LotePublicacaoRepository {
	public LotePublicacao insert(LotePublicacao cdLotePublicacao, CustomConnection customConnection) throws Exception;
	public LotePublicacao update(LotePublicacao cdLotePublicacao, CustomConnection customConnection) throws Exception;
	public LotePublicacao get(int cdLotePublicacao, CustomConnection customConnection) throws Exception;
	public List<LotePublicacao> find(SearchCriterios searchCriterios, CustomConnection customConnection) throws Exception;
}
