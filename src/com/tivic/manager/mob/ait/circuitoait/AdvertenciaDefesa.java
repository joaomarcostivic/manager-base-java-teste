package com.tivic.manager.mob.ait.circuitoait;

import com.tivic.manager.mob.AitMovimentoServices;

public class AdvertenciaDefesa extends CircuitoAitItem {
	
	public AdvertenciaDefesa() {
		this.setTpStatus(AitMovimentoServices.ADVERTENCIA_DEFESA_ENTRADA);
		this.setItemCancelamento(AitMovimentoServices.CANCELAMENTO_ADVERTENCIA_DEFESA_ENTRADA);
		this.setItemExigido(new CircuitoAitItemPrazo(AitMovimentoServices.NAI_ENVIADO));
		this.addItensImpeditivos(AitMovimentoServices.ADVERTENCIA_DEFESA_DEFERIDA);
		this.addItensImpeditivos(AitMovimentoServices.ADVERTENCIA_DEFESA_INDEFERIDA);
		this.addItensPossiveis(AitMovimentoServices.ADVERTENCIA_DEFESA_DEFERIDA);
		this.addItensPossiveis(AitMovimentoServices.ADVERTENCIA_DEFESA_INDEFERIDA);
		this.addItensPossiveis(AitMovimentoServices.DEFESA_PREVIA);
		this.addItensPossiveis(AitMovimentoServices.APRESENTACAO_CONDUTOR);
	}
}
