package com.tivic.manager.mob.ait.circuitoait;

import com.tivic.manager.mob.AitMovimentoServices;

public class CancelarJariSemProvimento extends CircuitoAitItem {

	public CancelarJariSemProvimento() {
		this.setTpStatus(AitMovimentoServices.CANCELAMENTO_JARI_SEM_PROVIMENTO);
		this.setItemExigido(new CircuitoAitItemPrazo(AitMovimentoServices.JARI_SEM_PROVIMENTO));
	}
}