package com.tivic.manager.ptc.protocolosv3.builders;

import java.util.List;

import com.tivic.manager.util.pagination.PagedResponse;

public class PagedResponseRecursoJari <T> extends PagedResponse<T> {

	private double mediaTempoJulgamento ;

	public PagedResponseRecursoJari(List<T> items, int total, double mediaTempoJulgamento) {
		super(items, total);
		this.mediaTempoJulgamento  = mediaTempoJulgamento;
	}

	public double getMediaJulgamento() {
		return mediaTempoJulgamento ;
	}

	public void setMediaJulgamento(double mediaTempoJulgamento ) {
		this.mediaTempoJulgamento  = mediaTempoJulgamento ;
	}
}