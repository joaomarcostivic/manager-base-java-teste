package com.tivic.manager.mob.ait.circuitoait;

import com.tivic.manager.mob.AitMovimentoServices;

public class CetranIndeferido extends CircuitoAitItem {

	public CetranIndeferido() {
		this.setTpStatus(AitMovimentoServices.CETRAN_INDEFERIDO);
		this.setItemCancelamento(AitMovimentoServices.CANCELAMENTO_CETRAN_SEM_PROVIMENTO);
		this.setItemExigido(new CircuitoAitItemPrazo(AitMovimentoServices.RECURSO_CETRAN));
		this.addItensImpeditivos(AitMovimentoServices.CETRAN_DEFERIDO);
		this.addItensPossiveis(AitMovimentoServices.APRESENTACAO_CONDUTOR);
	}
}