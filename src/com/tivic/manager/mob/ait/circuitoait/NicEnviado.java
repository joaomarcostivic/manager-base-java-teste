package com.tivic.manager.mob.ait.circuitoait;

import com.tivic.manager.mob.AitMovimentoServices;

public class NicEnviado extends CircuitoAitItem {
	public NicEnviado() {
		super();
		this.setTpStatus(AitMovimentoServices.NIC_ENVIADO);
		this.setItemCancelamento(AitMovimentoServices.CANCELA_REGISTRO_MULTA);
		this.addItensImpeditivos(AitMovimentoServices.NAI_ENVIADO);
		this.addItensPossiveis(AitMovimentoServices.NAI_ENVIADO);
	}
}
