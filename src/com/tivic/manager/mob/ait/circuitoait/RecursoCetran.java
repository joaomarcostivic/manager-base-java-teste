package com.tivic.manager.mob.ait.circuitoait;

import com.tivic.manager.mob.AitMovimentoServices;

public class RecursoCetran extends CircuitoAitItem {

	public RecursoCetran() {
		this.setTpStatus(AitMovimentoServices.RECURSO_CETRAN);
		this.setItemCancelamento(AitMovimentoServices.CANCELAMENTO_RECURSO_CETRAN);
		this.setItemExigido(new CircuitoAitItemPrazo(AitMovimentoServices.JARI_SEM_PROVIMENTO));
		this.addItensImpeditivos(AitMovimentoServices.JARI_COM_PROVIMENTO);
		this.addItensImpeditivos(AitMovimentoServices.CETRAN_DEFERIDO);
		this.addItensImpeditivos(AitMovimentoServices.CETRAN_INDEFERIDO);
		this.addItensPossiveis(AitMovimentoServices.CETRAN_DEFERIDO);
		this.addItensPossiveis(AitMovimentoServices.CETRAN_INDEFERIDO);
		this.addItensPossiveis(AitMovimentoServices.APRESENTACAO_CONDUTOR);
	}
}
