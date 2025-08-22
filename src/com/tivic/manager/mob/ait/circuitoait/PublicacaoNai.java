package com.tivic.manager.mob.ait.circuitoait;

import com.tivic.manager.mob.AitMovimentoServices;

public class PublicacaoNai extends CircuitoAitItem {

	public PublicacaoNai() {
		this.setTpStatus(AitMovimentoServices.PUBLICACAO_NAI);
		this.setItemExigido(new CircuitoAitItemPrazo(AitMovimentoServices.NAI_ENVIADO));
	}
}
