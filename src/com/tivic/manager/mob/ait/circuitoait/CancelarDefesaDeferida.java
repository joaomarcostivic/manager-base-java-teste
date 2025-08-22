package com.tivic.manager.mob.ait.circuitoait;

import com.tivic.manager.mob.AitMovimentoServices;

public class CancelarDefesaDeferida extends CircuitoAitItem {

	public CancelarDefesaDeferida() {
		this.setTpStatus(AitMovimentoServices.CANCELAMENTO_DEFESA_DEFERIDA);
		this.setItemExigido(new CircuitoAitItemPrazo(AitMovimentoServices.DEFESA_DEFERIDA));
	}
}