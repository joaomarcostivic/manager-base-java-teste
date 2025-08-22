package com.tivic.manager.mob.ait.circuitoait;

import com.tivic.manager.mob.AitMovimentoServices;

public class CancelarCetranDeferido extends CircuitoAitItem {

	public CancelarCetranDeferido() {
		this.setTpStatus(AitMovimentoServices.CANCELAMENTO_CETRAN_COM_PROVIMENTO);
		this.setItemExigido(new CircuitoAitItemPrazo(AitMovimentoServices.CETRAN_DEFERIDO));
	}
}