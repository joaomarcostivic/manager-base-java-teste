package com.tivic.manager.mob.ait.circuitoait;

import com.tivic.manager.mob.AitMovimentoServices;

public class CancelarRegistroDetran extends CircuitoAitItem {

	public CancelarRegistroDetran() {
		super();
		this.setTpStatus(AitMovimentoServices.CANCELA_REGISTRO_MULTA);
		this.setItemExigido(new CircuitoAitItemPrazo(AitMovimentoServices.REGISTRO_INFRACAO));
	}
}