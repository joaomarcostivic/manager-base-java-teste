package com.tivic.manager.mob.listagempagamentos.builders;

import java.sql.Types;

import com.tivic.sol.search.SearchCriterios;

import sol.dao.ItemComparator;

public class ListagemPagamentosSearchBuilder {
	private SearchCriterios searchCriterios;

	public ListagemPagamentosSearchBuilder() {
		searchCriterios = new SearchCriterios();
	}
	
	public ListagemPagamentosSearchBuilder setStPagamento(Integer stPagamento) {
		searchCriterios.addCriteriosEqualInteger("A.st_pagamento", stPagamento, stPagamento != null);
		return this;
	}

	public ListagemPagamentosSearchBuilder setCdBanco(Integer cdBanco) {
		searchCriterios.addCriteriosEqualInteger("D.cd_banco", cdBanco, cdBanco != null);
		return this;
	}
	
	public ListagemPagamentosSearchBuilder setTpFormaPagamento(Integer tpFormaPagamento) {
		searchCriterios.addCriteriosEqualInteger("A.tp_modalidade", tpFormaPagamento, tpFormaPagamento != null);
		return this;
	}
	
	public ListagemPagamentosSearchBuilder setCdAgencia(Integer cdAgencia) {
		searchCriterios.addCriteriosEqualInteger("E.cd_agencia", cdAgencia, cdAgencia != null);
		return this;
	}
	
	public ListagemPagamentosSearchBuilder setSgUfPagamento(String sgUfPagamento) {
		searchCriterios.addCriteriosEqualString("A.uf_pagamento", sgUfPagamento, sgUfPagamento != null);
		return this;
	}
	
	public ListagemPagamentosSearchBuilder setDtInicialPagamento(String dtInicialPagamento) {
		searchCriterios.addCriteriosGreaterDate("A.dt_pagamento", dtInicialPagamento, dtInicialPagamento != null);
		return this;
	}
	
	public ListagemPagamentosSearchBuilder setDtFinalPagamento(String dtFinalPagamento) {
		searchCriterios.addCriteriosMinorDate("A.dt_pagamento", dtFinalPagamento, dtFinalPagamento != null);
		return this;
	}
	
	public ListagemPagamentosSearchBuilder setDtInicialCredito(String dtInicialCredito) {
		searchCriterios.addCriteriosGreaterDate("A.dt_credito", dtInicialCredito, dtInicialCredito != null);
		return this;
	}
	
	public ListagemPagamentosSearchBuilder setDtFinalCredito(String dtFinalCredito) {
		searchCriterios.addCriteriosMinorDate("A.dt_credito", dtFinalCredito, dtFinalCredito != null);
		return this;
	}
	
	public ListagemPagamentosSearchBuilder setTpArrecadacao(Integer tpArrecadacao) {
		searchCriterios.addCriteriosEqualInteger("A.tp_arrecadacao", tpArrecadacao, tpArrecadacao != null);
		return this;
	}
	
	public ListagemPagamentosSearchBuilder setCdInfracao(Integer cdInfracao) {
		searchCriterios.addCriteriosEqualInteger("B.cd_infracao", cdInfracao, cdInfracao != null);
		return this;
	}
	
	public ListagemPagamentosSearchBuilder setTpPagamento(Integer tpPagamento) {
		searchCriterios.addCriteriosEqualInteger("A.tp_pagamento", tpPagamento, tpPagamento != null);
		return this;
	}
	
	public ListagemPagamentosSearchBuilder setTpCondicionalidade(Integer tpCondicionalidade) {
		if(tpCondicionalidade != null && tpCondicionalidade > 0) {
			searchCriterios.addCriterios("A.vl_pago", "B.vl_multa", ItemComparator.MINOR, Types.BOOLEAN, tpCondicionalidade > 0);
		}
		return this;
	}
	
	public ListagemPagamentosSearchBuilder setTpGrafico(Integer tpGrafico) {
		searchCriterios.addCriteriosEqualInteger("tp_grafico", tpGrafico, tpGrafico != null);
		return this;
	}
	
	public ListagemPagamentosSearchBuilder setCampoAnalisado(String campoAnalisado) {
		if (campoAnalisado.equals("tpFormaPagamento")) {
			searchCriterios.addCriteriosEqualString("campo_analisado", "tpModalidade");
		}
		else if (campoAnalisado.equals("sgUfPagamento")) {
			searchCriterios.addCriteriosEqualString("campo_analisado", "ufPagamento");
		}
		else {
			searchCriterios.addCriteriosEqualString("campo_analisado", campoAnalisado, campoAnalisado != null);
		}
		return this;
	}
	
	public SearchCriterios build() {
        return searchCriterios;
    }
}
