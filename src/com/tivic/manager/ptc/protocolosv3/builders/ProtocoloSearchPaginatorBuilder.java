package com.tivic.manager.ptc.protocolosv3.builders;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import com.tivic.manager.ptc.protocolosv3.search.ProtocoloSearchDTO;
import com.tivic.manager.util.pagination.PagedResponse;

public class ProtocoloSearchPaginatorBuilder {
	private int total;
	private List<ProtocoloSearchDTO> protocolos;

	public ProtocoloSearchPaginatorBuilder(List<ProtocoloSearchDTO> list, int total) throws SQLException {
	        this.protocolos = new ArrayList<ProtocoloSearchDTO>(list);
	        this.total = total;
	}

	public PagedResponse<ProtocoloSearchDTO> build() throws IllegalArgumentException, Exception {
		return new PagedResponse<ProtocoloSearchDTO>(protocolos, this.total);
	}
}
