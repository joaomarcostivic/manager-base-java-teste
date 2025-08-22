package com.tivic.manager.mob.trrav;

import java.util.List;

import com.tivic.manager.mob.Talonario;
import com.tivic.manager.mob.Trrav;
import com.tivic.manager.mob.TrravDAO;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.search.Search;
import com.tivic.sol.search.SearchBuilder;
import com.tivic.sol.search.SearchCriterios;


public class TrravRepositoryDAO implements TrravRepository {
	
	@Override
	public Trrav insert(Trrav trrav, CustomConnection customConnection) throws Exception {
		int cdTrrav = TrravDAO.insert(trrav, customConnection.getConnection());
		if(cdTrrav <= 0)
			throw new Exception("Erro ao inserir Trrav");
		trrav.setCdTrrav(cdTrrav);
		return trrav;
	}

	@Override
	public Trrav update(Trrav trrav, CustomConnection customConnection) throws Exception {
		TrravDAO.update(trrav, customConnection.getConnection());
		return trrav;
	}

	@Override
	public Trrav get(int cdTrrav, CustomConnection customConnection) throws Exception {
		return TrravDAO.get(cdTrrav, customConnection.getConnection());
	}

	@Override
	public List<Trrav> find(SearchCriterios criterios, CustomConnection customConnection) throws Exception {
		Search<Trrav> search = new SearchBuilder<Trrav>("mob_trrav")
				.searchCriterios(criterios)
				.build();
		return search.getList(Trrav.class);
	}
	
	@Override
	public int getUltimoNrTrrav(SearchCriterios searchCriterios, Talonario talonario, CustomConnection customConnection) throws Exception {
		Search<Trrav> search = new SearchBuilder<Trrav>("mob_trrav")
				.fields("nr_trrav")
				.searchCriterios(searchCriterios)
				.customConnection(customConnection)
				.orderBy("nr_trrav DESC")
				.build();
		List<Trrav> trravs = search.getList(Trrav.class);
		
		if(trravs.isEmpty()) {
			return talonario.getNrInicial() > 0 ? talonario.getNrInicial() - 1 : 0;
		}
		
		return trravs.get(0).getNrTrrav();
	}
	

}
