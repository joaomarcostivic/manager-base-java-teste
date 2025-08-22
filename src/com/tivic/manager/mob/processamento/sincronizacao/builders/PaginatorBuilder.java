package com.tivic.manager.mob.processamento.sincronizacao.builders;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.tivic.manager.util.pagination.PagedResponse;

public class PaginatorBuilder<T> {
	private int total;
	private List<T> list;

	public PaginatorBuilder(List<T> list, int total) throws SQLException {
	        this.list = new ArrayList<T>(list);
	        this.total = total;
	}

	public PagedResponse<T> build() throws IllegalArgumentException, Exception {
		return new PagedResponse<T>(this.list, this.total);
	}
}
