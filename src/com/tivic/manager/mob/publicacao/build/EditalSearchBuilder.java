package com.tivic.manager.mob.publicacao.build;

import com.tivic.sol.search.SearchCriterios;

public class EditalSearchBuilder {
	private SearchCriterios searchCriterios;

	public EditalSearchBuilder() {
		this.searchCriterios = new SearchCriterios();
	}

	public EditalSearchBuilder setTpDocumento(int tpDocumento) {
		this.searchCriterios.addCriteriosEqualInteger("C.tp_publicacao", tpDocumento, tpDocumento > 0);
		return this;
	}

	public EditalSearchBuilder setDtInicialPublicacao(String dtInicialPublicacao) {
		this.searchCriterios.addCriteriosGreaterDate("C.dt_publicacao", dtInicialPublicacao, dtInicialPublicacao != null && !dtInicialPublicacao.trim().equals(""));
		return this;
	}
	
	public EditalSearchBuilder setDtFinalPublicacao(String dtFinalPublicacao) {
		this.searchCriterios.addCriteriosMinorDate("C.dt_publicacao", dtFinalPublicacao, dtFinalPublicacao != null && !dtFinalPublicacao.trim().equals(""));
		return this;
	}
	
	public EditalSearchBuilder setDeslocamento(int nrLimite, int nrPagina) {
		searchCriterios.setQtDeslocamento(((nrLimite * nrPagina) - nrLimite));
		return this;
	}
	
	public EditalSearchBuilder setLimit(int limit) {
		searchCriterios.setQtLimite(limit);
		return this;
	}

	public SearchCriterios build() {
		return this.searchCriterios;
	}
}
