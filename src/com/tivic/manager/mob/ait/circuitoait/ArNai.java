package com.tivic.manager.mob.ait.circuitoait;

import com.tivic.manager.mob.AitMovimentoServices;

public class ArNai extends CircuitoAitItem {

	public ArNai() {
		this.setTpStatus(AitMovimentoServices.AR_NAI);
		this.setItemExigido(new CircuitoAitItemPrazo(AitMovimentoServices.NAI_ENVIADO));
	}
}
