package com.tivic.manager.mob.ait.relatorios;

import java.util.List;

import com.tivic.manager.util.pagination.PagedResponse;

public class PageResponseRelatorioAIT<T> extends PagedResponse<T> {
	
	private double totalValor;

	public PageResponseRelatorioAIT(List<T> items, int total, double totalValor) {
		super(items, total);
		this.totalValor = totalValor;
	}

	public double getTotalValor() {
		return totalValor;
	}

	public void setTotalValor(double totalValor) {
		this.totalValor = totalValor;
	}
}
