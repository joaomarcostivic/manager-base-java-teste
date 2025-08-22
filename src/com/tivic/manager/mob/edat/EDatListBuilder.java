package com.tivic.manager.mob.edat;

import java.sql.SQLException;
import java.util.List;

import com.tivic.manager.mob.Declarante;
import com.tivic.manager.util.ResultSetMapper;
import com.tivic.manager.util.pagination.PagedResponse;

import sol.dao.ResultSetMap;

public class EDatListBuilder {

	private int total;
	private ResultSetMapper<EDatDTO> boats;
	private ResultSetMapper<Declarante> declarantes;

	public EDatListBuilder(ResultSetMap rsm, int total) {
		try {
			this.boats = new ResultSetMapper<EDatDTO>(rsm, EDatDTO.class);
			this.setDeclarante(rsm);
			this.total = total;
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public EDatListBuilder setDeclarante(ResultSetMap rsm) {
		try {
			this.declarantes = new ResultSetMapper<Declarante>(rsm, Declarante.class);
			return this;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	public PagedResponse<EDatDTO> build() throws IllegalArgumentException, Exception {
		List<EDatDTO> boats = this.boats.toList();

		int size = boats.size();
		List<Declarante> declarantes = this.declarantes.toList();

		for (int i = 0; i < size; i++) {
			EDatDTO dto = boats.get(i);

			if (i < declarantes.size())
				dto.setDeclarante(declarantes.get(i));
		}

		return new PagedResponse<EDatDTO>(boats, this.total);
	}
}
