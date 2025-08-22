package com.tivic.manager.mob.processamento.sincronizacao.factories;

import com.tivic.manager.mob.EventoEquipamento;
import com.tivic.sol.search.SearchCriterios;

public class CriterioEquipamentoStrategy implements ICriterioStrategy {
	private SearchCriterios searchCriterios;

	public CriterioEquipamentoStrategy(SearchCriterios searchCriterios) {
		this.searchCriterios = searchCriterios;
	}
	
	@Override
	public SearchCriterios montarCriterios(EventoEquipamento eventoEquipamento) {
		this.searchCriterios.addCriteriosEqualString("id_equipamento", eventoEquipamento.getNmEquipamento());
		return this.searchCriterios;
	}
}
