package com.tivic.manager.ptc.protocolosv3.documento.ata;

import java.util.ArrayList;
import java.util.List;

import com.tivic.manager.util.pagination.PagedResponse;

public class AtaDTOSearchPaginatorBuilder {
	private int total;
	private List<AtaDTO> dtos;

	public AtaDTOSearchPaginatorBuilder(List<AtaDTO> list, int total) {
		this.dtos = new ArrayList<AtaDTO>(list);
		this.total = total;
	}

	PagedResponse<AtaDTO> build() throws IllegalArgumentException, Exception {
		return new PagedResponse<AtaDTO>(dtos, this.total);
	}
}
