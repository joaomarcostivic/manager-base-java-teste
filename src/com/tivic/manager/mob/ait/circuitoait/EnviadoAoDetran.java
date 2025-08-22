package com.tivic.manager.mob.ait.circuitoait;

import com.tivic.manager.mob.AitMovimentoServices;

public class EnviadoAoDetran extends CircuitoAitItem {

	public EnviadoAoDetran() {
		super();
		this.setTpStatus(AitMovimentoServices.ENVIADO_AO_DETRAN);
		this.setItemCancelamento(AitMovimentoServices.CADASTRO_CANCELADO);
		this.addItensImpeditivos(AitMovimentoServices.REGISTRO_INFRACAO);
		this.addItensPossiveis(AitMovimentoServices.REGISTRO_INFRACAO);
	}
}
