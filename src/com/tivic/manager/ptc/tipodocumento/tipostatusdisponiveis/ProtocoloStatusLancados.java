package com.tivic.manager.ptc.tipodocumento.tipostatusdisponiveis;

import java.util.ArrayList;
import java.util.List;

import com.tivic.manager.mob.AitMovimento;

public class ProtocoloStatusLancados {
	private List<AitMovimento> movimentos;
	private List<Integer> statusMovimentos;
	
	public ProtocoloStatusLancados() {}

	public ProtocoloStatusLancados(List<AitMovimento> movimentos) {
		statusMovimentos = new ArrayList<Integer>();
		this.movimentos = movimentos;
	}
	
	public List<Integer> getStatusLancados() {
		movimentos.forEach(movimento -> {
			statusMovimentos.add(movimento.getTpStatus());
		});
		
		return statusMovimentos;
	}
}