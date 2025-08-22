package com.tivic.manager.mob.ait.circuitoait;

import com.tivic.manager.mob.AitMovimentoServices;

public class FimPrazoDefesa extends CircuitoAitItem {

	public FimPrazoDefesa() {
		this.setTpStatus(AitMovimentoServices.FIM_PRAZO_DEFESA);
		this.setItemExigido(new CircuitoAitItemPrazo(AitMovimentoServices.NAI_ENVIADO));
		this.addItensImpeditivos(AitMovimentoServices.NIP_ENVIADA);
		this.addItensPossiveis(AitMovimentoServices.NIP_ENVIADA);
	}
}
