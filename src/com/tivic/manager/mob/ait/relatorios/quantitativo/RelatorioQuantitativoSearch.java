package com.tivic.manager.mob.ait.relatorios.quantitativo;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class RelatorioQuantitativoSearch {
	
	private String dtMovimentoInicial;
	private String dtMovimentoFinal;
	private int page;
	private int limit;
	
	public String getDtMovimentoInicial() {
		return dtMovimentoInicial;
	}

	public void setDtMovimentoInicial(String dtMovimentoInicial) {
		this.dtMovimentoInicial = dtMovimentoInicial;
	}

	public String getDtMovimentoFinal() {
		return dtMovimentoFinal;
	}

	public void setDtMovimentoFinal(String dtMovimentoFinal) {
		this.dtMovimentoFinal = dtMovimentoFinal;
	}

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public int getLimit() {
		return limit;
	}

	public void setLimit(int limit) {
		this.limit = limit;
	}

	@Override
	public String toString() {
        try {
            return new ObjectMapper().writeValueAsString(this);
        } 
        catch (JsonProcessingException e) {
            return "Não foi possível serializar o objeto informado";
        }
	}
	
}
