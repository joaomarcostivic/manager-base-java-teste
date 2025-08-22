package com.tivic.manager.mob.ait.circuitoait;

import com.tivic.manager.mob.AitMovimentoServices;

public class ApresentacaoCondutor extends CircuitoAitItem {

	public ApresentacaoCondutor() {
		this.setTpStatus(AitMovimentoServices.APRESENTACAO_CONDUTOR);
		this.setItemExigido(new CircuitoAitItemPrazo(AitMovimentoServices.NAI_ENVIADO));
		this.addItensPossiveis(AitMovimentoServices.APRESENTACAO_CONDUTOR_DEFERIDO);
		this.addItensPossiveis(AitMovimentoServices.DEFESA_PREVIA);
		this.addItensPossiveis(AitMovimentoServices.ADVERTENCIA_DEFESA_ENTRADA);
		
	}
}
