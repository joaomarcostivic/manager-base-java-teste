package com.tivic.manager.mob.aitmovimento;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.tivic.manager.util.pagination.PagedResponse;

public class AitMovimentoDTOListBuilder {
	private int total;
	private List<AitMovimentoDTO> movimentos;
	
	public AitMovimentoDTOListBuilder(List<AitMovimentoDTO> list, int total) throws SQLException {
		this.movimentos = new ArrayList<AitMovimentoDTO>(list);
		this.total = total;
	}
	
	public PagedResponse<AitMovimentoDTO> build() throws IllegalArgumentException, Exception {		
		return new PagedResponse<AitMovimentoDTO>(movimentos, this.total);
	}
}
