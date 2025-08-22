package com.tivic.manager.mob.lotes.builders.dividaativa;

import com.tivic.sol.search.SearchCriterios;

public class LoteDividaAtivaSerachBuilder {
	
	private SearchCriterios searchCriterios;
	
	public LoteDividaAtivaSerachBuilder() {
		searchCriterios = new SearchCriterios();
	}
	
	public LoteDividaAtivaSerachBuilder setCdLote(int cdLote) {
		searchCriterios.addCriteriosEqualInteger("A.cd_lote", cdLote, cdLote > 0);
		return this;
	}
	
	public LoteDividaAtivaSerachBuilder setIdAit(String idAit) {
		searchCriterios.addCriteriosEqualString("D.id_ait", idAit, idAit != null);
		return this;
	}
	
	public LoteDividaAtivaSerachBuilder setIdLote(String idLote) {
		searchCriterios.addCriteriosEqualString("A.id_lote", idLote, idLote != null);
		return this;
	}
	
	public LoteDividaAtivaSerachBuilder setNrPlaca(String nrPlaca) {
		searchCriterios.addCriteriosEqualString("D.nr_placa", nrPlaca, nrPlaca != null);
		return this;
	}
	
	public LoteDividaAtivaSerachBuilder setStLote(int stLote) {
		searchCriterios.addCriteriosEqualInteger("B.st_lote", stLote, stLote >= 0);
		return this;
	}
	
	public LoteDividaAtivaSerachBuilder setDtCriacaoInicial(String dtCriacaoInicial) {
		searchCriterios.addCriteriosGreaterDate("A.dt_criacao", dtCriacaoInicial, dtCriacaoInicial != null);
		return this;
	}

	public LoteDividaAtivaSerachBuilder setDtCriacaoFinal(String dtCriacaoFinal) {
		searchCriterios.addCriteriosMinorDate("A.dt_criacao", dtCriacaoFinal, dtCriacaoFinal != null);
		return this;
	}
	
	public LoteDividaAtivaSerachBuilder setDtEnvioInicial(String dtEnvioInicial) {
		searchCriterios.addCriteriosGreaterDate("dt_envio", dtEnvioInicial, dtEnvioInicial != null);
		return this;
	}

	public LoteDividaAtivaSerachBuilder setDtEnvioFinal(String dtEnvioFinal) {
		searchCriterios.addCriteriosMinorDate("dt_envio", dtEnvioFinal, dtEnvioFinal != null);
		return this;
	}
	
	public LoteDividaAtivaSerachBuilder setDeslocamento(int nrLimite, int nrPagina) {
		searchCriterios.setQtDeslocamento(((nrLimite * nrPagina) - nrLimite));
		return this;
	}
	
	public LoteDividaAtivaSerachBuilder setLimit(int limit) {
		searchCriterios.setQtLimite(limit);
		return this;
	}
	
	public SearchCriterios build() {
		return searchCriterios;
	}
}
