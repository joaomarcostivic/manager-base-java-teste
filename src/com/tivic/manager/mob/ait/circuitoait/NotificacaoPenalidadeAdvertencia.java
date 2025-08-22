package com.tivic.manager.mob.ait.circuitoait;

import com.tivic.manager.mob.AitMovimentoServices;

public class NotificacaoPenalidadeAdvertencia extends CircuitoAitItem {
	
	public NotificacaoPenalidadeAdvertencia() {
		this.setTpStatus(AitMovimentoServices.NOTIFICACAO_ADVERTENCIA);
		this.setItemExigido(new CircuitoAitItemPrazo(AitMovimentoServices.NAI_ENVIADO));
		this.addItensImpeditivos(AitMovimentoServices.NIP_ENVIADA);
		this.addItensPossiveis(AitMovimentoServices.NAI_ENVIADO);
		this.addItensPossiveis(AitMovimentoServices.RECURSO_JARI);
	}
}
