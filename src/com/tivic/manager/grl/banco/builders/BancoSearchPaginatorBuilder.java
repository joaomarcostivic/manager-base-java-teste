package com.tivic.manager.grl.banco.builders;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.tivic.manager.grl.Banco;
import com.tivic.manager.util.pagination.PagedResponse;

public class BancoSearchPaginatorBuilder {
	private int total;
	private List<Banco> bancos;

	public BancoSearchPaginatorBuilder(List<Banco> list, int total) throws SQLException {
	        this.bancos = new ArrayList<Banco>(list);
	        this.total = total;
	}

	public PagedResponse<Banco> build() throws IllegalArgumentException, Exception {
		return new PagedResponse<Banco>(bancos, this.total);
	}
}
