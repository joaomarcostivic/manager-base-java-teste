package com.tivic.manager.mob.ait.circuitoait;

import com.tivic.manager.mob.AitMovimentoServices;

public class CancelarRecursoCetran extends CircuitoAitItem {

	public CancelarRecursoCetran() {
		this.setTpStatus(AitMovimentoServices.CANCELAMENTO_RECURSO_CETRAN);
		this.setItemExigido(new CircuitoAitItemPrazo(AitMovimentoServices.RECURSO_CETRAN));
	}
}