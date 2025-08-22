package com.tivic.manager.mob.ait.circuitoait;

import com.tivic.manager.mob.AitMovimentoServices;

public class NipEnviado extends CircuitoAitItem {

	public NipEnviado() {
		this.setTpStatus(AitMovimentoServices.NIP_ENVIADA);
		this.setItemCancelamento(AitMovimentoServices.CANCELAMENTO_NIP);
		this.setItemExigido(new CircuitoAitItemPrazo(AitMovimentoServices.NAI_ENVIADO));
		this.addItensImpeditivos(AitMovimentoServices.NOTIFICACAO_ADVERTENCIA);
		this.addItensImpeditivos(AitMovimentoServices.MULTA_PAGA);
		this.addItensPossiveis(AitMovimentoServices.AR_NIP);
		this.addItensPossiveis(AitMovimentoServices.PUBLICACAO_NIP);
		this.addItensPossiveis(AitMovimentoServices.MULTA_PAGA);
		this.addItensPossiveis(AitMovimentoServices.RECURSO_JARI);
		this.addItensPossiveis(AitMovimentoServices.APRESENTACAO_CONDUTOR);
	}
}
