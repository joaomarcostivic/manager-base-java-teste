package com.tivic.manager.ptc.protocolosv3.documento.ata;

import java.util.ArrayList;
import java.util.List;

import com.tivic.manager.util.pagination.PagedResponse;

public class AtaSearchPaginatorBuilder {
	private int total;
	private List<Ata> atas;

	public AtaSearchPaginatorBuilder(List<Ata> list, int total) {
		this.atas = new ArrayList<Ata>(list);
		this.total = total;
	}

	PagedResponse<Ata> build() throws IllegalArgumentException, Exception {
		return new PagedResponse<Ata>(atas, this.total);
	}

}
