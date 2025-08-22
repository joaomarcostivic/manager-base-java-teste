package com.tivic.manager.mob.aitpagamento.builders;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import org.codehaus.groovy.syntax.Types;

import com.tivic.sol.search.SearchCriterios;

import sol.dao.ItemComparator;

public class PagamentosSearchBuilder {
	SearchCriterios searchCriterios;
	
	public PagamentosSearchBuilder() {
		searchCriterios = new SearchCriterios();
	}
	
	public PagamentosSearchBuilder setDtInicialPagamento(String dtInicial) {
		searchCriterios.addCriteriosGreaterDate("A.dt_pagamento", dtInicial, dtInicial != null);
		return this;
	}
	
	public PagamentosSearchBuilder setDtFinalPagamento(String dtFinal) {
		searchCriterios.addCriteriosMinorDate("A.dt_pagamento", dtFinal, dtFinal != null);
		return this;
	}
	
	public PagamentosSearchBuilder setDtInicialInfracao(String dtInicial) {
		searchCriterios.addCriteriosGreaterDate("A.dt_infracao", dtInicial, dtInicial != null);
		return this;
	}
	
	public PagamentosSearchBuilder setDtFinalInfracao(String dtFinal) {
		searchCriterios.addCriteriosMinorDate("A.dt_infracao", dtFinal, dtFinal != null);
		return this;
	}
	
	public PagamentosSearchBuilder setSgUfVeiculo(String sgUfVeiculo) {
		searchCriterios.addCriteriosEqualString("A.sg_uf_veiculo", sgUfVeiculo);
		return this;
	}
	
	public PagamentosSearchBuilder setNrBanco(String nrBanco, int tpConsulta) {
		if (tpConsulta == 3) return this;
		searchCriterios.addCriteriosEqualString("C.nr_banco", nrBanco, nrBanco != null);
		searchCriterios.addCriterios("C.nr_banco", null, ItemComparator.ISNULL, Types.STRING, nrBanco == null);
		return this;
	}
	
	public PagamentosSearchBuilder setTpConsulta(int tpConsulta) {
		searchCriterios.addCriteriosEqualInteger("tp_consulta", tpConsulta);
		return this;
	}
	
	public PagamentosSearchBuilder setTpPeriodicidade(int tpPeriodicidade) {
		searchCriterios.addCriteriosEqualInteger("tp_periodicidade", tpPeriodicidade);
		return this;
	}
	
	public PagamentosSearchBuilder setDtPagamentoDetalhamento(int tpPeriodicidade, String dtPagamento) {
        LocalDate localDate = OffsetDateTime.parse(dtPagamento).toLocalDate();
		LocalDate nextDay = localDate.plusDays(1);
		switch (tpPeriodicidade) {
		case 1:
			searchCriterios.addCriteriosGreaterDate("A.dt_pagamento", localDate.toString(), dtPagamento != null);
			searchCriterios.addCriterios("A.dt_pagamento", nextDay.toString(), ItemComparator.MINOR, java.sql.Types.CHAR);
			break;
		case 2: 
			searchCriterios.addCriteriosEqualInteger("date_part('month', A.dt_pagamento)", localDate.getMonthValue());
			searchCriterios.addCriteriosEqualInteger("date_part('year', A.dt_pagamento)", localDate.getYear());
			break;
		case 3:
			searchCriterios.addCriteriosEqualInteger("date_part('year', A.dt_pagamento)", localDate.getYear());
			break;
		}
		return this;
	}
	
	public PagamentosSearchBuilder setPeriodicidade(int tpPeriodicidade) {
		String periodicidade = "";
		String aggregate = "";
		switch(tpPeriodicidade) {
		case 1:
			periodicidade = "date(A.dt_pagamento) AS dt_periodo_pagamento";
			aggregate = "dt_periodo_pagamento";
			break;
		case 2:
			periodicidade = "date_part('month', A.dt_pagamento) AS mes_pagamento, date_part('year', A.dt_pagamento) AS ano_pagamento";
			aggregate = "ano_pagamento, mes_pagamento";
			break;
		case 3:
			periodicidade = "date_part('year', A.dt_pagamento) AS ano_pagamento";
			aggregate = "ano_pagamento";
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
