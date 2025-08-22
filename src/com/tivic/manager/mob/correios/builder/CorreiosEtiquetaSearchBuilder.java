package com.tivic.manager.mob.correios.builder;

import com.tivic.manager.mob.correios.CorreiosEtiquetaSearch;

public class CorreiosEtiquetaSearchBuilder {
	
	private CorreiosEtiquetaSearch correiosEtiquetaSearch;
	
	public CorreiosEtiquetaSearchBuilder() {
		this.correiosEtiquetaSearch = new CorreiosEtiquetaSearch();
	}
	
	public CorreiosEtiquetaSearchBuilder setCdTipoArquivo(int cdTipoArquivo) {
        this.correiosEtiquetaSearch.setCdTipoArquivo(cdTipoArquivo);
        return this;
    }
	
	public CorreiosEtiquetaSearchBuilder setNmArquivo(String nmArquivo) {
        this.correiosEtiquetaSearch.setNmArquivo(nmArquivo);
        return this;
    }
	
	public CorreiosEtiquetaSearchBuilder setDtCriacaoInicial(String dtCriacaoInicial) {
        this.correiosEtiquetaSearch.setDtCriacaoInicial(dtCriacaoInicial);
        return this;
    }
	
	public CorreiosEtiquetaSearchBuilder setDtCriacaoFinal(String dtCriacaoFinal) {
        this.correiosEtiquetaSearch.setDtCriacaoFinal(dtCriacaoFinal);
        return this;
    }
	
	public CorreiosEtiquetaSearchBuilder setNrRegistro(int nrRegistro) {
        this.correiosEtiquetaSearch.setNrRegistro(nrRegistro);
        return this;
    }
	
	public CorreiosEtiquetaSearchBuilder setPage(int page) {
        this.correiosEtiquetaSearch.setPage(page);
        return this;
    }
	
	public CorreiosEtiquetaSearchBuilder setLimit(int limit) {
        this.correiosEtiquetaSearch.setLimit(limit);
        return this;
    }
	
	public CorreiosEtiquetaSearch build() {
		return this.correiosEtiquetaSearch;
	}
}
