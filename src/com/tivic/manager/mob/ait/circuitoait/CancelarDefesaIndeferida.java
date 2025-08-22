package com.tivic.manager.mob.ait.circuitoait;

import com.tivic.manager.mob.AitMovimentoServices;

public class CancelarDefesaIndeferida extends CircuitoAitItem {

	public CancelarDefesaIndeferida() {
		this.setTpStatus(AitMovimentoServices.CANCELAMENTO_DEFESA_INDEFERIDA);
		this.setItemExigido(new CircuitoAitItemPrazo(AitMovimentoServices.DEFESA_INDEFERIDA));
	}
}