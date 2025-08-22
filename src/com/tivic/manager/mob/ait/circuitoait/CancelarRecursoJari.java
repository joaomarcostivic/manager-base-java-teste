package com.tivic.manager.mob.ait.circuitoait;

import com.tivic.manager.mob.AitMovimentoServices;

public class CancelarRecursoJari extends CircuitoAitItem {

	public CancelarRecursoJari() {
		this.setTpStatus(AitMovimentoServices.CANCELAMENTO_RECURSO_JARI);
		this.setItemExigido(new CircuitoAitItemPrazo(AitMovimentoServices.RECURSO_JARI));
	}
}