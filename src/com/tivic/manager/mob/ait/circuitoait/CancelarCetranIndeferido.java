package com.tivic.manager.mob.ait.circuitoait;

import com.tivic.manager.mob.AitMovimentoServices;

public class CancelarCetranIndeferido extends CircuitoAitItem {

	public CancelarCetranIndeferido() {
		this.setTpStatus(AitMovimentoServices.CANCELAMENTO_CETRAN_SEM_PROVIMENTO);
		this.setItemExigido(new CircuitoAitItemPrazo(AitMovimentoServices.CETRAN_INDEFERIDO));
	}
}