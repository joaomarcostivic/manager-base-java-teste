package com.tivic.manager.mob.ait.circuitoait;

import com.tivic.manager.mob.AitMovimentoServices;

public class JariSemProvimento extends CircuitoAitItem {

	public JariSemProvimento() {
		this.setTpStatus(AitMovimentoServices.JARI_SEM_PROVIMENTO);
		this.setItemCancelamento(AitMovimentoServices.CANCELAMENTO_JARI_SEM_PROVIMENTO);
		this.setItemExigido(new CircuitoAitItemPrazo(AitMovimentoServices.RECURSO_JARI));
		this.addItensImpeditivos(AitMovimentoServices.JARI_COM_PROVIMENTO);
		this.addItensPossiveis(AitMovimentoServices.MULTA_PAGA);
		this.addItensPossiveis(AitMovimentoServices.RECURSO_CETRAN);
		this.addItensPossiveis(AitMovimentoServices.APRESENTACAO_CONDUTOR);
	}
}