package com.tivic.manager.mob.ait.circuitoait;

import com.tivic.manager.mob.AitMovimentoServices;

public class DefesaIndeferida extends CircuitoAitItem {

	public DefesaIndeferida() {
		this.setTpStatus(AitMovimentoServices.DEFESA_INDEFERIDA);
		this.setItemCancelamento(AitMovimentoServices.CANCELAMENTO_DEFESA_INDEFERIDA);
		this.setItemExigido(new CircuitoAitItemPrazo(AitMovimentoServices.DEFESA_PREVIA));
		this.addItensImpeditivos(AitMovimentoServices.DEFESA_DEFERIDA);
		this.addItensPossiveis(AitMovimentoServices.FIM_PRAZO_DEFESA);
		this.addItensPossiveis(AitMovimentoServices.NIP_ENVIADA);
		this.addItensPossiveis(AitMovimentoServices.APRESENTACAO_CONDUTOR);
	}
}
