package com.tivic.manager.mob.ait.validacao;

public class ValidacaoAitPendenteSearchBuilder {
	
	private ValidacaoAitPendenteSearch validacaoAitPendenteSearch;
	
	public ValidacaoAitPendenteSearchBuilder() {
		this.validacaoAitPendenteSearch = new ValidacaoAitPendenteSearch();
	}
	
	public ValidacaoAitPendenteSearchBuilder setIdAit(String idAit) {
        this.validacaoAitPendenteSearch.setIdAit(idAit);
        return this;
    }

    public ValidacaoAitPendenteSearchBuilder setCdAgente(int cdAgente) {
        this.validacaoAitPendenteSearch.setCdAgente(cdAgente);
        return this;
    }
    
    public ValidacaoAitPendenteSearchBuilder setDtInfracao(String dtInfracao) {
        this.validacaoAitPendenteSearch.setDtInfracao(dtInfracao);
        return this;
    }

    public ValidacaoAitPendenteSearchBuilder setCdOcorrencia(int cdOcorrencia) {
        this.validacaoAitPendenteSearch.setCdOcorrencia(cdOcorrencia);
        return this;
    }
    
    public ValidacaoAitPendenteSearchBuilder setPage(int page) {
        this.validacaoAitPendenteSearch.setPage(page);
        return this;
    }

    public ValidacaoAitPendenteSearchBuilder setLimit(int limit) {
        this.validacaoAitPendenteSearch.setLimit(limit);
        return this;
    }
    
    public ValidacaoAitPendenteSearch build() {
		return this.validacaoAitPendenteSearch;
	}

}
