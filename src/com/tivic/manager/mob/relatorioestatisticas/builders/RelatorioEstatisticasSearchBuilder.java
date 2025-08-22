package com.tivic.manager.mob.relatorioestatisticas.builders;

import com.tivic.sol.search.SearchCriterios;

public class RelatorioEstatisticasSearchBuilder {
	SearchCriterios searchCriterios;

	public RelatorioEstatisticasSearchBuilder() {
		searchCriterios = new SearchCriterios();
	}

	public RelatorioEstatisticasSearchBuilder setDtInicialMovimento(String dtInicial) {
		searchCriterios.addCriteriosGreaterDate("A.dt_movimento", dtInicial, dtInicial != null);
		return this;
	}

	public RelatorioEstatisticasSearchBuilder setDtFinalMovimento(String dtFinal) {
		searchCriterios.addCriteriosMinorDate("A.dt_movimento", dtFinal, dtFinal != null);
		return this;
	}
	
	public RelatorioEstatisticasSearchBuilder setDtInicialFiltro(String dtInicial) {
		searchCriterios.addCriteriosEqualString("dt_inicial", dtInicial, dtInicial != null);
		return this;
	}

	public RelatorioEstatisticasSearchBuilder setDtFinalFiltro(String dtFinal) {
		searchCriterios.addCriteriosEqualString("dt_final", dtFinal, dtFinal != null);
		return this;
	}
	
	public RelatorioEstatisticasSearchBuilder setDtInicialVencimento(String dtInicial) {
		searchCriterios.addCriteriosGreaterDate("B.dt_vencimento", dtInicial, dtInicial != null);
		return this;
	}
	
	public RelatorioEstatisticasSearchBuilder setDtFinalVencimento(String dtFinal) {
		searchCriterios.addCriteriosMinorDate("B.dt_vencimento", dtFinal, dtFinal != null);
		return this;
	}
	
	public RelatorioEstatisticasSearchBuilder setTpConsulta(int tpConsulta) {
		searchCriterios.addCriteriosEqualInteger("tp_consulta", tpConsulta);
		return this;
	}
	
	public RelatorioEstatisticasSearchBuilder setTpGrafico(int tpGrafico) {
		searchCriterios.addCriteriosEqualInteger("tp_grafico", tpGrafico);
		return this;
	}
	
	public RelatorioEstatisticasSearchBuilder setTpPeriodicidade(int tpPeriodicidade) {
		searchCriterios.addCriteriosEqualInteger("tp_periodicidade", tpPeriodicidade);
		return this;
	}
	
	public RelatorioEstatisticasSearchBuilder setPeriodicidade(int tpPeriodicidade) {
		String periodicidade = "";
		String aggregate = "";
		switch(tpPeriodicidade) {
		case 1:
			periodicidade = "date(A.dt_movimento) AS dt_notificacao";
			aggregate = " date(A.dt_movimento) ";
			break;
		case 2:
			periodicidade = "date_part('month', A.dt_movimento) AS mes_notificacao, date_part('year', A.dt_movimento) AS ano_notificacao";
			aggregate = " ano_notificacao, mes_notificacao ";
			break;
		case 3:
			periodicidade = "date_part('year', A.dt_movimento) AS ano_notificacao";
			aggregate = " ano_notificacao ";
			break;
		}
		searchCriterios.addCriteriosEqualString("periodicidade", periodicidade, !periodicidade.isEmpty());
		searchCriterios.addCriteriosEqualString("aggregate_function", aggregate, !aggregate.isEmpty());
		return this;
	}
	
	public RelatorioEstatisticasSearchBuilder setPeriodicidadePagamento(int tpPeriodicidade) {
		String periodicidade = "";
		String aggregate = "";
		switch(tpPeriodicidade) {
		case 1:
			periodicidade = "date(B.dt_vencimento) AS dt_vencimento";
			aggregate = " date(B.dt_vencimento) ";
			break;
		case 2:
			periodicidade = "date_part('month', B.dt_vencimento) AS mes_vencimento, date_part('year', B.dt_vencimento) AS ano_vencimento";
			aggregate = " ano_vencimento, mes_vencimento ";
			break;
		case 3:
			periodicidade = "date_part('year', B.dt_vencimento) AS ano_vencimento";
			aggregate = " ano_vencimento ";
			break;
		}
		searchCriterios.addCriteriosEqualString("periodicidade", periodicidade, !periodicidade.isEmpty());
		searchCriterios.addCriteriosEqualString("aggregate_function", aggregate, !aggregate.isEmpty());
		return this;
	}

	public SearchCriterios build() {
		return searchCriterios;
	}
}
