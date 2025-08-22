package com.tivic.manager.mob.ait.circuitoait;

import com.tivic.manager.mob.AitMovimentoServices;

public class CetranDeferido extends CircuitoAitItem {

	public CetranDeferido() {
		this.setTpStatus(AitMovimentoServices.CETRAN_DEFERIDO);
		this.setItemCancelamento(AitMovimentoServices.CANCELAMENTO_CETRAN_COM_PROVIMENTO);
		this.setItemExigido(new CircuitoAitItemPrazo(AitMovimentoServices.RECURSO_CETRAN));
		this.addItensImpeditivos(AitMovimentoServices.CETRAN_INDEFERIDO);
		this.addItensPossiveis(AitMovimentoServices.APRESENTACAO_CONDUTOR);
	}
}