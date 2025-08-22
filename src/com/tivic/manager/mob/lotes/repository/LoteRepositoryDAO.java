package com.tivic.manager.mob.lotes.repository;

import java.util.List;

import com.tivic.manager.mob.lotes.dao.LoteDAO;
import com.tivic.manager.mob.lotes.model.Lote;
import com.tivic.manager.wsdl.exceptions.ValidacaoException;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.search.Search;
import com.tivic.sol.search.SearchBuilder;
import com.tivic.sol.search.SearchCriterios;

public class LoteRepositoryDAO implements LoteRepository {
	
	@Override
	public void insert(Lote lote, CustomConnection customConnection) throws Exception {
		int cdLote = LoteDAO.insert(lote, customConnection.getConnection());
		if (cdLote <= 0)
			throw new Exception("Erro ao inserir Lote.");
		lote.setCdLote(cdLote);
	}

	@Override
	public void update(Lote lote, CustomConnection customConnection) throws Exception {
		LoteDAO.update(lote, customConnection.getConnection());
	}
	
	@Override
	public void delete(int cdLote, CustomConnection customConnection) throws Exception {
		int codigoRetorno = LoteDAO.delete(cdLote, customConnection.getConnection());
		if (codigoRetorno <= 0)
			throw new ValidacaoException("Erro ao deletar arquivo do lote");
	}

	@Override
	public Lote get(int cdLote, CustomConnection customConnection) throws Exception {
		return LoteDAO.get(cdLote, customConnection.getConnection());
	}

	@Override
	public List<Lote> find(SearchCriterios searchCriterios, CustomConnection customConnection) throws Exception {
		Search<Lote> search = new SearchBuilder<Lote>("grl_lote")
				.searchCriterios(searchCriterios)
				.customConnection(customConnection)
				.build();
		return search.getList(Lote.class);
	}
}
