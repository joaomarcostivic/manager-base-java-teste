package com.tivic.manager.mob.processamento.sincronizacao.factories;

import com.tivic.manager.mob.EventoEquipamento;
import com.tivic.sol.search.SearchCriterios;

public class CriterioSerialStrategy implements ICriterioStrategy {
	private SearchCriterios searchCriterios;
	
	public CriterioSerialStrategy(SearchCriterios searchCriterios) {
		this.searchCriterios = searchCriterios;
	}
	
	@Override
	public SearchCriterios montarCriterios(EventoEquipamento eventoEquipamento) {
		this.searchCriterios.addCriteriosEqualString("id_serial", eventoEquipamento.getNrSerial());
		return this.searchCriterios;
	}

}
