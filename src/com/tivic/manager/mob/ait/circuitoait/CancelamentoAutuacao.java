package com.tivic.manager.mob.ait.circuitoait;

import com.tivic.manager.mob.AitMovimentoServices;
import com.tivic.manager.mob.ait.circuitoait.CircuitoAitItem;

public class CancelamentoAutuacao extends CircuitoAitItem {

	public CancelamentoAutuacao() {
		this.setTpStatus(AitMovimentoServices.CANCELAMENTO_AUTUACAO);
	}
}