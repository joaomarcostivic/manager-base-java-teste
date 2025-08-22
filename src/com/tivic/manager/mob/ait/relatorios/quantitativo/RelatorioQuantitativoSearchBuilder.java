package com.tivic.manager.mob.ait.relatorios.quantitativo;

public class RelatorioQuantitativoSearchBuilder {
	
	private RelatorioQuantitativoSearch relatorioQuantitativoSearch;
	
	public RelatorioQuantitativoSearchBuilder() {
		this.relatorioQuantitativoSearch = new RelatorioQuantitativoSearch();
	}

    public RelatorioQuantitativoSearchBuilder setDtMovimentoInicial(String dtMovimentoIncial) {
        this.relatorioQuantitativoSearch.setDtMovimentoInicial(dtMovimentoIncial);
        return this;
    }
    
    public RelatorioQuantitativoSearchBuilder setDtMovimentoFinal(String dtMovimentoFinal) {
        this.relatorioQuantitativoSearch.setDtMovimentoFinal(dtMovimentoFinal);
        return this;
    }

    public RelatorioQuantitativoSearchBuilder setPage(int page) {
        this.relatorioQuantitativoSearch.setPage(page);
        return this;
    }

    public RelatorioQuantitativoSearchBuilder setLimit(int limit) {
        this.relatorioQuantitativoSearch.setLimit(limit);
        return this;
    }
    
    public RelatorioQuantitativoSearch build() {
		return this.relatorioQuantitativoSearch;
	}

}
