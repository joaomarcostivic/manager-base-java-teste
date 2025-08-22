package com.tivic.manager.mob.lotes.builders.impressao;

public class LoteImpressaoSearchBuilder {
	
	private LoteImpressaoSearch loteImpressaoSearch;
	 
	public LoteImpressaoSearchBuilder() {
		this.loteImpressaoSearch = new LoteImpressaoSearch();
	}
	
	public LoteImpressaoSearchBuilder setCdLoteImpressao(int cdLoteImpressao) {
		this.loteImpressaoSearch.setCdLoteImpressao(cdLoteImpressao);
		return this;
	}
	
	public LoteImpressaoSearchBuilder setCdAit(int cdAit) {
		this.loteImpressaoSearch.setCdAit(cdAit);
		return this;
	}
	
	public LoteImpressaoSearchBuilder setIdLote(String idLote) {
		this.loteImpressaoSearch.setIdLote(idLote);
		return this;
	}
	
	public LoteImpressaoSearchBuilder setIdAit(String idAit) {
		this.loteImpressaoSearch.setIdAit(idAit);
		return this;
	}

	public LoteImpressaoSearchBuilder setDtCriacao(String dtCriacao) {
		this.loteImpressaoSearch.setDtCriacao(dtCriacao);
		return this;
	}

	public LoteImpressaoSearchBuilder setStLote(int stLote) {
		this.loteImpressaoSearch.setStLote(stLote);
		return this;
	}
	
	public LoteImpressaoSearchBuilder setTpImpressao(int tpImpressao) {
		this.loteImpressaoSearch.setTpImpressao(tpImpressao);
		return this;
	}

	public LoteImpressaoSearchBuilder setCdUsuario(int cdUsuario) {
		this.loteImpressaoSearch.setCdUsuario(cdUsuario);
		return this;
	}
	
	public LoteImpressaoSearchBuilder setNrRenavan(String nrRenavan) {
		this.loteImpressaoSearch.setNrRenavan(nrRenavan);
		return this;
	}
	
	public LoteImpressaoSearchBuilder setNrPlaca(String nrPlaca) {
		this.loteImpressaoSearch.setNrPlaca(nrPlaca);
		return this;
	}
	
	public LoteImpressaoSearchBuilder setDtInfracao(String dtInfracao) {
		this.loteImpressaoSearch.setDtInfracao(dtInfracao);
		return this;
	}

	public LoteImpressaoSearchBuilder setPage(int page) {
		this.loteImpressaoSearch.setPage(page);
		return this;
	}

	public LoteImpressaoSearchBuilder setLimit(int limit) {
		this.loteImpressaoSearch.setLimit(limit);
		return this;
	}
	
	public LoteImpressaoSearch build() {
		return this.loteImpressaoSearch;
	}

}
