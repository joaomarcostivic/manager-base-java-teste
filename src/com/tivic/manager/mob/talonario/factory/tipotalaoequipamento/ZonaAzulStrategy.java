package com.tivic.manager.mob.talonario.factory.tipotalaoequipamento;

import com.tivic.manager.mob.talonario.enuns.TipoTalaoEnum;

public class ZonaAzulStrategy implements TipoTalaoEquipamentoStrategy {

	@Override
	public int getTpTalaoByEquipamento() {
		return TipoTalaoEnum.TP_TALONARIO_ZONA_AZUL.getKey();
	}
}
