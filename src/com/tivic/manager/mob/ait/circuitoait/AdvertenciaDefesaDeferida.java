package com.tivic.manager.mob.ait.circuitoait;

import com.tivic.manager.mob.AitMovimentoServices;

public class AdvertenciaDefesaDeferida extends CircuitoAitItem {

	public AdvertenciaDefesaDeferida() {
		this.setTpStatus(AitMovimentoServices.ADVERTENCIA_DEFESA_DEFERIDA);
		this.setItemCancelamento(AitMovimentoServices.CANCELAMENTO_ADVERTENCIA_DEFESA_DEFERIDA);
		this.setItemExigido(new CircuitoAitItemPrazo(AitMovimentoServices.ADVERTENCIA_DEFESA_ENTRADA));
		this.addItensImpeditivos(AitMovimentoServices.ADVERTENCIA_DEFESA_INDEFERIDA);
		this.addItensPossiveis(AitMovimentoServices.APRESENTACAO_CONDUTOR);
	}
}
