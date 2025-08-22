package com.tivic.manager.mob.ait.circuitoait;

import com.tivic.manager.mob.AitMovimentoServices;

public class AdvertenciaDefesaIndeferida extends CircuitoAitItem {

	public AdvertenciaDefesaIndeferida() {
		this.setTpStatus(AitMovimentoServices.ADVERTENCIA_DEFESA_INDEFERIDA);
		this.setItemCancelamento(AitMovimentoServices.CANCELAMENTO_ADVERTENCIA_DEFESA_INDEFERIDA);
		this.setItemExigido(new CircuitoAitItemPrazo(AitMovimentoServices.ADVERTENCIA_DEFESA_ENTRADA));
		this.addItensImpeditivos(AitMovimentoServices.ADVERTENCIA_DEFESA_DEFERIDA);
		this.addItensPossiveis(AitMovimentoServices.FIM_PRAZO_DEFESA);
		this.addItensPossiveis(AitMovimentoServices.NIP_ENVIADA);
		this.addItensPossiveis(AitMovimentoServices.APRESENTACAO_CONDUTOR);
	}
	
}
