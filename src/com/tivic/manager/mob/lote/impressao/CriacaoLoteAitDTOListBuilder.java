package com.tivic.manager.mob.lote.impressao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.tivic.manager.util.pagination.PagedResponse;

public class CriacaoLoteAitDTOListBuilder {

	private int total;
	private List<AitDTO> aits;

	public CriacaoLoteAitDTOListBuilder(List<AitDTO> list, int total) throws SQLException {
	        this.aits = new ArrayList<AitDTO>(list);
	        this.total = total;
	    }

	public PagedResponse<AitDTO> build() throws IllegalArgumentException, Exception {
		return new PagedResponse<AitDTO>(aits, this.total);

	}

}
