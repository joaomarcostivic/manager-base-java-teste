package com.tivic.manager.mob.ait.relatorios;

import java.util.List;

import com.tivic.manager.util.pagination.PagedResponse;

public class RelatorioPagedResponse extends PagedResponse<RelatorioAitDTO>  {

	double vlTotal;

	public RelatorioPagedResponse(List<RelatorioAitDTO> items, int total, double vlTotal) {
		super(items, total);
		this.vlTotal = vlTotal;
	}	
	
	public double getVlTotal() {
		return vlTotal;
	}

	public void setVlTotal(double vlTotal) {
		this.vlTotal = vlTotal;
	}

}
