package com.tivic.manager.mob.ait.circuitoait;

import com.tivic.manager.mob.AitMovimentoServices;

public class DefesaDeferida extends CircuitoAitItem {

	public DefesaDeferida() {
		this.setTpStatus(AitMovimentoServices.DEFESA_DEFERIDA);
		this.setItemCancelamento(AitMovimentoServices.CANCELAMENTO_DEFESA_DEFERIDA);
		this.setItemExigido(new CircuitoAitItemPrazo(AitMovimentoServices.DEFESA_PREVIA));
		this.addItensImpeditivos(AitMovimentoServices.DEFESA_INDEFERIDA);
		this.addItensPossiveis(AitMovimentoServices.APRESENTACAO_CONDUTOR);
	}
}
