package com.tivic.manager.mob.lote.impressao.loteimpressaosearch;

public class LoteImpressaoSearchBuilder {

	private LoteImpressaoSearch loteImpressaoSearch;
	 
	public LoteImpressaoSearchBuilder() {
		this.loteImpressaoSearch = new LoteImpressaoSearch();
	}
	
	public LoteImpressaoSearchBuilder setCdLoteImpressao(int cdLoteImpressao) {
		this.loteImpressaoSearch.setCdLoteImpressao(cdLoteImpressao);
		return this;
	}
	
	public LoteImpressaoSearchBuilder setCdLoteImpressaoAit(int cdLoteImpressaoAit) {
		this.loteImpressaoSearch.setCdLoteImpressaoAit(cdLoteImpressaoAit);
		return this;
	}
	
	public LoteImpressaoSearchBuilder setCdAit(int cdAit) {
		this.loteImpressaoSearch.setCdAit(cdAit);
		return this;
	}
	
	public LoteImpressaoSearchBuilder setIdLoteImpressao(String idLoteImpressao) {
		this.loteImpressaoSearch.setIdLoteImpressao(idLoteImpressao);
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
	
	public LoteImpressaoSearchBuilder setDtInfracao(String dtInfracao) {
		this.loteImpressaoSearch.setDtInfracao(dtInfracao);
		return this;
	}

	public LoteImpressaoSearchBuilder setStLoteImpressao(int stLoteImpressao) {
		this.loteImpressaoSearch.setStLoteImpressao(stLoteImpressao);
		return this;
	}

	public LoteImpressaoSearchBuilder setNrEtiqueta(int nrEtiqueta) {
		this.loteImpressaoSearch.setNrEtiqueta(nrEtiqueta);
		return this;
	}
	
	public LoteImpressaoSearchBuilder setNrPlaca(String nrPlaca) {
		this.loteImpressaoSearch.setNrPlaca(nrPlaca);
		return this;
	}
	
	public LoteImpressaoSearchBuilder setNrRenavan(String nrRenavan) {
		this.loteImpressaoSearch.setNrRenavan(nrRenavan);
		return this;
	}

	public LoteImpressaoSearchBuilder setTpLoteImpressao(int tpLoteImpressao) {
		this.loteImpressaoSearch.setTpLoteImpressao(tpLoteImpressao);
		return this;
	}

	public LoteImpressaoSearchBuilder setTpDocumento(int tpDocumento) {
		this.loteImpressaoSearch.setTpDocumento(tpDocumento);
		return this;
	}
	
	public LoteImpressaoSearchBuilder setTpDestino(int tpDestino) {
		this.loteImpressaoSearch.setTpDestino(tpDestino);
		return this;
	}

	public LoteImpressaoSearchBuilder setTpTransporte(int tpTransporte) {
		this.loteImpressaoSearch.setTpTransporte(tpTransporte);
		return this;
	}

	public LoteImpressaoSearchBuilder setCdUsuario(int cdUsuario) {
		this.loteImpressaoSearch.setCdUsuario(cdUsuario);
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
