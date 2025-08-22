package com.tivic.manager.mob.ait.circuitoait;

import com.tivic.manager.mob.AitMovimentoServices;

public class ApresentacaoCondutorDeferido extends CircuitoAitItem {

	public ApresentacaoCondutorDeferido() {
		this.setTpStatus(AitMovimentoServices.APRESENTACAO_CONDUTOR_DEFERIDO);
		this.setItemExigido(new CircuitoAitItemPrazo(AitMovimentoServices.APRESENTACAO_CONDUTOR));
	}
}
