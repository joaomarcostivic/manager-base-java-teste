package com.tivic.manager.mob.lotes.repository.aitmovimento;

import com.tivic.manager.mob.TipoStatusEnum;
import com.tivic.manager.mob.lotes.model.aitmovimento.AitMovimento;
import com.tivic.sol.search.Search;
import com.tivic.sol.search.SearchBuilder;
import com.tivic.sol.search.SearchCriterios;

public class AitMovimentoRepositoryDAO implements AitMovimentoRepository {
	
	public AitMovimento getByTpStatus(int cdAit, TipoStatusEnum tpStatus) throws Exception {
		SearchCriterios searchCriterios = new SearchCriterios();
		searchCriterios.addCriteriosEqualInteger("A.cd_ait", cdAit);
		searchCriterios.addCriteriosEqualInteger("A.tp_status", tpStatus.getKey());
		Search<AitMovimento> search = new SearchBuilder<AitMovimento>("mob_ait_movimento A")
				.searchCriterios(searchCriterios)
				.build();
		return search.getList(AitMovimento.class).get(0);
	}
}
