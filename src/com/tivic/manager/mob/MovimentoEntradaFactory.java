package com.tivic.manager.mob;

import java.sql.Connection;

public class MovimentoEntradaFactory {
	
	public AitMovimento getMovimento(AitMovimento movimentoResultado) throws Exception {
		return getMovimento(movimentoResultado, null);
	}
	
	public AitMovimento getMovimento(AitMovimento movimentoResultado, Connection connect) throws Exception {
		if(isDefesa(movimentoResultado))
			return AitMovimentoServices.getDefesaPrevia(movimentoResultado, connect);
		
		if(isJari(movimentoResultado))
			return AitMovimentoServices.getRecursoJari(movimentoResultado, connect);
		
		if(isCetran(movimentoResultado))
			return AitMovimentoServices.getRecursoCetran(movimentoResultado, connect);
		
		if(isDefesaAdvertencia(movimentoResultado))
			return AitMovimentoServices.getAdvertenciaDefesa(movimentoResultado, connect);
		
		throw new Exception("Tipo de recurso não suportado para correção de data de entrada");
	}
	
	private boolean isDefesa(AitMovimento movimento) {
		return movimento.getTpStatus() == AitMovimentoServices.DEFESA_DEFERIDA || movimento.getTpStatus() == AitMovimentoServices.DEFESA_INDEFERIDA;
	}
	
	private boolean isJari(AitMovimento movimento) {
		return movimento.getTpStatus() == AitMovimentoServices.JARI_SEM_PROVIMENTO || movimento.getTpStatus() == AitMovimentoServices.JARI_COM_PROVIMENTO;
	}
	
	private boolean isCetran(AitMovimento movimento) {
		return movimento.getTpStatus() == AitMovimentoServices.CETRAN_DEFERIDO || movimento.getTpStatus() == AitMovimentoServices.CETRAN_INDEFERIDO;
	}
	
	private boolean isDefesaAdvertencia(AitMovimento movimento) {
		return movimento.getTpStatus() == AitMovimentoServices.ADVERTENCIA_DEFESA_DEFERIDA || movimento.getTpStatus() == AitMovimentoServices.ADVERTENCIA_DEFESA_INDEFERIDA;
	}

}
