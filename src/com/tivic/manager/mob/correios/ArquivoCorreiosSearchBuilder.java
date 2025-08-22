package com.tivic.manager.mob.correios;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.tivic.manager.grl.Arquivo;
import com.tivic.manager.ptc.protocolosv3.search.ProtocoloSearchDTO;
import com.tivic.manager.util.pagination.PagedResponse;

public class ArquivoCorreiosSearchBuilder {

	private int total;
	private List<Arquivo> arquivos;

	public ArquivoCorreiosSearchBuilder(List<Arquivo> list, int total) throws SQLException {
	        this.arquivos = new ArrayList<Arquivo>(list);
	        this.total = total;
	}

	public PagedResponse<Arquivo> build() throws IllegalArgumentException, Exception {
		return new PagedResponse<Arquivo>(arquivos, this.total);
	}
}