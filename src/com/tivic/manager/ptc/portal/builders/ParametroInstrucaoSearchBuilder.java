package com.tivic.manager.ptc.portal.builders;

import com.tivic.sol.search.SearchCriterios;

public class ParametroInstrucaoSearchBuilder {
	
	private SearchCriterios criterios;

	public ParametroInstrucaoSearchBuilder() {
		this.criterios = new SearchCriterios();
	}

	public ParametroInstrucaoSearchBuilder setNmParametro(String nmParametro) {
		this.criterios.addCriteriosEqualString("nm_parametro", nmParametro, nmParametro != null && !nmParametro.trim().equals(""));
		return this;
	}

	public SearchCriterios build() {
		return this.criterios;
	}
}
