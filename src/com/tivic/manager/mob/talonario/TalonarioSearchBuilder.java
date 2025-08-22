package com.tivic.manager.mob.talonario;

import com.tivic.sol.search.SearchCriterios;

public class TalonarioSearchBuilder {

private SearchCriterios searchCriterios;
	
	public TalonarioSearchBuilder() {
		searchCriterios = new SearchCriterios();
	}
	
	public TalonarioSearchBuilder setSituacao(int situacao) {
		searchCriterios.addCriteriosEqualInteger("A.st_talao", situacao, situacao > -1);
		return this;
	}
	
	public TalonarioSearchBuilder setTipo(int tipo) {
		searchCriterios.addCriteriosEqualInteger("A.tp_talao", tipo, tipo > -1);
		return this;
	}
	
	public TalonarioSearchBuilder setCdAgente(int cdAgente) {
		searchCriterios.addCriteriosEqualInteger("B.cd_agente", cdAgente, cdAgente > 0);
		return this;
	}

	public TalonarioSearchBuilder setLimit(int limit, int page) {
		searchCriterios.setQtLimite(limit);
		searchCriterios.setQtDeslocamento(limit * page - limit);
    	return this;
    }
	
	public SearchCriterios build() {
        return searchCriterios;
    }
	
}
