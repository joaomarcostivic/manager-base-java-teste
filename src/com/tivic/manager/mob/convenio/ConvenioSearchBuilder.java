package com.tivic.manager.mob.convenio;

import com.tivic.sol.search.SearchCriterios;

public class ConvenioSearchBuilder {
	
	private SearchCriterios searchCriterios;
	
	public ConvenioSearchBuilder() {
		searchCriterios = new SearchCriterios();
	}
	
	public ConvenioSearchBuilder setNmConvenio(String nmConvenio) {
		searchCriterios.addCriteriosEqualString("nm_convenio", nmConvenio, nmConvenio != null);
		return this;
	}
	
	public ConvenioSearchBuilder setlgDefaul(int lgDefault) {
		searchCriterios.addCriteriosEqualInteger("lg_default", lgDefault, lgDefault > -1);
		return this;
	}
	
	public SearchCriterios build() {
		return searchCriterios;
	}
}
