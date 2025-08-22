package com.tivic.manager.mob.ait.circuitoait;

import com.tivic.manager.mob.AitMovimentoServices;

public class RecursoJari extends CircuitoAitItem {

	public RecursoJari() {
		this.setTpStatus(AitMovimentoServices.RECURSO_JARI);
		this.setItemCancelamento(AitMovimentoServices.CANCELAMENTO_RECURSO_JARI);
		this.setItemExigido(new CircuitoAitItemPrazo(AitMovimentoServices.NIP_ENVIADA));
		this.addItensImpeditivos(AitMovimentoServices.JARI_COM_PROVIMENTO);
		this.addItensImpeditivos(AitMovimentoServices.JARI_SEM_PROVIMENTO);
		this.addItensPossiveis(AitMovimentoServices.JARI_COM_PROVIMENTO);
		this.addItensPossiveis(AitMovimentoServices.JARI_SEM_PROVIMENTO);
		this.addItensPossiveis(AitMovimentoServices.APRESENTACAO_CONDUTOR);
	}
}
