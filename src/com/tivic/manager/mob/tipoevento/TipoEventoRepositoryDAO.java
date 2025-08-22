package com.tivic.manager.mob.tipoevento;

import java.util.List;

import com.tivic.manager.mob.TipoEvento;
import com.tivic.manager.mob.TipoEventoDAO;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.search.Search;
import com.tivic.sol.search.SearchBuilder;
import com.tivic.sol.search.SearchCriterios;


public class TipoEventoRepositoryDAO implements TipoEventoRepository {
	@Override
	public void insert(TipoEvento tipoEvento, CustomConnection customConnection) throws Exception {
		int cdTipoEvento = TipoEventoDAO.insert(tipoEvento, customConnection.getConnection());
		if(cdTipoEvento <= 0)
			throw new Exception("Erro ao inserir TipoEvento");
	}

	@Override
	public void update(TipoEvento tipoEvento, CustomConnection customConnection) throws Exception {
		TipoEventoDAO.update(tipoEvento, customConnection.getConnection());
	}

	@Override
	public TipoEvento get(int cdTipoEvento, CustomConnection customConnection) throws Exception {
		return TipoEventoDAO.get(cdTipoEvento, customConnection.getConnection());
	}

	@Override
	public List<TipoEvento> find(SearchCriterios criterios, CustomConnection customConnection) throws Exception {
		Search<TipoEvento> search = new SearchBuilder<TipoEvento>("mob_tipo_evento")
				.searchCriterios(criterios)
				.build();
		return search.getList(TipoEvento.class);
	}

}
