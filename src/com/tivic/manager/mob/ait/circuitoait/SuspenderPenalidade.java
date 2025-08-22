package com.tivic.manager.mob.ait.circuitoait;

import com.tivic.manager.mob.AitMovimentoServices;

public class SuspenderPenalidade extends CircuitoAitItem {

	public SuspenderPenalidade() {
		this.setTpStatus(AitMovimentoServices.PENALIDADE_SUSPENSA);
		this.addItensImpeditivos(AitMovimentoServices.JARI_COM_PROVIMENTO);
		this.addItensImpeditivos(AitMovimentoServices.CETRAN_DEFERIDO);
		this.addItensImpeditivos(AitMovimentoServices.CETRAN_INDEFERIDO);
	}
}
