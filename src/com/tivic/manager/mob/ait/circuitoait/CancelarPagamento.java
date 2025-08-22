package com.tivic.manager.mob.ait.circuitoait;

import com.tivic.manager.mob.AitMovimentoServices;

public class CancelarPagamento extends CircuitoAitItem {
	
	public CancelarPagamento() {
		this.setTpStatus(AitMovimentoServices.CANCELAMENTO_PAGAMENTO);
	}
}
