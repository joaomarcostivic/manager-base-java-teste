package com.tivic.manager.mob.ait.circuitoait;

import com.tivic.manager.mob.AitMovimentoServices;

public class NaiEnviado extends CircuitoAitItem {

	public NaiEnviado() {
		super();
		this.setTpStatus(AitMovimentoServices.NAI_ENVIADO);
		this.setItemCancelamento(AitMovimentoServices.CANCELAMENTO_MULTA);
		this.setItemExigido(new CircuitoAitItemPrazo(AitMovimentoServices.REGISTRO_INFRACAO));
		this.addItensImpeditivos(AitMovimentoServices.FIM_PRAZO_DEFESA);
		this.addItensImpeditivos(AitMovimentoServices.NIP_ENVIADA);
		this.addItensPossiveis(AitMovimentoServices.AR_NAI);
		this.addItensPossiveis(AitMovimentoServices.PUBLICACAO_NAI);
		this.addItensPossiveis(AitMovimentoServices.NIP_ENVIADA);
		this.addItensPossiveis(AitMovimentoServices.FIM_PRAZO_DEFESA);
		this.addItensPossiveis(AitMovimentoServices.DEFESA_PREVIA);
		this.addItensPossiveis(AitMovimentoServices.ADVERTENCIA_DEFESA_ENTRADA);
		this.addItensPossiveis(AitMovimentoServices.APRESENTACAO_CONDUTOR);
		this.addItensPossiveis(AitMovimentoServices.NOVO_PRAZO_DEFESA);
		this.addItensPossiveis(AitMovimentoServices.NOVO_PRAZO_JARI);
	}
}
