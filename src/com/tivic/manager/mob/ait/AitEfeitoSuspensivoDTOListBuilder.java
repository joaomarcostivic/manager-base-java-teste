package com.tivic.manager.mob.ait;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.tivic.manager.mob.EfeitoSuspensivoDTO;
import com.tivic.manager.util.pagination.PagedResponse;

public class AitEfeitoSuspensivoDTOListBuilder {
	private int total;
	private List<EfeitoSuspensivoDTO> aits;
	
	public AitEfeitoSuspensivoDTOListBuilder(List<EfeitoSuspensivoDTO> list, int total) throws SQLException {
		this.aits = new ArrayList<EfeitoSuspensivoDTO>(list);
		this.total = total;
	}
	
	public PagedResponse<EfeitoSuspensivoDTO> build() throws IllegalArgumentException, Exception {		
		return new PagedResponse<EfeitoSuspensivoDTO>(aits, this.total);
	}
}
