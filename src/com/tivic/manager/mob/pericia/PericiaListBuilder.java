package com.tivic.manager.mob.pericia;

import java.sql.SQLException;
import java.util.List;

import com.tivic.manager.util.ResultSetMapper;
import com.tivic.manager.util.pagination.PagedResponse;

import sol.dao.ResultSetMap;

public class PericiaListBuilder {

	private int total;
	private ResultSetMapper<ResultadoPericiaDTO> pericias;

	public PericiaListBuilder(ResultSetMap rsm, int total) {
		try {
			this.pericias = new ResultSetMapper<ResultadoPericiaDTO>(rsm, ResultadoPericiaDTO.class);
			this.total = total;
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public PagedResponse<ResultadoPericiaDTO> build() throws IllegalArgumentException, Exception {
		List<ResultadoPericiaDTO> pericias = this.pericias.toList();

		return new PagedResponse<ResultadoPericiaDTO>(pericias, this.total);
	}

}
