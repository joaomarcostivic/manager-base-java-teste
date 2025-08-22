package com.tivic.manager.mob.ait.circuitoait;

import com.tivic.manager.mob.AitMovimentoServices;

public class CancelarDefesaPrevia extends CircuitoAitItem {

	public CancelarDefesaPrevia() {
		this.setTpStatus(AitMovimentoServices.CANCELAMENTO_DEFESA_PREVIA);
		this.setItemExigido(new CircuitoAitItemPrazo(AitMovimentoServices.DEFESA_PREVIA));
	}
}