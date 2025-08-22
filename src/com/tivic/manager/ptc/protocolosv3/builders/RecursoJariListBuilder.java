package com.tivic.manager.ptc.protocolosv3.builders;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.tivic.manager.ptc.protocolosv3.search.ProtocoloSearchDTO;

public class RecursoJariListBuilder {
	private int total;
	private List<ProtocoloSearchDTO> protocolos;
    private double mediaTempoJulgamento ;

	public RecursoJariListBuilder(List<ProtocoloSearchDTO> list, int total, Double mediaTempoJulgamento) throws SQLException {
	        this.protocolos = new ArrayList<ProtocoloSearchDTO>(list);
	        this.total = total;
	        this.mediaTempoJulgamento  = mediaTempoJulgamento ;
	}

	public PagedResponseRecursoJari<ProtocoloSearchDTO> build() throws IllegalArgumentException, Exception {
		return new PagedResponseRecursoJari<ProtocoloSearchDTO>(protocolos, this.total, this.mediaTempoJulgamento );
	}
}