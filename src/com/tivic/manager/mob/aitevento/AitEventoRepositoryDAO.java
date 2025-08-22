package com.tivic.manager.mob.aitevento;

import java.util.List;

import com.tivic.manager.mob.AitEvento;
import com.tivic.manager.mob.AitEventoDAO;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.search.Search;
import com.tivic.sol.search.SearchBuilder;
import com.tivic.sol.search.SearchCriterios;

public class AitEventoRepositoryDAO implements AitEventoRepository {

	@Override
	public void insert(AitEvento aitEvento, CustomConnection customConnection) throws Exception {
		int cdAit = AitEventoDAO.insert(aitEvento, customConnection.getConnection());
		if (cdAit <= 0)
			throw new Exception("Erro ao inserir evento.");
		aitEvento.setCdEvento(cdAit);
	}

	@Override
	public void update(AitEvento aitEvento, CustomConnection customConnection) throws Exception {
		int aitEventoUpdate = AitEventoDAO.update(aitEvento, customConnection.getConnection());
		if(aitEventoUpdate <= 0)
			throw new Exception("Erro ao atualizar o evento");
	}

	@Override
	public AitEvento get(int cdAit, int cdEvento, CustomConnection customConnection) throws Exception {
		return AitEventoDAO.get(cdAit, cdEvento, customConnection.getConnection());
	}

	@Override
	public List<AitEvento> find(SearchCriterios searchCriterios, CustomConnection customConnection) throws Exception {
		Search<AitEvento> search = new SearchBuilder<AitEvento>("mob_ait_evento")
				.searchCriterios(searchCriterios)
				.customConnection(customConnection).build();
		return search.getList(AitEvento.class);
	}
}
