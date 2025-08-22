package com.tivic.manager.mob.ait.circuitoait;

import com.tivic.manager.mob.AitMovimentoServices;

public class CancelarJariComProvimento extends CircuitoAitItem {

	public CancelarJariComProvimento() {
		this.setTpStatus(AitMovimentoServices.CANCELAMENTO_JARI_COM_PROVIMENTO);
		this.setItemExigido(new CircuitoAitItemPrazo(AitMovimentoServices.JARI_COM_PROVIMENTO));
	}
}