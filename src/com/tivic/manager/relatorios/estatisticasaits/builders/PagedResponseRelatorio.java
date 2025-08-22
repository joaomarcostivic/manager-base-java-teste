package com.tivic.manager.relatorios.estatisticasaits.builders;

import java.util.List;

import com.tivic.manager.util.pagination.PagedResponse;

public class PagedResponseRelatorio<T> extends PagedResponse<T> {
	private int somaQuantidade;

	public PagedResponseRelatorio(List<T> items, int total, int somaQuantidade) {
		super(items, total);
		this.somaQuantidade  = somaQuantidade;
	}

	public int getSomaQuantidade() {
		return somaQuantidade;
	}

	public void setSomaQuantidade(int somaQuantidade) {
		this.somaQuantidade = somaQuantidade;
	}
}
