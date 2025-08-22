package com.tivic.manager.mob.ait.circuitoait;

import com.tivic.manager.mob.AitMovimentoServices;

public class CancelarCadastro extends CircuitoAitItem {

	public CancelarCadastro() {
		this.setTpStatus(AitMovimentoServices.CADASTRO_CANCELADO);
		this.setItemExigido(new CircuitoAitItemPrazo(AitMovimentoServices.ENVIADO_AO_DETRAN));
	}
}