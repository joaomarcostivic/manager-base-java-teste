package com.tivic.manager.mob.ait.circuitoait;

import com.tivic.manager.mob.AitMovimentoServices;

public class DefesaPrevia extends CircuitoAitItem {

	public DefesaPrevia() {
		this.setTpStatus(AitMovimentoServices.DEFESA_PREVIA);
		this.setItemCancelamento(AitMovimentoServices.CANCELAMENTO_DEFESA_PREVIA);
		this.setItemExigido(new CircuitoAitItemPrazo(AitMovimentoServices.NAI_ENVIADO));
		this.addItensImpeditivos(AitMovimentoServices.DEFESA_DEFERIDA);
		this.addItensImpeditivos(AitMovimentoServices.DEFESA_INDEFERIDA);
		this.addItensPossiveis(AitMovimentoServices.DEFESA_DEFERIDA);
		this.addItensPossiveis(AitMovimentoServices.DEFESA_INDEFERIDA);
		this.addItensPossiveis(AitMovimentoServices.ADVERTENCIA_DEFESA_ENTRADA);
		this.addItensPossiveis(AitMovimentoServices.APRESENTACAO_CONDUTOR);
	}
}
