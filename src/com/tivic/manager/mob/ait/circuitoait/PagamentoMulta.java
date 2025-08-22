package com.tivic.manager.mob.ait.circuitoait;

import com.tivic.manager.mob.AitMovimentoServices;

public class PagamentoMulta extends CircuitoAitItem {

	public PagamentoMulta() {
		this.setTpStatus(AitMovimentoServices.MULTA_PAGA);
		//:TODO cancelamento de multa
		this.addItensPossiveis(AitMovimentoServices.DEVOLUCAO_PAGAMENTO);
		this.addItensPossiveis(AitMovimentoServices.RECURSO_JARI);
		this.addItensPossiveis(AitMovimentoServices.RECURSO_CETRAN);
		this.addItensPossiveis(AitMovimentoServices.APRESENTACAO_CONDUTOR);
		
	}
}
