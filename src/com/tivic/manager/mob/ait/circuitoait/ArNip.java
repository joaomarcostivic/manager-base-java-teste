package com.tivic.manager.mob.ait.circuitoait;

import com.tivic.manager.mob.AitMovimentoServices;

public class ArNip extends CircuitoAitItem {

	public ArNip() {
		this.setTpStatus(AitMovimentoServices.AR_NIP);
		this.setItemExigido(new CircuitoAitItemPrazo(AitMovimentoServices.NIP_ENVIADA));
	}
}
