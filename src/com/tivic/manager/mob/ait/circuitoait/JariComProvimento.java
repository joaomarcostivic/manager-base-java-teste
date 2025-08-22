package com.tivic.manager.mob.ait.circuitoait;

import com.tivic.manager.mob.AitMovimentoServices;

public class JariComProvimento extends CircuitoAitItem {

	public JariComProvimento() {
		this.setTpStatus(AitMovimentoServices.JARI_COM_PROVIMENTO);
		this.setItemCancelamento(AitMovimentoServices.CANCELAMENTO_JARI_COM_PROVIMENTO);
		this.setItemExigido(new CircuitoAitItemPrazo(AitMovimentoServices.RECURSO_JARI));
		this.addItensImpeditivos(AitMovimentoServices.JARI_SEM_PROVIMENTO);
		this.addItensPossiveis(AitMovimentoServices.APRESENTACAO_CONDUTOR);
	}
}