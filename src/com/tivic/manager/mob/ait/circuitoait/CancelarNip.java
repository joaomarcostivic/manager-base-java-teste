package com.tivic.manager.mob.ait.circuitoait;

import com.tivic.manager.mob.AitMovimentoServices;

public class CancelarNip extends CircuitoAitItem {

	public CancelarNip() {
		this.setTpStatus(AitMovimentoServices.CANCELAMENTO_NIP);
		this.setItemExigido(new CircuitoAitItemPrazo(AitMovimentoServices.NIP_ENVIADA));
	}
}