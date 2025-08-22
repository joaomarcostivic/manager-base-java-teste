package com.tivic.manager.mob.ait.circuitoait;

import com.tivic.manager.mob.AitMovimentoServices;

public class DevolverPagamento extends CircuitoAitItem {

	public DevolverPagamento() {
		this.setTpStatus(AitMovimentoServices.DEVOLUCAO_PAGAMENTO);
		this.setItemExigido(new CircuitoAitItemPrazo(AitMovimentoServices.MULTA_PAGA));
	}
}
