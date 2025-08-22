package com.tivic.manager.mob.ait.circuitoait;

import com.tivic.manager.mob.AitMovimentoServices;

public class PublicacaoNip extends CircuitoAitItem {

	public PublicacaoNip() {
		this.setTpStatus(AitMovimentoServices.PUBLICACAO_NIP);
		this.setItemExigido(new CircuitoAitItemPrazo(AitMovimentoServices.NIP_ENVIADA));
	}
}
