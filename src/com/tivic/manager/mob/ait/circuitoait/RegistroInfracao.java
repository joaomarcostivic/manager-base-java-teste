package com.tivic.manager.mob.ait.circuitoait;

import com.tivic.manager.mob.AitMovimentoServices;

public class RegistroInfracao extends CircuitoAitItem {

	public RegistroInfracao() {
		super();
		this.setTpStatus(AitMovimentoServices.REGISTRO_INFRACAO);
		this.setItemCancelamento(AitMovimentoServices.CANCELA_REGISTRO_MULTA);
		this.addItensImpeditivos(AitMovimentoServices.NAI_ENVIADO);
		this.addItensPossiveis(AitMovimentoServices.NAI_ENVIADO);
	}
}
