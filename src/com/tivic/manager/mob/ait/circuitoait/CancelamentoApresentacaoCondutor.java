package com.tivic.manager.mob.ait.circuitoait;

import com.tivic.manager.mob.AitMovimentoServices;

public class CancelamentoApresentacaoCondutor  extends CircuitoAitItem {
	
	public CancelamentoApresentacaoCondutor() {
		this.setTpStatus(AitMovimentoServices.CANCELAMENTO_TRANSFERENCIA_PONTUACAO);
		this.setItemExigido(new CircuitoAitItemPrazo(AitMovimentoServices.APRESENTACAO_CONDUTOR));
	}
}
