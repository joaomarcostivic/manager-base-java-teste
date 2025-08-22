package com.tivic.manager.mob.processamento.sincronizacao.factories;

import com.tivic.manager.mob.EventoEquipamento;
import com.tivic.sol.search.SearchCriterios;

public interface ICriterioStrategy {
	 SearchCriterios montarCriterios(EventoEquipamento eventoEquipamento);
}
