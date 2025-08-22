package com.tivic.manager.mob.ait.relatorios;

public class RelatorioAitCompetenciaEstadualSearchBuilder {
	
	private RelatorioAitCompetenciaEstadualSearch relatorioAitCompetenciaEstadualSearch;
	
	public RelatorioAitCompetenciaEstadualSearchBuilder() {
		this.relatorioAitCompetenciaEstadualSearch = new RelatorioAitCompetenciaEstadualSearch();
	}

    public RelatorioAitCompetenciaEstadualSearchBuilder setCtMovimento(int ctMovimento) {
        this.relatorioAitCompetenciaEstadualSearch.setCtMovimento(ctMovimento);
        return this;
    }
    
    public RelatorioAitCompetenciaEstadualSearchBuilder setDtInicialInfracao(String dtInicialInfracao) {
        this.relatorioAitCompetenciaEstadualSearch.setDtInicialInfracao(dtInicialInfracao);
        return this;
    }

    public RelatorioAitCompetenciaEstadualSearchBuilder setDtFinalInfracao(String dtFinalInfracao) {
        this.relatorioAitCompetenciaEstadualSearch.setDtFinalInfracao(dtFinalInfracao);
        return this;
    }

    public RelatorioAitCompetenciaEstadualSearchBuilder setTpCompetencia(int tpCompetencia) {
        this.relatorioAitCompetenciaEstadualSearch.setTpCompetencia(tpCompetencia);
        return this;
    }
    
    public RelatorioAitCompetenciaEstadualSearch build() {
		return this.relatorioAitCompetenciaEstadualSearch;
	}

}
