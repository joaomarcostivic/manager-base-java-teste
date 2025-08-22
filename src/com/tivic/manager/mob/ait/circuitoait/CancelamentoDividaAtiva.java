package com.tivic.manager.mob.ait.circuitoait;

import com.tivic.manager.mob.AitMovimentoServices;

public class CancelamentoDividaAtiva extends CircuitoAitItem {

	public CancelamentoDividaAtiva() {
		this.setTpStatus(AitMovimentoServices.CANCELAMENTO_DIVIDA_ATIVA);
	}
}
