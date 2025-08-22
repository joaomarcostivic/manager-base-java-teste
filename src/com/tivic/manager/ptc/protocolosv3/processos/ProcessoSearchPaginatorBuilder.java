package com.tivic.manager.ptc.protocolosv3.processos;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.tivic.manager.util.pagination.PagedResponse;

public class ProcessoSearchPaginatorBuilder {
	private int total;
	private List<ProcessosSearchDTO> protocolos;

	public ProcessoSearchPaginatorBuilder(List<ProcessosSearchDTO> list, int total) throws SQLException {
	        this.protocolos = new ArrayList<ProcessosSearchDTO>(list);
	        this.total = total;
	}

	public PagedResponse<ProcessosSearchDTO> build() throws IllegalArgumentException, Exception {
		return new PagedResponse<ProcessosSearchDTO>(protocolos, this.total);
	}
}
