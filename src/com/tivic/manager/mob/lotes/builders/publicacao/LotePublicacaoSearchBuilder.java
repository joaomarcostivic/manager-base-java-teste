package com.tivic.manager.mob.lotes.builders.publicacao;

public class LotePublicacaoSearchBuilder {

	private LotePublicacaoSearch lotePublicacaoSearch;

	public LotePublicacaoSearchBuilder() {
		this.lotePublicacaoSearch = new LotePublicacaoSearch();
	}

	public LotePublicacaoSearchBuilder setTpPublicacao(int tpPublicacao) {
		this.lotePublicacaoSearch.setTpPublicacao(tpPublicacao);
		return this;
	}

	public LotePublicacaoSearchBuilder setIdAit(String idAit) {
		this.lotePublicacaoSearch.setIdAit(idAit);
		return this;
	}

	public LotePublicacaoSearchBuilder setDtCriacaoInicial(String dtCriacaoInicial) {
		this.lotePublicacaoSearch.setDtCriacaoInicial(dtCriacaoInicial);
		return this;
	}

	public LotePublicacaoSearchBuilder setDtCriacaoFinal(String dtCriacaoFinal) {
		this.lotePublicacaoSearch.setDtCriacaoFinal(dtCriacaoFinal);
		return this;
	}

	public LotePublicacaoSearchBuilder setDtPublicacaoInicial(String dtPublicacaoInicial) {
		this.lotePublicacaoSearch.setDtPublicacaoInicial(dtPublicacaoInicial);
		return this;
	}

	public LotePublicacaoSearchBuilder setDtPublicacaoFinal(String dtPublicacaoFinal) {
		this.lotePublicacaoSearch.setDtPublicacaoFinal(dtPublicacaoFinal);
		return this;
	}

	public LotePublicacaoSearchBuilder setPage(int page) {
		this.lotePublicacaoSearch.setPage(page);
		return this;
	}

	public LotePublicacaoSearchBuilder setLimit(int limit) {
		this.lotePublicacaoSearch.setLimit(limit);
		return this;
	}

	public LotePublicacaoSearch build() {
		return this.lotePublicacaoSearch;
	}
}
