package com.tivic.manager.mob.ait.circuitoait;

import com.tivic.manager.mob.AitMovimentoServices;

public class AlteraPrazoDefesa extends CircuitoAitItem {
	
	 public AlteraPrazoDefesa() {
	        super();
	        this.setTpStatus(AitMovimentoServices.NOVO_PRAZO_DEFESA);
	        this.setItemExigido(new CircuitoAitItemPrazo(AitMovimentoServices.NAI_ENVIADO));
	        this.addItensImpeditivos(AitMovimentoServices.NIP_ENVIADA);
	        this.addItensPossiveis(AitMovimentoServices.AR_NAI);
	        this.addItensPossiveis(AitMovimentoServices.PUBLICACAO_NAI);
	        this.addItensPossiveis(AitMovimentoServices.NIP_ENVIADA);
	        this.addItensPossiveis(AitMovimentoServices.FIM_PRAZO_DEFESA);
	        this.addItensPossiveis(AitMovimentoServices.DEFESA_PREVIA);
	        this.addItensPossiveis(AitMovimentoServices.ADVERTENCIA_DEFESA_ENTRADA);
	        this.addItensPossiveis(AitMovimentoServices.APRESENTACAO_CONDUTOR);
	    }
}
