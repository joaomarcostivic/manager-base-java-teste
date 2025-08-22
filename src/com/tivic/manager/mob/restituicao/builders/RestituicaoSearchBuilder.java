package com.tivic.manager.mob.restituicao.builders;

import com.tivic.sol.search.SearchCriterios;

public class RestituicaoSearchBuilder {
	SearchCriterios searchCriterios;
	
	public RestituicaoSearchBuilder() {
		searchCriterios = new SearchCriterios();
	}
	
	public RestituicaoSearchBuilder setDtInicialPagamento(String dtInicial) {
		searchCriterios.addCriteriosGreaterDate("B.dt_pagamento", dtInicial, dtInicial != null);
		return this;
	}
	
	public RestituicaoSearchBuilder setDtFinalPagamento(String dtFinal) {
		searchCriterios.addCriteriosMinorDate("B.dt_pagamento", dtFinal, dtFinal != null);
		return this;
	}
	
	public RestituicaoSearchBuilder setDtInicialMovimento(String dtInicial) {
		searchCriterios.addCriteriosGreaterDate("B.dt_movimento", dtInicial, dtInicial != null);
		return this;
	}
	
	public RestituicaoSearchBuilder setDtFinalMovimento(String dtFinal) {
		searchCriterios.addCriteriosMinorDate("B.dt_movimento", dtFinal, dtFinal != null);
		return this;
	}
	
	public RestituicaoSearchBuilder setTpConsulta(int tpConsulta) {
		searchCriterios.addCriteriosEqualInteger("tp_consulta", tpConsulta);
		return this;
	}
	
	public RestituicaoSearchBuilder setQtLimit(int limit) {
		searchCriterios.setQtLimite(limit);
		return this;
	}
	
	public RestituicaoSearchBuilder setQtDeslocamento(int limit, int page) {
		searchCriterios.setQtDeslocamento((limit*page)-limit);
		return this;
	}
	
	public SearchCriterios build() {
		return searchCriterios;
	}
}
